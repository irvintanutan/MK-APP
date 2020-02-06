package com.irvin.makeapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.irvin.makeapp.Models.Reminder;
import com.irvin.makeapp.R;

import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {
    private List<Reminder> reminders;
    private Context mContext = null;

    public ReminderAdapter(List<Reminder> reminders, Context mContext) {
        this.reminders = reminders;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reminder_card_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {


        viewHolder.title.setText(reminders.get(position).getKEY_TITLE());
        viewHolder.description.setText(reminders.get(position).getKEY_BODY());
        viewHolder.date.setText(reminders.get(position).getKEY_DATE_TIME());
    }

    public void update(List<Reminder> reminders) {
        this.reminders = reminders;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return reminders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, description, date;

        public ViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            date = view.findViewById(R.id.date);

        }
    }

}