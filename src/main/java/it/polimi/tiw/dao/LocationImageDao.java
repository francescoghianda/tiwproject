package it.polimi.tiw.dao;

import it.polimi.tiw.beans.Annotation;
import it.polimi.tiw.beans.LocationImage;
import it.polimi.tiw.beans.validation.InvalidBeanException;
import it.polimi.tiw.utils.ImagesAnnotationsMap;
import it.polimi.tiw.utils.beans.LocationImageBeanFactory;
import it.polimi.tiw.utils.dao.Dao;
import it.polimi.tiw.utils.sql.ConnectionManager;
import it.polimi.tiw.utils.sql.PooledConnection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class LocationImageDao extends Dao<LocationImage>
{

    public LocationImageDao()
    {
        super("location_image", new LocationImageBeanFactory());
    }

    public Optional<LocationImage> findLocationImageById(int locationImageId) throws SQLException
    {
        return rawGet("SELECT img.*, ST_AsText(img.location) location_coordinates from location_image img where id = ?", locationImageId).stream().findFirst();
    }

    public List<LocationImage> findImagesByCampaignId(int campaignId) throws SQLException
    {
        return findImagesByCampaignId(campaignId, true);
    }

    public List<LocationImage> findImagesByCampaignId(int campaignId, boolean media) throws SQLException
    {
        if(media)return rawGet("SELECT img.*, ST_AsText(img.location) location_coordinates from location_image img where campaign_id = ?", campaignId);
        return rawGet("SELECT img.id, img.campaign_id, img.municipality, img.region, img.source, img.date, img.resolution, ST_AsText(img.location) location_coordinates from location_image img where campaign_id = ?", campaignId);
    }

    public Map<LocationImage, List<Annotation>> findImagesAndAnnotationsByCampaignId(int campaignId) throws SQLException
    {
        try(PooledConnection connection = ConnectionManager.getInstance().getConnection();
            PreparedStatement statement = connection.getConnection().prepareStatement(
                    "SELECT img.id id, img.campaign_id, ST_AsText(img.location) location_coordinates, img.municipality," +
                    "       img.region, img.source, img.date date, img.resolution, ann.id ann_id, ann.user_id, ann.image_id, ann.date ann_date," +
                            "ann.valid, ann.trust, ann.notes " +
                    "FROM location_image img " +
                    "LEFT JOIN annotation ann " +
                    "ON ann.image_id = img.id " +
                    "WHERE img.campaign_id = ? " +
                    "ORDER BY img.id"))
        {
            statement.setInt(1, campaignId);

            try(ResultSet resultSet = statement.executeQuery())
            {
                return buildImageAnnotationMap(resultSet);
            }
        }
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


    private Map<LocationImage, List<Annotation>> buildImageAnnotationMap(ResultSet resultSet) throws SQLException
    {
        ImagesAnnotationsMap map = new ImagesAnnotationsMap();
        int lastId = 0;

        while(resultSet.next())
        {
            int imageId = resultSet.getInt("id");
            if(imageId > lastId)map.next(beanFactory.convert(resultSet));
            lastId = imageId;
            map.put(buildAnnotation(resultSet));
        }

        return map.buildMap();
    }

    private Annotation buildAnnotation(ResultSet resultSet) throws SQLException
    {
        int id = resultSet.getInt("ann_id");
        if(id == 0)return null;
        int userId = resultSet.getInt("user_id");
        int imageId = resultSet.getInt("image_id");
        Date date = resultSet.getDate("ann_date");
        boolean valid = resultSet.getBoolean("valid");
        String trust = resultSet.getString("trust");
        String notes = resultSet.getString("notes");
        return new Annotation(id, userId, imageId, date, valid, trust, notes);
    }
}
