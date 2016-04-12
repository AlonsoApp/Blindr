package com.cloupix.blindr.business;

import java.util.Comparator;

/**
 * Created by alonsousa on 10/12/15.
 *
 */
public class WifiAPLectureComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {

        WifiAPLecture lecture1 = (WifiAPLecture) o1;
        WifiAPLecture lecture2 = (WifiAPLecture) o2;




        // + if o1 > o2
        // 0 if o1 = o2
        // - if o2 > o1

        // Los he invertido porque lo quiero d emayor a menor
        if (lecture1.getScanResult().level > lecture2.getScanResult().level) {
            return -1;
        }else if(lecture1.getScanResult().level < lecture2.getScanResult().level) {
            return 1;
        }else {
            return 0;
        }
    }
}
