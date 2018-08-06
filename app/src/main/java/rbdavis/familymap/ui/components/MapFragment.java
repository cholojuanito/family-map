package rbdavis.familymap.ui.components;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
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

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import rbdavis.familymap.R;
import rbdavis.familymap.models.App;
import rbdavis.familymap.models.MapMarkerColor;
import rbdavis.shared.models.data.Event;
import rbdavis.shared.models.data.Gender;
import rbdavis.shared.models.data.Person;

public class MapFragment extends Fragment implements OnMapReadyCallback{
    private final DateTimeFormatter EVENT_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");


    private GoogleMap map;
    private RelativeLayout infoPanel;
    private ImageView genderIcon;
    private TextView personName;
    private TextView date;
    private TextView location;


    private String focusedEvent;
    private Map<Marker, String> eventMarkers;
    private Map<Marker, String> personMarkers;

    public MapFragment() {
        focusedEvent = null;
        eventMarkers = new HashMap<>();
        personMarkers = new HashMap<>();
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            
        }

        genderIcon = (ImageView) v.findViewById(R.id.icon_gender);
        personName = (TextView) v.findViewById(R.id.person_name);
        date = (TextView) v.findViewById(R.id.date);
        location = (TextView) v.findViewById(R.id.location);

        infoPanel = (RelativeLayout) v.findViewById(R.id.info_panel);

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

        // TODO Check for setting/filter stuff

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                focusMapOnEvent(marker.getPosition());
                showEventForMarker(marker);
                return true;
            }
        });

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }

    private void addMarkers(App model) {

        eventMarkers.clear();
        personMarkers.clear();

        for (Map.Entry<String, Event> entry : model.getEvents().entrySet()) {
            Event event = entry.getValue();
            double lat = Double.parseDouble(event.getLatitude());
            double lng = Double.parseDouble(event.getLongitude());
            LatLng location = new LatLng(lat, lng);

            MapMarkerColor markerColor = App.getInstance().getEventTypeColors().get(event.getEventType());

            Marker marker = map.addMarker(
                    new MarkerOptions()
                     .position(location)
                     .title(event.getEventType())
                     .icon(BitmapDescriptorFactory.defaultMarker(markerColor.getValue()))
            );

            if (event.getPersonId().equals(model.getUserPersonId()) && event.getEventType().equals("Birth")) {
                focusMapOnEvent(marker.getPosition());
            }

            eventMarkers.put(marker, entry.getKey());
            personMarkers.put(marker, model.getPeople().get(event.getPersonId()).getId());
        }
    }

    private void showEventForMarker(Marker marker) {
        String eventId = eventMarkers.get(marker);
        Event event = App.getInstance().getEvents().get(eventId);
        Person person = App.getInstance().getPeople().get(event.getPersonId());
        String name = person.getFirstName() + " " + person.getLastName();
        String dateStr = event.getEventType() + ": " + event.getDateHappened().format(EVENT_FORMATTER);
        String locationStr = event.getCity() + ", " + event.getCountry();

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

    private void focusMapOnEvent(LatLng position) {
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(position, 3);
        map.animateCamera(location);
    }
}
