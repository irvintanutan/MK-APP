package com.irvin.makeapp.Adapters;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.irvin.makeapp.Activities.StockInActivity;
import com.irvin.makeapp.Activities.StockInDetailsActivity;
import com.irvin.makeapp.Constant.ClickListener;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Constant.RecyclerTouchListener;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Database.DatabaseStockin;
import com.irvin.makeapp.Models.StockIn;
import com.irvin.makeapp.Models.StockInList;
import com.irvin.makeapp.R;
import com.irvin.makeapp.Services.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 *
 * @author irvin
 * @date 2/7/17
 */
public class TabFragmentStockInList extends Fragment {
    View view;
    DatabaseHelper databaseHelper;
    DatabaseStockin databaseStockin;
    List<StockInList> stockInListList;
    LinearLayout nothing;
    RecyclerView recyclerView;
    StockInMainAdapter stockInMainAdapter;
    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        databaseHelper = new DatabaseHelper(getActivity());
        databaseStockin = new DatabaseStockin(getActivity());
        view = inflater.inflate(R.layout.stock_in_tab, container, false);
        nothing = view.findViewById(R.id.nothing);
        fab = view.findViewById(R.id.floating_action_button);
        stockInListList = new ArrayList<>();
        stockInListList = databaseStockin.getAllStockIn();

        if (stockInListList.size() > 0) {
            nothing.setVisibility(View.GONE);
        }

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);
        stockInMainAdapter = new StockInMainAdapter(stockInListList, getActivity());
        recyclerView.setAdapter(stockInMainAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {

            @Override
            public void onClick(View view, int position) {
                try {
                    StockInList stockInList = databaseStockin.getAllStockIn(stockInListList.get(position).getId());


                    JSONArray jsonArray = new JSONArray(stockInList.getDetails());
                    ArrayList<StockIn> stockIns = new ArrayList<>();
                    for (int a = 0 ; a < jsonArray.length() ; a++){

                        JSONObject object = jsonArray.getJSONObject(a);
                        StockIn stockIn = new StockIn(object.getString("productName")
                                ,object.getString("productCode") , object.getString("quantity")
                                , object.getString("price"));

                        stockIns.add(stockIn);
                    }

                    ModGlobal.stockIns = stockIns;

                    Intent intent = new Intent(getActivity(), StockInDetailsActivity.class);
                    ModGlobal.indicator = true;
                    startActivity(intent);
                    getActivity().finish();

                } catch (JSONException e) {
                    e.printStackTrace();

                    Logger.CreateNewEntry(e , new File(getActivity().getExternalFilesDir("") , ModGlobal.logFile));
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }


        }));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModGlobal.stockIns.clear();
                Intent intent = new Intent(getActivity(), StockInActivity.class);
                startActivity(intent);
                getActivity().finish();
                ModGlobal.ProductModelListCopy.clear();
                ModGlobal.stockIns.clear();
                ModGlobal.ProductModelList.clear();

            }
        });

        return view;
    }

}
