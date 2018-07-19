package rbdavis.server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import rbdavis.shared.models.http.requests.LoginRequest;
import rbdavis.shared.models.http.responses.LoginOrRegisterResponse;
import rbdavis.shared.models.http.responses.Response;
import sun.rmi.runtime.Log;

import static rbdavis.server.StreamCommunicator.*;

public class LoginHandler extends Handler implements HttpHandler {

    public void handle(HttpExchange exchange) throws IOException {
        String respData = null;
        int responseCode = 0;
        int emptyBodyCode = 0;
        Response errorResponse;

        switch (exchange.getRequestMethod().toLowerCase()) {
            case "post":
                try {
                    // Read request body
                    InputStream reqBody = exchange.getRequestBody();
                    String reqData = readString(reqBody);

                    // Make a RegisterRequest obj
                    LoginRequest request = gson.fromJson(reqData, LoginRequest.class);
                    if (isValidLoginRequest(request)) {

                        // Pass it to the LoginService

                        // Write response body
                        LoginOrRegisterResponse response = new LoginOrRegisterResponse("fakeToken", request.getUserName(), "fakePersonID");
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

    private boolean isValidLoginRequest (LoginRequest request) throws NullPointerException {
        boolean isValid = true;
        if (request.getUserName() == null || request.getPassword() == null) {
            isValid = false;
        }
        return isValid;
    }
}
