package com.alfa.HebrewSongDownloaderApp.library;

public class LibraryListModel {

    private String songTitle;
    private boolean playing;

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongTitle() {

        return this.songTitle;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }
}