package com.irvin.makeapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.irvin.makeapp.Models.StockIn;
import com.irvin.makeapp.Models.StockInList;
import com.irvin.makeapp.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class StockInMainAdapter extends RecyclerView.Adapter<StockInMainAdapter.ViewHolder> {
    private List<StockInList> products;
    private Context mContext = null;

    public StockInMainAdapter(List<StockInList> products, Context mContext) {
        this.products = products;
        this.mContext = mContext;
    }

    @Override
    public StockInMainAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stock_in_card_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StockInMainAdapter.ViewHolder viewHolder, final int position) {

        try {
            viewHolder.stockInId.setText("#"+products.get(position).getId());
            viewHolder.dateCreated.setText(products.get(position).getDateCreated());


            JSONArray json = new JSONArray(products.get(position).getDetails());



            viewHolder.numberOfItems.setText(Integer.toString(json.length()));

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void update(List<StockInList> products) {
        this.products = products;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView stockInId, dateCreated, numberOfItems;


        public ViewHolder(View view) {
            super(view);

            stockInId = view.findViewById(R.id.stockInId);
            dateCreated = view.findViewById(R.id.dateCreated);
            numberOfItems = view.findViewById(R.id.numberOfItems);
        }
    }


}