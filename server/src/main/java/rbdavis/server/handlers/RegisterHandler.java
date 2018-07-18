package rbdavis.server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.rmi.server.ExportException;

import rbdavis.shared.models.http.requests.RegisterRequest;
import rbdavis.shared.models.http.responses.Response;

import static rbdavis.server.StreamCommunicator.*;

public class RegisterHandler implements HttpHandler {

    private GsonBuilder gsonBuild = new GsonBuilder();
    private Gson gson = gsonBuild.create();

    public void handle(HttpExchange exchange) throws IOException {
        String respData = null;
        int responseCode = 0;
        int emptyBodyCode = 0;
        Response errorResponse;

            switch (exchange.getRequestMethod().toLowerCase()) {
                case "post":
                    try {
                        // Read request body
                        InputStream reqBody = exchange.getRequestBody();
                        String reqData = readString(reqBody);

                        // Make a RegisterRequest obj
                        RegisterRequest request = gson.fromJson(reqData, RegisterRequest.class);

                        // Write response body
                        respData = gson.toJson(request);
                        responseCode = HttpURLConnection.HTTP_OK;
                        // TODO: Log here
                    }
                    catch (JsonParseException e) {
                        errorResponse = new Response("Error occurred while reading JSON. Please check your syntax");
                        responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
                        respData = gson.toJson(errorResponse);
                        // TODO: Log here
                    }
                    catch (IOException e) {
                        errorResponse = new Response("An error occurred on our end. Sorry!");
                        responseCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
                        respData = gson.toJson(errorResponse);
                        // TODO: Log here
                    }

                    break;

                default:
                    errorResponse = new Response(exchange.getRequestMethod() + " is not supported for this URL");
                    responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
                    respData = gson.toJson(errorResponse);
                    // TODO: Log here
                    break;
            }

        exchange.sendResponseHeaders(responseCode, emptyBodyCode);
        OutputStream respBody = exchange.getResponseBody();
        writeString(respData, respBody);
        respBody.close();

    }

}
