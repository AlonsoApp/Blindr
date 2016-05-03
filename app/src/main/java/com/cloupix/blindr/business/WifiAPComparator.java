package com.cloupix.blindr.business;

import java.util.Comparator;

/**
 * Created by alonsousa on 10/12/15.
 *
 */
public class WifiAPComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {

        WifiAP wifiAP1 = (WifiAP) o1;
        WifiAP wifiAP2 = (WifiAP) o2;




        // + if o1 > o2
        // 0 if o1 = o2
        // - if o2 > o1

        // Los he invertido porque lo quiero d emayor a menor
        if (wifiAP1.getLectures().get(wifiAP1.getLectures().size()-1).getLevel() > wifiAP2.getLectures().get(wifiAP2.getLectures().size()-1).getLevel()) {
            return -1;
        }else if(wifiAP1.getLectures().get(wifiAP1.getLectures().size()-1).getLevel() < wifiAP2.getLectures().get(wifiAP2.getLectures().size()-1).getLevel()) {
            return 1;
        }else {
            return 0;
        }
    }
}
