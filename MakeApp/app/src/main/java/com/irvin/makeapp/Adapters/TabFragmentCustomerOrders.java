package com.irvin.makeapp.Adapters;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.irvin.makeapp.Activities.PaymentActivity;
import com.irvin.makeapp.Activities.SalesInvoiceProductActivity;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Constant.TranStatus;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Database.DatabaseInvoice;
import com.irvin.makeapp.Models.Invoice;
import com.irvin.makeapp.R;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 *
 * @author irvin
 * @date 2/7/17
 */
public class TabFragmentCustomerOrders extends Fragment {
    DatabaseHelper databaseHelper;
    DatabaseInvoice databaseInvoice;
    List<Invoice> invoices;
    LinearLayout nothing;
    RecyclerView recyclerView;
    View view;
    SalesInvoiceAdapterCustomer salesInvoiceAdapter;
    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_customer_order, container, false);

        invoices = new ArrayList<>();
        databaseInvoice = new DatabaseInvoice(getActivity());
        databaseHelper = new DatabaseHelper(getActivity());
        invoices = databaseInvoice.getAllInvoicesByCustomer(ModGlobal.customerId, TranStatus.PENDING.toString());

        nothing = view.findViewById(R.id.nothing);
        fab = view.findViewById(R.id.floating_action_button);
        if (invoices.size() > 0) {
            nothing.setVisibility(View.GONE);


            recyclerView = view.findViewById(R.id.pending_card_recycler_view);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);


            salesInvoiceAdapter = new SalesInvoiceAdapterCustomer(invoices, getActivity());
            recyclerView.setAdapter(salesInvoiceAdapter);


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


                        ModGlobal.invoice = new Invoice();
                        ModGlobal.invoice = invoices.get(position);

                        Intent i = new Intent(getActivity(), PaymentActivity.class);
                        startActivity(i);
                        getActivity().finish();
                        getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

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


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ModGlobal.InvoiceOriginView = "SALES_INVOICE_CUSTOMER";

                Intent intent = new Intent(getActivity(), SalesInvoiceProductActivity.class);
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
