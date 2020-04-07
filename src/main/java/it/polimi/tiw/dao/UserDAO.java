package it.polimi.tiw.dao;

import it.polimi.tiw.authentication.security.PBKDF2WithHmacSHA512;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.beans.Worker;
import it.polimi.tiw.beans.validation.BeanValidator;
import it.polimi.tiw.beans.validation.InvalidBeanException;
import it.polimi.tiw.beans.validation.Validation;
import it.polimi.tiw.utils.dao.AtomicTransaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public boolean usernameExist(String username) throws SQLException
    {
        try(PreparedStatement statement = connection.prepareStatement("SELECT username FROM users WHERE username = ?"))
        {
            statement.setString(1, username);
            try(ResultSet result = statement.executeQuery())
            {
                return result.next();
            }
        }
    }

    public boolean emailExist(String email) throws SQLException
    {
        try(PreparedStatement statement = connection.prepareStatement("SELECT email FROM users WHERE email = ?"))
        {
            statement.setString(1, email);
            try(ResultSet result = statement.executeQuery())
            {
                return result.next();
            }
        }
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
            query.setString(3, PBKDF2WithHmacSHA512.getInstance().hashPassword(user.getPassword()));
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
        Validation validation = BeanValidator.validate(user);
        if(!validation.isValid())throw new InvalidBeanException(validation);
        return insertUser(connection, user);
    }

    public void insertWorker(User user, Worker worker)throws SQLException, InvalidBeanException
    {
        List<Validation> validations = BeanValidator.validate(user, worker);
        validations = validations.stream().filter(Validation::isInvalid).collect(Collectors.toList());
        if(!validations.isEmpty())throw new InvalidBeanException(validations);

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
        user.setId(result.getInt("id"));
        user.setUsername(result.getString("username"));
        user.setEmail(result.getString("email"));
        user.setPassword(result.getString("password"));
        user.setRole(result.getString("role"));
        return user;
    }


}
