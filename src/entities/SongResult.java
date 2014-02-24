package entities;


import android.support.v4.app.FragmentManager;
import android.view.View;
import com.alfa.utils.async.AsyncTaskManager;

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

    public void downloadSongResult(View v, FragmentManager fm) {
        AsyncTaskManager.downloadSong(v.getContext(), this.getDownloadURL(), this.getNameOfSong());

    }

    @Override
    public String toString() {
        return nameOfSong;
    }
}
