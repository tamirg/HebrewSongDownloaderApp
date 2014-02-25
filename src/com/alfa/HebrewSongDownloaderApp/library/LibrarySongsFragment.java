package com.alfa.HebrewSongDownloaderApp.library;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.alfa.utils.logic.DataUtils;
import com.alfa.utils.logic.LogUtils;
import com.alfa.utils.logic.SharedPref;
import com.alfa.utils.ui.FragmentUtils;
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

        View view = super.onCreateView(inflater, container, savedInstanceState);
        
        UIUtils.hideSoftKeyboard(view.getContext());

        return view;
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
            if (!rowContainer.editState.equals(LibraryAdapter.LibraryRowContainer.EDIT_STATE.EDITING)) {
                PlayerFragment.playSongAt(position);
            }

        } catch (Exception e) {
            //UIUtils.printError(v.getContext(), "play song from list item:" + e.toString());
        }
    }

    // on long item click event
    public void onItemLongClick(int position, LibraryAdapter.LibraryRowContainer rowContainer) {
        try {
            // just for test
            //UIUtils.PrintToast(rowContainer.rowView.getContext(), "long click test", Toast.LENGTH_SHORT);

            rowContainer.songTitle.setVisibility(View.INVISIBLE);
            rowContainer.renameText.setVisibility(View.VISIBLE);
            rowContainer.renameText.setText(rowContainer.songTitle.getText());
            rowContainer.renameText.setSelectAllOnFocus(true);


            rowContainer.editState = LibraryAdapter.LibraryRowContainer.EDIT_STATE.EDITING;


            // TODO : micha (rename feature)

            // 1) make song title text view invisible
            // 2) make edit text visible
            // 3) set edit text value to be the song title value
            // 4) update list on modification


        } catch (Exception e) {
            //UIUtils.printError(v.getContext(), "play song from list item:" + e.toString());
        }
    }

    public void onRenameSubmit(int position, LibraryAdapter.LibraryRowContainer rowContainer, String oldValue, String newValue) {


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
            libModel.setRenameMode(false);

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


    public boolean onRenameKeyChange(int position, View v, int keyCode, KeyEvent event) {

        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                (keyCode == KeyEvent.KEYCODE_ENTER)) {

            String renamedSong = ((EditText) v).getText().toString();
            String songTitle = libraryListModels.get(position).getSongTitle();
            DataUtils.renameFile(songTitle, renamedSong);
            FragmentUtils.loadLibraryFragment(v.getContext(), DataUtils.listFiles(SharedPref.songDirectory));

            return true;
        }

        return false;
    }

    public void onRenameFocusChange(int position, View v, boolean hasFocus) {

        if (hasFocus) {

            libraryListModels.get(position).setRenameMode(hasFocus);
            updateListModifications();
        }
    }
}