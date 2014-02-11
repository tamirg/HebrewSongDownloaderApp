package entities;

/**
 * Created by Tamir on 11/02/14.
 */
public class SongResult {
    private String nameOfSong;
    private String downloadURL;

    public SongResult() {
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
}
