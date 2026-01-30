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
                "Shymbulak Resort",
                "25 min drive • Mountain Resort",
                4.9f,
                R.drawable.header_image,
                new String[] { "Skiing", "Hiking", "Nature" }));
        items.add(new PopularPlace(
                "Kok Tobe Hill",
                "City Center • Entertainment",
                4.8f,
                R.drawable.dir2,
                new String[] { "Cable Car", "City Views" }));
        items.add(new PopularPlace(
                "Big Almaty Lake",
                "1 hr drive • Scenic Nature",
                4.9f,
                R.drawable.dir1,
                new String[] { "Lake", "Photography" }));
        items.add(new PopularPlace(
                "Charyn Canyon",
                "3 hrs drive • National Park",
                4.8f,
                R.drawable.dir3,
                new String[] { "Hiking", "Adventure" }));
        items.add(new PopularPlace(
                "Zenkov Cathedral",
                "Panfilov Park • Historic",
                4.7f,
                R.drawable.spain,
                new String[] { "Architecture", "History" }));
        items.add(new PopularPlace(
                "Medeu Rink",
                "20 min drive • Sports Complex",
                4.8f,
                R.drawable.dir4,
                new String[] { "Ice Skating", "Sports" }));
        items.add(new PopularPlace(
                "Green Bazaar",
                "City Center • Market",
                4.6f,
                R.drawable.dir5,
                new String[] { "Food", "Culture" }));

        adapter.setItems(items);
    }

    private void openDetail(PopularPlace item) {
        Intent intent = new Intent(this, PopularDestinationDetailActivity.class);
        intent.putExtra(PopularDestinationDetailActivity.EXTRA_TITLE, item.title);
        intent.putExtra(PopularDestinationDetailActivity.EXTRA_SUBTITLE, item.subtitle);
        intent.putExtra(PopularDestinationDetailActivity.EXTRA_RATING, item.rating);
        intent.putExtra(PopularDestinationDetailActivity.EXTRA_IMAGE, item.imageRes);
        intent.putExtra(PopularDestinationDetailActivity.EXTRA_TAGS, item.tags);
        startActivity(intent);
    }
}
