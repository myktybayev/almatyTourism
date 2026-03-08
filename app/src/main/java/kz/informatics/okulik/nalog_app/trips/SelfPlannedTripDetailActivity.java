package kz.informatics.okulik.nalog_app.trips;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kz.informatics.okulik.R;
import kz.informatics.okulik.nalog_app.profile.LocaleHelper;

/**
 * Read-only detail screen for a self-planned trip.
 * Shows title, dates, description, itinerary stops with visited status,
 * and budget estimate summary.
 */
public class SelfPlannedTripDetailActivity extends AppCompatActivity {

    private static final String EXTRA_TRIP_ID = "trip_id";
    private static final String PREFS_VISITED = "self_planned_visited";

    private SelfPlannedTrip trip;
    private List<Boolean> visitedFlags;

    private TextView textTitle;
    private TextView textDates;
    private TextView textDescription;
    private LinearLayout layoutStops;
    private LinearLayout layoutBudget;
    private TextView textDetailTotal;

    private static final int[] EXPENSE_ICONS = {
            R.drawable.ic_hotel_room, // 0 Accommodation
            R.drawable.ic_car, // 1 Transport
            R.drawable.ic_confirmation_number_24, // 2 Entry Tickets
            R.drawable.ic_dining, // 3 Food (Custom)
            R.drawable.baseline_fastfood_24,
            R.drawable.baseline_bedroom_parent_24,
            R.drawable.baseline_downhill_skiing_24,
            R.drawable.baseline_local_parking_24,
            R.drawable.ic_cable_car,
            R.drawable.baseline_tour_24,
            R.drawable.baseline_landscape_24,
            R.drawable.baseline_emergency_24,
            R.drawable.baseline_support_24,
            R.drawable.baseline_shopping_bag_24,
    };

    public static void open(Context context, String tripId) {
        Intent i = new Intent(context, SelfPlannedTripDetailActivity.class);
        i.putExtra(EXTRA_TRIP_ID, tripId);
        context.startActivity(i);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_planned_trip_detail);

        String id = getIntent().getStringExtra(EXTRA_TRIP_ID);
        trip = SelfPlannedTripsRepository.getInstance(this).getById(id);
        if (trip == null) {
            finish();
            return;
        }

        textTitle = findViewById(R.id.textTitle);
        textDates = findViewById(R.id.textDates);
        textDescription = findViewById(R.id.textDescription);
        layoutStops = findViewById(R.id.layoutDetailStops);
        layoutBudget = findViewById(R.id.layoutDetailBudget);
        textDetailTotal = findViewById(R.id.textDetailTotal);

        ImageButton back = findViewById(R.id.buttonBack);
        back.setOnClickListener(v -> finish());

        ImageButton buttonShare = findViewById(R.id.buttonShare);
        buttonShare.setOnClickListener(v -> shareTrip());

        textTitle.setText(trip.title);
        textDates.setText(trip.dates);
        textDescription.setText(trip.locationTheme);

        visitedFlags = loadVisitedFlags(trip);

