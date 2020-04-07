package it.polimi.tiw.utils;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonGenerator;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Arrays;

@Deprecated
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

    public static void writeJson(PrintWriter printWriter, String[]... pairs)
    {
        try(JsonGenerator generator = Json.createGenerator(printWriter))
        {
            generator.writeStartObject();
            Arrays.stream(pairs).forEach(pair -> generator.write(pair[0], pair[1]));
            generator.writeEnd();
        }
    }
}
