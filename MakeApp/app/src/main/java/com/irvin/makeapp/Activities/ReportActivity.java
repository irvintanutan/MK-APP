package com.irvin.makeapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.irvin.makeapp.Adapters.DataAdapter;
import com.irvin.makeapp.Adapters.ReportsFragment.TabFragmentReportCustomer;
import com.irvin.makeapp.Adapters.ReportsFragment.TabFragmentReportInventory;
import com.irvin.makeapp.Adapters.ReportsFragment.TabFragmentReportSales;
import com.irvin.makeapp.Database.DatabaseCustomer;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Database.DatabaseInvoice;
import com.irvin.makeapp.Models.MenuForm;
import com.irvin.makeapp.Models.TopTenProductModel;
import com.irvin.makeapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author irvin
 */
public class ReportActivity extends AppCompatActivity {

    DecimalFormat dec = new DecimalFormat("#,##0.00");
    DatabaseCustomer databaseCustomer = new DatabaseCustomer(this);
    DatabaseInvoice databaseInvoice = new DatabaseInvoice(this);
    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    List<TopTenProductModel> topTenProductModels = new ArrayList<>();
    List<String> skuList = new ArrayList<>();
    private List<MenuForm> form;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Window window = getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(ContextCompat.getColor(this, R.color.end_color));

        init();

    }

    void init() {

        RecyclerView recyclerView = findViewById(R.id.user_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        form = new ArrayList<>();

        form.add(new MenuForm("Sales", R.drawable.invoice, "Sales Report"));
        form.add(new MenuForm("Customer", R.drawable.account, "Customer Report"));
        form.add(new MenuForm("Inventory", R.drawable.box, "Manage Inventory"));


        RecyclerView.Adapter adapter = new DataAdapter(form, this);
        recyclerView.setAdapter(adapter);

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

                    switch (position) {

                        case 0:
                            sales();
                            break;
                        case 1:
                            customer();
                            break;
                        case 2:
                            inventory();
                            break;

                        default:
                    }

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

        sales();

    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(ReportActivity.this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(ReportActivity.this, MainActivity.class));
            finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        }

        return super.onOptionsItemSelected(item);
    }


    public void sales() {
        //Toast.makeText(getApplicationContext()  , "SALES" , Toast.LENGTH_LONG).show();
        TabFragmentReportSales tabFragmentReportSales = new TabFragmentReportSales();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frameLayout, tabFragmentReportSales);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

    public void inventory() {

        //Toast.makeText(getApplicationContext()  , "INVENTORY" , Toast.LENGTH_LONG).show();

        TabFragmentReportInventory tabFragmentReportInventory = new TabFragmentReportInventory();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frameLayout, tabFragmentReportInventory);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

    public void customer() {

        // Toast.makeText(getApplicationContext()  , "CUSTOMER" , Toast.LENGTH_LONG).show();
        TabFragmentReportCustomer tabFragmentReportCustomer = new TabFragmentReportCustomer();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frameLayout, tabFragmentReportCustomer);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }


}
