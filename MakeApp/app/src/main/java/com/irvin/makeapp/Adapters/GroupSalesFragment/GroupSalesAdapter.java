package com.irvin.makeapp.Adapters.GroupSalesFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Database.DatabaseGroupSales;
import com.irvin.makeapp.Models.GroupSalesModel;
import com.irvin.makeapp.R;
import com.irvin.makeapp.Services.Logger;
import com.white.progressview.HorizontalProgressView;

import java.io.File;
import java.util.List;

/**
 * @author irvin
 */
public class GroupSalesAdapter extends RecyclerView.Adapter<GroupSalesAdapter.ViewHolder> {
    private List<GroupSalesModel> groupSalesModels;
    private Context mContext = null;
    DatabaseGroupSales databaseGroupSales = null;

    public GroupSalesAdapter(List<GroupSalesModel> groupSalesModels, Context mContext) {
        this.groupSalesModels = groupSalesModels;
        this.mContext = mContext;
        databaseGroupSales = new DatabaseGroupSales(mContext);
    }

    @Override
    public GroupSalesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_main_form_group_sales, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupSalesAdapter.ViewHolder viewHolder, final int position) {
        try {

            GroupSalesModel groupSalesModel = groupSalesModels.get(position);

            viewHolder.fullName.setText(groupSalesModel.getName());

        } catch (Exception e) {
            e.printStackTrace();
            Logger.CreateNewEntry(mContext ,e , new File(mContext.getExternalFilesDir("") , ModGlobal.logFile));
        }
    }

    public void update(List<GroupSalesModel> groupSalesModels) {
        this.groupSalesModels = groupSalesModels;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return groupSalesModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView fullName, totalAmount, totalAmountPaid, totalBalance;
        HorizontalProgressView progressBar;


        public ViewHolder(View view) {
            super(view);

            fullName = view.findViewById(R.id.fullName);
            totalAmount = view.findViewById(R.id.totalAmount);
            totalAmountPaid = view.findViewById(R.id.totalAmountPaid);
            totalBalance = view.findViewById(R.id.totalBalance);
            progressBar = view.findViewById(R.id.progressBar);

        }
    }


}