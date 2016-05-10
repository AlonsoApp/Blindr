package com.cloupix.blindr.dao;

import android.provider.BaseColumns;

/**
 * Created by alonsoapp on 23/04/16.
 *
 */
public class BlindrDbContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public BlindrDbContract() {}


    //<editor-fold desc="Tables">

    public static abstract class Map implements BaseColumns {

        public static final String TABLE_NAME = "map";
        public static final String COLUMN_NAME_MAP_ID = "map_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_HEIGHT = "height";
        public static final String COLUMN_NAME_WIDTH = "width";
    }

    public static abstract class Sector implements BaseColumns {

        public static final String TABLE_NAME = "sector";
        public static final String COLUMN_NAME_SECTOR_ID = "sector_id";
        public static final String COLUMN_NAME_LIST_N = "list_n";
        public static final String COLUMN_NAME_MATRIX_X = "matrix_x";
        public static final String COLUMN_NAME_MATRIX_Y = "matrix_y";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_MAP_ID = "map_id";
    }

    public static abstract class SectorView implements BaseColumns {

        public static final String TABLE_NAME = "sector_view";
        public static final String COLUMN_NAME_SECTOR_VIEW_ID = "sector_view_id";
        public static final String COLUMN_NAME_STROKE_N = "stroke_n";
        public static final String COLUMN_NAME_STROKE_E = "stroke_e";
        public static final String COLUMN_NAME_STROKE_S = "stroke_s";
        public static final String COLUMN_NAME_STROKE_W = "stroke_w";
        public static final String COLUMN_NAME_STROKE_NW_SE = "stroke_nw_se";
        public static final String COLUMN_NAME_STROKE_NE_SW = "stroke_ne_sw";
        public static final String COLUMN_NAME_SCANNED = "scanned";
        public static final String COLUMN_NAME_SECTOR_ID = "sector_id";
    }

    public static abstract class Lecture implements BaseColumns {

        public static final String TABLE_NAME = "lecture";
        public static final String COLUMN_NAME_LECTURE_ID = "lecture_id";
        public static final String COLUMN_NAME_MATH_GENERATED = "math_generated";
        public static final String COLUMN_NAME_LEVEL = "level";
        public static final String COLUMN_NAME_FRENQUENCY = "frequency";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
        public static final String COLUMN_NAME_SECTOR_ID = "sector_id";
        public static final String COLUMN_NAME_BSSID = "bssid";
    }

    public static abstract class WifiAP implements BaseColumns {

        public static final String TABLE_NAME = "wifi_ap";
        public static final String COLUMN_NAME_BSSID = "bssid";
        public static final String COLUMN_NAME_SSID = "ssid";
    }

    public static abstract class WifiAPSector implements BaseColumns {

        public static final String TABLE_NAME = "wifi_ap_sector";
        public static final String COLUMN_NAME_WIFI_AP_SECTOR_ID = "wifi_ap_sector_id";
        public static final String COLUMN_NAME_BSSID = "bssid";
        public static final String COLUMN_NAME_SECTOR_ID = "sector_id";
    }

    public static abstract class WifiAPView implements BaseColumns {

        public static final String TABLE_NAME = "wifi_ap_view";
        public static final String COLUMN_NAME_WIFI_AP_VIEW_ID = "wifi_ap_view_id";
        public static final String COLUMN_NAME_BACKGROUND_CIRCLE = "background_circle";
        public static final String COLUMN_NAME_AP_NUMBER = "ap_number";
        public static final String COLUMN_NAME_WIFI_AP_SECTOR_ID = "wifi_ap_sector_id";
    }

    //</editor-fold>


    //<editor-fold desc="Statements">

    private static final String TEXT_TYPE = " TEXT";
    private static final String REAL_TYPE = " REAL";
    private static final String INTEGER_TYPE = " INTEGER";

    private static final String COMMA_SEP = ", ";

    public static final String SQL_CREATE_TABLE_MAP =
            "CREATE TABLE " + Map.TABLE_NAME + " (" +
                    Map.COLUMN_NAME_MAP_ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                    Map.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    Map.COLUMN_NAME_HEIGHT + INTEGER_TYPE + COMMA_SEP +
                    Map.COLUMN_NAME_WIDTH + INTEGER_TYPE + COMMA_SEP +
                    " UNIQUE (" + Map.COLUMN_NAME_MAP_ID + ") ON CONFLICT REPLACE" +
                    " );";
    public static final String SQL_CREATE_TABLE_SECTOR =
            "CREATE TABLE " + Sector.TABLE_NAME + " (" +
                    Sector.COLUMN_NAME_SECTOR_ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                    Sector.COLUMN_NAME_LIST_N + INTEGER_TYPE + COMMA_SEP +
                    Sector.COLUMN_NAME_MATRIX_X + INTEGER_TYPE + COMMA_SEP +
                    Sector.COLUMN_NAME_MATRIX_Y + INTEGER_TYPE + COMMA_SEP +
                    Sector.COLUMN_NAME_LATITUDE + REAL_TYPE + COMMA_SEP +
                    Sector.COLUMN_NAME_LONGITUDE + REAL_TYPE + COMMA_SEP +
                    Sector.COLUMN_NAME_MAP_ID + INTEGER_TYPE + COMMA_SEP +
                    " FOREIGN KEY ("+ Sector.COLUMN_NAME_MAP_ID+")" +
                    " REFERENCES "+ Map.TABLE_NAME+" ("+ Map.COLUMN_NAME_MAP_ID+") ON DELETE CASCADE" +
                    " UNIQUE (" + Sector.COLUMN_NAME_SECTOR_ID + ") ON CONFLICT REPLACE" +
                    " );";
    public static final String SQL_CREATE_TABLE_SECTOR_VIEW =
            "CREATE TABLE " + SectorView.TABLE_NAME + " (" +
                    SectorView.COLUMN_NAME_SECTOR_VIEW_ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                    SectorView.COLUMN_NAME_STROKE_N + INTEGER_TYPE + COMMA_SEP +
                    SectorView.COLUMN_NAME_STROKE_E + INTEGER_TYPE + COMMA_SEP +
                    SectorView.COLUMN_NAME_STROKE_S + INTEGER_TYPE + COMMA_SEP +
                    SectorView.COLUMN_NAME_STROKE_W + INTEGER_TYPE + COMMA_SEP +
                    SectorView.COLUMN_NAME_STROKE_NW_SE + INTEGER_TYPE + COMMA_SEP +
                    SectorView.COLUMN_NAME_STROKE_NE_SW + INTEGER_TYPE + COMMA_SEP +
                    SectorView.COLUMN_NAME_SCANNED + INTEGER_TYPE + COMMA_SEP +
                    SectorView.COLUMN_NAME_SECTOR_ID + INTEGER_TYPE + COMMA_SEP +
                    " FOREIGN KEY ("+ SectorView.COLUMN_NAME_SECTOR_ID+")" +
                    " REFERENCES "+ Sector.TABLE_NAME+" ("+ Sector.COLUMN_NAME_SECTOR_ID+") ON DELETE CASCADE" +
                    " UNIQUE (" + SectorView.COLUMN_NAME_SECTOR_VIEW_ID + ") ON CONFLICT REPLACE" +
                    " );";
    public static final String SQL_CREATE_TABLE_LECTURE =
            "CREATE TABLE " + Lecture.TABLE_NAME + " (" +
                    Lecture.COLUMN_NAME_LECTURE_ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                    Lecture.COLUMN_NAME_MATH_GENERATED + INTEGER_TYPE + COMMA_SEP +
                    Lecture.COLUMN_NAME_LEVEL + INTEGER_TYPE + COMMA_SEP +
                    Lecture.COLUMN_NAME_FRENQUENCY + INTEGER_TYPE + COMMA_SEP +
                    Lecture.COLUMN_NAME_TIMESTAMP + INTEGER_TYPE + COMMA_SEP +
                    Lecture.COLUMN_NAME_BSSID + TEXT_TYPE + COMMA_SEP +
                    Lecture.COLUMN_NAME_SECTOR_ID + INTEGER_TYPE + COMMA_SEP +
                    " FOREIGN KEY ("+ Lecture.COLUMN_NAME_BSSID+")" +
                    " REFERENCES "+ WifiAP.TABLE_NAME+" ("+ WifiAP.COLUMN_NAME_BSSID+") " +
                    " FOREIGN KEY ("+ Lecture.COLUMN_NAME_SECTOR_ID+")" +
                    " REFERENCES "+ Sector.TABLE_NAME+" ("+ Sector.COLUMN_NAME_SECTOR_ID+") ON DELETE CASCADE" +
                    " UNIQUE (" + Lecture.COLUMN_NAME_LECTURE_ID + ") ON CONFLICT REPLACE" +
                    " );";
    public static final String SQL_CREATE_TABLE_WIFI_AP =
            "CREATE TABLE " + WifiAP.TABLE_NAME + " (" +
                    WifiAP.COLUMN_NAME_BSSID + TEXT_TYPE + " PRIMARY KEY " + COMMA_SEP +
                    WifiAP.COLUMN_NAME_SSID + TEXT_TYPE + COMMA_SEP +
                    " UNIQUE (" + WifiAP.COLUMN_NAME_BSSID + ") ON CONFLICT REPLACE" +
                    " );";
    public static final String SQL_CREATE_TABLE_WIFI_AP_VIEW =
            "CREATE TABLE " + WifiAPView.TABLE_NAME + " (" +
                    WifiAPView.COLUMN_NAME_WIFI_AP_VIEW_ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                    WifiAPView.COLUMN_NAME_BACKGROUND_CIRCLE + INTEGER_TYPE + COMMA_SEP +
                    WifiAPView.COLUMN_NAME_AP_NUMBER + INTEGER_TYPE + COMMA_SEP +
                    WifiAPView.COLUMN_NAME_WIFI_AP_SECTOR_ID + INTEGER_TYPE + COMMA_SEP +
                    " FOREIGN KEY ("+ WifiAPView.COLUMN_NAME_WIFI_AP_SECTOR_ID+")" +
                    " REFERENCES "+ WifiAPSector.TABLE_NAME+" ("+ WifiAPSector.COLUMN_NAME_WIFI_AP_SECTOR_ID+") ON DELETE CASCADE" +
                    " UNIQUE (" + WifiAPView.COLUMN_NAME_WIFI_AP_VIEW_ID + ") ON CONFLICT REPLACE" +
                    " );";
    public static final String SQL_CREATE_TABLE_WIFI_AP_SECTOR =
            "CREATE TABLE " + WifiAPSector.TABLE_NAME + " (" +
                    WifiAPSector.COLUMN_NAME_WIFI_AP_SECTOR_ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                    WifiAPSector.COLUMN_NAME_BSSID + TEXT_TYPE + COMMA_SEP +
                    WifiAPSector.COLUMN_NAME_SECTOR_ID + INTEGER_TYPE + COMMA_SEP +
                    " FOREIGN KEY ("+ WifiAPSector.COLUMN_NAME_BSSID+")" +
                    " REFERENCES "+ WifiAP.TABLE_NAME+" ("+ WifiAP.COLUMN_NAME_BSSID+") ON DELETE CASCADE" +
                    " FOREIGN KEY ("+ WifiAPSector.COLUMN_NAME_SECTOR_ID+")" +
                    " REFERENCES "+ Sector.TABLE_NAME+" ("+ Sector.COLUMN_NAME_SECTOR_ID+") ON DELETE CASCADE" +
                    " UNIQUE (" + WifiAPSector.COLUMN_NAME_WIFI_AP_SECTOR_ID + ") ON CONFLICT REPLACE" +
                    " );";

    public static final String SQL_DELETE_TABLE_MAP =
            "DROP TABLE IF EXISTS " + Map.TABLE_NAME;
    public static final String SQL_DELETE_TABLE_SECTOR =
            "DROP TABLE IF EXISTS " + Sector.TABLE_NAME;
    public static final String SQL_DELETE_TABLE_SECTOR_VIEW =
            "DROP TABLE IF EXISTS " + SectorView.TABLE_NAME;
    public static final String SQL_DELETE_TABLE_LECTURE =
            "DROP TABLE IF EXISTS " + Lecture.TABLE_NAME;
    public static final String SQL_DELETE_TABLE_WIFI_AP =
            "DROP TABLE IF EXISTS " + WifiAP.TABLE_NAME;
    public static final String SQL_DELETE_TABLE_WIFI_AP_VIEW =
            "DROP TABLE IF EXISTS " + WifiAPView.TABLE_NAME;
    public static final String SQL_DELETE_TABLE_WIFI_AP_SECTOR =
            "DROP TABLE IF EXISTS " + WifiAPSector.TABLE_NAME;

    //</editor-fold>
}
