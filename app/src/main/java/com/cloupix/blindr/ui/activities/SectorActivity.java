package com.cloupix.blindr.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.cloupix.blindr.R;
import com.cloupix.blindr.business.SectorView;
import com.cloupix.blindr.business.adapters.EditSectorPagerAdapter;

/**
 * Created by alonsoapp on 04/05/16.
 *
 */
public class SectorActivity extends AppCompatActivity {

    public static int EDIT_SECTOR_REQUEST = 1;
    public static String EXTRA_SECTOR_VIEW = "sector_view";
    public static final String EXTRA_SECTOR_VIEW_ID = "sector_view_id";

    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    private EditSectorPagerAdapter editSectorPagerAdapter;
    private ViewPager mViewPager;
    private SectorView sectorView;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sector);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(!getIntent().hasExtra(EXTRA_SECTOR_VIEW) && !getIntent().hasExtra(EXTRA_SECTOR_VIEW_ID)) {
            finish();
            return;
        }

        this.sectorView = (SectorView) getIntent().getParcelableExtra(EXTRA_SECTOR_VIEW);
        //MapLogic mapLogic = new MapLogic();
        //this.sectorView = mapLogic.getSectorViewBySectorId(getIntent().getLongExtra(EXTRA_SECTOR_VIEW_ID, -1), getApplicationContext());

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        editSectorPagerAdapter = new EditSectorPagerAdapter(getFragmentManager());
        editSectorPagerAdapter.setSectorView(sectorView);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        if(mViewPager!=null)
            mViewPager.setAdapter(editSectorPagerAdapter);

        setTitle("Sector " + sectorView.getMatrixX() + "x"+ sectorView.getMatrixY());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sector, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                sendResult(sectorView);
                return true;
            default:
                break;
        }

        return false;
    }

    private void sendResult(SectorView sectorView){
        // Create intent to deliver some kind of result data
        Intent returnIntent = new Intent();
        returnIntent.putExtra(EXTRA_SECTOR_VIEW, sectorView);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
