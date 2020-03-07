package com.irvin.makeapp.Services;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Models.Products;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;

/**
 * @author irvin
 */
public class GetProductTask extends AsyncTask<String, String, String> {
    boolean warning_indicator = true;
    ArrayList<Products> productsList = new ArrayList<>();
    private DatabaseHelper databaseHelper;
    Context serviceContext;
    ProgressDialog progressDialog;

    public GetProductTask(Context serviceContext) {
        this.serviceContext = serviceContext;
        databaseHelper = new DatabaseHelper(serviceContext);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialog(serviceContext);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Product Sync Process");
        progressDialog.setMessage("Downloading Products");
        progressDialog.setCancelable(false);
        progressDialog.show();


    }


    protected void onProgressUpdate(String... progUpdate) {

        int max = Integer.parseInt(progUpdate[0]), prog = Integer.parseInt(progUpdate[1]);

        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgress((int) (prog * 100 / max));


        progressDialog.setMessage("Downloading Products " + ((int) (prog * 100 / max) + "%"));

    }



    @Override
    protected String doInBackground(String... params) {

        try {
            final String isUpdate = params[0];
            warning_indicator = true;

            FirebaseApp.initializeApp(serviceContext);
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("products");
            Query query = ref.orderByChild("products");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ModGlobal.dataSnapshot = dataSnapshot;
                    for (DataSnapshot postSnapshot : ModGlobal.dataSnapshot.getChildren()) {

                        Products products = postSnapshot.getValue(Products.class);
                        productsList.add(products);
                    }

                    for (int a = 0; a < productsList.size(); a++) {
                        if (isUpdate.equals("0")) {
                            databaseHelper.addProduct(productsList.get(a));
                        }else {
                            databaseHelper.updateProduct(productsList.get(a));
                        }
                        publishProgress(String.format("%d", productsList.size())
                                , String.format("%d", a));
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.e("aaa", "loadPost:onCancelled", databaseError.toException());
                    // ...
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("asd", e.toString());
            warning_indicator = false;
        }


        return null;
    }

    @Override
    protected void onPostExecute(String strFromDoInBg) {

        String message = warning_indicator ? "Synchronization Successful" : "Can't Access Server!";
        progressDialog.dismiss();
        AlertDialog.Builder alert = new AlertDialog.Builder(serviceContext);
        alert.setTitle("Products");
        alert.setMessage(message);

        alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

            }
        });

        alert.show();
    }


}
