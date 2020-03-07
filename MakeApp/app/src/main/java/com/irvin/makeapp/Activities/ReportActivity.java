package com.irvin.makeapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Align;
import com.anychart.enums.HoverMode;
import com.anychart.enums.LegendLayout;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Anchor;
import com.irvin.makeapp.Adapters.DataAdapter;
import com.irvin.makeapp.Adapters.CustomerFragment.TabFragmentCustomerOrders;
import com.irvin.makeapp.Adapters.CustomerFragment.TabFragmentCustomerProfile;
import com.irvin.makeapp.Adapters.CustomerFragment.TabFragmentCustomerReminder;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Database.DatabaseCustomer;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Database.DatabaseInvoice;
import com.irvin.makeapp.Models.MenuForm;
import com.irvin.makeapp.Models.TopTenProductModel;
import com.irvin.makeapp.Models.TransactionModel;
import com.irvin.makeapp.R;
import com.irvin.makeapp.Services.Logger;

import java.io.File;
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
    List<String> skuList = new ArrayList<> ();
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
                            //tickets(true);
                            break;
                        case 5:
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



        productChart();
        topCustomer();

    }

    void topCustomer() {

        try {
            AnyChartView anyChartView = findViewById(R.id.any_chart_view1);

            APIlib.getInstance().setActiveAnyChartView(anyChartView);
            Pie pie = AnyChart.pie();

            pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
                @Override
                public void onClick(Event event) {
                }
            });

            List<TransactionModel> transactionModels = databaseCustomer.getTop5Customer();

            List<DataEntry> data = new ArrayList<>();
            for (TransactionModel transactionModel : transactionModels) {

                data.add(new ValueDataEntry(transactionModel.getCustomerName(), Integer.parseInt(transactionModel.getTotalAmount())));

            }


            pie.data(data);

            pie.title("Top 5 Customers Base on Purchases");

            pie.labels().position("outside");

            pie.legend().title().enabled(true);
            pie.legend().title()
                    .text("Customers")
                    .padding(0d, 0d, 10d, 0d);

            pie.legend()
                    .position("center-bottom")
                    .itemsLayout(LegendLayout.HORIZONTAL)
                    .align(Align.CENTER);
            anyChartView.setChart(pie);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.CreateNewEntry(e, new File(getExternalFilesDir(""), ModGlobal.logFile));
        }
    }

    void productChart() {
        try {
            AnyChartView anyChartView = findViewById(R.id.any_chart_view);

            APIlib.getInstance().setActiveAnyChartView(anyChartView);


            Cartesian cartesian = AnyChart.column();


            topTenProductModels = databaseHelper.getTopTenProduct();

            List<DataEntry> data = new ArrayList<>();
            for (int a = 0; a < topTenProductModels.size(); a++) {
                TopTenProductModel topTenProductModel = topTenProductModels.get(a);
                Log.e(topTenProductModel.getProductName() , Double.toString(topTenProductModel.getTotal()));
                data.add(new ValueDataEntry(topTenProductModel.getProductName(), topTenProductModel.getTotal()));
            }


            Column column = cartesian.column(data);

            column.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(String.valueOf(Anchor.CENTER_BOTTOM))
                    .offsetX(0d)
                    .offsetY(5d)
                    .format("₱{%Value}{groupsSeparator: }");

            cartesian.animation(true);
            cartesian.title("Top 10 Products by Revenue");

            cartesian.yScale().minimum(0d);

            cartesian.yAxis(0).labels().format("₱{%Value}{groupsSeparator: }");

            cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
            cartesian.interactivity().hoverMode(HoverMode.BY_X);

            cartesian.xAxis(0).title("Product");
            cartesian.yAxis(0).title("Revenue");
            cartesian.xAxis(0).enabled(false);
            anyChartView.setChart(cartesian);
        }catch (Exception e){
            e.printStackTrace();
            Logger.CreateNewEntry(e, new File(getExternalFilesDir(""), ModGlobal.logFile));
        }
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


    public void sales(){
        TabFragmentCustomerOrders tabFragmentCustomerOrders = new TabFragmentCustomerOrders();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frameLayout, tabFragmentCustomerOrders);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

    public void inventory() {
        TabFragmentCustomerProfile tabFragmentCustomerProfile = new TabFragmentCustomerProfile();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frameLayout, tabFragmentCustomerProfile);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

    public void customer() {
        TabFragmentCustomerReminder tabFragmentCustomerReminder = new TabFragmentCustomerReminder();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frameLayout, tabFragmentCustomerReminder);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }


}
