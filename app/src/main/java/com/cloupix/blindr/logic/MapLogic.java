package com.cloupix.blindr.logic;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cloupix.blindr.R;
import com.cloupix.blindr.business.Reading;
import com.cloupix.blindr.business.Map;
import com.cloupix.blindr.business.Sector;
import com.cloupix.blindr.business.SectorView;
import com.cloupix.blindr.business.WifiAP;
import com.cloupix.blindr.business.WifiAPView;
import com.cloupix.blindr.dao.Dao;
import com.cloupix.blindr.logic.network.CommunicationManagerTCP;

import java.io.IOException;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by alonsoapp on 28/04/16.
 *
 */
public class MapLogic {

    public long createMap(String name, int height, int width, Context context) {
        long mapId = -1;
        Dao dao = new Dao(context);
        try{
            dao.open();
            mapId = dao.insertMap(new Map(name, height, width));
            for(int i = 0; i<height*width; i++) {
                int[] matrixXY = getMatrixXYByListN(i, height, width);
                long sectorId = dao.insertSector(new Sector(i, matrixXY[0], matrixXY[1], 0, 0, mapId));
                dao.insertSectorView(new SectorView(sectorId));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(dao.isConnectionOpen())
                dao.close();
        }
        return mapId;
    }

    public Map getMapById(long mapId, Context context) {
        Map map = null;
        Dao dao = new Dao(context);
        try{
            dao.open();
            map = dao.getMapById(mapId);
            ArrayList<Sector> sectorList = dao.getSectorsByMapId(mapId);
            for (Sector sector : sectorList) {
                SectorView sectorView = dao.getSectorViewBySectorId(sector.getSectorId());
                sectorView.mergeSector(sector);
                map.addSector(sectorView, sectorView.getListN());
                sectorView.setReadings(dao.getReadingsBySectorId(sectorView.getSectorId()));
                sectorView.setWifiAPs(getWifiAPViewsBySectorId(sectorView.getSectorId(), dao));
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(dao.isConnectionOpen())
                dao.close();
        }
        return map;
    }

    public ArrayList<Map> getAllMapsEmpty(Context context) {
        ArrayList<Map> mapArrayList = new ArrayList<>();
        Dao dao = new Dao(context);
        try{
            dao.open();
            mapArrayList = dao.getMaps();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(dao.isConnectionOpen())
                dao.close();
        }
        return mapArrayList;
    }

    public void saveMap(Map map, Context context) {
        Dao dao = new Dao(context);
        try{
            dao.open();
            dao.updateMap(map);
            for(Sector sector : map.getaSectors()) {
                dao.updateSector(sector);
                if(sector instanceof SectorView)
                    dao.updateSectorView((SectorView) sector);
                // Readings
                for (Iterator<Reading> it = sector.getReadings().iterator(); it.hasNext(); ){//Reading reading : sector.getReadings()){
                    Reading reading = it.next();
                    // Aqui borramos los readings con delete=true y y luego los borramos de la lista
                    if(reading.isDeleteDBEntity()) {
                        dao.deleteReadingById(reading.getReadingId());
                        it.remove();
                    }else if(reading.isNewDBEntity()){
                        dao.insertReading(reading);
                        reading.setNewDBEntity(false);
                    }
                }
                /*
                dao.deleteReadingBySectorId(sector.getSectorId());
                // Aqui metemos todos los readings que tengan new = true
                for (Reading reading : sector.getReadings())
                    dao.insertReading(reading);
                */
                // Quitar esto
                //dao.deleteWifiAPSectorBySectorId(sector.getSectorId());
                for (Iterator<WifiAP> it = sector.getWifiAPs().iterator(); it.hasNext(); ){
                    WifiAP wifiAP = it.next();
                    // Aqui borramos el WifiAPView si tiene delete=true
                    // Aqui borramos el WifiAPSector que tengan el WifiAPView delete=true
                    // Creamos el WifiAP
                    if(dao.getWifiAPByBSSID(wifiAP.getBSSID()) == null)
                        dao.insertWifiAP(wifiAP);

                    if(wifiAP instanceof WifiAPView){
                        WifiAPView wifiAPView = (WifiAPView) wifiAP;
                        if(wifiAPView.isDeleteDBEntity()){
                            // Aqui borramos el WifiAPView si tiene delete=true
                            dao.deleteWifiAPViewById(wifiAPView.getWifiAPViewId());
                            // Aqui borramos el WifiAPSector que tengan el WifiAPView delete=true
                            dao.deleteWifiAPSectorById(wifiAPView.getWifiAPSectorId());
                            it.remove();
                        }else if(wifiAPView.isNewDBEntity()){
                            // Aqui metemos el WifiAPSector si tiene el WifiAPView new=true
                            long wifiAPSectorId = dao.insertWifiAPSector(new Dao.WifiAPSector(wifiAP.getBSSID(), sector.getSectorId()));
                            // Aqui metemos el WifiAPView con new=true
                            dao.insertWifiAPView((WifiAPView) wifiAP, wifiAPSectorId);
                            wifiAPView.setNewDBEntity(false);
                        }
                    }
                    /*
                    long wifiAPSectorId = dao.insertWifiAPSector(new Dao.WifiAPSector(wifiAP.getBSSID(), sector.getSectorId()));
                    if(wifiAP instanceof WifiAPView) {
                        dao.deleteWifiAPViewById(((WifiAPView) wifiAP).getWifiAPViewId());
                        dao.insertWifiAPView((WifiAPView) wifiAP, wifiAPSectorId);
                    }
                    */
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(dao.isConnectionOpen())
                dao.close();
        }
    }

    public void deleteMap(long mapId, Context context) {
        Dao dao = new Dao(context);
        try{
            dao.open();
            dao.deleteMapById(mapId);
            // No borramos to_do porque esta to_do puesto ON DELETE CASCADE y debería borrarse to_do automáticamente


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(dao.isConnectionOpen())
                dao.close();
        }
    }

    private ArrayList<WifiAP> getWifiAPViewsBySectorId(long sectorId, Dao dao){
        ArrayList<WifiAP> wifiAPs = new ArrayList<>();
        ArrayList<Dao.WifiAPSector> wifiAPSectors = dao.getWifiAPSectorBySectorId(sectorId);
        for(Dao.WifiAPSector wifiAPSector : wifiAPSectors){
            WifiAP wifiAP = dao.getWifiAPByBSSID(wifiAPSector.getBSSID());
            ArrayList<WifiAPView> wifiAPViews = dao.getWifiAPViews();
            WifiAPView wifiAPView = dao.getWifiAPViewByWifiAPSectorId(wifiAPSector.getWifiAPSectorId());
            wifiAPView.mergeWifiAP(wifiAP);
            wifiAPs.add(wifiAPView);
        }
        return wifiAPs;
    }

    public int[] getMatrixXYByListN(int listN, int height, int width) throws Exception {

        if(height<0 || width<0)
            throw new Exception("Se ha intenado sacar el equivanete de X e Y con height="+height+
                    " y width=" + width + ". Los dos valores deben ser mayores que 0");
        else if(listN > (height * width))
            throw new Exception("El valor listN (" + listN + ") es mayor que la resolución de la matrix "
                    + height + "x" + width);

        int x, y;

        y = listN / width;
        x = listN - (width*y);
        return new int[]{x,y};
    }

    public SectorView getSectorViewBySectorId(long sectorId, Context context) {
        SectorView sectorView = null;
        Dao dao = new Dao(context);
        try{
            dao.open();
            Sector sector = dao.getSectorById(sectorId);
            sectorView = dao.getSectorViewBySectorId(sector.getSectorId());
            sectorView.mergeSector(sector);
            sectorView.setReadings(dao.getReadingsBySectorId(sectorView.getSectorId()));
            sectorView.setWifiAPs(getWifiAPViewsBySectorId(sectorView.getSectorId(), dao));


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(dao.isConnectionOpen())
                dao.close();
        }
        return sectorView;
    }

    public boolean existMap(long mapId, Context context) {
        boolean exists = false;
        Dao dao = new Dao(context);
        try{
            dao.open();
            exists = !(dao.getMapById(mapId)==null);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(dao.isConnectionOpen())
                dao.close();
        }
        return exists;
    }

    public void generateLatLong(Context context, Map map) {
        Sector sector0 = map.getSector(0, map.getHeight()-1);
        double latLongIncrement = sector0.getLongitude()*2;
        /* This is not lat long anymore. Now is distance in meters for the source point 0,0 */
        for(int y =0; y<map.getHeight(); y++) {
            for(int x = 0; x<map.getWidth(); x++){
                Sector sector = map.getSector(x, y);
                sector.setLatitude(sector0.getLatitude()+(((map.getHeight()-1)-y)*latLongIncrement));
                sector.setLongitude(sector0.getLongitude()+(x*latLongIncrement));
            }
        }
        Toast.makeText(context, R.string.completed_latlong, Toast.LENGTH_SHORT).show();
    }

    public void generateFingerprinting(final Context context, final Map map, final ProgressBar progressBar) {
        new AsyncTask<Void, Integer, Boolean>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar.setMax(map.getaSectors().length * map.getMapWifiAPs().size());
                progressBar.setProgress(0);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                progressBar.setProgress(values[0]);
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try{
                    for(int i =0; i<map.getMapWifiAPs().size(); i++){
                        WifiAP wifiAP = map.getMapWifiAPs().get(i);
                        Sector wifiAPSector = map.getSectorOfWifiAP(wifiAP.getBSSID());
                        for(int j=0; j<map.getaSectors().length; j++){
                            publishProgress((i+1)*(j+1));

                            Sector currentSector = map.getaSectors()[j];
                            Double predictedLevel = getPredictedLevel(wifiAPSector.getLongitude(),
                                    wifiAPSector.getLatitude(), currentSector.getLongitude(),
                                    currentSector.getLatitude());
                            Reading reading = new Reading(
                                    true, // Generado Matemáticamente
                                    wifiAP.getBSSID(), // BSSID del wifiAP
                                    predictedLevel.intValue(), // Valor del level generado
                                    FREQUENCY_MHZ, // TODO Sacar la frecuencia del wifiAP
                                    currentSector.getSectorId(), // Secrtor id
                                    System.currentTimeMillis()); // Timestamp (está en millis)
                            currentSector.getReadings().add(reading);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                Toast.makeText(context, result?R.string.completed_generate_figerprinting:R.string.error_generate_figerprinting, Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        }.execute();
    }

    private double getPredictedLevel(double wifiAPSectorLongitude, double wifiAPSectorLatitude,
                                     double currentSectorLongitude, double currentSectorLatitude) throws Exception {

        return cost(wifiAPSectorLatitude, wifiAPSectorLongitude, currentSectorLatitude, currentSectorLongitude);
    }

    /**
     * To_do esto de aqui abajo podría sacarse a una clase externa
     * son métodos del/los modelos
     */

    // TODO Desharcodear todos estos valores y hacerlo un poco más cientifico
    private static final int FREQUENCY_MHZ = 2400;
    private static final double REF_DISTANCE = 1.0;
    private static final double N_CORRIDOR = 1.2;

    private static final String SERVER_IP = "";
    private static final int SERVER_PORT = 1170;

    // TODO Para desharcodear este valor, preguntar al usuario que tipo de entorno es, pasillo, oficina... hacer research para sacar los valores para los diferentes entornos
    private static final double N = N_CORRIDOR;

    // COST 231 Multi Wall
    private double cost(double wifiAPSectorLongitude, double wifiAPSectorLatitude,
                        double currentSectorLongitude, double currentSectorLatitude) throws Exception {
        double logDistanceValue = logDistance(getDistance(wifiAPSectorLongitude, wifiAPSectorLatitude, currentSectorLongitude, currentSectorLatitude));
        double numFloors = 0.0; // Number of floors
        double genericFloorLossFactor = 0.0; // Floor loss factor (a generic value for all the floors)
        double b = 0.0; // No tengo muy claro que es esto. Mirar doc
        double nFExp = numFloors>0.0 ? (((genericFloorLossFactor-2)/(genericFloorLossFactor+1))-b) : 0.0; // No se si es -2 o +2
        double floorLoss = genericFloorLossFactor * Math.pow(numFloors, nFExp);
        double pathLoss = logDistanceValue + floorLoss;
        ArrayList<Double> wallLossFactors = getWallLossFactors(wifiAPSectorLongitude, wifiAPSectorLatitude, currentSectorLongitude, currentSectorLatitude);
        for (double wallLossFactor :  wallLossFactors) {
            pathLoss += wallLossFactor;
        }
        return pathLoss;
    }

    private double logDistance(double distance){
        double pl0 = pathLossAtRef();
        double x0 = 0.0;

        return pl0 + 10*N*Math.log10(distance/REF_DISTANCE)+x0;
    }

    private double pathLossAtRef(){
        return 20 * Math.log10(FREQUENCY_MHZ) - 28;
    }

    private ArrayList<Double> getWallLossFactors(double wifiAPSectorLongitude, double wifiAPSectorLatitude,
                                        double currentSectorLongitude, double currentSectorLatitude) throws Exception {

        CommunicationManagerTCP cmTCP = new CommunicationManagerTCP(SERVER_IP, SERVER_PORT);
        ArrayList<Double> result = cmTCP.getWallLossFactors(wifiAPSectorLongitude, wifiAPSectorLatitude, currentSectorLongitude, currentSectorLatitude);


        return result;
    }

    private double getDistance(double x0, double y0, double x1, double y1){
        return 5.0;
        //return Math.sqrt(Math.pow((x1-x0), 2)+Math.pow((y1-y0), 2));
    }
}
