package com.cloupix.blindr.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.cloupix.blindr.business.Lecture;
import com.cloupix.blindr.business.Map;
import com.cloupix.blindr.business.Sector;
import com.cloupix.blindr.business.SectorView;
import com.cloupix.blindr.business.WifiAP;
import com.cloupix.blindr.business.WifiAPView;

import java.util.ArrayList;

/**
 * Created by alonsoapp on 23/04/16.
 *
 */
public class Dao {


    private SQLiteDatabase database;
    private SQLHelper sqlHelper;

    public Dao(Context context)
    {
        sqlHelper = new SQLHelper(context);
    }

    public void open() throws SQLException
    {
        database = sqlHelper.getWritableDatabase();
    }

    public void close()
    {
        sqlHelper.close();
    }

    public boolean isConnectionOpen(){
        if(database==null)
            return false;
        else
            return database.isOpen();
    }

    //<editor-fold desc="Internal classes">

    public class WifiAPSector{
        private long wifiAPSectorId;
        private String BSSID;
        private long sectorId;

        public WifiAPSector() {
        }

        public WifiAPSector(long wifiAPSectorId, String BSSID, long sectorId) {
            this.wifiAPSectorId = wifiAPSectorId;
            this.BSSID = BSSID;
            this.sectorId = sectorId;
        }

        public long getWifiAPSectorId() {
            return wifiAPSectorId;
        }

        public void setWifiAPSectorId(long wifiAPSectorId) {
            this.wifiAPSectorId = wifiAPSectorId;
        }

        public String getBSSID() {
            return BSSID;
        }

        public void setBSSID(String BSSID) {
            this.BSSID = BSSID;
        }

        public long getSectorId() {
            return sectorId;
        }

        public void setSectorId(long sectorId) {
            this.sectorId = sectorId;
        }
    }

    //</editor-fold>

    //<editor-fold desc="Inserts">

