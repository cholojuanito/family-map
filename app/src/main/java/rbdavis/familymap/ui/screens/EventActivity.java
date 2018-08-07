package rbdavis.familymap.ui.screens;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import rbdavis.familymap.R;
import rbdavis.familymap.models.App;
import rbdavis.familymap.ui.components.MapFragment;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String eventId = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            eventId = extras.getString(getString(R.string.eventKey));
            String type = App.getInstance().getEvents().get(eventId).getEventType();
            getSupportActionBar().setTitle(type + " event");
        }

        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.eventKey), eventId);

        FragmentManager fragManager = getSupportFragmentManager();
        MapFragment mapFrag = new MapFragment();
        mapFrag.setArguments(bundle);

        fragManager.beginTransaction().add(R.id.event_frag_container, mapFrag).commit();

    }
}
