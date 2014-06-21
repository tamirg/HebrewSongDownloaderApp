package utils.ui;

import entities.SongResult;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by Micha on 2/7/14.
 */
public class CachingUtils {
    private Queue<Map<String, List<SongResult>>> lruSongCache;
    private int threshold;

    public CachingUtils() {
        lruSongCache = new PriorityQueue<Map<String, List<SongResult>>>();
        threshold = 10;
    }

    public void cache(String query, List<SongResult> songResults) {

    }
}
