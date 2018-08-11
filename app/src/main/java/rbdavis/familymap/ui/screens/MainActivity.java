package rbdavis.familymap.ui.screens;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import rbdavis.familymap.R;
import rbdavis.familymap.net.http.ServerProxy;
import rbdavis.familymap.ui.components.LoginFragment;
import rbdavis.familymap.ui.components.MapFragment;
import rbdavis.familymap.ui.components.SyncDataProgressFragment;
import rbdavis.shared.utils.Constants;

public class MainActivity extends AppCompatActivity implements LoginFragment.Callback, SyncDataProgressFragment.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar appBar = (Toolbar) findViewById(R.id.main_appbar);
        setSupportActionBar(appBar);

        FragmentManager fragManager = getSupportFragmentManager();
        if (!ServerProxy.getInstance().isLoggedIn()) {
            LoginFragment loginFrag = LoginFragment.newInstance(MainActivity.this);
            loginFrag.setHideAppBarListener(new LoginFragment.HideAppBarListener() {
                @Override
                public void hideAppBar() {
                    getSupportActionBar().hide();
                }
            });
            fragManager.beginTransaction().add(R.id.frag_container, loginFrag).commit();
        }
        else {
            MapFragment mapFrag = new MapFragment();
            mapFrag.setShowAppBarListener(new MapFragment.ShowAppBarListener() {
                @Override
                public void showAppBar() {
                    getSupportActionBar().show();
                }
            });
            fragManager.beginTransaction().add(R.id.frag_container, mapFrag).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu_options, menu);
        return true;
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
        LoginFragment loginFrag = LoginFragment.newInstance(MainActivity.this);
        loginFrag.setHideAppBarListener(new LoginFragment.HideAppBarListener() {
            @Override
            public void hideAppBar() {
                getSupportActionBar().hide();
            }
        });

        Bundle bundle = new Bundle();
        bundle.putString(Constants.SYNC_ERROR_KEY, "Error occurred while syncing your data. Please try again");

        loginFrag.setArguments(bundle);
        fragManager.beginTransaction().replace(R.id.frag_container, loginFrag).commit();
    }

    @Override
    public void onSynced(String... messages) {
        if (android.os.Debug.isDebuggerConnected()) {
            android.os.Debug.waitForDebugger();
        }

        FragmentManager fragManager = getSupportFragmentManager();
        MapFragment mapFrag = MapFragment.newInstance();
        mapFrag.setShowAppBarListener(new MapFragment.ShowAppBarListener() {
            @Override
            public void showAppBar() {
                getSupportActionBar().show();
            }
        });
        Bundle bundle = new Bundle();
        // Add stuff to bundle if you need
        fragManager.beginTransaction().replace(R.id.frag_container, mapFrag).commit();
    }
}
