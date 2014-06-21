package utils.logic;

import android.util.Log;
import utils.conf.SharedPref;

/**
 * Created by Micha on 2/15/14.
 */
public class LogUtils {

	public static void logData(String logContext, String log) {
		String tag = "data:" + logContext;
		if (SharedPref.DEBUG_MODE) {
			Log.d(tag, log);
		}
	}

	public static void logWarning(String logContext, String log) {
		String tag = "warning:" + logContext;
		if (SharedPref.DEBUG_MODE) {
			Log.w(tag, log);
		}
	}

	public static void logError(String logContext, String log) {
		String tag = "error:" + logContext;
		if (SharedPref.DEBUG_MODE) {
			Log.e(tag, log);
		}
	}


}
