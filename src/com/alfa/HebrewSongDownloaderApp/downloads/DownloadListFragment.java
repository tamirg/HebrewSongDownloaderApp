package com.alfa.HebrewSongDownloaderApp.downloads;


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
public class DownloadListFragment extends ListFragment {

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

    public void setListModels() {

        downloadsModels = new LinkedList<DownloadsModel>();

        DownloadsModel libModel;

        for (String songName : songNames) {

            // initialize lib model
            libModel = new DownloadsModel();

            // set list row song title to the current value
            libModel.setSongTitle(songName);

            // add current model to model list
            downloadsModels.add(libModel);
        }
    }

    public static List<DownloadsModel> getDownloadsListModels() {
        return downloadsModels;
    }

    public static void updateListChange() {
        downloadsAdapter.notifyDataSetChanged();
    }


}