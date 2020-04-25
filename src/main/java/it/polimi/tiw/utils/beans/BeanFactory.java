package it.polimi.tiw.utils.beans;

import it.polimi.tiw.beans.Bean;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface BeanFactory<T extends Bean>
{
    T convert(ResultSet resultSet) throws SQLException;

    static <T extends Bean> T getBeanInstance(BeanFactory<T> factory, ResultSet resultSet) throws SQLException
    {
        return factory.convert(resultSet);
    }

    static <T extends Bean> List<T> getBeanList(BeanFactory<T> factory, ResultSet resultSet) throws SQLException
    {
        List<T> beanList = new ArrayList<>();
        while(resultSet.next())beanList.add(factory.convert(resultSet));
        return beanList;
    }

    default boolean hasColumn(ResultSet resultSet, String columnName) throws SQLException
    {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columns = metaData.getColumnCount();
        for (int i = 1; i <= columns; i++)
            if (columnName.equals(metaData.getColumnName(i))) return true;
        return false;
    }
}
