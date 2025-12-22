package com.example.restaurantguestapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context context; // context used for views and activites
    private List<NotificationModel> notificationList; // list of notifications to display

    // constructor
    public NotificationAdapter(Context context, List<NotificationModel> notifications) {
        this.context = context;
        this.notificationList = notifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // inflate notification item layout
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // get notification at this position
        NotificationModel notification = notificationList.get(position);
        holder.messageText.setText(notification.getMessage());
    }

    @Override
    public int getItemCount() {
        return notificationList == null ? 0 : notificationList.size();
    }

    // updates notification list and refreshes recyclerview
    public void setNotifications(List<NotificationModel> notifications) {
        this.notificationList = notifications;
        notifyDataSetChanged();
    }

    // viewholder to store references to notification item views
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.notificationMessageText);
        }
    }
}

