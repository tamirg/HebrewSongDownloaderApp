package com.alfa.HebrewSongDownloaderApp;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Micha on 2/13/14.
 */
public class SongListFragment extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                inflater.getContext(), android.R.layout.simple_list_item_1,
                getSongs());
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private String[] getSongs() {
        String[] songs = {"song1", "song2", "song3"};
        return songs;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Toast.makeText(getActivity().getBaseContext(), "this is test", Toast.LENGTH_LONG).show();
    }

}