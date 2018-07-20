package rbdavis.server.handlers;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import rbdavis.shared.models.http.requests.PersonRequest;
import rbdavis.shared.models.http.responses.PersonResponse;
import rbdavis.shared.models.http.responses.Response;

import static rbdavis.server.StreamCommunicator.readString;
import static rbdavis.server.StreamCommunicator.writeString;

public class PersonHandler extends Handler implements HttpHandler {

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
                    PersonRequest request = gson.fromJson(reqData, PersonRequest.class);
                    if (isValidPersonRequest(request)) {

                        // Pass it to the EventService.findbyId

                        // Write response body
                        PersonResponse response = new PersonResponse();
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

    private boolean isValidPersonRequest(PersonRequest request) throws NullPointerException {

        return false;
    }
}
