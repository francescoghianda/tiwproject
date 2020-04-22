package it.polimi.tiw.utils;

public final class Location
{
    private final float latitude;
    private final float longitude;

    public Location(float longitude, float latitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getLatitude()
    {
        return latitude;
    }

    public float getLongitude()
    {
        return longitude;
    }

    @Override
    public String toString()
    {
        return "POINT("+longitude+" "+latitude+")";
    }

    public static Location fromString(String latitude, String longitude)
    {
        return new Location(Float.parseFloat(longitude), Float.parseFloat(latitude));
    }

    public static Location fromWKT(String wkt)
    {
        String[] coordinates = wkt.substring(6, wkt.length()-1).split(" ");
        return new Location(Float.parseFloat(coordinates[0]), Float.parseFloat(coordinates[1]));
    }
}
