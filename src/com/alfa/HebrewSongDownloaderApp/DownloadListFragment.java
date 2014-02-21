package com.alfa.HebrewSongDownloaderApp;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.alfa.utils.ui.UIUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Micha on 2/13/14.
 */
public class DownloadListFragment extends ListFragment {

    private static List<String> downloadedSongs;

    public DownloadListFragment() {
        this.downloadedSongs = new LinkedList<String>();
        this.downloadedSongs.add("downloading 1..");
        this.downloadedSongs.add("downloading 2..");
        this.downloadedSongs.add("downloading 3..");
    }

    public static List<String> addDownloadedSong(String song) {
        downloadedSongs.add(song);
        return downloadedSongs;
    }

    public static List<String> removeDownloadedSong(String song) {
        downloadedSongs.remove(song);
        return downloadedSongs;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                inflater.getContext(), android.R.layout.simple_list_item_1,
                getDownloadedSongs());
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private String[] getDownloadedSongs() {

        String[] songs = new String[downloadedSongs.size()];
        int i = 0;

        for (String songName : downloadedSongs) {
            songs[i++] = songName;
        }

        Log.w("data:loading_songs", songs.toString());
        return songs;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        UIUtils.printDebug(v.getContext(), "downloading song..");
    }


}