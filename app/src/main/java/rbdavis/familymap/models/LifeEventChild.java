package rbdavis.familymap.models;

/*
 * A class that serves as a data holder for the list view portion of
 * the Expandable Recycler View class on the PersonActivity
 */

public class LifeEventChild {
    private String id;
    private String info;

    public LifeEventChild(String id, String info) {
        this.id = id;
        this.info = info;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
