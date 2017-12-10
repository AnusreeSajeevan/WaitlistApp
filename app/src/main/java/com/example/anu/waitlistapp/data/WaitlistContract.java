package com.example.anu.waitlistapp.data;

/**
 * Created by Design on 06-12-2017.
 */

import android.provider.BaseColumns;

/**
 * class which contains the contract of the tables and it's columns
 */
public class WaitlistContract {

    /**
     * to prevent anyone from accidentaly create instanceof the class,
     * we make the constructor private
     */
    private WaitlistContract(){}

    /**
     * create an inner class for each of the tables
     * inside the class, specify the name of the tables and it's columns
     * as static final fields
     * this class must implement BaseColumns interface
     */
    public static final class WaitlistEntry implements BaseColumns{

        /* table name */
        public static final String KEY_TABLE_NAME = "waitlist";

        /**
         * a column named _ID will be created automatically
         */

        //column for storing guest name
        public static final String KEY_COLUMN_GUEST_NAME = "guestName";

        //column for storing party size
        public static final String KEY_COLUMN_PARTY_SIZE = "partySize";

        //column for storing the timestamp
        public static final String KEY_COLUMN_TIMESTAMP = "timestamp";
    }

}
