package kz.informatics.okulik.nalog_app.trips;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import kz.informatics.okulik.R;

/**
 * Trips page: Guided Tours (default) and Self-Planned tabs.
 */
public class TripsFragment extends Fragment {

    private static final String TAB_GUIDED = "guided";
    private static final String TAB_SELF_PLANNED = "self_planned";

    private TextView tabGuidedTours;
    private TextView tabSelfPlanned;
    private RecyclerView recyclerTrips;
    private View textSelfPlannedHint;
    private View layoutSelfPlannedEmpty;
    private View fabCreateTrip;
    private String currentTab = TAB_GUIDED;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trips, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabGuidedTours = view.findViewById(R.id.tabGuidedTours);
        tabSelfPlanned = view.findViewById(R.id.tabSelfPlanned);
        recyclerTrips = view.findViewById(R.id.recyclerTrips);
        textSelfPlannedHint = view.findViewById(R.id.textSelfPlannedHint);
        layoutSelfPlannedEmpty = view.findViewById(R.id.layoutSelfPlannedEmpty);
        fabCreateTrip = view.findViewById(R.id.fabCreateTrip);

        recyclerTrips.setLayoutManager(new LinearLayoutManager(requireContext()));

        tabGuidedTours.setOnClickListener(v -> switchTab(TAB_GUIDED));
        tabSelfPlanned.setOnClickListener(v -> switchTab(TAB_SELF_PLANNED));

        if (layoutSelfPlannedEmpty != null) {
            View createBtn = layoutSelfPlannedEmpty.findViewById(R.id.buttonCreateNewTrip);
            if (createBtn != null) createBtn.setOnClickListener(v -> onCreateNewTrip());
        }
        if (fabCreateTrip != null) fabCreateTrip.setOnClickListener(v -> onCreateNewTrip());

