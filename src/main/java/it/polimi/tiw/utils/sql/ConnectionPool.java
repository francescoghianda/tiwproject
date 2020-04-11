package it.polimi.tiw.utils.sql;

import it.polimi.tiw.utils.logger.LogType;
import it.polimi.tiw.utils.logger.Logger;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionPool
{
    private static final long MAX_IDLE = 60*10*1000L;
    private static final int VALID_TIMEOUT_SECONDS = 5;

    private final String name;
    private final PriorityBlockingQueue<PooledConnection> connectionQueue;
    private final List<PooledConnection> inUseConnections;
    private final ConnectionManager connectionManager;

    public ConnectionPool(String name, ConnectionManager connectionManager)
    {
        this.name = name;
        this.connectionQueue = new PriorityBlockingQueue<>();
        this.inUseConnections = Collections.synchronizedList(new ArrayList<>());
        this.connectionManager = connectionManager;
    }

    public synchronized PooledConnection getConnection() throws SQLException
    {
        PooledConnection connection = connectionQueue.poll();

        boolean createNewConnection = false;

        try
        {
            if(connection == null || connection.getConnection().isClosed() || connection.getIdleTime() > MAX_IDLE && !connection.getConnection().isValid(VALID_TIMEOUT_SECONDS))
            {
                createNewConnection = true;
                Logger.out.log("A new connection in pool "+name+" has been created.");
                if(connection != null)connection.getConnection().close();
            }
        }
        catch (SQLException e){
            Logger.out.log("Error closing old connection from pool "+name, LogType.ERROR);
            System.err.println();
        }
        finally
        {
            if(createNewConnection) connection = new PooledConnection(this, DriverManager.getConnection(connectionManager.getDatabaseURL(), connectionManager.getConnectionProperties()));
        }

        connection.setInUse(true);
        inUseConnections.add(connection);
        return connection;
    }

    public synchronized boolean freeConnection(PooledConnection connection)
    {
        if(!inUseConnections.contains(connection))return false;
        inUseConnections.remove(connection);
        connection.setInUse(false);
        if(!connectionQueue.offer(connection))
        {
            try{
                connection.getConnection().close();
            } catch (SQLException e) {
                Logger.out.log("Error closing old connection from pool "+name, LogType.ERROR);
            }
        }
        return true;
    }

    public synchronized void checkConnections()
    {
        final AtomicInteger removedConnection = new AtomicInteger();
        Iterator<PooledConnection> iterator = connectionQueue.iterator();
        iterator.forEachRemaining(connection ->
        {
            try
            {
                if(!connection.getConnection().isValid(VALID_TIMEOUT_SECONDS))
                {
                    removedConnection.incrementAndGet();
                    iterator.remove();
                    connection.getConnection().close();
                }
            } catch (SQLException e) {
                Logger.out.log("Error closing old connection from pool "+name, LogType.ERROR);
            }
        });
        Logger.out.log("Removed "+removedConnection.get()+" connections from pool "+name);
    }

}
