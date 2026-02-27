package kz.informatics.okulik.nalog_app.hotels.module;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kz.informatics.okulik.R;

/**
 * Single source of all hotel data. Holds definitions with resource IDs;
 * strings are resolved at get-time using Context (like DestinationsRepository) so language changes apply.
 */
public class HotelsRepository {

    private static HotelsRepository instance;
    private final List<HotelDefinition> definitions = new ArrayList<>();

    private static final List<String> DEFAULT_AVAILABLE_DAYS = Arrays.asList(
            "2026-03-01", "2026-03-02", "2026-03-03", "2026-03-04", "2026-03-05", "2026-03-06", "2026-03-07",
            "2026-03-08", "2026-03-09", "2026-03-10", "2026-03-11", "2026-03-12", "2026-03-13", "2026-03-14",
            "2026-03-15", "2026-03-16", "2026-03-17", "2026-03-18", "2026-03-19", "2026-03-20", "2026-03-21",
            "2026-03-22", "2026-03-23", "2026-03-24", "2026-03-25", "2026-03-26", "2026-03-27", "2026-03-28",
            "2026-03-29", "2026-03-30", "2026-03-31",
            "2026-04-01", "2026-04-02", "2026-04-03", "2026-04-04", "2026-04-05", "2026-04-06", "2026-04-07",
            "2026-04-08", "2026-04-09", "2026-04-10", "2026-04-11", "2026-04-12", "2026-04-13", "2026-04-14",
            "2026-04-15", "2026-04-16", "2026-04-17", "2026-04-18", "2026-04-19", "2026-04-20", "2026-04-21",
            "2026-04-22", "2026-04-23", "2026-04-24", "2026-04-25", "2026-04-26", "2026-04-27", "2026-04-28",
            "2026-04-29", "2026-04-30",
            "2026-05-01", "2026-05-02", "2026-05-03", "2026-05-04", "2026-05-05", "2026-05-06", "2026-05-07",
            "2026-05-08", "2026-05-09", "2026-05-10", "2026-05-11", "2026-05-12", "2026-05-13", "2026-05-14",
            "2026-05-15", "2026-05-16", "2026-05-17", "2026-05-18", "2026-05-19", "2026-05-20", "2026-05-21",
            "2026-05-22", "2026-05-23", "2026-05-24", "2026-05-25", "2026-05-26", "2026-05-27", "2026-05-28",
            "2026-05-29", "2026-05-30", "2026-05-31"
    );

    public static HotelsRepository getInstance() {
        if (instance == null) {
            instance = new HotelsRepository();
        }
        return instance;
    }

    private HotelsRepository() {
        initDefinitions();
    }

