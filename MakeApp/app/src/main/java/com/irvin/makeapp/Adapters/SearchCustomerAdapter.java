package com.irvin.makeapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Models.CustomerModel;
import com.irvin.makeapp.R;

import java.io.File;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchCustomerAdapter extends RecyclerView.Adapter<SearchCustomerAdapter.ViewHolder> {
    private List<CustomerModel> customerModelList;
    private Context mContext = null;

    public SearchCustomerAdapter(List<CustomerModel> customerModelList, Context mContext) {
        this.customerModelList = customerModelList;
        this.mContext = mContext;
    }

    @Override
    public SearchCustomerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_search_customer, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchCustomerAdapter.ViewHolder viewHolder, int position) {

        if(ModGlobal.position == position)
            viewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
        else
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));


        if (!customerModelList.get(position).getPhotoUrl().isEmpty() && customerModelList.get(position).getPhotoUrl() != null) {
            Log.e("asd" , customerModelList.get(position).getPhotoUrl());
            Glide.with(mContext).load(new File(customerModelList.get(position).getPhotoUrl())).into(viewHolder.profilePicture);
        }else {
            Glide.with(mContext).load(mContext.getResources().getDrawable(R.drawable.user_img)).into(viewHolder.profilePicture);
        }
        viewHolder.fullName.setText(customerModelList.get(position).getFirstName() + " " + customerModelList.get(position).getMiddleName() + " " +
                " " + customerModelList.get(position).getLastName());

    }

    public void update(List<CustomerModel> customerModelList) {
        this.customerModelList = customerModelList;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return customerModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView fullName;
        CircleImageView profilePicture;
        CardView cv;

        public ViewHolder(View view) {
            super(view);
            cv = view.findViewById(R.id.cv);
            fullName = view.findViewById(R.id.fullName);
            profilePicture = view.findViewById(R.id.profilePicture);

        }
    }

}