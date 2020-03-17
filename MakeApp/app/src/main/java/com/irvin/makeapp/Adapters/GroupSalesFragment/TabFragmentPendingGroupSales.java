package com.irvin.makeapp.Adapters.GroupSalesFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.irvin.makeapp.Activities.PaymentActivity;
import com.irvin.makeapp.Adapters.ExpandableListAdapter;
import com.irvin.makeapp.Adapters.SalesInvoiceAdapter;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


/**
 *
 * @author irvin
 * @date 2/6/17
 */
public class TabFragmentPendingGroupSales extends Fragment {

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
    SearchView searchView;
    String filter = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.group_sales_pending_tab, container, false);
        databaseHelper = new DatabaseHelper(getActivity());
        databaseInvoice = new DatabaseInvoice(getActivity());
        databaseCustomer = new DatabaseCustomer(getActivity());
        filter = ModGlobal.searchFilter;
        loadCustomerInvoices();


        searchView = view.findViewById(R.id.searchLayout);
        searchView.setQuery(filter, false);
        searchView.setQueryHint("Search Filter");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filter = newText;
                ModGlobal.searchFilter = filter;
                loadCustomerInvoices();

                return false;
            }
        });

        return view;
    }

    void loadCustomerInvoices(){
        groupList = new ArrayList<>();
        invoices = new ArrayList<>();
        invoices = databaseInvoice.getAllInvoices(TranStatus.PENDING.toString());
        createCollection();
        expListView = view.findViewById(R.id.expandableListView);
        expListAdapter = new ExpandableListAdapter(
                getActivity(), groupList, draftCollection);
        expListView.setAdapter(expListAdapter);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                Invoice inv = draftCollection.get(groupList.get(groupPosition)).get(childPosition);
                ModGlobal.invoice = new Invoice();
                ModGlobal.invoice = inv;


                ModGlobal.InvoiceOriginView = "SALES_INVOICE";
                Intent i = new Intent(getActivity(), PaymentActivity.class);
                startActivity(i);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

                return true;
            }
        });

        expListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    final int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    final int childPosition = ExpandableListView.getPackedPositionChild(id);


                    final Invoice inv = draftCollection.get(groupList.get(groupPosition)).get(childPosition);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    builder.setTitle("Warning");
                    builder.setIcon(getResources().getDrawable(R.drawable.warning));
                    builder.setMessage("Are you sure you want to delete #INV-" + String.format("%0" +
                            ModGlobal.receiptLimit.length() + "d", Integer.parseInt(inv.getInvoiceId())));

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            // Do nothing
                            dialog.dismiss();

                            databaseInvoice.deleteInvoice(inv.getInvoiceId());

                            Intent i = new Intent(getActivity(), getActivity().getClass());
                            startActivity(i);
                            getActivity().finish();
                            getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

                        }

                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                    return true;
                }

                return false;
            }
        });
    }


    private void createCollection() {
        draftCollection = new LinkedHashMap<>();

        List<TransactionModel> customerModels = databaseCustomer.getAllCustomerWithDueDates(false , filter);

        for (TransactionModel customerModel : customerModels) {

            Log.e("TABFRAGMENT", customerModel.getTotalAmount() + " " + customerModel.getTotalAmountPaid());

            double balance = Double.parseDouble(databaseInvoice.getAllDueInvoices(customerModel.getCustomerId(), false)) -
                    Double.parseDouble(customerModel.getTotalAmountPaid());


            groupList.add(new MainForm(customerModel.getPhotoUrl(),
                    customerModel.getCustomerName(), databaseInvoice.getAllDueInvoices(customerModel.getCustomerId(), false), customerModel.getTotalAmountPaid()
                    , Double.toString(balance), customerModel.getCustomerId()));
        }


        for (MainForm form : groupList) {

            loadChild(form.getCustomerId());

            draftCollection.put(form, childList);
        }

    }


    private void loadChild(String customerId) {
        childList = new ArrayList<>();
        for (Invoice model : invoices) {

            if (model.getCustomerId().equals(customerId)) {
                childList.add(model);
            }
        }

    }

}
