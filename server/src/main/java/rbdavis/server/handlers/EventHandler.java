package rbdavis.server.handlers;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import rbdavis.server.services.EventService;
import rbdavis.shared.models.http.requests.EventRequest;
import rbdavis.shared.models.http.responses.EventResponse;
import rbdavis.shared.models.http.responses.Response;

import static rbdavis.server.StreamCommunicator.readString;
import static rbdavis.server.StreamCommunicator.writeString;

public class EventHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String respData = null;
        int responseCode = 0;
        int emptyBodyCode = 0;
        Response response = new Response();

        switch (exchange.getRequestMethod().toLowerCase()) {
            case "get":
                try {
                    logger.info("One event request began");

                    // Get the "id" portion of the URI
                    String uri = exchange.getRequestURI().getPath();
                    int endURL = uri.length();
                    int afterLastBackSlash = uri.lastIndexOf("/") + 1;
                    String id = uri.substring(afterLastBackSlash, endURL);
                    if (id.length() == 0) {
                        throw new NullPointerException("Request property missing or has invalid value.");
                    }
                    Headers reqHeaders = exchange.getRequestHeaders();
                    EventService service = new EventService();
                    EventRequest request;

                    // Verify the auth token
                    if (reqHeaders.containsKey("Authorization")) {
                        String clientTokenStr = reqHeaders.getFirst("Authorization");
                        if (service.isVaildAuthToken(clientTokenStr)) {
                            // Create a request obj from the token
                            request = new EventRequest(id, clientTokenStr);
                            // Call the service
                            response = service.findEvent(request);
                            respData = gson.toJson(response);
                            responseCode = HttpURLConnection.HTTP_OK;
                            logger.info("One event request successful");
                        }
                        else {
                            logger.warning("Unauthorized request to /event/[id]");
                            response.setMessage("Error: You are not authorized to access this URL");
                            responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
                            respData = gson.toJson(response);
                        }
                    }
                    else {
                        logger.warning("Unauthorized request to /event/[id]");
                        response.setMessage("Error: You are not authorized to access this URL");
                        responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
                        respData = gson.toJson(response);
                    }
                }
                catch (NullPointerException e) {
                    logger.warning(e.getMessage());
                    response.setMessage(e.getMessage());
                    responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
                    respData = gson.toJson(response);
                }
                break;
            default:
                logger.info(exchange.getRequestMethod() + " method is not supported for this URL");
                response.setMessage("Error:" + exchange.getRequestMethod() + " method is not supported for this URL");
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
