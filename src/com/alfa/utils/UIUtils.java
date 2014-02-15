package com.alfa.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
     * prints error toast on the UI thread or a dialog in case of debug mode
     *
     * @param context
     * @param errMsg
     */
    public static void printError(Context context, String errMsg) {
        // setup final variables for thread
        final Context fContext = context;
        final Activity fAct = (Activity) context;
        final String fErrMsg = "Error: " + errMsg;
        final int fToastLength = Toast.LENGTH_LONG;
        final boolean fDebugMode = SharedPref.DEBUG_MODE;

        // setup alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(fContext);
        builder.setMessage(fErrMsg);
        builder.setCancelable(true);
        builder.setPositiveButton("dismiss", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog fDialog = builder.create();

        // setup thread for execution
        Thread t = new Thread() {
            public void run() {
                fAct.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!fDebugMode) {
                            Toast.makeText(fContext, fErrMsg, fToastLength).show();
                        } else {
                            fDialog.show();
                        }
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
                        fPB.setProgress(0);
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
    public static void showTextView(TextView tv, String text, Context context) {
        // setup final variables for thread
        final TextView fTv = tv;
        final String fText = text;
        final Activity fAct = (Activity) context;

        // setup thread for execution
        Thread t = new Thread() {
            public void run() {
                fAct.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fTv.setText(fText);
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
                        fTv.setVisibility(View.INVISIBLE);
                    }
                });
            }
        };

        // execute thread
        t.start();
    }

}
