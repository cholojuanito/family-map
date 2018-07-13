package rbdavis.server.database.dataaccess.sql;

import rbdavis.server.database.dataaccess.DAO;
import rbdavis.shared.models.User;

import java.util.List;

public class UserSqlDAO implements DAO<User>
{
    @Override
    public User create(User user)
    {
        return user;
    }

    @Override
    public User update(String id, User user)
    {
        return user;
    }

    @Override
    public boolean delete(String id)
    {
        return false;
    }

    @Override
    public User findById(String id)
    {
        return null;
    }

    @Override
    public List<User> findAll()
    {
        return null;
    }
}
