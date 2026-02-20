package kz.informatics.okulik.nalog_app.home.adapters;

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
import kz.informatics.okulik.nalog_app.home.module.PopularPlace;

public class BrowseDestinationAdapter extends RecyclerView.Adapter<BrowseDestinationAdapter.VH> {

    public interface OnDetailClickListener {
        void onDetailClick(PopularPlace item);
    }

    public interface OnMapClickListener {
        void onMapClick(PopularPlace item);
    }

    private final List<PopularPlace> items = new ArrayList<>();
    private final OnDetailClickListener detailListener;
    private final OnMapClickListener mapListener;

    public BrowseDestinationAdapter(OnDetailClickListener detailListener, OnMapClickListener mapListener) {
        this.detailListener = detailListener;
        this.mapListener = mapListener;
    }

    public void setItems(List<PopularPlace> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_browse_destination, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        PopularPlace item = items.get(position);

        h.image.setImageResource(item.imageRes);
        h.title.setText(item.title);
        h.subtitle.setText(item.subtitle);
        h.rating.setText(String.format(Locale.US, "%.1f", item.rating));
        h.about.setText(item.about != null ? item.about : "");

        // Tags (up to 3)
        TextView[] tags = {h.tag1, h.tag2, h.tag3};
        for (int i = 0; i < tags.length; i++) {
            if (item.tags != null && i < item.tags.length) {
                tags[i].setText(item.tags[i]);
                tags[i].setVisibility(View.VISIBLE);
            } else {
                tags[i].setVisibility(View.GONE);
            }
        }

        h.buttonDetails.setOnClickListener(v -> {
            if (detailListener != null) detailListener.onDetailClick(item);
        });

        h.buttonMap.setOnClickListener(v -> {
            if (mapListener != null) mapListener.onMapClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView subtitle;
        TextView rating;
        TextView about;
        TextView tag1;
        TextView tag2;
        TextView tag3;
        Button buttonMap;
        Button buttonDetails;

        VH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imagePopular);
            title = itemView.findViewById(R.id.textTitle);
            subtitle = itemView.findViewById(R.id.textSubtitle);
            rating = itemView.findViewById(R.id.textRating);
            about = itemView.findViewById(R.id.textAbout);
            tag1 = itemView.findViewById(R.id.tag1);
            tag2 = itemView.findViewById(R.id.tag2);
            tag3 = itemView.findViewById(R.id.tag3);
            buttonMap = itemView.findViewById(R.id.buttonMap);
            buttonDetails = itemView.findViewById(R.id.buttonDetails);
        }
    }
}
