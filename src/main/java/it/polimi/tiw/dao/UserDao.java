package it.polimi.tiw.dao;

import it.polimi.tiw.authentication.security.PBKDF2WithHmacSHA512;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.beans.Worker;
import it.polimi.tiw.beans.validation.InvalidBeanException;
import it.polimi.tiw.utils.beans.BeanFactory;
import it.polimi.tiw.utils.beans.UserBeanFactory;
import it.polimi.tiw.utils.beans.WorkerBeanFactory;
import it.polimi.tiw.utils.dao.Dao;

import java.sql.*;
import java.util.List;
import java.util.Optional;

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

    public Optional<Worker> findWorkerByUserId(int userId) throws SQLException
    {
        return findFirstBy("worker", "user_id", userId, workerBeanFactory);
    }

    public boolean insertUser(User user, Worker worker) throws SQLException, InvalidBeanException
    {
        if(!user.isValid())throw new InvalidBeanException(user.getValidation().orElse(null));
        if(user.getRole().equals("WORKER") && (worker == null || !worker.isValid()))
            throw new InvalidBeanException(worker != null ? worker.getValidation().orElse(null) : null);

        return transaction(connection ->
        {
            int userId;
            try (PreparedStatement query = connection.prepareStatement("INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS))
            {
                query.setString(1, user.getUsername());
                query.setString(2, user.getEmail());
                query.setString(3, PBKDF2WithHmacSHA512.getInstance().hashPassword(user.getPassword()));
                query.setObject(4, user.getRole(), Types.OTHER);

                if(query.executeUpdate() == 0) rollback("User not inserted!");

                try (ResultSet generatedKeys = query.getGeneratedKeys())
                {
                    if (!generatedKeys.next()) rollback("User key not generated!");
                    userId = generatedKeys.getInt(1);
                }
            }

            if(!user.getRole().equals("WORKER"))return;
            if(userId < 1) rollback();

            try(PreparedStatement query = connection.prepareStatement("INSERT INTO worker (user_id, experience, photo) VALUES (?, ?, ?)"))
            {
                query.setInt(1, userId);
                query.setObject(2, worker.getExpLvl(), Types.OTHER);
                query.setString(3, worker.getPhoto());

                if(query.executeUpdate() == 0) rollback("Worker not inserted!");
            }
        });
    }

    public boolean insertUser(User user) throws SQLException, InvalidBeanException
    {
        return insertUser(user, null);
    }
}
