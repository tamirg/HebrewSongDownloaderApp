package com.alfa.HebrewSongDownloaderApp.downloads;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.alfa.HebrewSongDownloaderApp.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// custom adapter to the downloads list view
public class DownloadsAdapter extends BaseAdapter implements View.OnClickListener {

    private DownloadListFragment downloadListFragment;
    public List<DownloadsModel> downloadsModels;

    private static LayoutInflater inflater;
    private static Map<Integer, DownloadsRowContainer> rowContainers = null;
    public Resources localResource;

    public DownloadsAdapter(DownloadListFragment downloadListFragment, LayoutInflater inflater, Resources localResource) {

        this.downloadListFragment = downloadListFragment;
        this.downloadsModels = DownloadListFragment.getDownloadsListModels();
        this.localResource = localResource;
        DownloadsAdapter.inflater = inflater;
    }

    // implement inner list functions
    public int getCount() {

        //if (downloadsModels.size() <= 0)
        //   return 1;
        return downloadsModels.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    // get view for each list row
    public View getView(int position, View convertView, ViewGroup parent) {

        DownloadsRowContainer rowContainer = new DownloadsRowContainer();
        rowContainer.rowView = convertView;

        if (convertView == null) {

            setupCurrentView(rowContainer);

        } else {
            rowContainer = (DownloadsRowContainer) rowContainer.rowView.getTag();
        }

        // load view according to the respective model
        if (downloadsModels.size() > 0) {

            // get model for the current list position
            DownloadsModel downloadsModel = downloadsModels.get(position);

            // handle downloads model
            rowContainer.songTitle.setText(downloadsModel.getSongTitle());
            rowContainer.cancel.setVisibility(View.VISIBLE);
            rowContainer.downloadProgressBar.setVisibility(View.VISIBLE);

            // TODO : tamir
            // this is the section where you load the view so you need to updated progress bar here
            //rowContainer.downloadProgressBar.setProgress(downloadsModel.getProgressPercentage());
            rowContainer.downloadProgressBar.setProgress(50);

            // set click listener for current row
            rowContainer.rowView.setOnClickListener(new OnItemClickListener(position, rowContainer));

            if (rowContainers == null) {
                rowContainers = new HashMap<Integer, DownloadsRowContainer>();
            } else if (rowContainers.get(position) == null) {
                rowContainers.put(position, rowContainer);
            }

        }

        return rowContainer.rowView;
    }

    private void setupCurrentView(DownloadsRowContainer rowContainer) {

        rowContainer.rowView = inflater.inflate(R.layout.downloads_list_item, null);
        rowContainer.songTitle = (TextView) rowContainer.rowView.findViewById(R.id.downloadsSongTitle);
        rowContainer.cancel = (Button) rowContainer.rowView.findViewById(R.id.downloadsCancelBtn);
        rowContainer.downloadProgressBar = (ProgressBar) rowContainer.rowView.findViewById(R.id.downloadsProgressBar);

        // setup buttons for current container
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
        private DownloadsRowContainer rowContainer;

        OnItemClickListener(int position, DownloadsRowContainer rowContainer) {
            this.position = position;
            this.rowContainer = rowContainer;
        }

        @Override
        public void onClick(View v) {

            // set on item click with current position

            downloadListFragment.onItemClick(position, rowContainer);
        }
    }

    private void setupButtons(final DownloadsRowContainer rowContainer) {

        rowContainer.cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO : tamir, handle song download cancel
            }
        });
    }

    // container class for holding the download row layout
    public static class DownloadsRowContainer {

        public View rowView;
        public TextView songTitle;
        public ProgressBar downloadProgressBar;
        public Button cancel;

    }

    public static DownloadsRowContainer getRowContainer(int position) {
        return rowContainers.get(position);
    }
}

