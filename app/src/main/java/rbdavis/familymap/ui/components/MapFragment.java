package rbdavis.familymap.ui.components;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import rbdavis.familymap.R;
import rbdavis.familymap.models.App;
import rbdavis.familymap.tasks.SyncDataTask;

public class MapFragment extends Fragment {


    private TextView textName;

    public MapFragment() {}

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
        textName = (TextView) v.findViewById(R.id.name);

        App model = App.getInstance();
        String usersName = App.getInstance().getUser().getFirstName() + " " + App.getInstance().getUser().getLastName() + " is logged in";
        textName.setText(usersName);

        return v;
    }
}
