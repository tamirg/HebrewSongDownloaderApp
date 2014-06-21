package ui.library;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import utils.logic.DataUtils;
import utils.logic.LogUtils;
import utils.logic.SharedPref;
import utils.ui.FragmentUtils;
import utils.ui.UIUtils;

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
    private static int positionFocus;

    private static OPER_EVENT operEvent = OPER_EVENT.NA;

    public static enum OPER_EVENT {ADD, DELETE, NA;}


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

        //UIUtils.hideSoftKeyboard(view.getContext());

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
    public void onItemLongClick(int position) {

        handleEditMode(position, null);
    }

    public void onEditClick(int position) {
        handleEditMode(position, null);
    }


    public void handleEditMode(int position, Boolean forceEditMode) {

        LibraryListModel libModel = libraryListModels.get(position);

        if (forceEditMode == null) {
            // toggle edit mode
            if (libModel.isInEditMode()) {
                switchEditMode(libModel, false);

            } else if (!LibraryAdapter.hasSongInEdit() && !libModel.isPlaying()) {
                // check that no other song is in edit before enabling edit mode
                switchEditMode(libModel, true);
            }

        } else {
            switchEditMode(libModel, forceEditMode);
        }
    }


    public void switchEditMode(LibraryListModel libModel, boolean editMode) {

        // set global adapter song in edit flag
        LibraryAdapter.setHasSongInEdit(editMode);

        // update model and list
        libModel.setEditMode(editMode);
        updateListModifications();
    }

    /**
     * ******************************************************************
     * ******************* Library Model functions  *********************
     * ******************************************************************
     */

    public void setListModels() {

        LogUtils.logData("flow_debug", "setListModels__setting list models");

        // init model list
        libraryListModels = new LinkedList<LibraryListModel>();
        LibraryListModel libModel;
        int currentModelPosition = 0;

        for (String songName : songNames) {

            // initialize lib model
            libModel = new LibraryListModel();

            // set list row song title to the current value
            libModel.setSongTitle(songName);

            if (PlayerFragment.isNotInited()) {

                // set playing mode to be false
                libModel.setPlaying(false);

            } else {

                int currSongPos = PlayerFragment.getCurrentSongPosition();

                // preserve playing mode to item after refresh
                if (currentModelPosition == currSongPos) {
                    libModel.setPlaying(true);

                    if (PlayerFragment.isInPlayingMode()) {
                        setPositionFocus(currSongPos);
                    }
                }
            }

            // set edit mode false
            libModel.setEditMode(false);

            // increment model position
            currentModelPosition++;

            // add current model to model list
            libraryListModels.add(libModel);
        }
    }

    public static List<LibraryListModel> getLibraryListModels() {

        if (libraryListModels == null) {
            libraryListModels = new LinkedList<LibraryListModel>();
        }

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

    public void deleteSong(int position, View v, LibraryAdapter.LibraryRowContainer rowContainer) {

        LogUtils.logData("flow_debug", "LibrarySongFragment__deleteSong deleting" + rowContainer.songTitle.getText().toString() + " ... ");

        setOperEvent(OPER_EVENT.DELETE);
        DataUtils.deleteFile(rowContainer.songTitle.getText().toString());

        // set position to return to after reload
        setPositionFocus(position);

        // reload fragment
        FragmentUtils.loadLibraryFragment(v.getContext());
    }

    public boolean onRenameKeyChange(int position, View v, int keyCode, KeyEvent event) {


        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                (keyCode == KeyEvent.KEYCODE_ENTER)) {

            submitSongEdit(v, position);

            return true;
        }

        return false;
    }

    private void submitSongEdit(View v, int position) {

        // rename song
        String newSongTitle = ((EditText) v).getText().toString();
        String songTitle = libraryListModels.get(position).getSongTitle();
        DataUtils.renameFile(songTitle, newSongTitle);
        LogUtils.logData("flow_debug", "onRenameKeyChange__renamed song to " + newSongTitle);


        // set position to return to after reload
        setPositionFocus(position);

        // reload fragment
        FragmentUtils.loadLibraryFragment(v.getContext(), DataUtils.listFiles(SharedPref.songDirectory, SharedPref.songExtension));
        handleEditMode(position, false);

        LogUtils.logData("flow_debug", "onRenameKeyChange__submited!");

    }

    public void onRenameFocusChange(int position, View v, boolean hasFocus) {

        if (hasFocus) {

            libraryListModels.get(position).setEditMode(hasFocus);
            updateListModifications();
        }
    }

    private static OPER_EVENT getOperEvent() {
        return operEvent;
    }


    /**
     * ******************************************************************
     * ************* Library event operation functions  *****************
     * ******************************************************************
     */

    private void ScrollTo(final int position) {

        final ListView listView = LibraryAdapter.getParentListView();

        if (listView != null && position > 0) {
            LogUtils.logData("flow_debug", "LibrarySongFragment__scrolling to " + position);

            UIUtils.listScroll(listView, position, true);
        }
    }

    public void ScrollToPositionFocus() {
        ScrollTo(getPositionFocus());
    }

    public static void setOperEvent(OPER_EVENT operEvent) {
        LibrarySongsFragment.operEvent = operEvent;
    }

    public static boolean isDeletedOperation() {
        return operEvent.equals(OPER_EVENT.DELETE);
    }

    public static boolean isAddedOperation() {
        return operEvent.equals(OPER_EVENT.ADD);
    }

    public static void initOperationState() {
        setOperEvent(OPER_EVENT.NA);
    }

    public static int getPositionFocus() {

        int returnedPositionFocus = -1;

        if (positionFocus > 0) {
            returnedPositionFocus = positionFocus;
        }

        initPositionFocus();

        return returnedPositionFocus;
    }

    public static void setPositionFocus(int positionFocus) {
        LibrarySongsFragment.positionFocus = positionFocus;
    }

    private static void initPositionFocus() {
        positionFocus = -1;
    }

}