package com.irvin.makeapp.Adapters;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.irvin.makeapp.Activities.PaymentActivity;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Constant.TranStatus;
import com.irvin.makeapp.Database.DatabaseCustomer;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Database.DatabaseInvoice;
import com.irvin.makeapp.Models.Invoice;
import com.irvin.makeapp.Models.MainForm;
import com.irvin.makeapp.Models.Payment;
import com.irvin.makeapp.Models.TransactionModel;
import com.irvin.makeapp.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by irvin on 2/6/17.
 */
public class TabFragmentPending extends Fragment {

    DatabaseHelper databaseHelper;
    DatabaseInvoice databaseInvoice;
    DatabaseCustomer databaseCustomer;
    List<Invoice> invoices;
    LinearLayout nothing;
    RecyclerView recyclerView;
    View view;
    SalesInvoiceAdapter salesInvoiceAdapter;
    List<Payment> payments;
    List<MainForm> groupList;
    List<Invoice> childList;
    Map<MainForm, List<Invoice>> draftCollection;
    ExpandableListAdapter expListAdapter;
    ExpandableListView expListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.pending_tab, container, false);
        databaseHelper = new DatabaseHelper(getActivity());
        databaseInvoice = new DatabaseInvoice(getActivity());
        databaseCustomer = new DatabaseCustomer(getActivity());
        groupList = new ArrayList<>();
        invoices = new ArrayList<>();
        invoices = databaseInvoice.getAllInvoices(TranStatus.PENDING.toString());
        createCollection();
        expListView = view.findViewById(R.id.expandableListView);
        expListAdapter = new ExpandableListAdapter(
                getActivity(), groupList, draftCollection);
        expListView.setAdapter(expListAdapter);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                Invoice inv = draftCollection.get(groupList.get(groupPosition)).get(childPosition);
                ModGlobal.invoice = new Invoice();
                ModGlobal.invoice = inv;

                Intent i = new Intent(getActivity(), PaymentActivity.class);
                startActivity(i);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

                return true;
            }
        });


      /*  databaseHelper = new DatabaseHelper(getActivity());
        invoices = new ArrayList<>();
        invoices = databaseHelper.getAllInvoices(TranStatus.PENDING.toString());
        if (invoices.size() > 0) {
            view = inflater.inflate(R.layout.pending_tab, container, false);

            recyclerView = view.findViewById(R.id.expandableListView);
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

        }*/

        return view;
    }


    private void createCollection() {
        draftCollection = new LinkedHashMap<>();

        List<TransactionModel> customerModels = databaseCustomer.getAllCustomerWithDueDates(false);

        for (TransactionModel customerModel : customerModels) {

            Log.e("TABFRAGMENT" , customerModel.getTotalAmount() + " " + customerModel.getTotalAmountPaid());

            double balance = Double.parseDouble(databaseInvoice.getAllDueInvoices(customerModel.getCustomerId(), false)) -
                    Double.parseDouble(customerModel.getTotalAmountPaid());



            groupList.add(new MainForm(customerModel.getPhotoUrl(),
                    customerModel.getCustomerName(), databaseInvoice.getAllDueInvoices(customerModel.getCustomerId() , false), customerModel.getTotalAmountPaid()
                    , Double.toString(balance) , customerModel.getCustomerId()));
        }


        for (MainForm form : groupList) {

            loadChild(form.getCustomerId());

            draftCollection.put(form, childList);
        }

    }


    private void loadChild(String customerId) {
        childList = new ArrayList<>();
        for (Invoice model : invoices) {

            if (model.getCustomerId().equals(customerId))
                childList.add(model);
        }

    }


}
