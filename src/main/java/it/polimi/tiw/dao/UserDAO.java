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

        try (PreparedStatement query = connection.prepareStatement("SELECT * FROM users");
             ResultSet result = query.executeQuery())
        {
            while (result.next()) users.add(createUser(result));
        }

        return users;
    }

    public User findUserByUsername(String username) throws SQLException
    {
        try (PreparedStatement query = connection.prepareStatement("SELECT * FROM users WHERE username = ?"))
        {
            query.setString(1, username);

            try(ResultSet result = query.executeQuery())
            {
                if(!result.next())return null;
                return createUser(result);
            }

        }
    }

    public void insertUser(String username, String email, String password, User.Role role) throws SQLException
    {
        try (PreparedStatement query = connection.prepareStatement("INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)"))
        {
            query.setString(1, username);
            query.setString(2, email);
            query.setString(3, password);
            query.setString(4, role.toString());

            query.executeUpdate();
        }
    }

    public void insertUser(User user) throws SQLException
    {
        insertUser(user.getUsername(), user.getEmail(), user.getPassword(), user.getRole());
    }

    private User createUser(ResultSet result) throws SQLException
    {
        User user = new User();
        user.setUsername(result.getString("username"));
        user.setEmail(result.getString("email"));
        user.setPassword(result.getString("password"));
        user.setRole(User.Role.valueOf(result.getString("role")));
        return user;
    }
}
