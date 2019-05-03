package com.scriptfloor.scanvix;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.scriptfloor.scanvix.fragment.HomeFragment;
import com.scriptfloor.scanvix.fragment.InfoFragment;
import com.scriptfloor.scanvix.fragment.ResultFragment;

/**
 * Created by LINCOLN on 3/3/2019.
 */

public class FragmentAdapter extends FragmentStatePagerAdapter {
    int pages;
    public FragmentAdapter(FragmentManager fm,int pages) {
        super(fm);
        this.pages=pages;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                ResultFragment resultFragment = new ResultFragment();
                return resultFragment;
            case 2:
                InfoFragment infoFragment = new InfoFragment();
                return infoFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return pages;
    }
}
