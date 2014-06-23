package engine;

import entities.SongResult;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Tamir on 21/06/2014.
 */
public class AlternativeEngine {
	private static AlternativeEngine alternativeEngine = null;

	private AlternativeEngine() {
	}

	public static AlternativeEngine getInstance() {
		if (alternativeEngine == null) {
			alternativeEngine = new AlternativeEngine();
		}
		return alternativeEngine;
	}

    public List<SongResult> getSongResults(final String nameOfSong) throws Exception {
        final List<SongResult> songResults = new LinkedList<SongResult>();
        new AlternativeSearchResultThread(nameOfSong, songResults).run();

        //TODO: Think about how to wait for the AlternativeSearchResultThread to finish its work(Maybe AsyncTask?)
        Thread.sleep(15000);

        return songResults;
    }


}
