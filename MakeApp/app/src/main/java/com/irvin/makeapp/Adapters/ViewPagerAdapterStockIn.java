package com.irvin.makeapp.Adapters;

import android.view.ViewGroup;

import com.irvin.makeapp.Adapters.InventoryFragment.TabFragmentStockInInventory;
import com.irvin.makeapp.Adapters.InventoryFragment.TabFragmentStockInList;

import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 *
 * @author irvin
 * @date 2/6/17
 */
public class ViewPagerAdapterStockIn extends FragmentStatePagerAdapter {

    private Map<Integer, String> mFragmentTags;
    private FragmentManager mFragmentManager;

    public ViewPagerAdapterStockIn(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
        mFragmentTags = new HashMap<Integer, String>();
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 1:
                return new TabFragmentStockInInventory();
            case 0:
                return new TabFragmentStockInList();
            default:
                return null;
        }

    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        if (obj instanceof Fragment) {
            // record the fragment tag here.
            Fragment f = (Fragment) obj;
            String tag = f.getTag();
            mFragmentTags.put(position, tag);
        }
        return obj;
    }

    @Override
    public int getItemPosition(Object object) {
        // POSITION_NONE makes it possible to reload the PagerAdapter
        return POSITION_NONE;
    }


    public Fragment getFragment(int position) {
        String tag = mFragmentTags.get(position);
        if (tag == null) {
            return null;
        }
        return mFragmentManager.findFragmentByTag(tag);
    }

    @Override
    public int getCount() {
        return 2;
        // As there are only 3 Tabs
    }
}
