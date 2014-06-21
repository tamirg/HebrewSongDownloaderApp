package utils.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import utils.logic.SharedPref;

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
     * prints toast on the UI thread
     *
     * @param context
     * @param debugMsg
     */
    public static void printDebug(Context context, String debugMsg) {
        // setup final variables for thread
        final Context fContext = context;
        final Activity fAct = (Activity) context;
        final String fMsg = debugMsg;

        // setup thread for execution
        Thread t = new Thread() {
            public void run() {
                fAct.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (SharedPref.DEBUG_MODE) {
                            Toast.makeText(fContext, fMsg, Toast.LENGTH_SHORT).show();
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

    public static void setQuery(Context context, SearchView searchView, String text, boolean submit) {
        // setup final variables for thread
        final SearchView fSearchView = searchView;
        final String fText = text;
        final boolean fSubmit = submit;
        final Activity fAct = (Activity) context;

        // setup thread for execution
        Thread t = new Thread() {
            public void run() {
                fAct.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fSearchView.setQuery(fText, fSubmit);
                    }
                });
            }
        };


        // execute thread
        t.start();

    }

    public static void editText(Context context, EditText editText, String text) {
        // setup final variables for thread
        final EditText fEditText = editText;
        final String fText = text;
        final Activity fAct = (Activity) context;

        // setup thread for execution
        Thread t = new Thread() {
            public void run() {
                fAct.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fEditText.setText(fText);
                    }
                });
            }
        };


        // execute thread
        t.start();
    }


    /**
     * hides keyboard from activity
     *
     * @param context
     */
    public static void hideSoftKeyboard(Context context) {
        Activity act = (Activity) context;
        if (act.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * show keyboard on activity
     *
     * @param context
     * @param view
     */
    public static void showSoftKeyboard(Context context, View view) {
        Activity act = (Activity) context;
        InputMethodManager inputMethodManager = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    public static void listScroll(final ListView listView, final int position, final boolean smooth) {
        listView.clearFocus();
        listView.post(new Runnable() {
            @Override
            public void run() {
                listView.requestFocusFromTouch();

                if (smooth) {
                    listView.smoothScrollToPosition(position);
                } else {
                    listView.setSelection(position);

                }
                listView.requestFocus();
            }
        });
    }
}
