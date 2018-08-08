package rbdavis.familymap.ui.screens;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import rbdavis.familymap.R;
import rbdavis.familymap.models.App;
import rbdavis.familymap.ui.components.MapFragment;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Toolbar appBar = (Toolbar) findViewById(R.id.event_appbar);
        setSupportActionBar(appBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        String eventId = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            eventId = extras.getString(getString(R.string.eventKey));
            String title = App.getInstance().getEvents().get(eventId).getEventType() + " event";

            TextView appBarTitle = (TextView) findViewById(R.id.event_appbar_title);
            appBarTitle.setText(title);
        }

        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.eventKey), eventId);

        FragmentManager fragManager = getSupportFragmentManager();
        MapFragment mapFrag = new MapFragment();
        mapFrag.setArguments(bundle);

        fragManager.beginTransaction().add(R.id.event_frag_container, mapFrag).commit();

    }
}
