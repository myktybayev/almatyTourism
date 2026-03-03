package kz.informatics.okulik.nalog_app.cabinet;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Persists and loads saved bookings for MyCabinet Bookings tab.
 */
public class SavedBookingsRepository {
    private static final String PREFS_NAME = "cabinet_bookings";
    private static final String KEY_LIST = "saved_bookings_json";

    private static volatile SavedBookingsRepository instance;
    private final SharedPreferences prefs;

    private SavedBookingsRepository(Context appContext) {
        this.prefs = appContext.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static SavedBookingsRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (SavedBookingsRepository.class) {
                if (instance == null) {
                    instance = new SavedBookingsRepository(context);
                }
            }
        }
        return instance;
    }

    public void addBooking(SavedBooking booking) {
        List<SavedBooking> list = getAllSaved();
        list.add(0, booking);
        saveAll(list);
    }

    /** Removes a saved booking by its reference (e.g. "BK-7829"). */
    public void removeBookingByReference(String reference) {
        if (reference == null) return;
        List<SavedBooking> list = getAllSaved();
        list.removeIf(b -> reference.equals(b.reference));
        saveAll(list);
    }

    public List<BookingItem> getAllBookingsAsItems() {
        List<SavedBooking> saved = getAllSaved();
        List<BookingItem> items = new ArrayList<>();
        for (SavedBooking b : saved) {
            items.add(b.toBookingItem());
        }
        return items;
    }

    private List<SavedBooking> getAllSaved() {
        String json = prefs.getString(KEY_LIST, "[]");
        List<SavedBooking> list = new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                list.add(SavedBooking.fromJson(arr.getJSONObject(i)));
            }
        } catch (JSONException ignored) {
        }
        return list;
    }

    private void saveAll(List<SavedBooking> list) {
        JSONArray arr = new JSONArray();
        for (SavedBooking b : list) {
            try {
                arr.put(b.toJson());
            } catch (JSONException ignored) {
            }
        }
        prefs.edit().putString(KEY_LIST, arr.toString()).apply();
    }
}
