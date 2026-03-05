package kz.informatics.okulik.nalog_app.trips;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kz.informatics.okulik.R;
import kz.informatics.okulik.nalog_app.home.module.PopularPlace;

/**
 * Adapter for suggested places in Add Location dialog. Supports live filter by search query.
 */
public class SuggestedPlaceAdapter extends RecyclerView.Adapter<SuggestedPlaceAdapter.Holder> {

    private final List<PopularPlace> fullList = new ArrayList<>();
    private final List<PopularPlace> filteredList = new ArrayList<>();
    private OnPlaceSelectedListener listener;

    public interface OnPlaceSelectedListener {
        void onPlaceSelected(PopularPlace place);
    }

    public void setOnPlaceSelectedListener(OnPlaceSelectedListener listener) {
        this.listener = listener;
    }

    public void setPlaces(List<PopularPlace> places) {
        fullList.clear();
        fullList.addAll(places);
        filteredList.clear();
        filteredList.addAll(places);
        notifyDataSetChanged();
    }

    /** Filter by search query (live search). */
    public void filter(String query) {
        filteredList.clear();
        if (query == null || query.trim().isEmpty()) {
            filteredList.addAll(fullList);
        } else {
            String q = query.toLowerCase(Locale.getDefault()).trim();
            for (PopularPlace p : fullList) {
                if (p.title != null && p.title.toLowerCase(Locale.getDefault()).contains(q))
                    filteredList.add(p);
                else if (p.subtitle != null && p.subtitle.toLowerCase(Locale.getDefault()).contains(q))
                    filteredList.add(p);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggested_place, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        PopularPlace place = filteredList.get(position);
        h.textPlaceName.setText(place.title);
        String category = place.categories != null && place.categories.length > 0
                ? place.categories[0] : "";
        String categoryLabel = category.isEmpty() ? "" : category.substring(0, 1).toUpperCase() + category.substring(1);
        h.textPlaceCategoryDistance.setText(categoryLabel + " · — km");
        h.imagePlace.setImageResource(place.imageRes);
        h.buttonAddPlace.setOnClickListener(v -> {
            if (listener != null) listener.onPlaceSelected(place);
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final ImageView imagePlace;
        final TextView textPlaceName;
        final TextView textPlaceCategoryDistance;
        final ImageButton buttonAddPlace;

        Holder(View itemView) {
            super(itemView);
            imagePlace = itemView.findViewById(R.id.imagePlace);
            textPlaceName = itemView.findViewById(R.id.textPlaceName);
            textPlaceCategoryDistance = itemView.findViewById(R.id.textPlaceCategoryDistance);
            buttonAddPlace = itemView.findViewById(R.id.buttonAddPlace);
        }
    }
}
