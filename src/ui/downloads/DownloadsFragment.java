package ui.downloads;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.alfa.HebrewSongDownloaderApp.R;

/**
 * Created by Micha on 2/13/14.
 */

public class DownloadsFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.downloads_fragment, container, false);
        setupFragmentView(view);
        return view;
    }

    private void setupFragmentView(View view) {

        FragmentManager fm = getFragmentManager();

        if (fm != null) {

            FragmentTransaction ft = fm.beginTransaction();

            DownloadListFragment downloadListFragment = new DownloadListFragment();

            // load downloads list
            ft.replace(R.id.download_list_container, downloadListFragment);

            ft.commit();
        }
    }

    @Override
    public String toString() {
        return "DownloadsFragment";
    }

}