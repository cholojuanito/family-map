package rbdavis.server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Handler {
    private GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.serializeNulls().create();

    static Logger logger;

    static {
        try {
            initLog();
        }
        catch (IOException e) {
            System.out.println("Could not initialize log: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void initLog() throws IOException {
        final String handlerLogPath = "server" + File.separator + "logs" + File.separator + "handlers.txt";

        Level logLevel = Level.ALL;

        logger = Logger.getLogger("handler");
        logger.setLevel(logLevel);
        logger.setUseParentHandlers(false);

        java.util.logging.FileHandler handlerFileHandler = new java.util.logging.FileHandler(handlerLogPath, false);
        handlerFileHandler.setLevel(logLevel);
        handlerFileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(handlerFileHandler);
    }
}
