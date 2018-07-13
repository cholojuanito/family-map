package rbdavis.server.database.dataaccess;

import java.util.List;

public interface DAO<T>
{
    T create(T type);
    T update(String id, T type);
    boolean delete(String id);
    T find(String id);
    List<T> findAll();

    class DatabaseException extends Exception
    {
        public DatabaseException() { super(); }
        public DatabaseException(String message) { super(message); }
        public DatabaseException(String message, Throwable cause) { super(message, cause); }
        public DatabaseException(Throwable cause) { super(cause); }
    }
}
