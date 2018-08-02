package rbdavis.familymap.ui.screens;


import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import rbdavis.familymap.R;
import rbdavis.familymap.net.http.ServerProxy;
import rbdavis.familymap.ui.components.LoginFragment;
import rbdavis.familymap.ui.components.MapFragment;
import rbdavis.familymap.ui.components.SyncDataProgressFragment;

public class MainActivity extends AppCompatActivity implements LoginFragment.Callback, SyncDataProgressFragment.Callback {

    private static final String SYNC_ERROR_KEY = "syncError";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragManager = getSupportFragmentManager();
        if (!ServerProxy.getInstance().isLoggedIn()) {
            LoginFragment loginFrag = LoginFragment.newInstance(MainActivity.this);
            fragManager.beginTransaction().add(R.id.frag_container, loginFrag).commit();

        }
        else {
            MapFragment mapFrag = new MapFragment();
            fragManager.beginTransaction().add(R.id.frag_container, mapFrag).commit();
        }
    }

    @Override
    public void onLogin() {
        FragmentManager fragManager = getSupportFragmentManager();
        SyncDataProgressFragment syncDataFrag = SyncDataProgressFragment.newInstance(MainActivity.this);
        Bundle bundle = new Bundle();
        // Add stuff to bundle if you need
        fragManager.beginTransaction().replace(R.id.frag_container, syncDataFrag).commit();
    }

    @Override
    public void onSyncError(String... messages) {
        FragmentManager fragManager = getSupportFragmentManager();
        MapFragment mapFrag = MapFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString(SYNC_ERROR_KEY, "Error occurred while syncing the data");

        mapFrag.setArguments(bundle);
        fragManager.beginTransaction().replace(R.id.frag_container, mapFrag).commit();
    }

    @Override
    public void onSynced(String... messages) {
        FragmentManager fragManager = getSupportFragmentManager();
        MapFragment mapFrag = MapFragment.newInstance();
        Bundle bundle = new Bundle();
        // Add stuff to bundle if you need
        fragManager.beginTransaction().replace(R.id.frag_container, mapFrag).commit();
    }
}
