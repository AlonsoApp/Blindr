package com.cloupix.blindr.business.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.cloupix.blindr.business.SectorView;
import com.cloupix.blindr.ui.fragments.EditSectorViewWallsFragment;
import com.cloupix.blindr.ui.fragments.EditSectorViewWifiAPsFragment;

/**
 * Created by alonsoapp on 03/05/16.
 *
 */
public class EditSectorPagerAdapter extends FragmentPagerAdapter {

    public static final int FRAGMENT_WALLS = 0;
    public static final int FRAGMENT_WIFI_APS = 1;

    private Fragment[] aFragments;
    private SectorView sectorView;

    public EditSectorPagerAdapter(FragmentManager fm) {
        super(fm);
        aFragments = new Fragment[2];
        aFragments[FRAGMENT_WALLS] = new EditSectorViewWallsFragment();
        aFragments[FRAGMENT_WIFI_APS] = new EditSectorViewWifiAPsFragment();
    }

    @Override
    public Fragment getItem(int position) {
        return aFragments[position];
    }

    @Override
    public int getCount() {
        return aFragments.length;
    }

    // Helpers

    public void setSectorView(SectorView sectorView){
        this.sectorView = sectorView;
        EditSectorViewWallsFragment wallsFragment = (EditSectorViewWallsFragment) aFragments[FRAGMENT_WALLS];
        wallsFragment.setSectorView(sectorView);
        EditSectorViewWifiAPsFragment wifiFragment = (EditSectorViewWifiAPsFragment) aFragments[FRAGMENT_WIFI_APS];
        wifiFragment.setSectorView(sectorView);
    }

    public void notifySectorViewChanged(){
        EditSectorViewWallsFragment wallsFragment = (EditSectorViewWallsFragment) aFragments[FRAGMENT_WALLS];
        wallsFragment.setSectorView(sectorView);
        //wallsFragment.notifySectorViewChanged();

        EditSectorViewWifiAPsFragment wifiFragment = (EditSectorViewWifiAPsFragment) aFragments[FRAGMENT_WIFI_APS];
        wifiFragment.setSectorView(sectorView);
        //wifiFragment.notifySectorViewChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Walls";
            case 1:
                return "WiFi Access Points";
        }

        return null;
    }
}
