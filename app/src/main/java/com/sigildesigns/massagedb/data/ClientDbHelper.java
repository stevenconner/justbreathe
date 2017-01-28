package com.sigildesigns.massagedb.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Helper methods to create the database.
 */

public class ClientDbHelper extends SQLiteOpenHelper {
    // Name of the database
    private static final String DATABASE_NAME = "clientdb.db";
    // Database version. If you change the database schema you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    public ClientDbHelper(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * SCHEMA FOR THE DATABASE
         *
         * CREATE TABLE clients (_ID INTEGER PRIMARY KEY, firstName TEXT NOT NULL, lastName TEXT
         * NOT NULL, lastService TEXT NOT NULL, important INTEGER NOT NULL DEFAULT 0, importantNote
         * TEXT, itWorks INTEGER NOT NULL DEFAULT 0, gender INTEGER, email TEXT, phone TEXT, birthday TEXT,
         * referral TEXT, emergencyName TEXT, emergencyPhone TEXT, massagePreviously INTEGER NOT
         * NULL DEFAULT 0, massagePreviouslyDate TEXT, pregnant INTEGER NOT NULL DEFAULT 0,
         * pregnantWeeks TEXT, lastPregnancy TEXT, medications TEXT, goals TEXT, surgeries TEXT,
         * healthIssues TEXT, numbness INTEGER NOT NULL DEFAULT 0, swelling INTEGER NOT NULL
         * DEFAULT 0, headaches INTEGER NOT NULL DEFAULT 0, deragatoryNotes TEXT, notes TEXT);
         *
         * Create a String that contains the SQL statement to create the clients table.
          */
        String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " + ClientContract.ClientEntry.TABLE_NAME + " ("
                + ClientContract.ClientEntry._ID + " INTEGER PRIMARY KEY, "
                + ClientContract.ClientEntry.COLUMN_FIRST_NAME + " TEXT NOT NULL, "
                + ClientContract.ClientEntry.COLUMN_LAST_NAME + " TEXT NOT NULL, "
                + ClientContract.ClientEntry.COLUMN_LAST_SERVICE + " TEXT NOT NULL, "
                + ClientContract.ClientEntry.COLUMN_IMPORTANT + " INTEGER NOT NULL DEFAULT 0, "
                + ClientContract.ClientEntry.COLUMN_IMPORTANT_NOTES + " TEXT, "
                + ClientContract.ClientEntry.COLUMN_IT_WORKS + " INTEGER NOT NULL DEFAULT 0, "
                + ClientContract.ClientEntry.COLUMN_GENDER + " INTEGER, "
                + ClientContract.ClientEntry.COLUMN_EMAIL + " TEXT, "
                + ClientContract.ClientEntry.COLUMN_PHONE_NUMBER + " TEXT, "
                + ClientContract.ClientEntry.COLUMN_BIRTHDAY + " TEXT, "
                + ClientContract.ClientEntry.COLUMN_REFERRAL + " TEXT, "
                + ClientContract.ClientEntry.COLUMN_EMERGENCY_CONTACT_NAME + " TEXT, "
                + ClientContract.ClientEntry.COLUMN_EMERGENCY_CONTACT_PHONE_NUMBER + " TEXT, "
                + ClientContract.ClientEntry.COLUMN_HAD_MASSAGE_PREVIOUSLY + " INTEGER NOT NULL DEFAULT 0, "
                + ClientContract.ClientEntry.COLUMN_HAD_MASSAGE_PREVIOUSLY_DATE + " TEXT, "
                + ClientContract.ClientEntry.COLUMN_PREGNANT + " INTEGER NOT NULL DEFAULT 0, "
                + ClientContract.ClientEntry.COLUMN_PREGNANT_WEEKS + " TEXT, "
                + ClientContract.ClientEntry.COLUMN_LAST_PREGNANCY_DATE + " TEXT, "
                + ClientContract.ClientEntry.COLUMN_MEDICATIONS + " TEXT, "
                + ClientContract.ClientEntry.COLUMN_GOALS + " TEXT, "
                + ClientContract.ClientEntry.COLUMN_RECENT_SURGERIES + " TEXT, "
                + ClientContract.ClientEntry.COLUMN_HEALTH_ISSUES + " TEXT, "
                + ClientContract.ClientEntry.COLUMN_NUMBNESS + " INTEGER NOT NULL DEFAULT 0, "
                + ClientContract.ClientEntry.COLUMN_SWELLING + " INTEGER NOT NULL DEFAULT 0, "
                + ClientContract.ClientEntry.COLUMN_HEADACHES + " INTEGER NOT NULL DEFAULT 0, "
                + ClientContract.ClientEntry.COLUMN_DEROGATORY_NOTES + " TEXT, "
                + ClientContract.ClientEntry.COLUMN_NOTES + " TEXT);";
        Log.v(ClientProvider.LOG_TAG, SQL_CREATE_ITEMS_TABLE);
        db.execSQL(SQL_CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
