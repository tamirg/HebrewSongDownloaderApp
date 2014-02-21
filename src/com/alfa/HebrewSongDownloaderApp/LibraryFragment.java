package com.alfa.HebrewSongDownloaderApp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.alfa.utils.logic.LogUtils;
import com.alfa.utils.ui.FragmentUtils;

/**
 * Created by Micha on 2/13/14.
 */

public class LibraryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LogUtils.logData("flow_debug", "LibraryFragment__create");
        View view = inflater.inflate(R.layout.library_fragment, container, false);
        setupFragmentView(view);
        return view;
    }

    private void setupFragmentView(View view) {

        LogUtils.logData("flow_debug", "LibraryFragment__setup");
        // load library fragment (player and library list)
        FragmentUtils.loadLibraryFragment(getFragmentManager(), view.getContext());
    }

}