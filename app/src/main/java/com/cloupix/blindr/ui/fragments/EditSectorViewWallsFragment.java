package com.cloupix.blindr.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.cloupix.blindr.R;
import com.cloupix.blindr.business.SectorView;

/**
 * Created by alonsoapp on 03/05/16.
 *
 */
public class EditSectorViewWallsFragment extends Fragment implements View.OnClickListener {

    private View nStroke, eStroke, sStroke, wStroke, neswStroke, nwseStroke;
    //private ImageView imgViewCircle;
    //private TextView textViewApNumber;
    private EditText editTextLatitude, editTextLongitude;


    private SectorView sectorView;

    public void setSectorView(SectorView sectorView){
        this.sectorView = sectorView;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        /*
        if(getActivity()!=null)
                getActivity().setTitle("Sector " + sectorView.getMatrixX() + "x"+ sectorView.getMatrixY());
                */
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_sector_walls, container, false);
        loadViewElements(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateViews();
    }

    private void loadViewElements(View rootView){
        nStroke = rootView.findViewById(R.id.nStroke);
        eStroke = rootView.findViewById(R.id.eStroke);
        sStroke = rootView.findViewById(R.id.sStroke);
        wStroke = rootView.findViewById(R.id.wStroke);
        neswStroke = rootView.findViewById(R.id.neswStroke);
        nwseStroke = rootView.findViewById(R.id.nwseStroke);
        //imgViewCircle = (ImageView) rootView.findViewById(R.id.imgViewCircle);
        //textViewApNumber = (TextView) rootView.findViewById(R.id.textViewApNumber);
        editTextLatitude = (EditText) rootView.findViewById(R.id.editTextLatitude);
        editTextLongitude = (EditText) rootView.findViewById(R.id.editTextLongitude);

        nStroke.setOnClickListener(this);
        eStroke.setOnClickListener(this);
        sStroke.setOnClickListener(this);
        wStroke.setOnClickListener(this);
        neswStroke.setOnClickListener(this);
        nwseStroke.setOnClickListener(this);

        editTextLatitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    sectorView.setLatitude(Double.parseDouble(s.toString()));
                }catch (Exception e){}
            }
        });
        editTextLongitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    sectorView.setLongitude(Double.parseDouble(s.toString()));
                }catch(Exception e){}
            }
        });

        updateViews();
    }

    private void updateViews(){
        nStroke.setBackgroundColor(getResources().getColor(sectorView.isnStroke()?R.color.wall_on:R.color.wall_off, null));
        eStroke.setBackgroundColor(getResources().getColor(sectorView.iseStroke()?R.color.wall_on:R.color.wall_off, null));
        sStroke.setBackgroundColor(getResources().getColor(sectorView.issStroke()?R.color.wall_on:R.color.wall_off, null));
        wStroke.setBackgroundColor(getResources().getColor(sectorView.iswStroke()?R.color.wall_on:R.color.wall_off, null));
        neswStroke.setBackgroundColor(getResources().getColor(sectorView.isNeswStroke()?R.color.wall_on:R.color.wall_off, null));
        nwseStroke.setBackgroundColor(getResources().getColor(sectorView.isNwseStroke()?R.color.wall_on:R.color.wall_off, null));
        editTextLatitude.setText(Double.toString(sectorView.getLatitude()));
        editTextLongitude.setText(Double.toString(sectorView.getLongitude()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nStroke:
                sectorView.setnStroke(!sectorView.isnStroke());
                break;
            case R.id.eStroke:
                sectorView.seteStroke(!sectorView.iseStroke());
                break;
            case R.id.sStroke:
                sectorView.setsStroke(!sectorView.issStroke());
                break;
            case R.id.wStroke:
                sectorView.setwStroke(!sectorView.iswStroke());
                break;
            case R.id.neswStroke:
                sectorView.setNeswStroke(!sectorView.isNeswStroke());
                break;
            case R.id.nwseStroke:
                sectorView.setNwseStroke(!sectorView.isNwseStroke());
                break;
        }
        updateViews();
    }
}
