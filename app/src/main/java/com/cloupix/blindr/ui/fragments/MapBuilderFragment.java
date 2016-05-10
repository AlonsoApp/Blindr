package com.cloupix.blindr.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.cloupix.blindr.ui.activities.SectorActivity;
import com.cloupix.blindr.ui.dialogs.NewMapDialog;

/**
 * Created by alonsoapp on 27/04/16.
 *
 */
public class MapBuilderFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    public static final String ARG_MAP_ID = "map_id";

    private MapLogic mapLogic;
    private Map map;
    private GridAdapter gridAdapter;

    private FloatingActionButton fab;
    private EditText editTextName;
    private GridView gridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mapLogic = new MapLogic();
        this.map = new Map();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_map, container, false);

        editTextName = (EditText) rootView.findViewById(R.id.editTextName);
        editTextName.setText("");
        gridView = (GridView) rootView.findViewById(R.id.gridview);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        gridAdapter = new GridAdapter(getContext(), map, GridAdapter.BUILD_MODE);
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
            fab.hide();
            showCreateDialog();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_new_map, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveMap();
                getActivity().onBackPressed();
                return true;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.fragment_title_builder);
        if(map!=null && editTextName !=null)
            editTextName.setText(map.getName());
        else if(editTextName !=null)
            editTextName.setText("");
    }

    private void loadMap(long mapId){
        map = mapLogic.getMapById(mapId, getContext());
        editTextName.setText(map.getName());
        gridView.setNumColumns(map.getWidth());
        gridAdapter.setMap(map);
        gridAdapter.notifyDataSetChanged();
    }

    private void showCreateDialog(){
        NewMapDialog newMapDialog = new NewMapDialog();
        newMapDialog.setNewMapDialogCallbacks(new NewMapDialog.NewMapDialogCallbacks() {
            @Override
            public void onDialogSet(long mapId) {
                loadMap(mapId);
                fab.show();
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

        if(!(gridAdapter.getItem(position) instanceof SectorView))
            return;
        Intent intent = new Intent(getContext(), SectorActivity.class);
        intent.putExtra(SectorActivity.EXTRA_SECTOR_VIEW, (SectorView) gridAdapter.getItem(position));
        //intent.putExtra(SectorActivity.EXTRA_SECTOR_VIEW_ID, ((SectorView) gridAdapter.getItem(position)).getSectorId());
        startActivityForResult(intent, SectorActivity.EDIT_SECTOR_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SectorActivity.EDIT_SECTOR_REQUEST) {
            if(resultCode == Activity.RESULT_OK){
                SectorView resultSector = (SectorView) data.getParcelableExtra(SectorActivity.EXTRA_SECTOR_VIEW);
                map.getaSectors()[resultSector.getListN()] = resultSector;
                gridAdapter.notifyDataSetChanged();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void saveMap(){
        map.setName(editTextName.getText().toString());
        mapLogic.saveMap(map, getContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.alert_delete_title);
                builder.setMessage(R.string.alert_delete_msg);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mapLogic==null)
                            mapLogic = new MapLogic();
                        mapLogic.deleteMap(map.getMapId(), getContext());
                        getActivity().onBackPressed();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, null);
                builder.show();
                break;
        }
    }
}
