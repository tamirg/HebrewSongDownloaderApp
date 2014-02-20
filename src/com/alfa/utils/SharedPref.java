package com.alfa.utils;

import android.os.Environment;

/**
 * Created by Micha on 2/14/14.
 */
public class SharedPref {

    public final static boolean DEBUG_MODE = true;
    public final static String songDirectory = Environment.getExternalStorageDirectory().toString() + "/HebrewSongDownloads";
    public final static int tabCount = 3;
}
