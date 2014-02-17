package com.alfa.HebrewSongDownloaderApp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.alfa.utils.AsyncTaskManager;
import com.alfa.utils.LogUtils;
import com.alfa.utils.UIUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Micha on 2/13/14.
 */
public class SearchFragment extends Fragment implements View.OnClickListener {

    private String SONGS_DIRECTORY = Environment.getExternalStorageDirectory().toString();

    private ProgressBar progressBar;
    private ProgressBar loadingWheel;

    private ImageButton songSearchButton;
    private ImageButton voiceRecognitionBtn;

    private TextView loadingText;
    private FragmentManager fm;
    private TextView percentageProgress;
    private EditText songQuerySearch;
    private View view;


    protected static final int REQUEST_OK = 1;
    protected static final int RESULT_OK = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.search_fragment, container, false);
        setupFragmentView(view);
        this.view = view;
        return view;
    }

    private void setupFragmentView(final View view) {

        // setup view widgets

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        loadingWheel = (ProgressBar) view.findViewById(R.id.loadingWheel);

        loadingText = (TextView) view.findViewById(R.id.loadingText);
        percentageProgress = (TextView) view.findViewById(R.id.percentageProgress);

        songSearchButton = (ImageButton) view.findViewById(R.id.searchSongBtn);
        voiceRecognitionBtn = (ImageButton) view.findViewById(R.id.voiceRecognitionBtn);

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

        // setup voice recognition button listener
        voiceRecognitionBtn.setOnClickListener(this);

        // setup song search button listener
        songSearchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                songQuerySearch = (EditText) view.findViewById(R.id.searchSongQuery);
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

    @Override
    public void onClick(View v) {
        try {
            Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.US.toString());
            startActivityForResult(i, REQUEST_OK);
        } catch (Exception e) {
            LogUtils.logError("Voice Recognition fail!", e.toString());
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            ArrayList<String> recognizedText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            Context c = this.view.getContext();
            if (recognizedText != null) {

                UIUtils.PrintToast(this.view.getContext(), recognizedText.get(0), Toast.LENGTH_LONG);

                // UIUtils.setQuery(c, songQuerySearch, recognizedText.get(0), false);

                //UIUtils.editText(c, songQuerySearch, recognizedText.get(0));
            }
        } catch (Exception e) {
            LogUtils.logError("voice recognition", requestCode + resultCode + e.toString());
        }

    }


}