package com.alfa.HebrewSongDownloaderApp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class TabsPagerAdapter extends FragmentPagerAdapter {

    private SearchFragment searchFragment;
    private DownloadsFragment downloadsFragment;
    private LibraryFragment libraryFragment;

    public TabsPagerAdapter(FragmentManager fm,
                            SearchFragment searchFragment,
                            DownloadsFragment downloadsFragment,
                            LibraryFragment libraryFragment) {
        super(fm);
        this.searchFragment = searchFragment;
        this.downloadsFragment = downloadsFragment;
        this.libraryFragment = libraryFragment;

    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0: {
                return searchFragment;
            }
            case 1: {
                return downloadsFragment;
            }
            case 2: {
                return libraryFragment;
            }
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}
