package rbdavis.server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.rmi.server.ExportException;
import java.security.InvalidParameterException;
import java.util.logging.Logger;

import rbdavis.server.services.RegisterService;
import rbdavis.shared.models.http.requests.RegisterRequest;
import rbdavis.shared.models.http.responses.LoginOrRegisterResponse;
import rbdavis.shared.models.http.responses.Response;

import static rbdavis.server.StreamCommunicator.*;

public class RegisterHandler extends Handler implements HttpHandler {

    public void handle(HttpExchange exchange) throws IOException {
        String respData = null;
        int responseCode = 0;
        int emptyBodyCode = 0;
        Response errorResponse;

            switch (exchange.getRequestMethod().toLowerCase()) {
                case "post":
                    logger.info("Register request began");
                    try {
                        // Read request body
                        InputStream reqBody = exchange.getRequestBody();
                        String reqData = readString(reqBody);

                        // Make a RegisterRequest obj
                        RegisterRequest request = gson.fromJson(reqData, RegisterRequest.class);
                        if (isValidRegisterRequest(request)) {
                            // Pass it to the RegisterService
                            LoginOrRegisterResponse response = new RegisterService().register(request);

                            // Write response body
                            //LoginOrRegisterResponse response = new LoginOrRegisterResponse("fakeToken", request.getUserName(), "fakePersonID");
                            respData = gson.toJson(response);
                            responseCode = HttpURLConnection.HTTP_OK;

                            logger.info("Register request successful");
                        }
                        else {
                            throw new NullPointerException("Request property missing or has invalid value.");
                        }
                    }
                    catch (NullPointerException e) {
                        logger.warning(e.getMessage());

                        errorResponse = new Response(e.getMessage());
                        responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
                        respData = gson.toJson(errorResponse);
                    }
                    catch (JsonParseException e) {
                        logger.warning("Error occurred while reading JSON " + e.getMessage());

                        errorResponse = new Response("Error occurred while reading JSON. Please check your syntax");
                        responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
                        respData = gson.toJson(errorResponse);
                    }
                    catch (IOException e) {
                        logger.severe("Internal server error occurred " + e.getMessage());

                        errorResponse = new Response("An error occurred on our end. Sorry!");
                        responseCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
                        respData = gson.toJson(errorResponse);
                    }

                    break;

                default:
                    logger.info(exchange.getRequestMethod() + " method is not supported for '/user/register'");
                    
                    errorResponse = new Response(exchange.getRequestMethod() + " method is not supported for '/user/register'");
                    responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
                    respData = gson.toJson(errorResponse);
                    break;
            }

        exchange.sendResponseHeaders(responseCode, emptyBodyCode);
        OutputStream respBody = exchange.getResponseBody();
        writeString(respData, respBody);
        respBody.close();

    }

    private boolean isValidRegisterRequest (RegisterRequest request) throws NullPointerException {
        boolean isValid = true;
        if (request.getUserName() == null || request.getPassword() == null || request.getEmail() == null ||
            request.getFirstName() == null || request.getLastName() == null || request.getGender() == null) {
            isValid = false;
        }
        return isValid;
    }

}
