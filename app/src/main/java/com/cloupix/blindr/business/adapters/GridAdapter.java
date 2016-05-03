package com.cloupix.blindr.business.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloupix.blindr.R;
import com.cloupix.blindr.business.Map;
import com.cloupix.blindr.business.SectorView;
import com.cloupix.blindr.business.SquareImageView;
import com.cloupix.blindr.business.WifiAPView;

/**
 * Created by alonsousa on 15/12/15.
 *
 */
public class GridAdapter extends BaseAdapter {

    private Context mContext;

    private LayoutInflater mInflater;
    private Map map;

    private boolean buildMode;


    public GridAdapter(Context c, Map map, boolean buildMode) {
        mContext = c;
        //listSectors = new ArrayList<>();
        this.map = map;
        this.mInflater = LayoutInflater.from(c);
        this.buildMode = buildMode;

    }

    public int getCount() {
        return map.getaSectors().length;
    }

    public Object getItem(int position) {
        return map.getaSectors()[position];
    }

    public long getItemId(int position) {
        return map.getaSectors()[position].getSectorId();
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
            holder.neswStroke = convertView.findViewById(R.id.neswStroke);
            holder.nwseStroke = convertView.findViewById(R.id.nwseStroke);
            holder.imgViewCircle = (ImageView) convertView.findViewById(R.id.imgViewCircle);
            holder.textViewApNumber = (TextView) convertView.findViewById(R.id.textViewApNumber);
            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        if(map.getaSectors()[position]!=null && map.getaSectors()[position] instanceof SectorView){
            SectorView sector = (SectorView) map.getaSectors()[position];

            // Dibujos
            holder.nStroke.setVisibility(sector.getStroke(SectorView.STROKE_N)?View.VISIBLE:View.INVISIBLE);
            holder.eStroke.setVisibility(sector.getStroke(SectorView.STROKE_E)?View.VISIBLE:View.INVISIBLE);
            holder.sStroke.setVisibility(sector.getStroke(SectorView.STROKE_S)?View.VISIBLE:View.INVISIBLE);
            holder.wStroke.setVisibility(sector.getStroke(SectorView.STROKE_W)?View.VISIBLE:View.INVISIBLE);
            holder.neswStroke.setVisibility(sector.getStroke(SectorView.STROKE_NE_SW)?View.VISIBLE:View.INVISIBLE);
            holder.nwseStroke.setVisibility(sector.getStroke(SectorView.STROKE_NW_SE)?View.VISIBLE:View.INVISIBLE);

            // Visualización del (los) WifiAP(s) en el sector
            if(sector.getWifiAPs().size()>0){
                if(sector.getWifiAPs().size()>1){
                    // Más de 1 AP
                    holder.imgViewCircle.setImageResource(WifiAPView.multipleAPCircleRes);
                    String text = Integer.toString(((WifiAPView) sector.getWifiAPs().get(0)).getApNumber());
                    for(int i = 1; i<sector.getWifiAPs().size(); i++){
                        text += "," + ((WifiAPView) sector.getWifiAPs().get(i)).getApNumber();
                    }
                    holder.textViewApNumber.setText(text);
                }else{
                    // 1 AP
                    holder.imgViewCircle.setImageResource(((WifiAPView)sector.getWifiAPs().get(0)).getImgCircleRes());
                    holder.textViewApNumber.setText(((WifiAPView)sector.getWifiAPs().get(0)).getApNumber());
                }
                holder.imgViewCircle.setVisibility(View.VISIBLE);
                holder.textViewApNumber.setVisibility(View.VISIBLE);
            }else {
                // 0 APs
                holder.imgViewCircle.setVisibility(View.INVISIBLE);
                holder.textViewApNumber.setVisibility(View.INVISIBLE);
            }

            // Completed or not
            if(!buildMode) {
                int color = sector.isScanned()? mContext.getColor(android.R.color.holo_green_light):mContext.getColor(android.R.color.transparent);
                holder.imgViewSector.setBackgroundColor(color);
            }
        }

        return convertView;
    }


    static class ViewHolder {
        SquareImageView imgViewSector;
        View nStroke, eStroke, sStroke, wStroke, nwseStroke, neswStroke;
        TextView textViewApNumber;
        ImageView imgViewCircle;
    }

    // Helpers

    public void setSectorComplete(int position){
        if(map.getaSectors()[position]!=null && map.getaSectors()[position] instanceof SectorView)
            ((SectorView)map.getaSectors()[position]).setScanned(true);
        notifyDataSetChanged();
    }

    public void setMap(Map map){
        this.map = map;
    }
}