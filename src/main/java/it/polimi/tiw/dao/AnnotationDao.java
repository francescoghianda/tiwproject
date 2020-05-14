package it.polimi.tiw.dao;

import it.polimi.tiw.beans.Annotation;
import it.polimi.tiw.beans.Bean;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.beans.Worker;
import it.polimi.tiw.beans.validation.InvalidBeanException;
import it.polimi.tiw.utils.beans.*;
import it.polimi.tiw.utils.dao.Dao;
import it.polimi.tiw.utils.sql.ConnectionManager;
import it.polimi.tiw.utils.sql.PooledConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnnotationDao extends Dao<Annotation>
{
    private UserBeanFactory userBeanFactory;
    private WorkerBeanFactory workerBeanFactory;

    public AnnotationDao()
    {
        super("annotation", new AnnotationBeanFactory());

        userBeanFactory = new UserBeanFactory();
        workerBeanFactory = new WorkerBeanFactory();
    }

    public List<Annotation> findAnnotationByImageId(int imageId) throws SQLException
    {
        return findBy("image_id", imageId);
    }

    public List<JoinedBean<Annotation, User, Worker>> findAnnotationAndUserByImageId(int imageId) throws SQLException
    {
        try(PooledConnection connection = ConnectionManager.getInstance().getConnection();
            PreparedStatement statement = connection.getConnection().prepareStatement("SELECT * FROM annotation A, users U, worker W WHERE A.user_id = U.id and U.id = W.user_id and A.image_id = ?"))
        {
            statement.setInt(1, imageId);
            try(ResultSet resultSet = statement.executeQuery())
            {
                List<JoinedBean<Annotation, User, Worker>> joinedBeans = new ArrayList<>();
                while (resultSet.next())
                {
                    Annotation annotation = BeanFactory.getBeanInstance(beanFactory, resultSet);
                    User user = BeanFactory.getBeanInstance(userBeanFactory, resultSet);
                    Worker worker = BeanFactory.getBeanInstance(workerBeanFactory, resultSet);
                    joinedBeans.add(new JoinedBean<>(annotation, user, worker));
                }
                return joinedBeans;
            }
        }
    }

    public List<Annotation> findAnnotationByUserId(int userId) throws SQLException
    {
        return findBy("user_id", userId);
    }

    public boolean insertAnnotation(Annotation annotation) throws SQLException, InvalidBeanException
    {
        if(!annotation.isValid()) throw new InvalidBeanException(annotation.getValidation().orElseGet(null));
        try(PooledConnection connection = ConnectionManager.getInstance().getConnection();
            PreparedStatement statement = connection.getConnection().prepareStatement("INSERT INTO annotation (user_id, image_id, date, valid, trust, notes) values (?, ?, ?, ?, ?::gml_enum, ?) ON CONFLICT DO NOTHING"))
        {
            statement.setInt(1, annotation.getUserId());
            statement.setInt(2, annotation.getImageId());
            statement.setDate(3, annotation.getDate());
            statement.setBoolean(4, annotation.getValid());
            statement.setString(5, annotation.getTrust());
            statement.setString(6, annotation.getNotes());

            return statement.executeUpdate() == 1;
        }
    }
}
