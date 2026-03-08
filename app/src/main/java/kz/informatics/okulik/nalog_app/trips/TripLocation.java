package kz.informatics.okulik.nalog_app.trips;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Single location/stop in a self-planned trip with name and optional note.
 */
public class TripLocation {
    public final String locationName;
    public final String locationNote;

    public TripLocation(String locationName, String locationNote) {
        this.locationName = locationName != null ? locationName : "";
        this.locationNote = locationNote != null ? locationNote : "";
    }

    private static final String KEY_NAME = "locationName";
    private static final String KEY_NOTE = "locationNote";

    JSONObject toJson() throws JSONException {
        JSONObject o = new JSONObject();
        o.put(KEY_NAME, locationName);
        o.put(KEY_NOTE, locationNote);
        return o;
    }

    static TripLocation fromJson(JSONObject o) throws JSONException {
        return new TripLocation(
                o.optString(KEY_NAME, ""),
                o.optString(KEY_NOTE, "")
        );
    }
}
