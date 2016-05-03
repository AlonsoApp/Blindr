package com.cloupix.blindr.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.cloupix.blindr.R;
import com.cloupix.blindr.business.SectorView;
import com.cloupix.blindr.business.adapters.EditSectorPagerAdapter;
import com.cloupix.blindr.logic.MapLogic;

/**
 * Created by alonsoapp on 03/05/16.
 *
 */
public class EditSectorDialog extends DialogFragment {

    private ViewPager viewPager;

    private EditSectorDialogCallbacks mListener;
    private SectorView sectorView;
    private EditSectorPagerAdapter editSectorPagerAdapter;

    public void setSectorView(SectorView sectorView){
        //Hacemos una copia del sector para que los cambios que hagamos aqui no modifiquen el objeto
        MapLogic mapLogic = new MapLogic();
        this.sectorView = mapLogic.getSectorViewBySectorId(sectorView.getSectorId(), getContext());
    }

    public void setEditSectorDialogCallbacks(EditSectorDialogCallbacks mCallbacks){
        this.mListener = mCallbacks;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        View rootView = inflater.inflate(R.layout.dialog_edit_sector, null);
        loadViewElements(rootView);

        editSectorPagerAdapter = new EditSectorPagerAdapter(getFragmentManager());
        viewPager.setAdapter(editSectorPagerAdapter);

        updateView();


        builder.setView(rootView);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onSectorViewSave(sectorView);
                dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        return builder.create();
    }

    private void loadViewElements(View rootView){

        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
    }

    private void updateView(){
        if(sectorView==null)
            return;
        editSectorPagerAdapter.setSectorView(sectorView);
        editSectorPagerAdapter.notifySectorViewChanged();
    }

    public void notifySectorViewChanged() {
        updateView();
    }


    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the BoletusLoginDialogListener in case the host needs to query it. */
    public interface EditSectorDialogCallbacks {

        void onSectorViewSave(SectorView sectorView);

    }
}
