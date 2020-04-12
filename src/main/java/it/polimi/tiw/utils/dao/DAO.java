package it.polimi.tiw.utils.dao;

import it.polimi.tiw.utils.sql.ConnectionManager;
import it.polimi.tiw.utils.sql.PooledConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class DAO
{
    public DAO()
    {

    }


    protected final void rollback() throws RollbackException
    {
        throw new RollbackException();
    }

    protected final boolean atomicTransaction(TransactionConsumer<Connection> transaction) throws SQLException
    {
        Connection connection = null;
        try(PooledConnection pooledConnection = ConnectionManager.getInstance().getConnection())
        {
            connection = pooledConnection.getConnection();
            connection.setAutoCommit(false);
            transaction.accept(connection);
            connection.commit();
            connection.setAutoCommit(true);
            return true;
        }
        catch (RollbackException e)
        {
            try
            {
                connection.rollback();
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }
            return false;
        }
        catch (SQLException e)
        {
            try
            {
                if(connection != null)connection.rollback();
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }
            throw e;
        }
    }
}
