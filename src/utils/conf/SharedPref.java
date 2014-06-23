package utils.conf;

import android.os.Environment;

/**
 * Created by Micha on 2/14/14.
 */
public class SharedPref {

    /**
     * ******************************************************************
     * ******************** Debug constants *****************************
     * ******************************************************************
     */

    public final static boolean DEBUG_MODE = true;

    /**
     * ******************************************************************
     * ********************* Song constants *****************************
     * ******************************************************************
     */

    public final static String songDirectory = Environment.getExternalStorageDirectory().toString() + "/HebrewSongDownloads";
    public final static String songExtension = ".mp3";
    public final static String queryEncoding = "UTF-8";
    public final static String mp3tuberHomeUrl = "http://mp3tuber.org/";
    public final static String userAgent =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36";
    public final static String mp3tuberDownloadUrl = "http://mp3tuber.org/download.php?id=";
    /**
     * ******************************************************************
     * ***************** Tab handling constants *************************
     * ******************************************************************
     */

    public final static boolean destroyTabFragmentOnAction = false;
    public final static int tabCount = 3;

    /**
     * ******************************************************************
     * ********************** API keys **********************************
     * ******************************************************************
     */

    public final static String YouTubeApiKey = "AIzaSyBdKJxxzgIJdHlji5SaPzwwUl_tTMhG36c";

}
