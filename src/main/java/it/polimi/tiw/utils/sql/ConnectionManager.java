package it.polimi.tiw.utils.sql;

import it.polimi.tiw.utils.logger.LogType;
import it.polimi.tiw.utils.logger.Logger;

import javax.servlet.ServletContext;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

public class ConnectionManager
{
    public static final String DEFAULT_POOL_NAME = "default";
    private static ConnectionManager instance;

    private final String databaseURL;
    private final Properties connectionProperties;

    private final HashMap<String, ConnectionPool> connectionPoolMap;

    private Thread poolMonitor;
    private boolean monitorRunning;
    private int monitorPauseMinutes = 10;

    private ConnectionManager(ServletContext servletContext) throws ClassNotFoundException
    {
        String driver = servletContext.getInitParameter("dbDriver");
        databaseURL = servletContext.getInitParameter("dbUrl");
        String user = servletContext.getInitParameter("dbUser");
        String password = servletContext.getInitParameter("dbPassword");

        connectionProperties = new Properties();
        connectionProperties.setProperty("user", user);
        connectionProperties.setProperty("password", password);

        Class.forName(driver);

        connectionPoolMap = new HashMap<>();
        connectionPoolMap.put(DEFAULT_POOL_NAME, new ConnectionPool(DEFAULT_POOL_NAME, this));

        startMonitor();
    }

    public static ConnectionManager newInstance(ServletContext servletContext) throws ClassNotFoundException
    {
        if(instance != null)throw new IllegalStateException("Connection Manager is already initialized!");
        instance = new ConnectionManager(servletContext);
        return instance;
    }

    public static ConnectionManager getInstance()
    {
        if(instance == null)throw new IllegalStateException("Connection Manager is not initialized!");
        return instance;
    }

    public void createNewPoolConnection(String poolName)
    {
        if(connectionPoolMap.containsKey(poolName))throw new IllegalArgumentException("Connection pool named "+poolName+" already exist!");
        connectionPoolMap.put(poolName, new ConnectionPool(poolName, this));
    }

    public PooledConnection getConnection(String poolName) throws SQLException
    {
        ConnectionPool pool = connectionPoolMap.get(poolName);
        if(pool == null)throw new IllegalArgumentException("Connection Pool named "+poolName+" does not exist!");
        return pool.getConnection();
    }

    public PooledConnection getConnection() throws SQLException
    {
        return getConnection(DEFAULT_POOL_NAME);
    }

    public boolean freeConnection(String poolName, PooledConnection connection)
    {
        ConnectionPool pool = connectionPoolMap.get(poolName);
        if(pool == null)throw new IllegalArgumentException("Connection Pool named "+poolName+" does not exist!");
        return pool.freeConnection(connection);
    }

    public boolean freeConnection(PooledConnection connection)
    {
        return freeConnection(DEFAULT_POOL_NAME, connection);
    }

    public String getDatabaseURL()
    {
        return databaseURL;
    }

    public Properties getConnectionProperties()
    {
        return connectionProperties;
    }

    private void stopMonitor()
    {
        monitorRunning = false;
        poolMonitor.interrupt();
    }

    private void startMonitor()
    {
        if(monitorRunning)return;
        poolMonitor = new Thread(() ->{
            monitorRunning = true;
            Logger.out.log("Connection Manager monitor has been started.");
            while (monitorRunning)
            {
                connectionPoolMap.values().forEach(ConnectionPool::checkConnections);
                try
                {
                    Thread.sleep(monitorPauseMinutes*60*1000L);
                }
                catch (InterruptedException e)
                {
                    Logger.out.log("Connection Manager monitor interrupted!", LogType.WARNING);
                }
            }
            Logger.out.log("Connection Manager monitor has stopped.");
        });
        poolMonitor.setDaemon(true);
        poolMonitor.start();
    }

    public void stop()
    {
        stopMonitor();
        connectionPoolMap.values().forEach(connectionPool -> connectionPool.close());
    }
}
