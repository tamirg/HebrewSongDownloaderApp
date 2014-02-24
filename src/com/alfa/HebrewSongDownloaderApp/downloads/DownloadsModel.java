package com.alfa.HebrewSongDownloaderApp.downloads;

public class DownloadsModel {

    private String songTitle;
    private int progressPercentage;

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongTitle() {

        return this.songTitle;
    }

    public int getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(int progressPercentage) {
        this.progressPercentage = progressPercentage;
    }
}