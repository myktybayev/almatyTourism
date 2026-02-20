package kz.informatics.okulik.nalog_app.home.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import kz.informatics.okulik.nalog_app.profile.LocaleHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kz.informatics.okulik.R;
import kz.informatics.okulik.nalog_app.home.adapters.PopularDestinationListAdapter;
import kz.informatics.okulik.nalog_app.home.module.DestinationsRepository;
import kz.informatics.okulik.nalog_app.home.module.PopularPlace;

public class PopularDestinationsActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }

    private RecyclerView recycler;
    private PopularDestinationListAdapter adapter;

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

        List<PopularPlace> places = DestinationsRepository.getInstance().getPlacesForExplore(this);
        adapter.setItems(places);
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
