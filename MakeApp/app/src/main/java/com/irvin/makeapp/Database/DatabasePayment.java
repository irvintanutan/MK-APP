package com.irvin.makeapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Models.Payment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DatabasePayment extends SQLiteOpenHelper {

    private Context mContext;
    DatabaseHelper databaseHelper;
    DatabaseCustomer databaseCustomer;
    //database version
    private static final int DATABASE_VERSION = ModGlobal.DATABASE_VERSION;
    ///database name
    private static final String DATABASE_NAME = "mkdb";

    //table name
    private static final String tbl_payment = "tbl_payment";

    private static final String paymentId = "paymentId";
    private static final String balance = "balance";
    private static final String amount = "amount";
    private static final String dateCreated = "dateCreated";
    private static final String invoiceId = "invoiceId";

    public DatabasePayment(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);
        databaseCustomer = new DatabaseCustomer(mContext);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }



    public void addPayment(Payment payment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(amount, payment.getAmount());
        values.put(invoiceId, payment.getInvoiceId());
        values.put(dateCreated, getDateToday());
        values.put(balance, payment.getBalance());

        // Inserting Row
        db.insert(tbl_payment, null, values);
        db.close(); // Closing database connection
    }


    public List<Payment> getPaymentPerInvoice(String invoiceId) {
        List<Payment> payments = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + tbl_payment + " WHERE invoiceId = '" + invoiceId + "' ORDER BY dateCreated DESC";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Payment p = new Payment();

                p.setPaymentId(cursor.getString(0));
                p.setAmount(cursor.getString(1));
                p.setInvoiceId(cursor.getString(2));
                p.setDateCreated(cursor.getString(3));
                p.setBalance(cursor.getString(4));

                payments.add(p);
            } while (cursor.moveToNext());
        }
        // return quote list

        db.close();
        return payments;
    }


    private String getDateToday() {
        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }
}
