package com.alfa.HebrewSongDownloaderApp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Micha on 2/13/14.
 */
public class SearchFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // onCreateView() is a lifecycle event that is unique to a Fragment. This is called when Android
        // needs the layout for this Fragment. The call to LayoutInflater::inflate() simply takes the layout
        // ID for the layout file, the parent view that will hold the layout, and an option to add the inflated
        // view to the parent. This should always be false or an exception will be thrown. Android will add
        // the view to the parent when necessary.
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        Button searchBtn = (Button) view.findViewById(R.id.searchSongBtn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch the tab content to display the list view.
                loadSongFragment();
            }
        });

        return view;
    }

    private void loadSongFragment() {


        FragmentManager fm = getFragmentManager();

        /*
        if (fm.findFragmentById(android.R.id.content) == null) {
            SongListFragment list = new SongListFragment();
            fm.beginTransaction().add(android.R.id.content, list).commit();
        }*/


        if (fm != null) {
            // Perform the FragmentTransaction to load in the list tab content.
            // Using FragmentTransaction#replace will destroy any Fragments
            // currently inside R.id.fragment_content and add the new Fragment
            // in its place.
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.list_container, new SongListFragment());
            ft.commit();
        }


    }

}