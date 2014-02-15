package com.alfa.HebrewSongDownloaderApp;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.alfa.utils.LogUtils;
import entities.SongResult;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Micha on 2/13/14.
 */
public class SongListFragment extends ListFragment {

    List<String> songNames;
    List<SongResult> listOfSongResults;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                inflater.getContext(), android.R.layout.simple_list_item_1,
                getSongs());
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

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

    private String[] getSongs() {
        String[] songs = new String[this.songNames.size()];
        int i = 0;

        for (String songName : this.songNames) {
            songs[i++] = songName;
        }

        LogUtils.logData("loading_songs", songs.toString());
        return songs;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //Toast.makeText(getActivity().getBaseContext(), "this is test", Toast.LENGTH_LONG).show();
        SongResult chosenSongResult = this.getSongResultByName((String) l.getItemAtPosition(position));
        chosenSongResult.downloadSongResult(v);
    }

    public SongResult getSongResultByName(String songName) {
        for (SongResult currSongResult : this.listOfSongResults) {
            if (currSongResult.getNameOfSong().equals(songName)) {
                return currSongResult;
            }
        }
        return null;
    }

}