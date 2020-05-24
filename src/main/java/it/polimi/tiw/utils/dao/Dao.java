package it.polimi.tiw.utils.dao;

import it.polimi.tiw.beans.Bean;
import it.polimi.tiw.utils.beans.BeanFactory;
import it.polimi.tiw.utils.logger.LogType;
import it.polimi.tiw.utils.logger.Logger;
import it.polimi.tiw.utils.sql.ConnectionManager;
import it.polimi.tiw.utils.sql.PooledConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Dao<T extends Bean>
{
    protected final BeanFactory<T> beanFactory;
    private final String tableName;
    private final List<SelectOrder> selectOrderList;
    private String orderString;
    private boolean computeOrderString;

    public Dao(String tableName, BeanFactory<T> beanFactory)
    {
        this.tableName = tableName;
        this.beanFactory = beanFactory;
        selectOrderList = new ArrayList<>();
    }

    public void addSelectOrder(SelectOrder selectOrder)
    {
        selectOrderList.add(selectOrder);
        computeOrderString = true;
    }

    public void removeSelectOrder(SelectOrder selectOrder)
    {
        selectOrderList.remove(selectOrder);
        computeOrderString = true;
    }

    public void clearSelectOrderList()
    {
        selectOrderList.clear();
        computeOrderString = true;
    }

    private String getOrderString()
    {
        if(orderString == null || computeOrderString)
            orderString = selectOrderList.isEmpty() ? "" : " ORDER BY " + selectOrderList.stream().map(SelectOrder::toString).collect(Collectors.joining(", "));
        computeOrderString = false;
        return orderString;
    }

    protected  <B extends Bean> List<B> getAll(String tableName, BeanFactory<B> beanFactory) throws SQLException
    {
        try (PooledConnection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement query = connection.getConnection().prepareStatement("SELECT * FROM "+tableName+getOrderString());
             ResultSet result = query.executeQuery())
        {
            return BeanFactory.getBeanList(beanFactory, result);
        }
    }

    protected List<T> getAll() throws SQLException
    {
        return getAll(tableName, beanFactory);
    }

    protected  <B extends Bean> List<B> rawGet(BeanFactory<B> beanFactory, String sql, Object... parameters) throws SQLException
    {
        try(PooledConnection connection = ConnectionManager.getInstance().getConnection();
            PreparedStatement statement = connection.getConnection().prepareStatement(sql))
        {
            for(int i = 0; i < parameters.length; i++)statement.setObject(i+1, parameters[i]);
            try(ResultSet resultSet = statement.executeQuery())
            {
                return BeanFactory.getBeanList(beanFactory, resultSet);
            }
        }
    }

    protected List<T> rawGet(String sql, Object... parameters) throws SQLException
    {
        return rawGet(beanFactory, sql, parameters);
    }

    protected  <B extends Bean> List<B> findBy(String tableName, String columnName, Object value, BeanFactory<B> beanFactory) throws SQLException
    {
        try(PooledConnection connection = ConnectionManager.getInstance().getConnection();
            PreparedStatement statement = connection.getConnection().prepareStatement("SELECT * from "+tableName+" where "+columnName+" = ?"+getOrderString()))
        {
            statement.setObject(1, value);
            try(ResultSet resultSet = statement.executeQuery())
            {
                return BeanFactory.getBeanList(beanFactory, resultSet);
            }
        }
    }

    protected List<T> findBy(String columnName, Object value) throws SQLException
    {
        return findBy(tableName, columnName, value, beanFactory);
    }

    protected <B extends Bean> Optional<B> findFirstBy(String tableName, String columnName, Object value, BeanFactory<B> beanFactory) throws SQLException
    {
        return findBy(tableName, columnName, value, beanFactory).stream().findFirst();
    }

    protected Optional<T> findFirstBy(String columnName, Object value) throws SQLException
    {
        return findBy(tableName, columnName, value, beanFactory).stream().findFirst();
    }

    protected boolean exist(String tableName, String columnName, Object value) throws SQLException
    {
        try(PooledConnection connection = ConnectionManager.getInstance().getConnection();
            PreparedStatement statement = connection.getConnection().prepareStatement("SELECT true FROM "+tableName+" WHERE "+columnName+" = ?"))
        {
            statement.setObject(1, value);
            try(ResultSet result = statement.executeQuery())
            {
                return result.next();
            }
        }
    }

    protected boolean exist(String columnName, Object value) throws SQLException
    {
        return exist(tableName, columnName, value);
    }

    protected final void rollback() throws RollbackException
    {
        throw new RollbackException();
    }

    protected final void rollback(String errorMessage) throws RollbackException
    {
        Logger.out.log(errorMessage, LogType.ERROR);
        throw new RollbackException();
    }

    protected final boolean transaction(TransactionConsumer transaction) throws SQLException
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
