package com.alfa.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Micha on 2/7/14.
 */
public class UIUtils {

    /**
     * prints toast on the UI thread
     *
     * @param context
     * @param msg
     * @param toast_length
     */
    public static void PrintToast(Context context, String msg, int toast_length) {
        // setup final variables for thread
        final Context fContext = context;
        final Activity fAct = (Activity) context;
        final String fMsg = msg;
        final int fToastLength = toast_length;

        // setup thread for execution
        Thread t = new Thread() {
            public void run() {
                fAct.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(fContext, fMsg, fToastLength).show();
                    }
                });
            }
        };

        // execute thread
        t.start();
    }

    /**
     * shows progress bar on the UI thread
     *
     * @param PB
     * @param context
     */
    public static void showProgressBar(ProgressBar PB, Context context) {
        // setup final variables for thread
        final ProgressBar fPB = PB;
        final Activity fAct = (Activity) context;

        // setup thread for execution
        Thread t = new Thread() {
            public void run() {
                fAct.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fPB.setVisibility(View.VISIBLE);
                    }
                });
            }
        };

        // execute thread
        t.start();
    }

    /**
     * hides progress bar on the UI thread
     *
     * @param PB
     * @param context
     */
    public static void hideProgressBar(ProgressBar PB, Context context) {
        // setup final variables for thread
        final ProgressBar fPB = PB;
        final Activity fAct = (Activity) context;

        // setup thread for execution
        Thread t = new Thread() {
            public void run() {
                fAct.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fPB.setVisibility(View.INVISIBLE);
                    }
                });
            }
        };

        // execute thread
        t.start();
    }

    /**
     * @param tv
     * @param context
     */
    public static void showTextView(TextView tv, Context context) {
        // setup final variables for thread
        final TextView fTv = tv;
        final Activity fAct = (Activity) context;

        // setup thread for execution
        Thread t = new Thread() {
            public void run() {
                fAct.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fTv.setVisibility(View.VISIBLE);
                    }
                });
            }
        };

        // execute thread
        t.start();
    }

    /**
     * @param tv
     * @param context
     */
    public static void hideTextView(TextView tv, Context context) {
        // setup final variables for thread
        final TextView fTv = tv;
        final Activity fAct = (Activity) context;

        // setup thread for execution
        Thread t = new Thread() {
            public void run() {
                fAct.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fTv.setVisibility(View.GONE);
                    }
                });
            }
        };

        // execute thread
        t.start();
    }

}
