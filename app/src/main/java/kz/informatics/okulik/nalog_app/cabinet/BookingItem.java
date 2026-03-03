package kz.informatics.okulik.nalog_app.cabinet;

/**
 * One booking entry for My Cabinet list.
 */
public class BookingItem {
    public static final String STATUS_CONFIRMED = "confirmed";
    public static final String STATUS_COMPLETED = "completed";

    public final String hotelId;
    public final String hotelName;
    public final String reference;
    public final String status;
    public final String checkIn;
    public final String checkOut;
    public final String guests;
    public final String totalPrice;
    public final String roomType;
    public final int imageResId;
    /** true = show Cancel + View Details; false = show View Receipt */
    public final boolean showCancelAndDetails;

    /** Full constructor including hotelId (can be null for demo items). */
    public BookingItem(String hotelId, String hotelName, String reference, String status,
                       String checkIn, String checkOut, String guests, String totalPrice, String roomType,
                       int imageResId, boolean showCancelAndDetails) {
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.reference = reference;
        this.status = status;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.guests = guests;
        this.totalPrice = totalPrice;
        this.roomType = roomType;
        this.imageResId = imageResId;
        this.showCancelAndDetails = showCancelAndDetails;
    }

    /** Backward compatibility: hotelId will be null. */
    public BookingItem(String hotelName, String reference, String status,
                       String checkIn, String checkOut, String guests, String totalPrice, String roomType,
                       int imageResId, boolean showCancelAndDetails) {
        this(null, hotelName, reference, status, checkIn, checkOut, guests, totalPrice, roomType, imageResId, showCancelAndDetails);
    }
}
