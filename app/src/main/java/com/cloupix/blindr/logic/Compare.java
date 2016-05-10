package com.cloupix.blindr.logic;

import com.cloupix.blindr.business.Lecture;
import com.cloupix.blindr.business.Map;
import com.cloupix.blindr.business.Sector;


public class Compare {

    /*
    public double[] cloupix1 = {};
    public double[] cloupix2 = {};
    public double[] cloupix3 = {};
    public double[] cloupix4 = {};
    public double[] cloupix5 = {};

    public double[] cloupix1map = {};
    public double[] cloupix2map = {};
    public double[] cloupix3map = {};
    public double[] cloupix4map = {};
    public double[] cloupix5map = {};
    */
    public double[] ap1 = {};
    public double[] ap2 = {};
    public double[] ap3 = {};

    public double[] ap1map = {};
    public double[] ap2map = {};
    public double[] ap3map = {};

    public void clasifyLevels (Lecture[] lectures){
        /*
        for (int i = 0; i < lectures.length; i++){
            switch (lectures[i].getBSSID()){
                case "aaaa": cloupix1[cloupix1.length] = lectures[i].getLevel();
                    break;
                case "bbbb": cloupix2[cloupix2.length] = lectures[i].getLevel();
                    break;
                case "cccc": cloupix3[cloupix3.length] = lectures[i].getLevel();
                    break;
                case "dddd": cloupix4[cloupix4.length] = lectures[i].getLevel();
                    break;
                case "eeee": cloupix5[cloupix5.length] = lectures[i].getLevel();
                    break;
            }
        }
        */

        for (int i = 0; i < lectures.length; i++){
            switch (lectures[i].getBSSID()){
                case "30:b5:c2:62:54:1d": ap1[ap1.length] = lectures[i].getLevel();
                    break;
                case "20:c9:d0:d0:28:df": ap2[ap2.length] = lectures[i].getLevel();
                    break;
                case "d8:fe:e3:95:47:40": ap3[ap3.length] = lectures[i].getLevel();
                    break;
            }
        }
    }

    public void clasifyLevelsMap (Lecture[] lectures){
        /*
        for (int i = 0; i < lectures.length; i++){
            switch (lectures[i].getBSSID()){
                case "aaaa": cloupix1map[cloupix1map.length] = lectures[i].getLevel();
                    break;
                case "bbbb": cloupix2map[cloupix2map.length] = lectures[i].getLevel();
                    break;
                case "cccc": cloupix3map[cloupix3map.length] = lectures[i].getLevel();
                    break;
                case "dddd": cloupix4map[cloupix4map.length] = lectures[i].getLevel();
                    break;
                case "eeee": cloupix5map[cloupix5map.length] = lectures[i].getLevel();
                    break;
            }
        }
        */
        for (int i = 0; i < lectures.length; i++){
            switch (lectures[i].getBSSID()){
                case "30:b5:c2:62:54:1d": ap1map[ap1map.length] = lectures[i].getLevel();
                    break;
                case "20:c9:d0:d0:28:df": ap2map[ap2map.length] = lectures[i].getLevel();
                    break;
                case "d8:fe:e3:95:47:40": ap3map[ap3map.length] = lectures[i].getLevel();
                    break;
            }
        }

    }

    //Distancia a un sector
    public double distanceToSector (Lecture[] lectures, Sector sector){

        Lecture[] lecturesFromSector = new Lecture[sector.getLectures().size()];
        for (int i = 0; i < sector.getLectures().size(); i++){
            lecturesFromSector[i] = sector.getLectures().get(i);
        }

        clasifyLevels(lectures);
        clasifyLevelsMap(lecturesFromSector);

        double distance = 0;

        /*
        distance = distance +
                Math.pow(avDiference(cloupix1map, cloupix1), 2) +
                Math.pow(avDiference(cloupix2map, cloupix2), 2) +
                Math.pow(avDiference(cloupix3map, cloupix3), 2) +
                Math.pow(avDiference(cloupix4map, cloupix4), 2) +
                Math.pow(avDiference(cloupix5map, cloupix5), 2);
        */

        distance = distance +
                Math.pow(avDiference(ap1map, ap1), 2) +
                Math.pow(avDiference(ap2map, ap2), 2) +
                Math.pow(avDiference(ap3map, ap3), 2);

        distance = Math.sqrt(distance);

        return distance;
    }


    //Para obtener los sectores más cercanos partiendo de unas lecturas
    //Se le pasan las lecturas, el mapa, y el número de sectores más cercanos que se quiere
    public Sector[] getNearestSectors (Lecture[] lectures, Map map, int numberOfNearest){
        Sector[] sectors = map.getaSectors();

        double distance = 0;
        boolean first = true;
        int contNumberOfNearest = 0;
        double[] smallestDist = new double[numberOfNearest];
        Sector[] nearestSectors = new Sector[numberOfNearest];

        for (int i = 0; i < sectors.length; i++){

            if(sectors[i].getLectures() != null){


                distance = distanceToSector(lectures, sectors[i]);


                if(first){
                    smallestDist[0] = distance;
                    nearestSectors[0] = sectors[i];
                    first = false;
                }else{
                    for (int j = 0; j < numberOfNearest; j++){
                        if (smallestDist[j] != 0){
                            if (smallestDist[j] >= distance){
                                for (int k = 0; (numberOfNearest-1-k-1) >= j; k++){
                                    smallestDist[(numberOfNearest-1-k)] = smallestDist[(numberOfNearest-1-k-1)];
                                    nearestSectors[(numberOfNearest-1-k)] = nearestSectors[(numberOfNearest-1-k-1)];
                                }
                                smallestDist[j] = distance;
                                nearestSectors[j] = sectors[i];
                            }
                        }else{
                            smallestDist[j] = distance;
                            nearestSectors[j] = sectors[i];
                        }
                    }
                }
            }


        }

        return nearestSectors;

    }


    //Average de un array de levels
    public double getAverageRSSI(double[] levels){
        double average = 0;
        for (int i = 0; i<levels.length; i++){
            average = average + levels[i];
        }
        average = average/levels.length;
        return average;
    }

    //Resta entre las medias de cada AP para la Euclidean
    public double avDiference (double[] apMap, double[] apRead){
        return (getAverageRSSI(apMap) - getAverageRSSI(apRead));
    }
}
