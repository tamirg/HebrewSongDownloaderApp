package ui.search;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.alfa.HebrewSongDownloaderApp.R;
import ui.MainActivity;

public class SearchResultsActivity extends Activity {

    private TextView txtQuery;

    private ActionBar actionBar;
    private MenuItem refreshMenuItem;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_search_results);

        // get the action bar
        this.actionBar = getActionBar();
        this.menu = MainActivity.getMenu();

        // Enabling Back navigation on Action Bar icon
        actionBar.setDisplayHomeAsUpEnabled(true);

        txtQuery = (TextView) findViewById(R.id.txtQuery);

        // TODO : uncomment below
        //handleIntent(getIntent());

        // TODO : remove, this is just for test
        handleIntentWithLoader(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        // TODO : uncomment below
        //handleIntent(intent);
        // TODO : remove, this is just for test
        handleIntentWithLoader(intent);
    }

    private void handleIntentWithLoader(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            txtQuery.setText("Search Query: " + query);

        }
        new SyncData().execute();
    }

    /**
     * Handling intent data
     */
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            /**
             * Use this query to display search results like
             * 1. Getting the data from SQLite and showing in listview
             * 2. Making webrequest and displaying the data
             * For now we just display the query only
             */
            txtQuery.setText("Search Query: " + query);

        }

    }

    // TODO : remove!

    /**
     * Async task to load the data from server
     * *
     */
    private class SyncData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            // set the progress bar view
            refreshMenuItem = menu.findItem(R.id.action_refresh);
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

    ;
}
