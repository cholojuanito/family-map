package rbdavis.server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import rbdavis.shared.models.http.requests.EventRequest;
import rbdavis.shared.models.http.responses.EventResponse;
import rbdavis.shared.models.http.responses.Response;

import static rbdavis.server.StreamCommunicator.readString;
import static rbdavis.server.StreamCommunicator.writeString;

public class EventHandler implements HttpHandler {

    private GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
    private Gson gson = gsonBuilder.create();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String respData = null;
        int responseCode = 0;
        int emptyBodyCode = 0;
        Response errorResponse;

        switch (exchange.getRequestMethod().toLowerCase()) {
            case "get":
                try {
                    // Read request body
                    InputStream reqBody = exchange.getRequestBody();
                    String reqData = readString(reqBody);

                    // Make a RegisterRequest obj
                    EventRequest request = gson.fromJson(reqData, EventRequest.class);
                    if (isValidEventRequest(request)) {

                        // Pass it to the EventService.findbyId

                        // Write response body
                        EventResponse response = new EventResponse();
                        respData = gson.toJson(response);
                        responseCode = HttpURLConnection.HTTP_OK;
                        // TODO: Log here
                    }
                    else {
                        // TODO: Log here
                        throw new NullPointerException("Missing a property");
                    }
                }
                catch (NullPointerException e) {
                    System.out.println(e.getMessage());
                    errorResponse = new Response("Request property missing or has invalid value.");
                    responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
                    respData = gson.toJson(errorResponse);
                }
                catch (JsonParseException e) {
                    errorResponse = new Response("Error occurred while reading JSON. Please check your syntax");
                    responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
                    respData = gson.toJson(errorResponse);
                    // TODO: Log here
                }
                catch (IOException e) {
                    errorResponse = new Response("An error occurred on our end. Sorry!");
                    responseCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
                    respData = gson.toJson(errorResponse);
                    // TODO: Log here
                }
                break;
            default:
                errorResponse = new Response(exchange.getRequestMethod() + " method is not supported for this URL");
                responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
                respData = gson.toJson(errorResponse);
                // TODO: Log here
                break;
        }

        exchange.sendResponseHeaders(responseCode, emptyBodyCode);
        OutputStream respBody = exchange.getResponseBody();
        writeString(respData, respBody);
        respBody.close();
    }

    private boolean isValidEventRequest(EventRequest request) throws NullPointerException {

        return false;
    }
}
