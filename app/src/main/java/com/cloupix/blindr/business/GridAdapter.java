package com.cloupix.blindr.business;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cloupix.blindr.R;

import java.util.ArrayList;

/**
 * Created by alonsousa on 15/12/15.
 *
 */
public class GridAdapter extends BaseAdapter {

    private Context mContext;

    private LayoutInflater mInflater;
    private ArrayList<MapSector> listMapSectors;


    private static final int[][] strokes = new int[][]{
            { 0, 1, 2, 3, 5, 6, 7, 8, 9, 35, 36, 37, 38, 39, 40, 41, 42, 43 }, /* N */
            { 4, 9, 14, 19, 29, 39, 44, 49, 54, 59, 64, 69, 74, 79, 84, 89 }, /* E */
            { 80, 81, 82, 83, 84, 85, 86, 87, 88, 89 }, /* S */
            { 0, 10, 20, 30, 40, 50, 60, 70, 80 }  /* W */
    };

    /*
    private static final int[] nStroke = new int[]{1};
    private static final int[] eStroke = new int[]{1};
    private static final int[] sStroke = new int[]{1};
    private static final int[] wStroke = new int[]{1};
    */

    public GridAdapter(Context c) {
        mContext = c;
        listMapSectors = new ArrayList<>();
        this.mInflater = LayoutInflater.from(c);

        int sectorWidth = c.getResources().getInteger(R.integer.sector_width);
        int sectorHeight = c.getResources().getInteger(R.integer.sector_height);
        for (int i = 0; i< (sectorWidth * sectorHeight); i++)
            listMapSectors.add(new MapSector());

        // Cargamos la fonfiguración de paredes a pintar en el grind
        // Para cada emisferio (N, E, S, W) y para cada elemento de ese emisferio (que contiene la posición que debe tener pintado ese emisferio)
        // sacar de la lista en la posicion obtenida su array de emisferios y en la posición del emisferio en el que nos encontramos marcarlo a true
        for (int i = 0; i < strokes.length; i++) {
            for (int j = 0; j < strokes[i].length; j++) {
                listMapSectors.get(strokes[i][j]).getStroke()[i] = true;
            }
        }

    }

    public int getCount() {
        return listMapSectors.size();
    }

    public Object getItem(int position) {
        return listMapSectors.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.row_grid_fingerprint, null);
            holder = new ViewHolder();
            holder.imgViewSector = (SquareImageView) convertView.findViewById(R.id.imgViewSector);
            holder.nStroke = convertView.findViewById(R.id.nStroke);
            holder.eStroke = convertView.findViewById(R.id.eStroke);
            holder.sStroke = convertView.findViewById(R.id.sStroke);
            holder.wStroke = convertView.findViewById(R.id.wStroke);
            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        MapSector mapSctor = listMapSectors.get(position);

        // Dibujos
        holder.nStroke.setVisibility(mapSctor.getStroke()[0]?View.VISIBLE:View.INVISIBLE);
        holder.eStroke.setVisibility(mapSctor.getStroke()[1]?View.VISIBLE:View.INVISIBLE);
        holder.sStroke.setVisibility(mapSctor.getStroke()[2]?View.VISIBLE:View.INVISIBLE);
        holder.wStroke.setVisibility(mapSctor.getStroke()[3]?View.VISIBLE:View.INVISIBLE);


        return convertView;
    }



    static class ViewHolder {
        SquareImageView imgViewSector;
        View nStroke, eStroke, sStroke, wStroke;
    }
}