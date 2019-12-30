package com.irvin.makeapp.Activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Models.CustomerModel;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Constant.MultiSelectionSpinner;
import com.irvin.makeapp.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CustomerDetailsActivity extends AppCompatActivity implements MultiSelectionSpinner.OnMultipleItemsSelectedListener {
    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    CustomerModel customerModel = new CustomerModel();
    String mCurrentPhotoPath = "";
    private static int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_REQUEST = 20;
    AppCompatImageView profilePicture;
    EditText firstName, middleName, lastName, address, email, age, occupation, contactNumber, bestTimeToBeContacted, referredBy;
    Button birthday;
    Spinner skinType, skinTone;
    MultiSelectionSpinner skinConcern, interest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);


        Toolbar tb = findViewById(R.id.app_bar);
        setSupportActionBar(tb);
        final ActionBar ab = getSupportActionBar();

        Intent intent = getIntent();


        ab.setTitle(intent.getStringExtra("toolBarTitle"));
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(true); // disable the default title element here (for centered title)


        init();

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

            customerModel = databaseHelper.getAllCustomer(ModGlobal.customerId);
            if (!customerModel.getPhotoUrl().isEmpty() && customerModel.getPhotoUrl() != null) {
                Log.e("asd" , customerModel.getPhotoUrl());
                Glide.with(getApplicationContext()).load(new File(customerModel.getPhotoUrl())).into(profilePicture);
            }else {
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

                startActivity(new Intent(CustomerDetailsActivity.this, CustomerActivity.class));
                finish();
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);


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
        } else if (item.getItemId() == R.id.action_call) {

            callCustomer();

        } else if (item.getItemId() == R.id.action_message) {

            messageCustomer();

        }

        return super.onOptionsItemSelected(item);
    }

    private void callCustomer() {
        Intent intent = new Intent(Intent.ACTION_CALL);

        intent.setData(Uri.parse("tel:" + customerModel.getContactNumber()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        startActivity(intent);
    }

    private void messageCustomer(){


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least KitKat
        {
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(this); // Need to change the build to API 19

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "text");
            sendIntent.putExtra(Intent.EXTRA_PHONE_NUMBER, customerModel.getContactNumber());


            if (defaultSmsPackageName != null)// Can be null in case that there is no default, then the user would be able to choose
            // any app that support this intent.
            {
                sendIntent.setPackage(defaultSmsPackageName);
            }
            startActivity(sendIntent);

        }
        else // For early versions, do what worked for you before.
        {
            Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra(Intent.EXTRA_PHONE_NUMBER, customerModel.getContactNumber());
            smsIntent.putExtra("sms_body","");
            startActivity(smsIntent);
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

        if (isCreateNew)
            databaseHelper.addCustomer(customerModel);
        else
            databaseHelper.updateCustomer(customerModel, ModGlobal.customerId);


        startActivity(new Intent(CustomerDetailsActivity.this, CustomerActivity.class));
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
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

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            photoFile = createImageFile();
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.irvin.makeapp.provider",
                        photoFile);


                List<ResolveInfo> resolvedIntentActivities = getApplicationContext().getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                    String packageName = resolvedIntentInfo.activityInfo.packageName;

                    getApplicationContext().grantUriPermission(packageName, photoURI,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Glide.with(this).load(new File(mCurrentPhotoPath)).into(profilePicture);
        }
        else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            mCurrentPhotoPath = cursor.getString(columnIndex);

            Glide.with(this).load(new File(mCurrentPhotoPath)).into(profilePicture);

            cursor.close();

            // String picturePath contains the path of selected Image
        }
    }


    private File createImageFile() {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;

        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = image.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }


    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {
        // Toast.makeText(this.getApplicationContext(),"Selected Charmelets" + strings, Toast.LENGTH_LONG).show();


    }


    public void gallery(View view) {

        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
}
