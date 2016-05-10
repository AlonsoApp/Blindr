package com.cloupix.blindr.ui.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.cloupix.blindr.R;
import com.cloupix.blindr.ui.fragments.FingerprintingFragment;
import com.cloupix.blindr.ui.fragments.WifiAPDetailFragment;
import com.cloupix.blindr.ui.fragments.WifiListFragment;

public class MainActivity extends AppCompatActivity implements WifiListFragment.WifiListFragmentCallbacks{

    //private static final int FINGERPRINTING_FRAGMENT = 0;
    private static final int WIFI_LIST_FRAGMENT = 0;
    private static final int WIFI_AP_DETAIL_FRAGMENT = 1;

    private Fragment fingerprintingFragment, wifiFragment, wifiAPDetailFragment;
    //private FloatingActionButton fab = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                //sendData();
                updateFragment(currentFragment==FINGERPRINTING_FRAGMENT?WIFI_LIST_FRAGMENT:FINGERPRINTING_FRAGMENT);
            }
        });
        */


        replaceFragment(WIFI_LIST_FRAGMENT, null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_fingerprinting:
                Intent intent = new Intent(this, FingerprintingActivity.class);
                startActivity(intent);
                return true;
            // case R.id.finger:

        }

        return super.onOptionsItemSelected(item);
    }

    private Fragment getFragment(int fragmentId){
        switch (fragmentId) {
            /*
            case FINGERPRINTING_FRAGMENT:

                if(fingerprintingFragment == null)
                    fingerprintingFragment = new FingerprintingFragment();
                return fingerprintingFragment;
            */
            case WIFI_LIST_FRAGMENT:
                if(wifiFragment == null)
                    wifiFragment = new WifiListFragment();
                return wifiFragment;
            case WIFI_AP_DETAIL_FRAGMENT:
                if(wifiAPDetailFragment == null)
                    wifiAPDetailFragment = new WifiAPDetailFragment();
                return wifiAPDetailFragment;
            default:
                fingerprintingFragment = new FingerprintingFragment();
                return fingerprintingFragment;
        }
    }

    /*
    private void updateFragment(int fragmentId){

        Fragment newFragment = getFragment(fragmentId);
        // Create new transaction
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack if needed
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
        currentFragment = fragmentId;
        //updateFloatingActionButton(fragmentId);
    }
    */

    /*
    private void updateFloatingActionButton(int fragmentId){
        if(fab==null)
            return;
        switch (fragmentId) {
            case FINGERPRINTING_FRAGMENT:
                fab.setImageDrawable(getResources().getDrawable(R.mipmap.ic_timeline_white_48dp, getApplicationContext().getTheme()));
                break;
            case WIFI_LIST_FRAGMENT:
                fab.setImageDrawable(getResources().getDrawable(R.mipmap.ic_fingerprint_white_48dp, getApplicationContext().getTheme()));
                break;
        }
    }
    */



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
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    @Override
    public void onWifiAPClick(String bssi) {
        // A este fragment le pasamos el Extra de BSSI
        Bundle bundle = new Bundle();
        bundle.putString(WifiAPDetailFragment.ARG_BSSI, bssi);

        replaceFragment(WIFI_AP_DETAIL_FRAGMENT, bundle);
    }

    @Override
    public void onBackPressed(){
        if (getFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        else {
            getFragmentManager().popBackStack();
            //super.onBackPressed();
        }
    }
}
