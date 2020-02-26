package com.irvin.makeapp.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hbb20.GThumb;
import com.irvin.makeapp.Adapters.ReminderAdapter;
import com.irvin.makeapp.Constant.ClickListener;
import com.irvin.makeapp.Constant.MarshMallowPermission;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Constant.MultiSelectionSpinner;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerProfileViewActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    DatabaseCustomer databaseCustomer = new DatabaseCustomer(this);
    CustomerModel customerModel = new CustomerModel();
    EditText  skinType, skinTone , skinConcern , interest;

    GThumb profilePicture;
    CircleImageView profilePicture2;
    TextView customerName , phoneNumber , email , location;
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

    MarshMallowPermission marshMallowPermission;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile_view);


        marshMallowPermission = new MarshMallowPermission(this);
        customerModel = databaseCustomer.getAllCustomer(ModGlobal.customerId);

        init();

    }


    private void init() {

        profilePicture = findViewById(R.id.profilePicture);
        profilePicture2 = findViewById(R.id.profilePicture2);
        customerName = findViewById(R.id.customerName);
        phoneNumber = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.email);
        location = findViewById(R.id.address);
        skinType = findViewById(R.id.skinType);
        skinTone = findViewById(R.id.skinTone);
        skinConcern = findViewById(R.id.skinConcern);
        interest = findViewById(R.id.interest);

        if (!customerModel.getPhotoUrl().isEmpty() && customerModel.getPhotoUrl() != null) {
            profilePicture.setVisibility(View.GONE);
            profilePicture2.setVisibility(View.VISIBLE);
            Glide.with(this).load(new File(customerModel.getPhotoUrl())).into(profilePicture2);
        } else {
            profilePicture2.setVisibility(View.GONE);
            profilePicture.setVisibility(View.VISIBLE);
            profilePicture.applyMultiColor();
            profilePicture.loadThumbForName(customerModel.getPhotoUrl(), customerModel.getFirstName(),
                    customerModel.getLastName());
        }


        customerName.setText(customerModel.getFullName());
        phoneNumber.setText(customerModel.getContactNumber());
        email.setText(customerModel.getEmail());
        location.setText(customerModel.getAddress());
        skinType.setText(customerModel.getSkinType());
        skinConcern.setText(customerModel.getSkinConcern());
        skinTone.setText(customerModel.getSkinTone());
        interest.setText(customerModel.getInterests());

        Button rem = findViewById(R.id.reminders);
        TextView followUp = findViewById(R.id.followUp);
        if (!ModGlobal.isCreateNew) {
            recyclerView = findViewById(R.id.reminder_view);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            reminders = new ArrayList<>();
            reminders = databaseHelper.getAllReminders(Integer.toString(ModGlobal.customerId));

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


                    final Reminder reminder = reminders.get(position);

                    AlertDialog.Builder builder = new AlertDialog.Builder(CustomerProfileViewActivity.this);

                    builder.setTitle("Confirm");
                    builder.setIcon(getResources().getDrawable(R.drawable.confirmation));
                    builder.setMessage("Are you sure you want to delete " + reminder.getKEY_TITLE() + " reminder ?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            CalendarReminder.deleteEvent(Long.parseLong(reminder.getKEY_EVENT_ID()), CustomerProfileViewActivity.this);
                            databaseHelper.deleteReminder(Long.parseLong(reminder.getKEY_ROWID()));
                            reminders.clear();
                            reminders = databaseHelper.getAllReminders(Integer.toString(ModGlobal.customerId));

                            reminderAdapter = new ReminderAdapter(reminders, CustomerProfileViewActivity.this);
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
            followUp.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(CustomerProfileViewActivity.this, CustomerActivity.class));
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    public void edit(View view) {
        ModGlobal.isCreateNew = false;
        ModGlobal.customerId = customerModel.getId();
        ModGlobal.customerName = customerModel.getFullName();
        Intent i = new Intent(CustomerProfileViewActivity.this, CustomerDetailsActivity.class);
        i.putExtra("toolBarTitle" , "");
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }

    public void reminder(View view) {
        MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);

        if (marshMallowPermission.checkPermissionForWriteCalendar()) {
            PopUpReminder(null);
        } else {
            marshMallowPermission.requestPermissionForWriteCalendar();
        }
    }


    void PopUpReminder(final Reminder reminder) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alertLayout = inflater.inflate(R.layout.layout_reminder, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

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
                Logger.CreateNewEntry(e, new File(getExternalFilesDir(""), ModGlobal.logFile));
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
                                CustomerProfileViewActivity.this, Long.parseLong("1"));

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

                            CalendarReminder.updateEvent(Long.parseLong(reminder.getKEY_EVENT_ID()), CustomerProfileViewActivity.this,
                                    mCalendar, new Reminder(editTextTopic.getText().toString(),
                                            Integer.toString(ModGlobal.customerId), editTextBody.getText().toString(),
                                            reminderDateTime, mRowId.toString(), eventId, reminder.getKEY_INVOICE_ID()), customerModel.getFullName());

                        } catch (Exception e) {
                            e.printStackTrace();
                            Logger.CreateNewEntry(e, new File(getExternalFilesDir(""), ModGlobal.logFile));
                        }

                    }

                    reminders.clear();
                    reminders = databaseHelper.getAllReminders(Integer.toString(ModGlobal.customerId));

                    reminderAdapter = new ReminderAdapter(reminders, CustomerProfileViewActivity.this);
                    recyclerView.setAdapter(reminderAdapter);
                    reminderAdapter.notifyDataSetChanged();


                    mRowId = null;
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.CreateNewEntry(e, new File(getExternalFilesDir(""), ModGlobal.logFile));
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

    private void callCustomer() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + customerModel.getContactNumber()));

        if (ActivityCompat.checkSelfPermission(CustomerProfileViewActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
        }
        startActivity(callIntent);
    }

    private void messageCustomer() {

        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setType("vnd.android-dir/mms-sms");
            i.setData(Uri.parse("smsto:" + customerModel.getContactNumber()));
            startActivity(i);
        } catch (Exception e) {
            Logger.CreateNewEntry(e, new File(getExternalFilesDir(""), ModGlobal.logFile));
            Toast.makeText(this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
        }
    }



    public void invoice(View view) {

        Intent intent = new Intent(CustomerProfileViewActivity.this, SalesInvoiceProductActivity.class);
        startActivity(intent);
        finish();

        ModGlobal.ProductModelListCopy.clear();
        ModGlobal.stockIns.clear();
        ModGlobal.ProductModelList.clear();

    }

    public void message(View view) {
        messageCustomer();
    }

    public void call(View view) {
        if (!marshMallowPermission.checkPermissionForCallPhone()) {
            marshMallowPermission.requestPermissionForCallPhone();
        } else
            callCustomer();
    }
}
