package com.alfa.HebrewSongDownloaderApp;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.alfa.utils.LogUtils;
import com.alfa.utils.UIUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Micha on 2/13/14.
 */
public class LibrarySongsFragment extends ListFragment {

    private List<String> songNames;
    private static PlayerFragment player = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                inflater.getContext(), android.R.layout.simple_list_item_1,
                getSongs());
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public PlayerFragment createPlayer(Context context) {
        if (player == null) {
            player = new PlayerFragment(context);
        }
        player.reloadSongList(songNames);
        return player;
    }

    public LibrarySongsFragment(LinkedList<String> songNames) {
        this.songNames = songNames;
    }

    private String[] getSongs() {
        String[] songs = new String[this.songNames.size()];
        int i = 0;

        for (String songName : this.songNames) {
            songs[i++] = songName;
        }

        LogUtils.logData("library_songs", songs.toString());
        return songs;
    }

    public PlayerFragment getPlayer() {
        return player;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        try {
            player.playSongAt(position);
        } catch (Exception e) {
            UIUtils.printError(v.getContext(), "play song from list item:" + e.toString());
        }

    }


}