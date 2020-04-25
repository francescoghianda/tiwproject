package it.polimi.tiw.dao;

import it.polimi.tiw.beans.LocationImage;
import it.polimi.tiw.beans.validation.InvalidBeanException;
import it.polimi.tiw.utils.beans.JoinedBean;
import it.polimi.tiw.utils.beans.LocationImageBeanFactory;
import it.polimi.tiw.utils.dao.Dao;
import it.polimi.tiw.utils.sql.ConnectionManager;
import it.polimi.tiw.utils.sql.PooledConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class LocationImageDao extends Dao<LocationImage>
{

    public LocationImageDao()
    {
        super("location_image", new LocationImageBeanFactory());
    }

    public Optional<LocationImage> findLocationImageById(int locationImageId) throws SQLException
    {
        return get("SELECT img.*, ST_AsText(img.location) location_coordinates from location_image img where id = ?", locationImageId).stream().findFirst();
    }

    public List<LocationImage> findImagesByCampaignId(int campaignId) throws SQLException
    {
        return findImagesByCampaignId(campaignId, true);
    }

    public List<LocationImage> findImagesByCampaignId(int campaignId, boolean media) throws SQLException
    {
        if(media)return get("SELECT img.*, ST_AsText(img.location) location_coordinates from location_image img where campaign_id = ?", campaignId);
        return get("SELECT img.id, img.campaign_id, img.municipality, img.region, img.source, img.date, img.resolution, ST_AsText(img.location) location_coordinates from location_image img where campaign_id = ?", campaignId);
    }

    public boolean deleteLocationImage(int imageId) throws SQLException
    {
        try(PooledConnection connection = ConnectionManager.getInstance().getConnection();
            PreparedStatement statement = connection.getConnection().prepareStatement("DELETE FROM location_image WHERE id = ?"))
        {
            statement.setInt(1, imageId);
            return statement.executeUpdate() == 1;
        }
    }

    public boolean atLeastOneByCampaignId(int campaignId) throws SQLException
    {
        return exist("campaign_id", campaignId);
    }

    public boolean insertLocationImages(LocationImage... locationImages) throws SQLException, InvalidBeanException
    {
        AtomicBoolean validBeans = new AtomicBoolean(true);

        boolean committed = transaction(connection ->
        {
            try(PreparedStatement statement = connection.prepareStatement("INSERT INTO location_image (campaign_id, location, municipality, region, source, date, resolution, image) values (?, ?::geography, ?, ?, ?, ?, ?::gml_enum, ?)", Statement.RETURN_GENERATED_KEYS))
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
                    statement.setString(8, image.getImage());

                    statement.addBatch();
                }
                int[] updateCounts = statement.executeBatch();
                for(int count : updateCounts)if(count == Statement.EXECUTE_FAILED) rollback();
            }
        });

        if(!committed && !validBeans.get())throw new InvalidBeanException();

        return committed;
    }

}
