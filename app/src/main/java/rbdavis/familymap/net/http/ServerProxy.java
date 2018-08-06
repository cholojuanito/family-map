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

import rbdavis.familymap.models.App;
import rbdavis.shared.models.http.requests.EventsRequest;
import rbdavis.shared.models.http.requests.LoginRequest;
import rbdavis.shared.models.http.requests.PeopleRequest;
import rbdavis.shared.models.http.requests.RegisterRequest;
import rbdavis.shared.models.http.responses.EventsResponse;
import rbdavis.shared.models.http.responses.LoginOrRegisterResponse;
import rbdavis.shared.models.http.responses.PeopleResponse;
import rbdavis.shared.models.http.responses.Response;

import static rbdavis.shared.utils.StreamCommunicator.readString;
import static rbdavis.shared.utils.StreamCommunicator.writeString;
import static rbdavis.shared.utils.Constants.*;

public class ServerProxy {
    /*Singleton*/
    private static ServerProxy _instance;

    public static ServerProxy getInstance() {
        return _instance;
    }

    static {
        _instance = new ServerProxy();
    }

    private String hostName;
    private int portNum;
    private String token;
    private boolean isLoggedIn = false;

    private GsonBuilder gb = new GsonBuilder();
    private Gson gson = gb.create();

    private ServerProxy() {}

    public LoginOrRegisterResponse login(LoginRequest request) {
        LoginOrRegisterResponse response = new LoginOrRegisterResponse();

        try {
            HttpURLConnection connection = openHttpConnection(LOGIN_ENDPOINT, POST);
            sendRequestBody(connection, request);
            response = (LoginOrRegisterResponse) deserializeResponse(connection, LoginOrRegisterResponse.class);
        }
        catch (MalformedURLException e) {
            response.setMessage(INVALID_URL);
        }
        catch (IOException e) {
            response.setMessage(CONNECT_SERVER_ERR);
        }
        catch (Exception e) {
            //TODO: Log it
            response.setMessage("Error while deserializing response");
        }

        if (response.getAuthToken() != null) {
            setToken(response.getAuthToken());
            App.getInstance().setUserPersonId(response.getPersonID());
            isLoggedIn = true;
        }

        return response;
    }

    public LoginOrRegisterResponse register(RegisterRequest request) {
        LoginOrRegisterResponse response = new LoginOrRegisterResponse();

        try {
            HttpURLConnection connection = openHttpConnection(REGISTER_ENDPOINT, POST);
            sendRequestBody(connection, request);
            response = (LoginOrRegisterResponse) deserializeResponse(connection, LoginOrRegisterResponse.class);
        }
        catch (MalformedURLException e) {
            response.setMessage(INVALID_URL);
        }
        catch (IOException e) {
            response.setMessage(CONNECT_SERVER_ERR);
        }
        catch (Exception e) {
            //TODO: Log it
            response.setMessage("Error while deserializing response");
        }

        if (response.getAuthToken() != null) {
            setToken(response.getAuthToken());
            App.getInstance().setUserPersonId(response.getPersonID());
            isLoggedIn = true;
        }

        return response;
    }

    public PeopleResponse getAllPeople(PeopleRequest request) {
        PeopleResponse response = new PeopleResponse();

        try {
            HttpURLConnection connection = openHttpConnection(PEOPLE_ENDPOINT, GET, request.getToken());
            response = (PeopleResponse) deserializeResponse(connection, PeopleResponse.class);
        }
        catch (MalformedURLException e) {
            response.setMessage(INVALID_URL);
        }
        catch (IOException e) {
            response.setMessage(CONNECT_SERVER_ERR);
        }
        catch (Exception e) {
            //TODO: Log it
            response.setMessage("Error while deserializing response");
        }

        return response;
    }

    public EventsResponse getAllEvents(EventsRequest request) {
        EventsResponse response = new EventsResponse();

        try {
            HttpURLConnection connection = openHttpConnection(EVENTS_ENDPOINT, GET, request.getToken());
            response = (EventsResponse) deserializeResponse(connection, EventsResponse.class);
        }
        catch (MalformedURLException e) {
            response.setMessage(INVALID_URL);
        }
        catch (IOException e) {
            response.setMessage(CONNECT_SERVER_ERR);
        }
        catch (Exception e) {
            //TODO: Log it
            response.setMessage("Error while deserializing response");
        }

        return response;
    }

    private HttpURLConnection openHttpConnection(String endPoint, String requestMethod) throws MalformedURLException, IOException  {
        HttpURLConnection result = null;
        URL url = new URL(getUrlPrefix() + endPoint);
        result = (HttpURLConnection) url.openConnection();
        result.setRequestMethod(requestMethod);
        result.setDoOutput(true);
        result.connect();

        return result;
    }

    private HttpURLConnection openHttpConnection(String endPoint, String requestMethod, String authCode) throws MalformedURLException, IOException {
        HttpURLConnection result = null;
        URL url = new URL(getUrlPrefix() + endPoint);
        result = (HttpURLConnection) url.openConnection();
        result.setRequestMethod(requestMethod);
        result.setRequestProperty(AUTH, authCode);
        result.setDoOutput(false);
        result.connect();

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

    private Object deserializeResponse(HttpURLConnection connection, Class<?> theClass) throws Exception {
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
            return new Response("JSON syntax error while deserializing response");
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

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public String getUrlPrefix() {
        return HTTP + this.hostName + COLON + this.portNum;
    }

    public String getUrlLogin() {
        return getUrlPrefix() + LOGIN_ENDPOINT;
    }

    public String getUrlRegister() {
        return getUrlPrefix() + REGISTER_ENDPOINT;
    }

    public String getUrlPeople() {
        return getUrlPrefix() + PEOPLE_ENDPOINT;
    }

    public String getUrlEvents() {
        return getUrlPrefix() + EVENTS_ENDPOINT;
    }
}
