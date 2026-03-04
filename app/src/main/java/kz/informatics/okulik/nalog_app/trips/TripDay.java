package kz.informatics.okulik.nalog_app.trips;

import java.util.List;

/** One day in the itinerary (e.g. Day 1: Charyn &amp; Kolsai) with time slots. */
public class TripDay {
    public final String dayLabel;
    public final List<TripItineraryItem> items;

    public TripDay(String dayLabel, List<TripItineraryItem> items) {
        this.dayLabel = dayLabel;
        this.items = items != null ? items : java.util.Collections.emptyList();
    }
}
