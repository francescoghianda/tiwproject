package it.polimi.tiw.utils;

import it.polimi.tiw.beans.*;
import it.polimi.tiw.dao.AnnotationDao;
import it.polimi.tiw.utils.beans.JoinedBean;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.sql.SQLException;
import java.util.List;

public class LocationImageJsonBuilder
{
    private final LocationImage locationImage;
    private List<JoinedBean<Annotation, User, Worker>> joinedBeans;

    public LocationImageJsonBuilder(LocationImage locationImage)
    {
        this.locationImage  = locationImage;
    }

    public void retrieveData(AnnotationDao annotationDao) throws SQLException
    {
        joinedBeans = annotationDao.findAnnotationAndUserByImageId(locationImage.getId());
    }

    public JsonObjectBuilder getJsonBuilder()
    {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        addLocationImage(objectBuilder);
        objectBuilder.add("annotatios", getAnnotationArrayBuilder());
        return objectBuilder;
    }

    private void addLocationImage(JsonObjectBuilder objectBuilder)
    {
        objectBuilder.add("id", locationImage.getId());
        objectBuilder.add("campaign_id", locationImage.getCampaignId());
        objectBuilder.add("location", getLocationObjectBuilder(locationImage.getLocation()));
        objectBuilder.add("municipality", locationImage.getMunicipality());
        objectBuilder.add("region", locationImage.getRegion());
        objectBuilder.add("source", locationImage.getSource());
        objectBuilder.add("date", locationImage.getDate().toString());
        objectBuilder.add("resolution", locationImage.getResolution());
        if(locationImage.getImage() != null && !locationImage.getImage().isEmpty())objectBuilder.add("image", locationImage.getImage());
    }

    private JsonObjectBuilder getLocationObjectBuilder(Location location)
    {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add("latitude", location.getLatitude());
        objectBuilder.add("longitude", location.getLongitude());
        return objectBuilder;
    }

    private JsonArrayBuilder getAnnotationArrayBuilder()
    {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        joinedBeans.forEach(joinedBean -> arrayBuilder.add(getAnnotationObjectBuilder(joinedBean.getBean1(), joinedBean.getBean2(), joinedBean.getBean3())));
        return arrayBuilder;
    }

    private JsonObjectBuilder getAnnotationObjectBuilder(Annotation annotation, User user, Worker worker)
    {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add("id", annotation.getId());
        objectBuilder.add("date", annotation.getDate().toString());
        objectBuilder.add("valid", annotation.isValid());
        objectBuilder.add("trust", annotation.getTrust());
        objectBuilder.add("notes", annotation.getNotes());
        objectBuilder.add("user", getUserObjectBuilder(user, worker));
        return objectBuilder;
    }

    private JsonObjectBuilder getUserObjectBuilder(User user, Worker worker)
    {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add("username", user.getUsername());
        objectBuilder.add("experience", worker.getExpLvl());
        return objectBuilder;
    }
}
