package com.cloupix.blindr.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.cloupix.blindr.R;
import com.cloupix.blindr.business.SectorView;
import com.cloupix.blindr.business.adapters.GridAdapter;
import com.cloupix.blindr.business.Map;
import com.cloupix.blindr.logic.MapLogic;
import com.cloupix.blindr.ui.dialogs.EditSectorDialog;
import com.cloupix.blindr.ui.dialogs.NewMapDialog;

/**
 * Created by alonsoapp on 27/04/16.
 *
 */
public class NewMapFragment extends Fragment implements AdapterView.OnItemClickListener, EditSectorDialog.EditSectorDialogCallbacks {

    public static final String ARG_MAP_ID = "map_id";

    private MapLogic mapLogic;
    private Map map;
    private GridAdapter gridAdapter;

    private EditText editTextName;
    private GridView gridView;

    private EditSectorDialog editSectorDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mapLogic = new MapLogic();
        this.map = new Map();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_map, container, false);

        editTextName = (EditText) rootView.findViewById(R.id.editTextName);
        gridView = (GridView) rootView.findViewById(R.id.gridview);

        gridAdapter = new GridAdapter(getContext(), map, true);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(this);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Sacamos los agrs
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            long mapId = bundle.getLong(ARG_MAP_ID, -1);
            loadMap(mapId);
        } else {
            showCreateDialog();
        }
    }

    private void loadMap(long mapId){
        map = mapLogic.getMapById(mapId, getContext());
        editTextName.setText(map.getName());
        gridAdapter.setMap(map);
        gridAdapter.notifyDataSetChanged();
    }

    private void showCreateDialog(){
        NewMapDialog newMapDialog = new NewMapDialog();
        newMapDialog.setNewMapDialogCallbacks(new NewMapDialog.NewMapDialogCallbacks() {
            @Override
            public void onDialogSet(long mapId) {
                loadMap(mapId);
            }

            @Override
            public void onDialogCancel() {
                getFragmentManager().popBackStack();
            }
        });
        newMapDialog.show(getActivity().getFragmentManager(), "NewMapDialogListener");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Crear dialog, pasarle el sectorView, conseguirlo actualizado, cambairlo en la posición y
        // actualizar grid
        if(editSectorDialog == null) {
            editSectorDialog = new EditSectorDialog();
            editSectorDialog.setEditSectorDialogCallbacks(this);
        }
        if(gridAdapter.getItem(position) instanceof SectorView) {
            editSectorDialog.setSectorView((SectorView) gridAdapter.getItem(position));
            editSectorDialog.notifySectorViewChanged();
        }

    }

    @Override
    public void onSectorViewSave(SectorView sectorView) {
        map.getaSectors()[sectorView.getListN()] = sectorView;
        gridAdapter.notifyDataSetChanged();
    }

    // TODO Save map
}
