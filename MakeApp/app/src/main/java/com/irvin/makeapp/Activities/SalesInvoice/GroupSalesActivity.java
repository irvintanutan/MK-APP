package com.irvin.makeapp.Activities.SalesInvoice;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;
import com.irvin.makeapp.Activities.MainActivity;
import com.irvin.makeapp.Adapters.GroupSalesFragment.ViewPagerAdapterGroupSales;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Database.DatabaseInvoice;
import com.irvin.makeapp.Models.Invoice;
import com.irvin.makeapp.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

/**
 * @author irvin
 */
public class GroupSalesActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapterGroupSales viewPagerAdapter;

    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    DatabaseInvoice databaseInvoice = new DatabaseInvoice(this);
    List<Invoice> invoices;
    LinearLayout nothing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_sales);

        Toolbar tb = findViewById(R.id.app_bar);
        setSupportActionBar(tb);
        final ActionBar ab = getSupportActionBar();

        ab.setTitle("Group Sales");
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
        nothing = findViewById(R.id.nothing);
        invoices = new ArrayList<>();
        invoices = databaseInvoice.getAllInvoices();

        if (invoices.size() > 0) {
            nothing.setVisibility(View.GONE);
        }
        tabLayout = findViewById(R.id.tabs);
        //tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = findViewById(R.id.viewpager);
        //tabLayout.setupWithViewPager(viewPager);


        viewPagerAdapter = new ViewPagerAdapterGroupSales(getSupportFragmentManager());
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

        tabLayout.addTab(pending, 0);
        tabLayout.addTab(verified, 1);



        tabLayout.setTabTextColors(ContextCompat.getColorStateList(this, R.color.white));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorAccent));

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
        startActivity(new Intent(GroupSalesActivity.this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(GroupSalesActivity.this, MainActivity.class));
            finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        } else if (item.getItemId() == R.id.action_search) {

        }

        return super.onOptionsItemSelected(item);
    }

    public void stockIn(View view) {
        startActivity(new Intent(GroupSalesActivity.this, GroupSalesDetailsActivity.class));
        finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }
}
