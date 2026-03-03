package kz.informatics.okulik.nalog_app.cabinet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kz.informatics.okulik.R;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.VH> {

    public interface OnBookingActionListener {
        void onAction(BookingItem item, String action);
    }

    private final List<BookingItem> items;
    private final OnBookingActionListener listener;

    public BookingAdapter(List<BookingItem> items, OnBookingActionListener listener) {
        this.items = items != null ? items : new java.util.ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_cabinet_booking, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        BookingItem item = items.get(position);
        Context c = holder.itemView.getContext();
        holder.imageHotel.setImageResource(item.imageResId);
        holder.textHotelName.setText(item.hotelName);
        holder.textRef.setText(c.getString(R.string.cabinet_ref, item.reference));
        if (BookingItem.STATUS_CONFIRMED.equals(item.status)) {
            holder.textStatus.setText(R.string.cabinet_status_confirmed);
            holder.textStatus.setBackgroundResource(R.drawable.bg_status_confirmed);
            holder.textStatus.setTextColor(c.getColor(R.color.cabinet_status_confirmed_text));
        } else {
            holder.textStatus.setText(R.string.cabinet_status_completed);
            holder.textStatus.setBackgroundResource(R.drawable.bg_status_completed);
            holder.textStatus.setTextColor(c.getColor(R.color.cabinet_status_completed_text));
        }
        holder.textCheckInLabel.setText(R.string.cabinet_check_in);
        holder.textCheckIn.setText(item.checkIn);
        holder.textCheckOutLabel.setText(R.string.cabinet_check_out);
        holder.textCheckOut.setText(item.checkOut);
        if (item.guests != null) {
            holder.textGuestsLabel.setVisibility(View.VISIBLE);
            holder.textGuests.setVisibility(View.VISIBLE);
            holder.textGuests.setText(item.guests);
        } else {
            holder.textGuestsLabel.setVisibility(View.GONE);
            holder.textGuests.setVisibility(View.GONE);
        }
        holder.textTotalLabel.setText(R.string.cabinet_total_price);
        holder.textTotal.setText(item.totalPrice);
        if (item.roomType != null) {
            holder.textRoomTypeLabel.setVisibility(View.VISIBLE);
            holder.textRoomType.setVisibility(View.VISIBLE);
            holder.textRoomType.setText(item.roomType);
        } else {
            holder.textRoomTypeLabel.setVisibility(View.GONE);
            holder.textRoomType.setVisibility(View.GONE);
        }
        View layoutConfirm = holder.itemView.findViewById(R.id.layoutButtonsConfirm);
        if (item.showCancelAndDetails) {
            layoutConfirm.setVisibility(View.VISIBLE);
            holder.buttonViewReceipt.setVisibility(View.GONE);
            holder.buttonCancel.setOnClickListener(v -> { if (listener != null) listener.onAction(item, "cancel"); });
            holder.buttonViewDetails.setOnClickListener(v -> { if (listener != null) listener.onAction(item, "view_details"); });
        } else {
            layoutConfirm.setVisibility(View.GONE);
            holder.buttonViewReceipt.setVisibility(View.VISIBLE);
            holder.buttonViewReceipt.setOnClickListener(v -> { if (listener != null) listener.onAction(item, "view_receipt"); });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView imageHotel;
        TextView textHotelName, textRef, textStatus;
        TextView textCheckInLabel, textCheckIn, textCheckOutLabel, textCheckOut;
        TextView textGuestsLabel, textGuests, textTotalLabel, textTotal;
        TextView textRoomTypeLabel, textRoomType;
        Button buttonCancel, buttonViewDetails, buttonViewReceipt;

        VH(@NonNull View itemView) {
            super(itemView);
            imageHotel = itemView.findViewById(R.id.imageBookingHotel);
            textHotelName = itemView.findViewById(R.id.textBookingHotelName);
            textRef = itemView.findViewById(R.id.textBookingRef);
            textStatus = itemView.findViewById(R.id.textBookingStatus);
            textCheckInLabel = itemView.findViewById(R.id.textBookingCheckInLabel);
            textCheckIn = itemView.findViewById(R.id.textBookingCheckIn);
            textCheckOutLabel = itemView.findViewById(R.id.textBookingCheckOutLabel);
            textCheckOut = itemView.findViewById(R.id.textBookingCheckOut);
            textGuestsLabel = itemView.findViewById(R.id.textBookingGuestsLabel);
            textGuests = itemView.findViewById(R.id.textBookingGuests);
            textTotalLabel = itemView.findViewById(R.id.textBookingTotalLabel);
            textTotal = itemView.findViewById(R.id.textBookingTotal);
            textRoomTypeLabel = itemView.findViewById(R.id.textBookingRoomTypeLabel);
            textRoomType = itemView.findViewById(R.id.textBookingRoomType);
            buttonCancel = itemView.findViewById(R.id.buttonBookingCancel);
            buttonViewDetails = itemView.findViewById(R.id.buttonBookingViewDetails);
            buttonViewReceipt = itemView.findViewById(R.id.buttonBookingViewReceipt);
        }
    }
}
