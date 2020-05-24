package it.polimi.tiw.dao;

import it.polimi.tiw.authentication.security.PBKDF2WithHmacSHA512;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.beans.UserWorker;
import it.polimi.tiw.beans.Worker;
import it.polimi.tiw.beans.validation.InvalidBeanException;
import it.polimi.tiw.beans.validation.validators.Image64Validator;
import it.polimi.tiw.utils.beans.BeanFactory;
import it.polimi.tiw.utils.beans.UserBeanFactory;
import it.polimi.tiw.utils.beans.WorkerBeanFactory;
import it.polimi.tiw.utils.dao.Dao;
import it.polimi.tiw.utils.sql.ConnectionManager;
import it.polimi.tiw.utils.sql.PooledConnection;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class UserDao extends Dao<User>
{
    private BeanFactory<Worker> workerBeanFactory;

    public UserDao()
    {
        super("users", new UserBeanFactory());
        workerBeanFactory = new WorkerBeanFactory();
    }

    public List<User> getAllUsers() throws SQLException
    {
        return getAll();
    }

    public boolean usernameExist(String username) throws SQLException
    {
        return exist("username", username);
    }

    public boolean emailExist(String email) throws SQLException
    {
        return exist("email", email);
    }

    public Optional<User> findUserByUsername(String username) throws SQLException
    {
        return findFirstBy("username", username);
    }

    public Optional<User> findUserById(int userId) throws SQLException
    {
        return findFirstBy("id", userId);
    }

    public Optional<Worker> findWorkerByUserId(int userId) throws SQLException
    {
        return findFirstBy("worker", "user_id", userId, workerBeanFactory);
    }

    public Optional<UserWorker> findWorkerById(int userId) throws SQLException
    {
        try(PooledConnection connection = ConnectionManager.getInstance().getConnection();
            PreparedStatement statement = connection.getConnection().prepareStatement("SELECT * FROM users U, worker W WHERE U.id = W.user_id and U.id = ?"))
        {
            statement.setInt(1, userId);
            try(ResultSet resultSet = statement.executeQuery())
            {
                if(!resultSet.next())return Optional.empty();
                User user = BeanFactory.getBeanInstance(beanFactory, resultSet);
                Worker worker = BeanFactory.getBeanInstance(workerBeanFactory, resultSet);
                return Optional.of(new UserWorker(user, worker));
            }
        }
    }

    public boolean insertUser(User user, Worker worker) throws SQLException, InvalidBeanException
    {
        if(!user.isValid())throw new InvalidBeanException(user.getValidation().orElse(null));
        if(user.getRole().equals("WORKER") && (worker == null || !worker.isValid()))
            throw new InvalidBeanException(worker != null ? worker.getValidation().orElse(null) : null);

        return transaction(connection ->
        {
            int userId;
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS))
            {
                statement.setString(1, user.getUsername());
                statement.setString(2, user.getEmail());
                statement.setString(3, PBKDF2WithHmacSHA512.getInstance().hashPassword(user.getPassword()));
                statement.setObject(4, user.getRole(), Types.OTHER);

                if(statement.executeUpdate() == 0) rollback("User not inserted!");

                try (ResultSet generatedKeys = statement.getGeneratedKeys())
                {
                    if (!generatedKeys.next()) rollback("User key not generated!");
                    userId = generatedKeys.getInt(1);
                    user.setId(userId);
                    worker.setUserId(userId);
                }
            }

            if(!user.getRole().equals("WORKER"))return;
            if(userId < 1) rollback();

            try(PreparedStatement statement = connection.prepareStatement("INSERT INTO worker (user_id, experience, photo) VALUES (?, ?, ?)"))
            {
                statement.setInt(1, userId);
                statement.setObject(2, worker.getExpLvl(), Types.OTHER);
                statement.setString(3, worker.getPhoto());

                if(statement.executeUpdate() == 0) rollback("Worker not inserted!");
            }
        });
    }

    public boolean insertUser(User user) throws SQLException, InvalidBeanException
    {
        return insertUser(user, null);
    }

    public boolean updateUser(User user, Worker worker) throws SQLException, InvalidBeanException
    {
        AtomicBoolean changePassword = new AtomicBoolean(true);
        if(user.getPassword().trim().isEmpty())
        {
            changePassword.set(false);
            user.setPassword("******");
        }

        if(!user.isValid())throw new InvalidBeanException(user.getValidation().orElse(null));
        if(user.getRole().equals("WORKER") && (worker == null || !worker.isValid()))
            throw new InvalidBeanException(worker != null ? worker.getValidation().orElse(null) : null);

        return transaction(connection ->
        {
            String sql = changePassword.get() ? "UPDATE users SET email = ?, password = ? WHERE id = ?" : "UPDATE users SET email = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql))
            {
                statement.setString(1, user.getEmail());
                if(changePassword.get())statement.setString(2, PBKDF2WithHmacSHA512.getInstance().hashPassword(user.getPassword()));
                statement.setInt(changePassword.get() ? 3 : 2, user.getId());

                if(statement.executeUpdate() == 0) rollback("User not updated!");
            }

            if(!user.getRole().equals("WORKER") || worker == null)return;

            try(PreparedStatement statement = connection.prepareStatement("UPDATE worker SET experience = ?::gml_enum WHERE user_id = ?"))
            {
                statement.setString(1, worker.getExpLvl());
                statement.setInt(2, user.getId());

                if(statement.executeUpdate() == 0) rollback("User not updated!");
            }
        });
    }

    public boolean updateUserPhoto(int userId, String photo) throws SQLException
    {
        if(!Image64Validator.pattern.matcher(photo).matches()) return false;

        try (PooledConnection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement statement = connection.getConnection().prepareStatement("UPDATE worker SET photo = ? where user_id = ?"))
        {
            statement.setString(1, photo);
            statement.setInt(2, userId);

            return statement.executeUpdate() == 1;
        }
    }

}
