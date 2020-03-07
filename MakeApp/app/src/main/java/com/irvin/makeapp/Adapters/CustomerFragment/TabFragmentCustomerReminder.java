package com.irvin.makeapp.Adapters.CustomerFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.irvin.makeapp.Adapters.ReminderAdapter;
import com.irvin.makeapp.Constant.ClickListener;
import com.irvin.makeapp.Constant.MarshMallowPermission;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Constant.RecyclerTouchListener;
import com.irvin.makeapp.Database.DatabaseCustomer;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Models.CustomerModel;
import com.irvin.makeapp.Models.Reminder;
import com.irvin.makeapp.R;
import com.irvin.makeapp.Services.CalendarReminder;
import com.irvin.makeapp.Services.Logger;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 *
 * @author irvin
 * @date 2/7/17
 */
public class TabFragmentCustomerReminder extends Fragment {
    View view;

    DatabaseHelper databaseHelper ;
    DatabaseCustomer databaseCustomer ;
    CustomerModel customerModel = new CustomerModel();


    ReminderAdapter reminderAdapter;
    RecyclerView recyclerView;
    List<Reminder> reminders;
    EditText editTextTopic, editTextBody;
    Button date;
    Button time;
    Button customer;
    String topic, body, strDate, strTime;
    String eventId = "";
    private Long mRowId;
    private Calendar mCalendar;
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "kk:mm";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm:ss";
    private int mYear, mMonth, mDay, mHour, mMinute;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_customer_reminder, container, false);
        databaseHelper = new DatabaseHelper(getActivity());
        databaseCustomer = new DatabaseCustomer(getActivity());

        Button rem = view.findViewById(R.id.reminders);
        if (!ModGlobal.isCreateNew) {



            recyclerView = view.findViewById(R.id.reminder_view);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            reminders = new ArrayList<>();
            reminders = databaseHelper.getAllReminders(Integer.toString(ModGlobal.customerId));

            reminderAdapter = new ReminderAdapter(reminders, getActivity());
            recyclerView.setAdapter(reminderAdapter);
            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {

                @Override
                public void onClick(View view, int position) {

                    Reminder reminder = reminders.get(position);
                    mRowId = Long.parseLong(reminder.getKEY_ROWID());
                    PopUpReminder(reminder);

                }

                @Override
                public void onLongClick(View view, int position) {


                    final Reminder reminder = reminders.get(position);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setTitle("Confirm");
                    builder.setIcon(getResources().getDrawable(R.drawable.confirmation));
                    builder.setMessage("Are you sure you want to delete " + reminder.getKEY_TITLE() + " reminder ?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            CalendarReminder.deleteEvent(Long.parseLong(reminder.getKEY_EVENT_ID()), getActivity());
                            databaseHelper.deleteReminder(Long.parseLong(reminder.getKEY_ROWID()));
                            reminders.clear();
                            reminders = databaseHelper.getAllReminders(Integer.toString(ModGlobal.customerId));

                            reminderAdapter = new ReminderAdapter(reminders, getActivity());
                            recyclerView.setAdapter(reminderAdapter);
                            reminderAdapter.notifyDataSetChanged();

                        }

                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                }


            }));
        } else {
            rem.setVisibility(View.GONE);
        }

        rem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MarshMallowPermission marshMallowPermission = new MarshMallowPermission(getActivity());

                if (marshMallowPermission.checkPermissionForWriteCalendar()) {
                    PopUpReminder(null);
                } else {
                    marshMallowPermission.requestPermissionForWriteCalendar();
                }
            }
        });

        return view;
    }


    void PopUpReminder(final Reminder reminder) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alertLayout = inflater.inflate(R.layout.layout_reminder, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        date = alertLayout.findViewById(R.id.date);
        time = alertLayout.findViewById(R.id.time);
        editTextTopic = alertLayout.findViewById(R.id.topic);
        editTextBody = alertLayout.findViewById(R.id.body);
        mCalendar = Calendar.getInstance();

        if (reminder != null) {

            editTextTopic.setText(reminder.getKEY_TITLE());
            editTextBody.setText(reminder.getKEY_BODY());
            // Get the date from the database and format it for our use.
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
            Date date;
            try {
                String dateString = reminder.getKEY_DATE_TIME();
                date = dateTimeFormat.parse(dateString);
                mCalendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
                Logger.CreateNewEntry(e, new File(getActivity().getExternalFilesDir(""), ModGlobal.logFile));
                Log.e("ReminderEditActivity", e.getMessage(), e);
            }

            updateDateButtonText();
            updateTimeButtonText();
        }


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

        builder.setView(alertLayout);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Set", null);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mRowId = null;
            }
        });

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
                    String reminderDateTime = dateTimeFormat.format(mCalendar.getTime());

                    if (mRowId == null) {

                        eventId = CalendarReminder.addEvent(new Reminder(editTextTopic.getText().toString(),
                                        Integer.toString(ModGlobal.customerId), editTextBody.getText().toString(),
                                        reminderDateTime, "", eventId, ""), mCalendar, customerModel.getFullName(),
                                getActivity(), Long.parseLong("1"), false);

                        long id = databaseHelper.createReminder(new Reminder(editTextTopic.getText().toString(),
                                Integer.toString(ModGlobal.customerId), editTextBody.getText().toString(),
                                reminderDateTime, "", eventId, ""));
                        if (id > 0) {
                            mRowId = id;
                        }
                    } else {
                        databaseHelper.updateReminder(new Reminder(editTextTopic.getText().toString(),
                                Integer.toString(ModGlobal.customerId), editTextBody.getText().toString(),
                                reminderDateTime, mRowId.toString(), eventId, reminder.getKEY_INVOICE_ID()), mRowId.toString());
                        try {

                            CalendarReminder.updateEvent(Long.parseLong(reminder.getKEY_EVENT_ID()), getActivity() ,
                                    mCalendar, new Reminder(editTextTopic.getText().toString(),
                                            Integer.toString(ModGlobal.customerId), editTextBody.getText().toString(),
                                            reminderDateTime, mRowId.toString(), eventId, reminder.getKEY_INVOICE_ID()), customerModel.getFullName());

                        } catch (Exception e) {
                            e.printStackTrace();
                            Logger.CreateNewEntry(e, new File(getActivity().getExternalFilesDir(""), ModGlobal.logFile));
                        }

                    }

                    reminders.clear();
                    reminders = databaseHelper.getAllReminders(Integer.toString(ModGlobal.customerId));

                    reminderAdapter = new ReminderAdapter(reminders, getActivity());
                    recyclerView.setAdapter(reminderAdapter);
                    reminderAdapter.notifyDataSetChanged();


                    mRowId = null;
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.CreateNewEntry(e, new File(getActivity().getExternalFilesDir(""), ModGlobal.logFile));
                }

            }
        });

        builder.show();
    }


    private void showDatePicker() {

        // Get Current Date
        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mCalendar.set(Calendar.YEAR, year);
                        mCalendar.set(Calendar.MONTH, monthOfYear);
                        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateButtonText();

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void showTimePicker() {

        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        mCalendar.set(Calendar.MINUTE, minute);
                        updateTimeButtonText();
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void updateTimeButtonText() {
        // Set the time button text based upon the value from the database
        SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
        String timeForButton = timeFormat.format(mCalendar.getTime());
        strTime = timeForButton;
        time.setText(timeForButton);
    }

    private void updateDateButtonText() {
        // Set the date button text based upon the value from the database
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String dateForButton = dateFormat.format(mCalendar.getTime());
        strDate = dateForButton;
        date.setText(dateForButton);
    }

}
