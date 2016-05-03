package com.cloupix.blindr.ui.fragments;

import android.app.ListFragment;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cloupix.blindr.R;
import com.cloupix.blindr.business.adapters.WifiAPListAdapter;
import com.cloupix.blindr.logic.WifiLogic;

import java.util.List;

/**
 * Created by alonsousa on 6/12/15.
 *
 */
public class WifiListFragment extends ListFragment implements WifiLogic.WifiLogicScannCallbacks {

    private WifiAPListAdapter listAdapter;
    private View viewFooter;
    private WifiManager wifi;
    private WifiLogic wifiLogic;

    private WifiListFragmentCallbacks mCallbacks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listAdapter = new WifiAPListAdapter(getActivity().getApplicationContext());
        setListAdapter(listAdapter);

        viewFooter = inflater.inflate(R.layout.footer_wifi_ap, null);

        return inflater.inflate(R.layout.fragment_wifi_ap_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        /*
        listAdapter.addWifiAP(new WifiAP("Drama", "MC2", new ArrayList<Lecture>() {{
            add(new Lecture(-20));
        }}));
        listAdapter.addWifiAP(new WifiAP("Zarrator", "MC3", new ArrayList<Lecture>(){{ add(new Lecture(-30));}}));
        listAdapter.addWifiAP(new WifiAP("Alatzas", "MC4", new ArrayList<Lecture>(){{ add(new Lecture(-40));}}));
        listAdapter.addWifiAP(new WifiAP("Ludee", "MC5", new ArrayList<Lecture>(){{ add(new Lecture(-50));}}));
        listAdapter.notifyDataSetChanged();
        */

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
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallbacks = (WifiListFragmentCallbacks) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+ " must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /*
        switch(item.getItemId()) {
            case R.id.action_upload:
                sendPreset();
                return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        // Sacamos el BSSI del AP d ela posición position
        String bssi = listAdapter.getBSSI(position);

        mCallbacks.onWifiAPClick(bssi);
    }

    @Override
    public void onReceive(List<ScanResult> results, int loopCounter) {
        listAdapter.addScanResultList(results);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onScanFinished() {

    }


    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public interface WifiListFragmentCallbacks {

        void onWifiAPClick(String bssi);
    }
}

