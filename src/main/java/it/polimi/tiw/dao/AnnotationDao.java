package it.polimi.tiw.dao;

import it.polimi.tiw.beans.Annotation;
import it.polimi.tiw.beans.validation.InvalidBeanException;
import it.polimi.tiw.utils.beans.AnnotationBeanFactory;
import it.polimi.tiw.utils.dao.Dao;
import it.polimi.tiw.utils.sql.ConnectionManager;
import it.polimi.tiw.utils.sql.PooledConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AnnotationDao extends Dao<Annotation>
{
    public AnnotationDao()
    {
        super("annotation", new AnnotationBeanFactory());
    }

    public List<Annotation> findAnnotationByImageId(int imageId) throws SQLException
    {
        return findBy("image_id", imageId);
    }

    public List<Annotation> findAnnotationByUserId(int userId) throws SQLException
    {
        return findBy("user_id", userId);
    }

    public boolean insertAnnotation(Annotation annotation) throws SQLException
    {
        try(PooledConnection connection = ConnectionManager.getInstance().getConnection();
            PreparedStatement statement = connection.getConnection().prepareStatement("INSERT INTO annotation (user_id, image_id, date, valid, trust, notes) values (?, ?, ?, ?, ?::gml_enum, ?)"))
        {
            statement.setInt(1, annotation.getUserId());
            statement.setInt(2, annotation.getImageId());
            statement.setDate(3, annotation.getDate());
            statement.setBoolean(4, annotation.getValid());
            statement.setString(5, annotation.getTrust());
            statement.setString(6, annotation.getNotes());

            return statement.executeUpdate() != 0;
        }
    }
}
