package rbdavis.server.services;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import rbdavis.server.database.DAO;
import rbdavis.server.database.sql.SqlDatabase;
import rbdavis.server.database.sql.dataaccess.AuthTokenSqlDAO;
import rbdavis.shared.models.data.AuthToken;
import static rbdavis.shared.utils.Constants.*;

public abstract class Service {
    static Logger logger;

    static {
        try {
            initLog();
        }
        catch (IOException e) {
            System.out.println("Could not initialize log: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void initLog() throws IOException {
        final String handlerLogPath = "server" + File.separator + "logs" + File.separator + "services.txt";

        Level logLevel = Level.ALL;

        logger = Logger.getLogger("service");
        logger.setLevel(logLevel);
        logger.setUseParentHandlers(false);

        java.util.logging.FileHandler handlerFileHandler = new java.util.logging.FileHandler(handlerLogPath, false);
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
