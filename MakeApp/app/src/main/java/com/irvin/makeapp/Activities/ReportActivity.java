package com.irvin.makeapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.irvin.makeapp.Database.DatabaseCustomer;
import com.irvin.makeapp.Database.DatabaseInvoice;
import com.irvin.makeapp.Models.TransactionModel;
import com.irvin.makeapp.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReportActivity extends AppCompatActivity {
    DecimalFormat dec = new DecimalFormat("#,##0.00");
    TextView receivables , thisMonth, totalSales;
    DatabaseCustomer databaseCustomer = new DatabaseCustomer(this);
    DatabaseInvoice databaseInvoice = new DatabaseInvoice(this);

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
}