    public long insertMap(Map map) {
        if(database==null)
            return -1;

        //  Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(BlindrDbContract.Map.COLUMN_NAME_NAME, map.getName());
        values.put(BlindrDbContract.Map.COLUMN_NAME_HEIGHT, map.getHeight());
        values.put(BlindrDbContract.Map.COLUMN_NAME_WIDTH, map.getWidth());

        return database.insertWithOnConflict(BlindrDbContract.Map.TABLE_NAME, null, values,
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    public long insertSector(Sector sector) {
        if(database==null)
            return -1;

        //  Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(BlindrDbContract.Sector.COLUMN_NAME_LIST_N, sector.getListN());
        values.put(BlindrDbContract.Sector.COLUMN_NAME_MATRIX_X, sector.getMatrixX());
        values.put(BlindrDbContract.Sector.COLUMN_NAME_MATRIX_Y, sector.getMatrixY());
        values.put(BlindrDbContract.Sector.COLUMN_NAME_LATITUDE, sector.getLatitude());
        values.put(BlindrDbContract.Sector.COLUMN_NAME_LONGITUDE, sector.getLongitude());
        values.put(BlindrDbContract.Sector.COLUMN_NAME_MAP_ID, sector.getMapId());

        return database.insertWithOnConflict(BlindrDbContract.Sector.TABLE_NAME, null, values,
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    public long insertLecture(Lecture lecture) {
        if(database==null)
            return -1;

        //  Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(BlindrDbContract.Lecture.COLUMN_NAME_MATH_GENERATED, lecture.isMathGenerated());
        values.put(BlindrDbContract.Lecture.COLUMN_NAME_BSSID, lecture.getBSSID());
        values.put(BlindrDbContract.Lecture.COLUMN_NAME_LEVEL, lecture.getLevel());
        values.put(BlindrDbContract.Lecture.COLUMN_NAME_FRENQUENCY, lecture.getFrequency());
        values.put(BlindrDbContract.Lecture.COLUMN_NAME_TIMESTAMP, lecture.getTimestamp());
        values.put(BlindrDbContract.Lecture.COLUMN_NAME_SECTOR_ID, lecture.getSectorId());

        return database.insertWithOnConflict(BlindrDbContract.Lecture.TABLE_NAME, null, values,
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    public long insertWifiAP(WifiAP wifiAP){
        if(database==null)
            return -1;

        //  Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(BlindrDbContract.WifiAP.COLUMN_NAME_BSSID, wifiAP.getBSSID());
        values.put(BlindrDbContract.WifiAP.COLUMN_NAME_SSID, wifiAP.getSSID());

        return database.insertWithOnConflict(BlindrDbContract.WifiAP.TABLE_NAME, null, values,
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    public long insertWifiAPView(WifiAPView wifiAPView){
        if(database==null)
            return -1;

        //  Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(BlindrDbContract.WifiAPView.COLUMN_NAME_BACKGROUND_IMAGE, wifiAPView.getBackgroundImage());
        values.put(BlindrDbContract.WifiAPView.COLUMN_NAME_AP_NUMBER, wifiAPView.getApNumber());
        values.put(BlindrDbContract.WifiAPView.COLUMN_NAME_BSSID, wifiAPView.getBSSID());

        return database.insertWithOnConflict(BlindrDbContract.WifiAPView.TABLE_NAME, null, values,
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    public long insertSectorView(com.cloupix.blindr.business.SectorView sectorView){
        if(database==null)
            return -1;

        //  Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(BlindrDbContract.SectorView.COLUMN_NAME_STROKE_N, sectorView.getStroke()[com.cloupix.blindr.business.SectorView.STROKE_N]);
        values.put(BlindrDbContract.SectorView.COLUMN_NAME_STROKE_E, sectorView.getStroke()[com.cloupix.blindr.business.SectorView.STROKE_E]);
        values.put(BlindrDbContract.SectorView.COLUMN_NAME_STROKE_S, sectorView.getStroke()[com.cloupix.blindr.business.SectorView.STROKE_S]);
        values.put(BlindrDbContract.SectorView.COLUMN_NAME_STROKE_W, sectorView.getStroke()[com.cloupix.blindr.business.SectorView.STROKE_W]);
        values.put(BlindrDbContract.SectorView.COLUMN_NAME_STROKE_NW_SE, sectorView.getStroke()[com.cloupix.blindr.business.SectorView.STROKE_NW_SE]);
        values.put(BlindrDbContract.SectorView.COLUMN_NAME_STROKE_NE_SW, sectorView.getStroke()[com.cloupix.blindr.business.SectorView.STROKE_NE_SW]);
        values.put(BlindrDbContract.SectorView.COLUMN_NAME_SCANNED, sectorView.isScanned());
        values.put(BlindrDbContract.SectorView.COLUMN_NAME_SECTOR_ID, sectorView.getSectorId());

        return database.insertWithOnConflict(BlindrDbContract.SectorView.TABLE_NAME, null, values,
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    public long insertWifiAPSector(WifiAPSector wifiAPSector){
        if(database==null)
            return -1;

        //  Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(BlindrDbContract.WifiAPSector.COLUMN_NAME_BSSID, wifiAPSector.getBSSID());
        values.put(BlindrDbContract.WifiAPSector.COLUMN_NAME_SECTOR_ID, wifiAPSector.getSectorId());

        return database.insertWithOnConflict(BlindrDbContract.WifiAPSector.TABLE_NAME, null, values,
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    //</editor-fold>

    //<editor-fold desc="Queries">

    public Map getMapById(long mapId) {
        if(database==null)
            return null;

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BlindrDbContract.Map.COLUMN_NAME_MAP_ID,
                BlindrDbContract.Map.COLUMN_NAME_NAME,
                BlindrDbContract.Map.COLUMN_NAME_HEIGHT,
                BlindrDbContract.Map.COLUMN_NAME_WIDTH
        };

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection =
                BlindrDbContract.Map.COLUMN_NAME_MAP_ID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                Long.toString(mapId)
        };

        // How you want the results sorted in the resulting Cursor
        //String sortOrder = FeedEntry.COLUMN_NAME_UPDATED + " DESC";

        Cursor c = database.query(
                BlindrDbContract.Map.TABLE_NAME,       // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        Map sector = cursorToMap(c);
        c.close();
        return sector;
    }

    public Sector getSectorById(long sectorId) {
        if(database==null)
            return null;

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BlindrDbContract.Sector.COLUMN_NAME_SECTOR_ID,
                BlindrDbContract.Sector.COLUMN_NAME_LIST_N,
                BlindrDbContract.Sector.COLUMN_NAME_MATRIX_X,
                BlindrDbContract.Sector.COLUMN_NAME_MATRIX_Y,
                BlindrDbContract.Sector.COLUMN_NAME_LATITUDE,
                BlindrDbContract.Sector.COLUMN_NAME_LONGITUDE,
                BlindrDbContract.Sector.COLUMN_NAME_MAP_ID
        };

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection =
                BlindrDbContract.Sector.COLUMN_NAME_SECTOR_ID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                Long.toString(sectorId)
        };

        // How you want the results sorted in the resulting Cursor
        //String sortOrder = FeedEntry.COLUMN_NAME_UPDATED + " DESC";

        Cursor c = database.query(
                BlindrDbContract.Sector.TABLE_NAME,       // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        Sector sector = cursorToSector(c);
        c.close();
        return sector;
    }

    public ArrayList<Sector> getSectorsByMapId(long mapId) {
        if(database==null)
            return null;

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BlindrDbContract.Sector.COLUMN_NAME_SECTOR_ID,
                BlindrDbContract.Sector.COLUMN_NAME_LIST_N,
                BlindrDbContract.Sector.COLUMN_NAME_MATRIX_X,
                BlindrDbContract.Sector.COLUMN_NAME_MATRIX_Y,
                BlindrDbContract.Sector.COLUMN_NAME_LATITUDE,
                BlindrDbContract.Sector.COLUMN_NAME_LONGITUDE,
                BlindrDbContract.Sector.COLUMN_NAME_MAP_ID
        };

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection =
                BlindrDbContract.Sector.COLUMN_NAME_MAP_ID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                Long.toString(mapId)
        };

        // How you want the results sorted in the resulting Cursor
        //String sortOrder = FeedEntry.COLUMN_NAME_UPDATED + " DESC";

        Cursor c = database.query(
                BlindrDbContract.Sector.TABLE_NAME,       // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        ArrayList<Sector> list = cursorToSectorList(c);
        c.close();
        return list;
    }

    public Lecture getLectureById(long lectureId) {
        if(database==null)
            return null;

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BlindrDbContract.Lecture.COLUMN_NAME_LECTURE_ID,
                BlindrDbContract.Lecture.COLUMN_NAME_MATH_GENERATED,
                BlindrDbContract.Lecture.COLUMN_NAME_LEVEL,
                BlindrDbContract.Lecture.COLUMN_NAME_FRENQUENCY,
                BlindrDbContract.Lecture.COLUMN_NAME_TIMESTAMP,
                BlindrDbContract.Lecture.COLUMN_NAME_SECTOR_ID,
                BlindrDbContract.Lecture.COLUMN_NAME_BSSID
        };

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection =
                BlindrDbContract.Lecture.COLUMN_NAME_LECTURE_ID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                Long.toString(lectureId)
        };

        // How you want the results sorted in the resulting Cursor
        //String sortOrder = FeedEntry.COLUMN_NAME_UPDATED + " DESC";

        Cursor c = database.query(
                BlindrDbContract.Lecture.TABLE_NAME,       // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        Lecture lecture = cursorToLecture(c);
        c.close();
        return lecture;
    }

    public ArrayList<Lecture> getLecturesBySectorId(long sectorId) {
        if(database==null)
            return null;

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BlindrDbContract.Lecture.COLUMN_NAME_LECTURE_ID,
                BlindrDbContract.Lecture.COLUMN_NAME_MATH_GENERATED,
                BlindrDbContract.Lecture.COLUMN_NAME_LEVEL,
                BlindrDbContract.Lecture.COLUMN_NAME_FRENQUENCY,
                BlindrDbContract.Lecture.COLUMN_NAME_TIMESTAMP,
                BlindrDbContract.Lecture.COLUMN_NAME_SECTOR_ID,
                BlindrDbContract.Lecture.COLUMN_NAME_BSSID
        };

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection =
                BlindrDbContract.Lecture.COLUMN_NAME_SECTOR_ID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                Long.toString(sectorId)
        };

        // How you want the results sorted in the resulting Cursor
        //String sortOrder = FeedEntry.COLUMN_NAME_UPDATED + " DESC";

        Cursor c = database.query(
                BlindrDbContract.Lecture.TABLE_NAME,       // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        ArrayList<Lecture> list = cursorToLectureList(c);
        c.close();
        return list;
    }

    public WifiAP getWifiAPByBSSID(String bssid) {
        if(database==null)
            return null;

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BlindrDbContract.WifiAP.COLUMN_NAME_BSSID,
                BlindrDbContract.WifiAP.COLUMN_NAME_SSID
        };

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection =
                BlindrDbContract.WifiAP.COLUMN_NAME_BSSID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                bssid
        };

        // How you want the results sorted in the resulting Cursor
        //String sortOrder = FeedEntry.COLUMN_NAME_UPDATED + " DESC";

        Cursor c = database.query(
                BlindrDbContract.WifiAP.TABLE_NAME,       // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        WifiAP wifiAP = cursorToWifiAP(c);
        c.close();
        return wifiAP;
    }

    public WifiAPView getWifiAPViewById(long wifiAPViewId) {
        if(database==null)
            return null;

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BlindrDbContract.WifiAPView.COLUMN_NAME_WIFI_AP_VIEW_ID,
                BlindrDbContract.WifiAPView.COLUMN_NAME_BACKGROUND_IMAGE,
                BlindrDbContract.WifiAPView.COLUMN_NAME_AP_NUMBER,
                BlindrDbContract.WifiAPView.COLUMN_NAME_BSSID
        };

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection =
                BlindrDbContract.WifiAPView.COLUMN_NAME_WIFI_AP_VIEW_ID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                Long.toString(wifiAPViewId)
        };

        // How you want the results sorted in the resulting Cursor
        //String sortOrder = FeedEntry.COLUMN_NAME_UPDATED + " DESC";

        Cursor c = database.query(
                BlindrDbContract.WifiAPView.TABLE_NAME,       // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        WifiAPView wifiAPView = cursorToWifiAPView(c);
        c.close();
        return wifiAPView;
    }

    public WifiAPView getWifiAPViewByBSSID(String bssid) {
        if(database==null)
            return null;

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BlindrDbContract.WifiAPView.COLUMN_NAME_WIFI_AP_VIEW_ID,
                BlindrDbContract.WifiAPView.COLUMN_NAME_BACKGROUND_IMAGE,
                BlindrDbContract.WifiAPView.COLUMN_NAME_AP_NUMBER,
                BlindrDbContract.WifiAPView.COLUMN_NAME_BSSID
        };

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection =
                BlindrDbContract.WifiAPView.COLUMN_NAME_BSSID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                bssid
        };

        // How you want the results sorted in the resulting Cursor
        //String sortOrder = FeedEntry.COLUMN_NAME_UPDATED + " DESC";

        Cursor c = database.query(
                BlindrDbContract.WifiAPView.TABLE_NAME,       // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        WifiAPView wifiAPView = cursorToWifiAPView(c);
        c.close();
        return wifiAPView;
    }

    public WifiAPSector getWifiAPSectorById(long wifiAPSectorId) {
        if(database==null)
            return null;

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BlindrDbContract.WifiAPSector.COLUMN_NAME_WIFI_AP_SECTOR_ID,
                BlindrDbContract.WifiAPSector.COLUMN_NAME_BSSID,
                BlindrDbContract.WifiAPSector.COLUMN_NAME_SECTOR_ID
        };

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection =
                BlindrDbContract.WifiAPSector.COLUMN_NAME_WIFI_AP_SECTOR_ID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                Long.toString(wifiAPSectorId)
        };

        // How you want the results sorted in the resulting Cursor
        //String sortOrder = FeedEntry.COLUMN_NAME_UPDATED + " DESC";

        Cursor c = database.query(
                BlindrDbContract.WifiAPSector.TABLE_NAME,       // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        WifiAPSector wifiAPSector = cursorToWifiAPSector(c);
        c.close();
        return wifiAPSector;
    }

    public ArrayList<WifiAPSector> getWifiAPSectorBySectorId(long sectorId) {
        if(database==null)
            return null;

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BlindrDbContract.WifiAPSector.COLUMN_NAME_WIFI_AP_SECTOR_ID,
                BlindrDbContract.WifiAPSector.COLUMN_NAME_BSSID,
                BlindrDbContract.WifiAPSector.COLUMN_NAME_SECTOR_ID
        };

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection =
                BlindrDbContract.WifiAPSector.COLUMN_NAME_SECTOR_ID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                Long.toString(sectorId)
        };

        // How you want the results sorted in the resulting Cursor
        //String sortOrder = FeedEntry.COLUMN_NAME_UPDATED + " DESC";

        Cursor c = database.query(
                BlindrDbContract.WifiAPSector.TABLE_NAME,       // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        ArrayList<WifiAPSector> list = cursorToWifiAPSectorList(c);
        c.close();
        return list;
    }

    public ArrayList<WifiAPSector> getWifiAPSectorByBSSID(String bssid) {
        if(database==null)
            return null;

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BlindrDbContract.WifiAPSector.COLUMN_NAME_WIFI_AP_SECTOR_ID,
                BlindrDbContract.WifiAPSector.COLUMN_NAME_BSSID,
                BlindrDbContract.WifiAPSector.COLUMN_NAME_SECTOR_ID
        };

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection =
                BlindrDbContract.WifiAPSector.COLUMN_NAME_BSSID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                bssid
        };

        // How you want the results sorted in the resulting Cursor
        //String sortOrder = FeedEntry.COLUMN_NAME_UPDATED + " DESC";

        Cursor c = database.query(
                BlindrDbContract.WifiAPSector.TABLE_NAME,       // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        ArrayList<WifiAPSector> list = cursorToWifiAPSectorList(c);
        c.close();
        return list;
    }

    public com.cloupix.blindr.business.SectorView getSectorViewById(long sectorViewId) {
        if(database==null)
            return null;

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BlindrDbContract.SectorView.COLUMN_NAME_SECTOR_VIEW_ID,
                BlindrDbContract.SectorView.COLUMN_NAME_STROKE_N,
                BlindrDbContract.SectorView.COLUMN_NAME_STROKE_E,
                BlindrDbContract.SectorView.COLUMN_NAME_STROKE_S,
                BlindrDbContract.SectorView.COLUMN_NAME_STROKE_W,
                BlindrDbContract.SectorView.COLUMN_NAME_STROKE_NW_SE,
                BlindrDbContract.SectorView.COLUMN_NAME_STROKE_NE_SW,
                BlindrDbContract.SectorView.COLUMN_NAME_SCANNED,
                BlindrDbContract.SectorView.COLUMN_NAME_SECTOR_ID
        };

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection =
                BlindrDbContract.SectorView.COLUMN_NAME_SECTOR_VIEW_ID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                Long.toString(sectorViewId)
        };

        // How you want the results sorted in the resulting Cursor
        //String sortOrder = FeedEntry.COLUMN_NAME_UPDATED + " DESC";

        Cursor c = database.query(
                BlindrDbContract.SectorView.TABLE_NAME,       // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        com.cloupix.blindr.business.SectorView sectorView = cursorToSectorView(c);
        c.close();
        return sectorView;
    }

    public com.cloupix.blindr.business.SectorView getSectorViewBySectorId(long sectorId) {
        if(database==null)
            return null;

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BlindrDbContract.SectorView.COLUMN_NAME_SECTOR_VIEW_ID,
                BlindrDbContract.SectorView.COLUMN_NAME_STROKE_N,
                BlindrDbContract.SectorView.COLUMN_NAME_STROKE_E,
                BlindrDbContract.SectorView.COLUMN_NAME_STROKE_S,
                BlindrDbContract.SectorView.COLUMN_NAME_STROKE_W,
                BlindrDbContract.SectorView.COLUMN_NAME_STROKE_NW_SE,
                BlindrDbContract.SectorView.COLUMN_NAME_STROKE_NE_SW,
                BlindrDbContract.SectorView.COLUMN_NAME_SCANNED,
                BlindrDbContract.SectorView.COLUMN_NAME_SECTOR_ID
        };

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection =
                BlindrDbContract.SectorView.COLUMN_NAME_SECTOR_ID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                Long.toString(sectorId)
        };

        // How you want the results sorted in the resulting Cursor
        //String sortOrder = FeedEntry.COLUMN_NAME_UPDATED + " DESC";

        Cursor c = database.query(
                BlindrDbContract.SectorView.TABLE_NAME,       // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        com.cloupix.blindr.business.SectorView sectorView = cursorToSectorView(c);
        c.close();
        return sectorView;
    }

    //</editor-fold>

    //<editor-fold desc="Update">

    public void updateMap(Map map) {

        if(database==null)
            return;

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(BlindrDbContract.Map.COLUMN_NAME_NAME, map.getName());
        values.put(BlindrDbContract.Map.COLUMN_NAME_HEIGHT, map.getHeight());
        values.put(BlindrDbContract.Map.COLUMN_NAME_WIDTH, map.getWidth());

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection =
                BlindrDbContract.Map.COLUMN_NAME_MAP_ID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                Long.toString(map.getMapId())
        };

        // Insert the new row, returning the primary key value of the new row
        database.update(
                BlindrDbContract.Map.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }

    public void updateSectorView(com.cloupix.blindr.business.SectorView sectorView) {

        if(database==null)
            return;

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(BlindrDbContract.SectorView.COLUMN_NAME_STROKE_N, sectorView.getStroke(com.cloupix.blindr.business.SectorView.STROKE_N) ? 1 : 0);
        values.put(BlindrDbContract.SectorView.COLUMN_NAME_STROKE_E, sectorView.getStroke(com.cloupix.blindr.business.SectorView.STROKE_E) ? 1 : 0);
        values.put(BlindrDbContract.SectorView.COLUMN_NAME_STROKE_S, sectorView.getStroke(com.cloupix.blindr.business.SectorView.STROKE_S) ? 1 : 0);
        values.put(BlindrDbContract.SectorView.COLUMN_NAME_STROKE_W, sectorView.getStroke(com.cloupix.blindr.business.SectorView.STROKE_W) ? 1 : 0);
        values.put(BlindrDbContract.SectorView.COLUMN_NAME_STROKE_NW_SE, sectorView.getStroke(com.cloupix.blindr.business.SectorView.STROKE_NW_SE) ? 1 : 0);
        values.put(BlindrDbContract.SectorView.COLUMN_NAME_STROKE_NW_SE, sectorView.getStroke(com.cloupix.blindr.business.SectorView.STROKE_NW_SE) ? 1 : 0);
        values.put(BlindrDbContract.SectorView.COLUMN_NAME_SCANNED, sectorView.isScanned() ? 1 : 0);

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection =
                BlindrDbContract.SectorView.COLUMN_NAME_SECTOR_VIEW_ID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                Long.toString(sectorView.getSectorViewId())
        };

        // Insert the new row, returning the primary key value of the new row
        database.update(
                BlindrDbContract.SectorView.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }

    //</editor-fold>

    //<editor-fold desc="Delete">

    public void deleteMaps(){

        database.delete(
                BlindrDbContract.Map.TABLE_NAME,
                null,
                null
        );
    }

    public void deleteMapById(long mapId) {

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection =
                BlindrDbContract.Map.COLUMN_NAME_MAP_ID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                Long.toString(mapId)
        };

        database.delete(
                BlindrDbContract.Map.TABLE_NAME,
                selection,
                selectionArgs
        );
    }

