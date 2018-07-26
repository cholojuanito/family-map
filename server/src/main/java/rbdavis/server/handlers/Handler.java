package rbdavis.server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import rbdavis.shared.models.data.Event;
import rbdavis.shared.utils.MockDataGenerator;

import static rbdavis.shared.utils.Constants.*;

public class Handler {
    /*
     * This deserializer is just so I can pass off the lab.
     * I need to be able to take in an int for the "dateHappened" data member
     * and produce a full LocalDate object.
     */
    JsonDeserializer<Event> eventDeserializer = new JsonDeserializer<Event>() {
        @Override
        public Event deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObj = json.getAsJsonObject();

            Event.EventType type;
            switch (jsonObj.get("type").getAsString()) {
                case "BIRTH":
                case "Birth":
                    type = Event.EventType.BIRTH;
                    break;
                case "BAPTISM":
                case "Baptism":
                    type = Event.EventType.BAPTISM;
                    break;
                case "MARRIAGE":
                case "Marriage":
                    type = Event.EventType.MARRIAGE;
                    break;
                case "DEATH":
                case "Death":
                    type = Event.EventType.DEATH;
                    break;
                default:
                    type = null;
                    break;
            }

            int year = jsonObj.get("year").getAsInt();
            LocalDate date = MockDataGenerator.createRandomDate(year);


            return new Event(
                    jsonObj.get("id").getAsString(),
                    jsonObj.get("personId").getAsString(),
                    jsonObj.get("descendant").getAsString(),
                    type,
                    jsonObj.get("latitude").getAsString(),
                    jsonObj.get("longitude").getAsString(),
                    jsonObj.get("city").getAsString(),
                    jsonObj.get("country").getAsString(),
                    date
            );
        }
    };
    private GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.registerTypeAdapter(Event.class, eventDeserializer).create();

    static Logger logger;

    static {
        try {
            initLog();
        }
        catch (IOException e) {
            System.out.println(INIT_LOG_ERR + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void initLog() throws IOException {
        Level logLevel = Level.ALL;

        logger = Logger.getLogger(HANDLER_LOG);
        logger.setLevel(logLevel);
        logger.setUseParentHandlers(false);

        java.util.logging.FileHandler handlerFileHandler = new java.util.logging.FileHandler(HANDLER_LOG_PATH, false);
        handlerFileHandler.setLevel(logLevel);
        handlerFileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(handlerFileHandler);
    }
}
