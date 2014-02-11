package Exceptions;

/**
 * Created by Tamir on 11/02/14.
 */
public class UnRecognizedSongEngine extends Exception {
    public UnRecognizedSongEngine() {
    }

    public UnRecognizedSongEngine(String detailMessage) {
        super(detailMessage);
    }
}
