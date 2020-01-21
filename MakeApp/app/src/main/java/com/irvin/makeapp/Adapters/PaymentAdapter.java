package com.irvin.makeapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Models.Payment;
import com.irvin.makeapp.Models.Products;
import com.irvin.makeapp.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {
    private List<Payment> payments;
    private Context mContext = null;

    public PaymentAdapter(List<Payment> payments, Context mContext) {
        this.payments = payments;
        this.mContext = mContext;
    }

    @Override
    public PaymentAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.payment_card_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PaymentAdapter.ViewHolder viewHolder, int position) {
        try {


            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(payments.get(position).getDateCreated());

            DateFormat formatter = new SimpleDateFormat("E. MMM dd, yyyy HH:mm:ss");

            viewHolder.dateCreated.setText(formatter.format(date));

            viewHolder.id.setText("#RCPT-" + String.format("%0" + ModGlobal.receiptLimit.length() + "d", Integer.parseInt(payments.get(position).getPaymentId())));

            DecimalFormat dec = new DecimalFormat("#,##0.00");

            viewHolder.amount.setText("₱ " + dec.format(Double.parseDouble(payments.get(position).getAmount())));


        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void update(List<Payment> payments) {
        this.payments = payments;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return payments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView id, dateCreated, amount;


        public ViewHolder(View view) {
            super(view);

            id = view.findViewById(R.id.paymentId);
            dateCreated = view.findViewById(R.id.dateCreated);
            amount = view.findViewById(R.id.totalAmount);


        }
    }


}