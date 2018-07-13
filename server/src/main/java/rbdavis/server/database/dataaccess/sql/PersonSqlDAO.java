package rbdavis.server.database.dataaccess.sql;

import rbdavis.server.database.dataaccess.DAO;
import rbdavis.shared.models.Person;

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
