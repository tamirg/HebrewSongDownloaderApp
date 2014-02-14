package com.alfa.HebrewSongDownloaderApp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Service;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import com.alfa.utils.UIUtils;
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
    private ProgressBar loadingWheel;
    private TextView loadingText;
    TextView showPercentageTextView;
    SearchView songQuerySearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.search_fragment, container, false);

        setupSearchView(view);

        return view;
    }

    private void setupSearchView(final View hostView) {

        // setup view widgets
        progressBar = (ProgressBar) hostView.findViewById(R.id.progressBar);
        loadingWheel = (ProgressBar) hostView.findViewById(R.id.loadingWheel);
        loadingText = (TextView) hostView.findViewById(R.id.loadingText);
        showPercentageTextView = (TextView) hostView.findViewById(R.id.textShowingPercentage);

        SONGS_DIRECTORY += getString(R.string.downloadFolder);

        // setup progress bar
        progressBar.setVisibility(View.GONE);

        // setup widget initial state
        loadingWheel.setVisibility(View.GONE);
        loadingText.setVisibility(View.GONE);


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
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(songQuerySearch.getWindowToken(), 0);
                try {
                    new SearchSongResultsAsyncTask(v.getContext(), hostView).execute(null, null, null);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });


    }

    /**
     * Background Async Task to download file
     */
    public class SearchSongResultsAsyncTask extends AsyncTask<String, String, String> {

        private Context context;
        private View hostView;
        private String errorContent = "";
        private List<SongResult> songResults;


        // TODO tamir : remove after testing
        // TODO micha : think of better way of passing the song list to postExecute (pass to context?)


        public SearchSongResultsAsyncTask(Context appContext, View v) {
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

            // show loading information
            String q = songQuerySearch.getQuery().toString();
            String fixedLoadingInfo = "מחפש תוצאות עבור ";
            loadingText.setText(fixedLoadingInfo + q + " ... ");
            UIUtils.showTextView(loadingText, this.context);
            UIUtils.showProgressBar(loadingWheel, this.context);

            // load empty list fragment
            LoadListFragment(null);
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... params) {
            SongResult chosenSongResult = null;
            FetchSongs fetchSongsInstance = FetchSongs.getInstance();

            try {
                String q = songQuerySearch.getQuery().toString();

                songResults = fetchSongsInstance.getSongResults(q);

                Log.w("data:fetch_songs", songResults.toString());
            } catch (Exception exc) {
                errorContent = "הרעה שגיאה";
            }

            return errorContent;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
        }


        /**
         * After completing background task
         * Dismiss the progress dialog
         * *
         */
        @Override
        protected void onPostExecute(String result) {

            // hide loading information
            //UIUtils.hideTextView(loadingText, this.context);
            loadingText.setText("");

            String q = songQuerySearch.getQuery().toString();
            String fixedLoadingInfo = "שירים שנמצאו עבור ";
            loadingText.setText(fixedLoadingInfo + q + " : ");

            UIUtils.hideProgressBar(loadingWheel, this.context);

            // TODO : should it be here?
            LoadListFragment(this.songResults);
        }

        /**
         * TODO : should be here?
         */
        private void LoadListFragment(List<SongResult> songResults) {
            FragmentManager fm = getFragmentManager();

            if (fm != null) {

                FragmentTransaction ft = fm.beginTransaction();

                Log.w("data:fetch_songs", "before replacing fragment..");
                ft.replace(R.id.list_container, new SongListFragment(songResults));
                ft.commit();
            }
        }
    }


}