package kz.informatics.okulik.nalog_app.trips;

/**
 * Model for a commercial guided tour (agency-organized).
 */
public class GuidedTour {
    public final String id;
    public final String name;
    public final String summary;
    /** e.g. "2 Days", "6 Hours" */
    public final String duration;
    public final int imageResId;
    /** Total price in tenge */
    public final int totalPrice;
    public final String organizerName;
    public final String organizerPhone;
    public final float organizerRating;
    /** Route as "Location1 → Location2 → Location3" */
    public final String locationsRoute;
    /** e.g. "Next: Sat, 10 Oct", "Daily", "Weekends" */
    public final String availability;
    /** e.g. "Max 15", "Small Group", "Walking" */
    public final String groupInfo;

    public GuidedTour(String id, String name, String summary, String duration,
                      int imageResId, int totalPrice, String organizerName, String organizerPhone, float organizerRating,
                      String locationsRoute, String availability, String groupInfo) {
        this.id = id;
        this.name = name;
        this.summary = summary;
        this.duration = duration;
        this.imageResId = imageResId;
        this.totalPrice = totalPrice;
        this.organizerName = organizerName;
        this.organizerPhone = organizerPhone;
        this.organizerRating = organizerRating;
        this.locationsRoute = locationsRoute;
        this.availability = availability;
        this.groupInfo = groupInfo;
    }
}
