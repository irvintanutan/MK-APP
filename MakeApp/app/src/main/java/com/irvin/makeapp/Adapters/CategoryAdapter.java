package com.irvin.makeapp.Adapters;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.irvin.makeapp.Models.Category;
import com.irvin.makeapp.R;

import java.util.List;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<Category> form;
    private Context mContext;

    public CategoryAdapter(List<Category> form , Context mContext) {
        this.form = form;
        this.mContext = mContext;
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_category, viewGroup, false);
        return new ViewHolder(view);
    }

    public void update(List<Category> categories) {
        this.form = categories;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.ViewHolder viewHolder, int i) {
        viewHolder.name.setText(form.get(i).getName());

        if (form.get(i).isClicked()){
            viewHolder.name.setTextColor(Color.WHITE);
            viewHolder.name.setBackground(mContext.getResources().getDrawable(R.drawable.transparent_bg_bordered2));
        }else {
            viewHolder.name.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
            viewHolder.name.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            viewHolder.name.setBackground(mContext.getResources().getDrawable(R.drawable.transparent_bg_bordered));
        }

    }

    @Override
    public int getItemCount() {
        return form.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;


        public ViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.category);
        }
    }

}