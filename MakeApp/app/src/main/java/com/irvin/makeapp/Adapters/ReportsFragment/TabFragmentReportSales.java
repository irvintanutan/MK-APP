package com.irvin.makeapp.Adapters.ReportsFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Database.DatabaseInvoice;
import com.irvin.makeapp.R;
import com.irvin.makeapp.Services.Logger;
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;

import java.io.File;
import java.util.Calendar;

import androidx.fragment.app.Fragment;

/**
 * @author irvin
 * @date 2/7/17
 */
public class TabFragmentReportSales extends Fragment {
    View view;
    DatabaseInvoice mDatabaseInvoice;
    Button today, monthly, gross, periodic;
    TextView salesValue, salesLabel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.report_tab_sales, container, false);
        mDatabaseInvoice = new DatabaseInvoice(getActivity());
        init();

        return view;
    }

    void init() {

        monthly = view.findViewById(R.id.monthSales);
        today = view.findViewById(R.id.todaySales);
        gross = view.findViewById(R.id.grossSales);
        periodic = view.findViewById(R.id.periodicSales);
        salesValue = view.findViewById(R.id.salesValue);
        salesLabel = view.findViewById(R.id.label);

        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                monthly.setBackgroundResource(R.drawable.round3);
                today.setBackgroundResource(R.drawable.round_quantity);
                periodic.setBackgroundResource(R.drawable.round_quantity);
                gross.setBackgroundResource(R.drawable.round_quantity);


                showMonthlyPicker();
            }
        });

        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salesValue.setText(mDatabaseInvoice.getTodaySales());
                today.setBackgroundResource(R.drawable.round3);
                monthly.setBackgroundResource(R.drawable.round_quantity);
                periodic.setBackgroundResource(R.drawable.round_quantity);
                gross.setBackgroundResource(R.drawable.round_quantity);
            }
        });

        gross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salesValue.setText(mDatabaseInvoice.getTotalSales());
                gross.setBackgroundResource(R.drawable.round3);
                monthly.setBackgroundResource(R.drawable.round_quantity);
                periodic.setBackgroundResource(R.drawable.round_quantity);
                today.setBackgroundResource(R.drawable.round_quantity);
            }
        });

        periodic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                periodic.setBackgroundResource(R.drawable.round3);
                monthly.setBackgroundResource(R.drawable.round_quantity);
                today.setBackgroundResource(R.drawable.round_quantity);
                gross.setBackgroundResource(R.drawable.round_quantity);
            }
        });


        salesValue.setText(mDatabaseInvoice.getTotalSales());

    }

    void showMonthlyPicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2010, 01, 01);

        YearMonthPickerDialog yearMonthPickerDialog = new YearMonthPickerDialog(getContext(), new YearMonthPickerDialog.OnDateSetListener() {
            @Override
            public void onYearMonthSet(int year, int month) {
                try {
                    String date = year + "-" + (month+1);
                    salesValue.setText(mDatabaseInvoice.getMonthlySales(date));
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.CreateNewEntry(getContext() ,e, new File(getActivity().getExternalFilesDir(""), ModGlobal.logFile));
                }
            }
        });

        yearMonthPickerDialog.show();
    }

}
