package kz.informatics.okulik.nalog_app.trips;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import kz.informatics.okulik.R;
import kz.informatics.okulik.nalog_app.cabinet.BookingItem;
import kz.informatics.okulik.nalog_app.cabinet.MyCabinet;
import kz.informatics.okulik.nalog_app.cabinet.SavedBooking;
import kz.informatics.okulik.nalog_app.cabinet.SavedBookingsRepository;
import kz.informatics.okulik.nalog_app.home.activities.GalleryPreviewActivity;
import kz.informatics.okulik.nalog_app.home.module.FavoriteRepository;
import kz.informatics.okulik.nalog_app.profile.LocaleHelper;

public class TripsDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TOUR_ID = "extra_tour_id";

    private TripDetailData data;
    private int participantsCount = 2;
    private Calendar selectedDate = Calendar.getInstance();
    private TextView textTotalLabel;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_detail);

        String tourId = getIntent().getStringExtra(EXTRA_TOUR_ID);
        data = TripsRepository.getInstance(this).getTripDetail(this, tourId);
        if (data == null) {
            finish();
            return;
        }

        ImageButton back = findViewById(R.id.buttonBack);
        back.setOnClickListener(v -> finish());

        ImageButton favorite = findViewById(R.id.buttonFavorite);
        FavoriteRepository repo = FavoriteRepository.getInstance(this);
        boolean isFav = repo.isFavorite("trip_" + data.tour.id);
        updateFavoriteIcon(favorite, isFav);
        favorite.setOnClickListener(v -> {
            boolean nowFav = repo.togglePlace("trip_" + data.tour.id, data.tour.name);
            updateFavoriteIcon(favorite, nowFav);
        });

        setupGallery();

        LinearLayout layoutLabels = findViewById(R.id.layoutLabels);
        layoutLabels.removeAllViews();
        for (String label : data.labels) {
            TextView chip = new TextView(this);
            chip.setText(label);
            chip.setTextSize(11);
            chip.setPadding((int) (12 * getResources().getDisplayMetrics().density), 6, 12, 6);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, (int) (8 * getResources().getDisplayMetrics().density), 0);
            chip.setLayoutParams(lp);
            if (label.toUpperCase().contains("BEST") || label.toUpperCase().contains("SELLER")) {
                chip.setTextColor(ContextCompat.getColor(this, R.color.hotel_gold));
                chip.setBackgroundResource(R.drawable.bg_trip_label_gold);
            } else {
                chip.setTextColor(ContextCompat.getColor(this, R.color.black));
                chip.setBackgroundResource(R.drawable.bg_trip_label_grey);
            }
            layoutLabels.addView(chip);
        }

        TextView textTitle = findViewById(R.id.textTitle);
        textTitle.setText(data.tour.name);

        TextView textRating = findViewById(R.id.textRating);
        textRating.setText(String.format(Locale.US, "%.1f %s", data.tour.organizerRating, "(128)"));

        TextView textLocation = findViewById(R.id.textLocation);
        textLocation.setText(data.location);
        textLocation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_location_on_24, 0, 0, 0);

        TextView textRoute = findViewById(R.id.textRoute);
        textRoute.setText(data.tour.locationsRoute);

        TextView textDurationLong = findViewById(R.id.textDurationLong);
        textDurationLong.setText(data.durationLong);

        ((TextView) findViewById(R.id.textDistance)).setText(data.distance);
        ((TextView) findViewById(R.id.textTransport)).setText(data.transport);
        ((TextView) findViewById(R.id.textGroupSize)).setText(data.groupSize);

        LinearLayout layoutItinerary = findViewById(R.id.layoutItinerary);
        layoutItinerary.removeAllViews();
        for (TripDay day : data.itinerary) {
            View dayHeaderView = LayoutInflater.from(this).inflate(R.layout.item_trip_day_header, layoutItinerary, false);
            ((TextView) dayHeaderView.findViewById(R.id.textDayLabel)).setText(day.dayLabel);
            layoutItinerary.addView(dayHeaderView);
            for (TripItineraryItem item : day.items) {
                View row = LayoutInflater.from(this).inflate(R.layout.item_trip_timeline_row, layoutItinerary, false);
                ((TextView) row.findViewById(R.id.textTime)).setText(item.time);
                ((TextView) row.findViewById(R.id.textActivityTitle)).setText(item.title);
                ((TextView) row.findViewById(R.id.textActivityDesc)).setText(item.description);
                layoutItinerary.addView(row);
            }
        }

        LinearLayout layoutIncluded = findViewById(R.id.layoutIncluded);
        layoutIncluded.removeAllViews();
        for (String s : data.included) {
            layoutIncluded.addView(newListItem(s, true));
        }
        LinearLayout layoutNotIncluded = findViewById(R.id.layoutNotIncluded);
        layoutNotIncluded.removeAllViews();
        for (String s : data.notIncluded) {
            layoutNotIncluded.addView(newListItem(s, false));
        }

        ImageView imageOrganizer = findViewById(R.id.imageOrganizer);
        imageOrganizer.setImageResource(data.organizerAvatarRes);
        ((TextView) findViewById(R.id.textOrganizerName)).setText(data.tour.organizerName);
        findViewById(R.id.iconVerified).setVisibility(data.organizerVerified ? View.VISIBLE : View.GONE);
        ((TextView) findViewById(R.id.textOrganizerRatingReviews)).setText(
                String.format(Locale.US, "%.1f (%s)", data.tour.organizerRating, data.organizerReviews));
        findViewById(R.id.buttonOrganizerMessage).setOnClickListener(v -> sendWhatsAppMessage(this, data.tour.organizerPhone, "Message from Almaty Tourism App"));
        findViewById(R.id.buttonOrganizerPhone).setOnClickListener(v -> makeCall(this, data.tour.organizerPhone));

        TextView textPricePerPerson = findViewById(R.id.textPricePerPerson);
        textPricePerPerson.setText(TripsRepository.formatPrice(data.pricePerPerson));
        ((TextView) findViewById(R.id.textEcoTax)).setText(data.ecoTaxLabel);

        textTotalLabel = findViewById(R.id.textTotalLabel);
        textTotalLabel.setText(getString(R.string.trip_detail_total_guests, participantsCount));
        updateTotalPrice();

        updateSelectedDateText();
        findViewById(R.id.rowSelectDate).setOnClickListener(v -> showDatePicker());
        findViewById(R.id.rowParticipants).setOnClickListener(v -> showParticipantsPicker());
        ((TextView) findViewById(R.id.textParticipants)).setText(participantsCount + " " + getString(R.string.trip_detail_adults));

        findViewById(R.id.buttonSendRequest).setOnClickListener(v -> showTripRequestDialog());
    }

    public void sendWhatsAppMessage(Context context, String phoneNumber, String message) {
        try {
            String url = "https://api.whatsapp.com/send?phone="
                    + phoneNumber
                    + "&text="
                    + URLEncoder.encode(message, "UTF-8");

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            intent.setPackage("com.whatsapp");

            context.startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "WhatsApp not installed", Toast.LENGTH_SHORT).show();
        }
    }

    public void makeCall(Context context, String phone) {

        Uri uri = Uri.parse("tel:" + phone);

        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        context.startActivity(intent);
    }

    private void showTripRequestDialog() {
        int total = data.pricePerPerson * participantsCount;
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_trip_request_summary, null);
        ((TextView) view.findViewById(R.id.textDialogTripName)).setText(data.tour.name);
        ((TextView) view.findViewById(R.id.textDialogDate)).setText(
                ((TextView) findViewById(R.id.textSelectedDate)).getText());
        ((TextView) view.findViewById(R.id.textDialogParticipants)).setText(
                ((TextView) findViewById(R.id.textParticipants)).getText());
        ((TextView) view.findViewById(R.id.textDialogPricePerPerson))
                .setText(TripsRepository.formatPrice(data.pricePerPerson));
        ((TextView) view.findViewById(R.id.textDialogTotal)).setText(TripsRepository.formatPrice(total));

        new AlertDialog.Builder(this)
                .setView(view)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(R.string.trip_request_save, (d, which) -> saveTripRequest(total))
                .show();
    }

    private void saveTripRequest(int totalPrice) {
        String ref = "TRIP-" + System.currentTimeMillis();
        String dateStr = formatDateForStorage(selectedDate);
        String guestsStr = participantsCount + " " + getString(R.string.trip_detail_adults);
        String totalPriceStr = TripsRepository.formatPrice(totalPrice);
        SavedBooking saved = new SavedBooking(
                "trip_" + data.tour.id,
                data.tour.name,
                ref,
                dateStr,
                "",
                guestsStr,
                totalPriceStr,
                data.durationLong,
                data.tour.imageResId,
                BookingItem.STATUS_CONFIRMED
        );
        SavedBookingsRepository.getInstance(this).addBooking(saved);

        Intent intent = new Intent(this, MyCabinet.class);
        intent.putExtra(MyCabinet.EXTRA_INITIAL_TAB, MyCabinet.TAB_TRIPS);
        startActivity(intent);
        finish();
    }

    private String formatDateForStorage(Calendar cal) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return sdf.format(cal.getTime());
    }

    private void setupGallery() {
        LinearLayout layoutGallery = findViewById(R.id.layoutTripGallery);
        layoutGallery.removeAllViews();
        if (data.galleryResIds == null || data.galleryResIds.length == 0) return;

        float density = getResources().getDisplayMetrics().density;
        int imageWidth = (int) (200 * density);
        int imageHeight = (int) (260 * density);
        int margin = (int) (4 * density);

        for (int i = 0; i < data.galleryResIds.length; i++) {
            final int position = i;
            ImageView iv = new ImageView(this);
            iv.setImageResource(data.galleryResIds[i]);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setLayoutParams(new LinearLayout.LayoutParams(imageWidth, imageHeight));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) iv.getLayoutParams();
            lp.setMargins(0, 0, margin, 0);
            iv.setLayoutParams(lp);
            iv.setOnClickListener(v -> openGalleryAt(position));
            layoutGallery.addView(iv);
        }
    }

    private void openGalleryAt(int position) {
        if (data.galleryResIds == null || data.galleryResIds.length == 0) return;
        Intent intent = new Intent(this, GalleryPreviewActivity.class);
        intent.putExtra("gallery_photos", data.galleryResIds);
        intent.putExtra("current_position", position);
        startActivity(intent);
    }

    private void updateTotalPrice() {
        int total = data.pricePerPerson * participantsCount;
        ((TextView) findViewById(R.id.textTotalPrice)).setText(TripsRepository.formatPrice(total));
        ((TextView) findViewById(R.id.textBottomTotal)).setText(TripsRepository.formatPrice(total));
    }

    private void updateSelectedDateText() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM", Locale.US);
        ((TextView) findViewById(R.id.textSelectedDate)).setText(sdf.format(selectedDate.getTime()));
    }

    private void showDatePicker() {
        DatePickerDialog.OnDateSetListener listener = (view, year, month, dayOfMonth) -> {
            selectedDate.set(Calendar.YEAR, year);
            selectedDate.set(Calendar.MONTH, month);
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateSelectedDateText();
        };
        DatePickerDialog dialog = new DatePickerDialog(this,
                listener,
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 86400000L);
        dialog.show();
    }

    private void showParticipantsPicker() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_trip_participants, null);
        NumberPicker picker = view.findViewById(R.id.numberPickerParticipants);
        picker.setMinValue(1);
        picker.setMaxValue(50);
        picker.setValue(participantsCount);
        picker.setWrapSelectorWheel(false);

        new AlertDialog.Builder(this)
                .setTitle(R.string.trip_detail_participants)
                .setView(view)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    participantsCount = picker.getValue();
                    ((TextView) findViewById(R.id.textParticipants)).setText(participantsCount + " " + getString(R.string.trip_detail_adults));
                    if (textTotalLabel != null) {
                        textTotalLabel.setText(getString(R.string.trip_detail_total_guests, participantsCount));
                    }
                    updateTotalPrice();
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private View newListItem(String text, boolean included) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(13);
        tv.setTextColor(ContextCompat.getColor(this, R.color.black));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = (int) (4 * getResources().getDisplayMetrics().density);
        tv.setLayoutParams(lp);
        tv.setCompoundDrawablesWithIntrinsicBounds(
                included ? R.drawable.ic_included : R.drawable.ic_not_included, 0, 0, 0);
        tv.setCompoundDrawablePadding((int) (8 * getResources().getDisplayMetrics().density));
        return tv;
    }

    private void updateFavoriteIcon(ImageButton btn, boolean isFavorite) {
        if (isFavorite) {
            btn.setImageResource(R.drawable.baseline_favorite_24);
            btn.setColorFilter(ContextCompat.getColor(this, R.color.upai_red));
        } else {
            btn.setImageResource(R.drawable.favorite_outline_24);
            btn.setColorFilter(ContextCompat.getColor(this, R.color.black));
        }
    }

    public static void open(Context context, String tourId) {
        Intent intent = new Intent(context, TripsDetailActivity.class);
        intent.putExtra(EXTRA_TOUR_ID, tourId);
        context.startActivity(intent);
    }
}
