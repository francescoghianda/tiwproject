package it.polimi.tiw.utils.sql;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;

public class PooledConnection implements Closeable, Comparable<PooledConnection>
{
    private final Connection connection;
    private boolean inUse;
    private long lastUsedTimeMillis;
    private ConnectionPool pool;

    PooledConnection(ConnectionPool pool, Connection connection)
    {
        this.connection = connection;
        this.inUse = false;
        this.lastUsedTimeMillis = System.currentTimeMillis();
        this.pool = pool;
    }

    void setInUse(boolean inUse)
    {
        if(!inUse)lastUsedTimeMillis = System.currentTimeMillis();
        this.inUse = inUse;
    }

    long getLastUsedTimeMillis()
    {
        return lastUsedTimeMillis;
    }

    long getIdleTime()
    {
        return System.currentTimeMillis() - lastUsedTimeMillis;
    }

    boolean isInUse()
    {
        return inUse;
    }

    boolean isFree()
    {
        return !inUse;
    }

    public Connection getConnection()
    {
        return connection;
    }

    @Override
    public int compareTo(PooledConnection pooledConnection)
    {
        long currentTime = System.currentTimeMillis();
        long thisTime = currentTime - lastUsedTimeMillis;
        long otherTime = currentTime - pooledConnection.lastUsedTimeMillis;
        return Long.compare(otherTime, thisTime); //La connessione che non stata usata da più tempo ha la precedenza
    }

    @Override
    public void close()
    {
        pool.freeConnection(this);
    }
}
