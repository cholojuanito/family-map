package rbdavis.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import rbdavis.server.handlers.FileHandler;

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
        server.createContext("/", new FileHandler());
        //server.createContext("/games/list", new ListGamesHandler());
        //server.createContext("/routes/claim", new ClaimRouteHandler());

        // Log message indicating that the HttpServer is about the start accepting
        // incoming client connections.
        System.out.println("Starting server");
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        server.start();

        // Log message indicating that the server has successfully started.
        System.out.println("Server started");
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