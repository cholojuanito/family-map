package rbdavis.server.services;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import rbdavis.server.database.DAO;
import rbdavis.server.database.sql.SqlDatabase;
import rbdavis.server.database.sql.dataaccess.AuthTokenSqlDAO;
import rbdavis.shared.models.data.AuthToken;

import static rbdavis.shared.utils.Constants.DB_CLOSE_ERR;
import static rbdavis.shared.utils.Constants.INIT_LOG_ERR;
import static rbdavis.shared.utils.Constants.SERVICE_LOG;
import static rbdavis.shared.utils.Constants.SERVICE_LOG_PATH;
import static rbdavis.shared.utils.Constants.VALID_TOKEN;

public abstract class Service {
    static Logger logger;

    static {
        try {
            initLog();
        }
        catch (IOException e) {
            System.out.println(INIT_LOG_ERR + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void initLog() throws IOException {

        Level logLevel = Level.ALL;

        logger = Logger.getLogger(SERVICE_LOG);
        logger.setLevel(logLevel);
        logger.setUseParentHandlers(false);

        java.util.logging.FileHandler handlerFileHandler = new java.util.logging.FileHandler(SERVICE_LOG_PATH, false);
        handlerFileHandler.setLevel(logLevel);
        handlerFileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(handlerFileHandler);
    }

    public boolean isValidAuthToken(String clientToken) {
        boolean isValid = false;
        AuthToken token = null;
        SqlDatabase db = null;

        try {
            db = new SqlDatabase();
            AuthTokenSqlDAO authDao = db.getAuthTokenDao();

            token = authDao.findById(clientToken);
            if (token != null && !token.isExpired()) {
                logger.info(VALID_TOKEN);
                isValid = true;
            }

            db.endTransaction(false);
        }
        catch (DAO.DatabaseException e) {
            if (db != null) {
                try {
                    db.endTransaction(false);
                }
                catch (DAO.DatabaseException worthLessException) {
                    logger.severe(DB_CLOSE_ERR);
                }
            }
        }

        return isValid;
    }
}
