package com.alfa.HebrewSongDownloaderApp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import com.alfa.utils.logic.LogUtils;
import com.alfa.utils.logic.SharedPref;


public class TabsPagerAdapter extends FragmentPagerAdapter {

    private static SearchFragment searchFragmentInstance;
    private static DownloadsFragment downloadsFragmentInstance;
    private static LibraryFragment libraryFragmentInstance;

    public TabsPagerAdapter(FragmentManager fm,
                            SearchFragment searchFragment,
                            DownloadsFragment downloadsFragment,
                            LibraryFragment libraryFragment) {
        super(fm);
        searchFragmentInstance = searchFragment;
        downloadsFragmentInstance = downloadsFragment;
        libraryFragmentInstance = libraryFragment;

    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0: {
                return searchFragmentInstance;
            }
            case 1: {
                return downloadsFragmentInstance;
            }
            case 2: {
                return libraryFragmentInstance;
            }
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count
        return SharedPref.tabCount;
    }


    // this important functions prevents from a tab fragment to be destroyed and thus give prevents the tab from being unnecessarily refreshed
    @Override
    public void destroyItem(View container, int position, Object object) {

        if (SharedPref.destroyTabFragmentOnAction) {
            super.destroyItem(container, position, object);
        } else {
            LogUtils.logData("flow_debug", "TabsPagerAdapter__did not destroy fragment [" + position + "] (" + object.toString() + ")");
        }
    }
}
