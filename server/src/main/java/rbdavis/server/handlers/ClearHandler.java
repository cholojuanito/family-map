package rbdavis.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import rbdavis.server.services.ClearService;
import rbdavis.shared.models.http.responses.Response;

import static rbdavis.server.StreamCommunicator.writeString;
import static rbdavis.shared.utils.Constants.CLEAR_ERR;
import static rbdavis.shared.utils.Constants.CLEAR_SUCCESS;
import static rbdavis.shared.utils.Constants.METHOD_NOT_SUPPORTED;
import static rbdavis.shared.utils.Constants.POST;

public class ClearHandler extends Handler implements HttpHandler {

    public void handle(HttpExchange exchange) throws IOException {
        String respData = null;
        int responseCode = 0;
        int emptyBodyCode = 0;
        Response response = new Response();

        switch (exchange.getRequestMethod().toLowerCase()) {
            case POST:
                try {
                    response = ClearService.clear();
                    logger.info(CLEAR_SUCCESS);
                    responseCode = HttpURLConnection.HTTP_OK;
                    respData = gson.toJson(response);
                }
                catch (Exception e) {
                    logger.warning(CLEAR_ERR + e.getMessage());
                }
                break;
            default:
                response.setMessage(exchange.getRequestMethod() + METHOD_NOT_SUPPORTED);
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
