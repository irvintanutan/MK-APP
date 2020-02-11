package com.irvin.makeapp.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.irvin.makeapp.Adapters.ReminderAdapter;
import com.irvin.makeapp.Constant.ClickListener;
import com.irvin.makeapp.Constant.MarshMallowPermission;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Constant.MultiSelectionSpinner;
import com.irvin.makeapp.Constant.RecyclerTouchListener;
import com.irvin.makeapp.Database.DatabaseCustomer;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Database.RemindersDbAdapter;
import com.irvin.makeapp.Models.CustomerModel;
import com.irvin.makeapp.Models.Reminder;
import com.irvin.makeapp.R;
import com.irvin.makeapp.Services.CalendarReminder;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class CustomerDetailsActivity extends AppCompatActivity implements MultiSelectionSpinner.OnMultipleItemsSelectedListener {
    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    DatabaseCustomer databaseCustomer = new DatabaseCustomer(this);
    CustomerModel customerModel = new CustomerModel();
    String mCurrentPhotoPath = "";
    private static int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_REQUEST = 20;
    AppCompatImageView profilePicture;
    EditText firstName, middleName, lastName, address, email, age, occupation, contactNumber, bestTimeToBeContacted, referredBy, remarks;
    Button birthday;
    Spinner skinType, skinTone;
    MultiSelectionSpinner skinConcern, interest;
    Uri picUri;
    MarshMallowPermission marshMallowPermission;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;


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
    String eventId = "";
    private Long mRowId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        marshMallowPermission = new MarshMallowPermission(this);


        @SuppressLint("WrongViewCast") Toolbar tb = findViewById(R.id.app_bar);
        setSupportActionBar(tb);
        final ActionBar ab = getSupportActionBar();

        Intent intent = getIntent();


        ab.setTitle(intent.getStringExtra("toolBarTitle"));
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(true); // disable the default title element here (for centered title)


        init();

        permissions.add(CAMERA);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissionsToRequest = findUnAskedPermissions(permissions);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    private void init() {
        profilePicture = findViewById(R.id.profilePicture);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        middleName = findViewById(R.id.middleName);
        address = findViewById(R.id.address);
        birthday = findViewById(R.id.birthday);
        skinType = findViewById(R.id.skinType);
        skinConcern = findViewById(R.id.skinConcern);
        skinTone = findViewById(R.id.skinTone);
        interest = findViewById(R.id.interest);
        email = findViewById(R.id.email);
        occupation = findViewById(R.id.occupation);
        age = findViewById(R.id.age);
        bestTimeToBeContacted = findViewById(R.id.bestTimeToBeContacted);
        referredBy = findViewById(R.id.referredBy);
        contactNumber = findViewById(R.id.contactNumber);
        remarks = findViewById(R.id.remarks);


        email.addTextChangedListener(new TextWatcher() {

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
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    email.setError(null);
                } else email.setError("Invalid Email");
            }
        });

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.skin_type, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        skinType.setAdapter(adapter1);

        skinConcern.setItems(getResources().getStringArray(R.array.skin_concern));
        skinConcern.setListener(this);


        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.skin_tone, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        skinTone.setAdapter(adapter3);


        interest.setItems(getResources().getStringArray(R.array.interest));
        interest.setListener(this);

        if (!ModGlobal.isCreateNew) {

            customerModel = databaseCustomer.getAllCustomer(ModGlobal.customerId);
            if (!customerModel.getPhotoUrl().isEmpty() && customerModel.getPhotoUrl() != null) {
                Log.e("asd", customerModel.getPhotoUrl());
                Glide.with(getApplicationContext()).load(new File(customerModel.getPhotoUrl())).into(profilePicture);
            } else {
                Glide.with(getApplicationContext()).load(getApplication().getResources().getDrawable(R.drawable.user_img)).into(profilePicture);
            }
            mCurrentPhotoPath = customerModel.getPhotoUrl();
            firstName.setText(customerModel.getFirstName());
            lastName.setText(customerModel.getLastName());
            middleName.setText(customerModel.getMiddleName());
            address.setText(customerModel.getAddress());
            birthday.setText(customerModel.getBirthday());
            email.setText(customerModel.getEmail());
            occupation.setText(customerModel.getOccupation());
            age.setText(customerModel.getAge());
            bestTimeToBeContacted.setText(customerModel.getBestTimeToBeContacted());
            referredBy.setText(customerModel.getReferredBy());
            contactNumber.setText(customerModel.getContactNumber());
            remarks.setText(customerModel.getRemarks());

            if (customerModel.getSkinType().isEmpty()) {
                skinType.setSelection(0);
            } else {

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.skin_type, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                skinType.setAdapter(adapter);
                int spinnerPosition = adapter.getPosition(customerModel.getSkinType());
                skinType.setSelection(spinnerPosition);
            }

            if (customerModel.getSkinTone().isEmpty()) {
                skinTone.setSelection(0);
            } else {

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.skin_tone, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                skinTone.setAdapter(adapter);
                int spinnerPosition = adapter.getPosition(customerModel.getSkinTone());
                skinTone.setSelection(spinnerPosition);
            }

            List<String> skinConcernList = new ArrayList<>();
            skinConcernList = Arrays.asList(customerModel.getSkinConcern().split(","));
            List<String> interestList = new ArrayList<>();
            interestList = Arrays.asList(customerModel.getInterests().split(","));

            skinConcern.setSelection(skinConcernList);
            interest.setSelection(interestList);


        }

        Button rem = findViewById(R.id.reminders);
        TextView followUp = findViewById(R.id.followUp);
        if (!ModGlobal.isCreateNew) {
            recyclerView = findViewById(R.id.reminder_view);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            reminders = new ArrayList<>();
            reminders = databaseHelper.getAllReminders(Integer.toString(ModGlobal.customerId));

/*        Toast.makeText(getApplicationContext() ,  Integer.toString(reminders.size()) , Toast.LENGTH_SHORT).show();
        Log.e("asdasdasdasdas" , Integer.toString(reminders.size()));*/
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(CustomerDetailsActivity.this);

                    builder.setTitle("Confirm");
                    builder.setIcon(getResources().getDrawable(R.drawable.confirmation));
                    builder.setMessage("Are you sure you want to delete " + reminder.getKEY_TITLE() + " reminder ?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            CalendarReminder.deleteEvent(Long.parseLong(reminder.getKEY_EVENT_ID()), CustomerDetailsActivity.this);
                            databaseHelper.deleteReminder(Long.parseLong(reminder.getKEY_ROWID()));
                            reminders.clear();
                            reminders = databaseHelper.getAllReminders(Integer.toString(ModGlobal.customerId));

                            reminderAdapter = new ReminderAdapter(reminders, CustomerDetailsActivity.this);
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


    void goBack() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Warning");
        builder.setIcon(getResources().getDrawable(R.drawable.warning));
        builder.setMessage("Are you sure you want to Quit ? Your progress will not be saved.");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                // Do nothing
                dialog.dismiss();

                if (ModGlobal.isInSalesInvoice) {
                    startActivity(new Intent(CustomerDetailsActivity.this, SalesInvoiceProductActivity.class));
                    finish();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                } else {
                    startActivity(new Intent(CustomerDetailsActivity.this, CustomerActivity.class));
                    finish();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                }


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


    @Override
    public void onBackPressed() {

        goBack();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (ModGlobal.isCreateNew)
            getMenuInflater().inflate(R.menu.customer_add, menu);
        else
            getMenuInflater().inflate(R.menu.customer_view, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            goBack();
        } else if (item.getItemId() == R.id.action_save) {
            boolean ok = true;

            if (firstName.getText() == null || firstName.getText().toString().isEmpty()) {
                firstName.setError("First Name is Required");
                ok = false;
            }

            if (lastName.getText() == null || lastName.getText().toString().isEmpty()) {
                lastName.setError("Last Name is Required");
                ok = false;
            }

            if (contactNumber.getText() == null || contactNumber.getText().toString().isEmpty()) {
                contactNumber.setError("Contact Number is Required");
                ok = false;
            }

            if (age.getText() == null || age.getText().toString().isEmpty()) {
                age.setError("Age is Required");
                ok = false;
            }

            if (ok) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Confirm");
                builder.setIcon(getResources().getDrawable(R.drawable.confirmation));
                builder.setMessage("Are you sure you want to save customer data ?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        saveCustomer(ModGlobal.isCreateNew);

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
            } else {
                Toast.makeText(getApplicationContext(), "Some Fields Are Required", Toast.LENGTH_LONG).show();
            }
        } else if (item.getItemId() == R.id.action_call) {
            if (!marshMallowPermission.checkPermissionForCallPhone()) {
                marshMallowPermission.requestPermissionForCallPhone();
            } else
                callCustomer();

        } else if (item.getItemId() == R.id.action_message) {

            messageCustomer();

        }

        return super.onOptionsItemSelected(item);
    }

    private void callCustomer() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + customerModel.getContactNumber()));

        if (ActivityCompat.checkSelfPermission(CustomerDetailsActivity.this,
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
            Toast.makeText(this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveCustomer(boolean isCreateNew) {

        CustomerModel customerModel = new CustomerModel();
        customerModel.setFirstName(firstName.getText().toString() == null ? " " : firstName.getText().toString());
        customerModel.setMiddleName(middleName.getText().toString() == null ? " " : middleName.getText().toString());
        customerModel.setLastName(lastName.getText().toString() == null ? " " : lastName.getText().toString());
        customerModel.setAddress(address.getText().toString() == null ? " " : address.getText().toString());
        customerModel.setBirthday(birthday.getText().toString() == null ? " " : birthday.getText().toString());
        customerModel.setAge(age.getText().toString() == null ? " " : age.getText().toString());
        customerModel.setOccupation(occupation.getText().toString() == null ? " " : occupation.getText().toString());
        customerModel.setEmail(email.getText().toString() == null ? " " : email.getText().toString());
        customerModel.setContactNumber(contactNumber.getText().toString() == null ? " " : contactNumber.getText().toString());
        customerModel.setBestTimeToBeContacted(bestTimeToBeContacted.getText().toString() == null ? " " : bestTimeToBeContacted.getText().toString());
        customerModel.setReferredBy(referredBy.getText().toString() == null ? " " : referredBy.getText().toString());
        customerModel.setSkinType(skinType.getSelectedItem().toString() == null ? " " : skinType.getSelectedItem().toString());
        customerModel.setSkinConcern(skinConcern.getSelectedItemsAsString() == null ? " " : skinConcern.getSelectedItemsAsString());
        customerModel.setSkinTone(skinTone.getSelectedItem().toString() == null ? " " : skinTone.getSelectedItem().toString());
        customerModel.setInterests(interest.getSelectedItemsAsString() == null ? " " : interest.getSelectedItemsAsString());
        customerModel.setPhotoUrl(mCurrentPhotoPath == null ? " " : mCurrentPhotoPath);
        customerModel.setRemarks(remarks.getText().toString() == null ? " " : remarks.getText().toString());

        if (isCreateNew)
            databaseCustomer.addCustomer(customerModel);
        else
            databaseCustomer.updateCustomer(customerModel, ModGlobal.customerId);

        if (ModGlobal.isInSalesInvoice) {
            startActivity(new Intent(CustomerDetailsActivity.this, SalesInvoiceProductActivity.class));
            finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        } else {
            startActivity(new Intent(CustomerDetailsActivity.this, CustomerActivity.class));
            finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        }
    }

    public void dateTime(View view) {

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alertLayout = inflater.inflate(R.layout.datetime, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final DatePicker datePicker = alertLayout.findViewById(R.id.date_picker);

        builder.setView(alertLayout);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Set", null);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String append = "";
                String appendMonth = "";
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();

                if (month < 10) appendMonth = "0";

                String date = year + "-" + appendMonth + month + "-" + day;
                birthday.setText(date);

            }
        });

        builder.show();
    }

    public void profilePicture(View view) {
        startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);
    }


    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {
        // Toast.makeText(this.getApplicationContext(),"Selected Charmelets" + strings, Toast.LENGTH_LONG).show();


    }


    public Intent getPickImageChooserIntent() {

        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }


    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalFilesDir("");
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == IMAGE_RESULT) {

                String filePath = getImageFilePath(data);
                if (filePath != null) {
                    //Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                    mCurrentPhotoPath = filePath;
                    Glide.with(this).load(new File(mCurrentPhotoPath)).into(profilePicture);
                }
            }

        }

    }


    private String getImageFromFilePath(Intent data) {
        boolean isCamera = data == null || data.getData() == null;

        if (isCamera) return getCaptureImageOutputUri().getPath();
        else return getPathFromURI(data.getData());

    }

    public String getImageFilePath(Intent data) {
        return getImageFromFilePath(data);
    }

    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("pic_uri", picUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        picUri = savedInstanceState.getParcelable("pic_uri");
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
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
                                CustomerDetailsActivity.this, Long.parseLong("1"));

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

                            CalendarReminder.updateEvent(Long.parseLong(reminder.getKEY_EVENT_ID()), CustomerDetailsActivity.this,
                                    mCalendar, new Reminder(editTextTopic.getText().toString(),
                                            Integer.toString(ModGlobal.customerId), editTextBody.getText().toString(),
                                            reminderDateTime, mRowId.toString(), eventId, reminder.getKEY_INVOICE_ID()), customerModel.getFullName());

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    reminders.clear();
                    reminders = databaseHelper.getAllReminders(Integer.toString(ModGlobal.customerId));

                    reminderAdapter = new ReminderAdapter(reminders, CustomerDetailsActivity.this);
                    recyclerView.setAdapter(reminderAdapter);
                    reminderAdapter.notifyDataSetChanged();


                    mRowId = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        builder.show();
    }


    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    public void reminder(View view) {

        MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);

        if (marshMallowPermission.checkPermissionForWriteCalendar()) {
            PopUpReminder(null);
        } else {
            marshMallowPermission.requestPermissionForWriteCalendar();
        }

    }

    public void invoice(View view) {

        Intent intent = new Intent(CustomerDetailsActivity.this, SalesInvoiceProductActivity.class);
        startActivity(intent);
        finish();

        ModGlobal.ProductModelListCopy.clear();
        ModGlobal.stockIns.clear();
        ModGlobal.ProductModelList.clear();

    }
}
