package kz.informatics.okulik.nalog_app.home.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private RecyclerView recycler;
    private TextView textTitle;
    private BrowseDestinationAdapter adapter;
    private String currentCategory;

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
        recycler = findViewById(R.id.recyclerDestinations);

        updateTitle();

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BrowseDestinationAdapter(
                this::openDetail,
                item -> Toast.makeText(this, getString(R.string.browse_button_map) + ": " + item.title, Toast.LENGTH_SHORT).show()
        );
        recycler.setAdapter(adapter);
        loadDestinations();

        buttonBack.setOnClickListener(v -> finish());
        buttonSearch.setOnClickListener(v -> Toast.makeText(this, getString(R.string.search_hint), Toast.LENGTH_SHORT).show());

        setupFilterChips();
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

    private void loadDestinations() {
        List<PopularPlace> list = DestinationsRepository.getInstance().getPlacesByCategory(this, currentCategory);
        if (adapter != null) {
            adapter.setItems(list);
        }
    }

    private void setupFilterChips() {
        TextView chipAll = findViewById(R.id.chipAll);
        TextView chipMuseums = findViewById(R.id.chipMuseums);
        TextView chipParks = findViewById(R.id.chipParks);
        TextView chipShopping = findViewById(R.id.chipShopping);

        chipAll.setOnClickListener(v -> {
            setChipSelected(chipAll);
            setChipUnselected(chipMuseums, chipParks, chipShopping);
        });
        chipMuseums.setOnClickListener(v -> {
            setChipSelected(chipMuseums);
            setChipUnselected(chipAll, chipParks, chipShopping);
        });
        chipParks.setOnClickListener(v -> {
            setChipSelected(chipParks);
            setChipUnselected(chipAll, chipMuseums, chipShopping);
        });
        chipShopping.setOnClickListener(v -> {
            setChipSelected(chipShopping);
            setChipUnselected(chipAll, chipMuseums, chipParks);
        });
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
