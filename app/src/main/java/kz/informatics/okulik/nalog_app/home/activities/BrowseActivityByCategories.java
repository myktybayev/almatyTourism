package kz.informatics.okulik.nalog_app.home.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kz.informatics.okulik.R;
import kz.informatics.okulik.nalog_app.home.adapters.BrowseDestinationAdapter;
import kz.informatics.okulik.nalog_app.home.module.DestinationsRepository;
import kz.informatics.okulik.nalog_app.home.module.PopularPlace;
import kz.informatics.okulik.nalog_app.profile.LocaleHelper;

public class BrowseActivityByCategories extends AppCompatActivity {

    public static final String EXTRA_CATEGORY = "extra_category";

    public static final String CATEGORY_CITYLIFE = "citylife";
    public static final String CATEGORY_NATURE = "nature";
    public static final String CATEGORY_PARKS = "parks";
    public static final String CATEGORY_SPIRITUAL = "spiritual";

    public static final String SUBFILTER_ALL = "all";
    // citylife
    public static final String SUBFILTER_MALL = "malls";
    public static final String SUBFILTER_ENTERTAINMENT = "entertainment";
    public static final String SUBFILTER_SHOPPING = "shopping";



    // nature
    public static final String SUBFILTER_MOUNTAINS = "mountains";
    public static final String SUBFILTER_LAKES = "lakes";
    public static final String SUBFILTER_CANYONS = "canyons";
    // parks
    public static final String SUBFILTER_PARKS = "parks";
    public static final String SUBFILTER_CITY_PARKS = "city_parks";
    public static final String SUBFILTER_BOTANICAL = "botanical";
    public static final String SUBFILTER_FAMILY = "family";
    // spiritual
    public static final String SUBFILTER_MOSQUES = "mosques";
    public static final String SUBFILTER_CATHEDRALS = "cathedrals";
    public static final String SUBFILTER_MUSEUMS = "museums";

