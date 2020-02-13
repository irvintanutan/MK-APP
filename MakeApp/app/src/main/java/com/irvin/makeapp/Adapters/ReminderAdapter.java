package com.irvin.makeapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Models.Reminder;
import com.irvin.makeapp.R;
import com.irvin.makeapp.Services.Logger;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

        try {

            viewHolder.title.setText(reminders.get(position).getKEY_TITLE());
            viewHolder.description.setText(reminders.get(position).getKEY_BODY());
            DateFormat formatter2 = new SimpleDateFormat("MMM. dd, yyyy");
            Date date2 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse(reminders.get(position).getKEY_DATE_TIME());
            viewHolder.date.setText(formatter2.format(date2));

        } catch (ParseException e) {
            e.printStackTrace();
            Logger.CreateNewEntry(e , new File(mContext.getExternalFilesDir("") , ModGlobal.logFile));
        }

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