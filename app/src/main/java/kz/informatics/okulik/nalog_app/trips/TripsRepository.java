package kz.informatics.okulik.nalog_app.trips;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import kz.informatics.okulik.R;

/**
 * Provides guided tours and (later) self-planned trips.
 */
public class TripsRepository {

    private static TripsRepository instance;
    private final List<GuidedTour> guidedTours = new ArrayList<>();

    private TripsRepository(Context context) {
        loadGuidedTours();
    }

    public static synchronized TripsRepository getInstance(Context context) {
        if (instance == null) {
            instance = new TripsRepository(context.getApplicationContext());
        }
        return instance;
    }

    public List<GuidedTour> getGuidedTours() {
        return new ArrayList<>(guidedTours);
    }

    public GuidedTour getGuidedTourById(String id) {
        for (GuidedTour t : guidedTours) {
            if (t.id != null && t.id.equals(id)) return t;
        }
        return null;
    }

    private void loadGuidedTours() {
        guidedTours.clear();
        guidedTours.add(new GuidedTour("t1", "Golden Triangle of Almaty", "",
                "2 Days", R.drawable.header_charyn_canyon, 35000, "Steppe Travel", "87071112233", 4.9f,
                "Charyn → Kolsai → Kaindy", "Next: Sat, 10 Oct", "Max 15"));
        guidedTours.add(new GuidedTour("t2", "High Mountain Classic", "",
                "6 Hours", R.drawable.img_alatau1, 12000, "Almaty City Tours", "87071112233", 4.7f,
                "Medeu → Shymbulak → Kok Tobe", "Daily", "Small Group"));
        guidedTours.add(new GuidedTour("t3", "Cultural Heritage Walk", "",
                "4 Hours", R.drawable.img_grand_aura1, 8500, "CultureGuide", "87071112233", 4.8f,
                "Mosque → Green Bazaar → Panfilov Park", "Weekends", "Walking"));

        // t4: Shopping Malls Tour
        guidedTours.add(new GuidedTour("t4", "Almaty Shopping Malls", "",
                "5 Hours", R.drawable.header_mega, 10000, "Almaty City Tours", "87071112233", 4.8f,
                "MEGA → Esentai → Aport → Dostyk Plaza", "Daily", "Small Group"));
        // t5: Parks Explorer
        guidedTours.add(new GuidedTour("t5", "Almaty Parks Trail", "",
                "6 Hours", R.drawable.header_panfilov, 9000, "Green Almaty", "87071112233", 4.7f,
                "Panfilov → Kok Tobe → First President → Central Park", "Weekends", "Walking"));
        // t6: Mosques Heritage
        guidedTours.add(new GuidedTour("t6", "Almaty Mosques Tour", "",
                "4 Hours", R.drawable.header_central_mosque, 7000, "CultureGuide", "87071112233", 4.9f,
                "Central Mosque → Aynalyn → Beisenova → Amankul Ata", "Daily", "Cultural"));
        // t7: Nature Lakes & Gorge
        guidedTours.add(new GuidedTour("t7", "Big Almaty & Issyk Lakes", "",
                "10 Hours", R.drawable.header_bigalmaty_lake, 15000, "Steppe Travel", "87071112233", 4.8f,
                "Big Almaty → Issyk → Turgen → Tamgaly Tas", "Sat-Sun", "Max 12"));

    }

    /** Full detail for TripsDetailActivity (itinerary, included/not included, organizer, etc.). */
    public TripDetailData getTripDetail(Context context, String tourId) {
        GuidedTour tour = getGuidedTourById(tourId);
        if (tour == null) return null;
        if ("t1".equals(tourId)) {
            List<TripDay> days = new ArrayList<>();
            days.add(new TripDay("Day 1: Charyn & Kolsai", Arrays.asList(
                    new TripItineraryItem("07:00", "Departure from Almaty", "Meet at Abay Central Stadium. Briefing and boarding the comfortable mini-bus."),
                    new TripItineraryItem("10:30", "Charyn Canyon", "Explore the famous Valley of Castles and take photos."),
                    new TripItineraryItem("14:00", "Lunch in Saty Village", "Traditional lunch at a guest house."),
                    new TripItineraryItem("16:00", "Kolsai Lake 1", "First lake visit and short hike.")
            )));
            days.add(new TripDay("Day 2: Kaindy Lake", Arrays.asList(
                    new TripItineraryItem("08:00", "Breakfast", "Breakfast at the guest house."),
                    new TripItineraryItem("09:30", "Off-road to Kaindy", "Jeep transfer to Kaindy Lake."),
                    new TripItineraryItem("15:00", "Return to Almaty", "Drive back to the city.")
            )));
            return new TripDetailData(tour,
                    new String[]{"BEST SELLER", "GROUP TOUR"},
                    "Almaty Region",
                    "2 Days, 1 Night",
                    "~600 km Total",
                    "Comfort Minibus",
                    "Max 15 people",
                    days,
                    new String[]{
                            "Round-trip transport (Comfort Minibus)",
                            "Professional English-speaking Guide",
                            "Accommodation at Saty village (Guest house)",
                            "Eco-fees & National Park entrance tickets",
                            "Meals (1 Dinner, 1 Breakfast, 1 Lunch)",
                            "Off-road transfer to Kaindy lake"
                    },
                    new String[]{
                            "Personal expenses & additional snacks",
                            "Horseback riding or boat rentals at Kolsai"
                    },
                    R.drawable.user_avatar,
                    "245 Reviews",
                    true,
                    35000,
                    "Included"
            );
        }
        if ("t2".equals(tourId)) {
            List<TripDay> days = Collections.singletonList(new TripDay("Single day", Arrays.asList(
                    new TripItineraryItem("09:00", "Medeu Gorge", "Visit the high-mountain skating rink."),
                    new TripItineraryItem("11:00", "Shymbulak", "Cable car and mountain views."),
                    new TripItineraryItem("14:00", "Kok Tobe", "City view and leisure.")
            )));
            return new TripDetailData(tour,
                    new String[]{"GROUP TOUR"},
                    "Almaty",
                    "6 Hours",
                    "~80 km Total",
                    "Minibus",
                    "Small Group",
                    days,
                    new String[]{"Transport", "Guide", "Cable car ticket"},
                    new String[]{"Lunch", "Personal expenses"},
                    R.drawable.user_avatar,
                    "128 Reviews",
                    true,
                    12000,
                    "Included"
            );
        }
        if ("t3".equals(tourId)) {
            List<TripDay> days = Collections.singletonList(new TripDay("Walking tour", Arrays.asList(
                    new TripItineraryItem("10:00", "Central Mosque", "Visit and introduction."),
                    new TripItineraryItem("11:30", "Green Bazaar", "Local market and snacks."),
                    new TripItineraryItem("14:00", "Panfilov Park", "Memorial and Zenkov Cathedral.")
            )));
            return new TripDetailData(tour,
                    new String[]{"WALKING"},
                    "Almaty City",
                    "4 Hours",
                    "On foot",
                    "Walking",
                    "Max 12 people",
                    days,
                    new String[]{"Professional guide", "Entrance to sites"},
                    new String[]{"Meals", "Personal shopping"},
                    R.drawable.user_avatar,
                    "89 Reviews",
                    false,
                    8500,
                    "Included"
            );
        }
        return new TripDetailData(tour, new String[0], "", tour.duration, "", "", tour.groupInfo,
                Collections.emptyList(), new String[0], new String[0], R.drawable.user_avatar, "", false,
                tour.totalPrice, "Included");
    }

    public static String formatPrice(int price) {
        return String.format(Locale.US, "%,d 〒", price);
    }
}
