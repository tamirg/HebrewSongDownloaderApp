package engine;

import Exceptions.NoSongFoundException;
import Exceptions.UnRecognizedSongEngineException;
import android.util.Log;
import com.alfa.utils.logic.SharedPref;
import entities.SongResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * Created by Tamir on 24/01/14.
 */
public class FetchSongs {
    public final String UNIDOWN_URL_QUERY = SharedPref.unidownUrlQuery;
    public final String QUERY_ENCODING = SharedPref.queryEncoding;
    public final String SONG_FILE_MP3_SUFFIX = SharedPref.songExtension;
    private static FetchSongs fetchSongsEngine = null;

    protected FetchSongs() {
    }

    public static FetchSongs getInstance() {
        if (fetchSongsEngine == null) {
            fetchSongsEngine = new FetchSongs();
        }
        return fetchSongsEngine;
    }

    public List<SongResult> getSongResults(String nameOfSong) throws Exception {
        final List<SongResult> songResults = new LinkedList<SongResult>();
        nameOfSong = URLEncoder.encode(nameOfSong, QUERY_ENCODING);
        Document pageDoc = Jsoup.connect(UNIDOWN_URL_QUERY + nameOfSong).get();
        Elements downloadButtonElements = pageDoc.getElementsByClass("download_button");
        if (downloadButtonElements.isEmpty()) {
            throw new NoSongFoundException();
        } else {

            ExecutorService threadPool = Executors.newFixedThreadPool(downloadButtonElements.size());
            for (Element singleDownloadButton : downloadButtonElements) {
                Runnable r = new SearchSingleResultThread(singleDownloadButton, songResults);
                threadPool.execute(r);
            }
            threadPool.shutdown();
            try {
                threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                Log.e("error:", e.toString());
            }

            return songResults;
        }
    }

    /**
     * @param songDownloadIframe - the website's URL which we need to extract the song result from.
     * @param iframeParsedHTML   - the website's DOM HTML elements tree which contains all page's data.
     * @return SongResult type object which contains
     * 1.song's name
     * 2.song's download URL
     */
    public SongResult fetchSingleSongResult(String songDownloadIframe,
                                            Document iframeParsedHTML) throws UnRecognizedSongEngineException {
        String songFinalDownloadURL = "";
        String songFileName = "";

        // Works for freeTUBEconvertor site!
        if (songDownloadIframe.contains("freetubeconvertor")) {
            if (iframeParsedHTML.getElementById("downloadbox") != null) {
                songFinalDownloadURL = iframeParsedHTML.getElementById("downloadbox").child(1).child(0).attr("abs:href");
                songFileName = iframeParsedHTML.getElementById("downloadbox").child(0).text();
            }
        }
        // Works for videotomp3now site!
        else if (songDownloadIframe.contains("videotomp3now")) {
            if (iframeParsedHTML.getElementById("downloadbutton") != null) {
                songFinalDownloadURL = iframeParsedHTML.getElementById("downloadbutton").child(0).attr("abs:href");
                songFileName = iframeParsedHTML.getElementById("url").children().select("h1").text();
            }
        }
        // Works for mp3tuber site!
        else if (songDownloadIframe.contains("mp3tuber")) {
            if (!iframeParsedHTML.select("p:has(a)").isEmpty()) {
                songFinalDownloadURL = iframeParsedHTML.select("p:has(a)").first().child(0).attr("abs:href");
                songFileName = iframeParsedHTML.select("p[dir]").first().text();
            }
        }
        // Works for hqconvertor site!
        else if (songDownloadIframe.contains("hqconvertor")) {
            if (iframeParsedHTML.getElementById("button_download") != null) {
                songFinalDownloadURL = iframeParsedHTML.getElementById("button_download").child(0).attr("abs:href");
                songFileName = iframeParsedHTML.getElementsByClass("songname").first().text();
            }
        } else {
            /** TODO: not throw this exception for the
             *   mainActivity to catch - catch it in the for loop and write to log (without
             *   stopping the loop
             */
            throw new UnRecognizedSongEngineException(songDownloadIframe + " - לא מוכר");
        }

        return (new SongResult(songFileName, songFinalDownloadURL));
    }

}