package rbdavis.server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import rbdavis.server.services.ClearService;
import rbdavis.shared.models.http.responses.Response;

import static rbdavis.server.StreamCommunicator.writeString;

public class ClearHandler implements HttpHandler {

    private GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
    private Gson gson = gsonBuilder.create();

    public void handle(HttpExchange exchange) throws IOException {
        String respData = null;
        int responseCode = 0;
        int emptyBodyCode = 0;
        Response errorResponse;
        Response successResponse;

        switch (exchange.getRequestMethod().toLowerCase()) {
            case "post":
                ClearService clearService = new ClearService();
                successResponse = clearService.clear();
                responseCode = HttpURLConnection.HTTP_OK;
                respData = gson.toJson(successResponse);
                break;
            default:
                errorResponse = new Response(exchange.getRequestMethod() + " method is not supported for this URL");
                responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
                respData = gson.toJson(errorResponse);
                break;
        }

        exchange.sendResponseHeaders(responseCode, emptyBodyCode);
        OutputStream respBody = exchange.getResponseBody();
        writeString(respData, respBody);
        respBody.close();
    }
}
