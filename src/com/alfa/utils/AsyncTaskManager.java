package com.alfa.utils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.alfa.HebrewSongDownloaderApp.R;
import com.alfa.HebrewSongDownloaderApp.SongListFragment;
import com.alfa.utils.logic.LogUtils;
import com.alfa.utils.logic.URLUtils;
import com.alfa.utils.ui.FragmentUtils;
import com.alfa.utils.ui.UIUtils;
import engine.FetchSongs;
import entities.SongResult;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Micha on 2/7/14.
 */

/**
 * AsyncTaskManager handles all asynchronous tasks in the application context
 */
public class AsyncTaskManager {


    /**
     * handles song result fetch
     *
     * @param context
     * @param query
     * @param loadingText
     * @param actionBarProgressBar
     * @param fm
     */
    public static void getSongResult(Context context, String query, TextView loadingText,
                                     MenuItem actionBarProgressBar, FragmentManager fm) {

        new SearchSongResults(context, query, loadingText, actionBarProgressBar, fm).execute();
    }

    public static void downloadSong(Context context, String downloadUrl, String songName, FragmentManager fm) {
        new DownloadSongResult(context, fm).execute(downloadUrl, songName);
    }

    // fetch song result task
    private static class SearchSongResults extends AsyncTask<String, String, String> {

        private String query;
        private MenuItem actionBarProgressBar;
        private TextView loadingText;
        private FragmentManager fm;
        private String errorContent = "";
        private List<SongResult> songResults;

        // TODO micha : think of better way of passing the song list to postExecute (pass to context?)

        public SearchSongResults(Context context, String query, TextView loadingText,
                                 MenuItem actionBarProgressBar, FragmentManager fm) {
            this.query = query;
            this.loadingText = loadingText;
            this.actionBarProgressBar = actionBarProgressBar;
            this.fm = fm;
            this.songResults = new LinkedList<SongResult>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // show loading information

            actionBarProgressBar.setActionView(R.layout.action_progressbar);
            actionBarProgressBar.expandActionView();

            // load empty list fragment
            LoadListFragment(null);
        }

        // fetch song results
        @Override
        protected String doInBackground(String... params) {
            FetchSongs fetchSongsInstance = FetchSongs.getInstance();

            try {

                songResults = fetchSongsInstance.getSongResults(this.query);
                LogUtils.logData("fetch_songs", songResults.toString());

            } catch (Exception exc) {
                errorContent = "אירעה שגיאה";
            }

            return errorContent;
        }

        // dismiss progress bar after completing task
        @Override
        protected void onPostExecute(String result) {

            String fixedLoadingInfo = "שירים שנמצאו עבור ";
            loadingText.setText(fixedLoadingInfo + this.query + " : ");

            actionBarProgressBar.collapseActionView();
            actionBarProgressBar.setActionView(null);

            // TODO : should it be here?
            LoadListFragment(this.songResults);
        }

        /**
         * TODO : should be here?
         */
        private void LoadListFragment(List<SongResult> songResults) {

            if (fm != null) {

                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.list_container, new SongListFragment(songResults));
                ft.commit();
            }
        }
    }


    /**
     * Background Async Task to download file
     */
    private static class DownloadSongResult extends AsyncTask<String, String, String> {
        String SONGS_DIRECTORY = Environment.getExternalStorageDirectory().toString();
        private Context context;
        private String errorContent = "";
        private ProgressBar songDownloadProgressBar;
        private TextView percentageText;
        private TextView headlineViewText;
        private FragmentManager fm;

        public DownloadSongResult(Context appContext, FragmentManager fm) {
            this.context = appContext;
            this.fm = fm;
            SONGS_DIRECTORY += "/HebrewSongDownloads";

            // setup view widgets
            songDownloadProgressBar = (ProgressBar) ((Activity) this.context).findViewById(R.id.progressBar);
            percentageText = (TextView) ((Activity) this.context).findViewById(R.id.percentageProgress);
            headlineViewText = (TextView) ((Activity) this.context).findViewById(R.id.loadingText);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            UIUtils.PrintToast(context, "ההורדה החלה", Toast.LENGTH_LONG);
            UIUtils.showProgressBar(songDownloadProgressBar, this.context);

        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... params) {
            FetchSongs fetchSongsInstance = FetchSongs.getInstance();
            String songFinalDownloadURL = params[0];
            String songFileName = params[1];

            try {
                URI songFinalDownloadURI = URLUtils.parseUrl(songFinalDownloadURL);
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpRequest = new HttpPost(songFinalDownloadURI.toString());
                HttpResponse responseSongFile = httpClient.execute(httpRequest);
                int songFileLengthInBytes;

                HttpEntity entity = responseSongFile.getEntity();
                songFileLengthInBytes = (int) entity.getContentLength();
                BufferedInputStream bfInputStream = new BufferedInputStream(entity.getContent());
                String filePath = SONGS_DIRECTORY + File.separator + songFileName +
                        fetchSongsInstance.SONG_FILE_MP3_SUFFIX;


                BufferedOutputStream bfOutputStream = new BufferedOutputStream(new FileOutputStream(filePath));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[8192];
                long totalBytesDownloaded = 0;
                int bytesRead;

                while ((bytesRead = bfInputStream.read(buffer)) > 0) {
                    totalBytesDownloaded += bytesRead;

                    // publishing the progress....
                    publishProgress("" + (int) ((totalBytesDownloaded * 100) / songFileLengthInBytes));

                    // Writing the song to disk
                    baos.write(buffer, 0, bytesRead);
                }
                bfOutputStream.write(baos.toByteArray());
                bfInputStream.close();
                bfOutputStream.close();
            } catch (Exception e) {
                errorContent = e.getMessage();
                System.out.println(e.getMessage());
            }
            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            int progressPercentage = Integer.parseInt(progress[0]);

            songDownloadProgressBar.setProgress(progressPercentage);

            // update percentage text with current percentage of the downloaded file
            UIUtils.showTextView(percentageText, progressPercentage + "\\" + songDownloadProgressBar.getMax(), this.context);

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                // hide loading information when down downloading
                UIUtils.hideProgressBar(songDownloadProgressBar, this.context);
                UIUtils.hideTextView(percentageText, this.context);
                UIUtils.showTextView(headlineViewText, this.context);

                if (errorContent.equals("")) {
                    UIUtils.PrintToast(context, "השיר ירד בהצלחה", Toast.LENGTH_LONG);

                    //LogUtils.logData("flow_debug", "DownloadSongResult__song was downloaded successfully");
                    //LogUtils.logData("flow_debug", "DownloadSongResult__reloading library fragment..");
                    FragmentUtils.loadLibraryFragment(fm, context);
                } else {
                    UIUtils.printError(context, errorContent);
                }
            } catch (Exception e) {
                UIUtils.printError(context, e.toString());
            }
        }
    }
}



