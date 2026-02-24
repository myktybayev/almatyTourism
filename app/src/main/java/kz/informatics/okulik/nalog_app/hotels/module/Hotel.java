package kz.informatics.okulik.nalog_app.hotels.module;

import java.util.List;

/**
 * Hotel model with all data for list and detail views.
 */
public class Hotel {
    public final String id;
    public final String name;
    public final String address;
    public final String description;
    public final float rating;
    public final int imageRes;
    public final int[] galleryResIds;
    public final String[] facilities; // e.g. "wifi", "dining", "pool", "spa"
    public final String location; // "lat,lng" for map
    public final int pricePerNight;
    /** Available date strings in format "yyyy-MM-dd". Hotel is available if ALL dates in search range are in this set. */
    public final List<String> availableDays;
    public final List<HotelRoom> rooms;
    public final List<HotelAddOn> addOns;

    public Hotel(String id, String name, String address, String description,
                 float rating, int imageRes, int[] galleryResIds, String[] facilities,
                 String location, int pricePerNight, List<String> availableDays,
                 List<HotelRoom> rooms, List<HotelAddOn> addOns) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.rating = rating;
        this.imageRes = imageRes;
        this.galleryResIds = galleryResIds != null ? galleryResIds : new int[0];
        this.facilities = facilities != null ? facilities : new String[0];
        this.location = location != null ? location : "";
        this.pricePerNight = pricePerNight;
        this.availableDays = availableDays != null ? availableDays : java.util.Collections.emptyList();
        this.rooms = rooms != null ? rooms : java.util.Collections.emptyList();
        this.addOns = addOns != null ? addOns : java.util.Collections.emptyList();
    }

    public boolean isAvailableForDates(String checkIn, String checkOut) {
        if (availableDays == null || availableDays.isEmpty()) return true;
        java.util.List<String> range = generateDateRange(checkIn, checkOut);
        for (String d : range) {
            if (!availableDays.contains(d)) return false;
        }
        return true;
    }

    private static java.util.List<String> generateDateRange(String start, String end) {
        java.util.List<String> result = new java.util.ArrayList<>();
        try {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US);
            java.util.Date s = sdf.parse(start);
            java.util.Date e = sdf.parse(end);
            if (s == null || e == null) return result;
            cal.setTime(s);
            while (!cal.getTime().after(e)) {
                result.add(sdf.format(cal.getTime()));
                cal.add(java.util.Calendar.DAY_OF_MONTH, 1);
            }
        } catch (Exception ignored) {
        }
        return result;
    }
}
