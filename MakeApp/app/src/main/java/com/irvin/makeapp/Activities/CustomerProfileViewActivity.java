package com.irvin.makeapp.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.hbb20.GThumb;
import com.irvin.makeapp.Adapters.ViewPagerAdapterCustomerProfile;
import com.irvin.makeapp.Constant.MarshMallowPermission;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Database.DatabaseCustomer;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Models.CustomerModel;
import com.irvin.makeapp.R;
import com.irvin.makeapp.Services.Logger;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerProfileViewActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    DatabaseCustomer databaseCustomer = new DatabaseCustomer(this);
    CustomerModel customerModel = new CustomerModel();

    GThumb profilePicture;
    CircleImageView profilePicture2;
    TextView customerName, phoneNumber, email, location;
    RecyclerView recyclerView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapterCustomerProfile viewPagerAdapter;
    MarshMallowPermission marshMallowPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile_view);


        marshMallowPermission = new MarshMallowPermission(this);
        customerModel = databaseCustomer.getAllCustomer(ModGlobal.customerId);

        init();

    }


    private void init() {
        try {
            profilePicture = findViewById(R.id.profilePicture);
            profilePicture2 = findViewById(R.id.profilePicture2);
            customerName = findViewById(R.id.customerName);
            phoneNumber = findViewById(R.id.phoneNumber);
            email = findViewById(R.id.email);
            location = findViewById(R.id.address);


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

            try {
                String[] splitDateValues = customerModel.getBirthday().split("-");
                int year = Integer.parseInt(splitDateValues[0]);
                int month = Integer.parseInt(splitDateValues[1]) - 1;
                int day = Integer.parseInt(splitDateValues[2]);

                customerName.setText(customerModel.getFullName() + ", " + ModGlobal.getAge(year, month, day));
            } catch (Exception e) {
                customerName.setText(customerModel.getFullName());
            }
            if (!customerModel.getContactNumber().equals(" ") || customerModel.getContactNumber() != null) {
                phoneNumber.setText(customerModel.getContactNumber());
            } else {
                phoneNumber.setText("none");
            }
            if (!customerModel.getEmail().equals(" ") || customerModel.getEmail() != null) {
                email.setText(customerModel.getEmail());
            } else {
                email.setText("none");
            }
            if (!customerModel.getAddress().equals(" ") || customerModel.getAddress() != null) {
                location.setText(customerModel.getAddress());
            } else {
                location.setText("none");
            }



        } catch (Exception e) {
            e.printStackTrace();
            Logger.CreateNewEntry(e, new File(getExternalFilesDir(""), ModGlobal.logFile));
        }


        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);

        viewPagerAdapter = new ViewPagerAdapterCustomerProfile(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);


        final TabLayout.Tab profile = tabLayout.newTab();
        final TabLayout.Tab reminders = tabLayout.newTab();
        final TabLayout.Tab orders = tabLayout.newTab();

        profile.setText("Profile");
        reminders.setText("Reminders");
        orders.setText("Orders");

        tabLayout.addTab(profile, 0);
        tabLayout.addTab(reminders, 1);
        tabLayout.addTab(orders, 2);


        tabLayout.setTabTextColors(ContextCompat.getColorStateList(this, R.color.colorAccent));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorPrimary));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(CustomerProfileViewActivity.this, CustomerActivity.class));
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
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
