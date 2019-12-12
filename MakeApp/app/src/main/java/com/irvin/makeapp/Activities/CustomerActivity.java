package com.irvin.makeapp.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.irvin.makeapp.Adapters.CustomerAdapter;
import com.irvin.makeapp.Constant.ClickListener;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Constant.RecyclerTouchListener;
import com.irvin.makeapp.Models.CustomerModel;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.R;

import java.util.List;

public class CustomerActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    FloatingActionButton fab;
    RecyclerView recyclerView;
    CustomerAdapter customerAdapter;
    List<CustomerModel> customerModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);



        Toolbar tb = findViewById(R.id.app_bar);
        setSupportActionBar(tb);
        final ActionBar ab = getSupportActionBar();

        ab.setTitle("Customer Profiles");
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
                Intent i = new Intent(CustomerActivity.this, CustomerDetailsActivity.class);
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
                                databaseHelper.deleteCustomer(customerModelList.get(position).getId());
                                customerModelList = databaseHelper.getAllCustomer();
                                customerAdapter.update(customerModelList);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure you want to delete " + customerModelList.get(position).getFullName().toUpperCase()).setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
            }
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
        customerModelList = databaseHelper.getAllCustomer();

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
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(CustomerActivity.this, MainActivity.class));
            finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        }

        return super.onOptionsItemSelected(item);
    }
}
