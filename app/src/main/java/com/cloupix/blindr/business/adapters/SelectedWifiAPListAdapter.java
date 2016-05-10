package com.cloupix.blindr.business.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cloupix.blindr.R;
import com.cloupix.blindr.business.WifiAP;

import java.util.ArrayList;

/**
 * Created by alonsoapp on 04/05/16.
 *
 */
public class SelectedWifiAPListAdapter extends BaseAdapter {

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

        WifiAP wifiAp = listWifiAPs.get(position);

        holder.textViewSSID.setText(wifiAp.getSSID());
        holder.textViewBSSID.setText(wifiAp.getBSSID());
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listWifiAPs.remove(position);
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
