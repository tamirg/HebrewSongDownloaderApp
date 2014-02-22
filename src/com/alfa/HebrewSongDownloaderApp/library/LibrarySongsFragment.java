package com.alfa.HebrewSongDownloaderApp.library;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.alfa.utils.logic.LogUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Micha on 2/13/14.
 */
public class LibrarySongsFragment extends ListFragment {

    private List<String> songNames;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LogUtils.logData("flow_debug", "LibrarySongsFragment__create");

        LibraryAdapter libraryAdapter = new LibraryAdapter(this, inflater, getListModel(), getResources());
        setListAdapter(libraryAdapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public PlayerFragment createPlayer(Context context) {

        // create new player instance
        PlayerFragment player = new PlayerFragment(context);

        // reload song list in case of library update
        PlayerFragment.reloadSongList(songNames);

        return player;
    }

    public LibrarySongsFragment(LinkedList<String> songNames) {
        this.songNames = songNames;
    }

    // on item click used by libraryAdapter
    public void onItemClick(int position, LibraryAdapter.LibraryRowContainer rowContainer) {
        try {
            PlayerFragment.playSongAt(position);

        } catch (Exception e) {
            //UIUtils.printError(v.getContext(), "play song from list item:" + e.toString());
        }
    }

    public List<LibraryListModel> getListModel() {

        List<LibraryListModel> libraryListModel = new ArrayList<LibraryListModel>();

        LibraryListModel libModel;

        for (String songName : songNames) {

            // initialize lib model
            libModel = new LibraryListModel();

            // set list row song title to the current value
            libModel.setSongTitle(songName);

            // add current model to model list
            libraryListModel.add(libModel);
        }

        return libraryListModel;
    }
}