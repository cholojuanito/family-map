package rbdavis.shared.utils;


public class Constants {
    // Basic Strings
    public static final String ERR = "Error: ";
    public static final String SUCCESS = "Success!";
    public static final String AUTH = "Authorization";

    // File Paths
    public static final String WEB_ROOT_DIR = "server/web";
    public static final String ROOT_INDEX = WEB_ROOT_DIR + "/index.html";
    public static final String FOUR_OH_FOUR = WEB_ROOT_DIR + "/HTML/404.html";
    public static final String FIVE_HUNDRED = WEB_ROOT_DIR + "/HTML/500.html";

    // Log Paths
    public static final String SERVER_LOG = "server";
    public static final String HANDLER_LOG = "handler";
    public static final String SERVICE_LOG = "service";
    public static final String DAO_LOG = "database";


    public static final String SERVER_LOG_PATH = "server/logs/server.txt";
    public static final String HANDLER_LOG_PATH = "server/logs/handlers.txt";
    public static final String SERVICE_LOG_PATH = "server/logs/services.txt";
    public static final String DAO_LOG_PATH = "server/logs/dataaccess.txt";

    public static final String MOCK_DATA_PATH = "shared/src/mockdata/";
    public static final String M_NAMES_PATH = MOCK_DATA_PATH + "mnames.json";
    public static final String F_NAMES_PATH = MOCK_DATA_PATH + "fnames.json";
    public static final String LAST_NAMES_PATH = MOCK_DATA_PATH + "snames.json";
    public static final String LOCATIONS_PATH = MOCK_DATA_PATH + "locations.json";

    // Http Methods
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PATCH = "PATCH";
    public static final String DELETE = "DELETE";

    // Http Url Construction
    public static final String HTTP = "http://";
    public static final String COLON = ":";

    // Http Error Messages
    public static final String INVALID_URL = "Invalid URL. Please check the URL and try again.";
    public static final String CONNECT_SERVER_ERR = "Unable to connect to server";

    // Success Messages
    public static final String LOGIN_SUCCESS = "Still coming back eh?";
    public static final String REG_SUCCESS = "Welcome! I've just sucked one year of your life away";

    // Endpoints
    public static final String LOGIN_ENDPOINT = "/user/login";
    public static final String REGISTER_ENDPOINT = "/user/register";
    public static final String PEOPLE_ENDPOINT = "/person";
    public static final String EVENTS_ENDPOINT = "/event";

    //  Handler Error Messages
    public static final String METHOD_NOT_SUPPORTED = " method is not supported for this URL";
    public static final String INVALID_PROP_ERR = ERR + "Request property missing or has invalid value ";
    public static final String UNAUTHORIZED_REQ_ERR = ERR + "You are not authorized to access this URL";
    public static final String TOKEN_EXPIRED = "Your session has ended. You must login in again";
    public static final String JSON_SYNTAX_ERR = ERR + "Invalid JSON syntax. Please check your syntax";
    public static final String INTERNAL_SERVER_ERR = ERR + "Whoops! Looks like something broke on our end. Sorry!";

    // Service Messages
    public static final String CLEAR_ERR = ERR + "Unable to clear database";
    public static final String MOCK_DATA_FILES_MIA = "Unable to open files with mock data";
    public static final String CLEAR_SUCCESS = "Clear succeeded";
    public static final String FILL_NEG_ERR = ERR + "Cannot generate a negative number of generations";
    public static final String INVALID_USERNAME_ERR = ERR + "Username unrecognized";
    public static final String INCORRECT_USERNAME = "That username does not match our records. Please sign up first";
    public static final String INCORRECT_PASS = "Incorrect password";
    public static final String DB_CLOSE_ERR = "Issue closing db connection";
    public static final String NO_RECORDS_ERR = ERR + "Unable to find any records";
    public static final String NOT_THEIRS = " does not belong to your family tree";


    // DAO Messages
    public static final String DB_CONN_OPENED = "Database connection opened";
    public static final String DB_CONN_CLOSED = "Database connection closed";
    public static final String DB_CONN_FAILED = "Database connection failed ";

    public static final String INVALID_SQL = "Invalid SQL syntax";
    public static final String TOKEN_TAKEN = "Token is already taken";
    public static final String EVENT_ID_TAKEN = "Event id is already taken";
    public static final String PERSON_ID_TAKEN = "Person id is already taken";
    public static final String USER_ID_TAKEN = "Username is already taken";

