package com.cloupix.blindr.ui.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.cloupix.blindr.R;
import com.cloupix.blindr.business.Map;
import com.cloupix.blindr.business.Sector;
import com.cloupix.blindr.business.adapters.GridAdapter;
import com.cloupix.blindr.business.Reading;
import com.cloupix.blindr.logic.LocationLogic;
import com.cloupix.blindr.logic.MapLogic;
import com.cloupix.blindr.logic.WifiLogic;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by alonsousa on 15/12/15.
 *
 */
public class FingerprintingFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, WifiLogic.WifiLogicScannCallbacks, View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final String ARG_MAP_ID = "map_id";
    private final int READINGS_PER_LOC = 2; // Numero de lecturas antes de predecir la loc

    private GridAdapter gridAdapter;
    private GridView gridView;
    private ProgressBar scanProgressBar;
    private Spinner spinner;

    private int lecturesPerScan = 8;
    private int currentMode;
    private int currentLocationReadingType = Sector.ALL_READINGS;

    private WifiLogic wifiLogic;
    private MapLogic mapLogic;
    //private ArrayList<WifiAP>[] wifiApMatrix;
    private int scanningSectorN = 0;
    private long mapId;
    private Map map;
    private ArrayList<Map> mapEmptyList;
    private ArrayAdapter<String> mapSpinnerAdapter;
    private FingerprintingFragmentCallbacks mCallbacks;

    private ArrayList<Reading> readingGroup = new ArrayList<>();
    private int contReadings = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentMode = GridAdapter.MAPPING_MODE;

        mapEmptyList = new ArrayList<>();
        //int matrixSize = getActivity().getResources().getInteger(R.integer.sector_height) * getActivity().getResources().getInteger(R.integer.sector_width);
        //wifiApMatrix = new ArrayList[matrixSize];
        this.mapLogic = new MapLogic();

        if(getArguments()!=null) {
            mapId = getArguments().getLong(ARG_MAP_ID, -1);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.title_activity_fingerprinting);
        MapLogic mapLogic = new MapLogic();
        mapEmptyList = mapLogic.getAllMapsEmpty(getContext());
        if(mapEmptyList.size()<=0) {
            mCallbacks.onNewMap();
            return;
        }


        mapSpinnerAdapter.clear();
        //for(String str : mapArrayListToStringArray(mapArrayList))
        //    mapSpinnerAdapter.add(str);
        mapSpinnerAdapter.addAll(mapArrayListToStringArrayList(mapEmptyList));

        //mapSpinnerAdapter.setMapList(mapArrayList);
        //spinner.setAdapter(new MapSpinnerAdapter(getContext(), R.layout.row_spinner_map, mapArrayList));
        //spinner.refreshDrawableState();

        if(!mapLogic.existMap(mapId, getContext()))
            mapId = mapEmptyList.get(0).getMapId();

        map = mapLogic.getMapById(mapId, getContext());
        gridView.setNumColumns(map.getWidth());
        gridAdapter.setMap(map);
        gridAdapter.notifyDataSetChanged();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (wifiLogic !=null)
            wifiLogic.stopScan();
        if(map!=null) {
            mapLogic.saveMap(map, getContext());
            /* Quitamos el thread por problemas de sincronización
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    mapLogic.saveMap(map, getContext());
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    Context context = getContext();
                    if (context != null)
                        Toast.makeText(context, R.string.map_saved, Toast.LENGTH_SHORT).show();
                }
            }.execute();
            */
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallbacks = (FingerprintingFragmentCallbacks) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+ " must implement FingerprintingFragmentCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fingerprint, container, false);

        spinner = (Spinner) rootView.findViewById(R.id.spinnerMaps);
        //mapSpinnerAdapter = new MapSpinnerAdapter(getContext(), R.layout.row_spinner_map,new ArrayList<Map>());
        mapSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, mapArrayListToStringArrayList(mapEmptyList));


        // Specify the layout to use when the list of choices appears
        mapSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(mapSpinnerAdapter);

        spinner.setOnItemSelectedListener(this);

        gridView = (GridView) rootView.findViewById(R.id.gridview);
        gridAdapter = new GridAdapter(getContext(), new Map(), GridAdapter.MAPPING_MODE);
        gridView.setAdapter(gridAdapter);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        gridView.setOnItemClickListener(this);
        gridView.setOnItemLongClickListener(this);

        scanProgressBar = (ProgressBar) rootView.findViewById(R.id.scanProgressBar);
        scanProgressBar.setMax(lecturesPerScan);


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_fingerprinting, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                mCallbacks.onEditMap(map.getMapId());
                return true;
            case R.id.action_location_mode:
                if(wifiLogic == null)
                    wifiLogic = new WifiLogic(getActivity());
                switch (currentMode){
                    case GridAdapter.COMPARE_MODE:
                    case GridAdapter.MAPPING_MODE:
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle(R.string.alert_select_location_mode);
                        builder.setMessage(R.string.alert_select_location_mode_msg);
                        builder.setNegativeButton(R.string.math_generated, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                currentLocationReadingType = Sector.MATH_GENERATED_READINGS;
                                startLocationMode(item);
                            }
                        });
                        builder.setNeutralButton(R.string.mapped, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                currentLocationReadingType = Sector.MAPPED_READINGS;
                                startLocationMode(item);
                            }
                        });
                        builder.setPositiveButton(R.string.all_readings, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                currentLocationReadingType = Sector.ALL_READINGS;
                                startLocationMode(item);

                            }
                        });
                        builder.show();
                        break;
                    case GridAdapter.LOCATION_MODE:
                        gridView.setClickable(true);

                        wifiLogic.stopScan();
                        gridAdapter.setViewMode(GridAdapter.MAPPING_MODE);
                        gridAdapter.notifyDataSetChanged();

                        currentMode = GridAdapter.MAPPING_MODE;
                        item.setIcon(R.mipmap.ic_navigation_white_48dp);
                        break;
                }

                return true;
            case R.id.action_compare_mode:
                switch (currentMode){
                    case GridAdapter.COMPARE_MODE:
                        gridAdapter.setViewMode(GridAdapter.MAPPING_MODE);
                        gridAdapter.notifyDataSetChanged();
                        currentMode = GridAdapter.MAPPING_MODE;
                        break;
                    default:
                        MapLogic mapLogic = new MapLogic();
                        mapLogic.compareMathGeneratedReadings(map);
                        gridAdapter.setViewMode(GridAdapter.COMPARE_MODE);
                        gridAdapter.notifyDataSetChanged();
                        currentMode = GridAdapter.COMPARE_MODE;
                }
                break;
            default:
                break;
        }

        return false;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

        // Método que se ejecuta cuando pulsas un sector (un cuadradito).
        // Aqui hay que guardad (o hacer y guardar) la lectura de los wifis que vea en ese momento con todos sus datos
        // Recomiendo. Poner una barra de progreso mientras (durante un tiempo determinado) se recopilan muestras de los diferentes APs y se marque la casilla con un tick

        Snackbar.make(getView(), getString(R.string.scanning_sector) + position, Snackbar.LENGTH_LONG).setAction("Action", null).show();


        // TODO Comprobar si el anterior ha terminado, si no ha termiando, paramos el scanner antes

        scanningSectorN = position;

        if(wifiLogic == null)
            wifiLogic = new WifiLogic(getActivity());

        // Limpiamos las lectures anteriores del sector UPDATED: Ya no, ahora las concatenamos
        //map.getaSectors()[position].getReadings().clear();
        scanProgressBar.setVisibility(View.VISIBLE);
        wifiLogic.startScan(this, lecturesPerScan);


    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.alert_clear_sector_title);
        builder.setMessage(R.string.alert_clear_sector_msg);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(Reading reading : map.getSector(position).getReadings(Sector.ALL_READINGS))
                    reading.setDeleteDBEntity(true);
                //map.getSector(position).getReadings().clear();
                gridAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.show();
        return true;
    }

    @Override
    public void onReceive(List<ScanResult> results, int loopCounter) {
        switch (currentMode){
            case GridAdapter.MAPPING_MODE:
                scanProgressBar.setProgress(loopCounter+1);
                addScanResultList(results);
                break;
            case GridAdapter.LOCATION_MODE:
                updateSectorLocationProbabilities(results);
                break;
        }
    }

    @Override
    public void onScanFinished() {
        // Pintamos de verde y quitamos el progres bar y guardamos
        scanProgressBar.setVisibility(View.INVISIBLE);
        scanProgressBar.setProgress(0);
        gridAdapter.notifyDataSetChanged();
    }

    private void startLocationMode(MenuItem item){

        gridView.setClickable(false);

        wifiLogic.startScan(this, WifiLogic.SCAN_LOOP_INIFINITE);
        gridAdapter.setViewMode(GridAdapter.LOCATION_MODE);

        currentMode = GridAdapter.LOCATION_MODE;
        item.setIcon(R.mipmap.ic_pause_white_48dp);
    }


    // Este metodo coge una lista de APs del ultimo escaneo y mete las lecturas nuevas en los
    // WifiAPs de la lista que ya estaban creados en este punto y crea nuevos WifiAPs si la lectura
    // corresponde a un AP que no estaba previamente
    public void addScanResultList(List<ScanResult> scanResultList) {

        for(ScanResult scanResult : scanResultList){
            map.getaSectors()[scanningSectorN].getReadings().add(new Reading(scanResult, map.getaSectors()[scanningSectorN].getSectorId()));
        }

    }

    public void updateSectorLocationProbabilities(List<ScanResult> results){
        for(ScanResult scanResult : results)
            readingGroup.add(new Reading(scanResult));

        if(contReadings >= READINGS_PER_LOC){
            LocationLogic locationLogic = new LocationLogic();
            locationLogic.getSectorProbabilities(map, readingGroup, currentLocationReadingType);
            readingGroup.clear();
            contReadings = 0;
            gridAdapter.notifyDataSetChanged();
        }

        contReadings = contReadings + 1;

    }

    @Override
    public void onDestroy() {
        //mapLogic.saveMap(map, getContext());
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if(mCallbacks!=null)
                    mCallbacks.onNewMap();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(mapEmptyList.size()>0){
            mapId = mapEmptyList.get(position).getMapId();
            onResume();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private ArrayList<String> mapArrayListToStringArrayList(ArrayList<Map> mapList){
        ArrayList<String> array = new ArrayList<>();
        for(int i =0; i<mapList.size(); i++)
            array.add(i, mapList.get(i).getName());
        return array;
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public interface FingerprintingFragmentCallbacks {

        void onEditMap(long mapId);
        void onNewMap();
    }
}