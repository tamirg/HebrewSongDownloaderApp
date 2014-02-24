package com.alfa.HebrewSongDownloaderApp.library;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.alfa.HebrewSongDownloaderApp.R;
import com.alfa.utils.logic.DataUtils;
import com.alfa.utils.logic.LogUtils;
import com.alfa.utils.ui.FragmentUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// custom adapter to the library list view
public class LibraryAdapter extends BaseAdapter implements View.OnClickListener {

    private LibrarySongsFragment libSongFragment;
    private LayoutInflater inflater;
    private static Map<Integer, LibraryRowContainer> rowContainers = null;
    private Resources localResource;
    private List<LibraryListModel> libraryListModels;

    public LibraryAdapter(LibrarySongsFragment libSongFragment, LayoutInflater inflater, Resources localResource) {

        this.libSongFragment = libSongFragment;
        this.localResource = localResource;
        this.libraryListModels = LibrarySongsFragment.getLibraryListModels();
        this.inflater = inflater;
    }

    // implement inner list functions
    public int getCount() {

        //if (libraryList.size() <= 0)
        //  return 1;
        return libraryListModels.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    // get view for each list row asynchronously when view is visible to the user
    public View getView(int position, View convertView, ViewGroup parent) {
        LibraryRowContainer rowContainer = new LibraryRowContainer();
        rowContainer.rowView = convertView;
        rowContainer.playerState = PlayerFragment.PLAYER_STATE.NA;

        if (convertView == null) {
            setupCurrentView(rowContainer);
        } else {
            rowContainer = (LibraryRowContainer) rowContainer.rowView.getTag();
        }

        setupCurrentView(rowContainer);

        if (libraryListModels.size() > 0) {

            // get model for the current list position
            LibraryListModel libraryModel = libraryListModels.get(position);

            // handle model libraryList
            rowContainer.songTitle.setText(libraryModel.getSongTitle());
            rowContainer.delete.setVisibility(View.VISIBLE);

            if (libraryModel.isPlaying()) {
                LogUtils.logData("get_view", "setting  " + position + " visible");
                rowContainer.playingIndicator.setVisibility(View.VISIBLE);
            } else {
                LogUtils.logData("get_view", "setting  " + position + " invisible");
                rowContainer.playingIndicator.setVisibility(View.INVISIBLE);
            }

            // just for debug
            rowContainer.listPosition = position;

            // set click listener for current row
            rowContainer.rowView.setOnClickListener(new OnItemClickListener(position, rowContainer));

            // set long click listener for current row
            rowContainer.rowView.setOnLongClickListener(new OnLongClickListener(position, rowContainer));


            if (rowContainers == null) {
                rowContainers = new HashMap<Integer, LibraryRowContainer>();
            } else if (rowContainers.get(position) == null) {
                rowContainers.put(position, rowContainer);
            }

        }

        return rowContainer.rowView;
    }


    private void setupCurrentView(LibraryRowContainer rowContainer) {

        rowContainer.rowView = inflater.inflate(R.layout.library_list_item, null);
        rowContainer.songTitle = (TextView) rowContainer.rowView.findViewById(R.id.librarySongTitle);
        rowContainer.playingIndicator = (ImageView) rowContainer.rowView.findViewById(R.id.libraryPlayingIndicator);
        rowContainer.delete = (Button) rowContainer.rowView.findViewById(R.id.libraryDeleteBtn);

        setupButtons(rowContainer);

        // set container with LayoutInflater
        rowContainer.rowView.setTag(rowContainer);
    }

    @Override
    public void onClick(View v) {
        //v.setBackgroundColor(0x4F76C7);
    }


    // set click functionality
    private class OnItemClickListener implements View.OnClickListener {
        private int position;
        private LibraryRowContainer rowContainer;

        OnItemClickListener(int position, LibraryRowContainer rowContainer) {
            this.position = position;
            this.rowContainer = rowContainer;
        }

        @Override
        public void onClick(View v) {

            // set on item click with current position

            libSongFragment.onItemClick(position, rowContainer);
        }
    }

    // set click functionality
    private class OnLongClickListener implements View.OnLongClickListener {

        private int position;
        private LibraryRowContainer rowContainer;

        OnLongClickListener(int position, LibraryRowContainer rowContainer) {
            this.position = position;
            this.rowContainer = rowContainer;
        }

        @Override
        public boolean onLongClick(View v) {
            libSongFragment.onItemLongClick(position, rowContainer);
            return false;
        }
    }

    private void setupButtons(final LibraryRowContainer rowContainer) {

        // show delete button

        rowContainer.delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DataUtils.deleteFile(rowContainer.songTitle.getText().toString());
                FragmentUtils.loadLibraryFragment(v.getContext());
            }
        });
    }

    // container class for holding the library row layout libraryList
    public static class LibraryRowContainer {

        public View rowView;
        public TextView songTitle;
        public ImageView playingIndicator;
        public Button delete;
        public PlayerFragment.PLAYER_STATE playerState;
        public int listPosition;

    }

    public static LibraryRowContainer getRowContainer(int position) {
        return rowContainers.get(position);
    }


}

