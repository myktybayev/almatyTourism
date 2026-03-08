package kz.informatics.okulik.nalog_app.trips;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Persists user-created self-planned trips (SharedPreferences).
 */
public class SelfPlannedTripsRepository {
    private static final String PREFS_NAME = "self_planned_trips";
    private static final String KEY_LIST = "trips_json";

    private static volatile SelfPlannedTripsRepository instance;
    private final SharedPreferences prefs;

    private SelfPlannedTripsRepository(Context appContext) {
        this.prefs = appContext.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SelfPlannedTripsRepository getInstance(Context context) {
        if (instance == null) {
            instance = new SelfPlannedTripsRepository(context.getApplicationContext());
        }
        return instance;
    }

    public List<SelfPlannedTrip> getAll() {
        String json = prefs.getString(KEY_LIST, "[]");
        List<SelfPlannedTrip> list = new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                list.add(SelfPlannedTrip.fromJson(arr.getJSONObject(i)));
            }
        } catch (JSONException ignored) {
        }
        return list;
    }

    public SelfPlannedTrip getById(String id) {
        if (id == null) return null;
        for (SelfPlannedTrip t : getAll()) {
            if (id.equals(t.id)) return t;
        }
        return null;
    }

    public void add(SelfPlannedTrip trip) {
        List<SelfPlannedTrip> list = new ArrayList<>(getAll());
        list.add(0, trip);
        saveAll(list);
    }

    public void removeById(String id) {
        if (id == null) return;
        List<SelfPlannedTrip> list = getAll();
        list.removeIf(t -> id.equals(t.id));
        saveAll(list);
    }

    public void update(SelfPlannedTrip trip) {
        List<SelfPlannedTrip> list = getAll();
        for (int i = 0; i < list.size(); i++) {
            if (trip.id.equals(list.get(i).id)) {
                list.set(i, trip);
                saveAll(list);
                return;
            }
        }
        add(trip);
    }

    private void saveAll(List<SelfPlannedTrip> list) {
        JSONArray arr = new JSONArray();
        for (SelfPlannedTrip t : list) {
            try {
                arr.put(t.toJson());
            } catch (JSONException ignored) {
            }
        }
        prefs.edit().putString(KEY_LIST, arr.toString()).apply();
    }

    public static String formatCost(int tenge) {
        return String.format(Locale.US, "≈ %s 〒", String.format(Locale.US, "%,d", tenge));
    }
}
