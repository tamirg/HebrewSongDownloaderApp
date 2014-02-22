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
import com.alfa.utils.ui.FragmentUtils;

import java.util.LinkedList;
import java.util.List;

// custom adapter to the library list view
public class LibraryAdapter extends BaseAdapter implements View.OnClickListener {

    private LibrarySongsFragment libSongFragment;
    private List libraryList;
    private static LayoutInflater inflater;
    private static List<LibraryRowContainer> rowContainers = null;
    public Resources localResource;
    LibraryListModel libraryModel;

    public LibraryAdapter(LibrarySongsFragment libSongFragment, LayoutInflater inflater, List libraryList, Resources localResource) {

        this.libSongFragment = libSongFragment;
        this.libraryList = libraryList;
        this.localResource = localResource;
        LibraryAdapter.inflater = inflater;

    }

    // implement inner list functions
    public int getCount() {

        if (libraryList.size() <= 0)
            return 1;
        return libraryList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    // get view for each list row
    public View getView(int position, View convertView, ViewGroup parent) {

        LibraryRowContainer rowContainer = new LibraryRowContainer();
        rowContainer.rowView = convertView;
        rowContainer.playerState = PlayerFragment.PLAYER_STATE.NA;

        if (convertView == null) {

            setupCurrentView(rowContainer);

        } else
            rowContainer = (LibraryRowContainer) rowContainer.rowView.getTag();

        if (libraryList.size() > 0) {

            // get model for the current list position
            libraryModel = (LibraryListModel) libraryList.get(position);

            // handle model libraryList
            rowContainer.songTitle.setText(libraryModel.getSongTitle());

            // set click listener for current row
            rowContainer.rowView.setOnClickListener(new OnItemClickListener(position, rowContainer));

            if (rowContainers == null) {
                rowContainers = new LinkedList<LibraryRowContainer>();
            }

            rowContainers.add(rowContainer);
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

    private void setupButtons(final LibraryRowContainer rowContainer) {

        // show delete button
        rowContainer.delete.setVisibility(View.VISIBLE);

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

    }

    public static LibraryRowContainer getRowContainer(int position) {
        return rowContainers.get(position);
    }
}

