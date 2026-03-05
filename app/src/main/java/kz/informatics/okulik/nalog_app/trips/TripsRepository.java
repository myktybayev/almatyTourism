package kz.informatics.okulik.nalog_app.trips;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import kz.informatics.okulik.R;

/**
 * Provides guided tours and (later) self-planned trips.
 * All user-visible text is loaded from strings.xml / arrays.xml for localization.
 */
public class TripsRepository {

    private static TripsRepository instance;
    /** Tour list definitions (resource IDs); resolved with Context in getGuidedTours(Context). */
    private final List<TripListDef> listDefs = new ArrayList<>();

    private TripsRepository(Context context) {
        loadGuidedToursDefinitions();
    }

    public static synchronized TripsRepository getInstance(Context context) {
        if (instance == null) {
            instance = new TripsRepository(context.getApplicationContext());
        }
        return instance;
    }

    /** Returns guided tours with strings resolved for the current locale. */
    public List<GuidedTour> getGuidedTours(Context context) {
        if (context == null) return Collections.emptyList();
        List<GuidedTour> list = new ArrayList<>();
        for (TripListDef d : listDefs) {
            list.add(new GuidedTour(
                    d.id,
                    context.getString(d.nameResId),
                    "",
                    context.getString(d.durationResId),
                    d.imageResId,
                    d.totalPrice,
                    context.getString(d.organizerResId),
                    d.organizerPhone,
                    d.organizerRating,
                    context.getString(d.routeResId),
                    context.getString(d.availabilityResId),
                    context.getString(d.groupResId)
            ));
        }
        return list;
    }

    public GuidedTour getGuidedTourById(Context context, String id) {
        for (GuidedTour t : getGuidedTours(context)) {
            if (t.id != null && t.id.equals(id)) return t;
        }
        return null;
    }

    private static final class TripListDef {
        final String id;
        final int nameResId;
        final int durationResId;
        final int routeResId;
        final int availabilityResId;
        final int groupResId;
        final int organizerResId;
        final int imageResId;
        final int totalPrice;
        final String organizerPhone;
        final float organizerRating;

        TripListDef(String id, int nameResId, int durationResId, int routeResId, int availabilityResId,
                    int groupResId, int organizerResId, int imageResId, int totalPrice, String organizerPhone, float organizerRating) {
            this.id = id;
            this.nameResId = nameResId;
            this.durationResId = durationResId;
            this.routeResId = routeResId;
            this.availabilityResId = availabilityResId;
            this.groupResId = groupResId;
            this.organizerResId = organizerResId;
            this.imageResId = imageResId;
            this.totalPrice = totalPrice;
            this.organizerPhone = organizerPhone;
            this.organizerRating = organizerRating;
        }
    }

    private void loadGuidedToursDefinitions() {
        listDefs.clear();
        listDefs.add(new TripListDef("t1", R.string.trip_t1_name, R.string.trip_t1_duration, R.string.trip_t1_route,
                R.string.trip_t1_availability, R.string.trip_t1_group, R.string.trip_t1_organizer,
                R.drawable.tour1_1, 35000, "77053701025", 4.9f));
        listDefs.add(new TripListDef("t8", R.string.trip_t8_name, R.string.trip_t8_duration, R.string.trip_t8_route,
                R.string.trip_t8_availability, R.string.trip_t8_group, R.string.trip_t8_organizer,
                R.drawable.tour_almarasan, 20000, "77776278800", 4.8f));
        listDefs.add(new TripListDef("t3", R.string.trip_t3_name, R.string.trip_t3_duration, R.string.trip_t3_route,
                R.string.trip_t3_availability, R.string.trip_t3_group, R.string.trip_t3_organizer,
                R.drawable.header_yurt_camp, 180000, "77003808000", 4.9f));
        listDefs.add(new TripListDef("t9", R.string.trip_t9_name, R.string.trip_t9_duration, R.string.trip_t9_route,
                R.string.trip_t9_availability, R.string.trip_t9_group, R.string.trip_t9_organizer,
                R.drawable.tour_glacier, 25000, "77757763287", 4.9f));
        listDefs.add(new TripListDef("t2", R.string.trip_t2_name, R.string.trip_t2_duration, R.string.trip_t2_route,
                R.string.trip_t2_availability, R.string.trip_t2_group, R.string.trip_t2_organizer,
                R.drawable.img_medeu1, 12000, "87071112233", 4.7f));
        listDefs.add(new TripListDef("t4", R.string.trip_t4_name, R.string.trip_t4_duration, R.string.trip_t4_route,
                R.string.trip_t4_availability, R.string.trip_t4_group, R.string.trip_t4_organizer,
                R.drawable.header_mega, 10000, "87071112233", 4.8f));
        listDefs.add(new TripListDef("t5", R.string.trip_t5_name, R.string.trip_t5_duration, R.string.trip_t5_route,
                R.string.trip_t5_availability, R.string.trip_t5_group, R.string.trip_t5_organizer,
                R.drawable.header_panfilov, 9000, "87071112233", 4.7f));
        listDefs.add(new TripListDef("t6", R.string.trip_t6_name, R.string.trip_t6_duration, R.string.trip_t6_route,
                R.string.trip_t6_availability, R.string.trip_t6_group, R.string.trip_t6_organizer,
                R.drawable.header_central_mosque, 7000, "87071112233", 4.9f));
        listDefs.add(new TripListDef("t7", R.string.trip_t7_name, R.string.trip_t7_duration, R.string.trip_t7_route,
                R.string.trip_t7_availability, R.string.trip_t7_group, R.string.trip_t7_organizer,
                R.drawable.header_bigalmaty_lake, 15000, "87071112233", 4.8f));
    }

