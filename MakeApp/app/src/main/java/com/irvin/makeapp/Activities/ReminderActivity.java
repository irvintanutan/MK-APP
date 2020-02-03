package com.irvin.makeapp.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.R;

public class ReminderActivity extends AppCompatActivity {

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        Toolbar tb = findViewById(R.id.app_bar);
        setSupportActionBar(tb);
        final ActionBar ab = getSupportActionBar();

        ab.setTitle("Reminders");
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(true); // disable the default title element here (for centered title)


        init();
    }

    private void init(){
        fab = findViewById(R.id.floating_action_button);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ModGlobal.isCreateNew = true;
                Intent i = new Intent(ReminderActivity.this, CustomerDetailsActivity.class);
                i.putExtra("toolBarTitle" , "Add New Customer");
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);


            }
        });
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
}
