package com.alfa.HebrewSongDownloaderApp;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.alfa.utils.logic.LogUtils;
import entities.SongResult;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Micha on 2/13/14.
 */
public class SongListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    List<String> songNames;
    List<SongResult> listOfSongResults;

    public SongListFragment(LinkedList<String> songNames) {
        this.songNames = songNames;
    }

    public SongListFragment(List<SongResult> songResults) {
        songNames = new LinkedList<String>();
        this.listOfSongResults = songResults;

        if (songResults != null) {

            for (SongResult res : songResults) {
                songNames.add(res.getNameOfSong());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LogUtils.logData("flow_debug", "SongListFragment__creating song list fragment view");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                inflater.getContext(), android.R.layout.simple_list_item_1,
                getSongs());
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private String[] getSongs() {
        String[] songs = new String[this.songNames.size()];
        int i = 0;

        for (String songName : this.songNames) {
            songs[i++] = songName;
        }

        LogUtils.logData("flow_debug", "SongListFragment__loading songs [" + songs.length + "]");
        return songs;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        SongResult chosenSongResult = this.getSongResultByName((String) l.getItemAtPosition(position));
        chosenSongResult.downloadSongResult(v, getFragmentManager());
    }

    public SongResult getSongResultByName(String songName) {
        for (SongResult currSongResult : this.listOfSongResults) {
            if (currSongResult.getNameOfSong().equals(songName)) {
                return currSongResult;
            }
        }
        return null;
    }


    // loader functions


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
}