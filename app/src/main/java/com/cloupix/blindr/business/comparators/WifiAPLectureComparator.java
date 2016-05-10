package com.cloupix.blindr.business.comparators;

import com.cloupix.blindr.business.Lecture;

import java.util.Comparator;

/**
 * Created by alonsousa on 10/12/15.
 *
 */
public class WifiAPLectureComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {

        Lecture lecture1 = (Lecture) o1;
        Lecture lecture2 = (Lecture) o2;




        // + if o1 > o2
        // 0 if o1 = o2
        // - if o2 > o1

        // Los he invertido porque lo quiero d emayor a menor
        if (lecture1.getLevel() > lecture2.getLevel()) {
            return -1;
        }else if(lecture1.getLevel() < lecture2.getLevel()) {
            return 1;
        }else {
            return 0;
        }
    }
}
