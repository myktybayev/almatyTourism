package kz.informatics.okulik.nalog_app.home.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kz.informatics.okulik.R;
import kz.informatics.okulik.nalog_app.home.ExploreFragment;
import kz.informatics.okulik.nalog_app.home.module.Destination;

public class PopularDestinationAdapter extends RecyclerView.Adapter<PopularDestinationAdapter.VH> {

    public interface OnItemClickListener {
        void onClick(Destination item);
    }

    private final List<Destination> items = new ArrayList<>();
    private final OnItemClickListener listener;

    public PopularDestinationAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<Destination> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_popular_destination, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Destination d = items.get(position);

        h.title.setText(d.title);
        h.subtitle.setText(d.distance);
        h.rating.setText(String.format(Locale.US, "%.1f", d.rating));

        // Very simple local mapping for images (can be replaced with real data later)
        int imageRes = R.drawable.hawaii_image;
        String t = d.title == null ? "" : d.title.toLowerCase(Locale.US);
        if (t.contains("shymbulak")) imageRes = R.drawable.header_image;
        else if (t.contains("kok")) imageRes = R.drawable.dir2;
        else if (t.contains("lake")) imageRes = R.drawable.dir1;
        h.image.setImageResource(imageRes);

        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(d);
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

        VH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imagePopular);
            title = itemView.findViewById(R.id.textPopularTitle);
            subtitle = itemView.findViewById(R.id.textPopularSubtitle);
            rating = itemView.findViewById(R.id.textPopularRating);
        }
    }
}

