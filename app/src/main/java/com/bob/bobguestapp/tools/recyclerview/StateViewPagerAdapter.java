package com.bob.bobguestapp.tools.recyclerview;


import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.bob.uimodule.views.viewpager.DynamicHeightRtlViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roi on 13/06/16.
 */
public class StateViewPagerAdapter extends FragmentStatePagerAdapter {

    private int mCurrentPosition = -1;

    protected final List<Fragment> fragmentList = new ArrayList<>();
    protected final List<String> fragmentTitleList = new ArrayList<>();

    public StateViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {

        fragmentList.add(fragment);
        fragmentTitleList.add(title);

    }

    public void clearFragments() {

        fragmentList.clear();
        fragmentTitleList.clear();

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (position != mCurrentPosition) {
            Fragment fragment = (Fragment) object;
            StateViewPager pager = (StateViewPager) container;
            if (fragment != null && fragment.getView() != null) {
                mCurrentPosition = position;
                pager.measureCurrentView(fragment.getView());
            }
        }
    }

}