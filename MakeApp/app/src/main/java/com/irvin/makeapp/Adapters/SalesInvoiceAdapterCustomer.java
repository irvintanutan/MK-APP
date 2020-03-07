package com.irvin.makeapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Constant.TranStatus;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Models.Invoice;
import com.irvin.makeapp.R;
import com.irvin.makeapp.Services.Logger;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author irvin
 */
public class SalesInvoiceAdapterCustomer extends RecyclerView.Adapter<SalesInvoiceAdapterCustomer.ViewHolder> {
    private List<Invoice> invoices;
    private Context mContext = null;
    DatabaseHelper databaseHelper;

    public SalesInvoiceAdapterCustomer(List<Invoice> invoices, Context mContext) {
        this.invoices = invoices;
        this.mContext = mContext;
        databaseHelper = new DatabaseHelper(mContext);
    }

    @Override
    public SalesInvoiceAdapterCustomer.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_invoice_list2, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SalesInvoiceAdapterCustomer.ViewHolder viewHolder, final int position) {
        try {

            Invoice invoice =  invoices.get(position);

            Date date, date2, now;

            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(invoice.getDateCreated());
            DateFormat formatter = new SimpleDateFormat("E. MMM dd, yyyy HH:mm:ss");
            DateFormat formatter2 = new SimpleDateFormat("E. MMM dd, yyyy");
            date2 = new SimpleDateFormat("yyyy-MM-dd").parse(invoice.getDueDate());


            now = new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

            if (date2.before(now) || date2.equals(now)) {

                Log.e(date2.toString() , now.toString());

                viewHolder.cv.setBackgroundColor(Color.parseColor("#FFCDD2"));
                viewHolder.cv.setBackgroundResource(R.drawable.round_quantity3);

            }else {
                viewHolder.cv.setBackgroundColor(Color.parseColor("#FFFFFF"));
                viewHolder.cv.setBackgroundResource(R.drawable.round_quantity4);
            }

            if (invoice.getStatus().equals(TranStatus.PENDING.toString())) {
                viewHolder.dateCreated.setText("DUE DATE : " + formatter2.format(date2));

            } else {
                viewHolder.dateCreated.setText(formatter.format(date));
            }


            viewHolder.invoiceId.setText("#INV-" + String.format("%0" + ModGlobal.receiptLimit.length() + "d", Integer.parseInt(invoice.getInvoiceId())));


            DecimalFormat dec = new DecimalFormat("#,##0.00");

            viewHolder.total.setText("₱ " + dec.format(Double.parseDouble(invoice.getTotalAmount())));

        } catch (Exception e) {
            e.printStackTrace();
            Logger.CreateNewEntry(mContext , e , new File(mContext.getExternalFilesDir("") , ModGlobal.logFile));
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

        TextView invoiceId, dateCreated, total;
        CardView cv;

        public ViewHolder(View view) {
            super(view);

            cv = view.findViewById(R.id.cv);
            invoiceId = view.findViewById(R.id.invoiceId);
            dateCreated = view.findViewById(R.id.dateCreated);

            total = view.findViewById(R.id.totalAmount);
        }
    }


}