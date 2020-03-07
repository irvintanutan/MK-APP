package com.irvin.makeapp.Adapters.ReportsFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.APIlib;
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
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Models.TopTenProductModel;
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
public class TabFragmentReportInventory extends Fragment {
    View view;
    DecimalFormat dec = new DecimalFormat("#,##0.00");
    DatabaseHelper databaseHelper;
    List<TopTenProductModel> topTenProductModels = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.report_tab_inventory, container, false);
        databaseHelper = new DatabaseHelper(getActivity());

        productChart();
        return view;
    }

    void productChart() {
        try {
            AnyChartView anyChartView = view.findViewById(R.id.any_chart_view);

            APIlib.getInstance().setActiveAnyChartView(anyChartView);


            Cartesian cartesian = AnyChart.column();


            topTenProductModels = databaseHelper.getTopTenProduct();

            List<DataEntry> data = new ArrayList<>();
            for (int a = 0; a < topTenProductModels.size(); a++) {
                TopTenProductModel topTenProductModel = topTenProductModels.get(a);
                Log.e(topTenProductModel.getProductName(), Double.toString(topTenProductModel.getTotal()));
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
        } catch (Exception e) {
            e.printStackTrace();
            Logger.CreateNewEntry(getContext() , e, new File(getActivity().getExternalFilesDir(""), ModGlobal.logFile));
        }
    }


}
