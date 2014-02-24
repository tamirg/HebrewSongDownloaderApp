package com.alfa.HebrewSongDownloaderApp.library;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.alfa.utils.logic.LogUtils;
import com.alfa.utils.ui.UIUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Micha on 2/13/14.
 */
public class LibrarySongsFragment extends ListFragment {

    /**
     * ******************************************************************
     * ******************* Library data members  ************************
     * ******************************************************************
     */

    private List<String> songNames;
    private static LibraryAdapter libraryAdapter;
    private static List<LibraryListModel> libraryListModels;

    /**
     * ******************************************************************
     * ******************* Library view functions  **********************
     * ******************************************************************
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LogUtils.logData("flow_debug", "LibrarySongsFragment__create");

        setListModels();

        libraryAdapter = new LibraryAdapter(this, inflater, getResources());
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

    public LibrarySongsFragment(List<String> songNames) {
        this.songNames = songNames;
    }

    // on item click event
    public void onItemClick(int position, LibraryAdapter.LibraryRowContainer rowContainer) {
        try {
            PlayerFragment.playSongAt(position);

        } catch (Exception e) {
            //UIUtils.printError(v.getContext(), "play song from list item:" + e.toString());
        }
    }

    // on long item click event
    public void onItemLongClick(int position, LibraryAdapter.LibraryRowContainer rowContainer) {
        try {
            // just for test
            UIUtils.PrintToast(rowContainer.rowView.getContext(), "long click test", Toast.LENGTH_SHORT);

            // TODO : micha (rename feature)

            // 1) make song title text view invisible
            // 2) make edit text visible
            // 3) set edit text value to be the song title value
            // 4) update list on modification


        } catch (Exception e) {
            //UIUtils.printError(v.getContext(), "play song from list item:" + e.toString());
        }
    }

    /**
     * ******************************************************************
     * ******************* Library Model functions  *********************
     * ******************************************************************
     */

    public void setListModels() {

        libraryListModels = new LinkedList<LibraryListModel>();

        LibraryListModel libModel;

        for (String songName : songNames) {

            // initialize lib model
            libModel = new LibraryListModel();

            // set list row song title to the current value
            libModel.setSongTitle(songName);

            // set playing mode to be false
            libModel.setPlaying(false);

            // add current model to model list
            libraryListModels.add(libModel);
        }

    }

    public static List<LibraryListModel> getLibraryListModels() {
        return libraryListModels;
    }


    /**
     * ******************************************************************
     * ******************* Library adapter functions  *******************
     * ******************************************************************
     */

    // update list modifications when changing one of the list views
    public static void updateListModifications() {
        libraryAdapter.notifyDataSetChanged();
    }

    /**
     * ******************************************************************
     * ******************** Library API functions  **********************
     * ******************************************************************
     */

    public static void setPlaying(int position, boolean isPlaying) {

        if (position < 0 || position >= libraryListModels.size()) {
            return;
        }

        libraryListModels.get(position).setPlaying(isPlaying);
        updateListModifications();
    }


}