package it.polimi.tiw.dao;

import it.polimi.tiw.authentication.security.PBKDF2WithHmacSHA512;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.beans.Worker;
import it.polimi.tiw.beans.validation.BeanValidator;
import it.polimi.tiw.beans.validation.InvalidBeanException;
import it.polimi.tiw.beans.validation.Validation;
import it.polimi.tiw.utils.beans.BeanFactory;
import it.polimi.tiw.utils.beans.UserBeanFactory;
import it.polimi.tiw.utils.beans.WorkerBeanFactory;
import it.polimi.tiw.utils.dao.DAO;
import it.polimi.tiw.utils.sql.ConnectionManager;
import it.polimi.tiw.utils.sql.PooledConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserDAO extends DAO
{
    public List<User> getAllUsers() throws SQLException
    {
        List<User> users = new ArrayList<>();

        try (PooledConnection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement query = connection.getConnection().prepareStatement("SELECT * FROM users");
             ResultSet result = query.executeQuery())
        {
            while (result.next()) users.add(BeanFactory.getBeanInstance(new UserBeanFactory(), result));
        }

        return users;
    }

    public boolean usernameExist(String username) throws SQLException
    {
        try(PooledConnection connection = ConnectionManager.getInstance().getConnection();
            PreparedStatement statement = connection.getConnection().prepareStatement("SELECT username FROM users WHERE username = ?"))
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
        try(PooledConnection connection = ConnectionManager.getInstance().getConnection();
            PreparedStatement statement = connection.getConnection().prepareStatement("SELECT email FROM users WHERE email = ?"))
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
        try (PooledConnection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement query = connection.getConnection().prepareStatement("SELECT * FROM users WHERE username = ?"))
        {
            query.setString(1, username);

            try(ResultSet result = query.executeQuery())
            {
                if(!result.next())return null;
                return BeanFactory.getBeanInstance(new UserBeanFactory(), result);
            }

        }
    }

    public Worker findWorkerById(int userId) throws SQLException
    {
        try(PooledConnection connection = ConnectionManager.getInstance().getConnection();
            PreparedStatement statement = connection.getConnection().prepareStatement("SELECT * FROM worker WHERE user_id = ?"))
        {
            statement.setInt(1, userId);

            try(ResultSet result = statement.executeQuery())
            {
                if(!result.next())return null;
                return BeanFactory.getBeanInstance(new WorkerBeanFactory(), result);
            }
        }
    }

    private int insertUser(User user) throws SQLException
    {
        try (PooledConnection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement query = connection.getConnection().prepareStatement("INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS))
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
        return insertUser(user);
    }

    public void insertWorker(User user, Worker worker)throws SQLException, InvalidBeanException
    {
        List<Validation> validations = BeanValidator.validate(user, worker);
        validations = validations.stream().filter(Validation::isInvalid).collect(Collectors.toList());
        if(!validations.isEmpty())throw new InvalidBeanException(validations);

        atomicTransaction(connection ->
        {
            int userId = insertUser(user);
            worker.setUserId(userId);

            try(PreparedStatement query = connection.prepareStatement("INSERT INTO worker (user_id, experience, photo) VALUES (?, ?, ?)"))
            {
                query.setInt(1, worker.getUserId());
                query.setObject(2, worker.getExpLvl(), Types.OTHER);
                query.setString(3, worker.getPhoto());

                int affectedRows = query.executeUpdate();
                if (affectedRows == 0) throw new SQLException("Worker creation failed!");
            }
        });
    }


}
