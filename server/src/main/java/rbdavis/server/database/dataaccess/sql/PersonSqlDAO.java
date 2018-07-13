package rbdavis.server.database.dataaccess.sql;

import database.dataaccess.DAO;
import models.Person;

import java.util.List;

public class PersonSqlDAO extends SqlDAO implements DAO<Person>
{
    @Override
    public Person create(Person type)
    {
        return null;
    }

    @Override
    public Person update(String id, Person type)
    {
        return null;
    }

    @Override
    public boolean delete(String id)
    {
        return false;
    }

    @Override
    public Person find(String id)
    {
        return null;
    }

    @Override
    public List<Person> findAll()
    {
        return null;
    }
}
