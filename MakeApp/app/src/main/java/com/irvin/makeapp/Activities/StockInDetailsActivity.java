package com.irvin.makeapp.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.irvin.makeapp.Adapters.StockInAdapter;
import com.irvin.makeapp.Adapters.StockInDetailsAdapter;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Models.StockIn;
import com.irvin.makeapp.R;

import java.text.DecimalFormat;
import java.util.HashMap;

public class StockInDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    StockInDetailsAdapter stockInAdapter;

    TextView totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stock_in_details);
        Toolbar tb = findViewById(R.id.app_bar);
        setSupportActionBar(tb);
        final ActionBar ab = getSupportActionBar();

        ab.setTitle("Stock In Details");
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(true); // disable the default title element here (for centered title)

        init();


    }

    private void init(){


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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(StockInDetailsActivity.this, StockInActivity.class));
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(StockInDetailsActivity.this, StockInActivity.class));
            finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        }

        return super.onOptionsItemSelected(item);
    }


    public void stockIn(View view) {
    }
}
