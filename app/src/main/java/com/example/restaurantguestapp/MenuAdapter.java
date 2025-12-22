package com.example.restaurantguestapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private List<MenuItemModel> menuItemList; // list of menu items to display
    private Context context; // context used for views and activites
    private boolean isStaff; // decides if staff controls are shown
    private AppDatabaseHelper db; // database helper for items

    // constructor
    public MenuAdapter(Context context, List<MenuItemModel> menuItems, boolean isStaff) {
        this.context = context;
        this.menuItemList = menuItems;
        this.isStaff = isStaff;
        this.db = new AppDatabaseHelper(context);
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // inflate menu card layout
        View view = LayoutInflater.from(context).inflate(R.layout.menu_card, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {

        // get menu item for this position
        MenuItemModel menuItem = menuItemList.get(position);

        // set item details
        holder.name.setText(menuItem.getName());
        holder.description.setText(menuItem.getDescription());
        holder.price.setText("£" + menuItem.getPrice());

        if (isStaff) {
            // shows edit/delete button for staff
            holder.staffButtons.setVisibility(View.VISIBLE);

            // edit menu item
            holder.editButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, AddMenuItemActivity.class);
                intent.putExtra("menu_item_id", menuItem.getId());
                context.startActivity(intent);
            });

            // delete menu item
            holder.deleteButton.setOnClickListener(v -> {
                db.deleteMenuItem(menuItem.getId());
                menuItemList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, menuItemList.size());
            });

        } else {
            // hide staff controls for guests
            holder.staffButtons.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return menuItemList.size();
    }

    // viewholder to store references to menu item views
    static class MenuViewHolder extends RecyclerView.ViewHolder {

        TextView name, description, price; // menu item text
        Button editButton, deleteButton; //staff only buttons
        LinearLayout staffButtons; // layout with staff buttons

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);

            // link ui and java
            name = itemView.findViewById(R.id.menuItemNameText);
            description = itemView.findViewById(R.id.menuItemDescriptionText);
            price = itemView.findViewById(R.id.menuItemPriceText);

            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            staffButtons = itemView.findViewById(R.id.staffActionLayout);
        }
    }
}

