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
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.irvin.makeapp.Adapters.StockInDetailsAdapter;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Models.StockIn;
import com.irvin.makeapp.R;

import java.text.DecimalFormat;

public class SalesInvoiceProductDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    StockInDetailsAdapter stockInAdapter;
    LinearLayout btnView, layoutBottom;
    boolean indicator = false;
    TextView totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_invoice_product_details);
        Toolbar tb = findViewById(R.id.app_bar);

        indicator = ModGlobal.indicator;


        setSupportActionBar(tb);
        final ActionBar ab = getSupportActionBar();

        if (indicator)
            ab.setTitle("Sales Invoice Summary");
        else
            ab.setTitle("Sales Invoice Details");
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
            startActivity(new Intent(SalesInvoiceProductDetailsActivity.this, SalesInvoiceActivity.class));
            ModGlobal.stockIns.clear();
            finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        } else {
            startActivity(new Intent(SalesInvoiceProductDetailsActivity.this, SalesInvoiceProductActivity.class));
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



    }


    public class StockInTask extends AsyncTask<String, String, String> {
        boolean warning_indicator = true;
        private DatabaseHelper databaseHelper;
        Context serviceContext;
        ProgressDialog progressDialog;

        public StockInTask(Context serviceContext) {
            this.serviceContext = serviceContext;
            databaseHelper = new DatabaseHelper(serviceContext);
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
                        , ModGlobal.stockIns.get(a).getQuantity());
            }

            String json = new Gson().toJson(ModGlobal.stockIns);
            databaseHelper.addStockIn(json);


            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {

            progressDialog.dismiss();

            AlertDialog.Builder builder = new AlertDialog.Builder(SalesInvoiceProductDetailsActivity.this);
            builder.setTitle("Success");
            builder.setIcon(getResources().getDrawable(R.drawable.check));
            builder.setMessage("Transaction Successful");

            builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    ModGlobal.stockIns.clear();
                    startActivity(new Intent(SalesInvoiceProductDetailsActivity.this, SalesInvoiceProductActivity.class));
                    finish();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();


        }


    }


    void calculateTotal() {
        DecimalFormat dec = new DecimalFormat("#,##0.00");
        double total = 0;

        for (StockIn stockIn : ModGlobal.stockIns) {

            total += Double.parseDouble(stockIn.getPrice().replace(",", "")) * Integer.parseInt(stockIn.getQuantity());

        }


        totalAmount.setText("₱ " + dec.format(total));
    }
}