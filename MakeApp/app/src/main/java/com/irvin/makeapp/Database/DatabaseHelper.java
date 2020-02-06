package com.irvin.makeapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Models.Products;
import com.irvin.makeapp.Models.Reminder;

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
    private static final String remarks = "remarks";


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

    private static final String tbl_reminder = "reminders";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CUSTOMER_ID = "customer";
    public static final String KEY_BODY = "body";
    public static final String KEY_DATE_TIME = "reminder_date_time";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_EVENT_ID = "event_id";


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
                + photoUrl + " TEXT , "
                + remarks + " TEXT );";
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


        String DATABASE_CREATE_REMINDER =
                "create table " + tbl_reminder + " ("
                        + KEY_ROWID + " integer primary key autoincrement, "
                        + KEY_TITLE + " text not null, "
                        + KEY_BODY + " text not null, "
                        + KEY_CUSTOMER_ID + " text not null, "
                        + KEY_DATE_TIME + " text not null, "
                        + KEY_EVENT_ID + " text not null);";
        db.execSQL(DATABASE_CREATE_REMINDER);

    }


    private static final String REMARKS_ADD_COLUMN = "ALTER TABLE " + tbl_customer +
            " ADD COLUMN " + remarks + " TEXT DEFAULT ''";


    private static final String DATABASE_CREATE_REMINDER =
            "create table " + tbl_reminder + " ("
                    + KEY_ROWID + " integer primary key autoincrement, "
                    + KEY_TITLE + " text not null, "
                    + KEY_BODY + " text not null, "
                    + KEY_CUSTOMER_ID + " text not null, "
                    + KEY_DATE_TIME + " text not null, "
                    + KEY_EVENT_ID + " text not null);";

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 2) {
            db.execSQL(REMARKS_ADD_COLUMN);
            db.execSQL(DATABASE_CREATE_REMINDER);
        }
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

    public void updateProduct(Products products) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put(productId, products.getProduct_id());
        values.put(productName, products.getProduct_name());
        values.put(productPrice, products.getProduct_price());
        values.put(productCategory, products.getProduct_category());


        db.update(tbl_product, values, "productId= ?", new String[]{products.getProduct_id()});
        db.close();

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


    /////////////////////////////REMINDERS//////////////////////////////

    public long createReminder(Reminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, reminder.getKEY_TITLE());
        initialValues.put(KEY_BODY, reminder.getKEY_BODY());
        initialValues.put(KEY_CUSTOMER_ID, reminder.getKEY_CUSTOMER_ID());
        initialValues.put(KEY_DATE_TIME, reminder.getKEY_DATE_TIME());
        initialValues.put(KEY_EVENT_ID, reminder.getKEY_EVENT_ID());

        return db.insert(tbl_reminder, null, initialValues);
    }

    public void updateReminder(Reminder reminder , String rowId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, reminder.getKEY_TITLE());
        initialValues.put(KEY_BODY, reminder.getKEY_BODY());
        initialValues.put(KEY_CUSTOMER_ID, reminder.getKEY_CUSTOMER_ID());
        initialValues.put(KEY_DATE_TIME, reminder.getKEY_DATE_TIME());
        initialValues.put(KEY_EVENT_ID, reminder.getKEY_EVENT_ID());

        db.update(tbl_reminder, initialValues, KEY_ROWID+"= ?", new String[]{rowId});
        db.close();
    }

    public List<Reminder> getAllReminders(String customerId) {
        List<Reminder> reminders = new ArrayList<>();
        // Select All Query

        String selectQuery = "SELECT  * FROM " + tbl_reminder + " where " + KEY_CUSTOMER_ID + "='" + customerId + "' ORDER BY " + KEY_ROWID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Reminder reminder = new Reminder();
                reminder.setKEY_ROWID(cursor.getString(0));
                reminder.setKEY_TITLE(cursor.getString(1));
                reminder.setKEY_BODY(cursor.getString(2));
                reminder.setKEY_CUSTOMER_ID(cursor.getString(3));
                reminder.setKEY_DATE_TIME(cursor.getString(4));
                reminder.setKEY_EVENT_ID(cursor.getString(5));


                reminders.add(reminder);
            } while (cursor.moveToNext());
        }
        // return quote list

        db.close();
        return reminders;
    }

    public boolean deleteReminder(long rowId) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(tbl_reminder, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Reminder getAllReminders(Long id) {
        Reminder reminder = new Reminder();

        String selectQuery = "SELECT  * FROM " + tbl_reminder + " WHERE " + KEY_ROWID + " = " + id + " ORDER BY " + KEY_ROWID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                reminder.setKEY_ROWID(cursor.getString(0));
                reminder.setKEY_TITLE(cursor.getString(1));
                reminder.setKEY_BODY(cursor.getString(2));
                reminder.setKEY_CUSTOMER_ID(cursor.getString(3));
                reminder.setKEY_DATE_TIME(cursor.getString(4));
                reminder.setKEY_EVENT_ID(cursor.getString(5));

            } while (cursor.moveToNext());
        }
        // return quote list

        db.close();
        return reminder;
    }
}