        switchTab(TAB_GUIDED);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (TAB_SELF_PLANNED.equals(currentTab)) {
            loadSelfPlanned();
        }
    }

    private void switchTab(String tab) {
        currentTab = tab;
        updateTabStyle();
        boolean isSelfPlanned = TAB_SELF_PLANNED.equals(tab);
        if (textSelfPlannedHint != null) textSelfPlannedHint.setVisibility(isSelfPlanned ? View.VISIBLE : View.GONE);
        if (fabCreateTrip != null) fabCreateTrip.setVisibility(isSelfPlanned ? View.VISIBLE : View.GONE);

        if (TAB_GUIDED.equals(tab)) {
            loadGuidedTours();
            if (layoutSelfPlannedEmpty != null) layoutSelfPlannedEmpty.setVisibility(View.GONE);
        } else {
            loadSelfPlanned();
        }
    }

    private void updateTabStyle() {
        boolean guidedActive = TAB_GUIDED.equals(currentTab);
        tabGuidedTours.setBackgroundResource(guidedActive ? R.drawable.bg_trips_tab_selected : 0);
        tabGuidedTours.setTextColor(ContextCompat.getColor(requireContext(), guidedActive ? R.color.black : R.color.cabinet_tab_inactive));
        tabSelfPlanned.setBackgroundResource(guidedActive ? 0 : R.drawable.bg_trips_tab_selected);
        tabSelfPlanned.setTextColor(ContextCompat.getColor(requireContext(), guidedActive ? R.color.cabinet_tab_inactive : R.color.black));
    }

    private void loadGuidedTours() {
        List<GuidedTour> tours = TripsRepository.getInstance(requireContext()).getGuidedTours(requireContext());
        GuidedToursAdapter adapter = new GuidedToursAdapter(this::onTourDetails);
        adapter.setItems(tours);
        recyclerTrips.setAdapter(adapter);
    }

    private void loadSelfPlanned() {
        SelfPlannedTripsRepository repo = SelfPlannedTripsRepository.getInstance(requireContext());
        List<SelfPlannedTrip> list = repo.getAll();
        if (list.isEmpty()) {
            seedDemoSelfPlannedTrips(repo);
            list = repo.getAll();
        }
        recyclerTrips.setAdapter(new SelfPlannedTripsAdapter(new SelfPlannedTripsAdapter.OnSelfPlannedTripListener() {
            @Override
            public void onOpenDetails(SelfPlannedTrip trip) {
                Toast.makeText(requireContext(), trip.title, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEditTrip(SelfPlannedTrip trip) {
                Toast.makeText(requireContext(), getString(R.string.trips_edit_trip) + ": " + trip.title, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteTrip(SelfPlannedTrip trip) {
                repo.removeById(trip.id);
                loadSelfPlanned();
            }

            @Override
            public void onMenuClick(SelfPlannedTrip trip, View anchor) {
                Toast.makeText(requireContext(), trip.title, Toast.LENGTH_SHORT).show();
            }
        }));
        ((SelfPlannedTripsAdapter) recyclerTrips.getAdapter()).setItems(list);

        if (layoutSelfPlannedEmpty != null) {
            layoutSelfPlannedEmpty.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
        }
        recyclerTrips.setVisibility(list.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void seedDemoSelfPlannedTrips(SelfPlannedTripsRepository repo) {
        if (!repo.getAll().isEmpty()) return;
        repo.add(new SelfPlannedTrip(UUID.randomUUID().toString(), "Summer in Almaty", "Almaty · City & Mountains",
                "12 Aug - 15 Aug", "Rixos Al", 90500));
        repo.add(new SelfPlannedTrip(UUID.randomUUID().toString(), "Golden Triangle Weekend", "Charyn · Kolsai · Kaindy",
                "Fri 20 Sep - Sun 22 Sep", null, 120000));
        repo.add(new SelfPlannedTrip(UUID.randomUUID().toString(), "Weekend in the City", "Almaty · Culture & Food",
                "Sat 05 Oct - Sun 06 Oct", null, 65000));
    }

    private void onCreateNewTrip() {
        showCreateTripDialog();
    }

    private void showCreateTripDialog() {
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_create_trip, null);
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        dialog.setContentView(view);

        EditText editTripName = view.findViewById(R.id.editTripName);
        EditText editDescription = view.findViewById(R.id.editDescription);
        TextView textStartDate = view.findViewById(R.id.textStartDate);
        TextView textEndDate = view.findViewById(R.id.textEndDate);
        View layoutStartDate = view.findViewById(R.id.layoutStartDate);
        View layoutEndDate = view.findViewById(R.id.layoutEndDate);

        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        endCal.add(Calendar.DAY_OF_MONTH, 3);

        view.findViewById(R.id.buttonCloseCreateTrip).setOnClickListener(v -> dialog.dismiss());

        layoutStartDate.setOnClickListener(v -> {
            new DatePickerDialog(requireContext(), (v2, year, month, dayOfMonth) -> {
                startCal.set(Calendar.YEAR, year);
                startCal.set(Calendar.MONTH, month);
                startCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                textStartDate.setText(formatDateForCreate(startCal));
            }, startCal.get(Calendar.YEAR), startCal.get(Calendar.MONTH), startCal.get(Calendar.DAY_OF_MONTH)).show();
        });
        layoutEndDate.setOnClickListener(v -> {
            new DatePickerDialog(requireContext(), (v2, year, month, dayOfMonth) -> {
                endCal.set(Calendar.YEAR, year);
                endCal.set(Calendar.MONTH, month);
                endCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                textEndDate.setText(formatDateForCreate(endCal));
            }, endCal.get(Calendar.YEAR), endCal.get(Calendar.MONTH), endCal.get(Calendar.DAY_OF_MONTH)).show();
        });

        view.findViewById(R.id.buttonCreateTrip).setOnClickListener(v -> {
            String name = editTripName.getText() != null ? editTripName.getText().toString().trim() : "";
            String description = editDescription.getText() != null ? editDescription.getText().toString().trim() : "";
            String startStr = textStartDate.getText() != null ? textStartDate.getText().toString() : "";
            String endStr = textEndDate.getText() != null ? textEndDate.getText().toString() : "";
            if (name.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.create_trip_name_label), Toast.LENGTH_SHORT).show();
                return;
            }
            dialog.dismiss();
            SelfPlannedTripActivity.open(requireContext(), name, description, startStr, endStr);
        });

        textStartDate.setText(formatDateForCreate(startCal));
        textEndDate.setText(formatDateForCreate(endCal));
        dialog.show();
    }

    private static String formatDateForCreate(Calendar cal) {
        return new java.text.SimpleDateFormat("d MMM", Locale.US).format(cal.getTime());
    }

    private void onTourDetails(GuidedTour tour) {
        TripsDetailActivity.open(requireContext(), tour.id);
    }
}
