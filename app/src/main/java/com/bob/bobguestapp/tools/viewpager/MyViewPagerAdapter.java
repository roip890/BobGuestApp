package com.bob.bobguestapp.tools.viewpager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.bob.uimodule.views.viewpager.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyViewPagerAdapter extends FragmentStatePagerAdapter {

    protected final List<Fragment> fragmentList = new ArrayList<>();
    protected final List<String> fragmentTitleList = new ArrayList<>();

    public MyViewPagerAdapter(FragmentManager manager) {
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

}
