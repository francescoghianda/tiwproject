package it.polimi.tiw.utils.beans;

import it.polimi.tiw.beans.Annotation;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AnnotationBeanFactory implements BeanFactory<Annotation>
{

    @Override
    public Annotation convert(ResultSet resultSet) throws SQLException
    {
        Annotation annotation = new Annotation();
        annotation.setId(resultSet.getInt("id"));
        annotation.setUserId(resultSet.getInt("user_id"));
        annotation.setImageId(resultSet.getInt("image_id"));
        annotation.setDate(resultSet.getDate("date"));
        annotation.setValid(resultSet.getBoolean("valid"));
        annotation.setTrust(resultSet.getString("trust"));
        annotation.setNotes(resultSet.getString("notes"));
        return annotation;
    }
}
