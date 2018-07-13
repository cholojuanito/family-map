package rbdavis.server.database.dataaccess.sql;

import database.dataaccess.DAO;
import models.User;

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
    public User find(String id)
    {
        return null;
    }

    @Override
    public List<User> findAll()
    {
        return null;
    }
}
