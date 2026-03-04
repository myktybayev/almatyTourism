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
                "2 Days", R.drawable.tour1_1, 35000, "Panda Travel", "77053701025", 4.9f,
                "Charyn → Kolsai → Kaindy", "Next: Sat, 10 Oct", "Max 15"));

        guidedTours.add(new GuidedTour("t8", "Alma-Arasan & Ayusay Gorges", "",
                "7-8 Hours", R.drawable.tour_almarasan, 20000, "Kazakhstan Guided Tours", "77776278800", 4.8f,
                "Alma-Arasan → Ayusay → Falcon Sunkar", "Daily", "Max 15"));

        guidedTours.add(new GuidedTour("t3", "Yurt Camp & Horseback Adventure", "",
                "2 Days", R.drawable.header_yurt_camp, 180000, "Kazakhstan Nomad Tours", "77003808000", 4.9f,
                "Yurt Camp → Horseback → Zhasylkol Lake", "Fri-Sat", "Max 10"));

        guidedTours.add(new GuidedTour("t9", "Shymbulak Glacier & Cave", "",
                "6-7 Hours", R.drawable.tour_glacier, 25000, "Adam Hiking Tours", "77757763287", 4.9f,
                "Shymbulak → Bogdanovich Glacier → Oktyabrskaya Cave", "Daily", "Max 12"));

        guidedTours.add(new GuidedTour("t2", "High Mountain Classic", "",
                "6 Hours", R.drawable.img_medeu1, 12000, "Almaty City Tours", "87071112233", 4.7f,
                "Medeu → Shymbulak → Kok Tobe", "Daily", "Small Group"));

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

    /**
     * Full detail for TripsDetailActivity (itinerary, included/not included, organizer, etc.).
     */
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
                    R.drawable.tour_panda_avatar,
                    "1 545 Reviews",
                    true,
                    35000,
                    "Included",
                    new int[]{R.drawable.tour1_1, R.drawable.tour1_2, R.drawable.tour1_3, R.drawable.tour1_4, R.drawable.tour1_5, R.drawable.tour1_7, R.drawable.tour1_8, R.drawable.tour1_6}
            );
        }
        if ("t8".equals(tourId)) {
            List<TripDay> days = Collections.singletonList(new TripDay("Gorges Day", Arrays.asList(
                    new TripItineraryItem("08:00", "Hotel Pickup", "Pick-up from major Almaty hotels (Ritz-Carlton, Ibis, etc.)."),
                    new TripItineraryItem("09:00", "Alma-Arasan Gorge", "Guided tour, sightseeing, 1.5h walk (45 min)."),
                    new TripItineraryItem("10:30", "Hot Springs", "Free time & swimming at natural hot springs."),
                    new TripItineraryItem("11:15", "Ayusay Gorge", "Lunch/shopping 1.5h (extra charge), 1h guided walk."),
                    new TripItineraryItem("14:00", "Falcon Sunkar", "Predatory birds show visit (1h)."),
                    new TripItineraryItem("16:00", "Return", "Drop-off at Almaty hotels.")
            )));
            return new TripDetailData(tour,
                    new String[]{"NATURE", "HOT SPRINGS", "GROUP TOUR"},
                    "Almaty Region",
                    "7-8 Hours",
                    "~120 km Total",
                    "Air-conditioned Minivan",
                    "Max 15 people",
                    days,
                    new String[]{
                            "Hotel pick-up/drop-off in Almaty",
                            "English/Russian speaking guide",
                            "Air-conditioned minivan transport",
                            "Bottled water",
                            "National park entrance tickets",
                            "Shower gel & shampoo",
                            "Falcon show entrance ticket"
                    },
                    new String[]{
                            "Meals & drinks (available at local cafe)",
                            "Personal expenses"
                    },
                    R.drawable.tour_kaz_avatar,
                    "40 Reviews",
                    true,
                    20000,
                    "Included",
                    new int[]{R.drawable.tour_almarasan, R.drawable.tour_almarasan2, R.drawable.tour_almarasan3, R.drawable.tour_almarasan4, R.drawable.tour_almarasan5, R.drawable.tour_almarasan6}
            );
        }
        if ("t9".equals(tourId)) {
            List<TripDay> days = Collections.singletonList(new TripDay("Glacier Day", Arrays.asList(
                    new TripItineraryItem("08:00", "Transfer to Medeu", "Pick-up from Almaty hotels."),
                    new TripItineraryItem("09:00", "Shymbulak Cable Car", "Ride to Shymbulak Ski Resort (3200m)."),
                    new TripItineraryItem("10:00", "Bogdanovich Glacier", "2km guided hike (1-1.5h one way)."),
                    new TripItineraryItem("12:00", "Oktyabrskaya Cave", "Explore ice cave with stunning formations."),
                    new TripItineraryItem("13:00", "Outdoor Lunch", "Picnic with glacier views."),
                    new TripItineraryItem("14:30", "Return Hike", "Back to Shymbulak and cable car down."),
                    new TripItineraryItem("16:00", "Almaty Drop-off", "Return to hotels.")
            )));
            return new TripDetailData(tour,
                    new String[]{"HIKING", "GLACIER", "ADVENTURE"},
                    "Almaty Mountains",
                    "6-7 Hours",
                    "~80 km Total",
                    "Air-conditioned Vehicle",
                    "Max 12 people",
                    days,
                    new String[]{
                            "Air-conditioned transport from Almaty",
                            "English speaking guide",
                            "Helmet, hiking poles, spikes for boots",
                            "Cable car to Shymbulak Ski Resort"
                    },
                    new String[]{
                            "Meals and drinks (bring your own snacks)",
                            "Personal expenses"
                    },
                    R.drawable.tour_adam_avatar,
                    "45 Reviews",
                    true,
                    25000,
                    "Not Included",
                    new int[]{R.drawable.tour_glacier, R.drawable.tour_glacier3, R.drawable.tour_glacier2, R.drawable.tour_glacier4, R.drawable.tour_glacier5, R.drawable.tour_glacier8, R.drawable.tour_glacier6, R.drawable.tour_glacier7}
            );
        }
        if ("t3".equals(tourId)) {
            List<TripDay> days = new ArrayList<>();
            days.add(new TripDay("Day 1: Yurt Traditions", Arrays.asList(
                    new TripItineraryItem("08:00", "Transfer to Yurt Camp", "Pick-up from Almaty hotels to Zhylandy Valley."),
                    new TripItineraryItem("10:00", "Yurt Check-in", "Welcome tea & traditional treats."),
                    new TripItineraryItem("12:00", "Cooking Class", "Make kurt & zhent with locals."),
                    new TripItineraryItem("14:00", "Lunch", "Traditional Kazakh dishes."),
                    new TripItineraryItem("15:00", "Horseback Riding", "Guided ride across grasslands (all levels)."),
                    new TripItineraryItem("18:00", "Camp Dinner", "Group dinner & campfire stories.")
            )));
            days.add(new TripDay("Day 2: Jeep to Zhasylkol", Arrays.asList(
                    new TripItineraryItem("07:00", "Breakfast", "Hearty morning meal."),
                    new TripItineraryItem("08:00", "Jeep Adventure", "Off-road through mountains & meadows."),
                    new TripItineraryItem("11:00", "Zhasylkol Lake", "Emerald lake picnic & photo session."),
                    new TripItineraryItem("14:00", "Return Journey", "Scenic drive back to camp."),
                    new TripItineraryItem("17:00", "Final Dinner", "Farewell group dinner."),
                    new TripItineraryItem("19:00", "Almaty Return", "Drop-off at hotels.")
            )));
            return new TripDetailData(tour,
                    new String[]{"NOMADIC", "HORSEBACK", "JEEP", "BEST SELLER"},
                    "Almaty Region",
                    "2 Days, 1 Night",
                    "~350 km Total",
                    "4x4 Jeep + Minivan",
                    "Max 10 people",
                    days,
                    new String[]{
                            "Round-trip transport from/to Almaty",
                            "Traditional yurt accommodation (1 night)",
                            "English/Russian speaking guide",
                            "All meals (breakfast x2, lunch x2, dinner x2)",
                            "Horseback riding with helmets",
                            "Cooking class & cultural activities",
                            "Jeep tour to Zhasylkol Lake",
                            "Entrance fees & eco-post charges",
                            "Professional photo session"
                    },
                    new String[]{
                            "Flights to/from Almaty",
                            "Travel insurance",
                            "Alcoholic drinks",
                            "Personal souvenirs & expenses"
                    },
                    R.drawable.tour_lepsi_avatar,
                    "67 Reviews",
                    true,
                    180000,
                    "Included",
                    new int[]{R.drawable.header_yurt_camp, R.drawable.tour_lepsi1, R.drawable.tour_lepsi3, R.drawable.tour_lepsi5, R.drawable.tour_lepsi2, R.drawable.tour_lepsi4}
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
                    "Included",
                    new int[]{R.drawable.img_medeu1, R.drawable.img_medeu2, R.drawable.img_shym1, R.drawable.header_koktobe}
            );
        }
        if ("t4".equals(tourId)) {
            List<TripDay> days = Collections.singletonList(new TripDay("Shopping Day", Arrays.asList(
                    new TripItineraryItem("09:00", "MEGA Alma-Ata", "Largest mall with shops, food court and entertainment."),
                    new TripItineraryItem("11:00", "Esentai Mall", "Luxury brands and premium shopping."),
                    new TripItineraryItem("13:00", "Lunch Break", "Lunch at mall food court."),
                    new TripItineraryItem("14:00", "Aport East", "Family shopping and hypermarket."),
                    new TripItineraryItem("16:00", "Dostyk Plaza", "High-end boutiques and return.")
            )));
            return new TripDetailData(tour,
                    new String[]{"SHOPPING", "GROUP TOUR"},
                    "Almaty City",
                    "5 Hours",
                    "~50 km Total",
                    "Minibus",
                    "Small Group",
                    days,
                    new String[]{"Transport", "English-speaking Guide", "Mall entry"},
                    new String[]{"Meals", "Personal shopping"},
                    R.drawable.user_avatar,
                    "156 Reviews",
                    true,
                    10000,
                    "Included",
                    new int[]{R.drawable.header_mega, R.drawable.header_esentai, R.drawable.header_dostyk, R.drawable.img_dostyk1}
            );
        }
        if ("t5".equals(tourId)) {
            List<TripDay> days = Collections.singletonList(new TripDay("Parks Trail", Arrays.asList(
                    new TripItineraryItem("09:00", "Panfilov Park", "Zenkov Cathedral and memorials."),
                    new TripItineraryItem("11:00", "Kok Tobe Hill", "Cable car and city views."),
                    new TripItineraryItem("13:00", "Lunch", "Picnic in park."),
                    new TripItineraryItem("14:00", "First President Park", "Dendropark and viewpoints."),
                    new TripItineraryItem("16:00", "Central Park", "Relax and end tour.")
            )));
            return new TripDetailData(tour,
                    new String[]{"NATURE WALK"},
                    "Almaty",
                    "6 Hours",
                    "~40 km",
                    "Minibus + Walk",
                    "Max 15",
                    days,
                    new String[]{"Transport", "Guide", "Park entries"},
                    new String[]{"Meals", "Cable car extra"},
                    R.drawable.user_avatar,
                    "134 Reviews",
                    true,
                    9000,
                    "Included",
                    new int[]{R.drawable.header_panfilov, R.drawable.header_koktobe, R.drawable.header_first_president_park, R.drawable.header_central_park}
            );
        }
        if ("t6".equals(tourId)) {
            List<TripDay> days = Collections.singletonList(new TripDay("Mosques Walk", Arrays.asList(
                    new TripItineraryItem("10:00", "Central Mosque", "Largest mosque in Almaty."),
                    new TripItineraryItem("11:30", "Aynalyn Mosque", "Historic site visit."),
                    new TripItineraryItem("12:30", "Lunch", "Halal meal nearby."),
                    new TripItineraryItem("13:30", "Beisenova Mosque", "Cultural insights."),
                    new TripItineraryItem("15:00", "Amankul Ata Mosque", "Modern mosque and return.")
            )));
            return new TripDetailData(tour,
                    new String[]{"CULTURAL", "WALKING"},
                    "Almaty City",
                    "4 Hours",
                    "On foot",
                    "Walking",
                    "Max 10",
                    days,
                    new String[]{"Professional guide", "Entrance fees"},
                    new String[]{"Meals", "Personal items"},
                    R.drawable.user_avatar,
                    "98 Reviews",
                    false,
                    7000,
                    "Included",
                    new int[]{R.drawable.header_central_mosque, R.drawable.img_central_mosque2, R.drawable.img_central_mosque4}
            );
        }
        if ("t7".equals(tourId)) {
            List<TripDay> days = Collections.singletonList(new TripDay("Nature Day", Arrays.asList(
                    new TripItineraryItem("07:00", "Big Almaty Lake", "Turquoise alpine lake hike."),
                    new TripItineraryItem("10:00", "Issyk Lake", "Scenic lake and history."),
                    new TripItineraryItem("13:00", "Lunch", "Picnic at Turgen Gorge."),
                    new TripItineraryItem("14:00", "Turgen Gorge Waterfalls", "Bear waterfall short hike."),
                    new TripItineraryItem("16:00", "Tamgaly Tas", "Petroglyphs viewing."),
                    new TripItineraryItem("18:00", "Return to Almaty", "End of tour.")
            )));
            return new TripDetailData(tour,
                    new String[]{"NATURE", "GROUP TOUR"},
                    "Almaty Region",
                    "10 Hours",
                    "~400 km Total",
                    "Comfort Minibus",
                    "Max 12",
                    days,
                    new String[]{"Round-trip transport", "Guide", "Entrance tickets", "Picnic lunch"},
                    new String[]{"Personal expenses", "Extra hikes"},
                    R.drawable.user_avatar,
                    "210 Reviews",
                    true,
                    15000,
                    "Included",
                    new int[]{R.drawable.header_bigalmaty_lake, R.drawable.img_issyk1, R.drawable.header_tamgaly}
            );
        }

        return new TripDetailData(tour, new String[0], "", tour.duration, "", "", tour.groupInfo,
                Collections.emptyList(), new String[0], new String[0], R.drawable.user_avatar, "", false,
                tour.totalPrice, "Included", new int[]{tour.imageResId});
    }

    public static String formatPrice(int price) {
        return String.format(Locale.US, "%,d 〒", price);
    }
}
