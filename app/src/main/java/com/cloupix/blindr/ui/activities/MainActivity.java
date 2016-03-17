package com.cloupix.blindr.ui.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cloupix.blindr.R;
import com.cloupix.blindr.ui.fragments.FingerprintingFragment;
import com.cloupix.blindr.ui.fragments.WifiListFragment;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {

    private static final int FINGERPRINTING_FRAGMENT = 0;
    private static final int WIFI_FRAGMENT = 1;

    private int currentFragment = WIFI_FRAGMENT;

    private Fragment fingerprintingFragment, wifiFragment;
    private FloatingActionButton fab = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                //sendData();
                updateFragment(currentFragment==FINGERPRINTING_FRAGMENT?WIFI_FRAGMENT:FINGERPRINTING_FRAGMENT);
            }
        });


        updateFragment(WIFI_FRAGMENT);

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Fragment getFragment(int fragmentId){
        switch (fragmentId) {
            case FINGERPRINTING_FRAGMENT:

                if(fingerprintingFragment == null)
                    fingerprintingFragment = new FingerprintingFragment();
                return fingerprintingFragment;
            case WIFI_FRAGMENT:
                if(wifiFragment == null)
                    wifiFragment = new WifiListFragment();
                return wifiFragment;
            default:
                fingerprintingFragment = new FingerprintingFragment();
                return fingerprintingFragment;
        }
    }

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
        updateFloatingActionButton(fragmentId);
    }

    private void updateFloatingActionButton(int fragmentId){
        if(fab==null)
            return;
        switch (fragmentId) {
            case FINGERPRINTING_FRAGMENT:
                fab.setImageDrawable(getResources().getDrawable(R.mipmap.ic_timeline_white_48dp, getApplicationContext().getTheme()));
                break;
            case WIFI_FRAGMENT:
                fab.setImageDrawable(getResources().getDrawable(R.mipmap.ic_fingerprint_white_48dp, getApplicationContext().getTheme()));
                break;
        }
    }

    // Metodo de prueba de UDP, se puede borrar o aprender algo de él
    private void sendData(){

        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {

                try {

                    String messageStr="Hello Android!";
                    int server_port = 21567;
                    DatagramSocket s = new DatagramSocket();
                    InetAddress local = InetAddress.getByName("192.168.2.1");
                    int msg_length=messageStr.length();
                    byte[] message = messageStr.getBytes();
                    DatagramPacket p = new DatagramPacket(message, msg_length,local,server_port);
                    s.send(p);

                }catch (Exception e){
                    e.printStackTrace();
                }

                return null;
            }
        }.execute();

    }
}
