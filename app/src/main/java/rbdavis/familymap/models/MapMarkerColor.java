package rbdavis.familymap.models;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public enum MapMarkerColor {
    RED(BitmapDescriptorFactory.HUE_RED),
    YELLOW(BitmapDescriptorFactory.HUE_YELLOW),
    AZURE(BitmapDescriptorFactory.HUE_AZURE),
    GREEN(BitmapDescriptorFactory.HUE_GREEN),
    MAGENTA(BitmapDescriptorFactory.HUE_MAGENTA),
    VIOLET(BitmapDescriptorFactory.HUE_VIOLET);

    private final float value;
    private boolean used = false;

    MapMarkerColor(float val) {
        value = val;
    }

    public float getValue() {
        return value;
    }

    public boolean isUsed() {
        return used;
    }

    public void setIsUsed(boolean isUsed) {
        this.used = isUsed;
    }

    public static void resetUsage() {
        for (MapMarkerColor color : MapMarkerColor.values()) {
            color.setIsUsed(false);
        }
    }
}
