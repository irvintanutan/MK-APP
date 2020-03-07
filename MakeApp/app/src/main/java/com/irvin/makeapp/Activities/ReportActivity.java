package com.irvin.makeapp.Activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;

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
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Database.DatabaseCustomer;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Database.DatabaseInvoice;
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

/**
 * @author irvin
 */
public class ReportActivity extends AppCompatActivity {

    DecimalFormat dec = new DecimalFormat("#,##0.00");
    DatabaseCustomer databaseCustomer = new DatabaseCustomer(this);
    DatabaseInvoice databaseInvoice = new DatabaseInvoice(this);
    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    List<TopTenProductModel> topTenProductModels = new ArrayList<>();

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

    public void inventory(View view) {

    }

    public void customer(View view) {

    }

    public void statistic(View view) {
    }

    public void month(View view) {

        createDialogWithoutDateField().show();
    }

    private DatePickerDialog createDialogWithoutDateField() {
        DatePickerDialog dpd = new DatePickerDialog(this, null, 2014, 1, 24);
        try {
            java.lang.reflect.Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(dpd);
                    java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
                    for (java.lang.reflect.Field datePickerField : datePickerFields) {
                        Log.i("test", datePickerField.getName());
                        if ("mDaySpinner".equals(datePickerField.getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
        catch (Exception ex) {
        }
        return dpd;
    }
}
