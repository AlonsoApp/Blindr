package com.cloupix.blindr.ui.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.cloupix.blindr.R;
import com.cloupix.blindr.ui.fragments.FingerprintingFragment;
import com.cloupix.blindr.ui.fragments.MapBuilderFragment;

public class FingerprintingActivity extends AppCompatActivity implements FingerprintingFragment.FingerprintingFragmentCallbacks {

    private static final int FINGERPRINTING_FRAGMENT = 0;
    public static final int NEW_MAP_FRAGMENT = 1;
    public static final int EDIT_MAP_FRAGMENT = 2;

    private Fragment fingerprintingFragment, newMapFragment;
    private FloatingActionButton fab = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprinting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Abrimos siempre el fingerprinting_fragment y él ya se encargará de mirar si hay maps creados y pedirnos que saquemos el newMapFragment si no hay
        replaceFragment(FINGERPRINTING_FRAGMENT, null);
    }

    private Fragment getFragment(int fragmentId){
        switch (fragmentId) {
            case FINGERPRINTING_FRAGMENT:
                // We show the Fab button
                updateFloatingActionButton(true);
                if(fingerprintingFragment == null)
                    fingerprintingFragment = new FingerprintingFragment();
                return fingerprintingFragment;
            case NEW_MAP_FRAGMENT:
                // We hide the Fab button
                updateFloatingActionButton(false);
                if(newMapFragment == null)
                    newMapFragment = new MapBuilderFragment();
                return newMapFragment;
            case EDIT_MAP_FRAGMENT:
                // We hide the Fab button
                updateFloatingActionButton(false);
                if(newMapFragment == null)
                    newMapFragment = new MapBuilderFragment();
                return newMapFragment;
            default:
                fingerprintingFragment = new FingerprintingFragment();
                return fingerprintingFragment;
        }
    }

    private void replaceFragment(int fragmentId, Bundle args){
        Fragment fragment = getFragment(fragmentId);

        String backStateName =  fragment.getClass().getName();
        String fragmentTag = backStateName;

        FragmentManager manager = getFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null){
            //fragment not in back stack, create it.
            fragment.setArguments(args);
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.fragment_container, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    private void updateFloatingActionButton(boolean show){
        if(fab==null)
            return;
        if(show)
            fab.show();
        else
            fab.hide();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                // This is for the fragment
                return false;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onBackPressed(){
        if (getFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onEditMap(long mapId) {
        Bundle args = new Bundle();
        args.putLong(MapBuilderFragment.ARG_MAP_ID, mapId);
        replaceFragment(NEW_MAP_FRAGMENT, args);
    }

    @Override
    public void onNewMap() {
        replaceFragment(NEW_MAP_FRAGMENT, null);
    }
}
