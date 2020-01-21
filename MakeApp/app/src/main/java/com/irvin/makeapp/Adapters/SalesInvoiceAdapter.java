package com.irvin.makeapp.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.irvin.makeapp.Activities.SalesInvoiceProductDetailsActivity;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Constant.TranStatus;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Models.Invoice;
import com.irvin.makeapp.Models.StockIn;
import com.irvin.makeapp.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SalesInvoiceAdapter extends RecyclerView.Adapter<SalesInvoiceAdapter.ViewHolder> {
    private List<Invoice> invoices;
    private Context mContext = null;
    DatabaseHelper databaseHelper;

    public SalesInvoiceAdapter(List<Invoice> invoices, Context mContext) {
        this.invoices = invoices;
        this.mContext = mContext;
        databaseHelper = new DatabaseHelper(mContext);
    }

    @Override
    public SalesInvoiceAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_invoice_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SalesInvoiceAdapter.ViewHolder viewHolder, final int position) {
        try {
            Date date = null, date2 = null;

            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(invoices.get(position).getDateCreated());
            DateFormat formatter = new SimpleDateFormat("E. MMM dd, yyyy HH:mm:ss");


            if (invoices.get(position).getStatus().equals(TranStatus.PENDING.toString())) {
                DateFormat formatter2 = new SimpleDateFormat("E. MMM dd, yyyy");
                date2 = new SimpleDateFormat("yyyy-MM-dd").parse(invoices.get(position).getDueDate());
                viewHolder.dateCreated.setText("DUE DATE : " + formatter2.format(date2));

                Log.e("asd", invoices.get(position).getDueDate());

            } else
                viewHolder.dateCreated.setText(formatter.format(date));

            Log.e("asd", invoices.get(position).getStatus());

            viewHolder.customerName.setText(invoices.get(position).getCustomerName());

            viewHolder.invoiceId.setText("#INV-" + String.format("%0" + ModGlobal.receiptLimit.length() + "d", Integer.parseInt(invoices.get(position).getInvoiceId())));

            if (invoices.get(position).getStatus().equals(TranStatus.PAID.toString())) {
                Log.e("asd", "Im here");
                viewHolder.status.setBackground(mContext.getResources().getDrawable(R.drawable.paid));
                viewHolder.status.setText("COMPLETED");
            }

            DecimalFormat dec = new DecimalFormat("#,##0.00");

            viewHolder.total.setText("₱ " + dec.format(Double.parseDouble(invoices.get(position).getTotalAmount())));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void update(List<Invoice> invoices) {
        this.invoices = invoices;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return invoices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView customerName, invoiceId, dateCreated, status, total;


        public ViewHolder(View view) {
            super(view);

            customerName = view.findViewById(R.id.customerName);
            invoiceId = view.findViewById(R.id.invoiceId);
            dateCreated = view.findViewById(R.id.dateCreated);
            status = view.findViewById(R.id.status);
            total = view.findViewById(R.id.totalAmount);
        }
    }


}