package ui.search;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Tamir on 23/01/14.
 */
public class SearchableActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            try {

                // replaced it with
                //FetchSongs.getSongURL(query);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}