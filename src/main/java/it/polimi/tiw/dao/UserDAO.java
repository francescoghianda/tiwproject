package it.polimi.tiw.dao;

import it.polimi.tiw.beans.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO
{
    private final Connection connection;

    public UserDAO(Connection connection)
    {
        this.connection = connection;
    }

    public List<User> getAllUsers() throws SQLException
    {
        List<User> users = new ArrayList<>();

        try (PreparedStatement allUserQuery = connection.prepareStatement("SELECT * FROM users");
             ResultSet result = allUserQuery.executeQuery())
        {

            while (result.next())
            {
                User user = new User();
                user.setUsername(result.getString("username"));
                user.setEmail(result.getString("email"));
                user.setPassword(result.getString("password"));
                user.setRole(result.getString("role"));
                users.add(user);
            }
        }

        return users;
    }
}
