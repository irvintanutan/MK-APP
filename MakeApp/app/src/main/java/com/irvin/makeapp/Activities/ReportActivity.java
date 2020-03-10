package com.irvin.makeapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.irvin.makeapp.Adapters.ReportsFragment.TabFragmentReportCustomer;
import com.irvin.makeapp.Adapters.ReportsFragment.TabFragmentReportInventory;
import com.irvin.makeapp.Adapters.ReportsFragment.TabFragmentReportSales;
import com.irvin.makeapp.Models.MenuForm;
import com.irvin.makeapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author irvin
 */
public class ReportActivity extends AppCompatActivity {

    private List<MenuForm> form;
    BottomNavigationView bottomNavigation;

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


        bottomNavigation = findViewById(R.id.navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);


        form = new ArrayList<>();

        form.add(new MenuForm("Sales", R.drawable.invoice, "Sales Report"));
        form.add(new MenuForm("Customer", R.drawable.account, "Customer Report"));
        form.add(new MenuForm("Inventory", R.drawable.box, "Manage Inventory"));


        sales();

    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_sales:
                            sales();
                            return true;
                        case R.id.action_customer:
                            customer();
                            return true;
                        case R.id.action_inventory:
                            inventory();
                            return true;
                    }
                    return false;
                }
            };


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
