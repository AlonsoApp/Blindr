package com.cloupix.blindr.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private SectorView sectorView;

    public void setSectorView(SectorView sectorView){
        this.sectorView = sectorView;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_edit_sector_walls, container, false);
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

        nStroke.setOnClickListener(this);
        eStroke.setOnClickListener(this);
        sStroke.setOnClickListener(this);
        wStroke.setOnClickListener(this);
        neswStroke.setOnClickListener(this);
        nwseStroke.setOnClickListener(this);

        updateViews();
    }

    public void notifySectorViewChanged(){
        updateViews();
    }

    private void updateViews(){
        nStroke.setBackgroundColor(getResources().getColor(sectorView.isnStroke()?R.color.wall_off:R.color.wall_on, null));
        eStroke.setBackgroundColor(getResources().getColor(sectorView.iseStroke()?R.color.wall_off:R.color.wall_on, null));
        sStroke.setBackgroundColor(getResources().getColor(sectorView.issStroke()?R.color.wall_off:R.color.wall_on, null));
        wStroke.setBackgroundColor(getResources().getColor(sectorView.iswStroke()?R.color.wall_off:R.color.wall_on, null));
        neswStroke.setBackgroundColor(getResources().getColor(sectorView.isneswStroke()?R.color.wall_off:R.color.wall_on, null));
        nwseStroke.setBackgroundColor(getResources().getColor(sectorView.isnwseStroke()?R.color.wall_off:R.color.wall_on, null));

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
                sectorView.setneswStroke(!sectorView.isneswStroke());
                break;
            case R.id.nwseStroke:
                sectorView.setnwseStroke(!sectorView.isnwseStroke());
                break;
        }
        updateViews();
    }
}
