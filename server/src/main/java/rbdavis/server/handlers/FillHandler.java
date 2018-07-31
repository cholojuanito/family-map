package rbdavis.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.security.InvalidParameterException;

import rbdavis.server.services.FillService;
import rbdavis.shared.models.http.requests.FillRequest;
import rbdavis.shared.models.http.responses.Response;

import static rbdavis.shared.utils.StreamCommunicator.writeString;
import static rbdavis.shared.utils.Constants.FILL_NEG_ERR;
import static rbdavis.shared.utils.Constants.FILL_REQ_START;
import static rbdavis.shared.utils.Constants.FILL_REQ_SUCCESS;
import static rbdavis.shared.utils.Constants.INVALID_PROP_ERR;
import static rbdavis.shared.utils.Constants.METHOD_NOT_SUPPORTED;
import static rbdavis.shared.utils.Constants.POST;

public class FillHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String respData = null;
        int responseCode = 0;
        int emptyBodyCode = 0;
        Response response = new Response();
        final int UN_INDEX = 2;
        final int NUM_GENS_INDEX = 3;

        switch (exchange.getRequestMethod().toLowerCase()) {
            case POST:
                try {
                    logger.info(FILL_REQ_START);

                    // Get the "username" and "numGenerations" portions of the uri
                    String uri = exchange.getRequestURI().getPath();
                    String[] uriSections = uri.split("/");
                    String userName;
                    int numGens = 4;
                    switch (uriSections.length) {
                        case 3:
                            userName = uriSections[UN_INDEX];
                            break;
                        case 4:
                            userName = uriSections[UN_INDEX];
                            numGens = Integer.parseInt(uriSections[NUM_GENS_INDEX]);
                            if (numGens < 0) {
                                throw new InvalidParameterException(FILL_NEG_ERR);
                            }
                            break;
                        default:
                            throw new InvalidParameterException(INVALID_PROP_ERR);
                    }

                    FillRequest request = new FillRequest(userName, numGens);
                    response = new FillService().fill(request);

                    responseCode = HttpURLConnection.HTTP_OK;
                    respData = gson.toJson(response);
                    logger.info(FILL_REQ_SUCCESS);
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
