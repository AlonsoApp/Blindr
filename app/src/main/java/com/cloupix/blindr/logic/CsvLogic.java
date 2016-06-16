package com.cloupix.blindr.logic;

import android.os.Environment;

import com.cloupix.blindr.business.Sector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by alonsoapp on 07/06/16.
 *
 */
public class CsvLogic {

    private final String ROW_SEPARATOR = "\n";

    private final String columnString =   "\"Time\",\"Manual\",\"Mapped\",\"Math\",\"All\"";
    private String csvStr = "";

    public void createCsv(){
        this.csvStr = columnString;
    }

    /*
    public void addRow(long time, int manualSectorListN, int mappedSectorListN, int mathSectorListN, int allSectorListN){

        csvStr += ROW_SEPARATOR + "\"" + time +"\",\"" + sectorPosition +"\",\"" + readingType + "\"";

    }
    */

    public void addRow(long time, int manualListN){
        csvStr += ROW_SEPARATOR + "\"" + time +"\",\"" + manualListN + "\",";
    }

    public void addElement(int listN, int readingType){
        csvStr +=  "\"" + listN + "\"";

        if(readingType != Sector.ALL_READINGS)
            csvStr +=  ",";
    }

    public void exportCsv(){
        File file   = null;
        File root   = Environment.getExternalStorageDirectory();
        if (root.canWrite()){

            String exportPath = Environment.getExternalStorageDirectory().getPath().concat("/");
            File dir    =   new File (exportPath);
            file   =   new File(dir, "test.csv");
            FileOutputStream out   =   null;
            try {
                out = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                if(out!=null)
                    out.write(csvStr.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(out!=null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
