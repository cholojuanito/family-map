package rbdavis.server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpURLConnection;

import rbdavis.shared.models.http.requests.LoadRequest;
import rbdavis.shared.models.http.responses.Response;

public class LoadHandler implements HttpHandler {

    private GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
    private Gson gson = gsonBuilder.create();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String respData = null;
        int responseCode = 0;
        int emptyBodyCode = 0;
        Response errorResponse;
        Response successResponse;

        switch (exchange.getRequestMethod().toLowerCase()) {
            case "post":
                break;
            default:
                errorResponse = new Response(exchange.getRequestMethod() + " method is not supported for this URL");
                responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
                respData = gson.toJson(errorResponse);
                break;
        }
    }

    private boolean isValidLoadRequest(LoadRequest request) throws NullPointerException {

        return false;
    }
}
