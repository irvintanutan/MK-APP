package com.irvin.makeapp.Adapters.SalesInvoiceFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.irvin.makeapp.Activities.PaymentActivity;
import com.irvin.makeapp.Adapters.SalesInvoiceAdapter;
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
public class TabFragmentPaid extends Fragment {
    DatabaseHelper databaseHelper;
    DatabaseInvoice databaseInvoice;
    List<Invoice> invoices;
    LinearLayout nothing;
    RecyclerView recyclerView;
    View view;
    SalesInvoiceAdapter salesInvoiceAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        invoices = new ArrayList<>();
        databaseInvoice = new DatabaseInvoice(getActivity());
        databaseHelper = new DatabaseHelper(getActivity());
        invoices = databaseInvoice.getAllInvoices(TranStatus.PAID.toString());
        if (invoices.size() > 0) {
            view = inflater.inflate(R.layout.paid_tab, container, false);

            recyclerView = view.findViewById(R.id.pending_card_recycler_view);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);


            salesInvoiceAdapter = new SalesInvoiceAdapter(invoices, getActivity());
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

                        ModGlobal.InvoiceOriginView = "SALES_INVOICE";
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

        return view;
    }

}
