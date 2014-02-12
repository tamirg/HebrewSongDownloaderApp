package Exceptions;

/**
 * Created by Tamir on 11/02/14.
 */
public class UnRecognizedSongEngineException extends Exception {
    public UnRecognizedSongEngineException() {
    }

    public UnRecognizedSongEngineException(String detailMessage) {
        super(detailMessage);
    }
}
