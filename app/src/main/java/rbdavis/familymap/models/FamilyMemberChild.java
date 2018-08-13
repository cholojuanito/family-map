package rbdavis.familymap.models;

import rbdavis.shared.models.data.Gender;

/*
 * A class that serves as a data holder for the list view portion of
 * the Expandable Recycler View class on the PersonActivity
 */

public class FamilyMemberChild {
    private String id;
    private Gender gender;
    private String name;
    private String relation;

    public FamilyMemberChild(String id, Gender gender, String name, String relation) {
        this.id = id;
        this.gender = gender;
        this.name = name;
        this.relation = relation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
