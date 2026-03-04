package kz.informatics.okulik.nalog_app.trips;

import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kz.informatics.okulik.R;

public class GuidedToursAdapter extends RecyclerView.Adapter<GuidedToursAdapter.VH> {

    public interface OnTourDetailsListener {
        void onDetailsClick(GuidedTour tour);
    }

    private final List<GuidedTour> items = new ArrayList<>();
    private final OnTourDetailsListener detailsListener;

    public GuidedToursAdapter(OnTourDetailsListener detailsListener) {
        this.detailsListener = detailsListener;
    }

    public void setItems(List<GuidedTour> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip_guided, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        GuidedTour t = items.get(position);
        h.image.setImageResource(t.imageResId);
        h.textDuration.setText(t.duration);
        h.textTourName.setText(t.name);
        h.textPrice.setText(TripsRepository.formatPrice(t.totalPrice));
        h.textOrganizer.setText(t.organizerName);
        h.textOrganizerRating.setText(String.format(Locale.US, "%.1f", t.organizerRating));

        h.textRoute.setText(t.locationsRoute);

        h.textAvailability.setText(t.availability);
        h.textGroupInfo.setText(t.groupInfo);
        h.buttonDetails.setOnClickListener(v -> {
            if (detailsListener != null) detailsListener.onDetailsClick(t);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView image;
        TextView textDuration, textTourName, textPrice;
        TextView textOrganizer, textOrganizerRating, textRoute, textAvailability, textGroupInfo;
        Button buttonDetails;

        VH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageTrip);
            textDuration = itemView.findViewById(R.id.textDuration);
            textTourName = itemView.findViewById(R.id.textTourName);
            textPrice = itemView.findViewById(R.id.textPrice);
            textOrganizer = itemView.findViewById(R.id.textOrganizer);
            textOrganizerRating = itemView.findViewById(R.id.textOrganizerRating);
            textRoute = itemView.findViewById(R.id.textRoute);
            textAvailability = itemView.findViewById(R.id.textAvailability);
            textGroupInfo = itemView.findViewById(R.id.textGroupInfo);
            buttonDetails = itemView.findViewById(R.id.buttonDetails);
        }
    }
}
