package com.alfa.HebrewSongDownloaderApp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.alfa.utils.DataUtils;
import com.alfa.utils.SharedPref;

import java.util.LinkedList;

/**
 * Created by Micha on 2/13/14.
 */

public class LibraryFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.library_fragment, container, false);
        setupFragmentView(view);
        return view;
    }

    private void setupFragmentView(View view) {

        FragmentManager fm = getFragmentManager();

        if (fm != null) {
            FragmentTransaction ft = fm.beginTransaction();
            // replaced with
            //ft.replace(R.id.library_files_container, new SongListFragment(getSongNamesFromDirectory()));
            ft.replace(R.id.library_files_container, new LibrarySongsFragment(getSongNamesFromDirectory()));
            ft.commit();
        }
    }

    private LinkedList<String> getSongNamesFromDirectory() {
        LinkedList<String> songNames = new LinkedList<String>();


        songNames = (LinkedList<String>) DataUtils.listFiles(SharedPref.songDirectory);

        if (songNames.size() == 0) {
            songNames.add("no songs in library");
        }
        return songNames;
    }

}