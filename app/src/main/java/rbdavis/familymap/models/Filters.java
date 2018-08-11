package rbdavis.familymap.models;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import rbdavis.shared.utils.Constants;

public class Filters {
    private Map<String, Boolean> filterOptions;

    public Filters() {
        filterOptions = new TreeMap<>();
        filterOptions.put(Constants.BY_FATHER_SIDE, true);
        filterOptions.put(Constants.BY_MOTHER_SIDE, true);
        filterOptions.put(Constants.BY_MALE, true);
        filterOptions.put(Constants.BY_FEMALE, true);
    }

    public Map<String, Boolean> getFilterOptions() {
        return filterOptions;
    }

    public void setFilterOptions(Map<String, Boolean> filterOptions) {
        this.filterOptions = filterOptions;
    }
}
