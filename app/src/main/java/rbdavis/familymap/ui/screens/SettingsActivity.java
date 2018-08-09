package rbdavis.familymap.ui.screens;

import android.content.Intent;
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

import rbdavis.familymap.R;
import rbdavis.familymap.models.App;
import rbdavis.familymap.models.Settings;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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
            }
        });

        ancestorsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                model.getSettings().setShowAncestorsLines(isChecked);
            }
        });

        spouseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                model.getSettings().setShowSpouseLines(isChecked);
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

    }

    private void logout() {
        // TODO Model logout first

//        Intent intent = new Intent( this , MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.map_spinner:
                // TODO reset previous maps
                break;
            case R.id.life_story_spinner:
                break;
            case R.id.ancestor_spinner:
                break;
            case R.id.spouse_spinner:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}
