package kz.informatics.okulik.nalog_app.trips;

/** One time slot in the itinerary (e.g. 07:00 — Departure from Almaty). */
public class TripItineraryItem {
    public final String time;
    public final String title;
    public final String description;

    public TripItineraryItem(String time, String title, String description) {
        this.time = time;
        this.title = title;
        this.description = description != null ? description : "";
    }
}
