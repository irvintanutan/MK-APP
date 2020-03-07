package com.irvin.makeapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Models.StockIn;
import com.irvin.makeapp.R;
import com.irvin.makeapp.Services.Logger;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author irvin
 */
public class StockInDetailsAdapter extends RecyclerView.Adapter<StockInDetailsAdapter.ViewHolder> {
    private List<StockIn> products;
    private Context mContext = null;
    AlertDialog finalDialog = null;

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
        viewHolder.productPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPrice(products.get(position), position);
            }
        });

        viewHolder.editPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPrice(products.get(position), position);
            }
        });

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
                    Logger.CreateNewEntry(mContext , e , new File(mContext.getExternalFilesDir("") , ModGlobal.logFile));
                }
            }
        });
    }

    public void update(List<StockIn> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    private void editPrice(final StockIn stockIn, final int position) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View alertLayout = inflater.inflate(R.layout.edit_price, null);

        final EditText price = alertLayout.findViewById(R.id.price);
        final Button apply = alertLayout.findViewById(R.id.apply);


        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (price.getText().toString().length() > 0) {
                    DecimalFormat dec = new DecimalFormat("#,##0.00");
                    stockIn.setPrice(dec.format(Double.parseDouble(price.getText().toString())));
                    products.set(position, stockIn);
                    notifyItemChanged(position);
                }
                finalDialog.dismiss();

            }
        });


        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        finalDialog = alert.create();
        finalDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        finalDialog.show();

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView product_code, productName, productPrice, productQuantity;
        LinearLayout container;
        ImageView add, minus, delete, editPrice;;
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
            editPrice = view.findViewById(R.id.editPrice);

            if (ModGlobal.indicator) {
                delete.setVisibility(View.GONE);
                add.setVisibility(View.GONE);
                editPrice.setVisibility(View.GONE);
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