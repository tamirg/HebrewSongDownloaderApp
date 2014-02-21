package com.alfa.utils.ui;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.alfa.HebrewSongDownloaderApp.LibrarySongsFragment;
import com.alfa.HebrewSongDownloaderApp.PlayerFragment;
import com.alfa.HebrewSongDownloaderApp.R;
import com.alfa.utils.logic.DataUtils;
import com.alfa.utils.logic.LogUtils;

/**
 * Created by Micha on 2/20/14.
 */
public class FragmentUtils {

    public static FragmentManager fm;

    public static void loadSearchFragment() {

    }

    public static void loadLibraryFragment(FragmentManager fm, Context context) {

        LogUtils.logData("flow_debug", "LibraryFragment__loading loadLibraryFragment..");
        if (fm != null) {

            FragmentTransaction ft = fm.beginTransaction();

            LibrarySongsFragment libSongFragment = new LibrarySongsFragment(DataUtils.getSongNamesFromDirectory());
            PlayerFragment player = libSongFragment.createPlayer(context);

            // load player
            ft.replace(R.id.player_container, player);

            // load file list
            ft.replace(R.id.library_files_container, libSongFragment);

            ft.commit();
        }
    }
}
