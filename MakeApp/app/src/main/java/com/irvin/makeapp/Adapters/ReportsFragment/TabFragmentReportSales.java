package com.irvin.makeapp.Adapters.ReportsFragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.HoverMode;
import com.anychart.enums.MarkerType;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Anchor;
import com.anychart.graphics.vector.Stroke;
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;
import com.irvin.makeapp.Activities.MainActivity;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Database.DatabaseInvoice;
import com.irvin.makeapp.R;
import com.irvin.makeapp.Services.Logger;
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    Calendar start = null, end = null;

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
        monthlySalesPerYear();
    }


    void dateRange() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.date_range_skema, null);
        final DateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");

        start = Calendar.getInstance();
        end = Calendar.getInstance();


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // this is set the view from XML inside AlertDialog
        builder.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        builder.setCancelable(false);


        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Set", null);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                salesValue.setText(mDatabaseInvoice.getPeriodicSales(start, end));
                salesLabel.setText(formatter.format(start.getTime()) + "  TO  " + formatter.format(end.getTime()));

            }
        });

        builder.show();


        final DateRangeCalendarView datePicker = alertLayout.findViewById(R.id.calendar);


        datePicker.setCalendarListener(new DateRangeCalendarView.CalendarListener() {
            @Override
            public void onFirstDateSelected(Calendar startDate) {
            }

            @Override
            public void onDateRangeSelected(Calendar startDate, Calendar endDate) {
                start = startDate;
                end = endDate;

            }
        });

    }

    void showMonthlyPicker() {


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


    void monthlySalesPerYear() {
        AnyChartView anyChartView = view.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));

        Calendar calendar = Calendar.getInstance();

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Annual Monthly Sales for year " + calendar.get(Calendar.YEAR));

        cartesian.yAxis(0).title("Total Amount Sales PHP");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> seriesData = new ArrayList<>();
        String[] months = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};

        for (int a = 0; a < months.length; a++) {
            String date = calendar.get(Calendar.YEAR) + "-" + months[a];
            DateFormat formatter2 = new SimpleDateFormat("MMM");
            try {
                Date d = new SimpleDateFormat("yyyy-MM").parse(date);

                seriesData.add(new CustomDataEntry(formatter2.format(d), Double.parseDouble(mDatabaseInvoice.getMonthlySales(date)
                        .replace(",",""))));

            } catch (Exception e) {
                e.printStackTrace();
                Logger.CreateNewEntry(getContext(), e, new File(getActivity().getExternalFilesDir(""), ModGlobal.logFile));
            }


        }


        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.name("Monthly Sales");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(String.valueOf(Anchor.LEFT_CENTER))
                .offsetX(5d)
                .offsetY(5d);


        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        anyChartView.setChart(cartesian);
    }

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value) {
            super(x, value);
        }

    }


}
