package kz.informatics.okulik.nalog_app.hotels.module;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kz.informatics.okulik.R;

/**
 * Single source of all hotel data. Add new hotels here.
 */
public class HotelsRepository {

    private static HotelsRepository instance;
    private final List<Hotel> hotels = new ArrayList<>();

    public static HotelsRepository getInstance() {
        if (instance == null) {
            instance = new HotelsRepository();
        }
        return instance;
    }

    private HotelsRepository() {
        initHotels();
    }

    private void initHotels() {
        hotels.clear();

        // Rixos Almaty
        List<String> rixosDays = Arrays.asList(
                // March 2026
                "2026-03-01","2026-03-02","2026-03-03","2026-03-04","2026-03-05","2026-03-06","2026-03-07",
                "2026-03-08","2026-03-09","2026-03-10","2026-03-11","2026-03-12","2026-03-13","2026-03-14",
                "2026-03-15","2026-03-16","2026-03-17","2026-03-18","2026-03-19","2026-03-20","2026-03-21",
                "2026-03-22","2026-03-23","2026-03-24","2026-03-25","2026-03-26","2026-03-27","2026-03-28",
                "2026-03-29","2026-03-30","2026-03-31",

                // April 2026 (30 days)
                "2026-04-01","2026-04-02","2026-04-03","2026-04-04","2026-04-05","2026-04-06","2026-04-07",
                "2026-04-08","2026-04-09","2026-04-10","2026-04-11","2026-04-12","2026-04-13","2026-04-14",
                "2026-04-15","2026-04-16","2026-04-17","2026-04-18","2026-04-19","2026-04-20","2026-04-21",
                "2026-04-22","2026-04-23","2026-04-24","2026-04-25","2026-04-26","2026-04-27","2026-04-28",
                "2026-04-29","2026-04-30",

                // May 2026 (31 days)
                "2026-05-01","2026-05-02","2026-05-03","2026-05-04","2026-05-05","2026-05-06","2026-05-07",
                "2026-05-08","2026-05-09","2026-05-10","2026-05-11","2026-05-12","2026-05-13","2026-05-14",
                "2026-05-15","2026-05-16","2026-05-17","2026-05-18","2026-05-19","2026-05-20","2026-05-21",
                "2026-05-22","2026-05-23","2026-05-24","2026-05-25","2026-05-26","2026-05-27","2026-05-28",
                "2026-05-29","2026-05-30","2026-05-31"
        );

        List<HotelRoom> rixosRooms = Arrays.asList(
                new HotelRoom("r1", "Deluxe King Room", R.drawable.img_esentai1, 85000,
                        new String[]{"42 m²", "King Bed", "Garden View"}),
                new HotelRoom("r2", "Deluxe Twin Room", R.drawable.img_esentai2, 90000,
                        new String[]{"48 m²", "2 Queen Beds", "City View"}),
                new HotelRoom("r3", "Royal Suite", R.drawable.img_esentai3, 150000,
                        new String[]{"65 m²", "King Bed", "Separate Living", "Spa Access"})
        );
        List<HotelAddOn> rixosAddOns = Arrays.asList(
                new HotelAddOn("a1", "Royal Breakfast", R.drawable.ic_breakfast, 12000, true),
                new HotelAddOn("a2", "VIP Airport Transfer", R.drawable.ic_car, 15000, false)
        );
        hotels.add(new Hotel(
                "h1",
                "Rixos Almaty",
                "Seifullin Avenue, 506",
                "Experience world-class luxury in the heart of Almaty. The Rixos Almaty Hotel offers a unique combination of comfort and traditional Kazakh hospitality. Enjoy spacious rooms, premium dining options, and a state-of-the-art spa center.",
                4.9f,
                R.drawable.header_esentai,
                new int[]{R.drawable.img_esentai1, R.drawable.img_esentai2, R.drawable.img_esentai3},
                new String[]{"wifi", "dining", "pool", "spa"},
                "43.258611,76.945000",
                85000,
                rixosDays,
                rixosRooms,
                rixosAddOns
        ));

        // Hotel Kazakhstan
        List<String> kzDays = Arrays.asList( // March 2026
                "2026-03-01","2026-03-02","2026-03-03","2026-03-04","2026-03-05","2026-03-06","2026-03-07",
                "2026-03-08","2026-03-09","2026-03-10","2026-03-11","2026-03-12","2026-03-13","2026-03-14",
                "2026-03-15","2026-03-16","2026-03-17","2026-03-18","2026-03-19","2026-03-20","2026-03-21",
                "2026-03-22","2026-03-23","2026-03-24","2026-03-25","2026-03-26","2026-03-27","2026-03-28",
                "2026-03-29","2026-03-30","2026-03-31",

                // April 2026 (30 days)
                "2026-04-01","2026-04-02","2026-04-03","2026-04-04","2026-04-05","2026-04-06","2026-04-07",
                "2026-04-08","2026-04-09","2026-04-10","2026-04-11","2026-04-12","2026-04-13","2026-04-14",
                "2026-04-15","2026-04-16","2026-04-17","2026-04-18","2026-04-19","2026-04-20","2026-04-21",
                "2026-04-22","2026-04-23","2026-04-24","2026-04-25","2026-04-26","2026-04-27","2026-04-28",
                "2026-04-29","2026-04-30",

                // May 2026 (31 days)
                "2026-05-01","2026-05-02","2026-05-03","2026-05-04","2026-05-05","2026-05-06","2026-05-07",
                "2026-05-08","2026-05-09","2026-05-10","2026-05-11","2026-05-12","2026-05-13","2026-05-14",
                "2026-05-15","2026-05-16","2026-05-17","2026-05-18","2026-05-19","2026-05-20","2026-05-21",
                "2026-05-22","2026-05-23","2026-05-24","2026-05-25","2026-05-26","2026-05-27","2026-05-28",
                "2026-05-29","2026-05-30","2026-05-31"
        );
        List<HotelRoom> kzRooms = Arrays.asList(
                new HotelRoom("k1", "Standard Room", R.drawable.img_palace1, 35000,
                        new String[]{"28 m²", "Double Bed", "City View"}),
                new HotelRoom("k2", "Superior Room", R.drawable.img_palace2, 42000,
                        new String[]{"35 m²", "King Bed", "Mountain View"})
        );
        List<HotelAddOn> kzAddOns = Arrays.asList(
                new HotelAddOn("k1a", "Breakfast", R.drawable.ic_breakfast, 8000, true)
        );
        hotels.add(new Hotel(
                "h2",
                "Hotel Kazakhstan",
                "Dostyk Avenue, 52",
                "A historic landmark in the heart of Almaty. Hotel Kazakhstan offers stunning views of the mountains and the city. Perfect blend of Soviet-era grandeur and modern comfort.",
                4.6f,
                R.drawable.header_palace,
                new int[]{R.drawable.img_palace1, R.drawable.img_palace2, R.drawable.img_palace3},
                new String[]{"wifi", "dining"},
                "43.250000,76.950000",
                35000,
                kzDays,
                kzRooms,
                kzAddOns
        ));

        // Novotel City Center
        List<String> novotelDays = Arrays.asList(
                // March 2026
                "2026-03-01","2026-03-02","2026-03-03","2026-03-04","2026-03-05","2026-03-06","2026-03-07",
                "2026-03-08","2026-03-09","2026-03-10","2026-03-11","2026-03-12","2026-03-13","2026-03-14",
                "2026-03-15","2026-03-16","2026-03-17","2026-03-18","2026-03-19","2026-03-20","2026-03-21",
                "2026-03-22","2026-03-23","2026-03-24","2026-03-25","2026-03-26","2026-03-27","2026-03-28",
                "2026-03-29","2026-03-30","2026-03-31",

                // April 2026 (30 days)
                "2026-04-01","2026-04-02","2026-04-03","2026-04-04","2026-04-05","2026-04-06","2026-04-07",
                "2026-04-08","2026-04-09","2026-04-10","2026-04-11","2026-04-12","2026-04-13","2026-04-14",
                "2026-04-15","2026-04-16","2026-04-17","2026-04-18","2026-04-19","2026-04-20","2026-04-21",
                "2026-04-22","2026-04-23","2026-04-24","2026-04-25","2026-04-26","2026-04-27","2026-04-28",
                "2026-04-29","2026-04-30",

                // May 2026 (31 days)
                "2026-05-01","2026-05-02","2026-05-03","2026-05-04","2026-05-05","2026-05-06","2026-05-07",
                "2026-05-08","2026-05-09","2026-05-10","2026-05-11","2026-05-12","2026-05-13","2026-05-14",
                "2026-05-15","2026-05-16","2026-05-17","2026-05-18","2026-05-19","2026-05-20","2026-05-21",
                "2026-05-22","2026-05-23","2026-05-24","2026-05-25","2026-05-26","2026-05-27","2026-05-28",
                "2026-05-29","2026-05-30","2026-05-31"
        );
        List<HotelRoom> novotelRooms = Arrays.asList(
                new HotelRoom("n1", "Standard Double", R.drawable.img_mega1, 45000,
                        new String[]{"30 m²", "Double Bed", "City View"}),
                new HotelRoom("n2", "Executive Room", R.drawable.img_mega2, 55000,
                        new String[]{"40 m²", "King Bed", "Mountain View"})
        );
        List<HotelAddOn> novotelAddOns = Arrays.asList(
                new HotelAddOn("n1a", "Breakfast", R.drawable.ic_breakfast, 10000, true),
                new HotelAddOn("n2a", "Airport Transfer", R.drawable.ic_car, 12000, false)
        );
        hotels.add(new Hotel(
                "h3",
                "Novotel City Center",
                "Dostyk Avenue, 104A",
                "Modern international hotel in the central business district. Novotel offers comfortable rooms, fitness center, and easy access to shopping and entertainment.",
                4.8f,
                R.drawable.header_mega,
                new int[]{R.drawable.img_mega1, R.drawable.img_mega2, R.drawable.img_mega3},
                new String[]{"wifi", "dining", "pool", "spa"},
                "43.248000,76.945000",
                45000,
                novotelDays,
                novotelRooms,
                novotelAddOns
        ));
    }

    public List<Hotel> getAllHotels() {
        return new ArrayList<>(hotels);
    }

    public List<Hotel> getHotelsByAvailability(String checkIn, String checkOut) {
        List<Hotel> result = new ArrayList<>();
        for (Hotel h : hotels) {
            if (h.isAvailableForDates(checkIn, checkOut)) {
                result.add(h);
            }
        }
        return result;
    }

    public Hotel getHotelById(String id) {
        for (Hotel h : hotels) {
            if (h.id.equals(id)) return h;
        }
        return null;
    }

    /** Get hotel name from string resource if needed; for now hotels use direct strings. */
    public String getHotelName(Context ctx, Hotel h) {
        return h.name;
    }
}
