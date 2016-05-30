package com.cloupix.blindr.business.comparators;

import com.cloupix.blindr.business.Reading;

import java.util.Comparator;

/**
 * Created by alonsousa on 10/12/15.
 *
 */
public class WifiAPLectureComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {

        Reading reading1 = (Reading) o1;
        Reading reading2 = (Reading) o2;




        // + if o1 > o2
        // 0 if o1 = o2
        // - if o2 > o1

        // Los he invertido porque lo quiero d emayor a menor
        if (reading1.getLevel() > reading2.getLevel()) {
            return -1;
        }else if(reading1.getLevel() < reading2.getLevel()) {
            return 1;
        }else {
            return 0;
        }
    }
}
