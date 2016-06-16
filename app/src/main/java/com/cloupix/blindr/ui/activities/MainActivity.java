package com.cloupix.blindr.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.cloupix.blindr.R;
import com.cloupix.blindr.dao.SQLHelper;
import com.cloupix.blindr.ui.fragments.FingerprintingFragment;
import com.cloupix.blindr.ui.fragments.WifiAPDetailFragment;
import com.cloupix.blindr.ui.fragments.WifiListFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity implements WifiListFragment.WifiListFragmentCallbacks{

    //private static final int FINGERPRINTING_FRAGMENT = 0;
    private static final int WIFI_LIST_FRAGMENT = 0;
    private static final int WIFI_AP_DETAIL_FRAGMENT = 1;


    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_LOCATION = 2;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };


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
    protected void onResume() {
        super.onResume();
        verifyLocationPermissions(this);
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
            case R.id.action_export_database:
                if (verifyStoragePermissions(this)) {
                    //exportDatabase();
                    try{
                        FileInputStream fis = new FileInputStream(getApplicationContext().getDatabasePath(SQLHelper.DATABASE_NAME));
                        String exportPath = Environment.getExternalStorageDirectory().getPath().concat("/"+SQLHelper.DATABASE_NAME);
                        FileOutputStream fos = new FileOutputStream(exportPath);

                        copyFile(fis, fos, "DB exported to " + exportPath, "DB exportDatabase ERROR");
                    }catch (Exception e){
                        e.printStackTrace();

                    }

                }

                return true;
            case R.id.action_import_database:
                if (verifyStoragePermissions(this)) {
                    //exportDatabase();
                    try{
                        String importPath = Environment.getExternalStorageDirectory().getPath().concat("/"+SQLHelper.DATABASE_NAME);
                        FileInputStream fis = new FileInputStream(importPath);
                        FileOutputStream fos = new FileOutputStream(getApplicationContext().getDatabasePath(SQLHelper.DATABASE_NAME));

                        copyFile(fis, fos, "DB imported correctly", "DB import ERROR");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                return true;

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


    public boolean verifyLocationPermissions(Activity activity) {
        // Check if we have write permission
        int permissionFine = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCoarse = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permissionFine != PackageManager.PERMISSION_GRANTED || permissionCoarse != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_LOCATION,
                    REQUEST_LOCATION
            );
            return false;
        }else{
            return true;
        }
    }


    // DATABASE DUMP

    private void exportDatabase(){
        String dbname = SQLHelper.DATABASE_NAME;
        File f = getApplicationContext().getDatabasePath(dbname);

        FileInputStream fis=null;
        FileOutputStream fos=null;

        try
        {
            String exportPath = Environment.getExternalStorageDirectory().getPath().concat("/"+SQLHelper.DATABASE_NAME);
            fis=new FileInputStream(f);
            fos=new FileOutputStream(exportPath);
            InputStream in = fis;
            OutputStream out = fos;

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();

            Toast.makeText(this, "DB exported to " + exportPath, Toast.LENGTH_LONG).show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this, "DB exportDatabase ERROR", Toast.LENGTH_LONG).show();
        }
        finally
        {
            try
            {
                fos.close();
                fis.close();
            }
            catch(IOException ioe)
            {}
        }

    }

    private void copyFile(FileInputStream fis, FileOutputStream fos, String msgOk, String msgError){
        try
        {
            InputStream in = fis;
            OutputStream out = fos;

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();

            Toast.makeText(this, msgOk, Toast.LENGTH_LONG).show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this, msgError, Toast.LENGTH_LONG).show();
        }
        finally
        {
            try
            {
                fos.close();
                fis.close();
            }
            catch(IOException ioe)
            {}
        }
    }


    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public boolean verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    //exportDatabase();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