        bindStops();
        bindBudget();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveVisitedFlags(trip, visitedFlags);
    }

    private void bindStops() {
        layoutStops.removeAllViews();
        List<TripLocation> stops = trip.locations != null && !trip.locations.isEmpty()
                ? trip.locations
                : buildFallbackLocationsFromTitle();

        // Ensure visitedFlags size matches stops
        if (visitedFlags == null) visitedFlags = new ArrayList<>();
        while (visitedFlags.size() < stops.size()) visitedFlags.add(false);

        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < stops.size(); i++) {
            final int index = i;
            TripLocation loc = stops.get(i);
            View row = inflater.inflate(R.layout.item_self_planned_detail_stop, layoutStops, false);
            TextView textNumber = row.findViewById(R.id.textStopNumber);
            TextView textName = row.findViewById(R.id.textStopName);
            TextView textNote = row.findViewById(R.id.textStopNote);
            TextView textStatus = row.findViewById(R.id.textStatus);
            View viewRadio = row.findViewById(R.id.viewRadio);

            textNumber.setText(String.format(Locale.US, "%d.", i + 1));
            textName.setText(loc != null ? loc.locationName : "");
            if (textNote != null) {
                String note = loc != null && loc.locationNote != null ? loc.locationNote.trim() : "";
                if (!note.isEmpty()) {
                    textNote.setText(note);
                    textNote.setVisibility(View.VISIBLE);
                } else {
                    textNote.setVisibility(View.GONE);
                }
            }

            View.OnClickListener toggle = v -> {
                boolean newVal = !visitedFlags.get(index);
                visitedFlags.set(index, newVal);
                updateStatusUi(newVal, textStatus, viewRadio);
            };

            boolean isVisited = visitedFlags.get(index);
            updateStatusUi(isVisited, textStatus, viewRadio);

            row.setOnClickListener(toggle);
            viewRadio.setOnClickListener(toggle);

            layoutStops.addView(row);
        }
    }

    private void updateStatusUi(boolean isVisited, TextView status, View radio) {
        if (isVisited) {
            status.setText(R.string.self_planned_status_visited);
            status.setTextColor(getColor(R.color.cabinet_status_confirmed_text));
            status.setBackgroundResource(R.drawable.bg_status_confirmed);
            radio.setBackgroundResource(R.drawable.dot_trip_gold);
        } else {
            status.setText(R.string.self_planned_status_not_visited);
            status.setTextColor(getColor(R.color.cabinet_status_completed_text));
            status.setBackgroundResource(R.drawable.bg_status_completed);
            radio.setBackgroundResource(R.drawable.dot_trip_grey);
        }
    }

    private void bindBudget() {
        layoutBudget.removeAllViews();
        long total = 0;
        LayoutInflater inflater = LayoutInflater.from(this);
        String[] categories = getResources().getStringArray(R.array.expense_categories);
        if (trip.expenses != null) {
            for (SelfPlannedTrip.SavedExpense e : trip.expenses) {
                View row = inflater.inflate(R.layout.item_self_planned_expense, layoutBudget, false);
                ImageView iconExpense = row.findViewById(R.id.iconExpense);
                TextView label = row.findViewById(R.id.textExpenseLabel);
                TextView amount = row.findViewById(R.id.textExpenseAmount);

                int iconIndex = 0;
                for (int i = 0; i < categories.length; i++) {
                    if(e.category.contains(categories[i])) iconIndex = i;
                }

                iconExpense.setImageResource(EXPENSE_ICONS[iconIndex]);

                label.setText(e.category);
                amount.setText(String.format(Locale.US, "%,d ₸", e.amountKzt));
                layoutBudget.addView(row);

                total += e.amountKzt;
            }
        }
        textDetailTotal.setText(String.format(Locale.US, "%,d ₸", total));
    }

    private List<Boolean> buildDefaultFlags(int size) {
        List<Boolean> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) list.add(false);
        return list;
    }

    private List<Boolean> loadVisitedFlags(SelfPlannedTrip trip) {
        List<TripLocation> stops = trip.locations != null && !trip.locations.isEmpty()
                ? trip.locations
                : buildFallbackLocationsFromTitle();
        SharedPreferences prefs = getSharedPreferences(PREFS_VISITED, MODE_PRIVATE);
        String json = prefs.getString(trip.id, null);
        if (json == null) return buildDefaultFlags(stops.size());
        try {
            JSONArray arr = new JSONArray(json);
            List<Boolean> list = new ArrayList<>();
            for (int i = 0; i < stops.size(); i++) {
                list.add(i < arr.length() && arr.optBoolean(i, false));
            }
            return list;
        } catch (JSONException e) {
            return buildDefaultFlags(stops.size());
        }
    }

    private void saveVisitedFlags(SelfPlannedTrip trip, List<Boolean> flags) {
        if (trip == null || flags == null) return;
        SharedPreferences prefs = getSharedPreferences(PREFS_VISITED, MODE_PRIVATE);
        JSONArray arr = new JSONArray();
        for (Boolean b : flags) arr.put(b != null && b);
        prefs.edit().putString(trip.id, arr.toString()).apply();
    }

    // Fallback: if no explicit stops saved, treat title as single location
    private List<TripLocation> buildFallbackLocationsFromTitle() {
        List<TripLocation> list = new ArrayList<>();
        if (trip.title != null && !trip.title.isEmpty()) {
            list.add(new TripLocation(trip.title, ""));
        }
        return list;
    }

    private void shareTrip() {
        if (trip == null) return;
        StringBuilder sb = new StringBuilder();
        sb.append(trip.title).append("\n\n");
        if (trip.dates != null && !trip.dates.isEmpty()) {
            sb.append(trip.dates).append("\n");
        }
        if (trip.locationTheme != null && !trip.locationTheme.isEmpty()) {
            sb.append(trip.locationTheme).append("\n\n");
        }
        List<TripLocation> locs = trip.locations != null && !trip.locations.isEmpty()
                ? trip.locations
                : buildFallbackLocationsFromTitle();
        if (!locs.isEmpty()) {
            sb.append(getString(R.string.trip_detail_itinerary)).append("\n");
            for (int i = 0; i < locs.size(); i++) {
                TripLocation loc = locs.get(i);
                String name = loc != null ? loc.locationName : "";
                String note = loc != null && loc.locationNote != null ? loc.locationNote.trim() : "";
                sb.append((i + 1)).append(". ").append(name);
                if (!note.isEmpty()) sb.append(" — ").append(note);
                sb.append("\n");
            }
            sb.append("\n");
        }
        long total = 0;
        sb.append(getString(R.string.self_planned_budget_estimate)+"\n");

        if (trip.expenses != null) {
            for (SelfPlannedTrip.SavedExpense e : trip.expenses){
                total += e.amountKzt;

                sb.append(e.category+" - "+e.amountKzt+" ₸");
                sb.append("\n");
            }
        }
        sb.append("\n");
        sb.append(getString(R.string.self_planned_total_estimated)).append(" ")
                .append(String.format(Locale.US, "%,d ₸", total));

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, sb.toString());
        share.putExtra(Intent.EXTRA_SUBJECT, trip.title);
        startActivity(Intent.createChooser(share, getString(R.string.share_trip)));
    }
}

