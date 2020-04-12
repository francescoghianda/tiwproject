package it.polimi.tiw.utils.beans;

import it.polimi.tiw.beans.Bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BeanFactory<T extends Bean>
{
    public BeanFactory() {}

    public abstract T convert(ResultSet resultSet) throws SQLException;

    public static <T extends Bean> T getBeanInstance(BeanFactory<T> factory, ResultSet resultSet) throws SQLException
    {
        return factory.convert(resultSet);
    }

    public static <T extends Bean> List<T> getBeanList(BeanFactory<T> factory, ResultSet resultSet) throws SQLException
    {
        List<T> beanList = new ArrayList<>();
        while(resultSet.next())beanList.add(factory.convert(resultSet));
        return beanList;
    }
}
