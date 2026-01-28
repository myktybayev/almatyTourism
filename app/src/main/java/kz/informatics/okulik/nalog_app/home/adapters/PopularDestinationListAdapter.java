package kz.informatics.okulik.nalog_app.home.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kz.informatics.okulik.R;
import kz.informatics.okulik.nalog_app.home.module.FavoriteRepository;
import kz.informatics.okulik.nalog_app.home.module.PopularPlace;

public class PopularDestinationListAdapter extends RecyclerView.Adapter<PopularDestinationListAdapter.VH> {

    public interface OnItemClickListener {
        void onClick(PopularPlace item);
    }

    private final List<PopularPlace> items = new ArrayList<>();
    private final OnItemClickListener listener;
    private final FavoriteRepository favorites = FavoriteRepository.getInstance();

    public PopularDestinationListAdapter(OnItemClickListener listener) {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_popular_destination_full, parent,
                false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        PopularPlace item = items.get(position);
        h.title.setText(item.title);
        h.subtitle.setText(item.subtitle);
        h.rating.setText(String.format(Locale.US, "%.1f", item.rating));
        h.image.setImageResource(item.imageRes);

        bindFavorite(h, item);

        // Tags (up to 3)
        TextView[] tags = new TextView[] { h.tag1, h.tag2, h.tag3 };
        for (int i = 0; i < tags.length; i++) {
            if (item.tags != null && i < item.tags.length) {
                tags[i].setText(item.tags[i]);
                tags[i].setVisibility(View.VISIBLE);
            } else {
                tags[i].setVisibility(View.GONE);
            }
        }

        h.itemView.setOnClickListener(v -> {
            if (listener != null)
                listener.onClick(item);
        });

        h.favorite.setOnClickListener(v -> {
            favorites.toggle(item);
            bindFavorite(h, item);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView image;
        ImageView favorite;
        TextView title;
        TextView subtitle;
        TextView rating;
        TextView tag1;
        TextView tag2;
        TextView tag3;

        VH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imagePopular);
            favorite = itemView.findViewById(R.id.buttonFavorite);
            title = itemView.findViewById(R.id.textTitle);
            subtitle = itemView.findViewById(R.id.textSubtitle);
            rating = itemView.findViewById(R.id.textRating);
            tag1 = itemView.findViewById(R.id.tag1);
            tag2 = itemView.findViewById(R.id.tag2);
            tag3 = itemView.findViewById(R.id.tag3);
        }
    }

    private void bindFavorite(@NonNull VH h, @NonNull PopularPlace item) {
        boolean isFav = favorites.isFavorite(item.id);
        if (isFav) {
            h.favorite.setImageResource(R.drawable.baseline_favorite_24);
            h.favorite.setColorFilter(ContextCompat.getColor(h.favorite.getContext(), R.color.upai_red));
        } else {
            h.favorite.setImageResource(R.drawable.favorite_outline_24);
            h.favorite.setColorFilter(ContextCompat.getColor(h.favorite.getContext(), R.color.black));
        }
    }
}
