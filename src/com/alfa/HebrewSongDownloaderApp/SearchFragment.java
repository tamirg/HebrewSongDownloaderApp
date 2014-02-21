package com.alfa.HebrewSongDownloaderApp;


import android.app.Service;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.alfa.utils.AsyncTaskManager;
import com.alfa.utils.logic.LogUtils;

import java.io.File;


/**
 * Created by Micha on 2/13/14.
 */
public class SearchFragment extends Fragment {

    private String SONGS_DIRECTORY = Environment.getExternalStorageDirectory().toString();

    private ProgressBar progressBar;
    private ProgressBar loadingWheel;
    private MenuItem actionBarProgressBar;
    private FragmentManager fm;
    private ImageButton songSearchButton;
    private TextView loadingText;
    private TextView percentageProgress;
    private EditText songQuerySearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LogUtils.logData("flow_debug", "SearchFragment__create");

        View view = inflater.inflate(R.layout.search_fragment, container, false);
        setupFragmentView(view);
        return view;
    }

    private void setupFragmentView(final View view) {

        LogUtils.logData("flow_debug", "SearchFragment__setup");

        // setup view widgets
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        loadingWheel = (ProgressBar) view.findViewById(R.id.loadingWheel);
        loadingText = (TextView) view.findViewById(R.id.loadingText);
        percentageProgress = (TextView) view.findViewById(R.id.percentageProgress);
        songSearchButton = (ImageButton) view.findViewById(R.id.searchSongBtn);
        songQuerySearch = (EditText) view.findViewById(R.id.searchSongQuery);

        // setup progress bar
        progressBar.setVisibility(View.INVISIBLE);

        // setup fragment manager
        fm = getFragmentManager();

        // setup widget initial state
        loadingWheel.setVisibility(View.INVISIBLE);
        loadingText.setVisibility(View.INVISIBLE);
        percentageProgress.setVisibility(View.INVISIBLE);

        // setup song directory folder
        SONGS_DIRECTORY += getString(R.string.downloadFolder);
        File folder = new File(SONGS_DIRECTORY);

        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }

        setupSearchButton(view);
    }

    private void setupSearchButton(final View view) {


        // setup song search button listener
        songSearchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                executeSongSearch(v.getContext(), "", null);
            }
        });
    }

    public void executeSongSearch(Context c, String query, MenuItem actionBarProgressBar) {
        // Perform action on click

        InputMethodManager imm = (InputMethodManager) c.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(songQuerySearch.getWindowToken(), 0);
        try {

            LogUtils.logData("flow_debug", "SearchFragment__invoking song search for " + query);

            if (query.length() > 0) {
                songQuerySearch.setText(query);
            }

            // get song result from search engine
            AsyncTaskManager.getSongResult(c, songQuerySearch, loadingText, loadingWheel, actionBarProgressBar, fm);

        } catch (Exception e) {
            LogUtils.logError("fetch_song_results", e.toString());
        }
    }

    @Override
    public String toString() {
        return "SearchFragment";
    }

}