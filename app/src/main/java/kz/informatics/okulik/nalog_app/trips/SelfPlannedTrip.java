package kz.informatics.okulik.nalog_app.trips;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * User-created self-planned trip (saved itinerary).
 */
public class SelfPlannedTrip {
    public final String id;
    public final String title;
    /** e.g. "Almaty · City & Mountains" */
    public final String locationTheme;
    /** e.g. "12 Aug - 15 Aug" or "Fri 20 Sep - Sun 22 Sep" */
    public final String dates;
    /** Accommodation name or null if none */
    public final String accommodation;
    /** Estimated total cost in tenge */
    public final int estimatedCostTenge;

    public SelfPlannedTrip(String id, String title, String locationTheme, String dates,
                           String accommodation, int estimatedCostTenge) {
        this.id = id != null ? id : "";
        this.title = title != null ? title : "";
        this.locationTheme = locationTheme != null ? locationTheme : "";
        this.dates = dates != null ? dates : "";
        this.accommodation = accommodation;
        this.estimatedCostTenge = estimatedCostTenge;
    }

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_LOCATION_THEME = "locationTheme";
    private static final String KEY_DATES = "dates";
    private static final String KEY_ACCOMMODATION = "accommodation";
    private static final String KEY_ESTIMATED_COST = "estimatedCostTenge";

    public JSONObject toJson() throws JSONException {
        JSONObject o = new JSONObject();
        o.put(KEY_ID, id);
        o.put(KEY_TITLE, title);
        o.put(KEY_LOCATION_THEME, locationTheme);
        o.put(KEY_DATES, dates);
        o.put(KEY_ACCOMMODATION, accommodation != null ? accommodation : "");
        o.put(KEY_ESTIMATED_COST, estimatedCostTenge);
        return o;
    }

    public static SelfPlannedTrip fromJson(JSONObject o) throws JSONException {
        String acc = o.optString(KEY_ACCOMMODATION, "");
        return new SelfPlannedTrip(
                o.optString(KEY_ID, ""),
                o.optString(KEY_TITLE, ""),
                o.optString(KEY_LOCATION_THEME, ""),
                o.optString(KEY_DATES, ""),
                acc.isEmpty() ? null : acc,
                o.optInt(KEY_ESTIMATED_COST, 0)
        );
    }
}
