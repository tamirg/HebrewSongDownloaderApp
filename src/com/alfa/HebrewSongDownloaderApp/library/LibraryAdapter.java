package com.alfa.HebrewSongDownloaderApp.library;

import android.content.res.Resources;
import android.view.*;
import android.widget.*;
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
    private static boolean hasSongInEdit;

    public LibraryAdapter(LibrarySongsFragment libSongFragment, LayoutInflater inflater, Resources localResource) {

        this.libSongFragment = libSongFragment;
        this.localResource = localResource;
        this.hasSongInEdit = false;
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
            setupCurrentView(position, rowContainer);
        } else {
            rowContainer = (LibraryRowContainer) rowContainer.rowView.getTag();
        }

        setupCurrentView(position, rowContainer);

        if (libraryListModels.size() > 0) {

            // get model for the current list position
            LibraryListModel libraryModel = libraryListModels.get(position);

            // handle model libraryList
            rowContainer.songTitle.setText(libraryModel.getSongTitle());
            rowContainer.delete.setVisibility(View.VISIBLE);

            if (libraryModel.isRenameMode()) {
                rowContainer.renameText.setVisibility(View.VISIBLE);
                rowContainer.renameText.setSelectAllOnFocus(true);
            } else {
                //rowContainer.renameText.setVisibility(View.INVISIBLE);
            }

            if (libraryModel.isPlaying()) {
                LogUtils.logData("get_view", "setting  " + position + " visible");
                rowContainer.playingIndicator.setVisibility(View.VISIBLE);
                rowContainer.rowView.setBackgroundColor(rowContainer.rowView.getContext().getResources().getColor(R.color.list_selected));
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


    private void setupCurrentView(final int position, LibraryRowContainer rowContainer) {

        rowContainer.rowView = inflater.inflate(R.layout.library_list_item, null);
        rowContainer.songTitle = (TextView) rowContainer.rowView.findViewById(R.id.librarySongTitle);
        rowContainer.playingIndicator = (ImageView) rowContainer.rowView.findViewById(R.id.libraryPlayingIndicator);
        rowContainer.delete = (ImageButton) rowContainer.rowView.findViewById(R.id.libraryDeleteBtn);
        rowContainer.edit = (ImageButton) rowContainer.rowView.findViewById(R.id.libraryEditBtn);
        rowContainer.renameText = (EditText) rowContainer.rowView.findViewById(R.id.renameText);

        rowContainer.editState = LibraryRowContainer.EDIT_STATE.NA;

        setupRenameText(position, rowContainer);

        setupButtons(rowContainer);

        // set container with LayoutInflater
        rowContainer.rowView.setTag(rowContainer);
    }

    private void setupRenameText(final int position, LibraryRowContainer rowContainer) {
        rowContainer.renameText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return libSongFragment.onRenameKeyChange(position, v, keyCode, event);
            }
        });

        rowContainer.renameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                // TODO : think if it is needed
                //libSongFragment.onRenameFocusChange(position,v,hasFocus);
            }
        });
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

        setTouchMode(rowContainer.delete, R.drawable.ic_delete, R.drawable.ic_delete_pressed);
        setTouchMode(rowContainer.edit, R.drawable.ic_edit, R.drawable.ic_edit_pressed);

        rowContainer.delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DataUtils.deleteFile(rowContainer.songTitle.getText().toString());
                FragmentUtils.loadLibraryFragment(v.getContext());
            }
        });

        rowContainer.edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                libSongFragment.onEditClick(rowContainer);
            }
        });
    }


    private void setTouchMode(final ImageButton imgBtn, final int normal, final int onPressed) {
        imgBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP: {
                        imgBtn.setImageResource(normal);
                        LogUtils.logData("setOnTouchListener", "up => normal edit " + event);
                        break;
                    }

                    case MotionEvent.ACTION_CANCEL: {
                        imgBtn.setImageResource(normal);
                        LogUtils.logData("setOnTouchListener", "cancelled => normal edit " + event);
                        break;
                    }

                    default: {
                        imgBtn.setImageResource(onPressed);
                        LogUtils.logData("setOnTouchListener", "default => pressed edit " + event);
                        break;
                    }
                }

                return false;
            }
        });
    }

    // container class for holding the library row layout libraryList
    public static class LibraryRowContainer {

        public View rowView;
        public TextView songTitle;
        public EditText renameText;
        public ImageView playingIndicator;
        public ImageButton delete;
        public ImageButton edit;
        public PlayerFragment.PLAYER_STATE playerState;
        public int listPosition;

        public enum EDIT_STATE {REGULAR, EDITING, NA;}

        public EDIT_STATE editState;

    }

    public static LibraryRowContainer getRowContainer(int position) {
        return rowContainers.get(position);
    }

    public static boolean hasSongInEdit() {
        return hasSongInEdit;
    }

    public static void setSongInEdit(boolean hasSongInEdit) {
        LibraryAdapter.hasSongInEdit = hasSongInEdit;
    }


}

