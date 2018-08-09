package rbdavis.familymap.ui.components;

import android.content.Intent;
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
import rbdavis.familymap.ui.screens.FilterActivity;
import rbdavis.familymap.ui.screens.PersonActivity;
import rbdavis.familymap.ui.screens.SearchActivity;
import rbdavis.familymap.ui.screens.SettingsActivity;
import rbdavis.shared.models.data.Event;
import rbdavis.shared.models.data.Gender;
import rbdavis.shared.models.data.Person;
import rbdavis.shared.utils.Constants;

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

    public MapFragment() {
        focusedEventId = null;
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
            addMarkers(model, model.hasFilters());
        }

        if (focusedEventId != null) {
            Marker marker = model.getEventsToMarkers().get(focusedEventId);
            focusOnEvent(marker.getPosition());
            showEventForMarker(marker);
            if (model.getSettings().isShowLines()) {
                drawMapLines(marker);
            }
        }

        // TODO Check for setting/filter stuff

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                focusOnEvent(marker.getPosition());
                showEventForMarker(marker);
                if (App.getInstance().getSettings().isShowLines()) {
                    drawMapLines(marker);
                }
                return true;
            }
        });

        infoPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonActivity.class);
                intent.putExtra(getString(R.string.personKey), App.getInstance().getFocusedPersonId());
                startActivity(intent);
            }
        });

        setMapType(model);
    }

    private void setMapType(App model) {
        if (model.getSettings().getMapTypeOptions().get(Constants.NORMAL)) {
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        else if (model.getSettings().getMapTypeOptions().get(Constants.HYBRID)) {
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }
        else {
            map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
    }

    // TODO: Setup settings/filter then handle this stuff.
    private void drawMapLines(Marker marker) {
        App model = App.getInstance();
        String eventId = model.getMarkersToEvents().get(marker);
        Event event = model.getEvents().get(eventId);
        Person person = model.getPeople().get(event.getPersonId());
        LatLng currLatLng = marker.getPosition();

        model.resetLines();

        drawLifeStoryLines(model, currLatLng, person);

        float initialLineWidth = 10;
        determineAncestorLinesToDraw(model, currLatLng, person, initialLineWidth);

        drawSpouseLines(model, currLatLng, person);

    }

    private void drawLifeStoryLines(App model, LatLng currLatLng, Person person) {
        List<Event> personalEvents = model.getPersonalEvents().get(person.getId());
        Map<String, Boolean> filters = model.getFilters().getFilterOptions();

        if (model.getSettings().isShowLifeStoryLines()) {
            for (Event e : personalEvents) {
                // If not filtered
                if (filters.get(e.getEventType())) {
                    double lat = Double.parseDouble(e.getLatitude());
                    double lng = Double.parseDouble(e.getLongitude());
                    LatLng eventLatLng = new LatLng(lat, lng);
                    Polyline line = addLine(currLatLng, eventLatLng);

                    // Get color from settings
                    for (Map.Entry<Integer, Boolean> entry : model.getSettings().getLifeStoryOptions().entrySet()) {
                        if (entry.getValue()) {
                            line.setColor(entry.getKey());
                        }
                    }

                    model.getConnections().add(line);
                }
            }
        }
    }

    private void determineAncestorLinesToDraw(App model, LatLng currLatLng, Person person, float lineWidth) {
        Map<String, Boolean> filters = model.getFilters().getFilterOptions();
        if (model.getSettings().isShowAncestorsLines()) {
            if (person.getFatherId() != null && filters.get(Constants.BY_FATHER_SIDE) &&
                    filters.get(Constants.BY_MALE)) {

                drawAncestorLine(model, currLatLng, person, lineWidth);
            }

            if (person.getMotherId() != null && filters.get(Constants.BY_MOTHER_SIDE) &&
                    filters.get(Constants.BY_FEMALE)) {

                drawAncestorLine(model, currLatLng, person, lineWidth);
            }
        }
    }

    private void drawAncestorLine(App model, LatLng currLatLng, Person person, float lineWidth) {
        List<Event> ancestorPersonalEvents = model.getPersonalEvents().get(person.getId());
        Map<String, Boolean> filters = model.getFilters().getFilterOptions();

        Event ancestorEvent = findBestUnfilteredEvent(ancestorPersonalEvents);

        LatLng newLatLng = null;
        if (ancestorEvent != null) {
            addAncestorLine(model, currLatLng, ancestorEvent, lineWidth);

            double newLat = Double.parseDouble(ancestorEvent.getLatitude());
            double newLng = Double.parseDouble(ancestorEvent.getLongitude());
            newLatLng = new LatLng(newLat, newLng);
        }

        lineWidth--;

        if (person.getFatherId() != null && filters.get(Constants.BY_MALE)) {
            Person father = model.getPeople().get(person.getFatherId());
            drawAncestorLine(model, newLatLng, father, lineWidth);
        }

        if (person.getMotherId() != null && filters.get(Constants.BY_FEMALE)) {
            Person mother = model.getPeople().get(person.getMotherId());
            drawAncestorLine(model, newLatLng, mother, lineWidth);
        }

    }

    private void addAncestorLine(App model, LatLng currLatLng, Event ancestorEvent, float lineWidth) {
        double lat = Double.parseDouble(ancestorEvent.getLatitude());
        double lng = Double.parseDouble(ancestorEvent.getLongitude());
        LatLng eventLatLng = new LatLng(lat, lng);
        Polyline line = addLine(currLatLng, eventLatLng);
        line.setWidth(lineWidth);

        // Get color from settings
        for (Map.Entry<Integer, Boolean> entry : model.getSettings().getAncestorsOptions().entrySet()) {
            if (entry.getValue()) {
                line.setColor(entry.getKey());
            }
        }

        model.getConnections().add(line);
    }

    private void drawSpouseLines(App model, LatLng currLatLng, Person person) {
        if (person.getSpouseId() != null && model.getSettings().isShowSpouseLines()) {
            List<Event> spousePersonalEvents = model.getPersonalEvents().get(person.getSpouseId());
            Map<String, Boolean> filters = model.getFilters().getFilterOptions();

            Event spouseEvent = findBestUnfilteredEvent(spousePersonalEvents);

            if (spouseEvent != null) {
                if (person.getGender() == Gender.M && (filters.get(Constants.BY_FEMALE))) {
                    addSpouseLine(model, currLatLng, spouseEvent);
                }
                else if (person.getGender() == Gender.F && (filters.get(Constants.BY_MALE))) {
                    addSpouseLine(model, currLatLng, spouseEvent);
                }
            }
        }
    }

    private void addSpouseLine(App model, LatLng currLatLng, Event spouseEvent) {
        double lat = Double.parseDouble(spouseEvent.getLatitude());
        double lng = Double.parseDouble(spouseEvent.getLongitude());
        LatLng eventLatLng = new LatLng(lat, lng);
        Polyline line = addLine(currLatLng, eventLatLng);

        // Get color from settings
        for (Map.Entry<Integer, Boolean> entry : model.getSettings().getSpouseOptions().entrySet()) {
            if (entry.getValue()) {
                line.setColor(entry.getKey());
            }
        }

        model.getConnections().add(line);

    }

    private Event findBestUnfilteredEvent(List<Event> personalEvents) {
        Map<String, Boolean> filters = App.getInstance().getFilters().getFilterOptions();
        for (Event e : personalEvents) {
            if (filters.get(e.getEventType())) {
                return e;
            }
        }
        return null;
    }

    private Polyline addLine(LatLng position, LatLng position2) {
        return map.addPolyline(
                new PolylineOptions()
                .clickable(false)
                .add(position, position2)
        );
    }

    private void addMarkers(App model, boolean useFilters) {

        Map<Marker, String> eventMarkers = model.getMarkersToEvents();
        Map<String, Marker> eventsToMarkers = model.getEventsToMarkers();
        Map<Marker, String> personMarkers = model.getPersonMarkers();

        eventMarkers.clear();
        personMarkers.clear();

        Map<String, Event> eventsToShow = (useFilters) ? model.getFilteredEvents() : model.getEvents();

        for (Map.Entry<String, Event> entry : eventsToShow.entrySet()) {
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

    private void showEventForMarker(Marker marker) {
        String eventId = App.getInstance().getMarkersToEvents().get(marker);
        Event event = App.getInstance().getEvents().get(eventId);
        Person person = App.getInstance().getPeople().get(event.getPersonId());
        String name = person.getFirstName() + " " + person.getLastName();
        String dateStr = event.getEventType() + ": " + event.getDateHappened().format(EVENT_FORMATTER);
        String locationStr = event.getCity() + ", " + event.getCountry();

        setFocusedEventId(eventId);
        App.getInstance().setFocusedPersonId(person.getId());

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
//                Intent searchIntent = new Intent(getContext(), SearchActivity.class);
//                startActivity(searchIntent);
                return true;
            case R.id.filter:
                Intent filterIntent = new Intent(getContext(), FilterActivity.class);
                startActivity(filterIntent);
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

    public interface ShowAppBarListener {

        void showAppBar();
    }

    public void setShowAppBarListener(ShowAppBarListener showAppBarListener) {
        this.showAppBarListener = showAppBarListener;
    }
}
