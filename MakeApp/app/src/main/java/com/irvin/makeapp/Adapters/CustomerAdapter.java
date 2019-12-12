package com.irvin.makeapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.irvin.makeapp.Models.CustomerModel;
import com.irvin.makeapp.R;

import java.io.File;
import java.util.List;

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
            Log.e("asd" , customerModelList.get(position).getPhotoUrl());
            Glide.with(mContext).load(new File(customerModelList.get(position).getPhotoUrl())).into(viewHolder.profilePicture);
        }else {
            Glide.with(mContext).load(mContext.getResources().getDrawable(R.drawable.user_img)).into(viewHolder.profilePicture);
        }
        viewHolder.fullName.setText(customerModelList.get(position).getFirstName() + " " + customerModelList.get(position).getMiddleName() + " " +
                " " + customerModelList.get(position).getLastName());
        viewHolder.age.setText("Age : " + customerModelList.get(position).getAge());
        viewHolder.contactNumber.setText("Contact # : " + customerModelList.get(position).getContactNumber());
        viewHolder.skinType.setText("Skin Type : " + customerModelList.get(position).getSkinType());
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
        CircleImageView profilePicture;

        public ViewHolder(View view) {
            super(view);

            fullName = view.findViewById(R.id.fullName);
            age = view.findViewById(R.id.age);
            contactNumber = view.findViewById(R.id.contactNumber);
            skinType = view.findViewById(R.id.skinType);
            profilePicture = view.findViewById(R.id.profilePicture);

        }
    }

}