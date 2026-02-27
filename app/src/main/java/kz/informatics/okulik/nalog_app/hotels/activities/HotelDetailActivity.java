package kz.informatics.okulik.nalog_app.hotels.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import java.util.Locale;

import kz.informatics.okulik.R;
import kz.informatics.okulik.nalog_app.home.module.FavoriteRepository;
import kz.informatics.okulik.nalog_app.hotels.module.Hotel;
import kz.informatics.okulik.nalog_app.hotels.module.HotelsRepository;
import kz.informatics.okulik.nalog_app.profile.LocaleHelper;

public class HotelDetailActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }

    public static final String EXTRA_HOTEL_ID = "extra_hotel_id";
    public static final String EXTRA_CHECK_IN = "extra_check_in";
    public static final String EXTRA_CHECK_OUT = "extra_check_out";
    public static final String EXTRA_GUESTS = "extra_guests";
    public static final String EXTRA_ROOMS = "extra_rooms";

    private Hotel hotel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);

        String hotelId = getIntent().getStringExtra(EXTRA_HOTEL_ID);
        hotel = HotelsRepository.getInstance().getHotelById(this, hotelId);
        if (hotel == null) {
            finish();
            return;
        }

        ImageButton back = findViewById(R.id.buttonBack);
        back.setOnClickListener(v -> finish());

        ImageButton favorite = findViewById(R.id.buttonFavorite);
        FavoriteRepository repo = FavoriteRepository.getInstance();
        updateFavoriteIcon(favorite, repo.isFavorite("hotel_" + hotel.id));
        favorite.setOnClickListener(v -> {
            boolean isFav = repo.togglePlace("hotel_" + hotel.id, hotel.name);
            updateFavoriteIcon(favorite, isFav);
        });

        ImageView imageHero = findViewById(R.id.imageHero);
        imageHero.setImageResource(hotel.imageRes);

        TextView textName = findViewById(R.id.textName);
        TextView textAddress = findViewById(R.id.textAddress);
        TextView textRating = findViewById(R.id.textRating);
        TextView textAbout = findViewById(R.id.textAbout);
        TextView textPrice = findViewById(R.id.textPrice);

        textName.setText(hotel.name);
        textAddress.setText(hotel.address);
        textRating.setText(String.format(Locale.US, "%.1f", hotel.rating));
        textAbout.setText(hotel.description);
        textPrice.setText(String.format(Locale.US, "%,d 〒", hotel.pricePerNight));

        setupFacilities();
        setupGallery();
        setupOpenMap();

        Button buttonBookNow = findViewById(R.id.buttonBookNow);
        buttonBookNow.setOnClickListener(v -> openBooking());
    }

    private void updateFavoriteIcon(ImageButton btn, boolean isFavorite) {
        if (btn == null) return;
        if (isFavorite) {
            btn.setImageResource(R.drawable.baseline_favorite_24);
            btn.setColorFilter(ContextCompat.getColor(this, R.color.upai_red));
        } else {
            btn.setImageResource(R.drawable.favorite_outline_24);
            btn.setColorFilter(ContextCompat.getColor(this, R.color.black));
        }
    }

    private void setupFacilities() {
        LinearLayout layout = findViewById(R.id.layoutFacilities);
        if (layout == null) return;

        int[] iconRes = {R.drawable.ic_wifi, R.drawable.ic_dining, R.drawable.ic_pool, R.drawable.ic_spa};
        int[] labelRes = {R.string.hotel_facility_wifi, R.string.hotel_facility_dining,
                R.string.hotel_facility_pool, R.string.hotel_facility_spa};

        String[] keys = {"wifi", "dining", "pool", "spa"};
        for (int i = 0; i < keys.length; i++) {
            boolean has = false;
            for (String f : hotel.facilities) {
                if (keys[i].equals(f)) {
                    has = true;
                    break;
                }
            }
            if (!has) continue;

            float density = getResources().getDisplayMetrics().density;
            int iconSize = (int) (32 * density);
            int circleSize = (int) (48 * density);
            int itemMargin = (int) (24 * density);

            LinearLayout item = new LinearLayout(this);
            item.setOrientation(LinearLayout.VERTICAL);
            item.setGravity(Gravity.CENTER_HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    (int) (80 * density),
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, itemMargin, 0);
            item.setLayoutParams(params);

            FrameLayout iconWrap = new FrameLayout(this);
            iconWrap.setBackgroundResource(R.drawable.facility_icon_circle_bg);
            FrameLayout.LayoutParams wrapParams = new FrameLayout.LayoutParams(circleSize, circleSize);
            iconWrap.setLayoutParams(wrapParams);

            ImageView icon = new ImageView(this);
            icon.setImageResource(iconRes[i]);
            FrameLayout.LayoutParams iconParams = new FrameLayout.LayoutParams(iconSize, iconSize);
            iconParams.gravity = Gravity.CENTER;
            icon.setLayoutParams(iconParams);
            iconWrap.addView(icon);
            item.addView(iconWrap);

            TextView label = new TextView(this);
            label.setText(getString(labelRes[i]));
            label.setTextSize(12);
            label.setTextColor(ContextCompat.getColor(this, R.color.black));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.topMargin = (int) (4 * density);
            label.setLayoutParams(lp);
            item.addView(label);

            layout.addView(item);
        }
    }

    private void setupGallery() {
        LinearLayout layout = findViewById(R.id.layoutGallery);
        if (layout == null || hotel.galleryResIds.length == 0) return;

        float density = getResources().getDisplayMetrics().density;
        int size = (int) (120 * density);
        int margin = (int) (8 * density);
        int cornerRadius = (int) (12 * density);

        for (int i = 0; i < Math.min(hotel.galleryResIds.length, 4); i++) {
            CardView card = new CardView(this);
            card.setRadius(cornerRadius);
            card.setCardElevation(0);
            card.setUseCompatPadding(false);
            card.setPreventCornerOverlap(false);
            LinearLayout.LayoutParams cardLp = new LinearLayout.LayoutParams(size, size);
            cardLp.setMargins(0, 0, margin, 0);
            card.setLayoutParams(cardLp);

            ImageView iv = new ImageView(this);
            iv.setImageResource(hotel.galleryResIds[i]);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            CardView.LayoutParams ivLp = new CardView.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            iv.setLayoutParams(ivLp);
            card.addView(iv);
            layout.addView(card);
        }
    }

    private void setupOpenMap() {
        Button btn = findViewById(R.id.buttonOpenMap);
        if (btn == null) return;
        btn.setOnClickListener(v -> {
            if (hotel.location == null || hotel.location.isEmpty()) return;
            Uri uri = Uri.parse("geo:" + hotel.location + "?q=" + hotel.location + "(" + hotel.name + ")");
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        });
    }

    private void openBooking() {
        Intent intent = new Intent(this, BookingActivity.class);
        intent.putExtra(BookingActivity.EXTRA_HOTEL_ID, hotel.id);
        intent.putExtra(BookingActivity.EXTRA_CHECK_IN, getIntent().getStringExtra(EXTRA_CHECK_IN));
        intent.putExtra(BookingActivity.EXTRA_CHECK_OUT, getIntent().getStringExtra(EXTRA_CHECK_OUT));
        intent.putExtra(BookingActivity.EXTRA_GUESTS, getIntent().getIntExtra(EXTRA_GUESTS, 2));
        intent.putExtra(BookingActivity.EXTRA_ROOMS, getIntent().getIntExtra(EXTRA_ROOMS, 1));
        startActivity(intent);
    }
}
