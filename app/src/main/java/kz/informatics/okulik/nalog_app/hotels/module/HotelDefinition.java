package kz.informatics.okulik.nalog_app.hotels.module;

import java.util.List;

/**
 * Hotel definition with resource IDs. Resolved to Hotel via HotelsRepository when Context is provided.
 */
public class HotelDefinition {
    public final String id;
    public final int nameResId;
    public final int addressResId;
    public final int descriptionResId;
    public final float rating;
    public final int imageRes;
    public final int[] galleryResIds;
    public final int[] facilityResIds;
    public final String location;
    public final int pricePerNight;
    public final List<String> availableDays;
    public final List<HotelRoomDefinition> rooms;
    public final List<HotelAddOnDefinition> addOns;

    public HotelDefinition(String id, int nameResId, int addressResId, int descriptionResId,
                           float rating, int imageRes, int[] galleryResIds, int[] facilityResIds,
                           String location, int pricePerNight, List<String> availableDays,
                           List<HotelRoomDefinition> rooms, List<HotelAddOnDefinition> addOns) {
        this.id = id;
        this.nameResId = nameResId;
        this.addressResId = addressResId;
        this.descriptionResId = descriptionResId;
        this.rating = rating;
        this.imageRes = imageRes;
        this.galleryResIds = galleryResIds != null ? galleryResIds : new int[0];
        this.facilityResIds = facilityResIds != null ? facilityResIds : new int[0];
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
