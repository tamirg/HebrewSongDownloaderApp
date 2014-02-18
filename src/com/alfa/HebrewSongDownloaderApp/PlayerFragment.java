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
    private ImageButton previousButton;
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

                if (currentSongPosition > songs.size()) {
                    nextButton.setEnabled(false);
                    return;
                }

                prevPlayer = mPlayer;
                mPlayer = MediaPlayer.create(context, songs.get(currentSongPosition + 1));
                currentSongPosition++;
                startSong();
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
            if (prevPlayer != null) {
                prevPlayer.stop();
            }

            if (mPlayer == null) {
                return;
            }

            mPlayer.start();
            playButton.setImageResource(R.drawable.ic_pause);
            playButton.setEnabled(true);
            stopButton.setEnabled(true);
            previousButton.setEnabled(true);
            nextButton.setEnabled(true);

            if (currentSongPosition == 0) {
                previousButton.setEnabled(false);
            }

            if (currentSongPosition == songs.size()) {
                nextButton.setEnabled(false);
            }
        } catch (Exception e) {
            UIUtils.printError(this.context, "play start" + e.toString());
        }
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

    public void playNextSong() {

        if (currentSongPosition >= songs.size() - 1) {
            nextButton.setEnabled(false);
            return;
        }

        initMediaPlayer(songs.get(currentSongPosition + 1));
        currentSongPosition++;
        startSong();
    }

    public void playPreviousSong() {

        if (currentSongPosition < 0) {
            previousButton.setEnabled(false);
            return;
        }

        initMediaPlayer(songs.get(currentSongPosition - 1));
        currentSongPosition--;
        startSong();
    }

    private void setupFragmentView(View view) {

        playButton = (ImageButton) view.findViewById(R.id.playBtn);
        stopButton = (ImageButton) view.findViewById(R.id.stopBtn);
        previousButton = (ImageButton) view.findViewById(R.id.previousBtn);
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
        previousButton.setOnClickListener(new View.OnClickListener() {
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
        } catch (Exception e) {
            UIUtils.printError(context, "error on reload" + e.toString());
        }
    }


}