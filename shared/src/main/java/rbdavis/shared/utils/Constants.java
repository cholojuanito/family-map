package rbdavis.shared.utils;

import java.io.File;

import javax.print.DocFlavor;

public class Constants {
    // Basic Strings
    public static final String ERR = "Error: ";
    public static final String SUCCESS = "Success! ";
    public static final String AUTH = "Authorization";

    // File Paths
    public static final String WEB_ROOT_DIR = "server" + File.separator + "web" + File.separator;
    public static final String ROOT_INDEX = WEB_ROOT_DIR + "index.html";
    public static final String FOUR_OH_FOUR = WEB_ROOT_DIR + "HTML" + File.separator + "404.html";
    public static final String FIVE_HUNDRED = WEB_ROOT_DIR + "HTML" + File.separator + "500.html";

    // Log Paths
    public static final String HANDLER_LOG = "handler";
    public static final String HANDLER_LOG_PATH = "server" + File.separator + "logs" + File.separator + "handlers.txt";
    public static final String MOCK_DATA_PATH = "shared/src/mockdata/";
    public static final String M_NAMES_PATH =  MOCK_DATA_PATH + "mnames.json";
    public static final String F_NAMES_PATH = MOCK_DATA_PATH + "fnames.json";
    public static final String LAST_NAMES_PATH = MOCK_DATA_PATH + "snames.json";
    public static final String LOCATIONS_PATH = MOCK_DATA_PATH + "locations.json";

    // Http Methods
    public static final String GET = "get";
    public static final String POST = "post";
    public static final String PATCH = "patch";
    public static final String DELETE = "delete";

    // Error Messages
    public static final String METHOD_NOT_SUPPORTED = " method is not supported for this URL";
    public static final String INVALID_PROP_ERR = ERR + "Request property missing or has invalid value ";
    public static final String UNAUTHORIZED_REQ_ERR = ERR + "You are not authorized to access this URL";
    public static final String TOKEN_EXPIRED = "Your session has ended. You must login in again";
    public static final String JSON_SYNTAX_ERR = ERR + "Invalid JSON syntax. Please check your syntax";
    public static final String INTERNAL_SERVER_ERR = ERR + "Whoops! Looks like something broke on our end. Sorry!";

    // API Specific Messages
    public static final String CLEAR_ERR = ERR + "Something happened while trying to clear database ";
    public static final String CLEAR_SUCCESS = "Clear succeeded";
    public static final String FILL_NEG_ERR = ERR + "Cannot generate a negative number of generations";


    // Logger Messages
    public static final String INIT_LOG_ERR = "Could not initialize log: ";
    public static final String FILE_SENT = " file was sent";
    public static final String INTERNAL_SERVER_ERR_LOG = "Internal server error occurred ";
    public static final String FOUR_OH_FOUR_ERR = "Unable to find file at ";
    public static final String UNAUTHORIZED_REQ_LOG = "Unauthorized request to: ";

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





}
