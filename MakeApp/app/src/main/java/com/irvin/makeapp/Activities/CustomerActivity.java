package com.irvin.makeapp.Activities;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.irvin.makeapp.Adapters.CustomerAdapter;
import com.irvin.makeapp.Constant.ClickListener;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Constant.RecyclerTouchListener;
import com.irvin.makeapp.Database.DatabaseCustomer;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Models.CustomerModel;
import com.irvin.makeapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CustomerActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    DatabaseCustomer databaseCustomer = new DatabaseCustomer(this);
    FloatingActionButton fab;
    RecyclerView recyclerView;
    CustomerAdapter customerAdapter;
    List<CustomerModel> customerModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        ModGlobal.isInSalesInvoice = false;

        @SuppressLint("WrongViewCast") Toolbar tb = findViewById(R.id.app_bar);
        setSupportActionBar(tb);
        final ActionBar ab = getSupportActionBar();

        ab.setTitle("Customer Profile");
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(true); // disable the default title element here (for centered title)

        init();

    }

    private void init(){

        fab = findViewById(R.id.floating_action_button);

        recyclerView = findViewById(R.id.customer_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        loadList();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {

            @Override
            public void onClick(View view, int position) {
                ModGlobal.isCreateNew = false;
                ModGlobal.customerId = customerModelList.get(position).getId();
                ModGlobal.customerName = customerModelList.get(position).getFullName();
                Intent i = new Intent(CustomerActivity.this, CustomerProfileViewActivity.class);
                i.putExtra("toolBarTitle" , "");
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }

            @Override
            public void onLongClick(View view, final int position) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                              /*  databaseHelper.deleteCustomer(customerModelList.get(position).getId());
                                customerModelList = databaseHelper.getAllCustomer();
                                customerAdapter.update(customerModelList);*/
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

               /* AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure you want to delete " + customerModelList.get(position).getFullName().toUpperCase()).setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
            */}
        }));



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ModGlobal.isCreateNew = true;
                Intent i = new Intent(CustomerActivity.this, CustomerDetailsActivity.class);
                i.putExtra("toolBarTitle" , "Add New Customer");
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);


            }
        });

    }


    private void loadList(){
        customerModelList = databaseCustomer.getAllCustomer();
        ModGlobal.customerModelList = customerModelList;

        Log.e("size" , Integer.toString(customerModelList.size()));

        customerAdapter = new CustomerAdapter(customerModelList , this);
        recyclerView.setAdapter(customerAdapter);
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(CustomerActivity.this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final MenuItem sync = menu.findItem(R.id.action_sync);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        sync.setVisible(false);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(CustomerActivity.this, MainActivity.class));
            finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        } else if (item.getItemId() == R.id.action_sync){
          /*  addEventToCalender("THIS IS A SAMPLE " , "THIS IS A SAMPLE DESCRIPTION" , "" ,
                    1 , new Date);*/
        }

        return super.onOptionsItemSelected(item);
    }

    private void filter(String text){

        List<CustomerModel> temp = new ArrayList();

        if (text.equals(""))
            temp = ModGlobal.customerModelList;
        else {
            for (CustomerModel p : ModGlobal.customerModelList) {
                //or use .contains(text)
                if (p.getFirstName().toLowerCase().contains(text.toLowerCase()) ||
                        p.getLastName().toLowerCase().contains(text.toLowerCase())) {
                    temp.add(p);
                }
            }
        }
        //update recyclerview
        customerAdapter.update(temp);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        filter(newText);
        return false;
    }
}
