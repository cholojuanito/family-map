package rbdavis.server.handlers;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.security.InvalidParameterException;

import rbdavis.server.services.LoginService;
import rbdavis.shared.models.http.requests.LoginRequest;
import rbdavis.shared.models.http.responses.Response;

import static rbdavis.shared.utils.StreamCommunicator.readString;
import static rbdavis.shared.utils.StreamCommunicator.writeString;
import static rbdavis.shared.utils.Constants.INTERNAL_SERVER_ERR;
import static rbdavis.shared.utils.Constants.INTERNAL_SERVER_ERR_LOG;
import static rbdavis.shared.utils.Constants.INVALID_PROP_ERR;
import static rbdavis.shared.utils.Constants.JSON_SYNTAX_ERR;
import static rbdavis.shared.utils.Constants.LOGIN_REQ_START;
import static rbdavis.shared.utils.Constants.LOGIN_REQ_SUCCESS;
import static rbdavis.shared.utils.Constants.METHOD_NOT_SUPPORTED;
import static rbdavis.shared.utils.Constants.POST;

public class LoginHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String respData = null;
        int responseCode = 0;
        int emptyBodyCode = 0;
        Response response = new Response();

        switch (exchange.getRequestMethod()) {
            case POST:
                try {
                    logger.info(LOGIN_REQ_START);
                    InputStream reqBody = exchange.getRequestBody();
                    String reqData = readString(reqBody);

                    LoginRequest request = gson.fromJson(reqData, LoginRequest.class);
                    if (isValidLoginRequest(request)) {
                        response = new LoginService().login(request);
                        responseCode = HttpURLConnection.HTTP_OK;
                        respData = gson.toJson(response);

                        logger.info(LOGIN_REQ_SUCCESS);
                    }
                    else {
                        throw new InvalidParameterException(INVALID_PROP_ERR);
                    }
                }
                catch (InvalidParameterException e) {
                    logger.warning(e.getMessage());
                    response.setMessage(e.getMessage());
                    responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
                    respData = gson.toJson(response);
                }
                catch (JsonParseException e) {
                    response.setMessage(JSON_SYNTAX_ERR);
                    responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
                    respData = gson.toJson(response);
                }
                catch (IOException e) {
                    logger.severe(INTERNAL_SERVER_ERR_LOG + e.getMessage());
                    response.setMessage(INTERNAL_SERVER_ERR);
                    responseCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
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

    private boolean isValidLoginRequest(LoginRequest request) throws InvalidParameterException {
        boolean isValid = true;
        try {
            if (request.getUserName() == null || request.getPassword() == null) {
                isValid = false;
            }
        }
        catch (NullPointerException e) {
            throw new InvalidParameterException(INVALID_PROP_ERR);
        }
        return isValid;
    }
}
