package com.alfa.HebrewSongDownloaderApp;

import android.app.ListFragment;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.alfa.utils.LogUtils;
import com.alfa.utils.SharedPref;
import com.alfa.utils.UIUtils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Micha on 2/13/14.
 */
public class LibrarySongsFragment extends ListFragment {

    List<String> songNames;
    PlayerFragment player;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                inflater.getContext(), android.R.layout.simple_list_item_1,
                getSongs());
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public LibrarySongsFragment(PlayerFragment player, LinkedList<String> songNames) {
        this.songNames = songNames;
        this.player = player;
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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Context c = v.getContext();

        try {

            String filePath = SharedPref.songDirectory + "/" + songNames.get(position) + ".mp3";
            File file = new File(filePath);

            if (!file.exists()) {
                UIUtils.printError(c, "file at: " + filePath + " does not exist!");
                return;
            }

            MediaPlayer mPlayer;

            player.stopPreviousSong();
            mPlayer = MediaPlayer.create(c, Uri.fromFile(file));
            player.setMediaPlayer(mPlayer);
            player.startSong();
            player.setPreviousPlayer(mPlayer);

        } catch (Exception e) {
            UIUtils.printError(c, e.toString());
        }


    }


}