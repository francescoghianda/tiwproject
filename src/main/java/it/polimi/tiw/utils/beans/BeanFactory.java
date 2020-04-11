package it.polimi.tiw.utils.beans;

import it.polimi.tiw.beans.Bean;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BeanFactory<T extends Bean>
{
    public BeanFactory() {}

    public abstract T convert(ResultSet resultSet) throws SQLException;

    public static <T extends Bean> T getBeanInstance(BeanFactory<T> factory, ResultSet resultSet) throws SQLException
    {
        return factory.convert(resultSet);
    }
}