    public static final String CREATE_AUTH_FAIL = "Create auth token failed ";
    public static final String CREATE_USER_FAIL = "Create user failed ";
    public static final String CREATE_PERSON_FAIL = "Create person failed ";
    public static final String CREATE_EVENT_FAIL = "Create event failed ";

    public static final String UPDATE_USER_FAIL = "Update user failed ";
    public static final String UPDATE_PERSON_FAIL = "Update person failed ";
    public static final String UPDATE_EVENT_FAIL = "Update event failed ";

    public static final String DELETE_AUTH_FAIL = "Delete auth token failed ";
    public static final String DELETE_USER_FAIL = "Delete user failed ";
    public static final String DELETE_PERSON_FAIL = "Delete person failed ";
    public static final String DELETE_EVENT_FAIL = "Delete event failed ";

    public static final String DELETE_AUTHS_FAIL = "Delete all auth tokens failed ";
    public static final String DELETE_USERS_FAIL = "Delete all users failed ";
    public static final String DELETE_PEOPLE_FAIL = "Delete all people failed ";
    public static final String DELETE_EVENTS_FAIL = "Delete all events failed ";

    public static final String DELETE_BY_USER_PERSON_FAIL = "Delete people by user_id failed ";
    public static final String DELETE_BY_USER_EVENT_FAIL = "Delete events by user_id failed ";

    public static final String FIND_BY_PERSON_EVENTS_FAIL = "Find all events by person_id failed ";

    public static final String FIND_AUTH_FAIL = "Find auth token by id failed ";
    public static final String FIND_USER_FAIL = "Find user by id failed ";
    public static final String FIND_PERSON_FAIL = "Find person by id failed ";
    public static final String FIND_EVENT_FAIL = "Find event by id failed ";

    public static final String FIND_BY_USER_AUTH_FAIL = "Find auth token by user_id failed ";
    public static final String FIND_BY_USER_PERSON_FAIL = "Find person by user_id failed ";
    public static final String FIND_BY_USER_EVENT_FAIL = "Find event by user_id failed ";

    public static final String FIND_AUTHS_FAIL = "Find all auth tokens failed ";
    public static final String FIND_USERS_FAIL = "Find all users failed ";
    public static final String FIND_PEOPLE_FAIL = "Find all people failed ";
    public static final String FIND_EVENTS_FAIL = "Find all events failed ";


    // Logger Messages
    public static final String INIT_SERVER = "Initializing HTTP Server";
    public static final String START_SERVER = "Starting HTTP server";
    public static final String SERVER_LISTENING_ON = "Server listening on port: ";
    public static final String SERVER_NO_PORT_ERR = ERR + "Server attempted to start without port #.";
    public static final String INIT_LOG_ERR = "Could not initialize log: ";
    public static final String FILE_SENT = " file was sent";
    public static final String INTERNAL_SERVER_ERR_LOG = "Internal server error occurred ";
    public static final String FOUR_OH_FOUR_ERR = "Unable to find file at ";
    public static final String UNAUTHORIZED_REQ_LOG = "Unauthorized request to: ";
    public static final String VALID_TOKEN = "Valid token found";

    //Log Messages for Database API's
    public static final String FILL_REQ_START = "Fill request began";
    public static final String FILL_REQ_SUCCESS = "Fill request successful";
    public static final String LOAD_REQ_START = "Load request began";
    public static final String LOAD_REQ_SUCCESS = "Load request successful";

    // Log Messages for User API's
    public static final String EVENT_REQ_START = "Event request began";
    public static final String EVENT_REQ_SUCCESS = "Event request successful";
    public static final String EVENTS_REQ_START = "Events request began";
    public static final String EVENTS_REQ_SUCCESS = "Events request successful";
    public static final String PERSON_REQ_START = "Person request began";
    public static final String PERSON_REQ_SUCCESS = "Person request successful";
    public static final String PEOPLE_REQ_START = "People request began";
    public static final String PEOPLE_REQ_SUCCESS = "People request successful";
    public static final String LOGIN_REQ_START = "Login request began";
    public static final String LOGIN_REQ_SUCCESS = "Login request successful";
    public static final String REG_REQ_START = "Register request began";
    public static final String REG_REQ_SUCCESS = "Register request successful";
    public static final String REG_REQ_UNSUCCESS = "Registration unsuccessful";
}
