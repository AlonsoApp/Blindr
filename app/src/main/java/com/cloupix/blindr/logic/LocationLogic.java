package com.cloupix.blindr.logic;

import com.cloupix.blindr.business.Reading;
import com.cloupix.blindr.business.Map;
import com.cloupix.blindr.business.Sector;
import com.cloupix.blindr.business.WifiAP;

import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alonsoapp on 08/05/16.
 *
 */
public class LocationLogic {

    ArrayList<Integer> filteredValues = new ArrayList<Integer>();

    public void compareMathGeneratedReadings(Map map){
        ArrayList<WifiAP> wifiAPsInMap = map.getMapWifiAPs();
        HashMap<String, ArrayList<Integer>> template = getWifiAPLevelHashMap(wifiAPsInMap);
        HashMap<String, ArrayList<Integer>>[] templateWithValues = fillTemplate(map, template, Sector.MAPPED_READINGS);
        HashMap<String, Double>[] mapWithAverageValues = getAverageValuesArray(templateWithValues);

        for(Sector sector : map.getaSectors()){
            HashMap<String, ArrayList<Integer>> readingsWithValues = getWifiAPLevelHashMapFromReadings(sector.getReadings(Sector.MATH_GENERATED_READINGS));
            HashMap<String, ArrayList<Integer>> curatedReadingsWithValues = deleteReadingsFromWifiAPsNotInMap(readingsWithValues, wifiAPsInMap);
            HashMap<String, Double> readingsWithAverageValues = getAverageValueHashMap(curatedReadingsWithValues);
            double euclideanDistance = getEuclideanDistance(mapWithAverageValues[sector.getListN()], readingsWithAverageValues);
            sector.setLocationProbability(euclideanDistance);
            // TODO Primero ver en que orden andan las distancias euclideas para luego meterles probabilidad o algo
        }
    }

    public void getSectorProbabilities(Map map, ArrayList<Reading> readings, int readingType){

        ArrayList<WifiAP> wifiAPsInMap = map.getMapWifiAPs();
        HashMap<String, ArrayList<Integer>> template = getWifiAPLevelHashMap(wifiAPsInMap);
        HashMap<String, ArrayList<Integer>>[] templateWithValues = fillTemplate(map, template, readingType);

        HashMap<String, Double>[] mapWithAverageValues = getAverageValuesArray(templateWithValues);
        HashMap<String, ArrayList<Integer>> readingsWithValues = getWifiAPLevelHashMapFromReadings(readings);
        HashMap<String, ArrayList<Integer>> curatedReadingsWithValues = deleteReadingsFromWifiAPsNotInMap(readingsWithValues, wifiAPsInMap);
        HashMap<String, Double> readingsWithAverageValues = getAverageValueHashMap(curatedReadingsWithValues);
        double[] euclideanDistanceArray = getEuclideanDistanceArray(mapWithAverageValues, readingsWithAverageValues);
        double[] probablilityArray = getProbabilityArray(euclideanDistanceArray);
        double[] correctedProbArray = getCorrectedProbabilityArray(mapWithAverageValues, readingsWithAverageValues, probablilityArray);
        applyProbabilityToSectors(map, correctedProbArray);

    }

    /**
     * Devuelve un HashMap "bssid":(level, level, level)
     * @param readings
     * @return
     */
    private HashMap<String, ArrayList<Integer>> getWifiAPLevelHashMapFromReadings(ArrayList<Reading> readings){
        // Entendemos que no hay dos wifiAPs iguales proque el mismo wifiAP no puede esta en dos sitios
        HashMap<String, ArrayList<Integer>> hashMap = new HashMap<>();
        for(Reading reading : readings){
            if(!hashMap.containsKey(reading.getBSSID()))
                hashMap.put(reading.getBSSID(), new ArrayList<Integer>());
            hashMap.get(reading.getBSSID()).add(reading.getLevel());
        }
        return hashMap;
    }

