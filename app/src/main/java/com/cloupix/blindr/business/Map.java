package com.cloupix.blindr.business;

/**
 * Created by alonsoapp on 23/04/16.
 *
 */
public class Map {

    private long mapId;
    private String name;
    private int height;
    private int width;

    private Sector[] aSectors;

    public Map() {
        this.mapId = -1;
        this.name = "";
        this.height = 0;
        this.width = 0;
        this.aSectors = new Sector[0];
    }

    public Map(String name, int height, int width) {
        this.name = name;
        this.height = height;
        this.width = width;
        this.aSectors = new Sector[height*width];
    }

    public Map(long mapId, String name, int height, int width) {
        this.mapId = mapId;
        this.name = name;
        this.height = height;
        this.width = width;
        this.aSectors = new Sector[height*width];
    }

    public long getMapId() {
        return mapId;
    }

    public void setMapId(long mapId) {
        this.mapId = mapId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        if(height!=-1 && width!=-1)
            this.aSectors = new Sector[height*width];
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        if(height!=-1 && width!=-1)
            this.aSectors = new Sector[height*width];
    }

    public Sector[] getaSectors() {
        return aSectors;
    }

    public void setaSectors(Sector[] aSectors) {
        this.aSectors = aSectors;
    }

    public Sector getSector(int index){
        return aSectors[index];
    }

    public void setSector(Sector sector, int index){
        aSectors[index] = sector;
    }

    public void addSector(SectorView sectorView, int listN) {
        this.aSectors[listN] = sectorView;
    }
}
