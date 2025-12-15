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

    private List<MenuItemModel> menuItems;
    private Context context;
    private boolean isStaff;
    private AppDatabaseHelper db;

    public MenuAdapter(Context context, List<MenuItemModel> menuItems, boolean isStaff) {
        this.context = context;
        this.menuItems = menuItems;
        this.isStaff = isStaff;
        this.db = new AppDatabaseHelper(context);
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.menu_card, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItemModel item = menuItems.get(position);

        holder.name.setText(item.getName());
        holder.description.setText(item.getDescription());
        holder.price.setText("£" + item.getPrice());

        if (isStaff) {
            holder.staffButtons.setVisibility(View.VISIBLE);

            holder.editButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, AddMenuItemActivity.class);
                intent.putExtra("menu_item_id", item.getId());
                context.startActivity(intent);
            });

            holder.deleteButton.setOnClickListener(v -> {
                db.deleteMenuItem(item.getId());
                menuItems.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, menuItems.size());
            });

        } else {
            holder.staffButtons.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    static class MenuViewHolder extends RecyclerView.ViewHolder {

        TextView name, description, price;
        Button editButton, deleteButton;
        LinearLayout staffButtons;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.text_menu_name);
            description = itemView.findViewById(R.id.text_menu_description);
            price = itemView.findViewById(R.id.text_menu_price);

            editButton = itemView.findViewById(R.id.button_edit_menu);
            deleteButton = itemView.findViewById(R.id.button_delete_menu);
            staffButtons = itemView.findViewById(R.id.layout_staff_buttons);
        }
    }
}

