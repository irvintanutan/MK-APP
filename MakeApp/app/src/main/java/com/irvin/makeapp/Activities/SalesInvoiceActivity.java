package com.irvin.makeapp.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.irvin.makeapp.Adapters.CustomerAdapter;
import com.irvin.makeapp.Adapters.SalesInvoiceAdapter;
import com.irvin.makeapp.Adapters.SearchCustomerAdapter;
import com.irvin.makeapp.Adapters.StockInMainAdapter;
import com.irvin.makeapp.Constant.ClickListener;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Constant.RecyclerTouchListener;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Models.CustomerModel;
import com.irvin.makeapp.Models.Invoice;
import com.irvin.makeapp.Models.StockIn;
import com.irvin.makeapp.Models.StockInList;
import com.irvin.makeapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SalesInvoiceActivity extends AppCompatActivity {


    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    List<Invoice> invoices;
    LinearLayout nothing;
    RecyclerView recyclerView;
    SalesInvoiceAdapter salesInvoiceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_invoice);

        Toolbar tb = findViewById(R.id.app_bar);
        setSupportActionBar(tb);
        final ActionBar ab = getSupportActionBar();

        ab.setTitle("Invoices");
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(true); // disable the default title element here (for centered title)

        init();


    }

    void init() {
        nothing = findViewById(R.id.nothing);
        invoices = new ArrayList<>();
        invoices = databaseHelper.getAllInvoices();

        if (invoices.size() > 0) {
            nothing.setVisibility(View.GONE);
        }

        recyclerView = findViewById(R.id.invoice_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        salesInvoiceAdapter = new SalesInvoiceAdapter(invoices, this);
        recyclerView.setAdapter(salesInvoiceAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {

            @Override
            public void onClick(View view, int position) {
                /*try {
                    StockInList stockInList = databaseHelper.getAllStockIn(stockInListList.get(position).getId());


                    JSONArray jsonArray = new JSONArray(stockInList.getDetails());
                    ArrayList<StockIn> stockIns = new ArrayList<>();
                    for (int a = 0 ; a < jsonArray.length() ; a++){

                        JSONObject object = jsonArray.getJSONObject(a);
                        StockIn stockIn = new StockIn(object.getString("productName")
                                ,object.getString("productCode") , object.getString("quantity")
                                , object.getString("price"));

                        stockIns.add(stockIn);
                    }

                    ModGlobal.stockIns = stockIns;

                    Intent intent = new Intent(StockInMainActivity.this, StockInDetailsActivity.class);
                    ModGlobal.indicator = true;
                    startActivity(intent);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
*/
            }

            @Override
            public void onLongClick(View view, int position) {

            }


        }));

    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(SalesInvoiceActivity.this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(SalesInvoiceActivity.this, MainActivity.class));
            finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        }

        return super.onOptionsItemSelected(item);
    }

    public void stockIn(View view) {
        Intent intent = new Intent(SalesInvoiceActivity.this, SalesInvoiceProductActivity.class);
        startActivity(intent);
        finish();

    }
}
