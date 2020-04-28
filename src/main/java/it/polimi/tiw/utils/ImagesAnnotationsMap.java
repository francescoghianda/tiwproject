package it.polimi.tiw.utils;

import it.polimi.tiw.beans.Annotation;
import it.polimi.tiw.beans.LocationImage;

import java.util.*;

public class ImagesAnnotationsMap
{
    private List<LocationImage> images;
    private List<List<Annotation>> annotations;

    public ImagesAnnotationsMap()
    {
        images = new ArrayList<>();
        annotations = new ArrayList<>();
    }

    public void next(LocationImage locationImage)
    {
        images.add(locationImage);
        annotations.add(new ArrayList<>());
    }

    public void put(Annotation annotation)
    {
        if(annotation != null)annotations.get(annotations.size()-1).add(annotation);
    }

    public Map<LocationImage, List<Annotation>> buildMap()
    {
        Map<LocationImage, List<Annotation>> map = new LinkedHashMap<>();
        for(int i = 0; i < images.size(); i++)
            map.put(images.get(i), annotations.get(i));
        return map;
    }
}
