package rbdavis.server.database.sql.dataaccess;

public abstract class SqlDAO {

    String eventDoesNotExist(String id) {
        return "Event with id '" + id + "' does not exist";
    }

    String userIdDoesNotExist(String userId) {
        return "UserId '" + userId + "' does not exist";
    }

    String personIdDoesNotExist(String personId) {
        return "PersonId '" + personId + "' does not exist";
    }

    String tokenDoesNotExist(String token) {
        return "Token '" + token + "' does not exist";
    }

    String userHasNoTokens(String userId) {
        return "User with id '" + userId + "' has no tokens";
    }
}
