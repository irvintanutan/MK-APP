package com.irvin.makeapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Models.Products;
import com.irvin.makeapp.R;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class StockInAdapter extends RecyclerView.Adapter<StockInAdapter.ViewHolder> {
    private List<Products> products;
    private Context mContext = null;

    public StockInAdapter(List<Products> products, Context mContext) {
        this.products = products;
        this.mContext = mContext;
    }

    @Override
    public StockInAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_card_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StockInAdapter.ViewHolder viewHolder, int position) {

            viewHolder.productName.setText(products.get(position).getProduct_name());
            viewHolder.product_code.setText(products.get(position).getProduct_id());
            viewHolder.productPrice.setText("₱ " + products.get(position).getProduct_price());

    }

    public void update(List<Products> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        products.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView product_code, productName, productPrice;
        CardView cv;
        LinearLayout container;


        public ViewHolder(View view) {
            super(view);
            container = view.findViewById(R.id.container);
            product_code = view.findViewById(R.id.product_code);
            productName = view.findViewById(R.id.productName);
            productPrice = view.findViewById(R.id.productPrice);
            cv = view.findViewById(R.id.cv);


        }
    }


}