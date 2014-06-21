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
	public final static String unidownUrlQuery = "http://www.unidown.com/search.php?q=";

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
