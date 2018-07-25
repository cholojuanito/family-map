package rbdavis.server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import static rbdavis.shared.utils.Constants.*;

public class Handler {
    private GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();

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
