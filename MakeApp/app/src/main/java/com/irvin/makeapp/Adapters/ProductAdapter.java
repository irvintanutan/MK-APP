package com.irvin.makeapp.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.irvin.makeapp.Activities.SalesInvoiceProductDetailsActivity;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Models.Products;
import com.irvin.makeapp.Models.StockIn;
import com.irvin.makeapp.R;

import java.text.DecimalFormat;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Products> products;
    AlertDialog finalDialog = null;
    private Context mContext = null;
    private DatabaseHelper databaseHelper;

    public ProductAdapter(List<Products> products, Context mContext) {
        this.products = products;
        this.mContext = mContext;
        this.databaseHelper = new DatabaseHelper(mContext);
    }

    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_product, viewGroup, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(ProductAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.fullName.setText(products.get(position).getProduct_name());
        viewHolder.age.setText("ID: " + products.get(position).getProduct_id());
        if (products.get(position).getProduct_quantity() != null) {
            viewHolder.skinType.setText("QTY: " + products.get(position).getProduct_quantity());

            if (Integer.parseInt(products.get(position).getProduct_quantity()) < 0) {
                viewHolder.skinType.setTextColor(Color.RED);
            } else {
                viewHolder.skinType.setTextColor(Color.BLACK);
            }
        } else {
            viewHolder.skinType.setText("QTY: 0");
        }

        viewHolder.contactNumber.setText("₱ " + products.get(position).getProduct_price());


        viewHolder.editQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editQuantity(products.get(position), position);
            }
        });
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
        ImageView editQuantity;


        public ViewHolder(View view) {
            super(view);

            editQuantity = view.findViewById(R.id.editQuantity);
            fullName = view.findViewById(R.id.fullName);
            age = view.findViewById(R.id.age);
            contactNumber = view.findViewById(R.id.contactNumber);
            skinType = view.findViewById(R.id.skinType);


        }
    }

    private void editQuantity(final Products prod, final int position) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View alertLayout = inflater.inflate(R.layout.edit_price, null);

        final EditText price = alertLayout.findViewById(R.id.price);
        final Button apply = alertLayout.findViewById(R.id.apply);

        price.setHint("Enter Adjusted Quantity");

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (price.getText().toString().isEmpty() || price.getText().toString() == null ||
                        price.getText().toString().length() == 0) {

                 Toast.makeText(mContext, "Invalid Input for quantity", Toast.LENGTH_SHORT).show();

                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                    builder.setTitle("Confirm");
                    builder.setIcon(mContext.getResources().getDrawable(R.drawable.confirmation));
                    builder.setMessage("Are you sure you want adjust QUANTITY of product\n------------------------------------------------------------\n"
                            + prod.getProduct_name() + "" +
                            "\n------------------------------------------------------------\n" +
                            "QTY("  +
                              price.getText().toString() + ")");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            prod.setProduct_quantity(price.getText().toString());
                            products.set(position, prod);
                            notifyItemChanged(position);
                            databaseHelper.updateProductQuantity(prod);

                        }

                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();


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


}