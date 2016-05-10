package com.cloupix.blindr.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.cloupix.blindr.R;
import com.cloupix.blindr.logic.MapLogic;

/**
 * Created by alonsoapp on 28/04/16.
 *
 */
public class NewMapDialog extends DialogFragment {

    private static final String TAG = "new_map_dialog";

    private EditText editTextName, editTextHeight, editTextWidth;

    private NewMapDialogCallbacks mListener;


    public void setNewMapDialogCallbacks(NewMapDialogCallbacks mCallbacks){
        this.mListener = mCallbacks;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        View rootView = inflater.inflate(R.layout.dialog_new_map, null);
        loadViewElements(rootView);



        builder.setView(rootView);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editTextName.getText().toString();
                name = TextUtils.isEmpty(name)?getString(R.string.untitled_map):name;
                String strHeight = editTextHeight.getText().toString();
                String strWidth = editTextWidth.getText().toString();
                if(TextUtils.isEmpty(strHeight) || TextUtils.isEmpty(strWidth))
                    return;
                int height = Integer.parseInt(editTextHeight.getText().toString());
                int width = Integer.parseInt(editTextWidth.getText().toString());


                MapLogic mapLogic = new MapLogic();
                long mapId = mapLogic.createMap(name, height, width, getContext());
                mListener.onDialogSet(mapId);
                dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogCancel();
                dismiss();
            }
        });
        return builder.create();
    }

    private void loadViewElements(View rootView){

        editTextName = (EditText) rootView.findViewById(R.id.editTextName);
        editTextHeight = (EditText) rootView.findViewById(R.id.editTextHeight);
        editTextWidth = (EditText) rootView.findViewById(R.id.editTextWidth);

    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the BoletusLoginDialogListener in case the host needs to query it. */
    public interface NewMapDialogCallbacks {

        //public void onDialogLoginClick(String userEmail, String userPassword);

        void onDialogSet(long mapId);
        void onDialogCancel();

    }

}
