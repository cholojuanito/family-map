package rbdavis.server.database.dataaccess.sql;

import database.dataaccess.DAO;
import models.AuthToken;

import java.util.List;

public class AuthTokenSqlDAO implements DAO<AuthToken>
{
    @Override
    public AuthToken create(AuthToken token)
    {
        return token;
    }

    @Override
    public AuthToken update(String id, AuthToken token)
    {
        return token;
    }

    @Override
    public boolean delete(String id)
    {
        return false;
    }

    @Override
    public AuthToken find(String id)
    {
        return null;
    }

    @Override
    public List<AuthToken> findAll()
    {
        return null;
    }
}
