package kz.informatics.okulik.nalog_app.cabinet;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Persistable booking data for MyCabinet Bookings tab.
 */
public class SavedBooking {
    private static final String KEY_HOTEL_ID = "hotelId";
    private static final String KEY_HOTEL_NAME = "hotelName";
    private static final String KEY_REFERENCE = "reference";
    private static final String KEY_CHECK_IN = "checkIn";
    private static final String KEY_CHECK_OUT = "checkOut";
    private static final String KEY_GUESTS = "guests";
    private static final String KEY_TOTAL_PRICE = "totalPrice";
    private static final String KEY_ROOM_TYPE = "roomType";
    private static final String KEY_IMAGE_RES_ID = "imageResId";
    private static final String KEY_STATUS = "status";

    public final String hotelId;
    public final String hotelName;
    public final String reference;
    public final String checkIn;
    public final String checkOut;
    public final String guests;
    public final String totalPrice;
    public final String roomType;
    public final int imageResId;
    public final String status;

    public SavedBooking(String hotelId, String hotelName, String reference,
                        String checkIn, String checkOut, String guests, String totalPrice, String roomType,
                        int imageResId, String status) {
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.reference = reference;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.guests = guests;
        this.totalPrice = totalPrice;
        this.roomType = roomType;
        this.imageResId = imageResId;
        this.status = status != null ? status : BookingItem.STATUS_CONFIRMED;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject o = new JSONObject();
        o.put(KEY_HOTEL_ID, hotelId);
        o.put(KEY_HOTEL_NAME, hotelName);
        o.put(KEY_REFERENCE, reference);
        o.put(KEY_CHECK_IN, checkIn);
        o.put(KEY_CHECK_OUT, checkOut);
        o.put(KEY_GUESTS, guests);
        o.put(KEY_TOTAL_PRICE, totalPrice);
        o.put(KEY_ROOM_TYPE, roomType);
        o.put(KEY_IMAGE_RES_ID, imageResId);
        o.put(KEY_STATUS, status);
        return o;
    }

    public static SavedBooking fromJson(JSONObject o) throws JSONException {
        return new SavedBooking(
                o.optString(KEY_HOTEL_ID, ""),
                o.optString(KEY_HOTEL_NAME, ""),
                o.optString(KEY_REFERENCE, ""),
                o.optString(KEY_CHECK_IN, ""),
                o.optString(KEY_CHECK_OUT, ""),
                o.optString(KEY_GUESTS, ""),
                o.optString(KEY_TOTAL_PRICE, ""),
                o.optString(KEY_ROOM_TYPE, ""),
                o.optInt(KEY_IMAGE_RES_ID, 0),
                o.optString(KEY_STATUS, BookingItem.STATUS_CONFIRMED)
        );
    }

    public BookingItem toBookingItem() {
        return new BookingItem(
                hotelId, hotelName, reference, status,
                formatDateDisplay(checkIn), formatDateDisplay(checkOut), guests, totalPrice, roomType,
                imageResId, BookingItem.STATUS_CONFIRMED.equals(status)
        );
    }

    private static String formatDateDisplay(String yyyyMmDd) {
        if (yyyyMmDd == null || yyyyMmDd.isEmpty()) return yyyyMmDd;
        try {
            java.text.SimpleDateFormat in = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US);
            java.text.SimpleDateFormat out = new java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.US);
            java.util.Date d = in.parse(yyyyMmDd);
            return d != null ? out.format(d) : yyyyMmDd;
        } catch (Exception e) {
            return yyyyMmDd;
        }
    }
}
