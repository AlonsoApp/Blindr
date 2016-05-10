package com.cloupix.blindr.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alonsoapp on 23/04/16.
 *
 */
public class SQLHelper extends SQLiteOpenHelper {


    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "blindr.db";


    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BlindrDbContract.SQL_CREATE_TABLE_MAP);
        db.execSQL(BlindrDbContract.SQL_CREATE_TABLE_WIFI_AP);
        db.execSQL(BlindrDbContract.SQL_CREATE_TABLE_SECTOR);
        db.execSQL(BlindrDbContract.SQL_CREATE_TABLE_SECTOR_VIEW);
        db.execSQL(BlindrDbContract.SQL_CREATE_TABLE_LECTURE);
        db.execSQL(BlindrDbContract.SQL_CREATE_TABLE_WIFI_AP_SECTOR);
        db.execSQL(BlindrDbContract.SQL_CREATE_TABLE_WIFI_AP_VIEW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        db.execSQL(BlindrDbContract.SQL_DELETE_TABLE_WIFI_AP_SECTOR);
        db.execSQL(BlindrDbContract.SQL_DELETE_TABLE_WIFI_AP_VIEW);
        db.execSQL(BlindrDbContract.SQL_DELETE_TABLE_LECTURE);
        db.execSQL(BlindrDbContract.SQL_DELETE_TABLE_SECTOR_VIEW);
        db.execSQL(BlindrDbContract.SQL_DELETE_TABLE_SECTOR);
        db.execSQL(BlindrDbContract.SQL_DELETE_TABLE_WIFI_AP);
        db.execSQL(BlindrDbContract.SQL_DELETE_TABLE_MAP);
        onCreate(db);
    }
}
