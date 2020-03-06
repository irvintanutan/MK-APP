package com.irvin.makeapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Models.CustomerModel;
import com.irvin.makeapp.Models.Invoice;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DatabaseInvoice extends SQLiteOpenHelper {
    DatabasePayment databasePayment;
    DecimalFormat dec = new DecimalFormat("#,##0.00");
    private Context mContext;
    DatabaseHelper databaseHelper;
    DatabaseCustomer databaseCustomer;
    //database version
    private static final int DATABASE_VERSION = ModGlobal.DATABASE_VERSION;
    ///database name
    private static final String DATABASE_NAME = "mkdb";


    //table name
    private static final String tbl_invoice = "tbl_invoice";

    private static final String invoiceId = "invoiceId";
    private static final String discount = "discount";
    private static final String customerId = "customerId";
    private static final String customerName = "customerName";
    private static final String totalAmount = "totalAmount";
    private static final String status = "status";
    private static final String invoiceDetail = "invoiceDetail";
    private static final String dueDate = "dueDate";
    private static final String dateCreated = "dateCreated";


    public DatabaseInvoice(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);
        databaseCustomer = new DatabaseCustomer(mContext);
        databasePayment = new DatabasePayment(mContext);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private String getDateToday() {
        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }

    public void addInvoice(Invoice invoice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(customerId, invoice.getCustomerId());
        values.put(customerName, invoice.getCustomerName());
        values.put(totalAmount, invoice.getTotalAmount());
        values.put(discount, invoice.getDiscount());
        values.put(status, invoice.getStatus());
        values.put(invoiceDetail, invoice.getInvoiceDetail());
        values.put(dateCreated, getDateToday());
        values.put(dueDate, invoice.getDueDate());

        // Inserting Row
        db.insert(tbl_invoice, null, values);
        db.close(); // Closing database connection
    }


    public void updateInvoice(Invoice invoice, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(customerId, invoice.getCustomerId());
        values.put(customerName, invoice.getCustomerName());
        values.put(totalAmount, invoice.getTotalAmount());
        values.put(discount, invoice.getDiscount());
        values.put(status, invoice.getStatus());
        values.put(invoiceDetail, invoice.getInvoiceDetail());
        values.put(dateCreated, getDateToday());
        values.put(dueDate, invoice.getDueDate());

        db.update(tbl_invoice, values, "invoiceId= ?", new String[]{id});
        db.close();

    }


    public String getLastInvoiceId() {

        String id = "1";
        // Select All Query
        String selectQuery = "SELECT  invoiceId FROM " + tbl_invoice + " ORDER BY invoiceId DESC";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
            id = Integer.toString(cursor.getInt(0) + 1);

        return id;

    }


    public List<Invoice> getAllInvoices() {
        List<Invoice> products = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + tbl_invoice + " ORDER BY dateCreated DESC";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Invoice p = new Invoice();

                p.setInvoiceId(cursor.getString(0));
                p.setDiscount(cursor.getString(1));
                p.setCustomerId(cursor.getString(2));
                p.setCustomerName(cursor.getString(3));
                p.setTotalAmount(cursor.getString(4));
                p.setStatus(cursor.getString(5));
                p.setInvoiceDetail(cursor.getString(6));
                p.setDateCreated(cursor.getString(7));
                p.setDueDate(cursor.getString(8));


                products.add(p);
            } while (cursor.moveToNext());
        }
        // return quote list

        db.close();
        return products;
    }


    public List<Invoice> getInvoiceById(String id) {

        Log.e("DATABASEHELPER", id);

        List<Invoice> products = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + tbl_invoice + " WHERE invoiceId = '" + id + "'";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Invoice p = new Invoice();

                p.setInvoiceId(cursor.getString(0));
                p.setDiscount(cursor.getString(1));
                p.setCustomerId(cursor.getString(2));
                p.setCustomerName(cursor.getString(3));
                p.setTotalAmount(cursor.getString(4));
                p.setStatus(cursor.getString(5));
                p.setInvoiceDetail(cursor.getString(6));
                p.setDateCreated(cursor.getString(7));
                p.setDueDate(cursor.getString(8));


                products.add(p);
            } while (cursor.moveToNext());
        }
        // return quote list

        db.close();
        return products;
    }


    public List<Invoice> getAllInvoices(String status) {
        List<Invoice> products = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + tbl_invoice + " WHERE status = '" + status + "' ORDER BY dateCreated DESC";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Invoice p = new Invoice();
                CustomerModel
                        customer = databaseCustomer.getAllCustomer(Integer.parseInt(cursor.getString(2)));

                p.setInvoiceId(cursor.getString(0));
                p.setDiscount(cursor.getString(1));
                p.setCustomerId(cursor.getString(2));
                p.setCustomerName(ModGlobal.toTitleCase(customer.getFirstName() + " " + customer.getLastName()));
                p.setTotalAmount(cursor.getString(4));
                p.setStatus(cursor.getString(5));
                p.setInvoiceDetail(cursor.getString(6));
                p.setDateCreated(cursor.getString(7));
                p.setDueDate(cursor.getString(8));

                products.add(p);
            } while (cursor.moveToNext());
        }
        // return quote list

        db.close();
        return products;
    }



    public List<Invoice> getAllInvoicesByCustomer(int customerId , String status) {
        List<Invoice> products = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + tbl_invoice + " WHERE status = '" + status + "' and  customerId = '" + customerId + "' ORDER BY dateCreated DESC";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Invoice p = new Invoice();
                CustomerModel
                        customer = databaseCustomer.getAllCustomer(Integer.parseInt(cursor.getString(2)));

                p.setInvoiceId(cursor.getString(0));
                p.setDiscount(cursor.getString(1));
                p.setCustomerId(cursor.getString(2));
                p.setCustomerName(ModGlobal.toTitleCase(customer.getFirstName() + " " + customer.getLastName()));
                p.setTotalAmount(cursor.getString(4));
                p.setStatus(cursor.getString(5));
                p.setInvoiceDetail(cursor.getString(6));
                p.setDateCreated(cursor.getString(7));
                p.setDueDate(cursor.getString(8));

                products.add(p);
            } while (cursor.moveToNext());
        }
        // return quote list

        db.close();
        return products;
    }

    public List<Invoice> getAllDueInvoices() {

        List<Invoice> products = new ArrayList<>();
        try {
            Date date = Calendar.getInstance().getTime();
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            Log.e("asdasda" , formatter.format(date));

            // Select All Query
            String selectQuery = "SELECT  * FROM " + tbl_invoice + " WHERE status = 'PENDING' " +
                  " and date('" + formatter.format(date) + "') >= date(dueDate) " +
                    " ORDER BY dateCreated DESC";

            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Invoice p = new Invoice();
                    CustomerModel
                            customer = databaseCustomer.getAllCustomer(Integer.parseInt(cursor.getString(2)));
                    p.setInvoiceId(cursor.getString(0));
                    p.setDiscount(cursor.getString(1));
                    p.setCustomerId(cursor.getString(2));
                    p.setCustomerName(ModGlobal.toTitleCase(customer.getFirstName() + " " + customer.getLastName()));
                    p.setTotalAmount(cursor.getString(4));
                    p.setStatus(cursor.getString(5));
                    p.setInvoiceDetail(cursor.getString(6));
                    p.setDateCreated(cursor.getString(7));
                    p.setDueDate(cursor.getString(8));

                    products.add(p);
                } while (cursor.moveToNext());
            }
            // return quote list

            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }


    public String getMonthlySales(String date) {
        Double result = 0.00;

        String query = "select sum(" + totalAmount + ") from " + tbl_invoice + " where strftime('%Y-%m', " + dateCreated + ") = '" + date + "';";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                result = cursor.getDouble(0);

            } while (cursor.moveToNext());
        }
        return "₱ " + dec.format(result);
    }

    public String getTotalSales() {
        Double result = 0.00;

        String query = "select sum(" + totalAmount + ") from " + tbl_invoice;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                result = cursor.getDouble(0);

            } while (cursor.moveToNext());
        }
        return "₱ " + dec.format(result);
    }


    public String getAllDueInvoices(String customerId, boolean isDueDate) {
        String result = "";
        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");


        // Select All Query
        String selectQuery;
        if (isDueDate) {
            selectQuery = "SELECT  sum(totalAmount) as totalAmount FROM " + tbl_invoice + " WHERE customerId = '" + customerId + "' and status = 'PENDING' " +
                    " and date('" + formatter.format(date) + "') >= date(dueDate) " +
                    " ORDER BY dateCreated DESC";
        } else {
            selectQuery = "SELECT  sum(totalAmount) as totalAmount FROM " + tbl_invoice + " WHERE customerId = '" + customerId + "' and status = 'PENDING' " +
                    /*" and date('" + formatter.format(date) + "') >= date(dueDate) " +*/
                    " ORDER BY dateCreated DESC";
        }
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                result = cursor.getString(0);

            } while (cursor.moveToNext());
        }
        // return quote list

        db.close();


        return result;
    }

    public boolean deleteInvoice(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        databasePayment.deletePayment(id);
        return db.delete(tbl_invoice, invoiceId + "=" + id, null) > 0;
    }


}
