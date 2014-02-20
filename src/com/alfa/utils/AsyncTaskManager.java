package com.alfa.utils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.alfa.HebrewSongDownloaderApp.LibrarySongsFragment;
import com.alfa.HebrewSongDownloaderApp.PlayerFragment;
import com.alfa.HebrewSongDownloaderApp.R;
import com.alfa.HebrewSongDownloaderApp.SongListFragment;
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
     * handles song result fetching
     *
     * @param context
     * @param songQuerySearch
     * @param loadingText
     * @param loadingWheel
     * @param fm
     */
    public static void getSongResult(Context context, EditText songQuerySearch, TextView loadingText, ProgressBar loadingWheel, FragmentManager fm) {

        new SearchSongResults(context, songQuerySearch, loadingText, loadingWheel, fm).execute();
    }

    public static void downloadSong(Context context, String downloadUrl, String songName, FragmentManager fm) {
        new DownloadSongResult(context, fm).execute(downloadUrl, songName);
    }

    // TOOD : remove!! just for test
    public static void syncData(MenuItem refreshMenuItem) {
        new SyncData(refreshMenuItem).execute();
    }

    // fetch song result task
    private static class SearchSongResults extends AsyncTask<String, String, String> {

        private EditText songQuerySearch;
        private Context context;
        private ProgressBar loadingWheel;
        private TextView loadingText;
        private FragmentManager fm;
        private String errorContent = "";
        private List<SongResult> songResults;
        private String query;

        // TODO micha : think of better way of passing the song list to postExecute (pass to context?)

        public SearchSongResults(Context context, EditText songQuerySearch, TextView loadingText, ProgressBar loadingWheel, FragmentManager fm) {
            this.context = context;
            this.songQuerySearch = songQuerySearch;
            this.loadingText = loadingText;
            this.loadingWheel = loadingWheel;
            this.fm = fm;

            this.songResults = new LinkedList<SongResult>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // show loading information
            this.query = songQuerySearch.getText().toString();

            String fixedLoadingInfo = "מחפש תוצאות עבור ";
            UIUtils.showTextView(loadingText, fixedLoadingInfo + query + " ... ", this.context);
            UIUtils.showProgressBar(loadingWheel, this.context);

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

            UIUtils.hideProgressBar(loadingWheel, this.context);

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
        private ProgressBar songDownloadprogressBar;
        private TextView percentageText;
        private TextView headlineViewText;
        private View rootView;
        private View songQuerySearchEditText;
        private FragmentManager fm;

        public DownloadSongResult(Context appContext, FragmentManager fm) {
            this.context = appContext;
            this.fm = fm;
            SONGS_DIRECTORY += "/HebrewSongDownloads";

            // setup view widgets
            songDownloadprogressBar = (ProgressBar) ((Activity) this.context).findViewById(R.id.progressBar);
            percentageText = (TextView) ((Activity) this.context).findViewById(R.id.percentageProgress);
            headlineViewText = (TextView) ((Activity) this.context).findViewById(R.id.loadingText);
            rootView = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            UIUtils.PrintToast(context, "ההורדה החלה", Toast.LENGTH_LONG);
            UIUtils.showProgressBar(songDownloadprogressBar, this.context);

            songQuerySearchEditText = rootView.findViewById(R.id.searchSongQuery);
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

            songDownloadprogressBar.setProgress(progressPercentage);

            // update percentage text with current percentage of the downloaded file
            UIUtils.showTextView(percentageText, progressPercentage + "\\" + songDownloadprogressBar.getMax(), this.context);

        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * *
         */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            UIUtils.hideProgressBar(songDownloadprogressBar, this.context);
            UIUtils.hideTextView(percentageText, this.context);
            UIUtils.showTextView(headlineViewText, this.context);

            if (errorContent.equals("")) {
                UIUtils.PrintToast(context, "השיר ירד בהצלחה", Toast.LENGTH_LONG);
                loadLibraryFragment();
            } else {
                UIUtils.PrintToast(context, errorContent, Toast.LENGTH_LONG);
                songQuerySearchEditText.requestFocus();
            }
        }

        private void loadLibraryFragment() {
            if (fm != null) {
                FragmentTransaction ft = fm.beginTransaction();

                LibrarySongsFragment libSongFragment = new LibrarySongsFragment(DataUtils.getSongNamesFromDirectory());
                PlayerFragment player = libSongFragment.createPlayer(context);

                // load player
                if (LibrarySongsFragment.isPlayerInited()) {
                    ft.replace(R.id.player_container, player);
                }


                // load file list
                ft.replace(R.id.library_files_container, libSongFragment);

                ft.commit();
            }
        }

    }


    // TOOD : remove!! just for test

    /**
     * Async task to load the data from server
     * *
     */
    private static class SyncData extends AsyncTask<String, Void, String> {

        private final MenuItem refreshMenuItem;

        public SyncData(MenuItem refreshMenuItem) {
            this.refreshMenuItem = refreshMenuItem;
        }

        @Override
        protected void onPreExecute() {
            // set the progress bar view
            refreshMenuItem.setActionView(R.layout.action_progressbar);

            refreshMenuItem.expandActionView();
        }

        @Override
        protected String doInBackground(String... params) {
            // not making real request in this demo
            // for now we use a timer to wait for sometime
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            refreshMenuItem.collapseActionView();
            // remove the progress bar view
            refreshMenuItem.setActionView(null);
        }
    }

}
