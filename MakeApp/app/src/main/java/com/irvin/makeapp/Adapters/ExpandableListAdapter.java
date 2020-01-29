package com.irvin.makeapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Constant.TranStatus;
import com.irvin.makeapp.Database.DatabaseHelper;
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
    private Activity context;
    private Map<MainForm, List<Invoice>> formDetails;
    private List<MainForm> formName;

    public ExpandableListAdapter(Activity context, List<MainForm> formName,
                                 Map<MainForm, List<Invoice>> formDetails) {
        this.context = context;
        this.formDetails = formDetails;
        this.formName = formName;

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

        invoiceId = convertView.findViewById(R.id.invoiceId);
        dateCreated = convertView.findViewById(R.id.dateCreated);

        total = convertView.findViewById(R.id.totalAmount);

        try {
            Date date, date2;

            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(invoices.getDateCreated());
            DateFormat formatter = new SimpleDateFormat("E. MMM dd, yyyy HH:mm:ss");


            if (invoices.getStatus().equals(TranStatus.PENDING.toString())) {
                DateFormat formatter2 = new SimpleDateFormat("E. MMM dd, yyyy");
                date2 = new SimpleDateFormat("yyyy-MM-dd").parse(invoices.getDueDate());
                dateCreated.setText("DUE DATE : " + formatter2.format(date2));

                Log.e("asd", invoices.getDueDate());

            } else
                dateCreated.setText(formatter.format(date));

            Log.e("asd", invoices.getStatus());

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
        CircleImageView profilePicture;


        fullName = convertView.findViewById(R.id.fullName);
        totalAmount = convertView.findViewById(R.id.totalAmount);
        totalAmountPaid = convertView.findViewById(R.id.totalAmountPaid);
        totalBalance = convertView.findViewById(R.id.totalBalance);
        profilePicture = convertView.findViewById(R.id.profilePicture);
        progressBar = convertView.findViewById(R.id.progressBar);


        progressBar.setMax((int)Double.parseDouble(formName.get(groupPosition).getTotalAmount()));
        progressBar.setProgress((int)Double.parseDouble(formName.get(groupPosition).getTotalAmountPaid()));


        if (!formName.get(groupPosition).getPath().isEmpty() && formName.get(groupPosition).getPath() != null) {
            Glide.with(context).load(new File(formName.get(groupPosition).getPath())).into(profilePicture);
        } else {
            Glide.with(context).load(context.getResources().getDrawable(R.drawable.user_img)).into(profilePicture);
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
}
