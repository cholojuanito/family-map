package rbdavis.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHandler implements HttpHandler {

    private String WEB_ROOT_DIR = "server" + File.separator + "web" + File.separator;
    private String ROOT_INDEX = WEB_ROOT_DIR + "index.html";
    private String FOUR_OH_FOUR = WEB_ROOT_DIR + "HTML" + File.separator + "404.html";
    private String FIVE_HUNDRED = WEB_ROOT_DIR + "HTML" + File.separator + "500.html";

    public void handle(HttpExchange exchange) throws IOException {

        int responseCode = 0;
        int emptyBodyCode = 0;
        byte[] result = new byte[0];

        switch (exchange.getRequestMethod().toLowerCase()) {
            case "get":
                String url = exchange.getRequestURI().getPath();
                if (url.length() == 0 || url.equals("/")) {
                    url = ROOT_INDEX;
                }

                Path path = Paths.get(url);
                try {
                    result = Files.readAllBytes(path);
                    responseCode = HttpURLConnection.HTTP_OK;
                    // TODO: Log here
                }
                catch (IOException error404) {
                    try {
                        path = Paths.get(FOUR_OH_FOUR);
                        result = Files.readAllBytes(path);
                        responseCode = HttpURLConnection.HTTP_NOT_FOUND;
                        // TODO: Log here
                    }
                    catch (IOException error500) {
                        path = Paths.get(FIVE_HUNDRED);
                        result = Files.readAllBytes(path);
                        responseCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
                        // TODO: Log here
                    }
                }
                break;
            default:
                path = Paths.get(FOUR_OH_FOUR);
                result = Files.readAllBytes(path);
                responseCode = HttpURLConnection.HTTP_NOT_FOUND;
                // TODO: Log here
                break;
        }
        exchange.sendResponseHeaders(responseCode, emptyBodyCode);

        OutputStream os = exchange.getResponseBody();
        os.write(result);
        os.close();
    }
}
