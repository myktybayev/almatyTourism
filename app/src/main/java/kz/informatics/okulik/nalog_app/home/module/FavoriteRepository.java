package kz.informatics.okulik.nalog_app.home.module;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Persists favorite places to SharedPreferences so they survive app restart.
 * Same pattern as SavedBookingsRepository.
 */
public class FavoriteRepository {
    private static final String PREFS_NAME = "favorite_places";
    private static final String KEY_LIST = "favorites_json";

    private static volatile FavoriteRepository instance;
    private final SharedPreferences prefs;
    private final Map<String, PopularPlace> favoriteMap = new LinkedHashMap<>();

    private FavoriteRepository(Context appContext) {
        this.prefs = appContext.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        loadFromPrefs();
    }

    public static FavoriteRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (FavoriteRepository.class) {
                if (instance == null) {
                    instance = new FavoriteRepository(context);
                }
            }
        }
        return instance;
    }

    private void loadFromPrefs() {
        favoriteMap.clear();
        String json = prefs.getString(KEY_LIST, "[]");
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                PopularPlace p = PopularPlace.fromJson(arr.getJSONObject(i));
                if (p.id != null && !p.id.isEmpty()) {
                    favoriteMap.put(p.id, p);
                }
            }
        } catch (JSONException ignored) {
        }
    }

    private void saveToPrefs() {
        JSONArray arr = new JSONArray();
        for (PopularPlace p : favoriteMap.values()) {
            try {
                arr.put(p.toJson());
            } catch (JSONException ignored) {
            }
        }
        prefs.edit().putString(KEY_LIST, arr.toString()).apply();
    }

    public boolean isFavorite(String id) {
        return id != null && favoriteMap.containsKey(id);
    }

    public boolean toggle(PopularPlace item) {
        if (item == null || item.id == null) return false;
        if (favoriteMap.containsKey(item.id)) {
            favoriteMap.remove(item.id);
            saveToPrefs();
            return false;
        } else {
            favoriteMap.put(item.id, item);
            saveToPrefs();
            return true;
        }
    }

    /** Toggle favorite for a generic place (e.g. hotel, trip) by id and title. */
    public boolean togglePlace(String id, String title) {
        if (id == null) return false;
        if (favoriteMap.containsKey(id)) {
            favoriteMap.remove(id);
            saveToPrefs();
            return false;
        } else {
            PopularPlace p = new PopularPlace(id, title != null ? title : "", "", 0f, 0, new String[0]);
            favoriteMap.put(id, p);
            saveToPrefs();
            return true;
        }
    }

    public List<PopularPlace> getFavorites() {
        return Collections.unmodifiableList(new ArrayList<>(favoriteMap.values()));
    }
}
