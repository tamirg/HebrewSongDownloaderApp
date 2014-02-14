package entities;

import android.view.View;
import com.alfa.utils.DownloadSongResultAsyncTask;

/**
 * Created by Tamir on 11/02/14.
 */
public class SongResult {
    private String nameOfSong;
    private String downloadURL;

    public SongResult() {
    }

    public SongResult(SongResult otherSongResult) {
        this.nameOfSong = otherSongResult.getNameOfSong();
        this.downloadURL = otherSongResult.getDownloadURL();
    }

    public SongResult(String nameOfSong, String downloadURL) {
        this.nameOfSong = nameOfSong;
        this.downloadURL = downloadURL;
    }

    public String getNameOfSong() {
        return nameOfSong;
    }

    public void setNameOfSong(String nameOfSong) {
        this.nameOfSong = nameOfSong;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public void downloadSongResult(View v) {
        new DownloadSongResultAsyncTask(v.getContext()).execute(this.getDownloadURL(), this.getNameOfSong());
    }


    @Override
    public String toString() {
        return nameOfSong;
    }
}
