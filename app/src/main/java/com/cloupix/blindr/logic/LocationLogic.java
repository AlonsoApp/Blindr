package com.cloupix.blindr.logic;

import com.cloupix.blindr.business.Lecture;
import com.cloupix.blindr.business.Map;
import com.cloupix.blindr.business.Sector;
import com.cloupix.blindr.business.WifiAP;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alonsoapp on 08/05/16.
 *
 */
public class LocationLogic {


    public void getSectorProbabilities(Map map, ArrayList<Lecture> lectures){

        ArrayList<WifiAP> wifiAPsInMap = map.getMapWifiAPs();
        HashMap<String, ArrayList<Integer>> template = getWifiAPLevelHashMap(wifiAPsInMap);
        HashMap<String, ArrayList<Integer>>[] templateWithValues = fillTemplate(map, template);
        HashMap<String, Double>[] mapWithAverageValues = getAverageValuesArray(templateWithValues);
        HashMap<String, ArrayList<Integer>> lecturesWithValues = getWifiAPLevelHashMapFromLectures(lectures);
        HashMap<String, ArrayList<Integer>> curatedLecturesWithValues = deleteLecturesFromWifiAPsNotInMap(lecturesWithValues, wifiAPsInMap);
        HashMap<String, Double> lecturesWithAverageValues = getAverageValueHashMap(curatedLecturesWithValues);
        double[] euclideanDistanceArray = getEuclideanDistanceArray(mapWithAverageValues, lecturesWithAverageValues);
        double[] probablilityArray = getProbabilityArray(euclideanDistanceArray);
        double[] correctedProbArray = getCorrectedProbabilityArray(mapWithAverageValues, lecturesWithAverageValues, probablilityArray);
        applyProbabilityToSectors(map, correctedProbArray);

    }

    /**
     * Devuelve un HashMap "bssid":(level, level, level)
     * @param lectures
     * @return
     */
    private HashMap<String, ArrayList<Integer>> getWifiAPLevelHashMapFromLectures(ArrayList<Lecture> lectures){
        // Entendemos que no hay dos wifiAPs iguales proque el mismo wifiAP no puede esta en dos sitios
        HashMap<String, ArrayList<Integer>> hashMap = new HashMap<>();
        for(Lecture lecture : lectures){
            if(!hashMap.containsKey(lecture.getBSSID()))
                hashMap.put(lecture.getBSSID(), new ArrayList<Integer>());
            hashMap.get(lecture.getBSSID()).add(lecture.getLevel());
        }
        return hashMap;
    }

    /**
     * Elimina las lectures de los apes que no esten registrados en el mapa
     * @param lecturesWithValues
     * @param wifiAPsInMap
     * @return
     */
    private HashMap<String, ArrayList<Integer>> deleteLecturesFromWifiAPsNotInMap(HashMap<String, ArrayList<Integer>> lecturesWithValues, ArrayList<WifiAP> wifiAPsInMap){
        HashMap<String, ArrayList<Integer>> result = new HashMap();
        for(WifiAP wifiAP : wifiAPsInMap)
            if(lecturesWithValues.containsKey(wifiAP.getBSSID()))
                result.put(wifiAP.getBSSID(), lecturesWithValues.get(wifiAP.getBSSID()));
        return result;
    }

    /**
     * Devuelve (wifiAPLevelValueColectionHashMapArray) la plantilla "bssid":() que usaremos en cada posición del array de sectores
     * @param wifiAPsInMap
     * @return
     */
    private HashMap<String, ArrayList<Integer>> getWifiAPLevelHashMap(ArrayList<WifiAP> wifiAPsInMap){
        // Entendemos que no hay dos wifiAPs iguales proque el mismo wifiAP no puede esta en dos sitios
        HashMap<String, ArrayList<Integer>> map = new HashMap<>();
        for(WifiAP wifiAP : wifiAPsInMap)
            map.put(wifiAP.getBSSID(), new ArrayList<Integer>());
        return map;
    }

    /**
     * Devuelve un array (posición = sector) con la plantilla rellenada con todos los valores de bssid en uns sector
     * @param map
     * @param template
     * @return
     */
    private HashMap<String, ArrayList<Integer>>[] fillTemplate(Map map, HashMap<String, ArrayList<Integer>> template){
        HashMap<String, ArrayList<Integer>>[] result = new HashMap[map.getaSectors().length];
        for(int i=0; i<result.length; i++)
            result[i] = new HashMap<>();
        for(Sector sector : map.getaSectors()){
            for(final Lecture lecture : sector.getLectures()) {
                // Descartamso als lecturas que no estén en la plantilla
                if(template.containsKey(lecture.getBSSID())){
                    if (!result[sector.getListN()].containsKey(lecture.getBSSID()))
                        result[sector.getListN()].put(lecture.getBSSID(), new ArrayList<Integer>());
                    result[sector.getListN()].get(lecture.getBSSID()).add(lecture.getLevel());
                }
            }
        }
        return result;
    }

