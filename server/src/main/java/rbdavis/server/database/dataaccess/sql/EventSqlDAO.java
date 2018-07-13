package rbdavis.server.database.dataaccess.sql;

import rbdavis.server.database.dataaccess.DAO;
import rbdavis.shared.models.Event;

import java.util.List;

public class EventSqlDAO implements DAO<Event>
{
    @Override
    public Event create(Event event)
    {
        return event;
    }

    @Override
    public Event update(String id, Event event)
    {
        return event;
    }

    @Override
    public boolean delete(String id)
    {
        return false;
    }

    @Override
    public Event find(String id)
    {
        return null;
    }

    @Override
    public List<Event> findAll()
    {
        return null;
    }
}