    /**
     * Full detail for TripsDetailActivity (itinerary, included/not included, organizer, etc.).
     * All text loaded from strings.xml and arrays.xml.
     */
    public TripDetailData getTripDetail(Context context, String tourId) {
        GuidedTour tour = getGuidedTourById(context, tourId);
        if (tour == null || context == null) return null;

        String[] labels = getStringArray(context, "trip_" + tourId + "_labels", "array");
        String location = getStringOrEmpty(context, "trip_" + tourId + "_location");
        String durationLong = getStringOrEmpty(context, "trip_" + tourId + "_duration_long");
        String distance = getStringOrEmpty(context, "trip_" + tourId + "_distance");
        String transport = getStringOrEmpty(context, "trip_" + tourId + "_transport");
        String groupSize = getStringOrEmpty(context, "trip_" + tourId + "_group_size");
        String reviews = getStringOrEmpty(context, "trip_" + tourId + "_reviews");
        String ecoTax = getStringOrEmpty(context, "trip_" + tourId + "_eco_tax");

        if (location.isEmpty()) location = getDefaultLocation(context, tourId);
        if (durationLong.isEmpty()) durationLong = getDefaultDurationLong(context, tourId, tour.duration);
        if (distance.isEmpty()) distance = getDefaultDistance(context, tourId);
        if (transport.isEmpty()) transport = getDefaultTransport(context, tourId);
        if (groupSize.isEmpty()) groupSize = getDefaultGroupSize(context, tourId);
        if (reviews.isEmpty()) reviews = getReviewsString(context, tourId);
        if (ecoTax.isEmpty()) ecoTax = getDefaultEcoTax(context, tourId);

        List<TripDay> itinerary = loadItinerary(context, tourId);
        String[] included = getStringArray(context, "trip_" + tourId + "_included", "array");
        String[] notIncluded = getStringArray(context, "trip_" + tourId + "_not_included", "array");

        int organizerAvatarRes = getOrganizerAvatarRes(tourId);
        boolean organizerVerified = true;

        return new TripDetailData(tour,
                labels != null ? labels : new String[0],
                location,
                durationLong,
                distance,
                transport,
                groupSize,
                itinerary,
                included != null ? included : new String[0],
                notIncluded != null ? notIncluded : new String[0],
                organizerAvatarRes,
                reviews,
                organizerVerified,
                tour.totalPrice,
                ecoTax,
                getGalleryResIds(tourId));
    }

    private List<TripDay> loadItinerary(Context context, String tourId) {
        List<TripDay> days = new ArrayList<>();
        int dayIndex = 1;
        while (true) {
            String titleResName = "trip_" + tourId + "_day" + dayIndex + "_title";
            int titleResId = context.getResources().getIdentifier(titleResName, "string", context.getPackageName());
            if (titleResId == 0) break;
            String dayTitle = context.getString(titleResId);
            String arrayResName = "trip_" + tourId + "_day" + dayIndex + "_items";
            int arrayResId = context.getResources().getIdentifier(arrayResName, "array", context.getPackageName());
            List<TripItineraryItem> items = new ArrayList<>();
            if (arrayResId != 0) {
                String[] raw = context.getResources().getStringArray(arrayResId);
                for (String s : raw) {
                    String[] parts = s.split("\\|\\|", 3);
                    String time = parts.length > 0 ? parts[0].trim() : "";
                    String title = parts.length > 1 ? parts[1].trim() : "";
                    String desc = parts.length > 2 ? parts[2].trim() : "";
                    items.add(new TripItineraryItem(time, title, desc));
                }
            }
            days.add(new TripDay(dayTitle, items));
            dayIndex++;
        }
        return days;
    }

    private String getStringOrEmpty(Context context, String resName) {
        int id = context.getResources().getIdentifier(resName, "string", context.getPackageName());
        return id != 0 ? context.getString(id) : "";
    }

    private String[] getStringArray(Context context, String resName, String defType) {
        int id = context.getResources().getIdentifier(resName, defType, context.getPackageName());
        return id != 0 ? context.getResources().getStringArray(id) : new String[0];
    }

    private String getDefaultLocation(Context context, String tourId) {
        if ("t2".equals(tourId) || "t5".equals(tourId)) return context.getString(R.string.trip_location_almaty);
        if ("t4".equals(tourId) || "t6".equals(tourId)) return context.getString(R.string.trip_location_almaty_city);
        if ("t9".equals(tourId)) return context.getString(R.string.trip_location_almaty_mountains);
        return context.getString(R.string.trip_location_almaty_region);
    }

