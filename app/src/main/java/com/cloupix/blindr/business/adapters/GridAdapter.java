package com.cloupix.blindr.business.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

    public static final int LOCATION_MODE = 0;
    public static final int BUILD_MODE = 1;
    public static final int MAPPING_MODE = 2;

    private Context mContext;

    private LayoutInflater mInflater;
    private Map map;

    private int viewMode;


    public GridAdapter(Context c, Map map, int viewMode) {
        mContext = c;
        //listSectors = new ArrayList<>();
        this.map = map;
        this.mInflater = LayoutInflater.from(c);
        this.viewMode = viewMode;

    }

    public int getCount() {
        return map==null?0:map.getaSectors().length;
    }

    public Object getItem(int position) {
        return map==null?0:map.getaSectors()[position];
    }

    public long getItemId(int position) {
        return map==null?-1:map.getaSectors()[position].getSectorId();
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
            holder.textViewProbability = (TextView) convertView.findViewById(R.id.textViewProbability);
            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        if(map==null)
            return convertView;

        if(map.getaSectors()[position]!=null && map.getaSectors()[position] instanceof SectorView){
            SectorView sector = (SectorView) map.getaSectors()[position];

            // Dibujos

            // Si esta en build mode poneomos to do a visible y pintamos los no rellenados con off
            if(viewMode == BUILD_MODE){
                holder.nStroke.setVisibility(View.VISIBLE);
                holder.eStroke.setVisibility(View.VISIBLE);
                holder.sStroke.setVisibility(View.VISIBLE);
                holder.wStroke.setVisibility(View.VISIBLE);
                holder.neswStroke.setVisibility(View.VISIBLE);
                holder.nwseStroke.setVisibility(View.VISIBLE);
                holder.nStroke.setBackgroundColor(sector.isnStroke()?mContext.getColor(R.color.wall_on):mContext.getColor(R.color.wall_off));
                holder.eStroke.setBackgroundColor(sector.iseStroke()?mContext.getColor(R.color.wall_on):mContext.getColor(R.color.wall_off));
                holder.sStroke.setBackgroundColor(sector.issStroke()?mContext.getColor(R.color.wall_on):mContext.getColor(R.color.wall_off));
                holder.wStroke.setBackgroundColor(sector.iswStroke()?mContext.getColor(R.color.wall_on):mContext.getColor(R.color.wall_off));
                holder.neswStroke.setBackgroundColor(sector.isNeswStroke()?mContext.getColor(R.color.wall_on):mContext.getColor(R.color.wall_off));
                holder.nwseStroke.setBackgroundColor(sector.isNwseStroke()?mContext.getColor(R.color.wall_on):mContext.getColor(R.color.wall_off));
            }else{
                // Si no hacemos el juego de visible invisible
                holder.nStroke.setVisibility(sector.isnStroke()?View.VISIBLE:View.INVISIBLE);
                holder.eStroke.setVisibility(sector.iseStroke()?View.VISIBLE:View.INVISIBLE);
                holder.sStroke.setVisibility(sector.issStroke()?View.VISIBLE:View.INVISIBLE);
                holder.wStroke.setVisibility(sector.iswStroke()?View.VISIBLE:View.INVISIBLE);
                holder.neswStroke.setVisibility(sector.isNeswStroke()?View.VISIBLE:View.INVISIBLE);
                holder.nwseStroke.setVisibility(sector.isNwseStroke()?View.VISIBLE:View.INVISIBLE);
            }

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
                    holder.imgViewCircle.setImageResource(((WifiAPView)sector.getWifiAPs().get(0)).getBackgroundCircleRes());
                    holder.textViewApNumber.setText(Integer.toString(((WifiAPView)sector.getWifiAPs().get(0)).getApNumber()));
                }
                holder.imgViewCircle.setVisibility(View.VISIBLE);
                holder.textViewApNumber.setVisibility(View.VISIBLE);
            }else {
                // 0 APs
                holder.imgViewCircle.setVisibility(View.INVISIBLE);
                holder.textViewApNumber.setVisibility(View.INVISIBLE);
            }

            // Sector background color
            if(viewMode == MAPPING_MODE) {

            }else if(viewMode == LOCATION_MODE){

            }

            switch (viewMode){
                case MAPPING_MODE:
                    int color = sector.hasLectures()? mContext.getColor(R.color.scanned):mContext.getColor(android.R.color.transparent);
                    holder.imgViewSector.setBackgroundColor(color);
                    break;
                case LOCATION_MODE:
                    int color2 = mContext.getColor(sector.getLocationProbabilityColorRes());
                    holder.imgViewSector.setBackgroundColor(color2);
                    holder.textViewProbability.setText(Double.toString(sector.getLocationProbability()));
                    break;
            }
        }

        return convertView;
    }

    public void setViewMode(int viewMode) {
        this.viewMode = viewMode;
    }


    static class ViewHolder {
        SquareImageView imgViewSector;
        View nStroke, eStroke, sStroke, wStroke, nwseStroke, neswStroke;
        TextView textViewApNumber, textViewProbability;
        ImageView imgViewCircle;
    }

    // Helpers

    /*
    public void setSectorComplete(int position){
        if(map.getaSectors()[position]!=null && map.getaSectors()[position] instanceof SectorView)
            ((SectorView)map.getaSectors()[position]).setScanned(true);
        notifyDataSetChanged();
    }
    */

    public void setMap(Map map){
        this.map = map;
    }
}