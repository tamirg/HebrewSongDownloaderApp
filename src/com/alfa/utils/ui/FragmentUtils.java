package com.alfa.utils.ui;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.alfa.HebrewSongDownloaderApp.LibrarySongsFragment;
import com.alfa.HebrewSongDownloaderApp.PlayerFragment;
import com.alfa.HebrewSongDownloaderApp.R;
import com.alfa.HebrewSongDownloaderApp.SongListFragment;
import com.alfa.utils.logic.DataUtils;
import com.alfa.utils.logic.LogUtils;
import entities.SongResult;

import java.util.List;

/**
 * Created by Micha on 2/20/14.
 */
public class FragmentUtils {

    public static FragmentManager fm;

    public static void initFragmentManager(FragmentManager initializedFragmentManager) {
        fm = initializedFragmentManager;
    }

    public static void loadSongListFragment(List<SongResult> songResults) {

        if (fm != null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.list_container, new SongListFragment(songResults));
            ft.commit();
        }
    }

    public static void loadLibraryFragment(Context context) {

        LogUtils.logData("flow_debug", "FragmentUtils__loading library fragment..");

        if (fm != null) {

            FragmentTransaction ft = fm.beginTransaction();

            LibrarySongsFragment libSongFragment = new LibrarySongsFragment(DataUtils.getSongNamesFromDirectory());
            PlayerFragment player = libSongFragment.createPlayer(context);

            // load player
            LogUtils.logData("flow_debug", "FragmentUtils__replacing player fragment..");
            ft.replace(R.id.player_container, player);
            ft.addToBackStack(null);

            // load file list
            LogUtils.logData("flow_debug", "FragmentUtils__replacing library song fragment..");
            ft.replace(R.id.library_files_container, libSongFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }
}
