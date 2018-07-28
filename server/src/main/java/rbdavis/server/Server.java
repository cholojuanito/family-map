package rbdavis.server;

import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import rbdavis.server.handlers.*;
import static rbdavis.shared.utils.Constants.*;

public class Server {
    private static final int MAX_WAITING_CONNECTIONS = 12;
    private HttpServer server;

    private static Logger logger;

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

        logger = Logger.getLogger(SERVER_LOG);
        logger.setLevel(logLevel);
        logger.setUseParentHandlers(false);

        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(logLevel);
        consoleHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(consoleHandler);

        java.util.logging.FileHandler serverFileHandler = new java.util.logging.FileHandler(SERVER_LOG_PATH, false);
        serverFileHandler.setLevel(logLevel);
        serverFileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(serverFileHandler);
    }


    private void run(String portNumber) {

        logger.info(INIT_SERVER);
        try {
            server = HttpServer.create(new InetSocketAddress(Integer.parseInt(portNumber)), MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return;
        }

        // Indicate that we are using the default "executor".
        // This line is necessary, but its function is unimportant for our purposes.
        server.setExecutor(null);

        // Create and install the HTTP handlers
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/fill", new FillHandler());
        server.createContext("/load", new LoadHandler());

        server.createContext("/event/", new EventHandler());
        server.createContext("/event", new EventsHandler());
        server.createContext("/person/", new PersonHandler());
        server.createContext("/person", new PeopleHandler());

        server.createContext("/", new rbdavis.server.handlers.FileHandler());

        logger.info(START_SERVER);

        server.start();

        logger.info(SERVER_LISTENING_ON + portNumber);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            logger.warning(SERVER_NO_PORT_ERR);
            System.out.println(SERVER_NO_PORT_ERR);
            return;
        }
        String port = args[0];
        new Server().run(port);
    }
}