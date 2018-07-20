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

public class FileHandler extends Handler implements HttpHandler {

    private String WEB_ROOT_DIR = "server" + File.separator + "web" + File.separator;
    private String ROOT_INDEX = WEB_ROOT_DIR + "index.html";
    private String FOUR_OH_FOUR = WEB_ROOT_DIR + "HTML" + File.separator + "404.html";
    private String FIVE_HUNDRED = WEB_ROOT_DIR + "HTML" + File.separator + "500.html";

    @Override
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
                    logger.info(url + " file was sent");
                    result = Files.readAllBytes(path);
                    responseCode = HttpURLConnection.HTTP_OK;
                }
                catch (IOException error404) {
                    try {
                        logger.warning("Unable to find file at " + url);
                        path = Paths.get(FOUR_OH_FOUR);
                        result = Files.readAllBytes(path);
                        responseCode = HttpURLConnection.HTTP_NOT_FOUND;
                    }
                    catch (IOException error500) {
                        logger.severe("Internal server error occurred. " + error500.getMessage());
                        path = Paths.get(FIVE_HUNDRED);
                        result = Files.readAllBytes(path);
                        responseCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
                    }
                }
                break;
            default:
                logger.info("Method other than GET was used on FileHandler");
                path = Paths.get(FOUR_OH_FOUR);
                result = Files.readAllBytes(path);
                responseCode = HttpURLConnection.HTTP_NOT_FOUND;
                break;
        }
        exchange.sendResponseHeaders(responseCode, emptyBodyCode);

        OutputStream os = exchange.getResponseBody();
        os.write(result);
        os.close();
    }
}
