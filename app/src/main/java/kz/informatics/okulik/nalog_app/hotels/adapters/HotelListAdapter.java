package kz.informatics.okulik.nalog_app.hotels.adapters;

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
import kz.informatics.okulik.nalog_app.hotels.module.Hotel;

public class HotelListAdapter extends RecyclerView.Adapter<HotelListAdapter.VH> {

    public interface OnHotelClickListener {
        void onHotelClick(Hotel hotel);
    }

    private final List<Hotel> items = new ArrayList<>();
    private final OnHotelClickListener listener;

    public HotelListAdapter(OnHotelClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<Hotel> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotel_card, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Hotel item = items.get(position);

        h.image.setImageResource(item.imageRes);
        h.name.setText(item.name);
        h.address.setText(item.address);
        h.description.setText(item.description);
        h.rating.setText(String.format(Locale.US, "%.1f", item.rating));
        h.price.setText(String.format(Locale.US, "%,d 〒", item.pricePerNight));

        h.buttonViewDetails.setOnClickListener(v -> {
            if (listener != null) listener.onHotelClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView address;
        TextView description;
        TextView rating;
        TextView price;
        Button buttonViewDetails;

        VH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageHotel);
            name = itemView.findViewById(R.id.textName);
            address = itemView.findViewById(R.id.textAddress);
            description = itemView.findViewById(R.id.textDescription);
            rating = itemView.findViewById(R.id.textRating);
            price = itemView.findViewById(R.id.textPrice);
            buttonViewDetails = itemView.findViewById(R.id.buttonViewDetails);
        }
    }
}
