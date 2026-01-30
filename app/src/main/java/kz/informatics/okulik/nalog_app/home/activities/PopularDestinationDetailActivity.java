package kz.informatics.okulik.nalog_app.home.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import kz.informatics.okulik.R;

public class PopularDestinationDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_SUBTITLE = "extra_subtitle";
    public static final String EXTRA_RATING = "extra_rating";
    public static final String EXTRA_IMAGE = "extra_image";
    public static final String EXTRA_TAGS = "extra_tags";
    public static final String EXTRA_GALLERY_PHOTOS = "extra_gallery_photos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_destination_detail);

        ImageView back = findViewById(R.id.buttonBack);
        back.setOnClickListener(v -> finish());

        Intent intent = getIntent();
        String title = intent.getStringExtra(EXTRA_TITLE);
        String subtitle = intent.getStringExtra(EXTRA_SUBTITLE);
        float rating = intent.getFloatExtra(EXTRA_RATING, 0f);
        int imageRes = intent.getIntExtra(EXTRA_IMAGE, 0);
        String[] tags = intent.getStringArrayExtra(EXTRA_TAGS);
        int[] galleryPhotos = intent.getIntArrayExtra(EXTRA_GALLERY_PHOTOS);

        ImageView headerImage = findViewById(R.id.imageHeader);
        TextView titleText = findViewById(R.id.textTitle);
        TextView ratingText = findViewById(R.id.textRating);
        TextView subtitleText = findViewById(R.id.textSubtitle);
        TextView aboutText = findViewById(R.id.textAbout);

        if (imageRes != 0) {
            headerImage.setImageResource(imageRes);
        }
        titleText.setText(title != null ? title : "");
        ratingText.setText(String.valueOf(rating));
        subtitleText.setText(subtitle != null ? subtitle : "");
        aboutText.setText(buildAboutText(title));

        TextView tag1 = findViewById(R.id.tag1);
        TextView tag2 = findViewById(R.id.tag2);
        TextView tag3 = findViewById(R.id.tag3);
        TextView[] tagViews = new TextView[]{tag1, tag2, tag3};
        for (int i = 0; i < tagViews.length; i++) {
            if (tags != null && i < tags.length) {
                tagViews[i].setText(tags[i]);
                tagViews[i].setVisibility(LinearLayout.VISIBLE);
            } else {
                tagViews[i].setVisibility(LinearLayout.GONE);
            }
        }

        // Load gallery photos
        loadGalleryPhotos(galleryPhotos);
    }

    private String buildAboutText(String title) {
        if (title == null) return "";
        String lower = title.toLowerCase();
        if (lower.contains("lake")) {
            return "Nestled in the Trans-Ili Alatau mountains, this alpine lake is famous for its turquoise waters and scenic surroundings.";
        }
        if (lower.contains("shymbulak")) {
            return "A popular mountain resort with breathtaking views, perfect for skiing and year‑round outdoor adventures.";
        }
        if (lower.contains("kok")) {
            return "A city viewpoint and entertainment spot offering panoramic views and cultural attractions.";
        }
        return "Discover a beautiful destination with unique atmosphere and unforgettable views.";
    }

    private int[] galleryPhotosArray; // Store gallery photos for click listeners

    private void loadGalleryPhotos(int[] galleryPhotos) {
        galleryPhotosArray = galleryPhotos; // Store for click listeners
        
        ImageView gallery1 = findViewById(R.id.gallery1);
        ImageView gallery2 = findViewById(R.id.gallery2);
        ImageView gallery3 = findViewById(R.id.gallery3);
        ImageView gallery4 = findViewById(R.id.gallery4);
        TextView gallery4Text = findViewById(R.id.gallery4Text);

        ImageView[] galleryViews = new ImageView[]{gallery1, gallery2, gallery3, gallery4};

        if (galleryPhotos != null && galleryPhotos.length > 0) {
            // Show first 4 photos
            for (int i = 0; i < galleryViews.length; i++) {
                if (i < galleryPhotos.length) {
                    galleryViews[i].setImageResource(galleryPhotos[i]);
                    galleryViews[i].setVisibility(android.view.View.VISIBLE);
                    
                    // Add click listener to open gallery preview
                    final int position = i;
                    galleryViews[i].setOnClickListener(v -> openGalleryPreview(position));
                } else {
                    galleryViews[i].setVisibility(android.view.View.GONE);
                }
            }

            // Show "+X" text if there are more than 4 photos
            if (galleryPhotos.length > 4) {
                if (gallery4Text != null) {
                    gallery4Text.setText("+" + (galleryPhotos.length - 4));
                    gallery4Text.setVisibility(android.view.View.VISIBLE);
                    // Also make gallery4 clickable
                    gallery4.setOnClickListener(v -> openGalleryPreview(3));
                }
            } else {
                if (gallery4Text != null) {
                    gallery4Text.setVisibility(android.view.View.GONE);
                }
            }
        } else {
            // Hide all gallery views if no photos
            for (ImageView view : galleryViews) {
                view.setVisibility(android.view.View.GONE);
            }
            if (gallery4Text != null) {
                gallery4Text.setVisibility(android.view.View.GONE);
            }
        }
    }

    private void openGalleryPreview(int startPosition) {
        if (galleryPhotosArray == null || galleryPhotosArray.length == 0) {
            return;
        }
        
        Intent intent = new Intent(this, GalleryPreviewActivity.class);
        intent.putExtra("gallery_photos", galleryPhotosArray);
        intent.putExtra("current_position", startPosition);
        startActivity(intent);
    }
}

