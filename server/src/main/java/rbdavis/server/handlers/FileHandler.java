package rbdavis.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static rbdavis.shared.utils.Constants.FILE_SENT;
import static rbdavis.shared.utils.Constants.FIVE_HUNDRED;
import static rbdavis.shared.utils.Constants.FOUR_OH_FOUR;
import static rbdavis.shared.utils.Constants.FOUR_OH_FOUR_ERR;
import static rbdavis.shared.utils.Constants.GET;
import static rbdavis.shared.utils.Constants.INTERNAL_SERVER_ERR_LOG;
import static rbdavis.shared.utils.Constants.ROOT_INDEX;
import static rbdavis.shared.utils.Constants.WEB_ROOT_DIR;

public class FileHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        int responseCode = 0;
        int emptyBodyCode = 0;

        switch (exchange.getRequestMethod().toLowerCase()) {
            case GET:
                String url = WEB_ROOT_DIR + exchange.getRequestURI().getPath();
                if (url.length() == 0 || url.equals(WEB_ROOT_DIR + "/")) {
                    url = ROOT_INDEX;
                }
                OutputStream os = exchange.getResponseBody();

                Path path;
                try {
                    path = FileSystems.getDefault().getPath(url);
                    if (path.toFile().exists() && path.toFile().canRead()) {
                        logger.info(url + FILE_SENT);
                        responseCode = HttpURLConnection.HTTP_OK;
                        exchange.sendResponseHeaders(responseCode, emptyBodyCode);
                        Files.copy(path, os);
                    }
                    else {
                        throw new IOException();
                    }
                }
                catch (IOException error404) {
                    try {
                        logger.warning(FOUR_OH_FOUR_ERR + url);
                        path = FileSystems.getDefault().getPath(FOUR_OH_FOUR);
                        responseCode = HttpURLConnection.HTTP_NOT_FOUND;
                        exchange.sendResponseHeaders(responseCode, emptyBodyCode);
                        Files.copy(path, os);
                    }
                    catch (IOException error500) {
                        logger.severe(INTERNAL_SERVER_ERR_LOG + error500.getMessage());
                        path = FileSystems.getDefault().getPath(FIVE_HUNDRED);
                        responseCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
                        exchange.sendResponseHeaders(responseCode, emptyBodyCode);
                        Files.copy(path, os);
                    }
                }
                os.close();
                break;

            default:
                path = FileSystems.getDefault().getPath(FOUR_OH_FOUR);
                responseCode = HttpURLConnection.HTTP_NOT_FOUND;
                exchange.sendResponseHeaders(responseCode, emptyBodyCode);
                OutputStream osErr = exchange.getResponseBody();
                Files.copy(path, osErr);
                osErr.close();
                break;
        }
    }
}