    private String getDefaultDistance(Context context, String tourId) {
        switch (tourId) {
            case "t1": return context.getString(R.string.trip_distance_600);
            case "t8": return context.getString(R.string.trip_distance_120);
            case "t2": case "t9": return context.getString(R.string.trip_distance_80);
            case "t3": return context.getString(R.string.trip_distance_350);
            case "t4": return context.getString(R.string.trip_distance_50);
            case "t5": return context.getString(R.string.trip_distance_40);
            case "t7": return context.getString(R.string.trip_distance_400);
            default: return "";
        }
    }

    private String getDefaultTransport(Context context, String tourId) {
        switch (tourId) {
            case "t1": case "t7": return context.getString(R.string.trip_transport_comfort_minibus);
            case "t8": return context.getString(R.string.trip_transport_minivan);
            case "t9": return context.getString(R.string.trip_transport_vehicle);
            case "t3": return context.getString(R.string.trip_transport_jeep);
            case "t2": case "t4": return context.getString(R.string.trip_transport_minibus_small);
            case "t5": return context.getString(R.string.trip_transport_walk);
            case "t6": return context.getString(R.string.trip_transport_walking);
            default: return context.getString(R.string.trip_transport_minibus_small);
        }
    }

    private String getDefaultDurationLong(Context context, String tourId, String fallback) {
        if ("t1".equals(tourId) || "t3".equals(tourId)) return context.getString(R.string.trip_duration_2d1n);
        return fallback;
    }

    private String getDefaultGroupSize(Context context, String tourId) {
        switch (tourId) {
            case "t1": case "t8": return context.getString(R.string.trip_group_max15);
            case "t9": case "t7": return context.getString(R.string.trip_group_max12);
            case "t3": return context.getString(R.string.trip_group_max10);
            case "t2": case "t4": return context.getString(R.string.trip_group_small);
            case "t5": return context.getString(R.string.trip_group_max15_short);
            case "t6": return context.getString(R.string.trip_group_max10_short);
            default: return "";
        }
    }

    private String getReviewsString(Context context, String tourId) {
        int id = context.getResources().getIdentifier("trip_" + tourId + "_reviews", "string", context.getPackageName());
        return id != 0 ? context.getString(id) : "";
    }

    private String getDefaultEcoTax(Context context, String tourId) {
        if ("t9".equals(tourId)) return context.getString(R.string.trip_eco_not_included);
        return context.getString(R.string.trip_eco_included);
    }

    private int getOrganizerAvatarRes(String tourId) {
        switch (tourId) {
            case "t1": return R.drawable.tour_panda_avatar;
            case "t8": return R.drawable.tour_kaz_avatar;
            case "t9": return R.drawable.tour_adam_avatar;
            case "t3": return R.drawable.tour_lepsi_avatar;
            default: return R.drawable.user_avatar;
        }
    }

    private int[] getGalleryResIds(String tourId) {
        switch (tourId) {
            case "t1": return new int[]{R.drawable.tour1_1, R.drawable.tour1_2, R.drawable.tour1_3, R.drawable.tour1_4, R.drawable.tour1_5, R.drawable.tour1_7, R.drawable.tour1_8, R.drawable.tour1_6};
            case "t8": return new int[]{R.drawable.tour_almarasan, R.drawable.tour_almarasan2, R.drawable.tour_almarasan3, R.drawable.tour_almarasan4, R.drawable.tour_almarasan5, R.drawable.tour_almarasan6};
            case "t9": return new int[]{R.drawable.tour_glacier, R.drawable.tour_glacier3, R.drawable.tour_glacier2, R.drawable.tour_glacier4, R.drawable.tour_glacier5, R.drawable.tour_glacier8, R.drawable.tour_glacier6, R.drawable.tour_glacier7};
            case "t3": return new int[]{R.drawable.header_yurt_camp, R.drawable.tour_lepsi1, R.drawable.tour_lepsi3, R.drawable.tour_lepsi5, R.drawable.tour_lepsi2, R.drawable.tour_lepsi4};
            case "t2": return new int[]{R.drawable.img_medeu1, R.drawable.img_medeu2, R.drawable.img_shym1, R.drawable.header_koktobe};
            case "t4": return new int[]{R.drawable.header_mega, R.drawable.header_esentai, R.drawable.header_dostyk, R.drawable.img_dostyk1};
            case "t5": return new int[]{R.drawable.header_panfilov, R.drawable.header_koktobe, R.drawable.header_first_president_park, R.drawable.header_central_park};
            case "t6": return new int[]{R.drawable.header_central_mosque, R.drawable.img_central_mosque2, R.drawable.img_central_mosque4};
            case "t7": return new int[]{R.drawable.header_bigalmaty_lake, R.drawable.img_issyk1, R.drawable.header_tamgaly};
            default: return new int[0];
        }
    }

    public static String formatPrice(int price) {
        return String.format(Locale.US, "%,d 〒", price);
    }
}
