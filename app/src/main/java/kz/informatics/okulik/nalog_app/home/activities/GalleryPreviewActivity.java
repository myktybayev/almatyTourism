package kz.informatics.okulik.nalog_app.home.activities;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import kz.informatics.okulik.R;
import kz.informatics.okulik.nalog_app.home.adapters.GalleryPagerAdapter;

public class GalleryPreviewActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TextView textPageIndicator;
    private ImageView buttonClose;
    private GalleryPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Full screen mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        setContentView(R.layout.activity_gallery_preview);

        int[] galleryPhotos = getIntent().getIntArrayExtra("gallery_photos");
        int currentPosition = getIntent().getIntExtra("current_position", 0);

        viewPager = findViewById(R.id.viewPagerGallery);
        textPageIndicator = findViewById(R.id.textPageIndicator);
        buttonClose = findViewById(R.id.buttonClose);

        if (galleryPhotos != null && galleryPhotos.length > 0) {
            adapter = new GalleryPagerAdapter(galleryPhotos);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(currentPosition, false);
            
            updatePageIndicator(currentPosition + 1, galleryPhotos.length);

            viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    updatePageIndicator(position + 1, galleryPhotos.length);
                }
            });
        }

        buttonClose.setOnClickListener(v -> finish());
    }

    private void updatePageIndicator(int current, int total) {
        textPageIndicator.setText(current + " / " + total);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
