package rbdavis.server.handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import rbdavis.server.services.EventService;
import rbdavis.shared.models.http.requests.EventsRequest;
import rbdavis.shared.models.http.responses.Response;
import static rbdavis.shared.utils.Constants.*;

import static rbdavis.server.StreamCommunicator.writeString;

public class EventsHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String respData = null;
        int responseCode = 0;
        int emptyBodyCode = 0;
        Response response = new Response();

        switch (exchange.getRequestMethod().toLowerCase()) {
            case GET:
                logger.info(EVENTS_REQ_START);

                Headers reqHeaders = exchange.getRequestHeaders();
                EventService service = new EventService();
                EventsRequest request;

                // Verify the auth token
                if (reqHeaders.containsKey(AUTH)) {
                    String clientTokenStr = reqHeaders.getFirst(AUTH);
                    if (service.isVaildAuthToken(clientTokenStr)) {
                        // Create a request obj from the token
                        request = new EventsRequest(clientTokenStr);
                        // Call the service
                        response = service.findAllEvents(request);
                        respData = gson.toJson(response);
                        responseCode = HttpURLConnection.HTTP_OK;
                        logger.info(EVENTS_REQ_SUCCESS);
                    }
                    else {
                        logger.warning(UNAUTHORIZED_REQ_LOG + "/event");
                        response.setMessage(UNAUTHORIZED_REQ_ERR);
                        responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
                        respData = gson.toJson(response);
                    }
                }
                else {
                    logger.warning(UNAUTHORIZED_REQ_LOG + "/event");
                    response.setMessage(UNAUTHORIZED_REQ_ERR);
                    responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
                    respData = gson.toJson(response);
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
