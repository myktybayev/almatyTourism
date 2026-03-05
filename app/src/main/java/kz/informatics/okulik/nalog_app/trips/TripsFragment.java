package kz.informatics.okulik.nalog_app.trips;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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

        recyclerTrips.setLayoutManager(new LinearLayoutManager(requireContext()));

        tabGuidedTours.setOnClickListener(v -> switchTab(TAB_GUIDED));
        tabSelfPlanned.setOnClickListener(v -> switchTab(TAB_SELF_PLANNED));

        switchTab(TAB_GUIDED);
    }

    private void switchTab(String tab) {
        currentTab = tab;
        updateTabStyle();
        if (TAB_GUIDED.equals(tab)) {
            loadGuidedTours();
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
        // Placeholder: empty list; later add user-created itineraries
        GuidedToursAdapter adapter = new GuidedToursAdapter(this::onTourDetails);
        adapter.setItems(java.util.Collections.emptyList());
        recyclerTrips.setAdapter(adapter);
    }

    private void onTourDetails(GuidedTour tour) {
        TripsDetailActivity.open(requireContext(), tour.id);
    }
}
