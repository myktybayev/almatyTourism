package kz.informatics.okulik.nalog_app.hotels;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import kz.informatics.okulik.R;
import kz.informatics.okulik.nalog_app.hotels.activities.HotelDetailActivity;
import kz.informatics.okulik.nalog_app.hotels.adapters.HotelListAdapter;
import kz.informatics.okulik.nalog_app.hotels.module.Hotel;
import kz.informatics.okulik.nalog_app.hotels.module.HotelsRepository;

public class HotelsFragment extends Fragment {

    private static final String DATE_FORMAT = "MMM dd";

    private TextView textCheckIn;
    private TextView textCheckOut;
    private TextView textGuests;
    private TextView textRooms;
    private RecyclerView recyclerHotels;
    private HotelListAdapter adapter;

    private String checkInDate = "";
    private String checkOutDate = "";
    private int guestsCount = 0;
    private int roomsCount = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hotels, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initDates();
        setupRecycler();
        setupListeners();
        loadHotels();
    }

    private void initViews(View view) {
        textCheckIn = view.findViewById(R.id.textCheckIn);
        textCheckOut = view.findViewById(R.id.textCheckOut);
        textGuests = view.findViewById(R.id.textGuests);
        textRooms = view.findViewById(R.id.textRooms);
        recyclerHotels = view.findViewById(R.id.recyclerHotels);
    }

    private void initDates() {
        checkInDate = "";
        checkOutDate = "";
        textCheckIn.setText(getString(R.string.hotels_select));
        textCheckOut.setText(getString(R.string.hotels_select));
        updateGuestsRoomsText();
    }

    private void updateGuestsRoomsText() {
        if (guestsCount <= 0) {
            textGuests.setText("0");
        } else {
            textGuests.setText(getString(R.string.hotels_guests_count, guestsCount));
        }
        if (roomsCount <= 0) {
            textRooms.setText("0");
        } else {
            textRooms.setText(getString(R.string.hotels_rooms_count, roomsCount));
        }
    }

    private String formatForFilter(Calendar cal) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return sdf.format(cal.getTime());
    }

    private void setupRecycler() {
        recyclerHotels.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HotelListAdapter(this::openHotelDetail);
        recyclerHotels.setAdapter(adapter);
    }

    private void setupListeners() {
        View layoutCheckIn = getView().findViewById(R.id.layoutCheckIn);
        View layoutCheckOut = getView().findViewById(R.id.layoutCheckOut);
        ImageButton btnGuestsMinus = getView().findViewById(R.id.buttonGuestsMinus);
        ImageButton btnGuestsPlus = getView().findViewById(R.id.buttonGuestsPlus);
        ImageButton btnRoomsMinus = getView().findViewById(R.id.buttonRoomsMinus);
        ImageButton btnRoomsPlus = getView().findViewById(R.id.buttonRoomsPlus);
        View buttonFindHotels = getView().findViewById(R.id.buttonFindHotels);

        if (layoutCheckIn != null) {
            layoutCheckIn.setOnClickListener(v -> showDatePicker(true));
        }
        if (layoutCheckOut != null) {
            layoutCheckOut.setOnClickListener(v -> showDatePicker(false));
        }

        if (btnGuestsMinus != null) {
            btnGuestsMinus.setOnClickListener(v -> {
                if (guestsCount > 0) {
                    guestsCount--;
                    updateGuestsRoomsText();
                }
            });
        }
        if (btnGuestsPlus != null) {
            btnGuestsPlus.setOnClickListener(v -> {
                if (guestsCount < 9) {
                    guestsCount++;
                    updateGuestsRoomsText();
                }
            });
        }
        if (btnRoomsMinus != null) {
            btnRoomsMinus.setOnClickListener(v -> {
                if (roomsCount > 0) {
                    roomsCount--;
                    updateGuestsRoomsText();
                }
            });
        }
        if (btnRoomsPlus != null) {
            btnRoomsPlus.setOnClickListener(v -> {
                if (roomsCount < 9) {
                    roomsCount++;
                    updateGuestsRoomsText();
                }
            });
        }

        if (buttonFindHotels != null) {
            buttonFindHotels.setOnClickListener(v -> loadHotels());
        }
    }

    private void showDatePicker(boolean isCheckIn) {
        Calendar cal = Calendar.getInstance();
        try {
            String dateStr = isCheckIn ? checkInDate : checkOutDate;
            if (dateStr != null && !dateStr.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                cal.setTime(sdf.parse(dateStr));
            }
        } catch (Exception ignored) {
        }

        DatePickerDialog.OnDateSetListener listener = (view, year, month, dayOfMonth) -> {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat sdfDisplay = new SimpleDateFormat(DATE_FORMAT, Locale.US);
            SimpleDateFormat sdfFilter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            if (isCheckIn) {
                checkInDate = sdfFilter.format(cal.getTime());
                textCheckIn.setText(sdfDisplay.format(cal.getTime()));
            } else {
                checkOutDate = sdfFilter.format(cal.getTime());
                textCheckOut.setText(sdfDisplay.format(cal.getTime()));
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(
                requireContext(),
                listener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        long minMillis = System.currentTimeMillis() - 1000;
        if (isCheckIn) {
            dialog.getDatePicker().setMinDate(minMillis);
        } else if (checkInDate != null && !checkInDate.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                long checkInMillis = sdf.parse(checkInDate).getTime();
                dialog.getDatePicker().setMinDate(checkInMillis);
            } catch (Exception ignored) {
                dialog.getDatePicker().setMinDate(minMillis);
            }
        } else {
            dialog.getDatePicker().setMinDate(minMillis);
        }
        dialog.show();
    }

    private void loadHotels() {
        List<Hotel> list;
        if (checkInDate == null || checkInDate.isEmpty() || checkOutDate == null || checkOutDate.isEmpty()) {
            list = HotelsRepository.getInstance().getAllHotels();
        } else {
            list = HotelsRepository.getInstance().getHotelsByAvailability(checkInDate, checkOutDate);
        }
        adapter.setItems(list);
    }

    private void openHotelDetail(Hotel hotel) {
        Intent intent = new Intent(getContext(), HotelDetailActivity.class);
        intent.putExtra(HotelDetailActivity.EXTRA_HOTEL_ID, hotel.id);
        intent.putExtra(HotelDetailActivity.EXTRA_CHECK_IN, checkInDate != null ? checkInDate : "");
        intent.putExtra(HotelDetailActivity.EXTRA_CHECK_OUT, checkOutDate != null ? checkOutDate : "");
        intent.putExtra(HotelDetailActivity.EXTRA_GUESTS, Math.max(1, guestsCount));
        intent.putExtra(HotelDetailActivity.EXTRA_ROOMS, Math.max(1, roomsCount));
        startActivity(intent);
    }
}
