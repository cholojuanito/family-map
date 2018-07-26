package rbdavis.server.handlers;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.security.InvalidParameterException;

import rbdavis.server.services.RegisterService;
import rbdavis.shared.models.http.requests.RegisterRequest;
import rbdavis.shared.models.http.responses.Response;

import static rbdavis.server.StreamCommunicator.readString;
import static rbdavis.server.StreamCommunicator.writeString;
import static rbdavis.shared.utils.Constants.INTERNAL_SERVER_ERR;
import static rbdavis.shared.utils.Constants.INTERNAL_SERVER_ERR_LOG;
import static rbdavis.shared.utils.Constants.INVALID_PROP_ERR;
import static rbdavis.shared.utils.Constants.JSON_SYNTAX_ERR;
import static rbdavis.shared.utils.Constants.METHOD_NOT_SUPPORTED;
import static rbdavis.shared.utils.Constants.POST;
import static rbdavis.shared.utils.Constants.REG_REQ_START;
import static rbdavis.shared.utils.Constants.REG_REQ_SUCCESS;

public class RegisterHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String respData = null;
        int responseCode = 0;
        int emptyBodyCode = 0;
        Response response = new Response();

            switch (exchange.getRequestMethod().toLowerCase()) {
                case POST:
                    logger.info(REG_REQ_START);
                    try {
                        InputStream reqBody = exchange.getRequestBody();
                        String reqData = readString(reqBody);

                        RegisterRequest request = gson.fromJson(reqData, RegisterRequest.class);
                        if (isValidRegisterRequest(request)) {
                            response = new RegisterService().register(request);
                            respData = gson.toJson(response);
                            responseCode = HttpURLConnection.HTTP_OK;

                            logger.info(REG_REQ_SUCCESS);
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

    private boolean isValidRegisterRequest (RegisterRequest request) throws InvalidParameterException {
        boolean isValid = true;
        try {
            if (request.getUserName() == null || request.getPassword() == null || request.getEmail() == null ||
                    request.getFirstName() == null || request.getLastName() == null ||
                    (!request.getGender().equals("m") && !request.getGender().equals("f"))) {

                isValid = false;
            }
        }
        catch (NullPointerException e) {
            throw new InvalidParameterException(INVALID_PROP_ERR);
        }
        return isValid;
    }

}
