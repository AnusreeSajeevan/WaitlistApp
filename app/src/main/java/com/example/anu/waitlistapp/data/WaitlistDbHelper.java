package com.example.anu.waitlistapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Design on 07-12-2017.
 */

/**
 * {@link SQLiteOpenHelper} is mainly responsible for creating the db for the first time
 * and upgrading it when th schema changes
 *
 */
public class WaitlistDbHelper extends SQLiteOpenHelper {

    /**
     * {@value DATABASE_NAME} is the name of the local file on our android device
     * which will store all the data
     */
    private static final String DATABASE_NAME = "waitlist.db";

    /**
     * {@value DATABASE_VERSION} stores the current database version
     * version always starts from 1, and whenever we are changing the db schema, w should increment
     * this version
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * call the super class constructor
     * @param context
     * second argument to the parent class constructor is the factory
     */
    public WaitlistDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * method responsible for creating the database for the first time
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /**
         * {@link WaitlistContract.WaitlistEntry._ID} is autoincrement and primary key
         * meaning that, it's value gets incremented whenever a new row is being inserted
         *
         * {@link WaitlistContract.WaitlistEntry.KEY_COLUMN_TIMESTAMP}
         * SQLite systme will automatically insert current timestamp in this field
         * whenever a new row is being inserted
         */
        String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " + WaitlistContract.WaitlistEntry.KEY_TABLE_NAME + " (" +
                WaitlistContract.WaitlistEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WaitlistContract.WaitlistEntry.KEY_COLUMN_GUEST_NAME + " TEXT NOT NULL, " +
                WaitlistContract.WaitlistEntry.KEY_COLUMN_PARTY_SIZE + " INTEGER NOT NULL, " +
                WaitlistContract.WaitlistEntry.KEY_COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";
        sqLiteDatabase.execSQL(SQL_CREATE_WAITLIST_TABLE);

    }

    /**
     * method responsible for making sure that database schema is up-to date
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WaitlistContract.WaitlistEntry.KEY_TABLE_NAME);
    }
}
