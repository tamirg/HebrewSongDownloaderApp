package com.alfa.HebrewSongDownloaderApp.library;


import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.alfa.HebrewSongDownloaderApp.R;
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
    private static int previousSongPosition = -1;
    private static int currentSongPosition;
    private static boolean playerInitialized = false;

    public static enum PLAYER_STATE {PLAY, PAUSE, FORCED_PAUSE, STOP, NA}

    private static PLAYER_STATE playerState;

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

        // handle player reload in case the PlayerFragment is refreshed
        handlePlayerReload();

        // setup all player button functionality
        setupPlayerButtons();

    }

    private static void handlePlayerReload() {

        if (mPlayer == null) {
            setInitialMode();
        } else if (inPlayingMode()) {
            setPlayingMode();
        }

    }

    private static void setupPlayerButtons() {
        // setup play button listener
        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (inPlayingMode()) {
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
        playerState = PLAYER_STATE.NA;
    }

    public static void initMediaPlayer(int songPosition) {

        LogUtils.logData("flow_debug", "PlayerFragment__initializing media player");

        prevPlayer = mPlayer;

        if (previousSongPosition >= 0) {
            removeIndicator(previousSongPosition);
        }

        setIndicator(songPosition);
        previousSongPosition = songPosition;

        mPlayer = MediaPlayer.create(context, songs.get(songPosition));
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer player) {
                playNextSong();
            }
        });
        playerState = PLAYER_STATE.NA;

    }

    public static void reloadSongList(List<String> songNames) {

        LogUtils.logData("flow_debug", "PlayerFragment__reloading song list");

        try {
            songs = new LinkedList<Uri>();
            for (String songName : songNames) {
                songs.add(Uri.fromFile(new File(SharedPref.songDirectory + "/" + songName + SharedPref.songExtension)));
            }

            // increment position in case of a song playing so that the automatic next song will work
            if (inPlayingMode()) {

                if (LibrarySongsFragment.isAddedOperation()) {

                    // in case of a new song downloaded and added to list
                    LogUtils.logData("flow_debug", "PlayerFragment__incrementing song position to " + (currentSongPosition + 1));
                    currentSongPosition++;
                } else if (LibrarySongsFragment.isDeletedOperation()) {

                    // in case of an existing song was deleted from library

                    /* TODO : uncomment when figuring out if it's necessary
                    currentSongPosition--; */
                }

                // initialize operation playerState
                LibrarySongsFragment.initOperationState();
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
        initMediaPlayer(currentSongPosition);
        startSong();

    }

    public static void startSong() {
        try {

            // stop previous song
            if (prevPlayer != null) {
                prevPlayer.stop();
            }

            // replay song after stop
            if (mPlayer == null) {
                playSongAt(currentSongPosition);
                return;
            }

            // start current song or continue already played one
            mPlayer.start();

            setPlayingMode();

        } catch (Exception e) {
            UIUtils.printError(context, "play_song" + e.toString());
        }
    }

    public static void stopSong() {

        // stop current song
        mPlayer.stop();
        mPlayer = null;
        setStopMode();
    }

    public static void pauseSong() {

        // pause current song
        mPlayer.pause();
        setPauseMode();
    }

    public static void playNextSong() {

        // handling edge case of choosing last song first
        if (currentSongPosition + 1 >= songs.size()) {
            currentSongPosition = -1;
        }

        // initialize current media player with current position song
        initMediaPlayer(currentSongPosition + 1);

        // handle playing UI
        setPlayingMode();

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
        initMediaPlayer(currentSongPosition - 1);

        // handle playing UI
        setPlayingMode();

        // update position
        currentSongPosition--;

        // start current song
        startSong();

        // circular functionality
        if (currentSongPosition <= 0) {
            currentSongPosition = songs.size();
        }
    }

    private static boolean inPlayingMode() {
        return mPlayer != null && mPlayer.isPlaying();
    }

    /**
     * ******************************************************************
     * ******************* Player UI handling ***************************
     * ******************************************************************
     */


    public static void setInitialMode() {
        playButton.setImageResource(R.drawable.ic_play);
        playButton.setEnabled(false);
        stopButton.setEnabled(false);
        prevButton.setEnabled(false);
        nextButton.setEnabled(false);
        playerState = PLAYER_STATE.NA;
    }

    public static void setPlayingMode() {
        playButton.setImageResource(R.drawable.ic_pause);
        playButton.setEnabled(true);
        stopButton.setEnabled(true);
        prevButton.setEnabled(true);
        nextButton.setEnabled(true);
        playerState = PLAYER_STATE.PLAY;
    }

    public static void setPauseMode() {
        playButton.setImageResource(R.drawable.ic_play);
        playButton.setEnabled(true);
        stopButton.setEnabled(true);
        prevButton.setEnabled(true);
        nextButton.setEnabled(true);
        playerState = PLAYER_STATE.PAUSE;
    }

    public static void setStopMode() {
        playButton.setImageResource(R.drawable.ic_play);
        playButton.setEnabled(true);
        stopButton.setEnabled(false);
        prevButton.setEnabled(true);
        nextButton.setEnabled(true);
        playerState = PLAYER_STATE.STOP;

        ImageView playingIndicator = LibraryAdapter.getRowContainer(currentSongPosition).playingIndicator;
        playingIndicator.setVisibility(View.INVISIBLE);

    }

    public static void setIndicator(int position) {

        try {
            LibrarySongsFragment.setPlaying(position, true);
        } catch (Exception e) {
            LogUtils.logError("indicator", e.toString());
        }

    }

    public static void removeIndicator(int position) {
        try {
            LibrarySongsFragment.setPlaying(position, false);
        } catch (Exception e) {
            LogUtils.logError("indicator", e.toString());
        }
    }

    /**
     * ******************************************************************
     * ************** Player revive functionality ***********************
     * ******************************************************************
     */

    public static void PausePlayer() {

        if (playerState == null) {
            return;
        }

        if (isInPlayingMode()) {
            // TODO : think how to do it better
            // pauseSong();
        }
    }

    public static void ResumePlayer() {

        if (playerState == null) {
            return;
        }

        if (isInPauseMode()) {
            // TODO : think how to do it better
            //startSong();
        }
    }


    /**
     * ******************************************************************
     * ************** Player API functions ***********************
     * ******************************************************************
     */

    public static boolean isInPlayingMode() {
        return playerState.equals(PLAYER_STATE.PLAY);
    }

    public static boolean isInPauseMode() {
        return playerState.equals(PLAYER_STATE.PAUSE);
    }

    public static boolean isInStopMode() {
        return playerState.equals(PLAYER_STATE.STOP);
    }

    public static boolean isNotInited() {
        return playerState.equals(PLAYER_STATE.NA);
    }

    public static int getCurrentSongPosition() {
        return currentSongPosition;
    }


}