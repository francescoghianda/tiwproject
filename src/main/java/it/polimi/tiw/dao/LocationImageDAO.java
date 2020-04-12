package it.polimi.tiw.dao;

import it.polimi.tiw.beans.LocationImage;
import it.polimi.tiw.beans.validation.InvalidBeanException;
import it.polimi.tiw.utils.beans.BeanFactory;
import it.polimi.tiw.utils.beans.LocationImageBeanFactory;
import it.polimi.tiw.utils.dao.DAO;
import it.polimi.tiw.utils.sql.ConnectionManager;
import it.polimi.tiw.utils.sql.PooledConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class LocationImageDAO extends DAO
{

    public List<LocationImage> findImageOfCampaign(int campaignId) throws SQLException
    {
        try(PooledConnection connection = ConnectionManager.getInstance().getConnection();
            PreparedStatement statement = connection.getConnection().prepareStatement("SELECT img.*, ST_AsText(img.location) location_coordinates from location_image img where campaign_id = ?"))
        {
            statement.setInt(1, campaignId);
            try(ResultSet resultSet = statement.executeQuery())
            {
                return BeanFactory.getBeanList(new LocationImageBeanFactory(), resultSet);
            }
        }
    }

    public boolean insertImages(LocationImage... locationImages) throws SQLException, InvalidBeanException
    {
        AtomicBoolean validBeans = new AtomicBoolean(true);

        boolean committed = atomicTransaction(connection ->
        {
            try(PreparedStatement statement = connection.prepareStatement("INSERT INTO location_image (campaign_id, location, municipality, region, source, date, resolution) values (?, ?::geography, ?, ?, ?, ?, ?::gml_enum)", Statement.RETURN_GENERATED_KEYS))
            {
                for(LocationImage image : locationImages)
                {
                    if(!image.isValid())
                    {
                        validBeans.set(false);
                        rollback();
                    }

                    statement.setInt(1, image.getCampaignId());
                    statement.setString(2, image.getLocation().toString());
                    statement.setString(3, image.getMunicipality());
                    statement.setString(4, image.getRegion());
                    statement.setString(5, image.getSource());
                    statement.setDate(6, image.getDate());
                    statement.setString(7, image.getResolution());

                    statement.addBatch();
                }
                int[] updateCounts = statement.executeBatch();
                for(int count : updateCounts)if(count == Statement.EXECUTE_FAILED)rollback();
            }
        });

        if(!committed && !validBeans.get())throw new InvalidBeanException();

        return committed;
    }

}
