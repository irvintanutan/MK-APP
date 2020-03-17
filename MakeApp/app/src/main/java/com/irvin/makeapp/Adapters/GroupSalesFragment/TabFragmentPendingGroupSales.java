package com.irvin.makeapp.Adapters.GroupSalesFragment;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.irvin.makeapp.Database.DatabaseGroupSales;
import com.irvin.makeapp.Models.GroupSalesModel;
import com.irvin.makeapp.R;

import java.util.ArrayList;
import java.util.List;


/**
 * @author irvin
 * @date 2/6/17
 */
public class TabFragmentPendingGroupSales extends Fragment {

    DatabaseGroupSales databaseGroupSales;
    List<GroupSalesModel> groupSalesModels = new ArrayList<>();
    View view;
    SearchView searchView;
    String filter = "";
    RecyclerView recyclerView;
    GroupSalesAdapter groupSalesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.group_sales_pending_tab, container, false);

        databaseGroupSales = new DatabaseGroupSales(getActivity());

        groupSalesModels = databaseGroupSales.getGroupSales("");

        if (groupSalesModels.size() > 0) {
            recyclerView = view.findViewById(R.id.pending_card_recycler_view);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);


            groupSalesAdapter = new GroupSalesAdapter(groupSalesModels, getActivity());
            recyclerView.setAdapter(groupSalesAdapter);


            recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

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

                        Toast.makeText(getActivity() , groupSalesModels.get(position).getConsultants() , Toast.LENGTH_LONG).show();
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

        }
        return view;
    }

}
