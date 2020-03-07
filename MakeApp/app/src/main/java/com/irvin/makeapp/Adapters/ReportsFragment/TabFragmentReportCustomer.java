package com.irvin.makeapp.Adapters.ReportsFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Database.DatabaseCustomer;
import com.irvin.makeapp.Models.TransactionModel;
import com.irvin.makeapp.R;
import com.irvin.makeapp.Services.Logger;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

/**
 * @author irvin
 * @date 2/7/17
 */
public class TabFragmentReportCustomer extends Fragment {
    View view;
    DecimalFormat dec = new DecimalFormat("#,##0.00");
    DatabaseCustomer databaseCustomer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.report_tab_customer, container, false);
        databaseCustomer = new DatabaseCustomer(getActivity());

        topCustomer();

        return view;
    }


    void topCustomer() {

        try {

            AnyChartView anyChartView = view.findViewById(R.id.any_chart_view);

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
            Logger.CreateNewEntry(getContext(), e, new File(getActivity().getExternalFilesDir(""), ModGlobal.logFile));
        }
    }


}