    /**
     * Elimina las readings de los apes que no esten registrados en el mapa
     * @param readingsWithValues
     * @param wifiAPsInMap
     * @return
     */
    private HashMap<String, ArrayList<Integer>> deleteReadingsFromWifiAPsNotInMap(HashMap<String, ArrayList<Integer>> readingsWithValues, ArrayList<WifiAP> wifiAPsInMap){
        HashMap<String, ArrayList<Integer>> result = new HashMap();
        for(WifiAP wifiAP : wifiAPsInMap)
            if(readingsWithValues.containsKey(wifiAP.getBSSID()))
                result.put(wifiAP.getBSSID(), readingsWithValues.get(wifiAP.getBSSID()));
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
    private HashMap<String, ArrayList<Integer>>[] fillTemplate(Map map, HashMap<String, ArrayList<Integer>> template, int readingType){
        HashMap<String, ArrayList<Integer>>[] result = new HashMap[map.getaSectors().length];
        for(int i=0; i<result.length; i++)
            result[i] = new HashMap<>();
        for(Sector sector : map.getaSectors()){
            for(final Reading reading : sector.getReadings(readingType)) {
                // Descartamso als lecturas que no estén en la plantilla
                if(template.containsKey(reading.getBSSID())){
                    if (!result[sector.getListN()].containsKey(reading.getBSSID()))
                        result[sector.getListN()].put(reading.getBSSID(), new ArrayList<Integer>());
                    result[sector.getListN()].get(reading.getBSSID()).add(reading.getLevel());
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
            result[i]=getFilteredAverageValueHashMap(templateWithValues[i]);
            //result[i]=getAverageValueHashMap(templateWithValues[i]);
            /*
            for(java.util.Map.Entry<String, ArrayList<Integer>> entry: templateWithValues[i].entrySet()){
                result[i].put(entry.getKey(), getAverage(entry.getValue()));
            }
            */
        }
        return result;
    }


    /**
     * Devuelve un HashMap "bssid":media llamando a filteredAverage
     * @param templateWithValues
     * @return
     */
    private HashMap<String, Double> getFilteredAverageValueHashMap(HashMap<String, ArrayList<Integer>> templateWithValues){
        HashMap<String, Double> result = new HashMap<>();
        for(java.util.Map.Entry<String, ArrayList<Integer>> entry: templateWithValues.entrySet()){
            result.put(entry.getKey(), getFilteredAverage(entry.getValue()));
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
     * Devuelve el valor medio de una lista de integers pero eliminando los outliers
     * @param valueList
     * @return
     */
    private double getFilteredAverage(ArrayList<Integer> valueList){

        double meanNotFiltered = 0;
        double range = 0;
        double variance = 0;
        double standardDeviation = 0.5;

        //double[] cumulativeProbabilities = new double[valueList.size()];
        double cumulativeProbability = 0;
        //ArrayList<Integer> filteredValues = new ArrayList<Integer>();
        filteredValues.clear();

        double filteredMean = 0;

        for(int i=0; i<valueList.size(); i++)
            meanNotFiltered += valueList.get(i);

        meanNotFiltered = meanNotFiltered/valueList.size();

        for(int j=0; j<valueList.size(); j++)
            range = range + (Math.pow((valueList.get(j) - meanNotFiltered), 2));

        variance = range / (valueList.size() - 1);

        //standardDeviation = Math.sqrt(variance);

        NormalDistribution normalDistribution = new NormalDistribution(meanNotFiltered, standardDeviation);

        for (int k=0; k<valueList.size(); k++){

            //cumulativeProbability = 1 - normalDistribution.cumulativeProbability(valueList.get(k));
            cumulativeProbability = normalDistribution.cumulativeProbability(valueList.get(k));

            if ((cumulativeProbability * valueList.size()) >= 0.5){
                //filteredValues.add(k, valueList.get(k));
                filteredValues.add(valueList.get(k));
            }
        }

        for(int z=0; z<filteredValues.size(); z++)
            filteredMean += filteredValues.get(z);

        filteredMean = filteredMean/filteredValues.size();

        return filteredMean;
    }

    /**
     * Devuelve un array (posición = sector) con la distancia enuclidea de cada sector
     * @param sectorHashMapArray
     * @param readings
     * @return
     */
    private double[] getEuclideanDistanceArray(HashMap<String, Double>[] sectorHashMapArray, HashMap<String, Double> readings){
        double[] result = new double[sectorHashMapArray.length];
        for(int i =0; i<sectorHashMapArray.length; i++){
            result[i] = getEuclideanDistance(sectorHashMapArray[i], readings);
        }
        return result;
    }
    /**
     * Compara dos HashMap y devuelve la distancia enuclidea que hay entre ellos
     * @param sectorHashMap
     * @param readings
     * @return
     */
    private double getEuclideanDistance(HashMap<String, Double> sectorHashMap, HashMap<String, Double> readings){
        double sum = 0;
        for(java.util.Map.Entry<String, Double> entry: sectorHashMap.entrySet()){
            if(readings.containsKey(entry.getKey()))
                sum += Math.pow(entry.getValue() - readings.get(entry.getKey()), 2);
            // (else) Aqui meteríamos qué hacer si en readings no hay un valor asociado a ese bssid -100
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
     * @param readings
     * @param probabilityArray
     * @return
     */
    private double[] getCorrectedProbabilityArray(HashMap<String, Double>[] sectorHashMapArray, HashMap<String, Double> readings, double[] probabilityArray){
        double[] correctedProbArray = probabilityArray.clone();
        for(int i =0; i<sectorHashMapArray.length; i++){
            // Restamos prob por que el readings no hay un valor que si hay en sector
            for(java.util.Map.Entry<String, Double> entry: sectorHashMapArray[i].entrySet()){
                if(!readings.containsKey(entry.getKey()))
                    correctedProbArray[i] = correctedProbArray[i] - (1.0/sectorHashMapArray[i].size());
            }
            // Restamos prob por que en sector no hay un valor que si hay en readings
            for(java.util.Map.Entry<String, Double> entry: readings.entrySet()){
                if(!sectorHashMapArray[i].containsKey(entry.getKey()))
                    correctedProbArray[i] = correctedProbArray[i] - (1.0/readings.size());
            }
        }
        return correctedProbArray;
    }

    private void applyProbabilityToSectors(Map map, double[] probablilityArray){


        for(int i =0; i<map.getaSectors().length; i++)
            map.getaSectors()[i].setLocationProbability(probablilityArray[i]);


        /*

        Sector previousProbSector = new Sector();

        double[] probablilityArray2 = probablilityArray;
        double mayor = 0;
        int posMayor = 0;
        for (int j = 0; j<probablilityArray2.length; j++){
            if (probablilityArray2[j] > mayor){
                mayor = probablilityArray2[j];
                posMayor = j;
            }
        }


        for(int i =0; i<map.getaSectors().length; i++)
            map.getaSectors()[i].setLocationProbability(probablilityArray[i]);


        ArrayList<Sector> sectorsWithProb = new ArrayList<Sector>();
        ArrayList<Sector> mostProbSectors = new ArrayList<Sector>();
        ArrayList<Sector> filteredMostProbSectors = new ArrayList<>();
        double highestProb = 0;
        int highestPosition = 0;
        if (previousProbSector.getReadings().size() != 0){


            //Meter todos los sectores del mapa con la probabilidad
            for (int i =0; i<map.getaSectors().length; i++){
                sectorsWithProb.add(map.getaSectors()[i]);
            }

            //Seleccionar los 5 sectores con mayor prob y meterlos a mostProbSectors
            while (mostProbSectors.size() <= 5){
                for (int j = 0; j<sectorsWithProb.size(); j++){
                    if (sectorsWithProb.get(j).getLocationProbability() > highestProb){
                        highestProb = sectorsWithProb.get(j).getLocationProbability();
                        highestPosition = j;
                    }
                }
                mostProbSectors.add(sectorsWithProb.get(highestPosition));
                sectorsWithProb.get(highestPosition).setLocationProbability(0);
            }

            for(int z=0; z <mostProbSectors.size(); z++){
                double physicalDistance = 0;
                physicalDistance = Math.pow((mostProbSectors.get(z).getMatrixX() - previousProbSector.getMatrixX()), 2)
                        + Math.pow((mostProbSectors.get(z).getMatrixY() - previousProbSector.getMatrixY()), 2);
                physicalDistance = Math.sqrt(physicalDistance);

                if (physicalDistance <= 1){
                    filteredMostProbSectors.add(mostProbSectors.get(z));
                }
            }

            if (filteredMostProbSectors.size() != 0){
                for (int k = 0; k<filteredMostProbSectors.size(); k++){
                    for(int kk = 0; kk<map.getaSectors().length; kk++){
                        if (filteredMostProbSectors.get(k).getSectorId() == map.getaSectors()[kk].getSectorId()){
                            map.getaSectors()[kk].setLocationProbability(filteredMostProbSectors.get(k).getLocationProbability()*2);
                        }
                    }
                }
            }
        }

        */

    }
}
