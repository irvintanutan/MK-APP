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


        // Inserting Row
        db.insert(tbl_customer, null, values);
        db.close(); // Closing database connection

        Log.e("DATABASE", "SUCCESS ADDED : " + customerModel.getFirstName() + " " + customerModel.getLastName());

    }

    public List<CustomerModel> getAllCustomer() {
        List<CustomerModel> personList = new ArrayList<CustomerModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + tbl_customer + " order by id DESC";

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

        db.update(tbl_customer, values, "id= ?", new String[]{Integer.toString(id)});
        db.close();

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

        Log.e("DATABASE", "SUCCESS ADDED : " + products.getProduct_id() + " " + products.getProduct_name());

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

    public void addStockIn(String details) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

        values.put(stockInId, formatter.format(date));
        values.put(stockInDetail, details);
        values.put(dateCreated, getDateToday());

        // Inserting Row
        db.insert(tbl_stockIn, null, values);
        db.close(); // Closing database connection
    }


    private String getDateToday() {
        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }


    public List<StockInList> getAllStockIn() {
        List<StockInList> products = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + tbl_stockIn + " ORDER BY dateCreated DESC";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                StockInList p = new StockInList();

                p.setId(cursor.getString(0));
                p.setDetails(cursor.getString(1));
                p.setDateCreated(cursor.getString(2));
                products.add(p);
            } while (cursor.moveToNext());
        }
        // return quote list

        db.close();
        return products;
    }


    public StockInList getAllStockIn(String id) {
        StockInList products = new StockInList();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + tbl_stockIn + " where " + stockInId + " ='" + id + "'";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                products.setId(cursor.getString(0));
                products.setDetails(cursor.getString(1));
                products.setDateCreated(cursor.getString(2));
            } while (cursor.moveToNext());
        }
        // return quote list

        db.close();
        return products;
    }


    public void addPayment(Payment payment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(amount, payment.getAmount());
        values.put(invoiceId, payment.getInvoiceId());
        values.put(dateCreated, getDateToday());
        values.put(balance , payment.getBalance());

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
        values.put(dueDate , invoice.getDueDate());

        // Inserting Row
        db.insert(tbl_invoice, null, values);
        db.close(); // Closing database connection
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

                Log.e("INVOICES" , p.getTotalAmount());

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

                p.setInvoiceId(cursor.getString(0));
                p.setDiscount(cursor.getString(1));
                p.setCustomerId(cursor.getString(2));
                p.setCustomerName(cursor.getString(3));
                p.setTotalAmount(cursor.getString(4));
                p.setStatus(cursor.getString(5));
                p.setInvoiceDetail(cursor.getString(6));
                p.setDateCreated(cursor.getString(7));
                p.setDueDate(cursor.getString(8));

                Log.e("INVOICES" , p.getTotalAmount());

                products.add(p);
            } while (cursor.moveToNext());
        }
        // return quote list

        db.close();
        return products;
    }


}
