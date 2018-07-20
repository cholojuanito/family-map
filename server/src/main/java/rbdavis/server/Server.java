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

import rbdavis.server.handlers.ClearHandler;
import rbdavis.server.handlers.EventHandler;
import rbdavis.server.handlers.EventsHandler;
import rbdavis.server.handlers.FillHandler;
import rbdavis.server.handlers.LoadHandler;
import rbdavis.server.handlers.LoginHandler;
import rbdavis.server.handlers.PeopleHandler;
import rbdavis.server.handlers.PersonHandler;
import rbdavis.server.handlers.RegisterHandler;

public class Server {
    private static final int MAX_WAITING_CONNECTIONS = 12;
    private HttpServer server;

    private static Logger logger;

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
        final String serverLogPath = "server" + File.separator + "logs" + File.separator + "server.txt";

        Level logLevel = Level.ALL;

        logger = Logger.getLogger("server");
        logger.setLevel(logLevel);
        logger.setUseParentHandlers(false);

        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(logLevel);
        consoleHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(consoleHandler);

        java.util.logging.FileHandler serverFileHandler = new java.util.logging.FileHandler(serverLogPath, false);
        serverFileHandler.setLevel(logLevel);
        serverFileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(serverFileHandler);
    }


    private void run(String portNumber) {

        logger.info("Initializing HTTP Server");
        try {
            server = HttpServer.create(
                    new InetSocketAddress(Integer.parseInt(portNumber)),
                                            MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return;
        }

        // Indicate that we are using the default "executor".
        // This line is necessary, but its function is unimportant for our purposes.
        server.setExecutor(null);

        logger.info("Creating contexts");

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

        logger.info("Starting HTTP server");

        server.start();

        logger.info("Server listening on port: " + portNumber);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Server attempted to start without port #.");
            return;
        }
        String port = args[0];
        new Server().run(port);
    }
}