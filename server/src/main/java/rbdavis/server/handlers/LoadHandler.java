package rbdavis.server.handlers;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.security.InvalidParameterException;

import rbdavis.server.services.LoadService;
import rbdavis.shared.models.data.Event;
import rbdavis.shared.models.data.Gender;
import rbdavis.shared.models.data.Person;
import rbdavis.shared.models.data.User;
import rbdavis.shared.models.http.requests.LoadRequest;
import rbdavis.shared.models.http.responses.Response;

import static rbdavis.shared.utils.StreamCommunicator.readString;
import static rbdavis.shared.utils.StreamCommunicator.writeString;
import static rbdavis.shared.utils.Constants.INTERNAL_SERVER_ERR;
import static rbdavis.shared.utils.Constants.INTERNAL_SERVER_ERR_LOG;
import static rbdavis.shared.utils.Constants.INVALID_PROP_ERR;
import static rbdavis.shared.utils.Constants.JSON_SYNTAX_ERR;
import static rbdavis.shared.utils.Constants.LOAD_REQ_START;
import static rbdavis.shared.utils.Constants.LOAD_REQ_SUCCESS;
import static rbdavis.shared.utils.Constants.METHOD_NOT_SUPPORTED;
import static rbdavis.shared.utils.Constants.POST;

public class LoadHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String respData = null;
        int responseCode = 0;
        int emptyBodyCode = 0;
        Response response = new Response();

        switch (exchange.getRequestMethod()) {
            case POST:
                logger.info(LOAD_REQ_START);
                try {
                    InputStream reqBody = exchange.getRequestBody();
                    String reqData = readString(reqBody);


                    LoadRequest request = gson.fromJson(reqData, LoadRequest.class);
                    if (isValidLoadRequest(request)) {
                        response = new LoadService().load(request);
                        respData = gson.toJson(response);
                        responseCode = HttpURLConnection.HTTP_OK;

                        logger.info(LOAD_REQ_SUCCESS);
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
                catch (Exception e) {
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

    private boolean isValidLoadRequest(LoadRequest request) throws InvalidParameterException {
        boolean isValid = true;

        try {
            for (User u : request.getUsers()) {
                if (u.getUsername() == null || u.getPersonId() == null || u.getPassword() == null || u.getEmail() == null ||
                        u.getFirstName() == null || u.getLastName() == null ||
                        (u.getGender() != Gender.M && u.getGender() != Gender.F)) {

                    isValid = false;
                }
            }

            for (Person p : request.getPeople()) {
                if (p.getId() == null || p.getUserId() == null || p.getFirstName() == null || p.getLastName() == null ||
                        (p.getGender() != Gender.M && p.getGender() != Gender.F)) {

                    isValid = false;
                }
            }

            for (Event e : request.getEvents()) {
                if (e.getId() == null || e.getPersonId() == null || e.getUserId() == null || e.getLatitude() == null ||
                        e.getLongitude() == null || e.getCity() == null || e.getCountry() == null || e.getDateHappened() == null ||
                        e.getEventType() == null) {

                    isValid = false;
                }
            }
        }
        catch (NullPointerException e) {
            throw new InvalidParameterException(INVALID_PROP_ERR);
        }

        return isValid;
    }
}
