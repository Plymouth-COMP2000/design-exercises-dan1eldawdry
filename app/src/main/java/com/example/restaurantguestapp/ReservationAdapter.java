package com.example.restaurantguestapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder> {

    private ArrayList<ReservationModel> reservationList; // list of reservations to display
    private Context context; // context used for views and activites
    private AppDatabaseHelper db; // database helper for items

    // constructor
    public ReservationAdapter(Context context, ArrayList<ReservationModel> reservations) {
        this.context = context;
        this.reservationList = reservations;
        db = new AppDatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // inflate reservation card layout
        View view = LayoutInflater.from(context).inflate(R.layout.reservation_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // get reservation for this position
        ReservationModel reservation = reservationList.get(position);

        // display reservation details
        holder.usernameText.setText("Guest: " + reservation.getUsername());
        holder.dateText.setText(reservation.getDate());
        holder.timeText.setText("Time: " + reservation.getTime());
        holder.groupSizeText.setText("Group Size: " + reservation.getGroupSize());
        holder.requestsText.setText("Requests: " + reservation.getSpecialRequests());

        // edit button
        holder.editButton.setOnClickListener(v -> {

            // Open reservation screen in edit mode
            Intent intent = new Intent(context, ReservationActivity.class);
            intent.putExtra("reservation_id", reservation.getId()); // tells activity to load and edit
            intent.putExtra("username", reservation.getUsername()); // edit according to username
            context.startActivity(intent);

            db.addGuestNotification(reservation.getUsername(), "Your reservation for the " + reservation.getDate() + " at " + reservation.getTime() + " was updated");

            // show android notification
            NotificationHelper helper = new NotificationHelper(context);
            helper.send("Reservation Updated", "Your reservation for the " + reservation.getDate() + " at " + reservation.getTime() + " was updated");
        });

        // cancel button
        holder.cancelButton.setOnClickListener(v -> {
            db.deleteReservation(reservation.getId()); // delete from the database
            db.addGuestNotification(reservation.getUsername(), "Your reservation for the " + reservation.getDate() + " at " + reservation.getTime() + " was cancelled");

            // show android notification
            NotificationHelper helper = new NotificationHelper(context);
            helper.send("Reservation Cancelled", "Your reservation for the " + reservation.getDate() + " at " + reservation.getTime() + " was cancelled");

            // remove from list and update the recyclerview
            reservationList.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    // viewholder to store references to reservation views
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView usernameText, dateText, timeText, groupSizeText, requestsText;
        Button editButton, cancelButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // link ui and java
            usernameText = itemView.findViewById(R.id.guestNameText);
            dateText = itemView.findViewById(R.id.reservationDateText);
            timeText = itemView.findViewById(R.id.reservationTimeText);
            groupSizeText = itemView.findViewById(R.id.groupSizeText);
            requestsText = itemView.findViewById(R.id.specialRequestsText);

            editButton = itemView.findViewById(R.id.editReservationButton);
            cancelButton = itemView.findViewById(R.id.cancelReservationButton);
        }
    }
}

