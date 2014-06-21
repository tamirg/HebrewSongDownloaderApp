package engine;

import Exceptions.NoSongFoundException;
import android.util.Log;
import entities.SongResult;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.conf.SharedPref;
import utils.logic.ConnectionUtils;

import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tamir on 21/06/2014.
 */
public class AlternativeEngine {
	public final String UNIDOWN_URL_QUERY = SharedPref.unidownUrlQuery;
	public final String QUERY_ENCODING = SharedPref.queryEncoding;
	public final String SONG_FILE_MP3_SUFFIX = SharedPref.songExtension;
	private static AlternativeEngine alternativeEngine = null;

	private AlternativeEngine() {
	}

	public static AlternativeEngine getInstance() {
		if (alternativeEngine == null) {
			alternativeEngine = new AlternativeEngine();
		}
		return alternativeEngine;
	}

	public List<SongResult> getSongResults(String nameOfSong) throws Exception {
		final List<SongResult> songResults = new LinkedList<SongResult>();
		nameOfSong = URLEncoder.encode(nameOfSong, QUERY_ENCODING);
		Document pageDoc = ConnectionUtils.connectToURL(UNIDOWN_URL_QUERY + nameOfSong);

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

	public SongResult fetchSingleSongResult(String songDownloadIframe,
	                                        Document iframeParsedHTML) {
//        if (!iframeParsedHTML.select("p:has(a)").isEmpty()) {
//            songFinalDownloadURL = iframeParsedHTML.select("p:has(a)").first().child(0).attr("abs:href");
//            songFileName = iframeParsedHTML.select("p[dir]").first().text();
//        }
		return null;
	}
}
