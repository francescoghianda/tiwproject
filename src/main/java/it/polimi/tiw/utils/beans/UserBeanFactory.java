package it.polimi.tiw.utils.beans;

import it.polimi.tiw.beans.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserBeanFactory implements BeanFactory<User>
{
    @Override
    public User convert(ResultSet resultSet) throws SQLException
    {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setUsername(resultSet.getString("username"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setRole(resultSet.getString("role"));
        return user;
    }
}
