package com.irvin.makeapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.irvin.makeapp.Activities.SalesInvoiceProductDetailsActivity;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Models.StockIn;
import com.irvin.makeapp.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class StockInDetailsAdapter extends RecyclerView.Adapter<StockInDetailsAdapter.ViewHolder> {
    private List<StockIn> products;
    private Context mContext = null;

    public StockInDetailsAdapter(List<StockIn> products, Context mContext) {
        this.products = products;
        this.mContext = mContext;
    }

    @Override
    public StockInDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stock_in_details_card_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StockInDetailsAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.productName.setText(products.get(position).getProductName());
        viewHolder.product_code.setText(products.get(position).getProductCode());
        viewHolder.productPrice.setText("₱ " + products.get(position).getPrice());
        viewHolder.productQuantity.setText(products.get(position).getQuantity());

        viewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StockIn stockIn = products.get(position);
                int qty = Integer.parseInt(stockIn.getQuantity()) + 1;
                stockIn.setQuantity(Integer.toString(qty));
                products.set(position, stockIn);
                notifyDataSetChanged();
            }
        });

        viewHolder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StockIn stockIn = products.get(position);
                int qty = Integer.parseInt(stockIn.getQuantity()) - 1;
                if (qty > 0) {
                    stockIn.setQuantity(Integer.toString(qty));
                }
                products.set(position, stockIn);
                notifyDataSetChanged();

            }
        });

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ModGlobal.insertProduct(products.get(position).getProductCode());
                    products.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void update(List<StockIn> products) {
        this.products = products;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView product_code, productName, productPrice, productQuantity;
        LinearLayout container;
        ImageView add, minus, delete;
        LinearLayout rightSideContainer, leftSideContainer;


        public ViewHolder(View view) {
            super(view);

            rightSideContainer = view.findViewById(R.id.rightSideContainer);
            leftSideContainer = view.findViewById(R.id.leftSideContainer);
            container = view.findViewById(R.id.container);
            product_code = view.findViewById(R.id.product_code);
            productName = view.findViewById(R.id.productName);
            productPrice = view.findViewById(R.id.productPrice);
            productQuantity = view.findViewById(R.id.productQuantity);
            add = view.findViewById(R.id.plus);
            minus = view.findViewById(R.id.minus);
            delete = view.findViewById(R.id.delete);

            if (ModGlobal.indicator) {
                delete.setVisibility(View.GONE);
                add.setVisibility(View.GONE);
                minus.setVisibility(View.GONE);
                rightSideContainer.setVisibility(View.GONE);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        1.0f);
                leftSideContainer.setLayoutParams(params);
            }

        }
    }


}