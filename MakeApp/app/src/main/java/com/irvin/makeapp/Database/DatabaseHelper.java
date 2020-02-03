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
import com.irvin.makeapp.Models.Payment;
import com.irvin.makeapp.Models.Products;
import com.irvin.makeapp.Models.StockInList;
import com.irvin.makeapp.Models.TransactionModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {


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


    //table name
    private static final String tbl_product = "tbl_product";

    private static final String productId = "productId";
    private static final String productName = "productName";
    private static final String productPrice = "productPrice";
    private static final String productCategory = "productCategory";
    private static final String productQuantity = "productQuantity";


    //table name
    private static final String tbl_stockIn = "tbl_stockIn";

    private static final String stockInId = "stockInId";
    private static final String stockInDetail = "stockInDetail";
    private static final String dateCreated = "dateCreated";


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


    //table name
    private static final String tbl_payment = "tbl_payment";

    private static final String paymentId = "paymentId";
    private static final String balance = "balance";
    private static final String amount = "amount";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_PERSON_TABLE = "CREATE TABLE " + tbl_customer + "( id integer primary key autoincrement , "
                + firstName + " TEXT , "
                + lastName + " TEXT , "
                + middleName + " TEXT , "
                + address + " TEXT , "
                + birthday + " TEXT , "
                + age + " TEXT , "
                + occupation + " TEXT , "
                + email + " TEXT , "
                + contactNumber + " TEXT , "
                + bestTimeToBeContacted + " TEXT , "
                + referredBy + " TEXT , "
                + skinType + " TEXT , "
                + skinConcern + " TEXT , "
                + skinTone + " TEXT , "
                + interests + " TEXT , "
                + photoUrl + " TEXT );";
        db.execSQL(CREATE_PERSON_TABLE);

        String CREATE_PRODUCT_TABLE = "CREATE TABLE " + tbl_product + "( productId TEXT primary key  , "
                + productName + " TEXT , "
                + productPrice + " TEXT , "
                + productCategory + " TEXT , "
                + productQuantity + " TEXT );";
        db.execSQL(CREATE_PRODUCT_TABLE);


        String CREATE_STOCK_IN_TABLE = "CREATE TABLE " + tbl_stockIn + "( stockInId TEXT primary key  , "
                + stockInDetail + " TEXT , "
                + dateCreated + " TEXT );";
        db.execSQL(CREATE_STOCK_IN_TABLE);

        String CREATE_INVOICE_TABLE = "CREATE TABLE " + tbl_invoice + "( invoiceId integer primary key autoincrement , "
                + discount + " TEXT , "
                + customerId + " TEXT , "
                + customerName + " TEXT , "
                + totalAmount + " TEXT , "
                + status + " TEXT , "
                + invoiceDetail + " TEXT , "
                + dateCreated + " TEXT , "
                + dueDate + " TEXT );";
        db.execSQL(CREATE_INVOICE_TABLE);

        String CREATE_PAYMENT_TABLE = "CREATE TABLE " + tbl_payment + "( paymentId integer primary key autoincrement , "
                + amount + " TEXT , "
                + invoiceId + " TEXT , "
                + dateCreated + " TEXT , "
                + balance + " TEXT );";
        db.execSQL(CREATE_PAYMENT_TABLE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addProduct(Products products) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(productId, products.getProduct_id());
        values.put(productName, products.getProduct_name());
        values.put(productPrice, products.getProduct_price());
        values.put(productCategory, products.getProduct_category());
        values.put(productQuantity, products.getProduct_quantity());

        // Inserting Row
        db.insert(tbl_product, null, values);
        db.close(); // Closing database connection


    }

    public List<Products> getAllProducts() {
        List<Products> products = new ArrayList<Products>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + tbl_product;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Products p = new Products();

                p.setProduct_id(cursor.getString(0));
                p.setProduct_name(cursor.getString(1));
                p.setProduct_price(cursor.getString(2).replace("PHP", ""));
                p.setProduct_category(cursor.getString(3));
                p.setProduct_quantity(cursor.getString(4));

                products.add(p);
            } while (cursor.moveToNext());
        }
        // return quote list

        db.close();
        return products;
    }

    public List<Products> getAllProducts(String id) {
        List<Products> products = new ArrayList<Products>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + tbl_product + " WHERE " + productId + " = '" + id + "'";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Products p = new Products();

                p.setProduct_id(cursor.getString(0));
                p.setProduct_name(cursor.getString(1));
                p.setProduct_price(cursor.getString(2).replace("PHP", ""));
                p.setProduct_category(cursor.getString(3));
                p.setProduct_quantity(cursor.getString(4));

                products.add(p);
            } while (cursor.moveToNext());
        }
        // return quote list

        db.close();
        return products;
    }

    public List<Products> getAllProductsWithQuantity() {
        List<Products> products = new ArrayList<Products>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + tbl_product + " where " + productQuantity + " > 0";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        int counter = 0;
        if (cursor.moveToFirst()) {
            do {
                Products p = new Products();

                p.setProduct_id(cursor.getString(0));
                p.setProduct_name(cursor.getString(1));
                p.setProduct_price(cursor.getString(2).replace("PHP", ""));
                p.setProduct_category(cursor.getString(3));
                p.setProduct_quantity(cursor.getString(4));
                p.setPosition(counter);
                p.setSelected(false);

                products.add(p);
                counter++;
            } while (cursor.moveToNext());
        }
        // return quote list

        db.close();
        return products;
    }

    public void stockIn(String code, String qty, boolean isAdd) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        int quantity;
        int q1 = Integer.parseInt(qty);

        String selectQuery = "SELECT  productQuantity FROM " + tbl_product + " where " + productId + " = '" + code + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        cursor.moveToFirst();

        if (cursor.getString(0) != null) {

            if (isAdd)
                quantity = q1 + Integer.parseInt(cursor.getString(0));
            else
                quantity = Integer.parseInt(cursor.getString(0)) - q1;

        } else {
            quantity = q1;
        }
        values.put(productQuantity, quantity);

        db.update(tbl_product, values, "productId= '" + code + "'", null);
        db.close(); // Closing database connection
    }

    private String getDateToday() {
        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }



}
