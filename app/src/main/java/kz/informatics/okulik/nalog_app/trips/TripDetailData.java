package kz.informatics.okulik.nalog_app.trips;

import java.util.List;

/**
 * Full detail data for TripsDetailActivity (itinerary, included/not included, organizer, etc.).
 */
public class TripDetailData {
    public final GuidedTour tour;
    public final String[] labels;           // e.g. "BEST SELLER", "GROUP TOUR"
    public final String location;          // e.g. "Almaty Region"
    public final String durationLong;      // e.g. "2 Days, 1 Night"
    public final String distance;           // e.g. "~600 km Total"
    public final String transport;          // e.g. "Comfort Minibus"
    public final String groupSize;          // e.g. "Max 15 people"
    public final List<TripDay> itinerary;
    public final String[] included;
    public final String[] notIncluded;
    public final int organizerAvatarRes;
    public final String organizerReviews;   // e.g. "245 Reviews"
    public final boolean organizerVerified;
    /** Price per person in tenge */
    public final int pricePerPerson;
    public final String ecoTaxLabel;        // e.g. "Included"

    public TripDetailData(GuidedTour tour, String[] labels, String location,
                          String durationLong, String distance, String transport, String groupSize,
                          List<TripDay> itinerary, String[] included, String[] notIncluded,
                          int organizerAvatarRes, String organizerReviews, boolean organizerVerified,
                          int pricePerPerson, String ecoTaxLabel) {
        this.tour = tour;
        this.labels = labels != null ? labels : new String[0];
        this.location = location != null ? location : "";
        this.durationLong = durationLong != null ? durationLong : "";
        this.distance = distance != null ? distance : "";
        this.transport = transport != null ? transport : "";
        this.groupSize = groupSize != null ? groupSize : "";
        this.itinerary = itinerary != null ? itinerary : java.util.Collections.emptyList();
        this.included = included != null ? included : new String[0];
        this.notIncluded = notIncluded != null ? notIncluded : new String[0];
        this.organizerAvatarRes = organizerAvatarRes;
        this.organizerReviews = organizerReviews != null ? organizerReviews : "";
        this.organizerVerified = organizerVerified;
        this.pricePerPerson = pricePerPerson;
        this.ecoTaxLabel = ecoTaxLabel != null ? ecoTaxLabel : "";
    }
}
