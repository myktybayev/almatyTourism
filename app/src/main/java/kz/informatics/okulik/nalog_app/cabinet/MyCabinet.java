package kz.informatics.okulik.nalog_app.cabinet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kz.informatics.okulik.MainActivity;
import kz.informatics.okulik.R;
import kz.informatics.okulik.nalog_app.cabinet.SavedBookingsRepository;
import kz.informatics.okulik.nalog_app.hotels.activities.HotelDetailActivity;

/**
 * Profile / My Cabinet screen: user info, tabs (Favorites, Bookings, Trips), list of bookings.
 */
public class MyCabinet extends AppCompatActivity {

    public static final String TAB_FAVORITES = "favorites";
    public static final String TAB_BOOKINGS = "bookings";
    public static final String TAB_TRIPS = "trips";

    private TextView tabFavorites, tabBookings, tabTrips;
    private View underlineFavorites, underlineBookings, underlineTrips;
    private RecyclerView recyclerContent;
    private String currentTab = TAB_BOOKINGS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cabinet);

        ImageButton buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));

        tabFavorites = findViewById(R.id.tabFavorites);
        tabBookings = findViewById(R.id.tabBookings);
        tabTrips = findViewById(R.id.tabTrips);
        underlineFavorites = findViewById(R.id.underlineFavorites);
        underlineBookings = findViewById(R.id.underlineBookings);
        underlineTrips = findViewById(R.id.underlineTrips);
        recyclerContent = findViewById(R.id.recyclerCabinetContent);

        tabFavorites.setOnClickListener(v -> switchTab(TAB_FAVORITES));
        tabBookings.setOnClickListener(v -> switchTab(TAB_BOOKINGS));
        tabTrips.setOnClickListener(v -> switchTab(TAB_TRIPS));

        recyclerContent.setLayoutManager(new LinearLayoutManager(this));
        switchTab(TAB_BOOKINGS);
    }

    private void switchTab(String tab) {
        currentTab = tab;
        updateTabUnderlines();
        if (TAB_BOOKINGS.equals(tab)) {
            loadBookings();
        } else {
            recyclerContent.setAdapter(new BookingAdapter(new ArrayList<>(), this::onBookingAction));
        }
    }

    private void updateTabUnderlines() {
        int visible = View.VISIBLE;
        int gone = View.GONE;
        underlineFavorites.setVisibility(TAB_FAVORITES.equals(currentTab) ? visible : gone);
        underlineBookings.setVisibility(TAB_BOOKINGS.equals(currentTab) ? visible : gone);
        underlineTrips.setVisibility(TAB_TRIPS.equals(currentTab) ? visible : gone);
        tabFavorites.setTextColor(getColor(TAB_FAVORITES.equals(currentTab) ? R.color.hotel_gold : R.color.cabinet_tab_inactive));
        tabBookings.setTextColor(getColor(TAB_BOOKINGS.equals(currentTab) ? R.color.hotel_gold : R.color.cabinet_tab_inactive));
        tabTrips.setTextColor(getColor(TAB_TRIPS.equals(currentTab) ? R.color.hotel_gold : R.color.cabinet_tab_inactive));
    }

    private void loadBookings() {
        List<BookingItem> items = new ArrayList<>(SavedBookingsRepository.getInstance(this).getAllBookingsAsItems());
        // Demo items when no saved bookings
        if (items.isEmpty()) {
            items.add(new BookingItem("h1", "Rixos Almaty", "BK-7829", BookingItem.STATUS_CONFIRMED,
                    "15 Oct 2023", "20 Oct 2023", "2 Adults", "425,000 〒", "Deluxe King with Mountain View",
                    R.drawable.header_rixos_almaty, true));
            items.add(new BookingItem("h2", "Hotel Kazakhstan", "BK-5521", BookingItem.STATUS_COMPLETED,
                    "10 Aug 2023", "12 Aug 2023", null, "70,000 〒", null,
                    R.drawable.header_hotel_kaz, false));
        }
        recyclerContent.setAdapter(new BookingAdapter(items, this::onBookingAction));
    }

    private void onBookingAction(BookingItem item, String action) {
        if ("view_details".equals(action)) {
            if (item.hotelId != null && !item.hotelId.isEmpty()) {
                Intent intent = new Intent(this, HotelDetailActivity.class);
                intent.putExtra(HotelDetailActivity.EXTRA_HOTEL_ID, item.hotelId);
                startActivity(intent);
            } else {
                Toast.makeText(this, getString(R.string.cabinet_view_details) + ": " + item.hotelName, Toast.LENGTH_SHORT).show();
            }
        } else if ("cancel".equals(action)) {
            SavedBookingsRepository.getInstance(this).removeBookingByReference(item.reference);
            loadBookings();
            Toast.makeText(this, getString(R.string.cabinet_cancel_booking), Toast.LENGTH_SHORT).show();
        } else if ("view_receipt".equals(action)) {
            Toast.makeText(this, getString(R.string.cabinet_view_receipt), Toast.LENGTH_SHORT).show();
        }
    }
}
