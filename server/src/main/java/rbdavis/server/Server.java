package rbdavis.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import rbdavis.server.handlers.ClearHandler;
import rbdavis.server.handlers.EventHandler;
import rbdavis.server.handlers.EventsHandler;
import rbdavis.server.handlers.FileHandler;
import rbdavis.server.handlers.LoadHandler;
import rbdavis.server.handlers.LoginHandler;
import rbdavis.server.handlers.PeopleHandler;
import rbdavis.server.handlers.PersonHandler;
import rbdavis.server.handlers.RegisterHandler;

public class Server {
    private static final int MAX_WAITING_CONNECTIONS = 12;
    private HttpServer server;


    private void run(String portNumber) {

        System.out.println("Initializing HTTP Server");

        try {
            server = HttpServer.create(new InetSocketAddress(Integer.parseInt(portNumber)),
                                        MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Indicate that we are using the default "executor".
        // This line is necessary, but its function is unimportant for our purposes.
        server.setExecutor(null);

        System.out.println("Creating contexts");

        // Create and install the HTTP handlers
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/fill", new FileHandler());
        server.createContext("/load", new LoadHandler());

        server.createContext("/event/", new EventHandler());
        server.createContext("/event", new EventsHandler());
        server.createContext("/person/", new PersonHandler());
        server.createContext("/person", new PeopleHandler());

        server.createContext("/", new FileHandler());

        // Log message indicating that the HttpServer is about the start accepting
        // incoming client connections.
        // TODO: Log here
        System.out.println("Starting server");

        server.start();

        // Log message indicating that the server has successfully started.
        // TODO: Log here
        System.out.println("Server listening on port: " + portNumber);
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