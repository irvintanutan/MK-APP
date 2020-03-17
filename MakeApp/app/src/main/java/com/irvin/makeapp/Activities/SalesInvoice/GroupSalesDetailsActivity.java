package com.irvin.makeapp.Activities.SalesInvoice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.irvin.makeapp.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

/**
 * @author irvin
 */
public class GroupSalesDetailsActivity extends AppCompatActivity {
    AutoCompleteTextView textView;
    TagContainerLayout mTagContainerLayout;
    List<String> tags = new ArrayList<>();
    public static final String PREFS_NAME = "PingBusPrefs";
    public static final String PREFS_SEARCH_HISTORY = "SearchHistory";
    private SharedPreferences settings;
    private Set<String> history;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_sales_details);

        @SuppressLint("WrongViewCast") Toolbar tb = findViewById(R.id.app_bar);
        setSupportActionBar(tb);
        final ActionBar ab = getSupportActionBar();

        ab.setTitle("Add New Group Sales");
        ab.setDisplayShowHomeEnabled(true);
        // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true);
        // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(true);
        // disable the default title element here (for centered title)

        init();
    }


    private void init() {

        mTagContainerLayout = findViewById(R.id.tagContainer);

        settings = getSharedPreferences(PREFS_NAME, 0);
        history = new HashSet<>(settings.getStringSet(PREFS_SEARCH_HISTORY, new HashSet<String>()));

        Log.e("history1" , history.toString());

        setAutoCompleteSource();

        textView = findViewById(R.id.textInput);
        textView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    addSearchInput(textView.getText().toString());
                    tags.add(textView.getText().toString());
                    refreshTags();
                    textView.setText(null);
                    return true;
                }
                return false;
            }
        });

        mTagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {

            @Override
            public void onTagClick(int position, String text) {
                // ...
            }

            @Override
            public void onTagLongClick(final int position, String text) {
                // ...
            }

            @Override
            public void onSelectedTagDrag(int position, String text){
                // ...
            }

            @Override
            public void onTagCrossClick(int position) {
                // ...
                mTagContainerLayout.removeTag(position);
                tags.remove(position);
            }
        });

        refreshTags();
    }


    void refreshTags() {
        mTagContainerLayout.setTags(tags);
    }

    @Override
    protected void onStop() {
        super.onStop();

        savePrefs();
    }

    private void setAutoCompleteSource() {
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.textInput);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, history.toArray(new String[history.size()]));
        textView.setAdapter(adapter);
    }

    private void addSearchInput(String input) {
        if (!history.contains(input)) {
            history.add(input);
            savePrefs();
            setAutoCompleteSource();
        }
    }


    private void savePrefs() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        Log.e("history2" , history.toString());
        editor.putStringSet(PREFS_SEARCH_HISTORY, history);

        editor.commit();
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(GroupSalesDetailsActivity.this, GroupSalesActivity.class));
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(GroupSalesDetailsActivity.this, GroupSalesActivity.class));
            finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        } else if (item.getItemId() == R.id.action_search) {

        }

        return super.onOptionsItemSelected(item);
    }

    public void save(View view) {


    }


    public void addBeautyConsultant(View view) {
        addSearchInput(textView.getText().toString());
        tags.add(textView.getText().toString());
        refreshTags();
        textView.setText(null);
    }
}
