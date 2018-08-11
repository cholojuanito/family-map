package rbdavis.familymap.models;

import android.graphics.Color;

import java.util.HashMap;
import java.util.Map;

import static rbdavis.shared.utils.Constants.*;

public class Settings {
    private Map<String, Boolean> mapTypeOptions;
    private Map<Integer, Boolean> lifeStoryOptions;
    private Map<Integer, Boolean> ancestorsOptions;
    private Map<Integer, Boolean> spouseOptions;

    private int selectedMapTypeIndex;
    private int selectedLifeStoryIndex;
    private int selectedAncestorsIndex;
    private int selectedSpouseIndex;

    private boolean showLines;
    private boolean showLifeStoryLines;
    private boolean showAncestorsLines;
    private boolean showSpouseLines;

    public Settings() {
        mapTypeOptions = new HashMap<>();
        lifeStoryOptions = new HashMap<>();
        ancestorsOptions = new HashMap<>();
        spouseOptions = new HashMap<>();

        selectedMapTypeIndex = 0;   // Normal
        selectedLifeStoryIndex = 0; // Red
        selectedAncestorsIndex = 1; // Green
        selectedSpouseIndex = 2;    // Blue

        initMapTypeOptions();
        initLifeStoryOptions();
        initAncestorOptions();
        initSpouseOptions();
        initLineSettings();
    }

    private void initMapTypeOptions() {
        mapTypeOptions.put(NORMAL, true);
        mapTypeOptions.put(HYBRID, false);
        mapTypeOptions.put(SATELLITE, false);
        mapTypeOptions.put(TERRAIN, false);
    }

    private void initLifeStoryOptions() {
        lifeStoryOptions.put(Color.RED, true);
        lifeStoryOptions.put(Color.GREEN, false);
        lifeStoryOptions.put(Color.BLUE, false);
    }

    private void initAncestorOptions() {
        ancestorsOptions.put(Color.RED, false);
        ancestorsOptions.put(Color.GREEN, true);
        ancestorsOptions.put(Color.BLUE, false);
    }

    private void initSpouseOptions() {
        spouseOptions.put(Color.RED, false);
        spouseOptions.put(Color.GREEN, false);
        spouseOptions.put(Color.BLUE, true);
    }

    private void initLineSettings() {
        showLines = true;
        showLifeStoryLines = true;
        showAncestorsLines = true;
        showSpouseLines = true;
    }

    public Map<String, Boolean> getMapTypeOptions() {
        return mapTypeOptions;
    }

    public void setMapTypeOptions(Map<String, Boolean> mapTypeOptions) {
        this.mapTypeOptions = mapTypeOptions;
    }

    public Map<Integer, Boolean> getLifeStoryOptions() {
        return lifeStoryOptions;
    }

    public void setLifeStoryOptions(Map<Integer, Boolean> lifeStoryOptions) {
        this.lifeStoryOptions = lifeStoryOptions;
    }

    public Map<Integer, Boolean> getAncestorsOptions() {
        return ancestorsOptions;
    }

    public void setAncestorsOptions(Map<Integer, Boolean> ancestorsOptions) {
        this.ancestorsOptions = ancestorsOptions;
    }

    public Map<Integer, Boolean> getSpouseOptions() {
        return spouseOptions;
    }

    public void setSpouseOptions(Map<Integer, Boolean> spouseOptions) {
        this.spouseOptions = spouseOptions;
    }

    public boolean isShowLines() {
        return showLines;
    }

    public void setShowLines(boolean showLines) {
        this.showLines = showLines;
    }

    public boolean isShowLifeStoryLines() {
        return showLifeStoryLines;
    }

    public void setShowLifeStoryLines(boolean showLifeStoryLines) {
        this.showLifeStoryLines = showLifeStoryLines;
    }

    public boolean isShowAncestorsLines() {
        return showAncestorsLines;
    }

    public void setShowAncestorsLines(boolean showAncestorsLines) {
        this.showAncestorsLines = showAncestorsLines;
    }

    public boolean isShowSpouseLines() {
        return showSpouseLines;
    }

    public int getSelectedMapTypeIndex() {
        return selectedMapTypeIndex;
    }

    public void setSelectedMapTypeIndex(int selectedMapTypeIndex) {
        this.selectedMapTypeIndex = selectedMapTypeIndex;
    }

    public void setShowSpouseLines(boolean showSpouseLines) {
        this.showSpouseLines = showSpouseLines;
    }

    public int getSelectedLifeStoryIndex() {
        return selectedLifeStoryIndex;
    }

    public void setSelectedLifeStoryIndex(int selectedLifeStoryIndex) {
        this.selectedLifeStoryIndex = selectedLifeStoryIndex;
    }

    public int getSelectedAncestorsIndex() {
        return selectedAncestorsIndex;
    }

    public void setSelectedAncestorsIndex(int selectedAncestorsIndex) {
        this.selectedAncestorsIndex = selectedAncestorsIndex;
    }

    public int getSelectedSpouseIndex() {
        return selectedSpouseIndex;
    }

    public void setSelectedSpouseIndex(int selectedSpouseIndex) {
        this.selectedSpouseIndex = selectedSpouseIndex;
    }
}
