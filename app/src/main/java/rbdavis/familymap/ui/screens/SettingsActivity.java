package rbdavis.familymap.ui.screens;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Map;

import rbdavis.familymap.R;
import rbdavis.familymap.models.App;
import rbdavis.familymap.models.Settings;
import rbdavis.familymap.ui.components.MapFragment;
import rbdavis.familymap.ui.components.SyncDataProgressFragment;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, SyncDataProgressFragment.Callback {

    private Switch allLinesSwitch;
    private Switch lifeStorySwitch;
    private Switch ancestorsSwitch;
    private Switch spouseSwitch;
    private Spinner lifeStorySpinner;
    private Spinner ancestorsSpinner;
    private Spinner spouseSpinner;
    private Spinner mapTypeSpinner;
    private Button syncButton;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar appBar = (Toolbar) findViewById(R.id.settings_appbar);
        setSupportActionBar(appBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        allLinesSwitch = (Switch) findViewById(R.id.all_lines_switch);
        lifeStorySwitch = (Switch) findViewById(R.id.life_story_switch);
        ancestorsSwitch = (Switch) findViewById(R.id.ancestor_switch);
        spouseSwitch = (Switch) findViewById(R.id.spouse_switch);
        lifeStorySpinner = (Spinner) findViewById(R.id.life_story_spinner);
        ancestorsSpinner = (Spinner) findViewById(R.id.ancestor_spinner);
        spouseSpinner = (Spinner) findViewById(R.id.spouse_spinner);
        mapTypeSpinner = (Spinner) findViewById(R.id.map_spinner);
        syncButton = (Button) findViewById(R.id.sync_button);
        logoutButton = (Button) findViewById(R.id.logout_button);


        App model = App.getInstance();

        initSwitches(model);
        initSpinnerAdapters(model);
        initButtons(model);
    }

    private void initSwitches(final App model) {
        allLinesSwitch.setChecked(model.getSettings().isShowLines());
        lifeStorySwitch.setChecked(model.getSettings().isShowLifeStoryLines());
        ancestorsSwitch.setChecked(model.getSettings().isShowAncestorsLines());
        spouseSwitch.setChecked(model.getSettings().isShowSpouseLines());

        allLinesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                model.getSettings().setShowLines(isChecked);

                if (!isChecked) {
                    model.getSettings().setShowLifeStoryLines(false);
                    model.getSettings().setShowAncestorsLines(false);
                    model.getSettings().setShowSpouseLines(false);

                    lifeStorySwitch.setChecked(false);
                    ancestorsSwitch.setChecked(false);
                    spouseSwitch.setChecked(false);
                }
            }
        });

        lifeStorySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                model.getSettings().setShowLifeStoryLines(isChecked);

                if (!model.getSettings().isShowLines() && isChecked) {
                    model.getSettings().setShowLines(true);
                    allLinesSwitch.setChecked(true);
                }
            }
        });

        ancestorsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                model.getSettings().setShowAncestorsLines(isChecked);

                if (!model.getSettings().isShowLines() && isChecked) {
                    model.getSettings().setShowLines(true);
                    allLinesSwitch.setChecked(true);
                }
            }
        });

        spouseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                model.getSettings().setShowSpouseLines(isChecked);

                if (!model.getSettings().isShowLines() && isChecked) {
                    model.getSettings().setShowLines(true);
                    allLinesSwitch.setChecked(true);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                returnToMainActivity();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void returnToMainActivity() {
        Intent mainIntent = new Intent(SettingsActivity.this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        finish();
    }

    private void initSpinnerAdapters(App model) {
        ArrayAdapter<CharSequence> mapTypeAdapter = ArrayAdapter.createFromResource(this, R.array.map_types, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> linesAdapter = ArrayAdapter.createFromResource(this, R.array.line_options, android.R.layout.simple_spinner_item);

        mapTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        linesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mapTypeSpinner.setAdapter(mapTypeAdapter);
        lifeStorySpinner.setAdapter(linesAdapter);
        ancestorsSpinner.setAdapter(linesAdapter);
        spouseSpinner.setAdapter(linesAdapter);

        mapTypeSpinner.setOnItemSelectedListener(this);
        lifeStorySpinner.setOnItemSelectedListener(this);
        ancestorsSpinner.setOnItemSelectedListener(this);
        spouseSpinner.setOnItemSelectedListener(this);

        mapTypeSpinner.setSelection(model.getSettings().getSelectedMapTypeIndex());
        lifeStorySpinner.setSelection(model.getSettings().getSelectedLifeStoryIndex());
        ancestorsSpinner.setSelection(model.getSettings().getSelectedAncestorsIndex());
        spouseSpinner.setSelection(model.getSettings().getSelectedSpouseIndex());
    }

    private void initButtons(App model) {
        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sync();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void sync() {
        App.getInstance().resetModel(true);

        FragmentManager fragManager = getSupportFragmentManager();
        SyncDataProgressFragment syncDataFrag = SyncDataProgressFragment.newInstance(SettingsActivity.this);
        Bundle bundle = new Bundle();
        // Add stuff to bundle if you need
        fragManager.beginTransaction().replace(R.id.frag_container_settings, syncDataFrag).commit();
    }

    @Override
    public void onSyncError(String... messages) {
        //"Error occurred while syncing your data. Please try again"
        FragmentManager fragManager = getSupportFragmentManager();
        SyncDataProgressFragment syncDataFrag = SyncDataProgressFragment.newInstance(SettingsActivity.this);
        fragManager.beginTransaction().remove(syncDataFrag).commit();

        Toast
        .makeText(SettingsActivity.this, messages[0], Toast.LENGTH_LONG)
        .show();
    }

    @Override
    public void onSynced(String... messages) {

        if (android.os.Debug.isDebuggerConnected()) {
            android.os.Debug.waitForDebugger();
        }

        Intent intent = new Intent( SettingsActivity.this , MainActivity.class);
        startActivity(intent);

    }

    private void logout() {
        App.getInstance().performLogout();

        App model = App.getInstance();

        Intent intent = new Intent( SettingsActivity.this , MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        App model = App.getInstance();

        switch (parent.getId()) {
            case R.id.map_spinner:
                for(Map.Entry<String, Boolean> entry : model.getSettings().getMapTypeOptions().entrySet()){
                    entry.setValue(false);
                }

                model.getSettings().getMapTypeOptions().put(parent.getSelectedItem().toString(), true);
                model.getSettings().setSelectedMapTypeIndex(position);
                break;

            case R.id.life_story_spinner:
                for(Map.Entry<Integer, Boolean> entry : model.getSettings().getLifeStoryOptions().entrySet()){
                    entry.setValue(false);
                }
                String lifeStoryColor = parent.getSelectedItem().toString();
                if (lifeStoryColor.equals(getString(R.string.green))) {
                    model.getSettings().getLifeStoryOptions().put(Color.GREEN, true);
                }
                else if (lifeStoryColor.equals(getString(R.string.red))) {
                    model.getSettings().getLifeStoryOptions().put(Color.RED, true);
                }
                else {
                    model.getSettings().getLifeStoryOptions().put(Color.BLUE, true);
                }

                model.getSettings().setSelectedLifeStoryIndex(position);
                break;

            case R.id.ancestor_spinner:
                for(Map.Entry<Integer, Boolean> entry : model.getSettings().getAncestorsOptions().entrySet()){
                    entry.setValue(false);
                }

                String ancestorColor = parent.getSelectedItem().toString();
                if (ancestorColor.equals(getString(R.string.green))) {
                    model.getSettings().getAncestorsOptions().put(Color.GREEN, true);
                }
                else if (ancestorColor.equals(getString(R.string.red))) {
                    model.getSettings().getAncestorsOptions().put(Color.RED, true);
                }
                else {
                    model.getSettings().getAncestorsOptions().put(Color.BLUE, true);
                }

                model.getSettings().setSelectedAncestorsIndex(position);
                break;

            case R.id.spouse_spinner:
                for(Map.Entry<Integer, Boolean> entry : model.getSettings().getSpouseOptions().entrySet()){
                    entry.setValue(false);
                }

                String spouseColor = parent.getSelectedItem().toString();
                if (spouseColor.equals(getString(R.string.green))) {
                    model.getSettings().getSpouseOptions().put(Color.GREEN, true);
                }
                else if (spouseColor.equals(getString(R.string.red))) {
                    model.getSettings().getSpouseOptions().put(Color.RED, true);
                }
                else {
                    model.getSettings().getSpouseOptions().put(Color.BLUE, true);
                }

                model.getSettings().setSelectedSpouseIndex(position);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}
