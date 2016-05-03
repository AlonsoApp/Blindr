package com.cloupix.blindr.logic;

import android.content.Context;

import com.cloupix.blindr.business.Map;
import com.cloupix.blindr.business.Sector;
import com.cloupix.blindr.business.SectorView;
import com.cloupix.blindr.business.WifiAP;
import com.cloupix.blindr.business.WifiAPView;
import com.cloupix.blindr.dao.Dao;

import java.util.ArrayList;

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
                // TODO Calcular lat long asociadas a ese sector
                long sectorId = dao.insertSector(new Sector(i, matrixXY[0], matrixXY[1], 0, 0, mapId));
                dao.insertSectorView(new SectorView(sectorId, SectorView.STROKE_EMPTY_SECTOR, false));
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
                sectorView.setLectures(dao.getLecturesBySectorId(sectorView.getSectorId()));
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

    private ArrayList<WifiAP> getWifiAPViewsBySectorId(long sectorId, Dao dao){
        ArrayList<WifiAP> wifiAPs = new ArrayList<>();
        ArrayList<Dao.WifiAPSector> wifiAPSectors = dao.getWifiAPSectorBySectorId(sectorId);
        for(Dao.WifiAPSector wifiAPSector : wifiAPSectors){
            WifiAP wifiAP = dao.getWifiAPByBSSID(wifiAPSector.getBSSID());
            WifiAPView wifiAPView = dao.getWifiAPViewByBSSID(wifiAP.getBSSID());
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
            sectorView.setLectures(dao.getLecturesBySectorId(sectorView.getSectorId()));
            sectorView.setWifiAPs(getWifiAPViewsBySectorId(sectorView.getSectorId(), dao));


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(dao.isConnectionOpen())
                dao.close();
        }
        return sectorView;
    }
}
