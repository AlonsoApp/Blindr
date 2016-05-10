package com.cloupix.blindr.business.comparators;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by alonsoapp on 12/04/16.
 */
public class LevelMapComparator implements Comparator {

    Map<Integer, Integer> base;

    public LevelMapComparator(Map base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with
    // equals.
    public int compare(Object a, Object b) {

        if ((Integer)a >= (Integer)b) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}
