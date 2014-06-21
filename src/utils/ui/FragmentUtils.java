package utils.ui;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.alfa.HebrewSongDownloaderApp.R;
import entities.SongResult;
import ui.downloads.DownloadListFragment;
import ui.library.LibrarySongsFragment;
import ui.library.PlayerFragment;
import ui.search.SongListFragment;
import utils.logic.DataUtils;
import utils.logic.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Micha on 2/20/14.
 */
public class FragmentUtils {

    public static FragmentManager fm;
    private static List<String> downloads;
    private static List<String> librarySongs = null;
    private static FragmentActivity parentActivity;

    public static void initFragmentManager(FragmentActivity parentActivity) {
        FragmentUtils.parentActivity = parentActivity;
    }

    public static void loadDownloadsListFragment(List<String> downloads) {

        fm = parentActivity.getSupportFragmentManager();


        if (fm != null) {

            FragmentUtils.downloads = downloads;

            FragmentTransaction ft = fm.beginTransaction();
            DownloadListFragment downloadListFragment = new DownloadListFragment(downloads);

            ft.replace(R.id.download_list_container, downloadListFragment);

            ft.commit();
        }
    }

    public static void removeDownloadAndReload(String songToBeRemoved) {

        fm = parentActivity.getSupportFragmentManager();


        if (fm != null) {

            downloads.remove(songToBeRemoved);

            FragmentTransaction ft = fm.beginTransaction();
            DownloadListFragment downloadListFragment = new DownloadListFragment(downloads);

            ft.replace(R.id.download_list_container, downloadListFragment);

            ft.commit();
        }
    }

    public static void removeLibrarySongAndReload(String songToBeRemoved) {

        fm = parentActivity.getSupportFragmentManager();


        if (fm != null) {

            librarySongs.remove(songToBeRemoved);

            FragmentTransaction ft = fm.beginTransaction();
            DownloadListFragment downloadListFragment = new DownloadListFragment(downloads);

            ft.replace(R.id.download_list_container, downloadListFragment);

            ft.commit();
        }
    }


    public static void loadSongListFragment(List<SongResult> songResults) {

        fm = parentActivity.getSupportFragmentManager();

        if (fm != null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.list_container, new SongListFragment(songResults));
            ft.commit();
        }
    }

    public static void filterLibrary(Context context, String filter) {

        List<String> filteredSongs = new ArrayList<String>();
        List<String> backupLibrarySongs;

        if (librarySongs == null) {
            librarySongs = DataUtils.getSongNamesFromDirectory();
        }

        backupLibrarySongs = new ArrayList<String>(librarySongs);

        if (filter.length() > 0) {
            for (String songName : backupLibrarySongs) {
                if (songName.toLowerCase().contains(filter.toLowerCase())) {
                    filteredSongs.add(songName);
                }
            }
        } else {
            filteredSongs = backupLibrarySongs;
        }

        loadLibraryFragment(context, filteredSongs);
    }

    public static void loadLibraryFragment(Context context) {

        LogUtils.logData("flow_debug", "FragmentUtils__loading library fragment..");

        librarySongs = DataUtils.getSongNamesFromDirectory();
        loadLibraryFragment(context, librarySongs);
    }

    public static void loadLibraryFragment(Context context, List<String> filteredList) {

        LogUtils.logData("flow_debug", "FragmentUtils__loading library fragment..");

        fm = parentActivity.getSupportFragmentManager();

        if (fm != null) {

            FragmentTransaction ft = fm.beginTransaction();

            LibrarySongsFragment libSongFragment = new LibrarySongsFragment(filteredList);
            PlayerFragment player = libSongFragment.createPlayer(context);

            // load player
            LogUtils.logData("flow_debug", "FragmentUtils__replacing player fragment..");
            ft.replace(R.id.player_container, player);
            //ft.addToBackStack(null);

            // load file list
            LogUtils.logData("flow_debug", "FragmentUtils__replacing library song fragment..");
            ft.replace(R.id.library_files_container, libSongFragment);
            //ft.addToBackStack(null);
            ft.commitAllowingStateLoss();

        }
    }
}
