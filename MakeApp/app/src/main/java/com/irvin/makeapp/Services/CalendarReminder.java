package com.irvin.makeapp.Services;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.irvin.makeapp.Models.Reminder;

import java.util.Calendar;
import java.util.TimeZone;

public class CalendarReminder {

    public CalendarReminder(Context context) {
        this.context = context;
    }

    private Context context;



    public static String addEvent(Reminder reminder , Calendar mCalendar , String fullName,  Activity activity ,Long calID) {
        String eventId = "";
        ContentResolver cr = activity.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, mCalendar.getTimeInMillis());
        values.put(CalendarContract.Events.DTEND, mCalendar.getTimeInMillis());
        values.put(CalendarContract.Events.TITLE, reminder.getKEY_TITLE());
        values.put(CalendarContract.Events.DESCRIPTION, fullName + "\n" + reminder.getKEY_BODY());
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());


        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            eventId = uri.getLastPathSegment();

            ContentValues reminders = new ContentValues();
            reminders.put(CalendarContract.Reminders.EVENT_ID, eventId);
            reminders.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            reminders.put(CalendarContract.Reminders.MINUTES, 60);


            cr.insert(CalendarContract.Reminders.CONTENT_URI, reminders);
            reminders = new ContentValues();
            reminders.put(CalendarContract.Reminders.EVENT_ID, eventId);
            reminders.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            reminders.put(CalendarContract.Reminders.MINUTES, 60 * 24);


            cr.insert(CalendarContract.Reminders.CONTENT_URI, reminders);

        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_CALENDAR}, 1);
        }

        return eventId;

    }

    public static void deleteEvent(long eventID , Context context) {
        Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        int rows = context.getContentResolver().delete(deleteUri, null, null);
        Log.i("Calendar", "Rows deleted: " + rows);
    }

    public static void updateEvent(long eventID , Context context , Calendar mCalendar , Reminder reminder , String fullName) {
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, mCalendar.getTimeInMillis());
        values.put(CalendarContract.Events.DTEND, mCalendar.getTimeInMillis());
        values.put(CalendarContract.Events.TITLE, reminder.getKEY_TITLE());
        values.put(CalendarContract.Events.DESCRIPTION, fullName + "\n" + reminder.getKEY_BODY());
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        Uri updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        int rows = context.getContentResolver().update(updateUri, values, null, null);
        Log.i("Calendar", "Rows updated: " + rows);
    }


}
