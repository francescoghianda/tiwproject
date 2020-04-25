package it.polimi.tiw.utils.beans;

import it.polimi.tiw.beans.Worker;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkerBeanFactory implements BeanFactory<Worker>
{

    @Override
    public Worker convert(ResultSet resultSet) throws SQLException
    {
        Worker worker = new Worker();
        worker.setUserId(resultSet.getInt("user_id"));
        worker.setExpLvl(resultSet.getString("experience"));
        worker.setPhoto(resultSet.getString("photo"));
        return worker;
    }
}
