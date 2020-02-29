package com.irvin.makeapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.irvin.makeapp.Adapters.ViewPagerAdapterStockIn;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.R;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

public class StockInMainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapterStockIn viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_in_main);

        @SuppressLint("WrongViewCast") Toolbar tb = findViewById(R.id.app_bar);
        setSupportActionBar(tb);
        final ActionBar ab = getSupportActionBar();

        ab.setTitle("Inventory Management");
        ab.setDisplayShowHomeEnabled(true);
        // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true);
        // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(true);
        // disable the default title element here (for centered title)

        init();
    }

    private void init() {

        tabLayout = findViewById(R.id.tabs);
        //tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = findViewById(R.id.viewpager);
        //tabLayout.setupWithViewPager(viewPager);


        //
        //Creating Adapter and setting that adapter to the viewPager
        //setSupportActionBar method takes the toolbar and sets it as
        //the default action bar thus making the toolbar work like a normal
        //action bar.
        //
        viewPagerAdapter = new ViewPagerAdapterStockIn(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        /*
        TabLayout.newTab() method creates a tab view, Now a Tab view is not the view
        which is below the tabs, its the tab itself.
         */

        final TabLayout.Tab stockin = tabLayout.newTab();
        final TabLayout.Tab inventory = tabLayout.newTab();

        /*
        Setting Title text for our tabs respectively
         */
        stockin.setText("Stock In");

        inventory.setText("Inventory");
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
        tabLayout.addTab(stockin, 0);
        tabLayout.addTab(inventory, 1);



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


}
