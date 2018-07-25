package rbdavis.server.handlers;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.security.InvalidParameterException;
import java.util.ArrayList;

import rbdavis.server.services.LoadService;
import rbdavis.shared.models.data.Event;
import rbdavis.shared.models.data.Gender;
import rbdavis.shared.models.data.Person;
import rbdavis.shared.models.data.User;
import rbdavis.shared.models.http.requests.LoadRequest;
import rbdavis.shared.models.http.responses.Response;

import static rbdavis.server.StreamCommunicator.readString;
import static rbdavis.server.StreamCommunicator.writeString;

public class LoadHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String respData = null;
        int responseCode = 0;
        int emptyBodyCode = 0;
        Response response = new Response();

        switch (exchange.getRequestMethod().toLowerCase()) {
            case "post":
                logger.info("Load request began");
                try {
                    InputStream reqBody = exchange.getRequestBody();
                    String reqData = readString(reqBody);

                    LoadRequest request = gson.fromJson(reqData, LoadRequest.class);
                    if (isValidLoadRequest(request)) {
                        response = new LoadService().load(request);
                        respData = gson.toJson(response);
                        responseCode = HttpURLConnection.HTTP_OK;

                        logger.info("Load successful");
                    }
                }
                catch (InvalidParameterException e) {
                    logger.warning(e.getMessage());
                    response.setMessage(e.getMessage());
                    responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
                    respData = gson.toJson(response);
                }
                catch (JsonParseException e) {
                    logger.warning("JSON syntax error in request");
                    response.setMessage("Error: Invalid JSON syntax. Please check your syntax");
                    responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
                    respData = gson.toJson(response);
                }
                catch (IOException e) {
                    logger.severe("Internal server error occurred " + e.getMessage());
                    response.setMessage("Error: An error occurred on our end. Sorry!");
                    responseCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
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
                    (e.getType() != Event.EventType.BIRTH && e.getType() != Event.EventType.BAPTISM &&
                     e.getType() != Event.EventType.MARRIAGE && e.getType() != Event.EventType.DEATH)) {

                    isValid = false;
                }
            }
        }
        catch (NullPointerException e) {
            throw new InvalidParameterException("Error: Request is missing a property or has invalid value.");
        }

        return isValid;
    }
}
