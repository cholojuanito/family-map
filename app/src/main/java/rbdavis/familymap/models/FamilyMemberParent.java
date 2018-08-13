package rbdavis.familymap.models;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

/*
 * A class that serves as a data holder for the list view portion of
 * the Expandable Recycler View class on the PersonActivity
 */

public class FamilyMemberParent implements Parent<FamilyMemberChild> {
    List<FamilyMemberChild> memberList;

    public FamilyMemberParent(List<FamilyMemberChild> memberList) {
        this.memberList = memberList;
    }

    @Override
    public List<FamilyMemberChild> getChildList() {
        return memberList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
