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


    // Este es el mapa, establece en que sectores hay que pintar el stroke N, E, S y W
    // aqui digamos que pintas las paredes

    private int[][] strokes;

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

        strokes = new int[][]{
                mContext.getResources().getIntArray(R.array.sector_n), /* N */
                mContext.getResources().getIntArray(R.array.sector_e), /* E */
                mContext.getResources().getIntArray(R.array.sector_s), /* S */
                mContext.getResources().getIntArray(R.array.sector_w)  /* W */
        };

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

        MapSector mapSector = listMapSectors.get(position);

        // Dibujos
        holder.nStroke.setVisibility(mapSector.getStroke()[0]?View.VISIBLE:View.INVISIBLE);
        holder.eStroke.setVisibility(mapSector.getStroke()[1]?View.VISIBLE:View.INVISIBLE);
        holder.sStroke.setVisibility(mapSector.getStroke()[2]?View.VISIBLE:View.INVISIBLE);
        holder.wStroke.setVisibility(mapSector.getStroke()[3]?View.VISIBLE:View.INVISIBLE);

        // Completed or not
        int color = mapSector.isCompleted()? mContext.getColor(android.R.color.holo_green_light):mContext.getColor(android.R.color.transparent);
        holder.imgViewSector.setBackgroundColor(color);


        return convertView;
    }



    static class ViewHolder {
        SquareImageView imgViewSector;
        View nStroke, eStroke, sStroke, wStroke;
    }

    // Helpers

    public void setSectorComplete(int position){
        listMapSectors.get(position).setCompleted(true);
        notifyDataSetChanged();
    }
}