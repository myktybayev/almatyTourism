package kz.informatics.okulik.nalog_app.trips;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * User-created self-planned trip (saved itinerary). Includes optional stop names and expenses for edit.
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
    /** Stop names in order (for edit screen) */
    public final List<String> stopNames;
    /** Budget expenses (for edit screen) */
    public final List<SavedExpense> expenses;

    public SelfPlannedTrip(String id, String title, String locationTheme, String dates,
                           String accommodation, int estimatedCostTenge) {
        this(id, title, locationTheme, dates, accommodation, estimatedCostTenge, new ArrayList<>(), new ArrayList<>());
    }

    public SelfPlannedTrip(String id, String title, String locationTheme, String dates,
                           String accommodation, int estimatedCostTenge,
                           List<String> stopNames, List<SavedExpense> expenses) {
        this.id = id != null ? id : "";
        this.title = title != null ? title : "";
        this.locationTheme = locationTheme != null ? locationTheme : "";
        this.dates = dates != null ? dates : "";
        this.accommodation = accommodation;
        this.estimatedCostTenge = estimatedCostTenge;
        this.stopNames = stopNames != null ? stopNames : new ArrayList<>();
        this.expenses = expenses != null ? expenses : new ArrayList<>();
    }

    public static final class SavedExpense {
        public final String category;
        public final long amountKzt;
        public final String note;

        public SavedExpense(String category, long amountKzt, String note) {
            this.category = category != null ? category : "";
            this.amountKzt = amountKzt;
            this.note = note != null ? note : "";
        }

        private static final String K_CAT = "category";
        private static final String K_AMOUNT = "amount";
        private static final String K_NOTE = "note";

        JSONObject toJson() throws JSONException {
            JSONObject o = new JSONObject();
            o.put(K_CAT, category);
            o.put(K_AMOUNT, amountKzt);
            o.put(K_NOTE, note);
            return o;
        }

        static SavedExpense fromJson(JSONObject o) throws JSONException {
            return new SavedExpense(
                    o.optString(K_CAT, ""),
                    o.optLong(K_AMOUNT, 0),
                    o.optString(K_NOTE, "")
            );
        }
    }

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_LOCATION_THEME = "locationTheme";
    private static final String KEY_DATES = "dates";
    private static final String KEY_ACCOMMODATION = "accommodation";
    private static final String KEY_ESTIMATED_COST = "estimatedCostTenge";
    private static final String KEY_STOPS = "stops";
    private static final String KEY_EXPENSES = "expenses";

    public JSONObject toJson() throws JSONException {
        JSONObject o = new JSONObject();
        o.put(KEY_ID, id);
        o.put(KEY_TITLE, title);
        o.put(KEY_LOCATION_THEME, locationTheme);
        o.put(KEY_DATES, dates);
        o.put(KEY_ACCOMMODATION, accommodation != null ? accommodation : "");
        o.put(KEY_ESTIMATED_COST, estimatedCostTenge);
        JSONArray stopsArr = new JSONArray();
        for (String s : stopNames) stopsArr.put(s != null ? s : "");
        o.put(KEY_STOPS, stopsArr);
        JSONArray expArr = new JSONArray();
        for (SavedExpense e : expenses) expArr.put(e.toJson());
        o.put(KEY_EXPENSES, expArr);
        return o;
    }

    public static SelfPlannedTrip fromJson(JSONObject o) throws JSONException {
        String acc = o.optString(KEY_ACCOMMODATION, "");
        List<String> stops = new ArrayList<>();
        if (o.has(KEY_STOPS)) {
            JSONArray arr = o.getJSONArray(KEY_STOPS);
            for (int i = 0; i < arr.length(); i++) stops.add(arr.optString(i, ""));
        }
        List<SavedExpense> expList = new ArrayList<>();
        if (o.has(KEY_EXPENSES)) {
            JSONArray arr = o.getJSONArray(KEY_EXPENSES);
            for (int i = 0; i < arr.length(); i++) expList.add(SavedExpense.fromJson(arr.getJSONObject(i)));
        }
        return new SelfPlannedTrip(
                o.optString(KEY_ID, ""),
                o.optString(KEY_TITLE, ""),
                o.optString(KEY_LOCATION_THEME, ""),
                o.optString(KEY_DATES, ""),
                acc.isEmpty() ? null : acc,
                o.optInt(KEY_ESTIMATED_COST, 0),
                stops,
                expList
        );
    }
}
