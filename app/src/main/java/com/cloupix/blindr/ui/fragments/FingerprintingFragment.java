package com.cloupix.blindr.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.cloupix.blindr.R;
import com.cloupix.blindr.business.GridAdapter;

/**
 * Created by alonsousa on 15/12/15.
 *
 */
public class FingerprintingFragment  extends Fragment implements AdapterView.OnItemClickListener{

    public static final String TAG = "fragment_photo";

    private GridAdapter photoAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fingerprint, container, false);

        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        photoAdapter = new GridAdapter(getActivity());
        gridview.setAdapter(photoAdapter);

        gridview.setOnItemClickListener(this);

        return rootView;
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

        // Método que se ejecuta cuando pulsas un sector (un cuadradito).
        // Aqui hay que guardad (o hacer y guardar) la lectura de los wifis que vea en ese momento con todos sus datos
        // Recomiendo. Poner una barra de progreso mientras (durante un tiempo determinado) se recopilan muestras de los diferentes APs y se marque la casilla con un tick

        Snackbar.make(getView(), position + "", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }


}