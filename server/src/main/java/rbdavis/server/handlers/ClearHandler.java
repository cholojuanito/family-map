package rbdavis.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import rbdavis.server.services.ClearService;
import rbdavis.shared.models.http.responses.Response;

import static rbdavis.server.StreamCommunicator.writeString;

public class ClearHandler extends Handler implements HttpHandler {

    public void handle(HttpExchange exchange) throws IOException {
        String respData = null;
        int responseCode = 0;
        int emptyBodyCode = 0;
        Response response = new Response();

        switch (exchange.getRequestMethod().toLowerCase()) {
            case "post":
                try {
                    response = ClearService.clear();
                    responseCode = HttpURLConnection.HTTP_OK;
                    respData = gson.toJson(response);
                }
                catch (Exception e) {
                    logger.warning("Something happened while trying to clear database\n" + e.getMessage());
                }
                break;
            default:
                response.setMessage(exchange.getRequestMethod() + " method is not supported for this URL");
                responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
                respData = gson.toJson(response);
                break;
        }

        exchange.sendResponseHeaders(responseCode, emptyBodyCode);
        OutputStream respBody = exchange.getResponseBody();
        writeString(respData, respBody);
        respBody.close();
    }
}
