package rbdavis.familymap.ui.screens;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rbdavis.familymap.R;
import rbdavis.familymap.models.App;
import rbdavis.familymap.models.SearchResult;
import rbdavis.familymap.net.http.ServerProxy;
import rbdavis.familymap.ui.adapters.SearchAdapter;
import rbdavis.familymap.ui.components.LoginFragment;
import rbdavis.familymap.ui.components.MapFragment;
import rbdavis.familymap.ui.components.SyncDataProgressFragment;
import rbdavis.shared.models.data.Gender;
import rbdavis.shared.utils.Constants;

public class MainActivity extends AppCompatActivity implements LoginFragment.Callback, SyncDataProgressFragment.Callback,
                                                               SearchView.OnQueryTextListener, SearchAdapter.OnSearchItemClickListener {

    private SearchAdapter searchAdapter;
    private RecyclerView searchRecyclerView;
    private SearchView searchView;
    private TextView noSearchResultsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar appBar = (Toolbar) findViewById(R.id.main_appbar);
        setSupportActionBar(appBar);

        searchRecyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
        noSearchResultsView = (TextView) findViewById(R.id.empty_view);

        searchRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        searchAdapter = new SearchAdapter(MainActivity.this);
        searchAdapter.setResults(new ArrayList<SearchResult>());
        searchRecyclerView.setAdapter(searchAdapter);

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

    /****** SEARCH METHODS ******/
    /*
     * I implemented the App's search functionality as a SearchView
     * by following Google's tutorial on SearchViews
     *
     * This way I didn't have to make a new activity just for
     * searching purposes
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu_options, menu);

        MenuItem searchItem = menu.findItem(R.id.search);

        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search your family tree...");
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                searchView.onActionViewExpanded();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchRecyclerView.setVisibility(View.GONE);
                noSearchResultsView.setVisibility(View.GONE);
                searchView.onActionViewCollapsed();
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        if (query != null && !query.equals("")) {
            List<SearchResult> results = App.getInstance().search(query.toLowerCase());
            if (results.size() > 0) {
                searchAdapter.setResults(results);
                //searchAdapter.setResults(results);
                noSearchResultsView.setVisibility(View.GONE);
                searchRecyclerView.setVisibility(View.VISIBLE);
            }
            else {
                searchRecyclerView.setVisibility(View.GONE);
                noSearchResultsView.setVisibility(View.VISIBLE);
            }
        }
        else {
            searchRecyclerView.setVisibility(View.GONE);
            noSearchResultsView.setVisibility(View.VISIBLE);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onSearchItemClick(View v, String id, int type) {
        startNewActivity(id, type);
    }

    private void startNewActivity(String id, int type) {
        switch (type) {
            case SearchResult.PERSON_RESULT:
                Intent personIntent = new Intent(this, PersonActivity.class);
                personIntent.putExtra(getString(R.string.personKey), id);
                startActivity(personIntent);
                searchRecyclerView.setVisibility(View.GONE);
                break;

            case SearchResult.EVENT_RESULT:
                Intent eventIntent = new Intent(this, EventActivity.class);
                eventIntent.putExtra(getString(R.string.eventKey), id);
                startActivity(eventIntent);
                searchRecyclerView.setVisibility(View.GONE);
                break;
        }
    }
    /****** END SEARCH METHODS ******/

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
