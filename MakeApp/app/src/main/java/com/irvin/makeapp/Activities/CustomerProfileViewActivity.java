package com.irvin.makeapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.irvin.makeapp.Adapters.ReminderAdapter;
import com.irvin.makeapp.Constant.MarshMallowPermission;
import com.irvin.makeapp.Constant.MultiSelectionSpinner;
import com.irvin.makeapp.Database.DatabaseCustomer;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Models.CustomerModel;
import com.irvin.makeapp.Models.Reminder;
import com.irvin.makeapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CustomerProfileViewActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_customer_profile_view);
    }
}
