package com.example.anu.waitlistapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.anu.waitlistapp.data.WaitlistContract;
import com.example.anu.waitlistapp.data.WaitlistDbHelper;

import org.junit.Before;
import org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static android.icu.text.MessagePattern.ArgType.SELECT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Design on 07-12-2017.
 */

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    /**
     * context used to perform operations on the database
     * and to create {@link com.example.anu.waitlistapp.data.WaitlistDbHelper}
     */
    private Context context = InstrumentationRegistry.getTargetContext();

    /**
     * class reference to load the constructor at run time
     */
    private Class dbHelperClass = WaitlistDbHelper.class;

    /**
     * method annotated with @{@link Before} will be called
     * before each method with @{@link org.junit.Test} annotation
     * we would like to perform each of our test clean, so we will delete the database
     * before each operation
     */
    @Before
    public void setup(){
        deleteDatabase();
    }

    public void create_database_test()throws Exception{
        /**
         * use reflection to try to run the correct constructor whenever implemented
         */
        SQLiteOpenHelper sqLiteOpenHelper = (SQLiteOpenHelper) dbHelperClass.getConstructor(Context.class).newInstance(context);

        /**
         * use {@link WaitlistDbHelper} to get access to a writable database
         */
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        /**
         * we will think the database is open
         * here we will verify it
         */
        assertEquals("The database should be open, now it's not", true, sqLiteDatabase.isOpen());

        /**
         * the {@link cursorTableNames} contains all the tables in the database
         */
        Cursor cursorTableNames = sqLiteDatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" +
                WaitlistContract.WaitlistEntry.KEY_TABLE_NAME + "'", null);

        /**
         * if cursorTableNames.moveToFirst() returns false, means the databse is not created properly
         */
        assertTrue("Databaseis not created properly", cursorTableNames.moveToFirst());

        /**
         *If this fails, this means that the database does not contains expected number of tables
         */
        assertEquals("Databse does not contains expected table(s)", WaitlistContract.WaitlistEntry.KEY_TABLE_NAME,
                cursorTableNames.getString(0));

        /**
         * always close the cursor when you are done with it
         */
        cursorTableNames.close();
    }

    /**
     * method to delete the entire database
     */
    private void deleteDatabase() {
        /**
         * use reflection to get the name of the database from {@link WaitlistDbHelper}
         */
        try {
            Field field = WaitlistDbHelper.class.getDeclaredField("DATABASE_NAME");
            field.setAccessible(true);
            context.deleteDatabase((String)field.get(null));
        } catch (NoSuchFieldException e) {
            fail("Make sure you have a variable named DATABASE_NAME in WaitlistDbHelper class");
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    /**
     * This method tests inserting a single record into an empty table from a brand new database.
     * The purpose is to test that the database is working as expected
     * @throws Exception in case the constructor hasn't been implemented yet
     */
    @Test
    public void insert_single_record_test() throws Exception{

        /* Use reflection to try to run the correct constructor whenever implemented */
        SQLiteOpenHelper dbHelper =
                (SQLiteOpenHelper) dbHelperClass.getConstructor(Context.class).newInstance(context);

        /* Use WaitlistDbHelper to get access to a writable database */
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues testValues = new ContentValues();
        testValues.put(WaitlistContract.WaitlistEntry.KEY_COLUMN_GUEST_NAME, "test name");
        testValues.put(WaitlistContract.WaitlistEntry.KEY_COLUMN_PARTY_SIZE, 99);

        /* Insert ContentValues into database and get first row ID back */
        long firstRowId = database.insert(
                WaitlistContract.WaitlistEntry.KEY_TABLE_NAME,
                null,
                testValues);

        /* If the insert fails, database.insert returns -1 */
        assertNotEquals("Unable to insert into the database", -1, firstRowId);

        /*
         * Query the database and receive a Cursor. A Cursor is the primary way to interact with
         * a database in Android.
         */
        Cursor wCursor = database.query(
                /* Name of table on which to perform the query */
                WaitlistContract.WaitlistEntry.KEY_TABLE_NAME,
                /* Columns; leaving this null returns every column in the table */
                null,
                /* Optional specification for columns in the "where" clause above */
                null,
                /* Values for "where" clause */
                null,
                /* Columns to group by */
                null,
                /* Columns to filter by row groups */
                null,
                /* Sort order to return in Cursor */
                null);

        /* Cursor.moveToFirst will return false if there are no records returned from your query */
        String emptyQueryError = "Error: No Records returned from waitlist query";
        assertTrue(emptyQueryError,
                wCursor.moveToFirst());

        /* Close cursor and database */
        wCursor.close();
        dbHelper.close();
    }
}
