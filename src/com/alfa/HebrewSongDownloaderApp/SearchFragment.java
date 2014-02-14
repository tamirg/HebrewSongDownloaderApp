package com.alfa.HebrewSongDownloaderApp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import engine.FetchSongs;
import entities.SongResult;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Micha on 2/13/14.
 */
public class SearchFragment extends Fragment {

    private String SONGS_DIRECTORY = Environment.getExternalStorageDirectory().toString();
    private ProgressBar progressBar;
    TextView showPercentageTextView;
    SearchView songQuerySearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // onCreateView() is a lifecycle event that is unique to a Fragment. This is called when Android
        // needs the layout for this Fragment. The call to LayoutInflater::inflate() simply takes the layout
        // ID for the layout file, the parent view that will hold the layout, and an option to add the inflated
        // view to the parent. This should always be false or an exception will be thrown. Android will add
        // the view to the parent when necessary.
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        setupSearchView(view);

        return view;
    }

    private void setupSearchView(final View hostView) {

        /*
        Button searchBtn = (Button) view.findViewById(R.id.searchSongBtn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch the tab content to display the list view.
                loadSongFragment();
            }
        });
*/


        SONGS_DIRECTORY += getString(R.string.downloadFolder);
        progressBar = (ProgressBar) hostView.findViewById(R.id.progressBar);
        showPercentageTextView = (TextView) hostView.findViewById(R.id.textShowingPercentage);
        progressBar.setVisibility(View.GONE);
        progressBar.setMax(100);

        File folder = new File(SONGS_DIRECTORY);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }

        final Button songSearchButton = (Button) hostView.findViewById(R.id.searchSongBtn);

        songSearchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                songQuerySearch = (SearchView) hostView.findViewById(R.id.searchSongQuery);
                //InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
                //imm.hideSoftInputFromWindow(songQuerySearch.getWindowToken(), 0);
                try {
                    new DownloadFileFromURL(v.getContext(), hostView).execute(null, null, null);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });


    }


    private void loadSongFragment() {


        FragmentManager fm = getFragmentManager();

        /*
        if (fm.findFragmentById(android.R.id.content) == null) {
            SongListFragment list = new SongListFragment();
            fm.beginTransaction().add(android.R.id.content, list).commit();
        }*/

/*
        if (fm != null) {
            // Perform the FragmentTransaction to load in the list tab content.
            // Using FragmentTransaction#replace will destroy any Fragments
            // currently inside R.id.fragment_content and add the new Fragment
            // in its place.
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.list_container, new SongListFragment());
            ft.commit();
        }
 */

    }


    /**
     * Background Async Task to download file
     */
    private class DownloadFileFromURL extends AsyncTask<String, String, String> {

        private Context context;
        private View hostView;
        private String errorContent = "";
        private List<SongResult> songResults;


        // TODO tamir : remove after testing
        // TODO micha : think of better way of passing the song list to postExecute (pass to context?)


        public DownloadFileFromURL(Context appContext, View v) {
            this.context = appContext;
            this.hostView = v;
            this.songResults = new LinkedList<SongResult>();
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
                String q = songQuerySearch.getQuery().toString();
                //List<SongResult> songResults = fetchSongsInstance.getSongResults(q);

                // TODO for tamir: remove , this is just for test
                songResults = new LinkedList<SongResult>();
                songResults.add(new SongResult(q + "_op1", ""));
                songResults.add(new SongResult(q + "_op2", ""));
                songResults.add(new SongResult(q + "_op3", ""));


                Log.w("data:fetch_songs", songResults.toString());


                // TODO: remove this line (its only for test the first result for downloading song)
                if (!songResults.isEmpty()) {
                    chosenSongResult = songResults.get(1);
                } else {
                    errorContent = "נמצאו תוצאות אך התוצאות פגומות";
                }
            /*} catch (NoSongFoundException noSongException) {
                errorContent = "לא נמצא שיר, נסה שם מפורט יותר";
            } catch (UnRecognizedSongEngineException noRecognitionException) {
                //errorContent = "לא נמצא מנוע לשיר"
                errorContent = noRecognitionException.getMessage();
            */
            } catch (Exception exc) {
                errorContent = "הרעה שגיאה";
            }

/*
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

            */
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


            /*super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
            showPercentageTextView.setVisibility(View.GONE);
            if (errorContent.equals("")) {
                Toast.makeText(context, "השיר ירד בהצלחה", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, errorContent, Toast.LENGTH_LONG).show();
                View rootView = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
                View songQuerySearchEditText = rootView.findViewById(R.id.searchSongQuery);
                songQuerySearchEditText.requestFocus();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                try {
                    Thread.sleep(3000);
                } catch (Exception exc) {
                    System.out.println(exc.getMessage());
                }
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }*/


            // TODO : should be here?
            LoadListFragment();


        }

        /**
         * TODO : should be here?
         */
        private void LoadListFragment() {
            FragmentManager fm = getFragmentManager();

            if (fm != null) {
                // Perform the FragmentTransaction to load in the list tab content.
                // Using FragmentTransaction#replace will destroy any Fragments
                // currently inside R.id.fragment_content and add the new Fragment
                // in its place.
                FragmentTransaction ft = fm.beginTransaction();

                Log.w("data:fetch_songs", "before replacing fragment..");
                ft.replace(R.id.list_container, new SongListFragment(this.songResults));
                ft.commit();
            }
        }
    }


}