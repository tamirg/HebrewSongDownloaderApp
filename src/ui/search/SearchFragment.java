package ui.search;


import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.alfa.HebrewSongDownloaderApp.R;
import utils.async.AsyncTaskManager;
import utils.logic.LogUtils;

import java.io.File;


/**
 * Created by Micha on 2/13/14.
 */
public class SearchFragment extends Fragment {

	private String SONGS_DIRECTORY = Environment.getExternalStorageDirectory().toString();
	private ProgressBar progressBar;
	private FragmentManager fm;
	private ImageButton songSearchButton;
	private TextView loadingText;
	private TextView percentageProgress;

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
		loadingText = (TextView) view.findViewById(R.id.loadingText);
		percentageProgress = (TextView) view.findViewById(R.id.percentageProgress);

		// setup progress bar
		progressBar.setVisibility(View.INVISIBLE);

		// setup fragment manager
		fm = getFragmentManager();

		// setup widget initial state
		loadingText.setVisibility(View.VISIBLE);
		loadingText.setText("הזן שם שיר לחיפוש");
		percentageProgress.setVisibility(View.INVISIBLE);

		// setup song directory folder
		SONGS_DIRECTORY += getString(R.string.downloadFolder);
		File folder = new File(SONGS_DIRECTORY);

		boolean success = true;
		if (!folder.exists()) {
			success = folder.mkdir();
		}
	}

	public TextView getLoadingText() {
		return loadingText;
	}

	public void executeSongSearch(Context c, String query, MenuItem actionBarProgressBar) {
		try {
			LogUtils.logData("flow_debug", "SearchFragment__invoking song search for " + query);

			// get song result from search engine
			AsyncTaskManager.getSongResult(c, query, loadingText, actionBarProgressBar);

		} catch (Exception e) {
			LogUtils.logError("fetch_song_results", e.toString());
		}
	}

	@Override
	public String toString() {
		return "SearchFragment";
	}
}
