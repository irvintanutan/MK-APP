package com.irvin.makeapp.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.irvin.makeapp.Adapters.DataAdapter;
import com.irvin.makeapp.Adapters.StockInMainAdapter;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Models.StockInList;
import com.irvin.makeapp.R;

import java.util.ArrayList;
import java.util.List;

public class StockInMainActivity extends AppCompatActivity {


    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    List<StockInList> stockInListList;
    LinearLayout nothing;
    RecyclerView recyclerView;
    StockInMainAdapter stockInMainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_in_main);

        Toolbar tb = findViewById(R.id.app_bar);
        setSupportActionBar(tb);
        final ActionBar ab = getSupportActionBar();

        ab.setTitle("Stock In");
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(true); // disable the default title element here (for centered title)

        init();
    }

    private void init() {
        nothing = findViewById(R.id.nothing);
        stockInListList = new ArrayList<>();
        stockInListList = databaseHelper.getAllStockIn();

        if (stockInListList.size() > 0) {
            nothing.setVisibility(View.GONE);
        }

        stockInListList = databaseHelper.getAllStockIn();

        recyclerView = findViewById(R.id.stock_in_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        stockInMainAdapter = new StockInMainAdapter(stockInListList , this);
        recyclerView.setAdapter(stockInMainAdapter);

    }


    @Override
    public void onBackPressed() {

        startActivity(new Intent(StockInMainActivity.this, MainActivity.class));
        finish();
        ModGlobal.stockIns.clear();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(StockInMainActivity.this, MainActivity.class));
            finish();
            ModGlobal.stockIns.clear();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        }

        return super.onOptionsItemSelected(item);
    }

    public void stockIn(View view) {
        Intent intent = new Intent(StockInMainActivity.this, StockInActivity.class);
        startActivity(intent);
        finish();

    }
}
