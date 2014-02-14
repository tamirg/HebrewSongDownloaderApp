package com.alfa.HebrewSongDownloaderApp;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import entities.SongResult;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Micha on 2/13/14.
 */
public class SongListFragment extends ListFragment {

    List<String> songNames;

    public SongListFragment(LinkedList<String> songNames) {
        this.songNames = songNames;
    }

    public SongListFragment(List<SongResult> songResults) {
        songNames = new LinkedList<String>();
        for (SongResult res : songResults) {
            songNames.add(res.getNameOfSong());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                inflater.getContext(), android.R.layout.simple_list_item_1,
                getSongs());
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private String[] getSongs() {
        //String[] songs = {"song1", "song2", "song3","song4","song5"};
        String[] songs = new String[this.songNames.size()];
        int i = 0;

        for (String songName : this.songNames) {
            songs[i++] = songName;
        }

        Log.w("data:loading_songs", songs.toString());
        return songs;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Toast.makeText(getActivity().getBaseContext(), "this is test", Toast.LENGTH_LONG).show();

    }

}