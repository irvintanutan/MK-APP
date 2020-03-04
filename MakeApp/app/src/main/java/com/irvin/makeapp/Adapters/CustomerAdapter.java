package com.irvin.makeapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hbb20.GThumb;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Models.CustomerModel;
import com.irvin.makeapp.R;

import java.io.File;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {
    private List<CustomerModel> customerModelList;
    private Context mContext = null;

    public CustomerAdapter(List<CustomerModel> customerModelList, Context mContext) {
        this.customerModelList = customerModelList;
        this.mContext = mContext;
    }

    @Override
    public CustomerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_customer, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerAdapter.ViewHolder viewHolder, int position) {

        if (!customerModelList.get(position).getPhotoUrl().isEmpty() && customerModelList.get(position).getPhotoUrl() != null) {
            Log.e("asd", customerModelList.get(position).getPhotoUrl());
            viewHolder.profilePicture.setVisibility(View.GONE);
            viewHolder.profilePicture2.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(new File(customerModelList.get(position).getPhotoUrl())).into(viewHolder.profilePicture2);
        } else {
            viewHolder.profilePicture2.setVisibility(View.GONE);
            viewHolder.profilePicture.setVisibility(View.VISIBLE);
            viewHolder.profilePicture.applyMultiColor();
            viewHolder.profilePicture.loadThumbForName(customerModelList.get(position).getPhotoUrl(), customerModelList.get(position).getFirstName(),
                    customerModelList.get(position).getLastName());
        }


        try {
            String[] splitDateValues = customerModelList.get(position).getBirthday().split("-");
            int year = Integer.parseInt(splitDateValues[0]);
            int month = Integer.parseInt(splitDateValues[1]) - 1;
            int day = Integer.parseInt(splitDateValues[2]);
            viewHolder.age.setText("Age : " + ModGlobal.getAge(year, month, day));
        } catch (Exception e) {
            viewHolder.age.setText("Age : " + customerModelList.get(position).getAge());
        }


        viewHolder.fullName.setText(ModGlobal.toTitleCase(customerModelList.get(position).getFirstName() +
                " " + customerModelList.get(position).getLastName()));
        viewHolder.contactNumber.setText("Contact # : " + customerModelList.get(position).getContactNumber());
        viewHolder.skinType.setText(customerModelList.get(position).getSkinType());
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

        TextView fullName, age, contactNumber, skinType;
        GThumb profilePicture;
        CircleImageView profilePicture2;

        public ViewHolder(View view) {
            super(view);

            fullName = view.findViewById(R.id.fullName);
            age = view.findViewById(R.id.age);
            contactNumber = view.findViewById(R.id.contactNumber);
            skinType = view.findViewById(R.id.skinType);
            profilePicture = view.findViewById(R.id.profilePicture);
            profilePicture2 = view.findViewById(R.id.profilePicture2);

        }
    }

}