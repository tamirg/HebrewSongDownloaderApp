package com.alfa.HebrewSongDownloaderApp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
                return MainActivity.libraryFragment;
            }
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count
        return SharedPref.tabCount;
    }

}
