package it.polimi.tiw.utils.dao;

import it.polimi.tiw.utils.sql.ConnectionManager;
import it.polimi.tiw.utils.sql.PooledConnection;
import javax.servlet.UnavailableException;
import java.sql.Connection;
import java.sql.SQLException;

public class DAO
{
    private final Object lock;
    private boolean rollback;

    public DAO() throws UnavailableException
    {
        lock = new Object();
    }

    protected void rollback()
    {
        synchronized (lock)
        {
            rollback = true;
        }
    }

    protected final void transaction(TransactionConsumer<Connection> transaction) throws SQLException
    {
        try(PooledConnection pooledConnection = ConnectionManager.getInstance().getConnection())
        {
            transaction.accept(pooledConnection.getConnection());
        }
    }

    protected final boolean atomicTransaction(TransactionConsumer<Connection> transaction) throws SQLException
    {
        synchronized (lock)
        {
            Connection connection = null;
            try(PooledConnection pooledConnection = ConnectionManager.getInstance().getConnection())
            {
                connection = pooledConnection.getConnection();
                rollback = false;
                connection.setAutoCommit(false);
                transaction.accept(connection);

                if(rollback) connection.rollback();
                else connection.commit();

                connection.setAutoCommit(true);
                return !rollback;
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
}
