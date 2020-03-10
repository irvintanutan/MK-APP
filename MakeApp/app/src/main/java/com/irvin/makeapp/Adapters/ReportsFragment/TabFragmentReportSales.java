package com.irvin.makeapp.Adapters.ReportsFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Database.DatabaseInvoice;
import com.irvin.makeapp.R;
import com.irvin.makeapp.Services.Logger;
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AlertDialog;
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
                salesLabel.setText("Daily Sales");

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
                salesLabel.setText("Gross Sales");

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

                dateRange();
            }
        });


        salesValue.setText(mDatabaseInvoice.getTotalSales());

    }


    void dateRange() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.date_range_skema, null);


        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();
        dialog.show();

        final DateRangeCalendarView datePicker = alertLayout.findViewById(R.id.calendar);


        datePicker.setCalendarListener(new DateRangeCalendarView.CalendarListener() {
            @Override
            public void onFirstDateSelected(Calendar startDate) {
            }

            @Override
            public void onDateRangeSelected(Calendar startDate, Calendar endDate) {

                salesValue.setText(mDatabaseInvoice.getPeriodicSales(startDate , endDate));
                DateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");

                salesLabel.setText(formatter.format(startDate.getTime()) + "  TO  " + formatter.format(endDate.getTime()));

                dialog.dismiss();
            }
        });

    }

    void showMonthlyPicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2010, 01, 01);

        YearMonthPickerDialog yearMonthPickerDialog = new YearMonthPickerDialog(getContext(), new YearMonthPickerDialog.OnDateSetListener() {
            @Override
            public void onYearMonthSet(int year, int month) {
                try {
                    String append = "";
                    if (month < 9) {
                        append = "0";
                    }
                    String date = year + "-" + append + (month + 1);


                    DateFormat formatter2 = new SimpleDateFormat("MMMM  yyyy");
                    Date d = new SimpleDateFormat("yyyy-MM").parse(date);

                    salesValue.setText(mDatabaseInvoice.getMonthlySales(date));
                    salesLabel.setText(formatter2.format(d) + "  Sales");


                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.CreateNewEntry(getContext(), e, new File(getActivity().getExternalFilesDir(""), ModGlobal.logFile));
                }
            }
        });

        yearMonthPickerDialog.show();
    }

}
