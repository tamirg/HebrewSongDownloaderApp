package ui.downloads;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import utils.logic.LogUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Micha on 2/13/14.
 */
public class DownloadListFragment extends ListFragment {

    /**
     * ******************************************************************
     * ******************* Downloads data members  **********************
     * ******************************************************************
     */

    private List<String> songNames;
    private static DownloadsAdapter downloadsAdapter;
    private static List<DownloadsModel> downloadsModels = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LogUtils.logData("flow_debug", "DownloadListFragment__create");

        setListModels();

        downloadsAdapter = new DownloadsAdapter(this, inflater, getResources());
        setListAdapter(downloadsAdapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public DownloadListFragment() {
        this.songNames = new ArrayList<String>();
    }

    public DownloadListFragment(List<String> songNames) {
        if (songNames == null) {
            this.songNames = new ArrayList<String>();
        }
        this.songNames = songNames;
    }

    // on item click used by downloadsAdapter
    public void onItemClick(int position, DownloadsAdapter.DownloadsRowContainer rowContainer) {
        // TODO : need to do something?
    }

    /**
     * ******************************************************************
     * ****************** Downloads model functions  ********************
     * ******************************************************************
     */

    public void setListModels() {

        downloadsModels = new LinkedList<DownloadsModel>();

        DownloadsModel libModel;

        for (String songName : songNames) {

            // initialize lib model
            libModel = new DownloadsModel();

            // set list row song title to the current value
            libModel.setSongTitle(songName);

            // initialize progress
            libModel.setProgressPercentage(0);

            // add current model to model list
            downloadsModels.add(libModel);
        }
    }

    public static List<DownloadsModel> getDownloadsListModels() {

        return downloadsModels;
    }

    /**
     * ******************************************************************
     * ****************** Downloads adapter functions  ******************
     * ******************************************************************
     */

    // update list modifications when changing one of the list views
    public static void updateListModifications() {
        downloadsAdapter.notifyDataSetChanged();
    }

    /**
     * ******************************************************************
     * ****************** Downloads API functions  **********************
     * ******************************************************************
     */

    public static void publishProgressAt(int position, String... progress) {

        // TODO for tamir
        // put in here what ever you need, just need to be added to the model
        // if it is more convenient for you, you can send here the song name or url or whatever

        if (position < 0 || position >= downloadsModels.size()) {
            return;
        }

        try {
            int progressPercentage = Integer.parseInt(progress[0]);
            downloadsModels.get(position).setProgressPercentage(progressPercentage);

            LogUtils.logData("progress_publish", " publishing " + progressPercentage + " to position " + position);
            updateListModifications();
        } catch (Exception e) {
            LogUtils.logError("progress_publish", e.getStackTrace().toString());

        }
    }


}