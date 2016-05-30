package com.cloupix.blindr.business.adapters;

import android.content.Context;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cloupix.blindr.R;
import com.cloupix.blindr.business.WifiAP;
import com.cloupix.blindr.business.WifiAPView;

import java.util.ArrayList;

/**
 * Created by alonsoapp on 04/05/16.
 *
 */
public class SelectedWifiAPListAdapter extends BaseAdapter {

    private static final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();

    private ArrayList<WifiAP> listWifiAPs;
    private LayoutInflater mInflater;
    private Context context;

    public SelectedWifiAPListAdapter(ArrayList<WifiAP> list, Context context) {
        this.context = context;
        this.listWifiAPs = list;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listWifiAPs.size();
    }

    @Override
    public Object getItem(int position) {
        return listWifiAPs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_selected_wifi_ap, null);
            holder = new ViewHolder();
            holder.textViewSSID = (TextView) convertView.findViewById(R.id.textViewSSID);
            holder.textViewBSSID = (TextView) convertView.findViewById(R.id.textViewBSSID);
            holder.btnRemove = (Button) convertView.findViewById(R.id.btnRemove);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final WifiAP wifiAp = listWifiAPs.get(position);

        // Tachamos el nombre dle wifiAP si esta borrado y quitamos el botón
        if(wifiAp instanceof WifiAPView){
            if(((WifiAPView)wifiAp).isDeleteDBEntity()){
                holder.textViewSSID.setText(wifiAp.getSSID(), TextView.BufferType.SPANNABLE);
                Spannable spannable = (Spannable) holder.textViewSSID.getText();
                spannable.setSpan(STRIKE_THROUGH_SPAN, 0, wifiAp.getSSID().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.btnRemove.setVisibility(View.INVISIBLE);
            }else
                holder.textViewSSID.setText(wifiAp.getSSID());
        }else{
            holder.textViewSSID.setText(wifiAp.getSSID());
        }
        holder.textViewBSSID.setText(wifiAp.getBSSID());
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //listWifiAPs.remove(position);
                if(wifiAp instanceof WifiAPView)
                    ((WifiAPView)listWifiAPs.get(position)).setDeleteDBEntity(true);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView textViewSSID, textViewBSSID;
        Button btnRemove;
    }

}
