package com.irvin.makeapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Models.StockInList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DatabaseStockin extends SQLiteOpenHelper {

    private Context mContext;
    DatabaseHelper databaseHelper;
    //database version
    private static final int DATABASE_VERSION = ModGlobal.DATABASE_VERSION;
    ///database name
    private static final String DATABASE_NAME = "mkdb";

    //table name
    private static final String tbl_stockIn = "tbl_stockIn";

    private static final String stockInId = "stockInId";
    private static final String stockInDetail = "stockInDetail";
    private static final String dateCreated = "dateCreated";

    public DatabaseStockin(Context context) {
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

}
