package it.polimi.tiw.beans;

import it.polimi.tiw.beans.validation.annotations.Enum;
import it.polimi.tiw.beans.validation.annotations.IntRange;
import it.polimi.tiw.beans.validation.annotations.NotNull;
import it.polimi.tiw.beans.validation.annotations.Size;
import it.polimi.tiw.utils.Location;

import java.sql.Date;


public class LocationImage extends Bean
{

    @IntRange(min = 0)
    private int id;

    @IntRange(min = 1)
    private int campaignId;

    @NotNull
    private Location location;

    @Size(min = 1)
    private String municipality;

    @Size(min = 1)
    private String region;

    @Size(min = 1)
    private String source;

    @NotNull
    private Date date;

    @Enum({"GOOD", "MEDIUM", "LOW"})
    private String resolution;

    @Size(min = 1)
    private String image;

    public LocationImage(){}

    public LocationImage(int id, int campaignId, Location location, String municipality, String region, String source, Date date, String resolution, String image)
    {
        this.id = id;
        this.campaignId = campaignId;
        this.location = location;
        this.municipality = municipality;
        this.region = region;
        this.source = source;
        this.date = date;
        this.resolution = resolution;
        this.image = image;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getCampaignId()
    {
        return campaignId;
    }

    public void setCampaignId(int campaignId)
    {
        this.campaignId = campaignId;
    }

    public Location getLocation()
    {
        return location;
    }

    public void setLocation(Location location)
    {
        this.location = location;
    }

    public String getMunicipality()
    {
        return municipality;
    }

    public void setMunicipality(String municipality)
    {
        this.municipality = municipality;
    }

    public String getRegion()
    {
        return region;
    }

    public void setRegion(String region)
    {
        this.region = region;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public String getResolution()
    {
        return resolution;
    }

    public void setResolution(String resolution)
    {
        this.resolution = resolution;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }
}
