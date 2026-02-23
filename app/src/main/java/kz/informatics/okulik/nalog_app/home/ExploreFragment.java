package kz.informatics.okulik.nalog_app.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import kz.informatics.okulik.nalog_app.home.api.WeatherApi;
import kz.informatics.okulik.nalog_app.home.activities.BrowseActivityByCategories;
import kz.informatics.okulik.nalog_app.home.activities.PopularDestinationsActivity;
import kz.informatics.okulik.nalog_app.home.activities.PopularDestinationDetailActivity;
import kz.informatics.okulik.nalog_app.home.adapters.PopularDestinationAdapter;
import kz.informatics.okulik.nalog_app.home.module.DestinationsRepository;
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

    private WeatherApi weatherApi;
    private final Handler weatherHandler = new Handler(Looper.getMainLooper());
    private Runnable weatherRefreshRunnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_explore, container, false);

        initViews();
        initLocalData();
        setupPopularRecycler();
        setupListeners();
        loadLiveWeather();

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
        allDestinations.clear();
        allDestinations.addAll(DestinationsRepository.getInstance().getPlacesForExplore(requireContext()));
        visibleDestinations.clear();
        visibleDestinations.addAll(allDestinations);
        weatherText.setText(getString(R.string.weather_loading));
    }

    private void loadLiveWeather() {
        if (weatherApi == null) {
            weatherApi = new WeatherApi();
        }
        weatherApi.fetchAlmatyWeather(new WeatherApi.Callback() {
            @Override
            public void onSuccess(String temperatureC, String description) {
                if (weatherText != null) {
                    weatherText.setText(getString(R.string.weather_format, temperatureC, description));
                }
            }

            @Override
            public void onError(String message) {
                if (weatherText != null) {
                    weatherText.setText(getString(R.string.weather_error));
                }
            }
        });

        // Live өзгеріп тұру үшін 30 минут сайын жаңарту
        if (weatherRefreshRunnable != null) {
            weatherHandler.removeCallbacks(weatherRefreshRunnable);
        }
        weatherRefreshRunnable = new Runnable() {
            @Override
            public void run() {
                loadLiveWeather();
                weatherHandler.postDelayed(this, weatherApi.getRefreshIntervalMs());
            }
        };
        weatherHandler.postDelayed(weatherRefreshRunnable, weatherApi.getRefreshIntervalMs());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (weatherRefreshRunnable != null) {
            weatherHandler.removeCallbacks(weatherRefreshRunnable);
        }
        if (weatherApi != null) {
            weatherApi.shutdown();
        }
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
            String category;
            if (v.getId() == R.id.cardCategoryCity) {
                category = BrowseActivityByCategories.CATEGORY_CITYLIFE;
            } else if (v.getId() == R.id.cardCategoryNature) {
                category = BrowseActivityByCategories.CATEGORY_NATURE;
            } else if (v.getId() == R.id.cardCategoryParks) {
                category = BrowseActivityByCategories.CATEGORY_PARKS;
            } else if (v.getId() == R.id.cardCategorySpiritual) {
                category = BrowseActivityByCategories.CATEGORY_SPIRITUAL;
            } else {
                category = BrowseActivityByCategories.CATEGORY_CITYLIFE;
            }
            Intent intent = new Intent(getContext(), BrowseActivityByCategories.class);
            intent.putExtra(BrowseActivityByCategories.EXTRA_CATEGORY, category);
            startActivity(intent);
        };

        cardCategoryCity.setOnClickListener(categoryClick);
        cardCategoryNature.setOnClickListener(categoryClick);
        cardCategoryParks.setOnClickListener(categoryClick);
        cardCategorySpiritual.setOnClickListener(categoryClick);

        cardWeekend.setOnClickListener(
                v -> Toast.makeText(getContext(), getString(R.string.toast_apple_festival), Toast.LENGTH_SHORT).show());

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
        intent.putExtra(PopularDestinationDetailActivity.EXTRA_ID, item.id);
        intent.putExtra(PopularDestinationDetailActivity.EXTRA_TITLE, item.title);
        intent.putExtra(PopularDestinationDetailActivity.EXTRA_SUBTITLE, item.subtitle);
        intent.putExtra(PopularDestinationDetailActivity.EXTRA_ABOUT, item.about);
        intent.putExtra(PopularDestinationDetailActivity.EXTRA_RATING, item.rating);
        intent.putExtra(PopularDestinationDetailActivity.EXTRA_IMAGE, item.imageRes);
        intent.putExtra(PopularDestinationDetailActivity.EXTRA_TAGS, item.tags);
        intent.putExtra(PopularDestinationDetailActivity.EXTRA_GALLERY_PHOTOS, item.listOfGalleryPhotos);
        intent.putExtra(PopularDestinationDetailActivity.EXTRA_LOCATION, item.location);
        startActivity(intent);
    }
}
