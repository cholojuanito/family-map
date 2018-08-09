package rbdavis.familymap.models;

import java.util.HashMap;
import java.util.Map;

import rbdavis.shared.utils.Constants;

public class Filters {
    private Map<String, Boolean> filterOptions;

    public Filters() {
        filterOptions = new HashMap<>();
        filterOptions.put(Constants.BY_FATHER_SIDE, false);
        filterOptions.put(Constants.BY_MOTHER_SIDE, false);
        filterOptions.put(Constants.BY_MALE, false);
        filterOptions.put(Constants.BY_FEMALE, false);
    }

    public Map<String, Boolean> getFilterOptions() {
        return filterOptions;
    }

    public void setFilterOptions(Map<String, Boolean> filterOptions) {
        this.filterOptions = filterOptions;
    }
}
