package com.cloupix.blindr.business.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cloupix.blindr.R;
import com.cloupix.blindr.business.Map;

import java.util.ArrayList;

/**
 * Created by alonsoapp on 05/05/16.
 *
 */
public class MapSpinnerAdapter extends ArrayAdapter<String> {

    private ArrayList<Map> maps;
    String element;
    LayoutInflater inflater;


    public MapSpinnerAdapter(Context context, int textViewResourceId, ArrayList objects){
        super(context, textViewResourceId, objects);

        this.maps = objects;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View row = inflater.inflate(R.layout.row_spinner_map, parent, false);

        TextView textViewName = (TextView)row.findViewById(R.id.textViewName);
        TextView textViewResolution = (TextView)row.findViewById(R.id.textViewResolution);

        Map map = maps.get(position);

        textViewName.setText(map.getName());
        textViewResolution.setText(map.getHeight() + "x" + map.getWidth());

        return row;
    }

    public void setMapList(ArrayList<Map> mapArrayList) {
        this.maps = mapArrayList;
    }
}
