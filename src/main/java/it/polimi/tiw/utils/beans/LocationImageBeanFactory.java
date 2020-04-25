package it.polimi.tiw.utils.beans;

import it.polimi.tiw.beans.LocationImage;
import it.polimi.tiw.utils.Location;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationImageBeanFactory implements BeanFactory<LocationImage>
{
    @Override
    public LocationImage convert(ResultSet resultSet) throws SQLException
    {
        LocationImage locationImage = new LocationImage();
        locationImage.setId(resultSet.getInt("id"));
        locationImage.setCampaignId(resultSet.getInt("campaign_id"));
        locationImage.setLocation(Location.fromWKT(resultSet.getString("location_coordinates")));
        locationImage.setMunicipality(resultSet.getString("municipality"));
        locationImage.setRegion(resultSet.getString("region"));
        locationImage.setSource(resultSet.getString("source"));
        locationImage.setDate(resultSet.getDate("date"));
        locationImage.setResolution(resultSet.getString("resolution"));
        if(hasColumn(resultSet, "image"))locationImage.setImage(resultSet.getString("image"));
        return locationImage;
    }
}
