package com.alfa.HebrewSongDownloaderApp;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Micha on 2/7/14.
 * <p/>
 * SonglistActivity shows all songs fetched from the user query request
 */
public class SongListFragment extends ListFragment {
    public void onCreate(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // this is just for test
        // TODO fetch songs from Searchable activity
        String[] values = new String[]{"song1", "song2", "song3"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.song_list, values);
        setListAdapter(adapter);

    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);

        // this is just for test
        Toast.makeText(v.getContext(), item + " selected", Toast.LENGTH_LONG).show();
    }
}