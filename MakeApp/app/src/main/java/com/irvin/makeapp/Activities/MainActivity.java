package com.irvin.makeapp.Activities;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.irvin.makeapp.Adapters.DataAdapter;
import com.irvin.makeapp.BuildConfig;
import com.irvin.makeapp.Constant.CountDrawable;
import com.irvin.makeapp.Constant.MarshMallowPermission;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Database.DatabaseInvoice;
import com.irvin.makeapp.Models.MenuForm;
import com.irvin.makeapp.R;
import com.irvin.makeapp.Services.GetProductTask;
import com.irvin.makeapp.Services.Logger;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<MenuForm> form;
    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    DatabaseInvoice databaseInvoice = new DatabaseInvoice(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView version = findViewById(R.id.versionName);
        version.setText("PinkHeartV"+BuildConfig.VERSION_NAME);

        ModGlobal.settingPref = PreferenceManager.getDefaultSharedPreferences(this);

        MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
        if (!marshMallowPermission.checkPermissionForWriteCalendar()) {
            marshMallowPermission.requestPermissionForWriteCalendar();
        }


        if (databaseHelper.getAllProducts().size() == 0) {
            new GetProductTask(MainActivity.this).execute("0");
        }

        @SuppressLint("WrongViewCast") Toolbar tb = findViewById(R.id.app_bar);
        setSupportActionBar(tb);
        final ActionBar ab = getSupportActionBar();

        ab.setTitle("MK App");
        ab.setDisplayShowHomeEnabled(false); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(true); // disable the default title element here (for centered title)


        RecyclerView recyclerView = findViewById(R.id.user_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);


        form = new ArrayList<>();


        form.add(new MenuForm("Customer", R.drawable.account, "Manage Customers"));
        form.add(new MenuForm("Products", R.drawable.product, "View Products"));
        form.add(new MenuForm("Purchase Order", R.drawable.box, "Manage Inventory"));
        form.add(new MenuForm("Sales Invoice", R.drawable.invoice, "Customer Purchase"));
        form.add(new MenuForm("Reports", R.drawable.analytics, "View Reports"));
        form.add(new MenuForm("Reminder", R.drawable.calendar, "Manage Reminders"));
        form.add(new MenuForm("Settings", R.drawable.power, "Manage Settings"));



        /*form.add(new MenuForm("Site Survey", R.drawable.salesentry));
        //form.add(new MenuForm("Edit Site", R.drawable.radiotower));*/


        RecyclerView.Adapter adapter = new DataAdapter(form);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);

                    switch (position) {

                        case 0:
                            customer();
                            break;
                        case 1:
                            products();
                            //tickets(true);
                            break;
                        case 5:
                            reminder();
                            break;
                        case 2:
                            stockIn();
                            break;
                        case 3:
                            invoice();
                            break;
                        case 4:
                            reports();
                            break;
                        case 6:
                            setting();
                            break;
                        default:
                    }

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        /*    try{

                double sample = Double.parseDouble("asd");

            }catch (Exception e){


                Logger.CreateNewEntry(e , new File(getExternalFilesDir("") , ModGlobal.logFile));

            }*/
    }


    private void customer() {
        Intent i = new Intent(MainActivity.this, CustomerActivity.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }

    private void products() {
        Intent i = new Intent(MainActivity.this, ProductActivity.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }

    private void stockIn() {
        Intent i = new Intent(MainActivity.this, StockInMainActivity.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }

    private void invoice() {
        ModGlobal.searchFilter = "";
        Intent i = new Intent(MainActivity.this, SalesInvoiceActivity.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }

    private void reports() {
        Intent i = new Intent(MainActivity.this, ReportActivity.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }

    private void logout() {
     /*   Intent i = new Intent(MainActivity.this, AttendanceActivity.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);*/
    }

    private void setting() {
        Intent i = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }

    private void reminder() {
      /*  Intent i = new Intent(MainActivity.this, ReminderActivity.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);*/
        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        ContentUris.appendId(builder, Calendar.getInstance().getTimeInMillis());
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setData(builder.build());
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setIcon(getResources().getDrawable(R.drawable.confirmation));
        builder.setMessage("Are you sure you want to exit application ?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {


                System.exit(0);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notification, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Confirm");
            builder.setIcon(getResources().getDrawable(R.drawable.confirmation));
            builder.setMessage("Are you sure you want to exit application ?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    finish();
                    System.exit(0);


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
        } else if (item.getItemId() == R.id.ic_group) {

            Log.e("INVOICES", Integer.toString(databaseInvoice.getAllDueInvoices().size()));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        int size = databaseInvoice.getAllDueInvoices().size();
        setCount(MainActivity.this, Integer.toString(size), menu);


        return true;
    }


    public void setCount(Context context, String count, Menu defaultMenu) {
        MenuItem menuItem = defaultMenu.findItem(R.id.ic_group);

        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();

        CountDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_group_count);
        if (reuse != null && reuse instanceof CountDrawable) {
            badge = (CountDrawable) reuse;
        } else {
            badge = new CountDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_group_count, badge);
    }


}
