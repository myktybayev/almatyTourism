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

        allDestinations.add(new PopularPlace(
                "1",
                "Shymbulak Resort",
                "25 min • Mountain Resort",
                "Shymbulak Resort is the largest ski resort in Kazakhstan, located in the scenic Ile-Alatau mountains near Almaty. It offers modern ski lifts, well-maintained slopes, and stunning panoramic views. In winter, it is a popular destination for skiing and snowboarding, while in summer visitors enjoy hiking and mountain activities. The resort sits at an altitude of over 2,200 meters above sea level. It is one of the most famous tourist attractions in the country.",
                4.9f,
                R.drawable.header_shymbulak,
                new String[]{"Skiing", "Hiking", "Nature"},
                new int[]{R.drawable.img_shymbulak1, R.drawable.img_shymbulak2, R.drawable.img_shymbulak6, R.drawable.img_shymbulak3, R.drawable.img_shymbulak4, R.drawable.img_shymbulak5}));
        allDestinations.add(new PopularPlace(
                "2",
                "Kok Tobe Hill",
                "City Center • Entertainment",
                "Kok Tobe Hill is a well-known viewpoint located on the outskirts of Almaty. Visitors can reach the top by cable car, enjoying breathtaking views of the city and surrounding mountains. At the summit, there are restaurants, souvenir shops, and a small amusement park. It is also home to the famous Beatles monument. Kok Tobe is especially beautiful in the evening when the city lights shine below.",
                4.8f,
                R.drawable.header_koktobe,
                new String[]{"Cable Car", "City Views"},
                new int[]{R.drawable.img_koktobe1, R.drawable.img_koktobe2, R.drawable.img_koktobe3,
                        R.drawable.img_koktobe4, R.drawable.img_koktobe5, R.drawable.img_koktobe6}));
        allDestinations.add(new PopularPlace(
                "3",
                "Big Almaty Lake",
                "1 hr drive • Scenic Nature",
                "Big Almaty Lake is a stunning alpine lake situated about 15 kilometers south of Almaty. It is famous for its bright turquoise water, which changes color depending on the season. The lake lies at an altitude of approximately 2,500 meters above sea level. Surrounded by mountain peaks, it offers spectacular natural scenery. It is a popular destination for nature lovers and photographers.",
                4.9f,
                R.drawable.header_bigalmaty_lake,
                new String[]{"Lake", "Photography"},
                new int[]{R.drawable.img_almatylake1, R.drawable.img_almatylake2, R.drawable.img_almatylake3, R.drawable.img_almatylake5, R.drawable.img_almatylake6,
                        R.drawable.img_almatylake4}));
        allDestinations.add(new PopularPlace(
                "4",
                "Charyn Canyon",
                "3 hrs drive • National Park",
                "Charyn Canyon is often compared to the Grand Canyon because of its dramatic rock formations. It is located about 200 kilometers east of Almaty. The canyon stretches for over 150 kilometers along the Charyn River. Its most famous section is the “Valley of Castles,” known for unique and colorful rock shapes. Charyn Canyon is one of Kazakhstan’s most impressive natural landmarks.",
                4.8f,
                R.drawable.header_charyn_canyon,
                new String[]{"Hiking", "Adventure"},
                new int[]{R.drawable.img_charyn1, R.drawable.img_charyn2, R.drawable.img_charyn3, R.drawable.img_charyn4, R.drawable.img_charyn5, R.drawable.img_charyn6}));
        allDestinations.add(new PopularPlace(
                "5",
                "Almaty central mosque",
                "City Center • Historic",
                "Almaty Central Mosque is one of the largest mosques in Kazakhstan. It is located near the Green Bazaar in the center of Almaty. The mosque was opened in 1999 and can accommodate up to 7,000 worshippers at a time. Its white marble walls and large golden dome make it very impressive. Today, it is an important religious and cultural center for Muslims in the city.",
                4.7f,
                R.drawable.header_central_mosque,
                new String[]{"Architecture", "History"},
                new int[]{R.drawable.img_central_mosque4, R.drawable.img_central_mosque2, R.drawable.img_central_mosque6, R.drawable.img_central_mosque1, R.drawable.img_central_mosque3, R.drawable.img_central_mosque5}));
        allDestinations.add(new PopularPlace(
                "6",
                "Medeu Rink",
                "20 min drive • Sports Complex",
                "Medeu Rink is a high-altitude outdoor ice skating rink located in the mountains near Almaty. It sits at 1,691 meters above sea level, making it one of the highest skating rinks in the world. Medeu is famous for hosting international speed skating competitions and setting world records. In winter, both professionals and visitors come to skate here. The surrounding mountain landscape makes it a unique and beautiful sports venue.",
                4.8f,
                R.drawable.header_medeu,
                new String[]{"Ice Skating", "Sports"},
                new int[]{R.drawable.img_medeu1, R.drawable.img_medeu2, R.drawable.img_medeu3, R.drawable.img_medeu5, R.drawable.img_medeu6, R.drawable.img_medeu4}));
        allDestinations.add(new PopularPlace(
                "7",
                "Green Bazaar",
                "City Center • Market",
                "Green Bazaar is one of the oldest and most popular markets in Almaty. It is the perfect place to experience local culture and traditional Kazakh cuisine. Visitors can buy fresh fruits, vegetables, meat, spices, and national sweets. The market is lively and colorful, attracting both locals and tourists. It offers a true taste of everyday life in Almaty.",
                4.6f,
                R.drawable.header_green_bazaar,
                new String[]{"Food", "Culture"},
                new int[]{R.drawable.img_green_bazaar6, R.drawable.img_green_bazaar1, R.drawable.img_green_bazaar4, R.drawable.img_green_bazaar5, R.drawable.img_green_bazaar3}));

        visibleDestinations.clear();
        visibleDestinations.addAll(allDestinations);

        weatherText.setText("Алматы • Жүктелуде...");
    }

    private void loadLiveWeather() {
        if (weatherApi == null) {
            weatherApi = new WeatherApi();
        }
        weatherApi.fetchAlmatyWeather(new WeatherApi.Callback() {
            @Override
            public void onSuccess(String temperatureC, String description) {
                if (weatherText != null) {
                    weatherText.setText("Алматы • " + temperatureC + " " + description);
                }
            }

            @Override
            public void onError(String message) {
                if (weatherText != null) {
                    weatherText.setText("Алматы • —");
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