    private void initDefinitions() {
        definitions.clear();

        // Rixos Almaty
        List<HotelRoomDefinition> rixosRooms = Arrays.asList(
                new HotelRoomDefinition("r1", R.string.room_deluxe_king, R.drawable.deluxe_room, 85000,
                        new int[]{R.string.room_sqm_format, R.string.room_feature_king_bed, R.string.room_feature_garden_view},
                        new String[]{"42", null, null}),
                new HotelRoomDefinition("r2", R.string.room_deluxe_twin, R.drawable.premium_room, 90000,
                        new int[]{R.string.room_sqm_format, R.string.room_feature_2_queen_beds, R.string.room_feature_city_view},
                        new String[]{"48", null, null}),
                new HotelRoomDefinition("r3", R.string.room_royal_suite, R.drawable.junior_suite, 150000,
                        new int[]{R.string.room_sqm_format, R.string.room_feature_king_bed, R.string.room_feature_separate_living, R.string.room_feature_spa_access},
                        new String[]{"65", null, null, null})
        );
        List<HotelAddOnDefinition> rixosAddOns = Arrays.asList(
                new HotelAddOnDefinition("a1", R.string.addon_royal_breakfast, R.drawable.ic_breakfast, 12000, true),
                new HotelAddOnDefinition("a2", R.string.addon_vip_transfer, R.drawable.ic_car, 15000, false)
        );
        definitions.add(new HotelDefinition(
                "h1",
                R.string.hotel_rixos_name, R.string.hotel_rixos_address, R.string.hotel_rixos_desc,
                4.9f, R.drawable.header_rixos_almaty,
                new int[]{R.drawable.img_rixos1, R.drawable.img_rixos2, R.drawable.img_rixos3},
                new int[]{R.string.hotel_facility_wifi, R.string.hotel_facility_dining, R.string.hotel_facility_pool, R.string.hotel_facility_spa},
                "43.258611,76.945000", 85000, new ArrayList<>(DEFAULT_AVAILABLE_DAYS),
                rixosRooms, rixosAddOns
        ));

        // Hotel Kazakhstan
        List<HotelRoomDefinition> kzRooms = Arrays.asList(
                new HotelRoomDefinition("k1", R.string.room_standard, R.drawable.standard_room, 35000,
                        new int[]{R.string.room_sqm_format, R.string.room_feature_double_bed, R.string.room_feature_city_view},
                        new String[]{"28", null, null}),
                new HotelRoomDefinition("k2", R.string.room_superior, R.drawable.superior_room, 42000,
                        new int[]{R.string.room_sqm_format, R.string.room_feature_king_bed, R.string.room_feature_mountain_view},
                        new String[]{"35", null, null})
        );
        List<HotelAddOnDefinition> kzAddOns = Arrays.asList(
                new HotelAddOnDefinition("k1a", R.string.addon_breakfast, R.drawable.ic_breakfast, 8000, true)
        );
        definitions.add(new HotelDefinition(
                "h2",
                R.string.hotel_kz_name, R.string.hotel_kz_address, R.string.hotel_kz_desc,
                4.6f, R.drawable.header_hotel_kaz,
                new int[]{R.drawable.img_hotel_kz1, R.drawable.img_hotel_kz2, R.drawable.img_hotel_kz3},
                new int[]{R.string.hotel_facility_wifi, R.string.hotel_facility_dining},
                "43.250000,76.950000", 35000, new ArrayList<>(DEFAULT_AVAILABLE_DAYS),
                kzRooms, kzAddOns
        ));

        // Novotel City Center
        List<HotelRoomDefinition> novotelRooms = Arrays.asList(
                new HotelRoomDefinition("n1", R.string.room_standard_double, R.drawable.standard_room2, 45000,
                        new int[]{R.string.room_sqm_format, R.string.room_feature_double_bed, R.string.room_feature_city_view},
                        new String[]{"30", null, null}),
                new HotelRoomDefinition("n2", R.string.room_executive, R.drawable.executive_suite, 55000,
                        new int[]{R.string.room_sqm_format, R.string.room_feature_king_bed, R.string.room_feature_mountain_view},
                        new String[]{"40", null, null})
        );
        List<HotelAddOnDefinition> novotelAddOns = Arrays.asList(
                new HotelAddOnDefinition("n1a", R.string.addon_breakfast, R.drawable.ic_breakfast, 10000, true),
                new HotelAddOnDefinition("n2a", R.string.addon_airport_transfer, R.drawable.ic_car, 12000, false)
        );
        definitions.add(new HotelDefinition(
                "h3",
                R.string.hotel_novotel_name, R.string.hotel_novotel_address, R.string.hotel_novotel_desc,
                4.8f, R.drawable.header_novotel,
                new int[]{R.drawable.img_novotel1, R.drawable.img_novotel2, R.drawable.img_novotel3},
                new int[]{R.string.hotel_facility_wifi, R.string.hotel_facility_dining, R.string.hotel_facility_pool, R.string.hotel_facility_spa},
                "43.248000,76.945000", 45000, new ArrayList<>(DEFAULT_AVAILABLE_DAYS),
                novotelRooms, novotelAddOns
        ));

        // Grand Aura Hotel
        List<HotelRoomDefinition> grandAuraRooms = Arrays.asList(
                new HotelRoomDefinition("ga1", R.string.room_deluxe_king, R.drawable.deluxe_room, 60000,
                        new int[]{R.string.room_sqm_format, R.string.room_feature_king_bed, R.string.room_feature_city_view},
                        new String[]{"35", null, null}),
                new HotelRoomDefinition("ga2", R.string.room_superior_twin, R.drawable.superior_room2, 65000,
                        new int[]{R.string.room_sqm_format, R.string.room_feature_2_queen_beds, R.string.room_feature_street_view},
                        new String[]{"40", null, null}),
                new HotelRoomDefinition("ga3", R.string.room_junior_suite, R.drawable.junior_suite, 95000,
                        new int[]{R.string.room_sqm_format, R.string.room_feature_king_bed, R.string.room_feature_living_area, R.string.room_feature_breakfast_incl},
                        new String[]{"55", null, null, null})
        );
        List<HotelAddOnDefinition> grandAuraAddOns = Arrays.asList(
                new HotelAddOnDefinition("gaa1", R.string.addon_breakfast_buffet, R.drawable.ic_breakfast, 8000, true),
                new HotelAddOnDefinition("gaa2", R.string.addon_airport_shuttle, R.drawable.ic_car, 12000, false)
        );
        definitions.add(new HotelDefinition(
                "h2",
                R.string.hotel_grand_aura_name, R.string.hotel_grand_aura_address, R.string.hotel_grand_aura_desc,
                4.8f, R.drawable.header_grand_aura,
                new int[]{R.drawable.img_grand_aura1, R.drawable.img_grand_aura2, R.drawable.img_grand_aura3},
                new int[]{R.string.hotel_facility_wifi, R.string.hotel_facility_restaurant, R.string.hotel_facility_gym, R.string.hotel_facility_parking},
                "43.250000,76.920000", 60000, new ArrayList<>(DEFAULT_AVAILABLE_DAYS),
                grandAuraRooms, grandAuraAddOns
        ));

        // Royal Tulip Almaty
        List<HotelRoomDefinition> royalRooms = Arrays.asList(
                new HotelRoomDefinition("rt1", R.string.room_standard, R.drawable.standard_room2, 55000,
                        new int[]{R.string.room_sqm_format, R.string.room_feature_queen_bed, R.string.room_feature_mountain_view},
                        new String[]{"30", null, null}),
                new HotelRoomDefinition("rt2", R.string.room_deluxe_double, R.drawable.deluxe_room, 70000,
                        new int[]{R.string.room_sqm_format, R.string.room_feature_2_beds, R.string.room_feature_balcony},
                        new String[]{"45", null, null}),
                new HotelRoomDefinition("rt3", R.string.room_suite, R.drawable.superior_room2, 120000,
                        new int[]{R.string.room_sqm_format, R.string.room_feature_king_bed, R.string.room_feature_jacuzzi},
                        new String[]{"70", null, null})
        );
        List<HotelAddOnDefinition> royalAddOns = Arrays.asList(
                new HotelAddOnDefinition("rta1", R.string.addon_spa_package, R.drawable.ic_spa, 15000, false),
                new HotelAddOnDefinition("rta2", R.string.addon_transfer_medeu, R.drawable.ic_car, 10000, true)
        );
        definitions.add(new HotelDefinition(
                "h3",
                R.string.hotel_royal_tulip_name, R.string.hotel_royal_tulip_address, R.string.hotel_royal_tulip_desc,
                4.6f, R.drawable.header_royal_tulip,
                new int[]{R.drawable.img_royal_tulip1, R.drawable.img_royal_tulip2, R.drawable.img_royal_tulip3},
                new int[]{R.string.hotel_facility_pool, R.string.hotel_facility_spa, R.string.hotel_facility_ski, R.string.hotel_facility_wifi},
                "43.210000,76.910000", 55000, new ArrayList<>(DEFAULT_AVAILABLE_DAYS),
                royalRooms, royalAddOns
        ));

        // Swissôtel Wellness Resort Alatau
        List<HotelRoomDefinition> swissRooms = Arrays.asList(
                new HotelRoomDefinition("s1", R.string.room_classic_room, R.drawable.standard_room, 75000,
                        new int[]{R.string.room_sqm_format, R.string.room_feature_king_bed, R.string.room_feature_forest_view},
                        new String[]{"40", null, null}),
                new HotelRoomDefinition("s2", R.string.room_premier_twin, R.drawable.premium_room, 85000,
                        new int[]{R.string.room_sqm_format, R.string.room_feature_2_beds, R.string.room_feature_terrace},
                        new String[]{"50", null, null}),
                new HotelRoomDefinition("s3", R.string.room_wellness_suite, R.drawable.suite, 140000,
                        new int[]{R.string.room_sqm_format, R.string.room_feature_spa_bath, R.string.room_feature_private_terrace},
                        new String[]{"80", null, null})
        );
        List<HotelAddOnDefinition> swissAddOns = Arrays.asList(
                new HotelAddOnDefinition("sa1", R.string.addon_wellness_breakfast, R.drawable.ic_breakfast, 10000, true),
                new HotelAddOnDefinition("sa2", R.string.addon_ski_lift_pass, R.drawable.ski, 20000, false)
        );
        definitions.add(new HotelDefinition(
                "h4",
                R.string.hotel_swissotel_name, R.string.hotel_swissotel_address, R.string.hotel_swissotel_desc,
                4.7f, R.drawable.header_hotel_resort2,
                new int[]{R.drawable.img_alatau1, R.drawable.img_alatau2, R.drawable.img_alatau3},
                new int[]{R.string.hotel_facility_spa, R.string.hotel_facility_pool, R.string.hotel_facility_gym, R.string.hotel_facility_hiking},
                "43.196330,76.812270", 75000, new ArrayList<>(DEFAULT_AVAILABLE_DAYS),
                swissRooms, swissAddOns
        ));

        // Qazaq Auyl Eco Hotel
        List<HotelRoomDefinition> qazaqRooms = Arrays.asList(
                new HotelRoomDefinition("q1", R.string.room_eco_room, R.drawable.deluxe_room, 45000,
                        new int[]{R.string.room_sqm_format, R.string.room_feature_double_bed, R.string.room_feature_nature_view},
                        new String[]{"25", null, null}),
                new HotelRoomDefinition("q2", R.string.room_family_room, R.drawable.room4, 55000,
                        new int[]{R.string.room_sqm_format, R.string.room_feature_2_beds, R.string.room_feature_yurt_style},
                        new String[]{"35", null, null}),
                new HotelRoomDefinition("q3", R.string.room_premium_cottage, R.drawable.premium_room, 90000,
                        new int[]{R.string.room_sqm_format, R.string.room_feature_king_bed, R.string.room_feature_private_yard},
                        new String[]{"60", null, null})
        );
        List<HotelAddOnDefinition> qazaqAddOns = Arrays.asList(
                new HotelAddOnDefinition("qa1", R.string.addon_national_breakfast, R.drawable.ic_breakfast, 7000, true),
                new HotelAddOnDefinition("qa2", R.string.addon_horse_riding, R.drawable.ic_horse, 15000, false)
        );
        definitions.add(new HotelDefinition(
                "h5",
                R.string.hotel_qazaq_auyl_name, R.string.hotel_qazaq_auyl_address, R.string.hotel_qazaq_auyl_desc,
                4.5f, R.drawable.header_hotel_resort3,
                new int[]{R.drawable.img_eco1, R.drawable.img_eco2, R.drawable.img_eco3},
                new int[]{R.string.hotel_facility_nature, R.string.hotel_facility_restaurant, R.string.hotel_facility_parking, R.string.hotel_facility_wifi},
                "43.200000,76.900000", 45000, new ArrayList<>(DEFAULT_AVAILABLE_DAYS),
                qazaqRooms, qazaqAddOns
        ));

        // The Ritz-Carlton Almaty
        List<HotelRoomDefinition> ritzRooms = Arrays.asList(
                new HotelRoomDefinition("rc1", R.string.room_deluxe_king_short, R.drawable.deluxe_room, 120000,
                        new int[]{R.string.room_sqm_format, R.string.room_feature_king_bed, R.string.room_feature_mountain_view},
                        new String[]{"50", null, null}),
                new HotelRoomDefinition("rc2", R.string.room_club_twin, R.drawable.superior_room, 140000,
                        new int[]{R.string.room_sqm_format, R.string.room_feature_2_beds, R.string.room_feature_club_lounge},
                        new String[]{"60", null, null}),
                new HotelRoomDefinition("rc3", R.string.room_ritz_suite, R.drawable.superior_room2, 250000,
                        new int[]{R.string.room_sqm_format, R.string.room_feature_panorama_view, R.string.room_feature_butler_service},
                        new String[]{"120", null, null})
        );
        List<HotelAddOnDefinition> ritzAddOns = Arrays.asList(
                new HotelAddOnDefinition("rca1", R.string.addon_club_lounge_access, R.drawable.ic_lounge, 25000, false),
                new HotelAddOnDefinition("rca2", R.string.addon_heli_transfer, R.drawable.ic_heli, 50000, false)
        );
        definitions.add(new HotelDefinition(
                "h6",
                R.string.hotel_ritz_name, R.string.hotel_ritz_address, R.string.hotel_ritz_desc,
                4.7f, R.drawable.header_hotel_mountain1,
                new int[]{R.drawable.img_ritz1, R.drawable.img_ritz2, R.drawable.img_ritz3},
                new int[]{R.string.hotel_facility_spa, R.string.hotel_facility_pool, R.string.hotel_facility_fine_dining, R.string.hotel_facility_fitness},
                "43.219078,76.929240", 120000, new ArrayList<>(DEFAULT_AVAILABLE_DAYS),
                ritzRooms, ritzAddOns
        ));

        // Shymbulak Resort Hotel & Spa
        List<HotelRoomDefinition> shymRooms = Arrays.asList(
                new HotelRoomDefinition("sh1", R.string.room_standard, R.drawable.standard_room, 80000,
                        new int[]{R.string.room_sqm_format, R.string.room_feature_queen_bed, R.string.room_feature_ski_view},
                        new String[]{"35", null, null}),
                new HotelRoomDefinition("sh2", R.string.room_deluxe, R.drawable.deluxe_room, 95000,
                        new int[]{R.string.room_sqm_format, R.string.room_feature_king_bed, R.string.room_feature_balcony},
                        new String[]{"45", null, null}),
                new HotelRoomDefinition("sh3", R.string.room_apartment_suite, R.drawable.junior_suite, 160000,
                        new int[]{R.string.room_sqm_format, R.string.room_feature_kitchenette, R.string.room_feature_mountain_view},
                        new String[]{"75", null, null})
        );
        List<HotelAddOnDefinition> shymAddOns = Arrays.asList(
                new HotelAddOnDefinition("sha1", R.string.addon_ski_lift_pass, R.drawable.ic_ski, 18000, true),
                new HotelAddOnDefinition("sha2", R.string.addon_spa_day, R.drawable.ic_spa, 20000, false)
        );
        definitions.add(new HotelDefinition(
                "h7",
                R.string.hotel_shymbulak_name, R.string.hotel_shymbulak_address, R.string.hotel_shymbulak_desc,
                4.6f, R.drawable.header_hotel_mountain2,
                new int[]{R.drawable.img_shym1, R.drawable.img_shym2, R.drawable.img_shym3},
                new int[]{R.string.hotel_facility_ski, R.string.hotel_facility_spa, R.string.hotel_facility_pool, R.string.hotel_facility_restaurant},
                "43.170000,76.880000", 80000, new ArrayList<>(DEFAULT_AVAILABLE_DAYS),
                shymRooms, shymAddOns
        ));

        // InterContinental Almaty
        List<HotelRoomDefinition> interRooms = Arrays.asList(
                new HotelRoomDefinition("i1", R.string.room_classic_king, R.drawable.standard_room, 70000,
                        new int[]{R.string.room_sqm_format, R.string.room_feature_king_bed, R.string.room_feature_city_mountain_view},
                        new String[]{"38", null, null}),
                new HotelRoomDefinition("i2", R.string.room_club_room, R.drawable.superior_room2, 90000,
                        new int[]{R.string.room_sqm_format, R.string.room_feature_2_beds, R.string.room_feature_club_benefits},
                        new String[]{"48", null, null}),
                new HotelRoomDefinition("i3", R.string.room_executive_suite, R.drawable.executive_suite, 150000,
                        new int[]{R.string.room_sqm_format, R.string.room_feature_separate_living, R.string.room_feature_executive_lounge},
                        new String[]{"90", null, null})
        );
        List<HotelAddOnDefinition> interAddOns = Arrays.asList(
                new HotelAddOnDefinition("ia1", R.string.addon_breakfast_incl, R.drawable.ic_breakfast, 12000, true),
                new HotelAddOnDefinition("ia2", R.string.addon_gym_pool_access, R.drawable.ic_pool, 0, true)
        );
        definitions.add(new HotelDefinition(
                "h8",
                R.string.hotel_inter_name, R.string.hotel_inter_address, R.string.hotel_inter_desc,
                4.4f, R.drawable.header_hotel_mountain3,
                new int[]{R.drawable.img_inter1, R.drawable.img_inter2, R.drawable.img_inter3},
                new int[]{R.string.hotel_facility_business, R.string.hotel_facility_pool, R.string.hotel_facility_gym, R.string.hotel_facility_dining},
                "43.235556,76.940000", 70000, new ArrayList<>(DEFAULT_AVAILABLE_DAYS),
                interRooms, interAddOns
        ));
    }

