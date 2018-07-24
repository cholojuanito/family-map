package rbdavis.server.handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.security.InvalidParameterException;

import rbdavis.server.services.FillService;
import rbdavis.shared.models.http.requests.FillRequest;
import rbdavis.shared.models.http.responses.Response;

import static rbdavis.server.StreamCommunicator.writeString;

public class FillHandler extends Handler implements HttpHandler{

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String respData = null;
        int responseCode = 0;
        int emptyBodyCode = 0;
        Response response = new Response();
        final int UN_INDEX = 2;
        final int NUM_GENS_INDEX = 3;

        switch (exchange.getRequestMethod().toLowerCase()) {
            case "post":
                try {
                    logger.info("Fill request began");

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
                                throw new InvalidParameterException("Error: Cannot generate a negative number of generations");
                            }
                            break;
                        default:
                        throw new InvalidParameterException("Error: Request property missing or has invalid value.");
                    }

                    FillRequest request = new FillRequest(userName, numGens);
                    response = new FillService().fill(request);

                    responseCode = HttpURLConnection.HTTP_OK;
                    respData = gson.toJson(response);
                    logger.info("Fill request successful");
                }
                catch (InvalidParameterException e) {
                    logger.warning(e.getMessage());
                    response.setMessage(e.getMessage());
                    responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
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
}
