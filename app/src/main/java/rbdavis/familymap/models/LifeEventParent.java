package rbdavis.familymap.models;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

/*
 * A class that serves as a data holder for the dropdown portion of
 * the Expandable Recycler View class on the PersonActivity
 */

public class LifeEventParent implements Parent<LifeEventChild> {

    private List<LifeEventChild> eventList;

    public LifeEventParent(List<LifeEventChild> eventList) {
        this.eventList = eventList;
    }

    @Override
    public List<LifeEventChild> getChildList() {
        return eventList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
