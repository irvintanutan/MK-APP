package com.irvin.makeapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Models.CustomerModel;
import com.irvin.makeapp.Models.GroupSalesModel;
import com.irvin.makeapp.Models.Invoice;
import com.irvin.makeapp.Models.TransactionModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DatabaseGroupSales extends SQLiteOpenHelper {

    private Context mContext;
    DatabaseHelper databaseHelper;
    //database version
    private static final int DATABASE_VERSION = ModGlobal.DATABASE_VERSION;
    ///database name
    private static final String DATABASE_NAME = "mkdb";


    //table name
    private static final String tbl_group_sales = "tbl_group_sales";
    private static final String groupSalesName = "paymentId";
    private static final String groupSalesConsultantDetails = "groupSalesConsultantDetails";
    private static final String dateCreated = "dateCreated";


    public DatabaseGroupSales(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public void addGroupSales(GroupSalesModel groupSalesModel) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(groupSalesName, groupSalesModel.getName());
        values.put(groupSalesConsultantDetails, groupSalesModel.getConsultants());
        values.put(dateCreated, getDateToday());

        // Inserting Row
        db.insert(tbl_group_sales, null, values);
        db.close(); // Closing database connection


    }



    public void updateGroupSales(GroupSalesModel groupSalesModel, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(groupSalesName, groupSalesModel.getName());
        values.put(groupSalesConsultantDetails, groupSalesModel.getConsultants());

        db.update(tbl_group_sales, values, "id= ?", new String[]{id});
        db.close();

    }




    public List<GroupSalesModel> getGroupSales(String id) {
        List<GroupSalesModel> groupSalesModels = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + tbl_group_sales + " where id like '%" + id + "%' ORDER BY dateCreated DESC";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                GroupSalesModel groupSalesModel = new GroupSalesModel();

                groupSalesModel.setId(cursor.getString(0));
                groupSalesModel.setName(cursor.getString(1));
                groupSalesModel.setConsultants(cursor.getString(2));
                groupSalesModel.setDateCreated(cursor.getString(3));



                groupSalesModels.add(groupSalesModel);
            } while (cursor.moveToNext());
        }
        // return quote list

        db.close();
        return groupSalesModels;
    }



    private String getDateToday() {
        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }

}
