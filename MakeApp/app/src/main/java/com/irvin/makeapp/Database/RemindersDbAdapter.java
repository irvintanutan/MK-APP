
package com.irvin.makeapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.irvin.makeapp.Models.Products;
import com.irvin.makeapp.Models.Reminder;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple reminder database access helper class. 
 * Defines the basic CRUD operations (Create, Read, Update, Delete)
 * for the example, and gives the ability to list all reminders as well as
 * retrieve or modify a specific reminder.
 * 
 */
public class RemindersDbAdapter extends SQLiteOpenHelper {

	//
	// Databsae Related Constants
	//
	private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "reminders";
    private static final int DATABASE_VERSION = 1;
    
	public static final String KEY_TITLE = "title";
	public static final String KEY_CUSTOMER_ID = "customer";
    public static final String KEY_BODY = "body";
    public static final String KEY_DATE_TIME = "reminder_date_time"; 
    public static final String KEY_ROWID = "_id";

    
    
    private static final String TAG = "ReminderDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    /**
     * Database creation SQL statement
     */
    private static final String DATABASE_CREATE =
            "create table " + DATABASE_TABLE + " ("
            		+ KEY_ROWID + " integer primary key autoincrement, "
                    + KEY_TITLE + " text not null, " 
                    + KEY_BODY + " text not null, "
                    + KEY_CUSTOMER_ID + " text not null, "
                    + KEY_DATE_TIME + " text not null);"; 

    

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param context the Context within which to work
     */
    public RemindersDbAdapter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mCtx = context;
    }



    /**
     * Open the database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public RemindersDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    public void close() {
        mDbHelper.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    /**
     * Create a new reminder using the title, body and reminder date time provided. 
     * If the reminder is  successfully created return the new rowId
     * for that reminder, otherwise return a -1 to indicate failure.
     * 
     * @param title the title of the reminder
     * @param body the body of the reminder
     * @param reminderDateTime the date and time the reminder should remind the user
     * @return rowId or -1 if failed
     */
    public long createReminder(String title, String body, String reminderDateTime, String customerId) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_BODY, body);
        initialValues.put(KEY_CUSTOMER_ID, customerId);
        initialValues.put(KEY_DATE_TIME, reminderDateTime); 

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the reminder with the given rowId
     * 
     * @param rowId id of reminder to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteReminder(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all reminders in the database
     * 
     * @return Cursor over all reminders
     */
    public Cursor fetchAllReminders() {


        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery(selectQuery, null);
    }

    public List<Reminder> getAllReminders() {
        List<Reminder> reminders = new ArrayList<>();
        // Select All Query

        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE + " ORDER BY " + KEY_ROWID + " DESC";

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


                reminders.add(reminder);
            } while (cursor.moveToNext());
        }
        // return quote list

        db.close();
        return reminders;
    }

    public Reminder getAllReminders(Long id) {
        Reminder reminder = new Reminder();

        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE + " WHERE " + KEY_ROWID + " = " + id + " ORDER BY " + KEY_ROWID + " DESC";

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


            } while (cursor.moveToNext());
        }
        // return quote list

        db.close();
        return reminder;
    }

    /**
     * Return a Cursor positioned at the reminder that matches the given rowId
     * 
     * @param rowId id of reminder to retrieve
     * @return Cursor positioned to matching reminder, if found
     * @throws SQLException if reminder could not be found/retrieved
     */
    public Cursor fetchReminder(long rowId) throws SQLException {

        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE + " WHERE " + KEY_ROWID + " = " + rowId;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor mCursor =
                db.rawQuery(selectQuery, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update the reminder using the details provided. The reminder to be updated is
     * specified using the rowId, and it is altered to use the title, body and reminder date time
     * values passed in
     * 
     * @param rowId id of reminder to update
     * @param title value to set reminder title to
     * @param body value to set reminder body to
     * @param reminderDateTime value to set the reminder time. 
     * @return true if the reminder was successfully updated, false otherwise
     */
    public boolean updateReminder(long rowId, String title, String body, String reminderDateTime, String customerId) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_BODY, body);
        args.put(KEY_CUSTOMER_ID, customerId);
        args.put(KEY_DATE_TIME, reminderDateTime);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
