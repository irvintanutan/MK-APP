package com.irvin.makeapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.irvin.makeapp.Models.Products;
import com.irvin.makeapp.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Products> products;
    private Context mContext = null;

    public ProductAdapter(List<Products> products, Context mContext) {
        this.products = products;
        this.mContext = mContext;
    }

    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_product, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductAdapter.ViewHolder viewHolder, int position) {

        viewHolder.fullName.setText(products.get(position).getProduct_name());
        viewHolder.age.setText("ID: " + products.get(position).getProduct_id());
        if (products.get(position).getProduct_quantity() != null)
            viewHolder.skinType.setText("QTY: " + products.get(position).getProduct_quantity());
        else
            viewHolder.skinType.setText("QTY: 0");
            viewHolder.contactNumber.setText("₱ " + products.get(position).getProduct_price());
    }

    public void update(List<Products> products) {
        this.products = products;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView fullName, age, contactNumber, skinType;


        public ViewHolder(View view) {
            super(view);

            fullName = view.findViewById(R.id.fullName);
            age = view.findViewById(R.id.age);
            contactNumber = view.findViewById(R.id.contactNumber);
            skinType = view.findViewById(R.id.skinType);


        }
    }


}