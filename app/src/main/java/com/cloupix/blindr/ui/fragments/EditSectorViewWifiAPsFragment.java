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
import com.cloupix.blindr.business.adapters.WifiAPListAdapter;
import com.cloupix.blindr.logic.WifiLogic;

import java.util.List;

/**
 * Created by alonsoapp on 03/05/16.
 * 
 */
public class EditSectorViewWifiAPsFragment extends ListFragment implements WifiLogic.WifiLogicScannCallbacks, View.OnClickListener {


    private WifiAPListAdapter listAdapter;
    private View viewFooter;
    private WifiLogic wifiLogic;

    private RelativeLayout layoutWifiAP1, layoutWifiAP2, layoutWifiAP3;
    private TextView textViewSSID1, textViewBSSID1, textViewSSID2, textViewBSSID2, textViewSSID3, textViewBSSID3;

    private SectorView sectorView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listAdapter = new WifiAPListAdapter(getActivity().getApplicationContext());
        setListAdapter(listAdapter);

        viewFooter = inflater.inflate(R.layout.footer_wifi_ap, null);

        View rootView = inflater.inflate(R.layout.fragment_wifi_ap_list, container, false);
        loadViewElements(rootView);
        return rootView;
    }


    private void loadViewElements(View rootView){
        layoutWifiAP1 = (RelativeLayout) rootView.findViewById(R.id.layoutWifiAP1);
        textViewSSID1 = (TextView) rootView.findViewById(R.id.textViewSSID1);
        textViewBSSID1 = (TextView) rootView.findViewById(R.id.textViewBSSID1);
        Button btnRemoveWifiAP1 = (Button) rootView.findViewById(R.id.btnRemoveWifiAP1);

        // TODO Repensar la UI para que esto sea dinámico, solo se visualizan un máximo de tres APs

        layoutWifiAP2 = (RelativeLayout) rootView.findViewById(R.id.layoutWifiAP2);
        textViewSSID2 = (TextView) rootView.findViewById(R.id.textViewSSID2);
        textViewBSSID2 = (TextView) rootView.findViewById(R.id.textViewBSSID2);
        Button btnRemoveWifiAP2 = (Button) rootView.findViewById(R.id.btnRemoveWifiAP2);

        layoutWifiAP3 = (RelativeLayout) rootView.findViewById(R.id.layoutWifiAP3);
        textViewSSID3 = (TextView) rootView.findViewById(R.id.textViewSSID3);
        textViewBSSID3 = (TextView) rootView.findViewById(R.id.textViewBSSID3);
        Button btnRemoveWifiAP3 = (Button) rootView.findViewById(R.id.btnRemoveWifiAP3);

        btnRemoveWifiAP1.setOnClickListener(this);
        btnRemoveWifiAP2.setOnClickListener(this);
        btnRemoveWifiAP3.setOnClickListener(this);

        updateWifiAPView();
    }

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
        updateWifiAPView();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRemoveWifiAP1:
                sectorView.getWifiAPs().remove(0);
                break;
            case R.id.btnRemoveWifiAP2:
                sectorView.getWifiAPs().remove(1);
                break;
            case R.id.btnRemoveWifiAP3:
                sectorView.getWifiAPs().remove(2);
                break;
        }
        updateWifiAPView();
    }

    public void notifySectorViewChanged() {
        updateWifiAPView();
    }
}
