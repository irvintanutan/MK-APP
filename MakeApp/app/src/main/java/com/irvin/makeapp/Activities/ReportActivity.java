package com.irvin.makeapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Anchor;
import com.irvin.makeapp.Database.DatabaseCustomer;
import com.irvin.makeapp.Database.DatabaseInvoice;
import com.irvin.makeapp.Models.Invoice;
import com.irvin.makeapp.Models.StockIn;
import com.irvin.makeapp.Models.TopTenProductModel;
import com.irvin.makeapp.Models.TransactionModel;
import com.irvin.makeapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ReportActivity extends AppCompatActivity {
    DecimalFormat dec = new DecimalFormat("#,##0.00");
    TextView receivables , thisMonth, totalSales;
    DatabaseCustomer databaseCustomer = new DatabaseCustomer(this);
    DatabaseInvoice databaseInvoice = new DatabaseInvoice(this);
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

    void init(){

        receivables = findViewById(R.id.receivables);
        thisMonth = findViewById(R.id.thisMonth);
        totalSales = findViewById(R.id.totalSales);

        receivables.setText(accountReceivable());
        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM");
        thisMonth.setText(databaseInvoice.getMonthlySales( formatter.format(date)));
        totalSales.setText(databaseInvoice.getTotalSales());

        productChart();

    }


    private String accountReceivable(){
        String result = "";
        Double balance = 0.00;

        List<TransactionModel> customerModels = databaseCustomer.getAllCustomerWithDueDates(false);

        for (TransactionModel customerModel : customerModels) {

            balance += Double.parseDouble(databaseInvoice.getAllDueInvoices(customerModel.getCustomerId(), false)) -
                    Double.parseDouble(customerModel.getTotalAmountPaid());

        }

        return  "₱ " + dec.format(balance);
    }

    void productChart(){
        int counter = 0;
        AnyChartView anyChartView = findViewById(R.id.any_chart_view);

        Cartesian cartesian = AnyChart.column();


        List<Invoice> invoices = databaseInvoice.getAllInvoices();


        for (Invoice invoice : invoices) {

            try {


                JSONArray jsonArray = new JSONArray(invoice.getInvoiceDetail());

                for (int a = 0 ; a < jsonArray.length() ; a++) {

                    JSONObject object = jsonArray.getJSONObject(a);
                    StockIn stockIn = new StockIn(object.getString("productName")
                            ,object.getString("productCode") , object.getString("quantity")
                            , object.getString("price"));

                    addProduct(stockIn);
                    counter++;
                    if (counter == 12) break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }



        Collections.sort(topTenProductModels, new Comparator<TopTenProductModel>() {
            @Override
            public int compare(TopTenProductModel topTenProductModel, TopTenProductModel t1) {
                return Double.toString(topTenProductModel.getTotal()).compareTo(Double.toString(t1.getTotal()));
            }
        });

        List<DataEntry> data = new ArrayList<>();
        for (int a = topTenProductModels.size() - 1 ; a >= 0 ; a--) {
            TopTenProductModel topTenProductModel = topTenProductModels.get(a);
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
        cartesian.title("Top Products by Revenue");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("₱{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Product");
        cartesian.yAxis(0).title("Revenue");
        cartesian.xAxis(0).enabled(false);
        anyChartView.setChart(cartesian);

    }

    void addProduct(StockIn stockIn){

        boolean indicator = false;
        int index = 0;

        String productCode = stockIn.getProductCode();
        String productName = stockIn.getProductName();
        double price = Double.parseDouble(stockIn.getPrice().replace(",","")) * Integer.parseInt(stockIn.getQuantity());


           for (int a = 0; a < topTenProductModels.size(); a++) {
               if (stockIn.getProductCode().equals(topTenProductModels.get(a).getProductCode())){
                    indicator = true;
                    index = a;
               }
           }

           if (indicator){
                price += topTenProductModels.get(index).getTotal();
               topTenProductModels.set(index , new TopTenProductModel(topTenProductModels.get(index).getProductCode() ,
                       topTenProductModels.get(index).getProductName() , price));
           }else {
               topTenProductModels.add(new TopTenProductModel(productCode , productName , price));

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
}
