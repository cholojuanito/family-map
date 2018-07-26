package rbdavis.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
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

public class FileHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        int responseCode = 0;
        int emptyBodyCode = 0;
        byte[] result = new byte[0];

        switch (exchange.getRequestMethod().toLowerCase()) {
            case GET:
                String url = exchange.getRequestURI().getPath();
                if (url.length() == 0 || url.equals("/")) {
                    url = ROOT_INDEX;
                }

                Path path = Paths.get(url);
                try {
                    logger.info(url + FILE_SENT);
                    result = Files.readAllBytes(path);
                    responseCode = HttpURLConnection.HTTP_OK;
                }
                catch (IOException error404) {
                    try {
                        logger.warning(FOUR_OH_FOUR_ERR + url);
                        path = Paths.get(FOUR_OH_FOUR);
                        result = Files.readAllBytes(path);
                        responseCode = HttpURLConnection.HTTP_NOT_FOUND;
                    }
                    catch (IOException error500) {
                        logger.severe(INTERNAL_SERVER_ERR_LOG + error500.getMessage());
                        path = Paths.get(FIVE_HUNDRED);
                        result = Files.readAllBytes(path);
                        responseCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
                    }
                }
                break;
            default:
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
