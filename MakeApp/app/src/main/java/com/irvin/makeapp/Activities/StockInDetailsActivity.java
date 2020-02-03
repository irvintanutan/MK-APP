package com.irvin.makeapp.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.gson.Gson;
import com.irvin.makeapp.Adapters.StockInAdapter;
import com.irvin.makeapp.Adapters.StockInDetailsAdapter;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Database.DatabaseStockin;
import com.irvin.makeapp.Models.Products;
import com.irvin.makeapp.Models.StockIn;
import com.irvin.makeapp.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class StockInDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    StockInDetailsAdapter stockInAdapter;
    LinearLayout btnView , layoutBottom;
    boolean indicator = false;
    TextView totalAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stock_in_details);
        Toolbar tb = findViewById(R.id.app_bar);

        indicator = ModGlobal.indicator;


        setSupportActionBar(tb);
        final ActionBar ab = getSupportActionBar();

        if (indicator)
            ab.setTitle("Purchase Order Summary");
        else
            ab.setTitle("Purchase Order Details");
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(true); // disable the default title element here (for centered title)

        init();

        calculateTotal();
    }

    private void init() {
        btnView = findViewById(R.id.btnView);
        totalAmount = findViewById(R.id.totalAmount);
        layoutBottom = findViewById(R.id.layoutBottom);
        LinearLayout stockIn = findViewById(R.id.stockIn);

        if (indicator)
            stockIn.setVisibility(View.GONE);
        else
            layoutBottom.setVisibility(View.GONE);


        recyclerView = findViewById(R.id.product_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager2);

        stockInAdapter = new StockInDetailsAdapter(ModGlobal.stockIns, this);
        recyclerView.setAdapter(stockInAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });


    }

    void goBack() {
        if (indicator) {
            startActivity(new Intent(StockInDetailsActivity.this, StockInMainActivity.class));
            ModGlobal.stockIns.clear();
            finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        } else {
            startActivity(new Intent(StockInDetailsActivity.this, StockInActivity.class));
            finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        }
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            goBack();
        }

        return super.onOptionsItemSelected(item);
    }


    public void stockIn(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setIcon(getResources().getDrawable(R.drawable.confirmation));
        builder.setMessage("Are you sure you want to save transaction ?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                new StockInTask(StockInDetailsActivity.this).execute("");

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


    public class StockInTask extends AsyncTask<String, String, String> {
        boolean warning_indicator = true;
        private DatabaseHelper databaseHelper;
        private DatabaseStockin databaseStockin;
        Context serviceContext;
        ProgressDialog progressDialog;

        public StockInTask(Context serviceContext) {
            this.serviceContext = serviceContext;
            databaseHelper = new DatabaseHelper(serviceContext);
            databaseStockin = new DatabaseStockin(serviceContext);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(serviceContext);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setTitle("Processing");
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();


        }


        @Override
        protected String doInBackground(String... params) {

            for (int a = 0; a < ModGlobal.stockIns.size(); a++) {

                Log.e(ModGlobal.stockIns.get(a).getProductCode(), ModGlobal.stockIns.get(a).getQuantity());

                databaseHelper.stockIn(ModGlobal.stockIns.get(a).getProductCode()
                        , ModGlobal.stockIns.get(a).getQuantity(), true);
            }

            String json = new Gson().toJson(ModGlobal.stockIns);
            databaseStockin.addStockIn(json);


            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {

            progressDialog.dismiss();

            AlertDialog.Builder builder = new AlertDialog.Builder(StockInDetailsActivity.this);
            builder.setTitle("Success");
            builder.setIcon(getResources().getDrawable(R.drawable.check));
            builder.setMessage("Transaction Successful");

            builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    ModGlobal.stockIns.clear();
                    startActivity(new Intent(StockInDetailsActivity.this, StockInMainActivity.class));
                    finish();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();


        }


    }


    void calculateTotal()
    {
        DecimalFormat dec=new DecimalFormat("#,##0.00");
        double total = 0;

        for (StockIn stockIn : ModGlobal.stockIns){

            total += Double.parseDouble(stockIn.getPrice().replace(",","")) * Integer.parseInt(stockIn.getQuantity());

        }


        totalAmount.setText("₱ " + dec.format(total));
    }

}
