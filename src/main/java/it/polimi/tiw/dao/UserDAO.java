package it.polimi.tiw.dao;

import it.polimi.tiw.beans.User;
import it.polimi.tiw.beans.Worker;
import it.polimi.tiw.beans.validation.InvalidBeanException;
import it.polimi.tiw.utils.dao.AtomicTransaction;

import java.sql.*;
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

    private int insertUser(Connection connection, User user) throws SQLException
    {
        try (PreparedStatement query = connection.prepareStatement("INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS))
        {
            query.setString(1, user.getUsername());
            query.setString(2, user.getEmail());
            query.setString(3, user.getPassword());
            query.setObject(4, user.getRole(), Types.OTHER);

            int affectedRows = query.executeUpdate();
            if (affectedRows == 0) throw new SQLException("User creation failed!");

            try (ResultSet generatedKeys = query.getGeneratedKeys())
            {
                if (generatedKeys.next()) return generatedKeys.getInt(1);
                else throw new SQLException("User creation failed!");
            }
        }
    }


    public int insertManager(User user) throws SQLException, InvalidBeanException
    {
        if(!user.isValid())throw new InvalidBeanException();
        return insertUser(connection, user);
    }

    public void insertWorker(User user, Worker worker)throws SQLException, InvalidBeanException
    {
        if(!worker.isValid() || !user.isValid())throw new InvalidBeanException();

        AtomicTransaction transaction = new AtomicTransaction(connection);
        transaction.execute(con ->
        {
            int userId = insertUser(con, user);
            worker.setUserId(userId);

            try(PreparedStatement query = con.prepareStatement("INSERT INTO worker (user_id, experience, photo) VALUES (?, ?, ?)"))
            {
                query.setInt(1, worker.getUserId());
                query.setObject(2, worker.getExpLvl(), Types.OTHER);
                query.setString(3, worker.getPhoto());

                int affectedRows = query.executeUpdate();
                if (affectedRows == 0) throw new SQLException("Worker creation failed!");
            }
        });
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
