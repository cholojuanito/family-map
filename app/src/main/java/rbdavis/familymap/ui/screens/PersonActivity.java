package rbdavis.familymap.ui.screens;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rbdavis.familymap.R;
import rbdavis.familymap.models.App;
import rbdavis.familymap.models.FamilyMemberChild;
import rbdavis.familymap.models.FamilyMemberParent;
import rbdavis.familymap.models.LifeEventChild;
import rbdavis.familymap.models.LifeEventParent;
import rbdavis.familymap.ui.adapters.FamilyMemberAdapter;
import rbdavis.familymap.ui.adapters.LifeEventAdapter;
import rbdavis.shared.models.data.Event;
import rbdavis.shared.models.data.Gender;
import rbdavis.shared.models.data.Person;

public class PersonActivity extends AppCompatActivity {

    private TextView firstNameLabel;
    private TextView firstNameText;
    private TextView lastNameLabel;
    private TextView lastNameText;
    private TextView genderLabel;
    private TextView genderText;

    private RecyclerView lifeEventsRecyclerView;
    private RecyclerView familyMemberRecyclerView;

    private String personId;
    private List<LifeEventParent> lifeEventParents; // There is only one of these but the interface requires a List
    private List<LifeEventChild> lifeEvents;
    private List<FamilyMemberParent> familyMemberParents;
    private List<FamilyMemberChild> familyMembers;

    public PersonActivity() {
        this.personId = null;
        // TODO See if these need to be linked lists
        this.lifeEventParents = new ArrayList<>();
        this.lifeEvents = new ArrayList<>();
        this.familyMemberParents = new ArrayList<>();
        this. familyMembers = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);


        App model = App.getInstance();
        if (savedInstanceState != null) {
            setPersonId(savedInstanceState.getString(getString(R.string.personKey)));
        }
        else {
            setPersonId(getIntent().getStringExtra(getString(R.string.personKey)));
        }

        addEventsToList(model);
        addFamilyMembersToList(model);

        firstNameLabel = (TextView) findViewById(R.id.first_name_label);
        firstNameText = (TextView) findViewById(R.id.first_name);
        lastNameLabel = (TextView) findViewById(R.id.last_name_label);
        lastNameText = (TextView) findViewById(R.id.last_name);
        genderLabel = (TextView) findViewById(R.id.gender_label);
        genderText = (TextView) findViewById(R.id.gender);

        lifeEventsRecyclerView = (RecyclerView) findViewById(R.id.life_events);
        familyMemberRecyclerView = (RecyclerView) findViewById(R.id.family_members);

        Person focusedPerson = model.getPeople().get(personId);
        setDefaultTexts(focusedPerson);

        Toolbar appBar = (Toolbar) findViewById(R.id.person_appbar);
        setSupportActionBar(appBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView appBarTitle = (TextView) findViewById(R.id.person_appbar_title);
        String title = focusedPerson.getFirstName() + " " + focusedPerson.getLastName() + "'s Info";
        appBarTitle.setText(title);

        setupAdapters();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                returnToMainActivity();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void returnToMainActivity() {
        Intent mainIntent = new Intent(PersonActivity.this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        finish();
    }

    private void setDefaultTexts(Person person) {
        firstNameText.setText(person.getFirstName());
        lastNameText.setText(person.getLastName());
        String genderStr = (person.getGender() == Gender.M) ? "Male" : "Female";
        genderText.setText(genderStr);
    }

    private void setupAdapters() {

        // TODO CHANGE THE ICON TO THE OPPOSITE ON A PARENT CLICK

        LifeEventAdapter lifeEventAdapter = new LifeEventAdapter(this, lifeEventParents);
        lifeEventAdapter.setChildClickListener(new LifeEventAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(View v, String id) {
                startNewEventActivity(id);
            }
        });

        FamilyMemberAdapter familyMemberAdapter = new FamilyMemberAdapter(this, familyMemberParents);
        familyMemberAdapter.setChildClickListener(new FamilyMemberAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(View v, String id) {
                startNewPersonActivity(id);
            }
        });

        lifeEventsRecyclerView.setAdapter(lifeEventAdapter);
        lifeEventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        familyMemberRecyclerView.setAdapter(familyMemberAdapter);
        familyMemberRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void addEventsToList(App model) {

        lifeEventParents.clear();
        lifeEvents.clear();

        List<Event> events = model.getPersonalEvents().get(personId);

        for (Event e : events) {
            String info = e.getEventType() + ": " + e.getCity() + ", " + e.getCountry() +
                    " (" + e.getDateHappened().getYear() + ")";
            LifeEventChild lifeEvent = new LifeEventChild(e.getId(), info);
            lifeEvents.add(lifeEvent);
        }

        lifeEventParents.add(new LifeEventParent(lifeEvents));
    }

    private void addFamilyMembersToList(App model) {

        familyMemberParents.clear();
        familyMembers.clear();

        Map<String, Person> people = model.getPeople();
        Person currPerson = people.get(personId);
        Person father = people.get(currPerson.getFatherId());
        Person mother = people.get(currPerson.getMotherId());
        Person spouse = people.get(currPerson.getSpouseId());
        List<Person> children = model.getChildrenOfPerson().get(personId);

        if (father != null) {
            String fatherName = father.getFirstName() + " " + father.getLastName();
            String relationship = "Father";
            FamilyMemberChild fatherMember = new FamilyMemberChild(father.getId(), father.getGender(), fatherName, relationship);
            familyMembers.add(fatherMember);
        }

        if (mother != null) {
            String motherName = mother.getFirstName() + " " + mother.getLastName();
            String relationship = "Mother";
            FamilyMemberChild motherMember = new FamilyMemberChild(mother.getId(), mother.getGender(), motherName, relationship);
            familyMembers.add(motherMember);
        }

        if (spouse != null) {
            String spouseName = spouse.getFirstName() + " " + spouse.getLastName();
            String relationship = "Spouse";
            FamilyMemberChild spouseMember = new FamilyMemberChild(spouse.getId(), spouse.getGender(), spouseName, relationship);
            familyMembers.add(spouseMember);
        }

        if (children != null) {
            for (Person child : children) {
                String childName = child.getFirstName() + " " + child.getLastName();
                String relationship = child.getGender() == Gender.M ? "Son" : "Daughter";
                FamilyMemberChild childMember = new FamilyMemberChild(child.getId(), child.getGender(), childName, relationship);
                familyMembers.add(childMember);
            }
        }

        familyMemberParents.add(new FamilyMemberParent(familyMembers));
    }

    private void startNewPersonActivity(String id) {
        Intent intent = new Intent(this, PersonActivity.class);
        intent.putExtra(getString(R.string.personKey), id);
        startActivity(intent);
    }

    private void startNewEventActivity(String id) {
        Intent intent = new Intent(this, EventActivity.class);
        intent.putExtra(getString(R.string.eventKey), id);
        startActivity(intent);
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }
}
