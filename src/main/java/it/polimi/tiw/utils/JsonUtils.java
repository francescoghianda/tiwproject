package it.polimi.tiw.utils;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedReader;

public class JsonUtils
{
    private JsonUtils(){}

    public static JsonObject readJson(BufferedReader reader)
    {
        JsonReader jsonReader = Json.createReader(reader);
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();
        return jsonObject;
    }
}
