package rbdavis.familymap.models;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import rbdavis.shared.utils.Constants;

/*
 * A wrapper class that encapsulates the possible event filters
 * within the app. All the event types are dynamically taken add
 * except for Father's side, Mother's side, male, and female.
 * Filters are just a Map of a String to a Boolean. They are either on
 * (allowed to show that event type) or off (not allowed to show that
 * event type).
 */

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
