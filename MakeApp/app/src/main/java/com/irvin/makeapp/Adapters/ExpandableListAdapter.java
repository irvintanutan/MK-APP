package com.irvin.makeapp.Adapters;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.hbb20.GThumb;
import com.irvin.makeapp.Activities.PaymentActivity;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Constant.TranStatus;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Database.DatabaseInvoice;
import com.irvin.makeapp.Models.Invoice;
import com.irvin.makeapp.Models.MainForm;
import com.irvin.makeapp.R;
import com.white.progressview.HorizontalProgressView;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by irvin on 8/30/17.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private DatabaseHelper db;
    private DatabaseInvoice databaseInvoice;
    private Activity context;
    private Map<MainForm, List<Invoice>> formDetails;
    private List<MainForm> formName;

    public ExpandableListAdapter(Activity context, List<MainForm> formName,
                                 Map<MainForm, List<Invoice>> formDetails) {
        this.context = context;
        this.formDetails = formDetails;
        this.formName = formName;
        databaseInvoice = new DatabaseInvoice(context);
        db = new DatabaseHelper(context);

    }

    public Object getChild(int groupPosition, int childPosition) {
        return formDetails.get(formName.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Invoice invoices = formDetails.get(formName.get(groupPosition)).get(childPosition);

        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_invoice_list2, null);
        }

        TextView invoiceId, dateCreated, total;
        CardView cv;

        cv = convertView.findViewById(R.id.cv);
        invoiceId = convertView.findViewById(R.id.invoiceId);
        dateCreated = convertView.findViewById(R.id.dateCreated);

        total = convertView.findViewById(R.id.totalAmount);

        try {
            Date date, date2, now;

            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(invoices.getDateCreated());
            DateFormat formatter = new SimpleDateFormat("E. MMM dd, yyyy HH:mm:ss");
            DateFormat formatter2 = new SimpleDateFormat("E. MMM dd, yyyy");
            date2 = new SimpleDateFormat("yyyy-MM-dd").parse(invoices.getDueDate());


            now = new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

            if (date2.before(now) || date2.equals(now)) {

                Log.e(date2.toString() , now.toString());

                cv.setBackgroundColor(Color.parseColor("#FFCDD2"));
                cv.setBackgroundResource(R.drawable.round_quantity3);
                createNotificationChannel(invoices);
            }else {
                cv.setBackgroundColor(Color.parseColor("#FFFFFF"));
                cv.setBackgroundResource(R.drawable.round_quantity4);
            }

            if (invoices.getStatus().equals(TranStatus.PENDING.toString())) {
                dateCreated.setText("DUE DATE : " + formatter2.format(date2));

            } else
                dateCreated.setText(formatter.format(date));


            invoiceId.setText("#INV-" + String.format("%0" + ModGlobal.receiptLimit.length() + "d", Integer.parseInt(invoices.getInvoiceId())));


            DecimalFormat dec = new DecimalFormat("#,##0.00");

            total.setText("₱ " + dec.format(Double.parseDouble(invoices.getTotalAmount())));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return formDetails.get(formName.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return formName.get(groupPosition);
    }

    public int getGroupCount() {
        return formName.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.layout_main_form_invoice,
                    null);
        }

        TextView fullName, totalAmount, totalAmountPaid, totalBalance;
        HorizontalProgressView progressBar;
        View view;
        CircleImageView profilePicture2;
        GThumb profilePicture;


        fullName = convertView.findViewById(R.id.fullName);
        totalAmount = convertView.findViewById(R.id.totalAmount);
        totalAmountPaid = convertView.findViewById(R.id.totalAmountPaid);
        totalBalance = convertView.findViewById(R.id.totalBalance);
        profilePicture2 = convertView.findViewById(R.id.profilePicture2);
        profilePicture = convertView.findViewById(R.id.profilePicture);
        progressBar = convertView.findViewById(R.id.progressBar);
        view = convertView.findViewById(R.id.view);

        progressBar.setMax((int) Double.parseDouble(formName.get(groupPosition).getTotalAmount()));
        progressBar.setProgress((int) Double.parseDouble(formName.get(groupPosition).getTotalAmountPaid()));


        if (databaseInvoice.getAllDueInvoices(formName.get(groupPosition).getCustomerId(), true) != null){
            view.setBackgroundColor(Color.parseColor("#D50000"));
        }

        if (!formName.get(groupPosition).getPath().isEmpty() && formName.get(groupPosition).getPath() != null) {
            profilePicture.setVisibility(View.GONE);
            Glide.with(context).load(new File(formName.get(groupPosition).getPath())).into(profilePicture2);
        } else {
            profilePicture2.setVisibility(View.GONE);
            profilePicture.applyMultiColor();
            profilePicture.loadThumbForName("", formName.get(groupPosition).getCustomerName().split(" ")[0],
                    formName.get(groupPosition).getCustomerName().split(" ")[1]);
        }




        DecimalFormat dec = new DecimalFormat("#,##0.00");
        fullName.setText(formName.get(groupPosition).getCustomerName());
        totalAmount.setText("₱ " + dec.format(Double.parseDouble(formName.get(groupPosition).getTotalAmount())));
        totalAmountPaid.setText("₱ " + dec.format(Double.parseDouble(formName.get(groupPosition).getTotalAmountPaid())));
        totalBalance.setText("₱ " + dec.format(Double.parseDouble(formName.get(groupPosition).getTotalBalance())));


        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public void update(Map<MainForm, List<Invoice>> details, List<MainForm> mainForm) {
        if (formName == null || formName.size() == 0)
            return;
        if (formName != null && formName.size() > 0) {
            formDetails.clear();
            formName.clear();
        }
        formDetails.putAll(details);
        formName.addAll(mainForm);

        notifyDataSetChanged();
    }


    private void createNotificationChannel(Invoice invoice) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("SALES_INVOICE", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

            Log.e("ad" , "asddd");
            // Create an explicit intent for an Activity in your app
            Intent intent = new Intent(context, PaymentActivity.class);
            intent.putExtra("invoice", invoice.getInvoiceId());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, Integer.parseInt(invoice.getInvoiceId()), intent, PendingIntent.FLAG_ONE_SHOT);


            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "SALES_INVOICE")
                    .setCategory(Notification.CATEGORY_ALARM)
                    .setTicker("PLEASE")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(invoice.getCustomerName())
                    .setContentText("#INV-" + String.format("%0" + ModGlobal.receiptLimit.length() + "d", Integer.parseInt(invoice.getInvoiceId())) + "" +
                            " is already DUE")
                    .setSubText("Tap To Resolve Invoice")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent);


            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            notificationManager.notify(Integer.parseInt(invoice.getInvoiceId()), builder.build());
        }
}
