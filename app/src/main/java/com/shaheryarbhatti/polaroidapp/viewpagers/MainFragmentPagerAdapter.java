package com.shaheryarbhatti.polaroidapp.viewpagers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shaheryarbhatti.polaroidapp.R;
import com.shaheryarbhatti.polaroidapp.fragments.DashboardFragment;
import com.shaheryarbhatti.polaroidapp.fragments.FeaturedFragment;
import com.shaheryarbhatti.polaroidapp.fragments.PopularFragment;

/**
 * Created by shaheryarbhatti on 14/12/2017.
 */

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public MainFragmentPagerAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){

            case 0:
                return DashboardFragment.newInstance("", "");
            case 1:
                return FeaturedFragment.newInstance("", "");
            case 2:
                return PopularFragment.newInstance("", "");
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return mContext.getString(R.string.dashboard_fragment);
            case 1:
                return mContext.getString(R.string.featured_fragment);
            case 2:
                return mContext.getString(R.string.popular_fragment);
        }
        return null;
    }
}