    private Hotel buildHotel(Context context, HotelDefinition def) {
        String[] facilities = new String[def.facilityResIds.length];
        for (int i = 0; i < def.facilityResIds.length; i++) {
            facilities[i] = context.getString(def.facilityResIds[i]);
        }
        List<HotelRoom> rooms = new ArrayList<>();
        for (HotelRoomDefinition rd : def.rooms) {
            rooms.add(buildRoom(context, rd));
        }
        List<HotelAddOn> addOns = new ArrayList<>();
        for (HotelAddOnDefinition ad : def.addOns) {
            addOns.add(buildAddOn(context, ad));
        }
        return new Hotel(
                def.id,
                context.getString(def.nameResId),
                context.getString(def.addressResId),
                context.getString(def.descriptionResId),
                def.rating,
                def.imageRes,
                def.galleryResIds,
                facilities,
                def.location,
                def.pricePerNight,
                def.availableDays,
                rooms,
                addOns
        );
    }

    private HotelRoom buildRoom(Context context, HotelRoomDefinition rd) {
        int len = Math.min(rd.featureResIds.length, rd.featureFormatArgs.length);
        String[] features = new String[len];
        for (int i = 0; i < len; i++) {
            if (rd.featureFormatArgs[i] != null) {
                features[i] = context.getString(rd.featureResIds[i], rd.featureFormatArgs[i]);
            } else {
                features[i] = context.getString(rd.featureResIds[i]);
            }
        }
        return new HotelRoom(
                rd.id,
                context.getString(rd.nameResId),
                rd.imageRes,
                rd.pricePerNight,
                features
        );
    }

    private HotelAddOn buildAddOn(Context context, HotelAddOnDefinition ad) {
        return new HotelAddOn(
                ad.id,
                context.getString(ad.nameResId),
                ad.iconRes,
                ad.price,
                ad.perGuest
        );
    }

    public List<Hotel> getAllHotels(Context context) {
        List<Hotel> result = new ArrayList<>();
        for (HotelDefinition def : definitions) {
            result.add(buildHotel(context, def));
        }
        return result;
    }

    public List<Hotel> getHotelsByAvailability(Context context, String checkIn, String checkOut) {
        List<Hotel> result = new ArrayList<>();
        for (HotelDefinition def : definitions) {
            if (def.isAvailableForDates(checkIn, checkOut)) {
                result.add(buildHotel(context, def));
            }
        }
        return result;
    }

    public Hotel getHotelById(Context context, String id) {
        for (HotelDefinition def : definitions) {
            if (def.id.equals(id)) {
                return buildHotel(context, def);
            }
        }
        return null;
    }
}
