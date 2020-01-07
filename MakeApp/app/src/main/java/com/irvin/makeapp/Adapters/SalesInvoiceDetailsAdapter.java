package com.irvin.makeapp.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.irvin.makeapp.Activities.SalesInvoiceProductDetailsActivity;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Models.StockIn;
import com.irvin.makeapp.R;

import java.util.List;

public class SalesInvoiceDetailsAdapter extends RecyclerView.Adapter<SalesInvoiceDetailsAdapter.ViewHolder> {
    private List<StockIn> products;
    private Context mContext = null;
    DatabaseHelper databaseHelper;

    public SalesInvoiceDetailsAdapter(List<StockIn> products, Context mContext) {
        this.products = products;
        this.mContext = mContext;
        databaseHelper = new DatabaseHelper(mContext);
    }

    @Override
    public SalesInvoiceDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stock_in_details_card_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SalesInvoiceDetailsAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.productName.setText(products.get(position).getProductName());
        viewHolder.product_code.setText(products.get(position).getProductCode());
        viewHolder.productPrice.setText("₱ " + products.get(position).getPrice());
        viewHolder.productQuantity.setText(products.get(position).getQuantity());

        viewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StockIn stockIn = products.get(position);
                int qty = Integer.parseInt(stockIn.getQuantity()) + 1;

                if (qty <= Integer.parseInt(databaseHelper.getAllProducts(products.get(position).getProductCode()).get(0).getProduct_quantity()))
                {
                    stockIn.setQuantity(Integer.toString(qty));
                    products.set(position, stockIn);
                    notifyDataSetChanged();
                    SalesInvoiceProductDetailsActivity.calculateTotal();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Warning");
                    builder.setIcon(mContext.getResources().getDrawable(R.drawable.warning));
                    builder.setMessage("Quantity Limit already reached");

                    builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
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
                SalesInvoiceProductDetailsActivity.calculateTotal();

            }
        });

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    products.remove(position);
                    notifyDataSetChanged();
                    SalesInvoiceProductDetailsActivity.calculateTotal();

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