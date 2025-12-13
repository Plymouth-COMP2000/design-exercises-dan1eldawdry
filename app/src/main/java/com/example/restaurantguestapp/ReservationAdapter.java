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

    private Context context;
    private ArrayList<ReservationModel> list;
    private AppDatabaseHelper db;

    public ReservationAdapter(Context context, ArrayList<ReservationModel> list) {
        this.context = context;
        this.list = list;
        db = new AppDatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.reservation_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ReservationModel r = list.get(position);

        holder.textDate.setText(r.getDate());
        holder.textTime.setText("Time: " + r.getTime());
        holder.textGroupSize.setText("Group Size: " + r.getGroupSize());
        holder.textSpecialRequests.setText("Requests: " + r.getSpecialRequests());
        holder.textStatus.setText("Status: " + r.getStatus());

        // edit button
        holder.buttonEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReservationActivity.class);
            intent.putExtra("reservation_id", r.getId());   // tells activity to load and edit
            context.startActivity(intent);
        });

        // cancel button
        holder.buttonCancel.setOnClickListener(v -> {
            db.deleteReservation(r.getId());
            list.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textDate, textTime, textGroupSize, textSpecialRequests, textStatus;
        Button buttonEdit, buttonCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textDate = itemView.findViewById(R.id.text_date);
            textTime = itemView.findViewById(R.id.text_time);
            textGroupSize = itemView.findViewById(R.id.text_group_size);
            textSpecialRequests = itemView.findViewById(R.id.text_special_requests);
            textStatus = itemView.findViewById(R.id.text_status);

            buttonEdit = itemView.findViewById(R.id.button_edit);
            buttonCancel = itemView.findViewById(R.id.button_cancel);
        }
    }
}