    /**
     * Devuelve un array (posición = sector) con un HashMap "bssid":media
     * @param templateWithValues
     * @return
     */
    private HashMap<String, Double>[] getAverageValuesArray(HashMap<String, ArrayList<Integer>>[] templateWithValues){
        HashMap<String, Double>[] result = new HashMap[templateWithValues.length];
        for(int i = 0; i<templateWithValues.length; i++){
            result[i]=getAverageValueHashMap(templateWithValues[i]);
            /*
            for(java.util.Map.Entry<String, ArrayList<Integer>> entry: templateWithValues[i].entrySet()){
                result[i].put(entry.getKey(), getAverage(entry.getValue()));
            }
            */
        }
        return result;
    }

    /**
     * Devuelve un HashMap "bssid":media
     * @param templateWithValues
     * @return
     */
    private HashMap<String, Double> getAverageValueHashMap(HashMap<String, ArrayList<Integer>> templateWithValues){
        HashMap<String, Double> result = new HashMap<>();
        for(java.util.Map.Entry<String, ArrayList<Integer>> entry: templateWithValues.entrySet()){
            result.put(entry.getKey(), getAverage(entry.getValue()));
        }
        return result;
    }

    /**
     * Devuelve el valor medio de una lista de integers
     * @param valueList
     * @return
     */
    private double getAverage(ArrayList<Integer> valueList){
        int sum =0;
        for(int i=0; i<valueList.size(); i++)
            sum += valueList.get(i);
        return sum/valueList.size();
    }

    /**
     * Devuelve un array (posición = sector) con la distancia enuclidea de cada sector
     * @param sectorHashMapArray
     * @param lectures
     * @return
     */
    private double[] getEuclideanDistanceArray(HashMap<String, Double>[] sectorHashMapArray, HashMap<String, Double> lectures){
        double[] result = new double[sectorHashMapArray.length];
        for(int i =0; i<sectorHashMapArray.length; i++){
            result[i] = getEuclideanDistance(sectorHashMapArray[i], lectures);
        }
        return result;
    }
    /**
     * Compara dos HashMap y devuelve la distancia enuclidea que hay entre ellos
     * @param sectorHashMap
     * @param lectures
     * @return
     */
    private double getEuclideanDistance(HashMap<String, Double> sectorHashMap, HashMap<String, Double> lectures){
        double sum = 0;
        for(java.util.Map.Entry<String, Double> entry: sectorHashMap.entrySet()){
            if(lectures.containsKey(entry.getKey()))
                sum += Math.pow(entry.getValue() - lectures.get(entry.getKey()), 2);
            // (else) Aqui meteríamos qué hacer si en lectures no hay un valor asociado a ese bssid -100
        }
        return Math.sqrt(sum);
    }

    /**
     * PAsa de distancia euclidea a probabilidad
     * @param euclideanDistanceArray
     * @return
     */
    private double[] getProbabilityArray(double[] euclideanDistanceArray){
        double[] result = new double[euclideanDistanceArray.length];
        for(int i=0; i<euclideanDistanceArray.length; i++)
            result[i] = 1/(1+(euclideanDistanceArray[i]));
        return result;
    }

    /**
     * Aplica el factor de correción que me he inventado. Resta porcentaje de probabilidad
     * equivalente al numero de lecturas que faltan tanto en el sector como en la lectura
     * @param sectorHashMapArray
     * @param lectures
     * @param probabilityArray
     * @return
     */
    private double[] getCorrectedProbabilityArray(HashMap<String, Double>[] sectorHashMapArray, HashMap<String, Double> lectures, double[] probabilityArray){
        double[] correctedProbArray = probabilityArray.clone();
        for(int i =0; i<sectorHashMapArray.length; i++){
            // Restamos prob por que el lectures no hay un valor que si hay en sector
            for(java.util.Map.Entry<String, Double> entry: sectorHashMapArray[i].entrySet()){
                if(!lectures.containsKey(entry.getKey()))
                    correctedProbArray[i] = correctedProbArray[i] - (1.0/sectorHashMapArray[i].size());
            }
            // Restamos prob por que en sector no hay un valor que si hay en lectures
            for(java.util.Map.Entry<String, Double> entry: lectures.entrySet()){
                if(!sectorHashMapArray[i].containsKey(entry.getKey()))
                    correctedProbArray[i] = correctedProbArray[i] - (1.0/lectures.size());
            }
        }
        return correctedProbArray;
    }

    private void applyProbabilityToSectors(Map map, double[] probablilityArray){
        for(int i =0; i<map.getaSectors().length; i++)
            map.getaSectors()[i].setLocationProbability(probablilityArray[i]);
    }
}
