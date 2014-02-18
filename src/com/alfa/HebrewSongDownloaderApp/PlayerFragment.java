package com.alfa.HebrewSongDownloaderApp;

import android.app.Fragment;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.alfa.utils.SharedPref;
import com.alfa.utils.UIUtils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Micha on 2/13/14.
 */

public class PlayerFragment extends Fragment {
    private MediaPlayer mPlayer;
    private MediaPlayer prevPlayer;
    private ImageButton playButton;
    private ImageButton stopButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private Context context;
    List<Uri> songs;
    private int currentSongPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.player_fragment, container, false);
        setupFragmentView(view);
        return view;
    }

    public PlayerFragment(Context context) {
        this.prevPlayer = null;
        this.mPlayer = null;
        this.currentSongPosition = 0;
        this.context = context;
        this.songs = new LinkedList<Uri>();
    }

    public void initMediaPlayer(Uri song) {
        prevPlayer = mPlayer;
        mPlayer = MediaPlayer.create(context, song);

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer player) {
                playNextSong();
            }
        });
    }

    public void playSongAt(int position) {

        if (position < 0 || position > songs.size()) {
            return;
        }

        currentSongPosition = position;
        initMediaPlayer(songs.get(position));
        startSong();
    }

    public void startSong() {

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
            UIUtils.printError(this.context, "play start" + e.toString());
        }
    }

    public void stopSong() {

        // stop current song
        mPlayer.stop();

        // handle UI consequences
        playButton.setImageResource(R.drawable.ic_play);
        playButton.setEnabled(false);
        stopButton.setEnabled(false);
    }

    public void pauseSong() {

        // pause current song
        mPlayer.pause();

        // handle UI consequences
        playButton.setImageResource(R.drawable.ic_play);
        stopButton.setEnabled(true);
    }

    public void playNextSong() {

        // handling edge case of choosing last song first
        if (currentSongPosition + 1 >= songs.size()) {
            currentSongPosition = -1;
            UIUtils.printDebug(context, (currentSongPosition + 1) + "");
        } else {
            UIUtils.printDebug(context, (currentSongPosition + 1) + "");
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

    public void playPreviousSong() {

        UIUtils.printDebug(context, (currentSongPosition - 1) + "");

        // handling edge case of choosing first song first
        if (currentSongPosition <= 0) {
            currentSongPosition = songs.size();
            UIUtils.printDebug(context, (currentSongPosition - 1) + "");
        } else {
            UIUtils.printDebug(context, (currentSongPosition - 1) + "");
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

    private void setupFragmentView(View view) {

        playButton = (ImageButton) view.findViewById(R.id.playBtn);
        stopButton = (ImageButton) view.findViewById(R.id.stopBtn);
        prevButton = (ImageButton) view.findViewById(R.id.prevBtn);
        nextButton = (ImageButton) view.findViewById(R.id.nextBtn);

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


    public void reloadSongList(List<String> songNames) {

        try {
            songs = new LinkedList<Uri>();
            for (String songName : songNames) {
                songs.add(Uri.fromFile(new File(SharedPref.songDirectory + "/" + songName + ".mp3")));
            }

            // increment position in case of a song playing so that the automatic next song will work
            if (mPlayer != null && mPlayer.isPlaying()) {
                this.currentSongPosition++;
            }

        } catch (Exception e) {
            UIUtils.printError(context, "error on reload" + e.toString());
        }
    }
}