    private RecyclerView recycler;
    private TextView textTitle;
    private BrowseDestinationAdapter adapter;
    private String currentCategory;
    private String currentSubFilter = SUBFILTER_ALL;
    private String searchQuery = "";
    private LinearLayout layoutSearch;
    private EditText editSearch;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_by_categories);

        currentCategory = getIntent().getStringExtra(EXTRA_CATEGORY);
        if (currentCategory == null) {
            currentCategory = CATEGORY_CITYLIFE;
        }

        textTitle = findViewById(R.id.textTitle);
        ImageView buttonBack = findViewById(R.id.buttonBack);
        ImageView buttonSearch = findViewById(R.id.buttonSearch);
        layoutSearch = findViewById(R.id.layoutSearch);
        editSearch = findViewById(R.id.editSearch);
        recycler = findViewById(R.id.recyclerDestinations);

        updateTitle();

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BrowseDestinationAdapter(
                this::openDetail,
                item -> Toast.makeText(this, getString(R.string.browse_button_map) + ": " + item.title, Toast.LENGTH_SHORT).show()
        );
        recycler.setAdapter(adapter);

        buttonBack.setOnClickListener(v -> finish());
        buttonSearch.setOnClickListener(v -> toggleSearch());
        setupSearch();
        setupFilterChips();
        applyFilters();
    }

    private void updateTitle() {
        switch (currentCategory) {
            case CATEGORY_CITYLIFE:
                textTitle.setText(R.string.browse_citylife_title);
                break;
            case CATEGORY_NATURE:
                textTitle.setText(R.string.category_nature);
                break;
            case CATEGORY_PARKS:
                textTitle.setText(R.string.category_parks);
                break;
            case CATEGORY_SPIRITUAL:
                textTitle.setText(R.string.category_spiritual);
                break;
            default:
                textTitle.setText(R.string.browse_citylife_title);
        }
    }

    private void toggleSearch() {
        if (layoutSearch.getVisibility() == View.VISIBLE) {
            layoutSearch.setVisibility(View.GONE);
            editSearch.setText("");
            searchQuery = "";
            applyFilters();
        } else {
            layoutSearch.setVisibility(View.VISIBLE);
            editSearch.requestFocus();
        }
    }

    private void setupSearch() {
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchQuery = s != null ? s.toString().trim() : "";
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void applyFilters() {
        List<PopularPlace> list = DestinationsRepository.getInstance()
                .getPlacesByCategoryAndSubFilter(this, currentCategory, currentSubFilter);

        if (searchQuery != null && !searchQuery.isEmpty()) {
            String lower = searchQuery.toLowerCase();
            List<PopularPlace> filtered = new ArrayList<>();
            for (PopularPlace p : list) {
                if (matchesSearch(p, lower)) {
                    filtered.add(p);
                }
            }
            list = filtered;
        }

        if (adapter != null) {
            adapter.setItems(list);
        }
    }

    private boolean matchesSearch(PopularPlace p, String query) {
        if (p.title != null && p.title.toLowerCase().contains(query)) return true;
        if (p.subtitle != null && p.subtitle.toLowerCase().contains(query)) return true;
        if (p.about != null && p.about.toLowerCase().contains(query)) return true;
        if (p.tags != null) {
            for (String tag : p.tags) {
                if (tag != null && tag.toLowerCase().contains(query)) return true;
            }
        }
        return false;
    }

    private void setupFilterChips() {
        TextView chipAll = findViewById(R.id.chipAll);
        TextView chipFilter1 = findViewById(R.id.chipFilter1);
        TextView chipFilter2 = findViewById(R.id.chipFilter2);
        TextView chipFilter3 = findViewById(R.id.chipFilter3);

        updateChipLabels(chipFilter1, chipFilter2, chipFilter3);

        chipAll.setOnClickListener(v -> {
            currentSubFilter = SUBFILTER_ALL;
            setChipSelected(chipAll);
            setChipUnselected(chipFilter1, chipFilter2, chipFilter3);
            applyFilters();
        });
        chipFilter1.setOnClickListener(v -> selectSubFilter(getSubFilter1(), chipFilter1, chipAll, chipFilter2, chipFilter3));
        chipFilter2.setOnClickListener(v -> selectSubFilter(getSubFilter2(), chipFilter2, chipAll, chipFilter1, chipFilter3));
        chipFilter3.setOnClickListener(v -> selectSubFilter(getSubFilter3(), chipFilter3, chipAll, chipFilter1, chipFilter2));
    }

    private void selectSubFilter(String subFilter, TextView chipSelected, TextView chipAll, TextView chipOther1, TextView chipOther2) {
        currentSubFilter = subFilter;
        setChipSelected(chipSelected);
        setChipUnselected(chipAll, chipOther1, chipOther2);
        applyFilters();
    }

    private void updateChipLabels(TextView chip1, TextView chip2, TextView chip3) {
        switch (currentCategory) {
            case CATEGORY_CITYLIFE:
                chip1.setText(R.string.browse_mall);
                chip2.setText(R.string.browse_entertainment);
                chip3.setText(R.string.browse_filter_shopping);
                break;
            case CATEGORY_NATURE:
                chip1.setText(R.string.browse_filter_mountains);
                chip2.setText(R.string.browse_filter_lakes);
                chip3.setText(R.string.browse_filter_canyons);
                break;
            case CATEGORY_PARKS:
                chip1.setText(R.string.browse_filter_city_parks);
                chip2.setText(R.string.browse_filter_botanical);
                chip3.setText(R.string.browse_filter_family);
                break;
            case CATEGORY_SPIRITUAL:
                chip1.setText(R.string.browse_filter_mosques);
                chip2.setText(R.string.browse_filter_cathedrals);
                chip3.setText(R.string.browse_filter_museums);
                break;
            default:
                chip1.setText(R.string.browse_filter_museums);
                chip2.setText(R.string.browse_filter_parks);
                chip3.setText(R.string.browse_filter_shopping);
        }
    }

    private String getSubFilter1() {
        switch (currentCategory) {
            case CATEGORY_CITYLIFE: return SUBFILTER_MALL;
            case CATEGORY_NATURE: return SUBFILTER_MOUNTAINS;
            case CATEGORY_PARKS: return SUBFILTER_CITY_PARKS;
            case CATEGORY_SPIRITUAL: return SUBFILTER_MOSQUES;
            default: return SUBFILTER_MUSEUMS;
        }
    }

    private String getSubFilter2() {
        switch (currentCategory) {
            case CATEGORY_CITYLIFE: return SUBFILTER_ENTERTAINMENT;
            case CATEGORY_NATURE: return SUBFILTER_LAKES;
            case CATEGORY_PARKS: return SUBFILTER_BOTANICAL;
            case CATEGORY_SPIRITUAL: return SUBFILTER_CATHEDRALS;
            default: return SUBFILTER_PARKS;
        }
    }

    private String getSubFilter3() {
        switch (currentCategory) {
            case CATEGORY_CITYLIFE: return SUBFILTER_SHOPPING;
            case CATEGORY_NATURE: return SUBFILTER_CANYONS;
            case CATEGORY_PARKS: return SUBFILTER_FAMILY;
            case CATEGORY_SPIRITUAL: return SUBFILTER_MUSEUMS;
            default: return SUBFILTER_SHOPPING;
        }
    }

    private void setChipSelected(TextView chip) {
        chip.setBackgroundResource(R.drawable.browse_chip_selected);
        chip.setTextColor(ContextCompat.getColor(this, R.color.white));
    }

    private void setChipUnselected(TextView... chips) {
        for (TextView c : chips) {
            c.setBackgroundResource(R.drawable.activity_chip_bg);
            c.setTextColor(ContextCompat.getColor(this, R.color.black));
        }
    }

    private void openDetail(PopularPlace item) {
        Intent intent = new Intent(this, PopularDestinationDetailActivity.class);
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
