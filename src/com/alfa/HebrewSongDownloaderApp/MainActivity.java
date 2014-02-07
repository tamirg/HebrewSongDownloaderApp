package com.alfa.HebrewSongDownloaderApp;

import Exceptions.NoSongFoundException;
import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import engine.FetchSongs;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;
import java.net.URI;
import java.util.Collections;

public class MainActivity extends Activity {
    private ProgressBar progressBar;
    TextView showPercentageTextView;
    SearchView songQuerySearch;
    //private Context context = this.getApplicationContext();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        showPercentageTextView = (TextView) findViewById(R.id.textShowingPercentage);
        progressBar.setVisibility(View.GONE);
        progressBar.setMax(100);

        File folder = new File(Environment.getExternalStorageDirectory() + "/HebrewSongDownloads");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }

        final Button songSearchButton = (Button) findViewById(R.id.searchSongBtn);

        songSearchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                songQuerySearch = (SearchView) findViewById(R.id.searchSongQuery);
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(songQuerySearch.getWindowToken(), 0);
                 try {
                    new DownloadFileFromURL(v.getContext()).execute(null, null, null);
                 } catch (Exception e) {
                        System.out.println(e.getMessage());
                 }
            }
        });
    }

    /**
     * Background Async Task to download file
     * */
    private class DownloadFileFromURL extends AsyncTask<String, String, String> {

        private Context context;
        private String errorContent = "";

        public DownloadFileFromURL(Context appContext) {
            this.context = appContext;
        }

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(context, "ההורדה החלה", Toast.LENGTH_LONG).show();
            progressBar.setProgress(0);
            progressBar.setVisibility(View.VISIBLE);
            showPercentageTextView.setText("");
            showPercentageTextView.setVisibility(View.VISIBLE);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... params) {
            String[] urlParamsForDownload = null;
            try {
                urlParamsForDownload = FetchSongs.downloadHebrewSong(songQuerySearch.getQuery().toString(),
                        Environment.getExternalStorageDirectory() + getString(R.string.downloadFolder));
            } catch (NoSongFoundException noSongException) {
                errorContent = "לא נמצא שיר, נסה שם מפורט יותר";
            } catch (Exception exc) {
                System.out.println(exc.getMessage());
                errorContent = "הרעה שגיאה";
            }
            if (urlParamsForDownload == null) {
                return errorContent;
            } else {
                String directoryForSong = urlParamsForDownload[0];
                String songFinalDownloadURL = urlParamsForDownload[1];
                String songFileName = urlParamsForDownload[2];

                try {
                    URI songFinalDownloadURI = FetchSongs.parseUrl(songFinalDownloadURL);
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpRequest = new HttpPost(songFinalDownloadURI.toString());
                    HttpResponse responseSongFile = httpClient.execute(httpRequest);
                    int songFileLengthInBytes;

                    HttpEntity entity = responseSongFile.getEntity();
                    songFileLengthInBytes = (int)entity.getContentLength();
                    BufferedInputStream bfInputStream = new BufferedInputStream(entity.getContent());
                    String filePath = directoryForSong + File.separator + songFileName + FetchSongs.SONG_FILE_MP3_SUFFIX;
                    BufferedOutputStream bfOutputStream = new BufferedOutputStream(new FileOutputStream(filePath));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[8192];
                    long totalBytesDownloaded = 0;
                    int bytesRead;
                    while ((bytesRead = bfInputStream.read(buffer)) > 0) {
                        totalBytesDownloaded += bytesRead;

                        // publishing the progress....
                        publishProgress(""+(int)((totalBytesDownloaded*100)/songFileLengthInBytes));

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
            }
            return errorContent;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            int progressPercentage = Integer.parseInt(progress[0]);
            progressBar.setProgress(progressPercentage);
            showPercentageTextView.setText(progressPercentage + "%\\" + progressBar.getMax() + "%");
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
            showPercentageTextView.setVisibility(View.GONE);
            if (errorContent.equals("")) {
                Toast.makeText(context, "השיר ירד בהצלחה", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, errorContent, Toast.LENGTH_LONG).show();
                View rootView = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
                View songQuerySearchEditText = rootView.findViewById(R.id.searchSongQuery);
                songQuerySearchEditText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                try {
                    Thread.sleep(3000);
                } catch (Exception exc) {
                    System.out.println(exc.getMessage());
                }
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            }
        }
    }
}
