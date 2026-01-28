package kz.informatics.okulik.nalog_app.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kz.informatics.okulik.R;

public class ExploreFragment extends Fragment {

    private View root;

    // Simple local data models
    public static class Destination {
        String title;
        String distance;
        float rating;

        Destination(String title, String distance, float rating) {
            this.title = title;
            this.distance = distance;
            this.rating = rating;
        }
    }

    private final List<Destination> allDestinations = new ArrayList<>();
    private final List<Destination> visibleDestinations = new ArrayList<>();

    private TextView weatherText;
    private EditText searchField;

    private RecyclerView recyclerPopular;
    private PopularDestinationAdapter popularAdapter;

    private View cardCategoryCity;
    private View cardCategoryNature;
    private View cardCategoryParks;
    private View cardCategorySpiritual;

    private View cardWeekend;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_explore, container, false);

        initViews();
        initLocalData();
        setupPopularRecycler();
        setupListeners();

        return root;
    }

    private void initViews() {
        weatherText = root.findViewById(R.id.textWeather);
        searchField = root.findViewById(R.id.editSearch);
        recyclerPopular = root.findViewById(R.id.recyclerPopular);

        cardCategoryCity = root.findViewById(R.id.cardCategoryCity);
        cardCategoryNature = root.findViewById(R.id.cardCategoryNature);
        cardCategoryParks = root.findViewById(R.id.cardCategoryParks);
        cardCategorySpiritual = root.findViewById(R.id.cardCategorySpiritual);

        cardWeekend = root.findViewById(R.id.cardWeekend);
    }

    private void initLocalData() {
        // Local dummy data – later can be replaced with API
        allDestinations.clear();
        allDestinations.add(new Destination("Shymbulak Resort", "25 min drive", 4.9f));
        allDestinations.add(new Destination("Kok Tobe", "15 min drive", 4.7f));
        allDestinations.add(new Destination("Big Almaty Lake", "40 min drive", 4.8f));

        visibleDestinations.clear();
        visibleDestinations.addAll(allDestinations);

        // Today weather (static for now)
        weatherText.setText("24°C Sunny");
    }

    private void setupPopularRecycler() {
        recyclerPopular.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        popularAdapter = new PopularDestinationAdapter(
                item -> Toast.makeText(getContext(), "Open details: " + item.title, Toast.LENGTH_SHORT).show());
        recyclerPopular.setAdapter(popularAdapter);
        popularAdapter.setItems(visibleDestinations);
    }

    private void setupListeners() {
        // Search local data
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterDestinations(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        View.OnClickListener categoryClick = v -> {
            String msg;
            if (v.getId() == R.id.cardCategoryCity) {
                msg = "CityLife";
            } else if (v.getId() == R.id.cardCategoryNature) {
                msg = "Nature";
            } else if (v.getId() == R.id.cardCategoryParks) {
                msg = "Parks";
            } else if (v.getId() == R.id.cardCategorySpiritual) {
                msg = "Spiritual";
            } else {
                msg = "Category";
            }
            Toast.makeText(getContext(), "Filter by: " + msg, Toast.LENGTH_SHORT).show();
        };

        cardCategoryCity.setOnClickListener(categoryClick);
        cardCategoryNature.setOnClickListener(categoryClick);
        cardCategoryParks.setOnClickListener(categoryClick);
        cardCategorySpiritual.setOnClickListener(categoryClick);

        cardWeekend.setOnClickListener(
                v -> Toast.makeText(getContext(), "Almaty Apple Festival details", Toast.LENGTH_SHORT).show());
    }

    private void filterDestinations(String query) {
        visibleDestinations.clear();
        if (query == null || query.trim().isEmpty()) {
            visibleDestinations.addAll(allDestinations);
        } else {
            String lower = query.toLowerCase();
            for (Destination d : allDestinations) {
                if (d.title.toLowerCase().contains(lower)) {
                    visibleDestinations.add(d);
                }
            }
        }
        if (popularAdapter != null) {
            popularAdapter.setItems(visibleDestinations);
        }
    }
}
