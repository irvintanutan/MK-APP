package com.irvin.makeapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.irvin.makeapp.Activities.SalesInvoiceProductDetailsActivity;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Models.StockIn;
import com.irvin.makeapp.R;

import java.text.DecimalFormat;
import java.util.List;

public class SalesInvoiceDetailsAdapter extends RecyclerView.Adapter<SalesInvoiceDetailsAdapter.ViewHolder> {
    private List<StockIn> products;
    private Context mContext = null;
    DatabaseHelper databaseHelper;
    AlertDialog finalDialog = null;

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
        viewHolder.productPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPrice(products.get(position), position);
            }
        });
        viewHolder.productQuantity.setText(products.get(position).getQuantity());

        viewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StockIn stockIn = products.get(position);
                int qty = Integer.parseInt(stockIn.getQuantity()) + 1;

                if (qty <= Integer.parseInt(databaseHelper.getAllProducts(products.get(position).getProductCode()).get(0).getProduct_quantity())) {
                    stockIn.setQuantity(Integer.toString(qty));
                    products.set(position, stockIn);
                    notifyDataSetChanged();
                    SalesInvoiceProductDetailsActivity.calculateTotal();
                } else {
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
                try {
                    ModGlobal.insertProduct(products.get(position).getProductCode());
                    products.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());
                    SalesInvoiceProductDetailsActivity.calculateTotal();
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
                    SalesInvoiceProductDetailsActivity.calculateTotal();
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