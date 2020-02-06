package com.irvin.makeapp.Activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.irvin.makeapp.Adapters.ReminderAdapter;
import com.irvin.makeapp.Adapters.SearchCustomerAdapter;
import com.irvin.makeapp.Constant.ClickListener;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Constant.RecyclerTouchListener;
import com.irvin.makeapp.Database.DatabaseCustomer;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Database.RemindersDbAdapter;
import com.irvin.makeapp.Models.CustomerModel;
import com.irvin.makeapp.Models.Reminder;
import com.irvin.makeapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReminderActivity extends AppCompatActivity {

    FloatingActionButton fab;
    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    DatabaseCustomer databaseCustomer = new DatabaseCustomer(this);
    RemindersDbAdapter remindersDbAdapter = new RemindersDbAdapter(this);

    private static final int DATE_PICKER_DIALOG = 0;
    private static final int TIME_PICKER_DIALOG = 1;
    List<CustomerModel> customerModelList;
    List<CustomerModel> tempCust = new ArrayList<>();
    private Calendar mCalendar;
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "kk:mm";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm:ss";

    private int mYear, mMonth, mDay, mHour, mMinute;

    EditText editTextTopic, editTextBody;
    ReminderAdapter reminderAdapter;
    RecyclerView recyclerView;
    List<Reminder> reminders;
    Button date;
    Button time;
    Button customer;
    String topic, body, strDate, strTime;
    private Long mRowId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        @SuppressLint("WrongViewCast") Toolbar tb = findViewById(R.id.app_bar);
        setSupportActionBar(tb);
        final ActionBar ab = getSupportActionBar();
        mCalendar = Calendar.getInstance();
        ab.setTitle("Reminders");
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(true); // disable the default title element here (for centered title)



        //init();
    }

   /* private void init() {
        fab = findViewById(R.id.floating_action_button);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopUpReminder(null);
            }
        });


        recyclerView = findViewById(R.id.reminder_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        reminders = new ArrayList<>();
        reminders = remindersDbAdapter.getAllReminders();

        reminderAdapter = new ReminderAdapter(reminders, this);
        recyclerView.setAdapter(reminderAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {

            @Override
            public void onClick(View view, int position) {

                Reminder reminder = reminders.get(position);
                mRowId = Long.parseLong(reminder.getKEY_ROWID());
                PopUpReminder(reminder);

            }

            @Override
            public void onLongClick(View view, int position) {

            }


        }));


    }*/



    private void populateFields()  {

        if (mRowId != null) {

    /*        Reminder reminder = remindersDbAdapter.getAllReminders(mRowId);

            PopUpReminder(reminder);*/
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ReminderActivity.this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(ReminderActivity.this, MainActivity.class));
            finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        }

        return super.onOptionsItemSelected(item);
    }


    private void showDatePicker() {

        // Get Current Date
        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
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
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
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

/*
    void PopUpReminder(Reminder reminder) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alertLayout = inflater.inflate(R.layout.layout_reminder, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        date = alertLayout.findViewById(R.id.date);
        time = alertLayout.findViewById(R.id.time);
        customer = alertLayout.findViewById(R.id.customer);
        editTextTopic = alertLayout.findViewById(R.id.topic);
        editTextBody = alertLayout.findViewById(R.id.body);


        if (reminder != null){

            customer.setText(databaseCustomer.getAllCustomer(Integer.parseInt(reminder.getKEY_CUSTOMER_ID())).getFullName());
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
                Log.e("ReminderEditActivity", e.getMessage(), e);
            }

            updateDateButtonText();
            updateTimeButtonText();
        }

        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchCustomer();
            }
        });

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

                        long id = mDbHelper.createReminder(editTextTopic.getText().toString(), editTextBody.getText().toString()
                                , reminderDateTime, Integer.toString(ModGlobal.customerId));
                        if (id > 0) {
                            mRowId = id;
                        }
                    } else {
                        mDbHelper.updateReminder(mRowId, editTextTopic.getText().toString(), editTextBody.getText().toString()
                                , reminderDateTime, Integer.toString(ModGlobal.customerId));
                    }

                    reminders.clear();
                    reminders = remindersDbAdapter.getAllReminders();

                    reminderAdapter = new ReminderAdapter(reminders, ReminderActivity.this);
                    recyclerView.setAdapter(reminderAdapter);
                    reminderAdapter.notifyDataSetChanged();

                    new ReminderManager(ReminderActivity.this).setReminder(mRowId, mCalendar);
                    mRowId = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        builder.show();
    }*/


    @SuppressLint("RestrictedApi")
    void searchCustomer() {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_search_customer, null);


        final SearchCustomerAdapter customerAdapter;
        RecyclerView recyclerView;
        final EditText searchCustomer = alertLayout.findViewById(R.id.search);
        final FloatingActionButton fab = alertLayout.findViewById(R.id.floating_action_button);
        fab.setVisibility(View.GONE);
        ImageView done = alertLayout.findViewById(R.id.done);

        recyclerView = alertLayout.findViewById(R.id.customer_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        customerModelList = databaseCustomer.getAllCustomer();
        ModGlobal.customerModelList = customerModelList;
        tempCust = customerModelList;

        customerAdapter = new SearchCustomerAdapter(customerModelList, this);
        recyclerView.setAdapter(customerAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {

            @Override
            public void onClick(View view, int position) {
                ModGlobal.position = position;
                customerAdapter.notifyDataSetChanged();

                ModGlobal.customerId = tempCust.get(position).getId();
                ModGlobal.customerName = tempCust.get(position).getFirstName() + " " + tempCust.get(position).getLastName();


            }

            @Override
            public void onLongClick(View view, final int position) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                };

            }
        }));

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ModGlobal.customerName.isEmpty()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ReminderActivity.this);
                    builder.setTitle("Alert");
                    builder.setIcon(getResources().getDrawable(R.drawable.warning));
                    builder.setMessage("No Customer Selected");

                    builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    customer.setText(ModGlobal.customerName);
                    dialog.dismiss();
                }
            }
        });

        searchCustomer.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                tempCust = new ArrayList();
                String text = searchCustomer.getText().toString();
                if (text.equals(""))
                    tempCust = ModGlobal.customerModelList;
                else {
                    for (CustomerModel p : ModGlobal.customerModelList) {
                        //or use .contains(text)
                        if (p.getFirstName().toLowerCase().contains(text.toLowerCase()) ||
                                p.getLastName().toLowerCase().contains(text.toLowerCase())) {
                            tempCust.add(p);
                        }
                    }
                }
                //update recyclerview
                customerAdapter.update(tempCust);
            }
        });


    }
}
