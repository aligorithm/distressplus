package xyz.yisa.distressplus.application;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import xyz.yisa.distressplus.fragments.AlertFragment;
import xyz.yisa.distressplus.fragments.ContactFragment;
import xyz.yisa.distressplus.fragments.HomeFragment;
import xyz.yisa.distressplus.fragments.ProfileFragment;

public class HomePagerAdapter extends FragmentPagerAdapter {
    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position){
            case 0 : return new HomeFragment();
            case 1 : return new AlertFragment();
            case 2 : return new ContactFragment();
            case 3 : return new ProfileFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0 :
                return "";
            case 1 :
                return "";
            case 2 :
                return "";
            case 3 :
                return "";
        }
        return null;
    }
}
