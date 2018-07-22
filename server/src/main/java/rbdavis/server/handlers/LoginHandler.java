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
        Response response = new LoginOrRegisterResponse();

        switch (exchange.getRequestMethod().toLowerCase()) {
            case "post":
                try {
                    logger.info("Login request began");
                    InputStream reqBody = exchange.getRequestBody();
                    String reqData = readString(reqBody);

                    LoginRequest request = gson.fromJson(reqData, LoginRequest.class);
                    if (isValidLoginRequest(request)) {
                        response = new LoginService().login(request);

                        responseCode = HttpURLConnection.HTTP_OK;
                        respData = gson.toJson(response);

                        logger.info("Login successful");
                    }
                    else {
                        throw new NullPointerException("Request property missing or has invalid value.");
                    }
                }
                catch (NullPointerException e) {
                    logger.warning(e.getMessage());
                    response.setMessage(e.getMessage());
                    responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
                    respData = gson.toJson(response);
                }
                catch (JsonParseException e) {
                    logger.warning("JSON syntax error in request");
                    response.setMessage("Error occurred while reading JSON. Please check your syntax");
                    responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
                    respData = gson.toJson(response);
                }
                catch (IOException e) {
                    logger.severe("Internal server error occurred " + e.getMessage());
                    response.setMessage("An error occurred on our end. Sorry!");
                    responseCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
                    respData = gson.toJson(response);
                }
                break;

            default:
                logger.info(exchange.getRequestMethod() + " method is not supported for this URL");
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

    private boolean isValidLoginRequest (LoginRequest request) throws NullPointerException {
        boolean isValid = true;
        if (request.getUserName() == null || request.getPassword() == null) {
            isValid = false;
        }
        return isValid;
    }
}
