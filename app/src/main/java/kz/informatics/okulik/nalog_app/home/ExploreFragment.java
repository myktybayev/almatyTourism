package kz.informatics.okulik.nalog_app.home;

import android.content.Intent;
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
import kz.informatics.okulik.nalog_app.home.activities.PopularDestinationsActivity;
import kz.informatics.okulik.nalog_app.home.activities.PopularDestinationDetailActivity;
import kz.informatics.okulik.nalog_app.home.adapters.PopularDestinationAdapter;
import kz.informatics.okulik.nalog_app.home.module.PopularPlace;

public class ExploreFragment extends Fragment {

    private View root;

    // Simple local data models

    private final List<PopularPlace> allDestinations = new ArrayList<>();
    private final List<PopularPlace> visibleDestinations = new ArrayList<>();

    private TextView weatherText;
    private EditText searchField;

    private RecyclerView recyclerPopular;
    private PopularDestinationAdapter popularAdapter;
    private TextView seeAll;

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
        seeAll = root.findViewById(R.id.textSeeAll);

        cardCategoryCity = root.findViewById(R.id.cardCategoryCity);
        cardCategoryNature = root.findViewById(R.id.cardCategoryNature);
        cardCategoryParks = root.findViewById(R.id.cardCategoryParks);
        cardCategorySpiritual = root.findViewById(R.id.cardCategorySpiritual);

        cardWeekend = root.findViewById(R.id.cardWeekend);
    }

    private void initLocalData() {
        // Local dummy data – later can be replaced with API
        allDestinations.clear();

        allDestinations.add(new PopularPlace(
                "1",
                "Shymbulak Resort",
                "25 min • Mountain Resort",
                4.9f,
                R.drawable.header_shymbulak,
                new String[] { "Skiing", "Hiking", "Nature" },
                new int[] { R.drawable.header_image, R.drawable.dir1, R.drawable.dir2, R.drawable.dir3, R.drawable.dir4,
                        R.drawable.dir5 }));
        allDestinations.add(new PopularPlace(
                "2",
                "Kok Tobe Hill",
                "City Center • Entertainment",
                4.8f,
                R.drawable.header_koktobe,
                new String[] { "Cable Car", "City Views" },
                new int[] { R.drawable.img_koktobe1, R.drawable.img_koktobe2, R.drawable.img_koktobe3,
                        R.drawable.img_koktobe4, R.drawable.img_koktobe5, R.drawable.img_koktobe6 }));
        allDestinations.add(new PopularPlace(
                "3",
                "Big Almaty Lake",
                "1 hr drive • Scenic Nature",
                4.9f,
                R.drawable.header_bigalmaty_lake,
                new String[] { "Lake", "Photography" },
                new int[] { R.drawable.img_almatylake1, R.drawable.img_almatylake2, R.drawable.img_almatylake3,
                        R.drawable.img_almatylake4 }));

        visibleDestinations.clear();
        visibleDestinations.addAll(allDestinations);

        // Today weather (static for now)
        weatherText.setText("24°C Sunny");
    }

    private void setupPopularRecycler() {
        recyclerPopular.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        popularAdapter = new PopularDestinationAdapter(
                item -> openDetail(item));
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

        seeAll.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), PopularDestinationsActivity.class);
            startActivity(intent);
        });
    }

    private void filterDestinations(String query) {
        visibleDestinations.clear();
        if (query == null || query.trim().isEmpty()) {
            visibleDestinations.addAll(allDestinations);
        } else {
            String lower = query.toLowerCase();
            for (PopularPlace d : allDestinations) {
                if (d.title != null && d.title.toLowerCase().contains(lower)) {
                    visibleDestinations.add(d);
                }
            }
        }
        if (popularAdapter != null) {
            popularAdapter.setItems(visibleDestinations);
        }
    }

    private void openDetail(PopularPlace item) {
        Intent intent = new Intent(getContext(), PopularDestinationDetailActivity.class);
        intent.putExtra(PopularDestinationDetailActivity.EXTRA_TITLE, item.title);
        intent.putExtra(PopularDestinationDetailActivity.EXTRA_SUBTITLE, item.subtitle);
        intent.putExtra(PopularDestinationDetailActivity.EXTRA_RATING, item.rating);
        intent.putExtra(PopularDestinationDetailActivity.EXTRA_IMAGE, item.imageRes);
        intent.putExtra(PopularDestinationDetailActivity.EXTRA_TAGS, item.tags);
        intent.putExtra(PopularDestinationDetailActivity.EXTRA_GALLERY_PHOTOS, item.listOfGalleryPhotos);
        startActivity(intent);
    }
}
