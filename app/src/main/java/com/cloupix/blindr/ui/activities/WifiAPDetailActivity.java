package com.cloupix.blindr.ui.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cloupix.blindr.R;
import com.cloupix.blindr.ui.fragments.FingerprintingFragment;
import com.cloupix.blindr.ui.fragments.WifiAPDetailFragment;
import com.cloupix.blindr.ui.fragments.WifiListFragment;

public class WifiAPDetailActivity extends AppCompatActivity {

    public static String EXTRA_BSSI = "extra_bssi";

    public static final int WIFI_AP_DETAIL_FRAGMENT = 1;

    private WifiAPDetailFragment wifiAPDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_apdetail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Sacamos el extra BSSI
        String bssi = getIntent().getStringExtra(EXTRA_BSSI);

        // A este fragment le pasamos el Extra de BSSI
        Bundle bundle = new Bundle();
        bundle.putString(WifiAPDetailFragment.ARG_BSSI, bssi);

        updateFragment(WIFI_AP_DETAIL_FRAGMENT, bundle);
    }

    private Fragment getFragment(int fragmentId){
        switch (fragmentId) {
            case WIFI_AP_DETAIL_FRAGMENT:

                if(wifiAPDetailFragment == null)
                    wifiAPDetailFragment = new WifiAPDetailFragment();
                return wifiAPDetailFragment;
            default:
                wifiAPDetailFragment = new WifiAPDetailFragment();
                return wifiAPDetailFragment;
        }
    }

    private void updateFragment(int fragmentId, Bundle args){

        Fragment newFragment = getFragment(fragmentId);
        if(args!=null)
            newFragment.setArguments(args);
        // Create new transaction
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack if needed
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

}
