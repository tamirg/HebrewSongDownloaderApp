package com.alfa.HebrewSongDownloaderApp;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by Micha on 2/13/14.
 */

public class PlayerFragment extends Fragment {
    private MediaPlayer mPlayer;
    private MediaPlayer prevPlayer;
    private ImageButton playButton;
    private ImageButton stopButton;
    private ImageButton forwardButton;
    private ImageButton backwardButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.player_fragment, container, false);
        setupFragmentView(view);
        return view;
    }

    public PlayerFragment() {
        this.prevPlayer = null;
    }

    public void setMediaPlayer(MediaPlayer mPlayer) {
        this.mPlayer = mPlayer;
        playButton.setVisibility(View.VISIBLE);
        stopButton.setVisibility(View.VISIBLE);
        forwardButton.setVisibility(View.VISIBLE);
        backwardButton.setVisibility(View.VISIBLE);
        stopButton.setEnabled(false);
    }

    public void setPreviousPlayer(MediaPlayer prevPlayer) {
        this.prevPlayer = prevPlayer;
    }

    public void stopPreviousSong() {
        if (prevPlayer != null) {
            prevPlayer.stop();
        }
    }

    public void startSong() {
        mPlayer.start();
        playButton.setImageResource(R.drawable.ic_pause);
        playButton.setEnabled(true);
        stopButton.setEnabled(true);
    }

    public void stopSong() {
        mPlayer.stop();
        playButton.setImageResource(R.drawable.ic_play);
        playButton.setEnabled(false);
        stopButton.setEnabled(false);
    }

    public void pauseSong() {
        mPlayer.pause();
        playButton.setImageResource(R.drawable.ic_play);
        stopButton.setEnabled(true);
    }

    private void setupFragmentView(View view) {

        playButton = (ImageButton) view.findViewById(R.id.playBtn);
        stopButton = (ImageButton) view.findViewById(R.id.stopBtn);
        forwardButton = (ImageButton) view.findViewById(R.id.forwardBtn);
        backwardButton = (ImageButton) view.findViewById(R.id.backwardBtn);


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
    }


}