package com.alfa.utils;

/**
 * Created by Tamir on 14/02/14.
 */

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;
import com.alfa.HebrewSongDownloaderApp.R;
import engine.FetchSongs;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;
import java.net.URI;

// JUST FOR TEST DONT FREAK OUT!!!!

/**
 * Background Async Task to download file
 */
public class DownloadSongResultAsyncTask extends AsyncTask<String, String, String> {
    String SONGS_DIRECTORY = Environment.getExternalStorageDirectory().toString();
    private Context context;
    private String errorContent = "";

    public DownloadSongResultAsyncTask(Context appContext) {
        this.context = appContext;
        SONGS_DIRECTORY += "/HebrewSongDownloads";
    }

    /**
     * Before starting background thread
     * Show Progress Bar Dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        UIUtils.PrintToast(context, "ההורדה החלה", Toast.LENGTH_LONG);
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
        //int progressPercentage = Integer.parseInt(progress[0]);
    }

    /**
     * After completing background task
     * Dismiss the progress dialog
     * *
     */
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (errorContent.equals("")) {
            UIUtils.PrintToast(context, "השיר ירד בהצלחה", Toast.LENGTH_LONG);
        } else {
            Toast.makeText(context, errorContent, Toast.LENGTH_LONG).show();
            View rootView = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
            View songQuerySearchEditText = rootView.findViewById(R.id.searchSongQuery);
            songQuerySearchEditText.requestFocus();
            try {
                Thread.sleep(3000);
            } catch (Exception exc) {
                System.out.println(exc.getMessage());
            }
        }
    }
}
