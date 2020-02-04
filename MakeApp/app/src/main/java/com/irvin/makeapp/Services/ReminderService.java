package com.irvin.makeapp.Services;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.irvin.makeapp.Activities.PaymentActivity;
import com.irvin.makeapp.Activities.ReminderActivity;
import com.irvin.makeapp.Database.DatabaseCustomer;
import com.irvin.makeapp.Database.RemindersDbAdapter;
import com.irvin.makeapp.R;

public class ReminderService extends WakeReminderIntentService {

    RemindersDbAdapter remindersDbAdapter = new RemindersDbAdapter(this);
    DatabaseCustomer databaseCustomer = new DatabaseCustomer(this);

    public ReminderService() {
        super("ReminderService");
    }

    @Override
    void doReminderWork(Intent intent) {
        Log.e("ReminderService", "Doing work.");
        Long rowId = intent.getExtras().getLong(RemindersDbAdapter.KEY_ROWID);

        createNotificationChannel(rowId);


    }


    private void createNotificationChannel(Long rowId) {

           // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("REMINDER", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, ReminderActivity.class);
        intent.putExtra(RemindersDbAdapter.KEY_ROWID, rowId);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, Integer.parseInt(Long.toString(rowId)), intent, PendingIntent.FLAG_ONE_SHOT);

        Cursor cursor = remindersDbAdapter.fetchReminder(rowId);
        String customerName = databaseCustomer.getAllCustomer(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RemindersDbAdapter.KEY_CUSTOMER_ID)))).getFullName() != null ?
                databaseCustomer.getAllCustomer(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RemindersDbAdapter.KEY_CUSTOMER_ID)))).getFullName() : "TAP TO VIEW";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "REMINDER")
                .setCategory(Notification.CATEGORY_ALARM)
                .setTicker("PLEASE")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(cursor.getString(cursor.getColumnIndex(RemindersDbAdapter.KEY_TITLE)))
                .setContentText(cursor.getString(cursor.getColumnIndex(RemindersDbAdapter.KEY_BODY)))
                .setSubText(customerName)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        //startForeground(Integer.parseInt(Long.toString(rowId)) , builder.build());
        notificationManager.notify(Integer.parseInt(Long.toString(rowId)), builder.build());

    }
}