    public void deleteSectors(){

        database.delete(
                BlindrDbContract.Sector.TABLE_NAME,
                null,
                null
        );
    }

    public void deleteSectorById(long sectorId) {

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection =
                BlindrDbContract.Sector.COLUMN_NAME_SECTOR_ID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                Long.toString(sectorId)
        };

        database.delete(
                BlindrDbContract.Sector.TABLE_NAME,
                selection,
                selectionArgs
        );
    }

    public void deleteSectorByMapId(long mapId) {

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection =
                BlindrDbContract.Sector.COLUMN_NAME_MAP_ID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                Long.toString(mapId)
        };

        database.delete(
                BlindrDbContract.Sector.TABLE_NAME,
                selection,
                selectionArgs
        );
    }

    public void deleteSectorViews(){

        database.delete(
                BlindrDbContract.SectorView.TABLE_NAME,
                null,
                null
        );
    }

    public void deleteSectorViewById(long sectorViewId) {

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection =
                BlindrDbContract.SectorView.COLUMN_NAME_SECTOR_VIEW_ID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                Long.toString(sectorViewId)
        };

        database.delete(
                BlindrDbContract.SectorView.TABLE_NAME,
                selection,
                selectionArgs
        );
    }

    public void deleteSectorViewBySectorId(long sectorId) {

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection =
                BlindrDbContract.SectorView.COLUMN_NAME_SECTOR_ID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                Long.toString(sectorId)
        };

        database.delete(
                BlindrDbContract.SectorView.TABLE_NAME,
                selection,
                selectionArgs
        );
    }

    public void deleteLectures(){

        database.delete(
                BlindrDbContract.Lecture.TABLE_NAME,
                null,
                null
        );
    }

    public void deleteLectureById(long lectureId) {

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection =
                BlindrDbContract.Lecture.COLUMN_NAME_LECTURE_ID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                Long.toString(lectureId)
        };

        database.delete(
                BlindrDbContract.Lecture.TABLE_NAME,
                selection,
                selectionArgs
        );
    }

    public void deleteLectureBySectorId(long sectorId) {

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection =
                BlindrDbContract.Lecture.COLUMN_NAME_SECTOR_ID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                Long.toString(sectorId)
        };

        database.delete(
                BlindrDbContract.Lecture.TABLE_NAME,
                selection,
                selectionArgs
        );
    }

    public void deleteWifiAPs(){

        database.delete(
                BlindrDbContract.WifiAP.TABLE_NAME,
                null,
                null
        );
    }

    public void deleteWifiAPByBSSID(String bssid) {

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection =
                BlindrDbContract.WifiAP.COLUMN_NAME_BSSID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                bssid
        };

        database.delete(
                BlindrDbContract.WifiAP.TABLE_NAME,
                selection,
                selectionArgs
        );
    }

    public void deleteWifiAPViews(){

        database.delete(
                BlindrDbContract.WifiAPView.TABLE_NAME,
                null,
                null
        );
    }

    public void deleteWifiAPById(long wifiAPViewId) {

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection =
                BlindrDbContract.WifiAPView.COLUMN_NAME_WIFI_AP_VIEW_ID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                Long.toString(wifiAPViewId)
        };

        database.delete(
                BlindrDbContract.WifiAPView.TABLE_NAME,
                selection,
                selectionArgs
        );
    }

    public void deleteWifiAPSectors(){

        database.delete(
                BlindrDbContract.WifiAPSector.TABLE_NAME,
                null,
                null
        );
    }

    public void deleteWifiAPSectorById(long wifiAPSectorId) {

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection =
                BlindrDbContract.WifiAPSector.COLUMN_NAME_SECTOR_ID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                Long.toString(wifiAPSectorId)
        };

        database.delete(
                BlindrDbContract.WifiAPSector.TABLE_NAME,
                selection,
                selectionArgs
        );
    }

    public void deleteWifiAPSectorByBSSID(String bssid) {

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection =
                BlindrDbContract.WifiAPSector.COLUMN_NAME_BSSID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                bssid
        };

        database.delete(
                BlindrDbContract.WifiAPSector.TABLE_NAME,
                selection,
                selectionArgs
        );
    }

    public void deleteWifiAPSectorBySectorId(long sectorId) {

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection =
                BlindrDbContract.WifiAPSector.COLUMN_NAME_SECTOR_ID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                Long.toString(sectorId)
        };

        database.delete(
                BlindrDbContract.WifiAPSector.TABLE_NAME,
                selection,
                selectionArgs
        );
    }

    //</editor-fold>

    //<editor-fold desc="Converters">

    private Map cursorToMap(Cursor cursor){
        if(cursor.isBeforeFirst())
            cursor.moveToFirst();
        if(cursor.isAfterLast())
            return null;

        Map map = new Map();

        map.setMapId(cursor.getLong(
                cursor.getColumnIndexOrThrow(BlindrDbContract.Map.COLUMN_NAME_MAP_ID)));
        map.setName(cursor.getString(
                cursor.getColumnIndexOrThrow(BlindrDbContract.Map.COLUMN_NAME_NAME)));
        map.setHeight(cursor.getInt(
                cursor.getColumnIndexOrThrow(BlindrDbContract.Map.COLUMN_NAME_HEIGHT)));
        map.setWidth(cursor.getInt(
                cursor.getColumnIndexOrThrow(BlindrDbContract.Map.COLUMN_NAME_WIDTH)));

        return map;
    }

    private Sector cursorToSector(Cursor cursor){
        if(cursor.isBeforeFirst())
            cursor.moveToFirst();
        if(cursor.isAfterLast())
            return null;

        Sector sector = new Sector();

        sector.setSectorId(cursor.getLong(
                cursor.getColumnIndexOrThrow(BlindrDbContract.Sector.COLUMN_NAME_SECTOR_ID)));
        sector.setListN(cursor.getInt(
                cursor.getColumnIndexOrThrow(BlindrDbContract.Sector.COLUMN_NAME_LIST_N)));
        sector.setMatrixX(cursor.getInt(
                cursor.getColumnIndexOrThrow(BlindrDbContract.Sector.COLUMN_NAME_MATRIX_X)));
        sector.setMatrixY(cursor.getInt(
                cursor.getColumnIndexOrThrow(BlindrDbContract.Sector.COLUMN_NAME_MATRIX_Y)));
        sector.setMatrixY(cursor.getInt(
                cursor.getColumnIndexOrThrow(BlindrDbContract.Sector.COLUMN_NAME_MATRIX_Y)));
        sector.setLatitude(cursor.getDouble(
                cursor.getColumnIndexOrThrow(BlindrDbContract.Sector.COLUMN_NAME_LATITUDE)));
        sector.setLongitude(cursor.getDouble(
                cursor.getColumnIndexOrThrow(BlindrDbContract.Sector.COLUMN_NAME_LONGITUDE)));
        sector.setMapId(cursor.getLong(
                cursor.getColumnIndexOrThrow(BlindrDbContract.Sector.COLUMN_NAME_MAP_ID)));

        return sector;
    }

    private Lecture cursorToLecture(Cursor cursor){
        if(cursor.isBeforeFirst())
            cursor.moveToFirst();
        if(cursor.isAfterLast())
            return null;

        Lecture lecture = new Lecture();

        lecture.setLectureId(cursor.getLong(
                cursor.getColumnIndexOrThrow(BlindrDbContract.Lecture.COLUMN_NAME_LECTURE_ID)));
        lecture.setMathGenerated(cursor.getInt(
                cursor.getColumnIndexOrThrow(BlindrDbContract.Lecture.COLUMN_NAME_MATH_GENERATED)));
        lecture.setLevel(cursor.getInt(
                cursor.getColumnIndexOrThrow(BlindrDbContract.Lecture.COLUMN_NAME_LEVEL)));
        lecture.setFrequency(cursor.getInt(
                cursor.getColumnIndexOrThrow(BlindrDbContract.Lecture.COLUMN_NAME_FRENQUENCY)));
        lecture.setTimestamp(cursor.getLong(
                cursor.getColumnIndexOrThrow(BlindrDbContract.Lecture.COLUMN_NAME_TIMESTAMP)));
        lecture.setBSSID(cursor.getString(
                cursor.getColumnIndexOrThrow(BlindrDbContract.Lecture.COLUMN_NAME_BSSID)));
        lecture.setSectorId(cursor.getLong(
                cursor.getColumnIndexOrThrow(BlindrDbContract.Lecture.COLUMN_NAME_SECTOR_ID)));

        return lecture;
    }

    private WifiAP cursorToWifiAP(Cursor cursor){
        if(cursor.isBeforeFirst())
            cursor.moveToFirst();
        if(cursor.isAfterLast())
            return null;

        WifiAP wifiAP = new WifiAP();

        wifiAP.setBSSID(cursor.getString(
                cursor.getColumnIndexOrThrow(BlindrDbContract.WifiAP.COLUMN_NAME_BSSID)));
        wifiAP.setSSID(cursor.getString(
                cursor.getColumnIndexOrThrow(BlindrDbContract.WifiAP.COLUMN_NAME_SSID)));

        return wifiAP;
    }

    private WifiAPView cursorToWifiAPView(Cursor cursor){
        if(cursor.isBeforeFirst())
            cursor.moveToFirst();
        if(cursor.isAfterLast())
            return null;

        WifiAPView wifiAPView = new WifiAPView();

        wifiAPView.setWifiAPViewId(cursor.getLong(
                cursor.getColumnIndexOrThrow(BlindrDbContract.WifiAPView.COLUMN_NAME_WIFI_AP_VIEW_ID)));
        wifiAPView.setBackgroundImage(cursor.getString(
                cursor.getColumnIndexOrThrow(BlindrDbContract.WifiAPView.COLUMN_NAME_BACKGROUND_IMAGE)));
        wifiAPView.setApNumber(cursor.getInt(
                cursor.getColumnIndexOrThrow(BlindrDbContract.WifiAPView.COLUMN_NAME_AP_NUMBER)));
        wifiAPView.setBSSID(cursor.getString(
                cursor.getColumnIndexOrThrow(BlindrDbContract.WifiAPView.COLUMN_NAME_BSSID)));

        return wifiAPView;
    }

    private com.cloupix.blindr.business.SectorView cursorToSectorView(Cursor cursor){
        if(cursor.isBeforeFirst())
            cursor.moveToFirst();
        if(cursor.isAfterLast())
            return null;

        SectorView sectorView = new com.cloupix.blindr.business.SectorView();

        sectorView.setSectorViewId(cursor.getLong(
                cursor.getColumnIndexOrThrow(BlindrDbContract.SectorView.COLUMN_NAME_SECTOR_VIEW_ID)));
        sectorView.setStroke(com.cloupix.blindr.business.SectorView.STROKE_N, cursor.getInt(
                cursor.getColumnIndexOrThrow(BlindrDbContract.SectorView.COLUMN_NAME_STROKE_N)));
        sectorView.setStroke(com.cloupix.blindr.business.SectorView.STROKE_E, cursor.getInt(
                cursor.getColumnIndexOrThrow(BlindrDbContract.SectorView.COLUMN_NAME_STROKE_E)));
        sectorView.setStroke(com.cloupix.blindr.business.SectorView.STROKE_S, cursor.getInt(
                cursor.getColumnIndexOrThrow(BlindrDbContract.SectorView.COLUMN_NAME_STROKE_S)));
        sectorView.setStroke(com.cloupix.blindr.business.SectorView.STROKE_W, cursor.getInt(
                cursor.getColumnIndexOrThrow(BlindrDbContract.SectorView.COLUMN_NAME_STROKE_W)));
        sectorView.setStroke(com.cloupix.blindr.business.SectorView.STROKE_NW_SE, cursor.getInt(
                cursor.getColumnIndexOrThrow(BlindrDbContract.SectorView.COLUMN_NAME_STROKE_NW_SE)));
        sectorView.setStroke(com.cloupix.blindr.business.SectorView.STROKE_NE_SW, cursor.getInt(
                cursor.getColumnIndexOrThrow(BlindrDbContract.SectorView.COLUMN_NAME_STROKE_NE_SW)));
        sectorView.setScanned(cursor.getInt(
                cursor.getColumnIndexOrThrow(BlindrDbContract.SectorView.COLUMN_NAME_SCANNED)));
        sectorView.setSectorId(cursor.getLong(
                cursor.getColumnIndexOrThrow(BlindrDbContract.SectorView.COLUMN_NAME_SECTOR_ID)));

        return sectorView;
    }

    private WifiAPSector cursorToWifiAPSector(Cursor cursor){
        if(cursor.isBeforeFirst())
            cursor.moveToFirst();
        if(cursor.isAfterLast())
            return null;

        WifiAPSector wifiAPSector = new WifiAPSector();

        wifiAPSector.setWifiAPSectorId(cursor.getLong(
                cursor.getColumnIndexOrThrow(BlindrDbContract.WifiAPSector.COLUMN_NAME_WIFI_AP_SECTOR_ID)));
        wifiAPSector.setBSSID(cursor.getString(
                cursor.getColumnIndexOrThrow(BlindrDbContract.WifiAPSector.COLUMN_NAME_BSSID)));
        wifiAPSector.setSectorId(cursor.getLong(
                cursor.getColumnIndexOrThrow(BlindrDbContract.WifiAPSector.COLUMN_NAME_SECTOR_ID)));

        return wifiAPSector;
    }


    private ArrayList<Map> cursorToMapList(Cursor cursor){
        ArrayList<Map> arrayList = new ArrayList<>();

        while(cursor.moveToNext()){
            arrayList.add(cursorToMap(cursor));
        }

        return arrayList;
    }

    private ArrayList<Sector> cursorToSectorList(Cursor cursor){
        ArrayList<Sector> arrayList = new ArrayList<>();

        while(cursor.moveToNext()){
            arrayList.add(cursorToSector(cursor));
        }

        return arrayList;
    }

    private ArrayList<Lecture> cursorToLectureList(Cursor cursor){
        ArrayList<Lecture> arrayList = new ArrayList<>();

        while(cursor.moveToNext()){
            arrayList.add(cursorToLecture(cursor));
        }

        return arrayList;
    }

    private ArrayList<WifiAP> cursorToWifiAPList(Cursor cursor){
        ArrayList<WifiAP> arrayList = new ArrayList<>();

        while(cursor.moveToNext()){
            arrayList.add(cursorToWifiAP(cursor));
        }

        return arrayList;
    }

    private ArrayList<WifiAPView> cursorToWifiAPViewList(Cursor cursor){
        ArrayList<WifiAPView> arrayList = new ArrayList<>();

        while(cursor.moveToNext()){
            arrayList.add(cursorToWifiAPView(cursor));
        }

        return arrayList;
    }

    private ArrayList<WifiAPSector> cursorToWifiAPSectorList(Cursor cursor){
        ArrayList<WifiAPSector> arrayList = new ArrayList<>();

        while(cursor.moveToNext()){
            arrayList.add(cursorToWifiAPSector(cursor));
        }

        return arrayList;
    }

    private ArrayList<com.cloupix.blindr.business.SectorView> cursorToSectorViewList(Cursor cursor){
        ArrayList<com.cloupix.blindr.business.SectorView> arrayList = new ArrayList<>();

        while(cursor.moveToNext()){
            arrayList.add(cursorToSectorView(cursor));
        }

        return arrayList;
    }

    //</editor-fold>

}