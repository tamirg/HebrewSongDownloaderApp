package com.alfa.HebrewSongDownloaderApp;


import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.alfa.utils.logic.LogUtils;
import com.alfa.utils.logic.SharedPref;
import com.alfa.utils.ui.UIUtils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Micha on 2/13/14.
 */

public class PlayerFragment extends Fragment {
    private static MediaPlayer mPlayer;
    private static MediaPlayer prevPlayer;
    private static ImageButton playButton;
    private static ImageButton stopButton;
    private static ImageButton nextButton;
    private static ImageButton prevButton;
    private static Context context;
    private static List<Uri> songs;
    private static int currentSongPosition;
    private static boolean playerInitialized = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LogUtils.logData("flow_debug", "PlayerFragment__create");

        View view = inflater.inflate(R.layout.player_fragment, container, false);
        setupFragmentView(view);
        return view;
    }

    /**
     * ******************************************************************
     * ******************** Player constructors *************************
     * ******************************************************************
     */

    // empty constructor
    public PlayerFragment() {

    }

    // constructor from a context
    public PlayerFragment(Context context) {
        if (!playerInitialized) {
            LogUtils.logData("flow_debug", "PlayerFragment__initializing");
            initPlayer(context);
            playerInitialized = true;
        }

        LogUtils.logData("flow_debug", "PlayerFragment__skipped initialization");
    }


    /**
     * ******************************************************************
     * ******************** Player view setup ***************************
     * ******************************************************************
     */

    private static void setupFragmentView(View view) {

        LogUtils.logData("flow_debug", "PlayerFragment__setup");

        playButton = (ImageButton) view.findViewById(R.id.playBtn);
        stopButton = (ImageButton) view.findViewById(R.id.stopBtn);
        prevButton = (ImageButton) view.findViewById(R.id.prevBtn);
        nextButton = (ImageButton) view.findViewById(R.id.nextBtn);

        // setup all player button functionality
        setupPlayerButtons();

    }

    private static void setupPlayerButtons() {
        // setup play button listener
        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mPlayer.isPlaying()) {
                    pauseSong();
                } else {
                    startSong();
                }
            }
        });

        // setup stop button listener
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopSong();
            }
        });

        // setup next button listener
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                playNextSong();
            }
        });

        // setup prev button listener
        prevButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                playPreviousSong();
            }
        });
    }


    // initialize player fragment
    public static void initPlayer(Context playerContext) {
        prevPlayer = null;
        mPlayer = null;
        currentSongPosition = 0;
        context = playerContext;
        songs = new LinkedList<Uri>();
    }

    public static void initMediaPlayer(Uri song) {

        LogUtils.logData("flow_debug", "PlayerFragment__initializing media player");

        prevPlayer = mPlayer;
        mPlayer = MediaPlayer.create(context, song);

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer player) {
                playNextSong();
            }
        });
    }

    public static void reloadSongList(List<String> songNames) {

        LogUtils.logData("flow_debug", "PlayerFragment__reloading song list");

        try {
            songs = new LinkedList<Uri>();
            for (String songName : songNames) {
                songs.add(Uri.fromFile(new File(SharedPref.songDirectory + "/" + songName + SharedPref.songExtension)));
            }

            // increment position in case of a song playing so that the automatic next song will work
            if (mPlayer != null && mPlayer.isPlaying()) {
                LogUtils.logData("flow_debug", "PlayerFragment__incrementing song position to " + currentSongPosition + 1);
                currentSongPosition++;
            }

        } catch (Exception e) {
            UIUtils.printError(context, "error on reload" + e.toString());
        }
    }

    /**
     * ******************************************************************
     * ****************** Player functionality **************************
     * ******************************************************************
     */

    public static void playSongAt(int position) {

        if (position < 0 || position > songs.size()) {
            return;
        }

        currentSongPosition = position;
        initMediaPlayer(songs.get(position));
        startSong();
    }

    public static void startSong() {
        try {

            // stop previous song
            if (prevPlayer != null) {
                prevPlayer.stop();
            }

            // unexpected, should not happen
            if (mPlayer == null) {
                return;
            }

            // start current song
            mPlayer.start();

            // handle UI consequences
            playButton.setImageResource(R.drawable.ic_pause);
            playButton.setEnabled(true);
            stopButton.setEnabled(true);
            prevButton.setEnabled(true);
            nextButton.setEnabled(true);

        } catch (Exception e) {
            UIUtils.printError(context, "play_song" + e.toString());
        }
    }

    public static void stopSong() {

        // stop current song
        mPlayer.stop();

        // handle UI consequences
        playButton.setImageResource(R.drawable.ic_play);
        playButton.setEnabled(false);
        stopButton.setEnabled(false);
    }

    public static void pauseSong() {

        // pause current song
        mPlayer.pause();

        // handle UI consequences
        playButton.setImageResource(R.drawable.ic_play);
        stopButton.setEnabled(true);
    }

    public static void playNextSong() {

        // handling edge case of choosing last song first
        if (currentSongPosition + 1 >= songs.size()) {
            currentSongPosition = -1;
        }

        // initialize current media player with current position song
        initMediaPlayer(songs.get(currentSongPosition + 1));

        // update position
        currentSongPosition++;

        // start current song
        startSong();

        // circular functionality
        if (currentSongPosition + 1 >= songs.size()) {
            currentSongPosition = -1;
        }
    }

    public static void playPreviousSong() {

        // handling edge case of choosing first song first
        if (currentSongPosition <= 0) {
            currentSongPosition = songs.size();
        }

        // initialize current media player with current position song
        initMediaPlayer(songs.get(currentSongPosition - 1));

        // update position
        currentSongPosition--;

        // start current song
        startSong();

        // circular functionality
        if (currentSongPosition <= 0) {
            currentSongPosition = songs.size();
        }
    }


}