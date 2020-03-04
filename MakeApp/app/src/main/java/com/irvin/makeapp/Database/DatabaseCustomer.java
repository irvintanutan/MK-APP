package com.irvin.makeapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Models.CustomerModel;
import com.irvin.makeapp.Models.TransactionModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DatabaseCustomer extends SQLiteOpenHelper {

    private Context mContext;
    DatabaseHelper databaseHelper;
    //database version
    private static final int DATABASE_VERSION = ModGlobal.DATABASE_VERSION;
    ///database name
    private static final String DATABASE_NAME = "mkdb";


    //table name
    private static final String tbl_customer = "tbl_customer";

    private static final String firstName = "firstName";
    private static final String lastName = "lastName";
    private static final String middleName = "middleName";
    private static final String address = "address";
    private static final String birthday = "birthday";
    private static final String age = "age";
    private static final String occupation = "occupation";
    private static final String email = "email";
    private static final String contactNumber = "contactNumber";
    private static final String bestTimeToBeContacted = "bestTimeToBeContacted";
    private static final String referredBy = "referredBy";
    private static final String skinType = "skinType";
    private static final String skinConcern = "skinConcern";
    private static final String skinTone = "skinTone";
    private static final String interests = "interests";
    private static final String photoUrl = "photoUrl";
    private static final String remarks = "remarks";
    private static final String birthdayEventId = "birthdayEventId";


    public DatabaseCustomer(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public void addCustomer(CustomerModel customerModel) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(firstName, customerModel.getFirstName());
        values.put(lastName, customerModel.getLastName());
        values.put(middleName, customerModel.getMiddleName());
        values.put(address, customerModel.getAddress());
        values.put(birthday, customerModel.getBirthday());
        values.put(age, customerModel.getAge());
        values.put(occupation, customerModel.getOccupation());
        values.put(email, customerModel.getEmail());
        values.put(contactNumber, customerModel.getContactNumber());
        values.put(bestTimeToBeContacted, customerModel.getBestTimeToBeContacted());
        values.put(referredBy, customerModel.getReferredBy());
        values.put(skinType, customerModel.getSkinType());
        values.put(skinConcern, customerModel.getSkinConcern());
        values.put(skinTone, customerModel.getSkinTone());
        values.put(interests, customerModel.getInterests());
        values.put(photoUrl, customerModel.getPhotoUrl());
        values.put(remarks, customerModel.getRemarks());
        values.put(birthdayEventId, customerModel.getBirthdayEventId());



        // Inserting Row
        db.insert(tbl_customer, null, values);
        db.close(); // Closing database connection


    }

    public List<CustomerModel> getAllCustomer() {
        List<CustomerModel> personList = new ArrayList<CustomerModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + tbl_customer + " order by firstName asc";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CustomerModel c = new CustomerModel();

                c.setId(cursor.getInt(0));
                c.setFirstName(cursor.getString(1));
                c.setLastName(cursor.getString(2));
                c.setMiddleName(cursor.getString(3));
                c.setAddress(cursor.getString(4));
                c.setBirthday(cursor.getString(5));
                c.setAge(cursor.getString(6));
                c.setOccupation(cursor.getString(7));
                c.setEmail(cursor.getString(8));
                c.setContactNumber(cursor.getString(9));
                c.setBestTimeToBeContacted(cursor.getString(10));
                c.setReferredBy(cursor.getString(11));
                c.setSkinType(cursor.getString(12));
                c.setSkinConcern(cursor.getString(13));
                c.setSkinTone(cursor.getString(14));
                c.setInterests(cursor.getString(15));
                c.setPhotoUrl(cursor.getString(16));
                c.setRemarks(cursor.getString(17));
                c.setBirthdayEventId(cursor.getString(18));


                personList.add(c);
            } while (cursor.moveToNext());
        }
        // return quote list

        db.close();
        return personList;
    }

    public List<TransactionModel> getAllCustomerWithDueDates(boolean isDueDate , String filter) {
        List<TransactionModel> personList = new ArrayList<>();
        // Select All Query
        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        String selectQuery;
        if (isDueDate) {
            selectQuery = "SELECT  c.photoUrl , c.firstName , c.lastName , " +
                    "sum (p.amount) as totalAmountPaid , c.id " +
                    "FROM tbl_invoice i " +
                    "INNER JOIN tbl_payment p on i.invoiceId = p.invoiceId " +
                    "INNER JOIN tbl_customer c on i.customerId  = c.id " +
                    "  WHERE i.status = 'PENDING' and (c.firstName like '%" + filter +"%' or c.lastName like '%"+ filter + "%')" +
                    " and date('" + formatter.format(date) + "') >= date(i.dueDate)" +
                    " GROUP BY c.id order by c.firstName asc";
        } else {
            selectQuery = "SELECT  c.photoUrl , c.firstName , c.lastName , " +
                    "sum (p.amount) as totalAmountPaid , c.id " +
                    "FROM tbl_invoice i " +
                    "INNER JOIN tbl_payment p on i.invoiceId = p.invoiceId " +
                    "INNER JOIN tbl_customer c on i.customerId  = c.id " +
                    "  WHERE i.status = 'PENDING' and (c.firstName like '%" + filter + "%' or c.lastName like '%"+ filter + "%')" +
                    /*     " and date('" + formatter.format(date) + "') >= date(i.dueDate)" +*/
                    " GROUP BY c.id order by c.firstName asc";
        }

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TransactionModel c = new TransactionModel();


                c.setCustomerName(ModGlobal.toTitleCase(cursor.getString(1) + " " + cursor.getString(2)));
                c.setPhotoUrl(cursor.getString(0));
                c.setTotalAmount("100");
                c.setCustomerId(cursor.getString(4));
                c.setTotalAmountPaid(cursor.getString(3));


                personList.add(c);
            } while (cursor.moveToNext());
        }
        // return quote list

        db.close();
        return personList;
    }


    public List<TransactionModel> getTop5Customer() {
        List<TransactionModel> personList = new ArrayList<>();
        // Select All Query

        String selectQuery;


            selectQuery = "SELECT  c.photoUrl , c.firstName , c.lastName , " +
                    "sum (i.totalAmount) as totalAmount , c.id " +
                    "FROM tbl_invoice i " +
                    "INNER JOIN tbl_customer c on i.customerId  = c.id " +
                    " GROUP BY c.id order by totalAmount desc limit 5";


        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TransactionModel c = new TransactionModel();


                c.setCustomerName(ModGlobal.toTitleCase(cursor.getString(1) + " " + cursor.getString(2)));
                c.setPhotoUrl(cursor.getString(0));
                c.setTotalAmount(cursor.getString(3));
                c.setCustomerId(cursor.getString(4));
                c.setTotalAmountPaid(cursor.getString(3));


                personList.add(c);
            } while (cursor.moveToNext());
        }
        // return quote list

        db.close();
        return personList;
    }

    public CustomerModel getAllCustomer(int id) {
        CustomerModel c = new CustomerModel();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + tbl_customer + " where id =" + id;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                c.setId(cursor.getInt(0));
                c.setFirstName(cursor.getString(1));
                c.setLastName(cursor.getString(2));
                c.setMiddleName(cursor.getString(3));
                c.setAddress(cursor.getString(4));
                c.setBirthday(cursor.getString(5));
                c.setAge(cursor.getString(6));
                c.setOccupation(cursor.getString(7));
                c.setEmail(cursor.getString(8));
                c.setContactNumber(cursor.getString(9));
                c.setBestTimeToBeContacted(cursor.getString(10));
                c.setReferredBy(cursor.getString(11));
                c.setSkinType(cursor.getString(12));
                c.setSkinConcern(cursor.getString(13));
                c.setSkinTone(cursor.getString(14));
                c.setInterests(cursor.getString(15));
                c.setPhotoUrl(cursor.getString(16));
                c.setRemarks(cursor.getString(17));
                c.setBirthdayEventId(cursor.getString(18));

            } while (cursor.moveToNext());
        }

        db.close();
        return c;
    }


    public void updateCustomer(CustomerModel customerModel, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(firstName, customerModel.getFirstName());
        values.put(lastName, customerModel.getLastName());
        values.put(middleName, customerModel.getMiddleName());
        values.put(address, customerModel.getAddress());
        values.put(birthday, customerModel.getBirthday());
        values.put(age, customerModel.getAge());
        values.put(occupation, customerModel.getOccupation());
        values.put(email, customerModel.getEmail());
        values.put(contactNumber, customerModel.getContactNumber());
        values.put(bestTimeToBeContacted, customerModel.getBestTimeToBeContacted());
        values.put(referredBy, customerModel.getReferredBy());
        values.put(skinType, customerModel.getSkinType());
        values.put(skinConcern, customerModel.getSkinConcern());
        values.put(skinTone, customerModel.getSkinTone());
        values.put(interests, customerModel.getInterests());
        values.put(photoUrl, customerModel.getPhotoUrl());
        values.put(remarks, customerModel.getRemarks());
        values.put(birthdayEventId, customerModel.getBirthdayEventId());

        db.update(tbl_customer, values, "id= ?", new String[]{Integer.toString(id)});
        db.close();

    }

}
