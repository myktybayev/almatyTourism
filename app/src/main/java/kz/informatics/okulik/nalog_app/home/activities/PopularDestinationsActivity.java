package kz.informatics.okulik.nalog_app.home.activities;

import android.os.Bundle;
import android.content.Intent;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kz.informatics.okulik.R;
import kz.informatics.okulik.nalog_app.home.adapters.PopularDestinationListAdapter;
import kz.informatics.okulik.nalog_app.home.module.PopularPlace;

public class PopularDestinationsActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private PopularDestinationListAdapter adapter;
    private final List<PopularPlace> items = new ArrayList<>();

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
        items.clear();
        items.add(new PopularPlace(
                "1",
                "Shymbulak Resort",
                "25 min drive • Mountain Resort",
                4.9f,
                R.drawable.header_shymbulak,
                new String[] { "Skiing", "Hiking", "Nature" },
                new int[] { R.drawable.header_image, R.drawable.dir1, R.drawable.dir2, R.drawable.dir3, R.drawable.dir4, R.drawable.dir5 }));
        items.add(new PopularPlace(
                "2",
                "Kok Tobe Hill",
                "City Center • Entertainment",
                4.8f,
                R.drawable.header_koktobe,
                new String[] { "Cable Car", "City Views" },
                new int[] { R.drawable.img_koktobe1, R.drawable.img_koktobe2, R.drawable.img_koktobe3, R.drawable.img_koktobe4, R.drawable.header_koktobe }));
        items.add(new PopularPlace(
                "3",
                "Big Almaty Lake",
                "1 hr drive • Scenic Nature",
                4.9f,
                R.drawable.header_bigalmaty_lake,
                new String[] { "Lake", "Photography" },
                new int[] { R.drawable.img_almatylake1, R.drawable.img_almatylake2, R.drawable.img_almatylake3, R.drawable.img_almatylake4}));
        items.add(new PopularPlace(
                "4",
                "Charyn Canyon",
                "3 hrs drive • National Park",
                4.8f,
                R.drawable.header_charyn_canyon,
                new String[] { "Hiking", "Adventure" },
                new int[] { R.drawable.dir3, R.drawable.dir1, R.drawable.dir2, R.drawable.dir4 }));
        items.add(new PopularPlace(
                "5",
                "Zenkov Cathedral",
                "Panfilov Park • Historic",
                4.7f,
                R.drawable.header_zenkov_cathedral,
                new String[] { "Architecture", "History" },
                new int[] { R.drawable.spain, R.drawable.dir1, R.drawable.dir2, R.drawable.dir3, R.drawable.dir4, R.drawable.dir5 }));
        items.add(new PopularPlace(
                "6",
                "Medeu Rink",
                "20 min drive • Sports Complex",
                4.8f,
                R.drawable.header_medeu,
                new String[] { "Ice Skating", "Sports" },
                new int[] { R.drawable.dir4, R.drawable.dir1, R.drawable.dir2, R.drawable.dir3 }));
        items.add(new PopularPlace(
                "7",
                "Green Bazaar",
                "City Center • Market",
                4.6f,
                R.drawable.header_green_bazaar,
                new String[] { "Food", "Culture" },
                new int[] { R.drawable.dir5, R.drawable.dir1, R.drawable.dir2, R.drawable.dir3, R.drawable.dir4, R.drawable.header_image }));

        adapter.setItems(items);
    }

    private void openDetail(PopularPlace item) {
        Intent intent = new Intent(this, PopularDestinationDetailActivity.class);
        intent.putExtra(PopularDestinationDetailActivity.EXTRA_TITLE, item.title);
        intent.putExtra(PopularDestinationDetailActivity.EXTRA_SUBTITLE, item.subtitle);
        intent.putExtra(PopularDestinationDetailActivity.EXTRA_RATING, item.rating);
        intent.putExtra(PopularDestinationDetailActivity.EXTRA_IMAGE, item.imageRes);
        intent.putExtra(PopularDestinationDetailActivity.EXTRA_TAGS, item.tags);
        intent.putExtra(PopularDestinationDetailActivity.EXTRA_GALLERY_PHOTOS, item.listOfGalleryPhotos);
        startActivity(intent);
    }
}
