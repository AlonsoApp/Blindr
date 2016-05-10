package com.cloupix.blindr.ui.fragments;

import android.app.ListFragment;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloupix.blindr.R;
import com.cloupix.blindr.business.SectorView;
import com.cloupix.blindr.business.WifiAPView;
import com.cloupix.blindr.business.adapters.SelectedWifiAPListAdapter;
import com.cloupix.blindr.business.adapters.WifiAPListAdapter;
import com.cloupix.blindr.logic.WifiLogic;

import java.util.List;

/**
 * Created by alonsoapp on 03/05/16.
 * 
 */
public class EditSectorViewWifiAPsFragment extends ListFragment implements WifiLogic.WifiLogicScannCallbacks {


    private WifiAPListAdapter listAdapter;
    private View viewFooter;
    private WifiLogic wifiLogic;

    private SelectedWifiAPListAdapter selectedWifiAPListAdapter;

    private SectorView sectorView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listAdapter = new WifiAPListAdapter(getActivity().getApplicationContext());
        setListAdapter(listAdapter);

        viewFooter = inflater.inflate(R.layout.footer_wifi_ap, null);

        View rootView = inflater.inflate(R.layout.fragment_edit_sector_wifi_aps, container, false);
        loadViewElements(rootView);
        return rootView;
    }


    private void loadViewElements(View rootView){
        ListView listViewSelected = (ListView) rootView.findViewById(R.id.listWifiAPSelected);
        selectedWifiAPListAdapter = new SelectedWifiAPListAdapter(sectorView.getWifiAPs(), getContext());
        listViewSelected.setAdapter(selectedWifiAPListAdapter);
        //updateWifiAPView();
    }

    /*
    private void updateWifiAPView(){
        layoutWifiAP1.setVisibility(View.GONE);
        layoutWifiAP2.setVisibility(View.GONE);
        layoutWifiAP3.setVisibility(View.GONE);
        if(sectorView.getWifiAPs().size()>0){
            layoutWifiAP1.setVisibility(View.VISIBLE);
            textViewSSID1.setText(sectorView.getWifiAPs().get(0).getSSID());
            textViewBSSID1.setText(sectorView.getWifiAPs().get(0).getSSID());
        }
        if(sectorView.getWifiAPs().size()>1){
            layoutWifiAP2.setVisibility(View.VISIBLE);
            textViewSSID2.setText(sectorView.getWifiAPs().get(1).getSSID());
            textViewBSSID2.setText(sectorView.getWifiAPs().get(1).getSSID());
        }
        if(sectorView.getWifiAPs().size()>2){
            layoutWifiAP3.setVisibility(View.VISIBLE);
            textViewSSID3.setText(sectorView.getWifiAPs().get(2).getSSID());
            textViewBSSID3.setText(sectorView.getWifiAPs().get(2).getSSID());
        }
    }
    */

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(wifiLogic==null)
            wifiLogic = new WifiLogic(getActivity());

        wifiLogic.startScan(this, WifiLogic.SCAN_LOOP_INIFINITE);

        // Esto va en el load
        getListView().removeFooterView(viewFooter);
        if(listAdapter.getCount()>0) {
            getListView().addFooterView(viewFooter);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if(wifiLogic==null)
            wifiLogic = new WifiLogic(getActivity());

        wifiLogic.stopScan();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        WifiAPView wifiAPView = (WifiAPView) listAdapter.getItem(position);
        sectorView.addWifiAPWithCheck(wifiAPView);
        selectedWifiAPListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onReceive(List<ScanResult> results, int loopCounter) {
        listAdapter.addScanResultList(results);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onScanFinished() {

    }

    public void setSectorView(SectorView sectorView){
        this.sectorView = sectorView;
    }

}
