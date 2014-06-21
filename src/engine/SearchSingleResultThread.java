package engine;

import Exceptions.UnRecognizedSongEngineException;
import android.util.Log;
import entities.SongResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.logic.ConnectionUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by Tamir on 15/02/14.
 */
public class SearchSingleResultThread implements Runnable {
    private Element singleDownloadButton;
    private List<SongResult> songResults;
    private FetchSongs fetchSongsEngine = null;

    public SearchSingleResultThread() {
    }

    ;

    public SearchSingleResultThread(Element singleDownloadButton, List<SongResult> songResults) {
        this.singleDownloadButton = singleDownloadButton;
        this.songResults = songResults;
        this.fetchSongsEngine = FetchSongs.getInstance();
    }

    ;

    @Override
    public void run() {
        try {
            String songDownloadURL = singleDownloadButton.child(0).attr("abs:href");

            Document songDownloadParsedHTML = ConnectionUtils.songDownloadParsedHTML(songDownloadURL);
            Elements elementsWithClassIframe =
                    songDownloadParsedHTML.getElementsByClass("iframecontent").select("iframe");
            String iframeURL = elementsWithClassIframe.attr("abs:src");

            Document iframeSongDownloadDocument = ConnectionUtils.connectToURL(iframeURL);
            String songDownloadIframe = iframeSongDownloadDocument.getElementsByTag("iframe").attr("abs:src");

            // Gets the iframe for the download, can be more than one website
            Document iframeParsedHTML = Jsoup.connect(songDownloadIframe).get();

            SongResult newSongResult = this.fetchSongsEngine.fetchSingleSongResult(songDownloadIframe, iframeParsedHTML);
            if (!newSongResult.getDownloadURL().equals("") &&
                    !newSongResult.getNameOfSong().equals("")) {
                songResults.add(newSongResult);
            }
        } catch (IOException e) {
            //e.printStackTrace();
            Log.e("error:fetching songs", e.toString());
        } catch (UnRecognizedSongEngineException e) {
            //e.printStackTrace();
            Log.e("error:fetching songs", e.toString());
        } catch (Exception e) {
            //e.printStackTrace();
            Log.e("error:fetching songs", e.toString());
        }
    }
}
