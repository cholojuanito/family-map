package rbdavis.familymap.ui.components;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import rbdavis.familymap.R;
import rbdavis.familymap.models.App;
import rbdavis.familymap.models.MapMarkerColor;
import rbdavis.familymap.ui.screens.PersonActivity;
import rbdavis.familymap.ui.screens.SettingsActivity;
import rbdavis.shared.models.data.Event;
import rbdavis.shared.models.data.Gender;
import rbdavis.shared.models.data.Person;

public class MapFragment extends Fragment implements OnMapReadyCallback{
    private final DateTimeFormatter EVENT_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");

    private ShowAppBarListener showAppBarListener;

    private GoogleMap map;
    private RelativeLayout infoPanel;
    private ImageView genderIcon;
    private TextView personName;
    private TextView date;
    private TextView location;


    private String focusedEventId;
    private String focusedPersonId;

    public MapFragment() {
        focusedEventId = null;
        focusedPersonId = null;
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            setFocusedEventId(getArguments().getString(getString(R.string.eventKey)));
        }

        genderIcon = (ImageView) v.findViewById(R.id.icon_gender);
        personName = (TextView) v.findViewById(R.id.person_name);
        date = (TextView) v.findViewById(R.id.date);
        location = (TextView) v.findViewById(R.id.location);
        infoPanel = (RelativeLayout) v.findViewById(R.id.info_panel);

        if (showAppBarListener != null) {
            showAppBarListener.showAppBar();
        }

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        if(map != null) {
            onMapReady(map);
        }

    }

    public void onMapReady(GoogleMap googleMap) {
        App model = App.getInstance();
        map = googleMap;
        if (map != null) {
            map.clear();
        }

        if (model.getEvents() != null) {
            addMarkers(model);
        }

        if (focusedEventId != null) {
            Marker marker = model.getEventsToMarkers().get(focusedEventId);
            focusOnEvent(marker.getPosition());
            showEventForMarker(marker);
        }

        // TODO Check for setting/filter stuff

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                focusOnEvent(marker.getPosition());
                showEventForMarker(marker);
                updateModelInfo(marker);
                //drawMapLines(marker);
                return true;
            }
        });

        infoPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonActivity.class);
                intent.putExtra(getString(R.string.personKey), focusedPersonId);
                startActivity(intent);
            }
        });

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }

    // TODO: Setup settings/filter then handle this stuff.
    private void drawMapLines(Marker marker) {
        App model = App.getInstance();
        String eventId = model.getMarkersToEvents().get(marker);
        Event event = model.getEvents().get(eventId);
        Person person = model.getPeople().get(event.getPersonId());
        List<Event> personEvents = model.getPersonalEvents().get(person.getId());
        LatLng currLatLng = marker.getPosition();
        MapMarkerColor lineColor = model.getEventTypeColors().get(event.getEventType());

        model.getConnections().clear();

        for (Event e : personEvents) {
            double lat = Double.parseDouble(e.getLatitude());
            double lng = Double.parseDouble(e.getLongitude());
            LatLng eventLatLng = new LatLng(lat, lng);

             Polyline line = addLine(currLatLng, eventLatLng, Color.RED);
             model.getConnections().add(line);
        }

    }

    private Polyline addLine(LatLng position, LatLng position2, int color) {
        return map.addPolyline(
                new PolylineOptions()
                .clickable(false)
                .add(position, position2)
                .color(color)
        );
    }

    private void addMarkers(App model) {

        Map<Marker, String> eventMarkers = model.getMarkersToEvents();
        Map<String, Marker> eventsToMarkers = model.getEventsToMarkers();
        Map<Marker, String> personMarkers = model.getPersonMarkers();

        eventMarkers.clear();
        personMarkers.clear();

        for (Map.Entry<String, Event> entry : model.getEvents().entrySet()) {
            Event event = entry.getValue();
            double lat = Double.parseDouble(event.getLatitude());
            double lng = Double.parseDouble(event.getLongitude());
            LatLng location = new LatLng(lat, lng);

            MapMarkerColor markerColor = model.getEventTypeColors().get(event.getEventType());

            Marker marker = map.addMarker(
                    new MarkerOptions()
                     .position(location)
                     .title(event.getEventType())
                     .icon(BitmapDescriptorFactory.defaultMarker(markerColor.getValue()))
            );

            eventMarkers.put(marker, entry.getKey());
            eventsToMarkers.put(entry.getKey(), marker);
            personMarkers.put(marker, event.getPersonId());
        }
    }

    // TODO Should this be async?
    private void updateModelInfo(Marker marker) {
        String personId = App.getInstance().getPersonMarkers().get(marker);
        Map<String, Person> people = App.getInstance().getPeople();

        for (Map.Entry<String, Person> entry : people.entrySet()) {
            Person p = entry.getValue();

        }
    }

    private void showEventForMarker(Marker marker) {
        String eventId = App.getInstance().getMarkersToEvents().get(marker);
        Event event = App.getInstance().getEvents().get(eventId);
        Person person = App.getInstance().getPeople().get(event.getPersonId());
        String name = person.getFirstName() + " " + person.getLastName();
        String dateStr = event.getEventType() + ": " + event.getDateHappened().format(EVENT_FORMATTER);
        String locationStr = event.getCity() + ", " + event.getCountry();

        setFocusedEventId(eventId);
        setFocusedPersonId(person.getId());

        if (person.getGender() == Gender.M) {
            genderIcon.setImageResource(R.drawable.ic_male);
        }
        else {
            genderIcon.setImageResource(R.drawable.ic_female);
        }

        personName.setText(name);
        date.setText(dateStr);
        location.setText(locationStr);

        infoPanel.setVisibility(View.VISIBLE);
    }

    private void focusOnEvent(LatLng position) {
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(position, 3);
        map.animateCamera(location);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent settingsIntent = new Intent(getContext(), SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.search:

                return true;
            case R.id.filter:

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getFocusedEventId() {
        return focusedEventId;
    }

    public void setFocusedEventId(String focusedEventId) {
        this.focusedEventId = focusedEventId;
    }

    public String getFocusedPersonId() {
        return focusedPersonId;
    }

    public void setFocusedPersonId(String focusedPersonId) {
        this.focusedPersonId = focusedPersonId;
    }

    public interface ShowAppBarListener {

        void showAppBar();
    }

    public void setShowAppBarListener(ShowAppBarListener showAppBarListener) {
        this.showAppBarListener = showAppBarListener;
    }
}
