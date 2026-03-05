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

import kz.informatics.okulik.R;

public class SelfPlannedTripsAdapter extends RecyclerView.Adapter<SelfPlannedTripsAdapter.VH> {

    public interface OnSelfPlannedTripListener {
        void onOpenDetails(SelfPlannedTrip trip);
        void onEditTrip(SelfPlannedTrip trip);
        void onDeleteTrip(SelfPlannedTrip trip);
        void onMenuClick(SelfPlannedTrip trip, View anchor);
    }

    private final List<SelfPlannedTrip> items = new ArrayList<>();
    private final OnSelfPlannedTripListener listener;

    public SelfPlannedTripsAdapter(OnSelfPlannedTripListener listener) {
        this.listener = listener;
    }

    public void setItems(List<SelfPlannedTrip> list) {
        items.clear();
        if (list != null) items.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip_self_planned, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        SelfPlannedTrip trip = items.get(position);
        h.textTitle.setText(trip.title);
        h.textLocation.setText(trip.locationTheme);
        h.textDates.setText(trip.dates);
        h.textCost.setText(SelfPlannedTripsRepository.formatCost(trip.estimatedCostTenge));

        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onOpenDetails(trip);
        });
        h.textTitle.setOnClickListener(v -> {
            if (listener != null) listener.onOpenDetails(trip);
        });

        h.textLocation.setOnClickListener(v -> {
            if (listener != null) listener.onOpenDetails(trip);
        });

        h.textDates.setOnClickListener(v -> {
            if (listener != null) listener.onOpenDetails(trip);
        });

        h.buttonMenu.setOnClickListener(v -> {
            if (listener != null) listener.onMenuClick(trip, h.buttonMenu);
        });
        h.buttonEditTrip.setOnClickListener(v -> {
            if (listener != null) listener.onEditTrip(trip);
        });
        h.buttonDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteTrip(trip);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final TextView textTitle;
        final TextView textLocation;
        final TextView textDates;
        final TextView textCost;
        final ImageButton buttonMenu;
        final android.widget.Button buttonEditTrip;
        final android.widget.Button buttonDelete;

        VH(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textSelfPlannedTitle);
            textLocation = itemView.findViewById(R.id.textSelfPlannedLocation);
            textDates = itemView.findViewById(R.id.textSelfPlannedDates);
            textCost = itemView.findViewById(R.id.textSelfPlannedCost);
            buttonMenu = itemView.findViewById(R.id.buttonMenu);
            buttonEditTrip = itemView.findViewById(R.id.buttonEditTrip);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
