package com.irvin.makeapp.Adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.irvin.makeapp.Database.DatabaseInvoice;
import com.irvin.makeapp.Models.MenuForm;
import com.irvin.makeapp.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private List<MenuForm> form;
    private Context context;
    private DatabaseInvoice databaseInvoice;

    public DataAdapter(List<MenuForm> form , Context context) {
        this.form = form;
        this.context = context;
        databaseInvoice = new DatabaseInvoice(context);
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, int i) {
        int size = databaseInvoice.getAllDueInvoices().size();

        Log.e("jhkasdhkjasdhk sak" , Integer.toString(size));

        if (form.get(i).getMenuName().equals("Sales Invoice")){
            if (size > 0) {
                viewHolder.badge.setVisibility(View.VISIBLE);
                viewHolder.badge.setText(Integer.toString(size));
            }
        }

        viewHolder.menu_icon.setImageResource(form.get(i).getPhotoid());
        viewHolder.menu_name.setText(form.get(i).getMenuName());
    }

    @Override
    public int getItemCount() {
        return form.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView menu_name , badge;
        private ImageView menu_icon;

        public ViewHolder(View view) {
            super(view);

            badge = view.findViewById(R.id.badgeNotification);
            menu_name = view.findViewById(R.id.menuText);
            menu_icon = view.findViewById(R.id.image);
        }
    }

}