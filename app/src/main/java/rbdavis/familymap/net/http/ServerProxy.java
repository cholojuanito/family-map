package rbdavis.familymap.net.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import rbdavis.shared.models.http.requests.EventsRequest;
import rbdavis.shared.models.http.requests.LoginRequest;
import rbdavis.shared.models.http.requests.PeopleRequest;
import rbdavis.shared.models.http.requests.RegisterRequest;
import rbdavis.shared.models.http.responses.EventsResponse;
import rbdavis.shared.models.http.responses.LoginOrRegisterResponse;
import rbdavis.shared.models.http.responses.PeopleResponse;

import static rbdavis.shared.utils.StreamCommunicator.readString;
import static rbdavis.shared.utils.StreamCommunicator.writeString;
import static rbdavis.shared.utils.Constants.*;

public class ServerProxy {
    private static ServerProxy _instance = new ServerProxy();

    public static ServerProxy getInstance() {
        return _instance;
    }

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT_NUMBER = 8080;
    //TODO: Change URL PREFIX to just http://
    private static final String URL_PREFIX = "http://" + SERVER_HOST + ":" + SERVER_PORT_NUMBER;

    private String hostName;
    private int portNum;
    private String token;
    private GsonBuilder gb = new GsonBuilder();
    private Gson gson = gb.create();

    private ServerProxy() {}

    public LoginOrRegisterResponse login(LoginRequest request) {
        LoginOrRegisterResponse response = null;
        HttpURLConnection connection = openHttpConnection(LOGIN_ENDPOINT, POST);
        sendRequestBody(connection, request);
        response = (LoginOrRegisterResponse) deserializeResponse(connection, LoginOrRegisterResponse.class);

        return response;
    }

    public LoginOrRegisterResponse register(RegisterRequest request) {
        LoginOrRegisterResponse response = null;
        HttpURLConnection connection = openHttpConnection(REGISTER_ENDPOINT, POST);
        sendRequestBody(connection, request);
        response = (LoginOrRegisterResponse) deserializeResponse(connection, LoginOrRegisterResponse.class);

        return response;
    }

    public PeopleResponse getAllPeople(PeopleRequest request) {
        PeopleResponse response = null;
        HttpURLConnection connection = openHttpConnection(PEOPLE_ENDPOINT, GET, request.getToken());
        response = (PeopleResponse) deserializeResponse(connection, PeopleResponse.class);


        return response;
    }

    public EventsResponse getAllEvents(EventsRequest request) {
        EventsResponse response = null;
        HttpURLConnection connection = openHttpConnection(EVENTS_ENDPOINT, GET, request.getToken());
        response = (EventsResponse) deserializeResponse(connection, EventsResponse.class);


        return response;
    }

    private HttpURLConnection openHttpConnection(String endPoint, String requestMethod) {
        HttpURLConnection result = null;
        try {
            //String urlStr = URL_PREFIX + hostName + ":" + portNum + endPoint;
            URL url = new URL(URL_PREFIX + endPoint);
            result = (HttpURLConnection) url.openConnection();
            result.setRequestMethod(requestMethod);
            result.setDoOutput(true);
            result.connect();
        }
        catch (MalformedURLException e) {
            //TODO: Log it
            e.printStackTrace();
        }
        catch (IOException e) {
            //TODO: Log it
            e.printStackTrace();
        }

        return result;
    }

    private HttpURLConnection openHttpConnection(String endPoint, String requestMethod, String authCode) {
        HttpURLConnection result = null;
        try {
            //String urlStr = URL_PREFIX + hostName + ":" + portNum + endPoint;
            URL url = new URL(URL_PREFIX + endPoint);
            result = (HttpURLConnection) url.openConnection();
            result.setRequestMethod(requestMethod);
            result.setRequestProperty(AUTH, authCode);
            result.setDoOutput(false);
            result.connect();
        }
        catch (MalformedURLException e) {
            //TODO: Log it
            e.printStackTrace();
        }
        catch (IOException e) {
            //TODO: Log it
            e.printStackTrace();
        }

        return result;
    }

    private void sendRequestBody(HttpURLConnection connection, Object request) {
        try {
            OutputStream os = connection.getOutputStream();
            String reqData = gson.toJson(request);
            writeString(reqData, os);
            os.close();
        }
        catch (IOException e) {
            //TODO: Log it
            e.printStackTrace();
        }
    }

    private Object deserializeResponse(HttpURLConnection connection, Class<?> theClass) {
        Object result = null;
        try {
            switch (connection.getResponseCode()) {
                case HttpURLConnection.HTTP_OK:

                    //0 means the body was empty
                    if (connection.getContentLength() == 0) {
                        System.out.println("Response body was empty");
                    }
                    else if (connection.getContentLength() == -1) {

                        //-1 means the body was not empty but has an unknown amount of information
                        InputStream respBody = connection.getInputStream();
                        String respData = readString(respBody);
                        result = gson.fromJson(respData, theClass);
                        respBody.close();
                    }
                    break;

                case HttpURLConnection.HTTP_BAD_REQUEST:
                    //TODO: Log it
                    InputStream errorResp = connection.getErrorStream();
                    String respData = readString(errorResp);
                    result = gson.fromJson(respData, theClass);
                    errorResp.close();
                    break;

                default:
                    //TODO: Log it
                    throw new Exception("HTTP code " + connection.getResponseCode());
            }
        }
        catch (JsonSyntaxException | JsonIOException | IOException e) {
            //TODO: Log it
            System.out.println("Error in deserializeResponse");
            e.printStackTrace();
        }
        catch (Exception e) {
            //TODO: Log it
            System.out.println("Error in deserializeResponse");
            e.printStackTrace();
        }

        return result;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPortNum() {
        return portNum;
    }

    public void setPortNum(int portNum) {
        this.portNum = portNum;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
