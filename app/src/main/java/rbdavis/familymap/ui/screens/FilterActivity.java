package rbdavis.familymap.ui.screens;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rbdavis.familymap.R;
import rbdavis.familymap.models.App;
import rbdavis.familymap.ui.adapters.FilterAdapter;

public class FilterActivity extends AppCompatActivity {

    private RecyclerView filtersView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Toolbar appBar = (Toolbar) findViewById(R.id.filters_appbar);
        setSupportActionBar(appBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        List<String> filters = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : App.getInstance().getFilters().getFilterOptions().entrySet()) {
            filters.add(entry.getKey());
        }

        filtersView = (RecyclerView) findViewById(R.id.recycler_filter);
        filtersView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        FilterAdapter filterAdapt = new FilterAdapter(filters);
        filtersView.setAdapter(filterAdapt);
    }
}
