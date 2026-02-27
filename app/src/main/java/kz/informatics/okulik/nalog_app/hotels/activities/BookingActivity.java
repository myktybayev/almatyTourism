package kz.informatics.okulik.nalog_app.hotels.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import kz.informatics.okulik.R;
import kz.informatics.okulik.nalog_app.hotels.module.Hotel;
import kz.informatics.okulik.nalog_app.hotels.module.HotelAddOn;
import kz.informatics.okulik.nalog_app.hotels.module.HotelRoom;
import kz.informatics.okulik.nalog_app.hotels.module.HotelsRepository;
import kz.informatics.okulik.nalog_app.profile.LocaleHelper;

public class BookingActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }

    public static final String EXTRA_HOTEL_ID = "extra_hotel_id";
    public static final String EXTRA_CHECK_IN = "extra_check_in";
    public static final String EXTRA_CHECK_OUT = "extra_check_out";
    public static final String EXTRA_GUESTS = "extra_guests";
    public static final String EXTRA_ROOMS = "extra_rooms";

    private Hotel hotel;
    private String checkIn;
    private String checkOut;
    private int guests;
    private int rooms;
    RoomAdapter adapter;

    private HotelRoom selectedRoom;
    private final java.util.Set<String> selectedAddOnIds = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        String hotelId = getIntent().getStringExtra(EXTRA_HOTEL_ID);
        checkIn = getIntent().getStringExtra(EXTRA_CHECK_IN);
        checkOut = getIntent().getStringExtra(EXTRA_CHECK_OUT);
        guests = getIntent().getIntExtra(EXTRA_GUESTS, 1);
        rooms = getIntent().getIntExtra(EXTRA_ROOMS, 1);
        if (checkIn == null || checkIn.isEmpty() || checkOut == null || checkOut.isEmpty()) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.US);
            checkIn = sdf.format(cal.getTime());
            cal.add(java.util.Calendar.DAY_OF_MONTH, 2);
            checkOut = sdf.format(cal.getTime());
        }

        hotel = HotelsRepository.getInstance().getHotelById(this, hotelId);
        if (hotel == null) {
            finish();
            return;
        }

        ImageButton back = findViewById(R.id.buttonBack);
        back.setOnClickListener(v -> finish());

        ImageView thumb = findViewById(R.id.imageHotelThumb);
        thumb.setImageResource(hotel.imageRes);

        TextView textHotelName = findViewById(R.id.textHotelName);
        textHotelName.setText(hotel.name);

        TextView textDates = findViewById(R.id.textDates);
        textDates.setText(formatDateRange(checkIn, checkOut));

        TextView textGuestsRooms = findViewById(R.id.textGuestsRooms);
        textGuestsRooms.setText(getString(R.string.booking_guests_rooms, guests, rooms));

        int nightsCount = getNightsCount();
        TextView textTotalLabel = findViewById(R.id.textTotalLabel);
        textTotalLabel.setText(getString(R.string.booking_total, nightsCount));

        setupRooms();
        setupAddOns();
        updateTotal();

        Button buttonContinue = findViewById(R.id.buttonContinue);
        buttonContinue.setOnClickListener(v -> {
            // Could navigate to confirmation
            finish();
        });
    }

    private String formatDateRange(String start, String end) {
        try {
            SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            SimpleDateFormat out = new SimpleDateFormat("dd MMM", Locale.US);
            Date s = in.parse(start);
            Date e = in.parse(end);
            if (s != null && e != null) {
                int nights = getNightsCount();
                return out.format(s) + " - " + out.format(e) + " (" + getString(R.string.booking_nights, nights) + ")";
            }
        } catch (ParseException ignored) {
        }
        return start + " - " + end + " (" + getString(R.string.booking_nights, getNightsCount()) + ")";
    }

    private int getNightsCount() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date s = sdf.parse(checkIn);
            Date e = sdf.parse(checkOut);
            if (s != null && e != null) {
                long diff = (e.getTime() - s.getTime()) / (24 * 60 * 60 * 1000);
                return Math.max(1, (int) diff);
            }
        } catch (ParseException ignored) {
        }
        return 2;
    }

    private void setupRooms() {
        RecyclerView recycler = findViewById(R.id.recyclerRooms);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RoomAdapter(this, hotel.rooms, room -> {
            selectedRoom = room;
            adapter.setSelectedRoom(room);
            updateTotal();
        });
        recycler.setAdapter(adapter);
        if (!hotel.rooms.isEmpty()) {
            selectedRoom = hotel.rooms.get(0);
            adapter.setSelectedRoom(selectedRoom);
        }
    }

    private void setupAddOns() {
        LinearLayout layout = findViewById(R.id.layoutAddOns);
        layout.removeAllViews();

        for (HotelAddOn addon : hotel.addOns) {
            View item = LayoutInflater.from(this).inflate(R.layout.item_booking_addon, layout, false);
            ImageView icon = item.findViewById(R.id.iconAddon);
            TextView name = item.findViewById(R.id.textAddonName);
            TextView price = item.findViewById(R.id.textAddonPrice);
            SwitchCompat sw = item.findViewById(R.id.switchAddon);

            icon.setImageResource(addon.iconRes);
            name.setText(addon.name);
            if (addon.perGuest) {
                price.setText(getString(R.string.booking_addon_per_guest, formatPrice(addon.price)));
            } else {
                price.setText(getString(R.string.booking_addon_fixed, formatPrice(addon.price)));
            }

            boolean defaultOn = "a1".equals(addon.id) || "n1a".equals(addon.id);
            sw.setChecked(defaultOn);
            if (defaultOn) selectedAddOnIds.add(addon.id);

            sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedAddOnIds.add(addon.id);
                } else {
                    selectedAddOnIds.remove(addon.id);
                }
                updateTotal();
            });
            layout.addView(item);
        }
    }

    private String formatPrice(int price) {
        return String.format(Locale.US, "%,d", price);
    }

    private void updateTotal() {
        int total = calculateTotal();
        TextView textTotal = findViewById(R.id.textTotalPrice);
        textTotal.setText(String.format(Locale.US, "%,d 〒", total));
    }

    private int calculateTotal() {
        int nights = getNightsCount();
        int roomTotal = 0;
        if (selectedRoom != null) {
            roomTotal = selectedRoom.pricePerNight * nights;
        }

        int addOnTotal = 0;
        for (HotelAddOn a : hotel.addOns) {
            if (!selectedAddOnIds.contains(a.id)) continue;
            if (a.perGuest) {
                addOnTotal += a.price * guests;
            } else {
                addOnTotal += a.price;
            }
        }

        return roomTotal + addOnTotal;
    }

    static class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.VH> {
        interface OnRoomSelectListener {
            void onRoomSelect(HotelRoom room);
        }

        final android.content.Context context;
        final List<HotelRoom> items;
        final OnRoomSelectListener listener;
        int selectedPosition = 0;

        RoomAdapter(android.content.Context context, List<HotelRoom> items, OnRoomSelectListener listener) {
            this.context = context;
            this.items = items != null ? items : new ArrayList<>();
            this.listener = listener;
        }

        void setSelectedRoom(HotelRoom room) {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i) == room) {
                    selectedPosition = i;
                    notifyDataSetChanged();
                    return;
                }
            }
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking_room, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH h, int position) {
            HotelRoom r = items.get(position);
            boolean isSelected = (position == selectedPosition);

            h.cardRoot.setBackgroundResource(isSelected ? R.drawable.bg_room_card_selected : R.drawable.bg_room_card_unselected);
            h.indicatorSelect.setBackgroundResource(isSelected ? R.drawable.room_select_indicator_selected : R.drawable.room_select_indicator_unselected);
            h.price.setTextColor(ContextCompat.getColor(context, isSelected ? R.color.hotel_gold : R.color.black));

            h.image.setImageResource(r.imageRes);
            h.name.setText(r.name);
            StringBuilder fs = new StringBuilder();
            for (int i = 0; i < r.features.length; i++) {
                if (i > 0) fs.append(" • ");
                fs.append(r.features[i]);
            }
            h.features.setText(fs.toString());
            h.price.setText(String.format(Locale.US, "%,d 〒", r.pricePerNight));

            h.itemView.setOnClickListener(v -> {
                selectedPosition = position;
                if (listener != null) listener.onRoomSelect(r);
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class VH extends RecyclerView.ViewHolder {
            View cardRoot;
            ImageView image;
            View indicatorSelect;
            TextView name;
            TextView features;
            TextView price;

            VH(View itemView) {
                super(itemView);
                cardRoot = itemView.findViewById(R.id.cardRoom);
                image = itemView.findViewById(R.id.imageRoom);
                indicatorSelect = itemView.findViewById(R.id.indicatorSelect);
                name = itemView.findViewById(R.id.textRoomName);
                features = itemView.findViewById(R.id.textRoomFeatures);
                price = itemView.findViewById(R.id.textRoomPrice);
            }
        }
    }
}
