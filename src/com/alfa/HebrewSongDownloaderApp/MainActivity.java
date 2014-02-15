package com.alfa.HebrewSongDownloaderApp;

import Exceptions.NoSongFoundException;
import Exceptions.UnRecognizedSongEngineException;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.alfa.utils.URLUtils;
import engine.FetchSongs;
import entities.SongResult;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;
import java.net.URI;
import java.util.List;

public class MainActivity extends Activity {
    private String SONGS_DIRECTORY = Environment.getExternalStorageDirectory().toString();
    private ProgressBar progressBar;
    TextView showPercentageTextView;
    SearchView songQuerySearch;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SONGS_DIRECTORY += getString(R.string.downloadFolder);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        showPercentageTextView = (TextView) findViewById(R.id.percentageProgress);
        progressBar.setVisibility(View.GONE);
        progressBar.setMax(100);

        File folder = new File(SONGS_DIRECTORY);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }

        final Button songSearchButton = (Button) findViewById(R.id.searchSongBtn);

        songSearchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                songQuerySearch = (SearchView) findViewById(R.id.searchSongQuery);
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
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
     */
    private class DownloadFileFromURL extends AsyncTask<String, String, String> {

        private Context context;
        private String errorContent = "";

        public DownloadFileFromURL(Context appContext) {
            this.context = appContext;
        }

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
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
         */
        @Override
        protected String doInBackground(String... params) {
            SongResult chosenSongResult = null;
            FetchSongs fetchSongsInstance = FetchSongs.getInstance();
            try {
                // TODO: implement listView which will display this list of song results
                List<SongResult> songResults = fetchSongsInstance.getSongResults(songQuerySearch.getQuery().toString());

                // TODO: remove this line (its only for test the first result for downloading song)
                if (!songResults.isEmpty()) {
                    chosenSongResult = songResults.get(1);
                } else {
                    errorContent = "נמצאו תוצאות אך התוצאות פגומות";
                }
            } catch (NoSongFoundException noSongException) {
                errorContent = "לא נמצא שיר, נסה שם מפורט יותר";
            } catch (UnRecognizedSongEngineException noRecognitionException) {
                //errorContent = "לא נמצא מנוע לשיר"
                errorContent = noRecognitionException.getMessage();
            } catch (Exception exc) {
                errorContent = "הרעה שגיאה";
            }
            if (chosenSongResult == null) {
                return errorContent;
            } else {
                String songFinalDownloadURL = chosenSongResult.getDownloadURL();
                String songFileName = chosenSongResult.getNameOfSong();

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
            }
            return errorContent;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            int progressPercentage = Integer.parseInt(progress[0]);
            progressBar.setProgress(progressPercentage);
            showPercentageTextView.setText(progressPercentage + "%\\" + progressBar.getMax() + "%");
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * *
         */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
            showPercentageTextView.setVisibility(View.GONE);
            if (errorContent.equals("")) {
                Toast.makeText(context, "השיר ירד בהצלחה", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, errorContent, Toast.LENGTH_LONG).show();
                View rootView = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
                View songQuerySearchEditText = rootView.findViewById(R.id.searchSongQuery);
                songQuerySearchEditText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                try {
                    Thread.sleep(3000);
                } catch (Exception exc) {
                    System.out.println(exc.getMessage());
                }
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }
    }
}
