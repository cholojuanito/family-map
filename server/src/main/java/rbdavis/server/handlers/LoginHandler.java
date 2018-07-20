package rbdavis.server.handlers;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import rbdavis.server.services.LoginService;
import rbdavis.shared.models.http.requests.LoginRequest;
import rbdavis.shared.models.http.responses.LoginOrRegisterResponse;
import rbdavis.shared.models.http.responses.Response;

import static rbdavis.server.StreamCommunicator.readString;
import static rbdavis.server.StreamCommunicator.writeString;

public class LoginHandler extends Handler implements HttpHandler {

    public void handle(HttpExchange exchange) throws IOException {
        String respData = null;
        int responseCode = 0;
        int emptyBodyCode = 0;
        Response errorResponse;

        switch (exchange.getRequestMethod().toLowerCase()) {
            case "post":
                try {
                    logger.info("Login request began");
                    InputStream reqBody = exchange.getRequestBody();
                    String reqData = readString(reqBody);

                    LoginRequest request = gson.fromJson(reqData, LoginRequest.class);
                    if (isValidLoginRequest(request)) {
                        LoginOrRegisterResponse response = new LoginService().login(request);

                        respData = gson.toJson(response);
                        responseCode = HttpURLConnection.HTTP_OK;

                        logger.info("Login successful");
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
                    logger.warning("JSON syntax error in request");
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
                logger.info(exchange.getRequestMethod() + " method is not supported for '/user/login'");
                errorResponse = new Response(exchange.getRequestMethod() + " method is not supported for '/user/login'");
                responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
                respData = gson.toJson(errorResponse);
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
