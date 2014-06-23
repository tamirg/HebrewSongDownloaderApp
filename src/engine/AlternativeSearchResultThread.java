package engine;

import android.util.Log;
import com.google.api.services.youtube.model.SearchResult;
import entities.SongResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import utils.conf.SharedPref;
import utils.logic.LogUtils;
import utils.logic.YouTubeSearch;

import java.io.IOException;
import java.util.List;

/**
 * Created by Tamir on 23/06/2014.
 */
public class AlternativeSearchResultThread implements Runnable {
    private String nameOfSong;
    private List<SongResult> songResults;

    public AlternativeSearchResultThread(String nameOfSong, List<SongResult> songResults) {
        this.nameOfSong = nameOfSong;
        this.songResults = songResults;
    }

    @Override
    public void run() {
        try {
            List<SearchResult> youtubeSearchResults = YouTubeSearch.getSearchResults(nameOfSong);
            for (SearchResult currYoutubeSearchResult : youtubeSearchResults) {
                handleSearchResult(currYoutubeSearchResult);
            }
        } catch (IOException e) {
            Log.e("error:fetching songs", e.toString());
        }
    }

    private void handleSearchResult(SearchResult currYoutubeSearchResult) throws IOException {
        //TODO: DELETE CAUSE NOT SURE IF NEEDED
        // Proxy connection for anonymous request
        System.setProperty("http.proxyHost", "64.186.149.57");
        System.setProperty("http.proxyPort", "8080");

        String resultYoutubeVideoId = currYoutubeSearchResult.getId().getVideoId();
        Document mp3tuberHtml = getHtmlResponseDocument(resultYoutubeVideoId);

        if (hasSongLinkTag(mp3tuberHtml)) {
            String songFinalDownloadURL = mp3tuberHtml.select("p:has(a)").first().child(0).attr("abs:href");
            String songFileName = mp3tuberHtml.select("p[dir]").first().text();

            songResults.add(new SongResult(songFileName, songFinalDownloadURL));
        }
        printResultToLog(currYoutubeSearchResult);
    }

    private Document getHtmlResponseDocument(String resultYoutubeVideoId) throws IOException {
        String mp3tuberUrl = SharedPref.mp3tuberDownloadUrl.concat(resultYoutubeVideoId);
        return Jsoup.connect(mp3tuberUrl)
                .userAgent(SharedPref.userAgent)
                .referrer(SharedPref.mp3tuberHomeUrl)
                .get();
    }

    private boolean hasSongLinkTag(Document mp3tuberHtml) {
        return !mp3tuberHtml.select("p:has(a)").isEmpty();
    }

    private void printResultToLog(SearchResult currYoutubeSearchResult) {
        // Prints to log
        LogUtils.logData(
                "flow_debug",
                "Found result:" +
                        "Title: " + currYoutubeSearchResult.getSnippet().getTitle() +
                        "VideoId: " + currYoutubeSearchResult.getId().getVideoId()
        );
    }
}
