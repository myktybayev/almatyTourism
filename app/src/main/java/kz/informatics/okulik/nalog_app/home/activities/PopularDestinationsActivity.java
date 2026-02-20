package kz.informatics.okulik.nalog_app.home.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import kz.informatics.okulik.nalog_app.profile.LocaleHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kz.informatics.okulik.R;
import kz.informatics.okulik.nalog_app.home.adapters.PopularDestinationListAdapter;
import kz.informatics.okulik.nalog_app.home.module.PopularPlace;

public class PopularDestinationsActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }

    private RecyclerView recycler;
    private PopularDestinationListAdapter adapter;
    private final List<PopularPlace> allDestinations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_destinations);

        ImageView back = findViewById(R.id.buttonBack);
        back.setOnClickListener(v -> finish());

        recycler = findViewById(R.id.recyclerPopularList);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PopularDestinationListAdapter(
                item -> openDetail(item));
        recycler.setAdapter(adapter);

        loadLocalData();
    }

    private void loadLocalData() {
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

        adapter.setItems(allDestinations);
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
