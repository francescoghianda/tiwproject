package it.polimi.tiw.utils.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class AtomicTransaction
{
    private Connection connection;

    public AtomicTransaction(Connection connection)
    {
        this.connection = connection;
    }

    public void execute(TransactionConsumer<Connection> transaction) throws SQLException
    {
        try
        {
            connection.setAutoCommit(false);
            transaction.accept(connection);
            connection.commit();
            connection.setAutoCommit(true);
        }
        catch (SQLException e)
        {
            try
            {
                connection.rollback();
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }
            throw e;
        }
    }


}
