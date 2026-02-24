package kz.informatics.okulik.nalog_app.home.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FavoriteRepository {
    private static FavoriteRepository instance;

    private final Map<String, PopularPlace> favoriteMap = new LinkedHashMap<>();

    private FavoriteRepository() {}

    public static FavoriteRepository getInstance() {
        if (instance == null) {
            instance = new FavoriteRepository();
        }
        return instance;
    }

    public boolean isFavorite(String id) {
        return id != null && favoriteMap.containsKey(id);
    }

    public boolean toggle(PopularPlace item) {
        if (item == null || item.id == null) return false;
        if (favoriteMap.containsKey(item.id)) {
            favoriteMap.remove(item.id);
            return false;
        } else {
            favoriteMap.put(item.id, item);
            return true;
        }
    }

    /** Toggle favorite for a generic place (e.g. hotel) by id and title. */
    public boolean togglePlace(String id, String title) {
        if (id == null) return false;
        if (favoriteMap.containsKey(id)) {
            favoriteMap.remove(id);
            return false;
        } else {
            PopularPlace p = new PopularPlace(id, title, "", 0f, 0, new String[0]);
            favoriteMap.put(id, p);
            return true;
        }
    }

    public List<PopularPlace> getFavorites() {
        return Collections.unmodifiableList(new ArrayList<>(favoriteMap.values()));
    }
}

