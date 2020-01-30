package com.irvin.makeapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.irvin.makeapp.Adapters.ViewPagerAdapter;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Models.Invoice;
import com.irvin.makeapp.R;

import java.util.ArrayList;
import java.util.List;

public class SalesInvoiceActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;


    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    List<Invoice> invoices;
    LinearLayout nothing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_invoice);

        @SuppressLint("WrongViewCast") Toolbar tb = findViewById(R.id.app_bar);
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
        tabLayout = findViewById(R.id.tabs);
        //tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = findViewById(R.id.viewpager);
        //tabLayout.setupWithViewPager(viewPager);


        /*
        Creating Adapter and setting that adapter to the viewPager
        setSupportActionBar method takes the toolbar and sets it as
        the default action bar thus making the toolbar work like a normal
        action bar.
         */
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        /*
        TabLayout.newTab() method creates a tab view, Now a Tab view is not the view
        which is below the tabs, its the tab itself.
         */

        final TabLayout.Tab pending = tabLayout.newTab();
        final TabLayout.Tab verified = tabLayout.newTab();

        /*
        Setting Title text for our tabs respectively
         */
        verified.setText("History");
        verified.setIcon(R.drawable.paid);

        pending.setText("Outstanding");
        pending.setIcon(R.drawable.pending);
/*
        //set custom view
        pending.setCustomView(R.layout.notification_badge);

        TextView textView = pending.getCustomView().findViewById(R.id.text);
        textView.setText("5");
        TextView textView2 = pending.getCustomView().findViewById(R.id.textTab);
        textView2.setText("Outstanding");*/



        /*
        Adding the tab view to our tablayout at appropriate positions
        As I want home at first position I am passing home and 0 as argument to
        the tablayout and like wise for other tabs as well
         */
        tabLayout.addTab(pending, 0);
        tabLayout.addTab(verified, 1);



        /*
        TabTextColor sets the color for the title of the tabs, passing a ColorStateList here makes
        tab change colors in different situations such as selected, active, inactive etc

        TabIndicatorColor sets the color for the indiactor below the tabs
         */

        tabLayout.setTabTextColors(ContextCompat.getColorStateList(this, R.color.white));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorAccent));


        /*
        Adding a onPageChangeListener to the viewPager
        1st we add the PageChangeListener and pass a TabLayoutPageChangeListener so that Tabs Selection
        changes when a viewpager page changes.
         */

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(SalesInvoiceActivity.this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.invoice_search, menu);

        return true;
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(SalesInvoiceActivity.this, MainActivity.class));
            finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        } else if (item.getItemId() == R.id.action_search){

        }

        return super.onOptionsItemSelected(item);
    }

    public void stockIn(View view) {
        Intent intent = new Intent(SalesInvoiceActivity.this, SalesInvoiceProductActivity.class);
        startActivity(intent);
        finish();

        ModGlobal.ProductModelListCopy.clear();
        ModGlobal.stockIns.clear();
        ModGlobal.ProductModelList.clear();
        ModGlobal.customerName = "";

    }


}
