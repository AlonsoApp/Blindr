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
    public static final int DATABASE_VERSION = 5;
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
        db.execSQL(BlindrDbContract.SQL_CREATE_TABLE_READING);
        db.execSQL(BlindrDbContract.SQL_CREATE_TABLE_WIFI_AP_SECTOR);
        db.execSQL(BlindrDbContract.SQL_CREATE_TABLE_WIFI_AP_VIEW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(oldVersion==3){

            db.execSQL("ALTER TABLE map ADD COLUMN map_framework_map_id TEXT DEFAULT ''");
            oldVersion++;
            /*
            //create temp. table to hold data Aqui usamos el la variable SQL_CREATE_TABLE_MAP antigua (de la v3) y la ponemos aqui con el nombre Map.TABLE_NAME+"_tmp
            db.execSQL("CREATE TEMPORARY TABLE "+BlindrDbContract.Map.TABLE_NAME+"_tmp (_id INTEGER PRIMARY KEY AUTOINCREMENT, column_1 TEXT, column_2 TEXT);");

            //insert data from old table into temp table
            db.execSQL("INSERT INTO map_backup SELECT _id, column_1, column_2 FROM map ");
            //drop the old table now that your data is safe in the temporary one
            db.execSQL("DROP TABLE map");
            //recreate the NEW table
            db.execSQL("CREATE TABLE map (_id INTEGER PRIMARY KEY AUTOINCREMENT, column_1 TEXT, column_2 TEXT, column_3 TEXT);");
            //fill it up using null (or whatever default value you want)for the column_3
            db.execSQL("INSERT INTO map SELECT _id, column_1, column_2, null FROM contacts_backup");
            //then drop the temporary table
            db.execSQL("DROP TABLE map_backup");
            oldVersion++;
            */

        }
        if(oldVersion==4){
            db.execSQL("ALTER TABLE map ADD COLUMN pathloss_exponent REAL DEFAULT 1.2");
            oldVersion++;
        }

        /*
        // Esto se va fuera con lo de abajo
        db.execSQL(BlindrDbContract.SQL_DELETE_TABLE_WIFI_AP_SECTOR);
        db.execSQL(BlindrDbContract.SQL_DELETE_TABLE_WIFI_AP_VIEW);
        db.execSQL(BlindrDbContract.SQL_DELETE_TABLE_READING);
        db.execSQL(BlindrDbContract.SQL_DELETE_TABLE_SECTOR_VIEW);
        db.execSQL(BlindrDbContract.SQL_DELETE_TABLE_SECTOR);
        db.execSQL(BlindrDbContract.SQL_DELETE_TABLE_WIFI_AP);
        db.execSQL(BlindrDbContract.SQL_DELETE_TABLE_MAP);
        onCreate(db);
        */
    }
}
