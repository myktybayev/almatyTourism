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
        // Local dummy data – later can be replaced with API
        allDestinations.clear();
        String[] tag1 = {getString(R.string.tag_skiing), getString(R.string.tag_hiking), getString(R.string.tag_nature)};
        String[] tag2 = {getString(R.string.tag_cable_car), getString(R.string.tag_city_views)};
        String[] tag3 = {getString(R.string.tag_lake), getString(R.string.tag_photography)};
        String[] tag4 = {getString(R.string.tag_hiking), getString(R.string.tag_adventure)};
        String[] tag5 = {getString(R.string.tag_architecture), getString(R.string.tag_history)};
        String[] tag6 = {getString(R.string.tag_ice_skating), getString(R.string.tag_sports)};
        String[] tag7 = {getString(R.string.tag_food), getString(R.string.tag_culture)};

        allDestinations.add(new PopularPlace(
                "1",
                getString(R.string.place_1_title),
                getString(R.string.place_1_subtitle),
                getString(R.string.place_1_about),
                4.9f,
                R.drawable.header_shymbulak,
                tag1,
                new int[]{R.drawable.img_shymbulak1, R.drawable.img_shymbulak2, R.drawable.img_shymbulak6, R.drawable.img_shymbulak3, R.drawable.img_shymbulak4, R.drawable.img_shymbulak5}));
        allDestinations.add(new PopularPlace(
                "2",
                getString(R.string.place_2_title),
                getString(R.string.place_2_subtitle),
                getString(R.string.place_2_about),
                4.8f,
                R.drawable.header_koktobe,
                tag2,
                new int[]{R.drawable.img_koktobe1, R.drawable.img_koktobe2, R.drawable.img_koktobe3,
                        R.drawable.img_koktobe4, R.drawable.img_koktobe5, R.drawable.img_koktobe6}));
        allDestinations.add(new PopularPlace(
                "3",
                getString(R.string.place_3_title),
                getString(R.string.place_3_subtitle),
                getString(R.string.place_3_about),
                4.9f,
                R.drawable.header_bigalmaty_lake,
                tag3,
                new int[]{R.drawable.img_almatylake1, R.drawable.img_almatylake2, R.drawable.img_almatylake3, R.drawable.img_almatylake5, R.drawable.img_almatylake6,
                        R.drawable.img_almatylake4}));
        allDestinations.add(new PopularPlace(
                "4",
                getString(R.string.place_4_title),
                getString(R.string.place_4_subtitle),
                getString(R.string.place_4_about),
                4.8f,
                R.drawable.header_charyn_canyon,
                tag4,
                new int[]{R.drawable.img_charyn1, R.drawable.img_charyn2, R.drawable.img_charyn3, R.drawable.img_charyn4, R.drawable.img_charyn5, R.drawable.img_charyn6}));
        allDestinations.add(new PopularPlace(
                "5",
                getString(R.string.place_5_title),
                getString(R.string.place_5_subtitle),
                getString(R.string.place_5_about),
                4.7f,
                R.drawable.header_central_mosque,
                tag5,
                new int[]{R.drawable.img_central_mosque4, R.drawable.img_central_mosque2, R.drawable.img_central_mosque6, R.drawable.img_central_mosque1, R.drawable.img_central_mosque3, R.drawable.img_central_mosque5}));
        allDestinations.add(new PopularPlace(
                "6",
                getString(R.string.place_6_title),
                getString(R.string.place_6_subtitle),
                getString(R.string.place_6_about),
                4.8f,
                R.drawable.header_medeu,
                tag6,
                new int[]{R.drawable.img_medeu1, R.drawable.img_medeu2, R.drawable.img_medeu3, R.drawable.img_medeu5, R.drawable.img_medeu6, R.drawable.img_medeu4}));
        allDestinations.add(new PopularPlace(
                "7",
                getString(R.string.place_7_title),
                getString(R.string.place_7_subtitle),
                getString(R.string.place_7_about),
                4.6f,
                R.drawable.header_green_bazaar,
                tag7,
                new int[]{R.drawable.img_green_bazaar6, R.drawable.img_green_bazaar1, R.drawable.img_green_bazaar4, R.drawable.img_green_bazaar5, R.drawable.img_green_bazaar3}));

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
            int resId;
            if (v.getId() == R.id.cardCategoryCity) {
                resId = R.string.category_citylife;
            } else if (v.getId() == R.id.cardCategoryNature) {
                resId = R.string.category_nature;
            } else if (v.getId() == R.id.cardCategoryParks) {
                resId = R.string.category_parks;
            } else if (v.getId() == R.id.cardCategorySpiritual) {
                resId = R.string.category_spiritual;
            } else {
                resId = R.string.category_citylife;
            }
            Toast.makeText(getContext(), getString(R.string.filter_by, getString(resId)), Toast.LENGTH_SHORT).show();
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
        startActivity(intent);
    }
}
