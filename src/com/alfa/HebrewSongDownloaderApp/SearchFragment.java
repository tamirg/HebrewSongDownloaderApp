package com.alfa.HebrewSongDownloaderApp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Service;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import com.alfa.utils.AsyncTaskManager;
import com.alfa.utils.LogUtils;

import java.io.File;

/**
 * Created by Micha on 2/13/14.
 */
public class SearchFragment extends Fragment {

    private String SONGS_DIRECTORY = Environment.getExternalStorageDirectory().toString();
    private ProgressBar progressBar;
    private ProgressBar loadingWheel;
    private TextView loadingText;
    private FragmentManager fm;
    private TextView percentageProgress;
    private SearchView songQuerySearch;

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
        percentageProgress = (TextView) hostView.findViewById(R.id.percentageProgress);
        final Button songSearchButton = (Button) hostView.findViewById(R.id.searchSongBtn);

        SONGS_DIRECTORY += getString(R.string.downloadFolder);

        // setup progress bar
        progressBar.setVisibility(View.INVISIBLE);

        // setup widget initial state
        loadingWheel.setVisibility(View.INVISIBLE);
        loadingText.setVisibility(View.INVISIBLE);
        percentageProgress.setVisibility(View.INVISIBLE);

        // setup fragment manager
        fm = getFragmentManager();

        File folder = new File(SONGS_DIRECTORY);

        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }


        // setup song search button listener
        songSearchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                songQuerySearch = (SearchView) hostView.findViewById(R.id.searchSongQuery);
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(songQuerySearch.getWindowToken(), 0);
                try {

                    // get song result from search engine
                    AsyncTaskManager.getSongResult(v.getContext(), songQuerySearch, loadingText, loadingWheel, fm);

                } catch (Exception e) {
                    LogUtils.logError("fetch_song_results", e.toString());
                }
            }
        });
    }

}