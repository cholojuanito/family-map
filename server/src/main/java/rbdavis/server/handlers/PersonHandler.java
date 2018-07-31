package rbdavis.server.handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.security.InvalidParameterException;

import rbdavis.server.services.PersonService;
import rbdavis.shared.models.http.requests.PersonRequest;
import rbdavis.shared.models.http.responses.Response;

import static rbdavis.shared.utils.StreamCommunicator.writeString;
import static rbdavis.shared.utils.Constants.AUTH;
import static rbdavis.shared.utils.Constants.GET;
import static rbdavis.shared.utils.Constants.INVALID_PROP_ERR;
import static rbdavis.shared.utils.Constants.METHOD_NOT_SUPPORTED;
import static rbdavis.shared.utils.Constants.PERSON_REQ_START;
import static rbdavis.shared.utils.Constants.PERSON_REQ_SUCCESS;
import static rbdavis.shared.utils.Constants.UNAUTHORIZED_REQ_ERR;
import static rbdavis.shared.utils.Constants.UNAUTHORIZED_REQ_LOG;

public class PersonHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String respData = null;
        int responseCode = 0;
        int emptyBodyCode = 0;
        Response response = new Response();

        switch (exchange.getRequestMethod().toLowerCase()) {
            case GET:
                try {
                    logger.info(PERSON_REQ_START);

                    String uri = exchange.getRequestURI().getPath();
                    int endURL = uri.length();
                    int afterLastBackSlash = uri.lastIndexOf("/") + 1;
                    String id = uri.substring(afterLastBackSlash, endURL);
                    if (id.length() == 0) {
                        throw new InvalidParameterException(INVALID_PROP_ERR);
                    }
                    Headers reqHeaders = exchange.getRequestHeaders();
                    PersonService service = new PersonService();
                    PersonRequest request;

                    // Verify the auth token
                    if (reqHeaders.containsKey(AUTH)) {
                        String clientTokenStr = reqHeaders.getFirst(AUTH);
                        if (service.isValidAuthToken(clientTokenStr)) {
                            // Create a request obj from the token
                            request = new PersonRequest(id, clientTokenStr);
                            // Call the service
                            response = service.findPerson(request);
                            respData = gson.toJson(response);
                            responseCode = HttpURLConnection.HTTP_OK;
                            logger.info(PERSON_REQ_SUCCESS);
                        }
                        else {
                            logger.warning(UNAUTHORIZED_REQ_LOG + "/person/[id]");
                            response.setMessage(UNAUTHORIZED_REQ_ERR);
                            responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
                            respData = gson.toJson(response);
                        }
                    }
                    else {
                        logger.warning(UNAUTHORIZED_REQ_LOG + "/person/[id]");
                        response.setMessage(UNAUTHORIZED_REQ_ERR);
                        responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
                        respData = gson.toJson(response);
                    }
                }
                catch (InvalidParameterException e) {
                    logger.warning(e.getMessage());
                    response.setMessage(e.getMessage());
                    responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
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
}
