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
import kz.informatics.okulik.nalog_app.home.module.PopularPlace;

public class PopularDestinationAdapter extends RecyclerView.Adapter<PopularDestinationAdapter.VH> {

    public interface OnItemClickListener {
        void onClick(PopularPlace item);
    }

    private final List<PopularPlace> items = new ArrayList<>();
    private final OnItemClickListener listener;

    public PopularDestinationAdapter(OnItemClickListener listener) {
        this.listener = listener;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_popular_destination, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        PopularPlace d = items.get(position);

        h.title.setText(d.title);
        h.subtitle.setText(d.subtitle);
        h.rating.setText(String.format(Locale.US, "%.1f", d.rating));

        // Use imageRes from PopularPlace
        if (d.imageRes != 0) {
            h.image.setImageResource(d.imageRes);
        }

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

