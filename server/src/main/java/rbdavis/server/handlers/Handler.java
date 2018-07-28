package rbdavis.server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import rbdavis.shared.models.data.Event;
import rbdavis.shared.utils.MockDataGenerator;

import static rbdavis.shared.utils.Constants.HANDLER_LOG;
import static rbdavis.shared.utils.Constants.HANDLER_LOG_PATH;
import static rbdavis.shared.utils.Constants.INIT_LOG_ERR;

public class Handler {

    private GsonBuilder gsonBuilder = new GsonBuilder();
    public Gson gson = gsonBuilder.create();

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
