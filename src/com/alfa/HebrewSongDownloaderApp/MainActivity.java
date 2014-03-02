package com.alfa.HebrewSongDownloaderApp;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import com.alfa.HebrewSongDownloaderApp.downloads.DownloadsFragment;
import com.alfa.HebrewSongDownloaderApp.library.LibraryFragment;
import com.alfa.HebrewSongDownloaderApp.library.PlayerFragment;
import com.alfa.HebrewSongDownloaderApp.search.SearchFragment;
import com.alfa.utils.logic.LogUtils;
import com.alfa.utils.ui.FragmentUtils;
import com.alfa.utils.ui.UIUtils;

/**
 * Created by Micha on 2/18/14.
 */
public class MainActivity extends FragmentActivity implements
        ActionBar.TabListener, SearchView.OnQueryTextListener {

    /**
     * ******************************************************************
     * ****************** activity data members *************************
     * ******************************************************************
     */

    private ActionBar actionBar;
    private MenuItem actionBarProgressBar;
    private static Menu menu;
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private String query;

    // Tab info
    private String[] tabs = {"Search", "Downloads", "Library"};

    private enum TAB_STATE {SEARCH, DOWNLOADS, LIBRARY, NA;}

    private TAB_STATE tabState = TAB_STATE.NA;

    private static SearchFragment searchFragment = null;
    private static DownloadsFragment downloadsFragment = null;
    public static LibraryFragment libraryFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LogUtils.logData("flow_debug", "MainActivity__create");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupMainView();

    }

    /**
     * ******************************************************************
     * ****************** activity setup functions **********************
     * ******************************************************************
     */

    private void setupMainView() {

        LogUtils.logData("flow_debug", "MainActivity__setup");

        setupTabFragments();
        setupActionBar();
        setupTabs();
        FragmentUtils.initFragmentManager(this);
    }

    private void setupTabFragments() {

        LogUtils.logData("flow_debug", "MainActivity__setup tab fragments");

        if (searchFragment == null) {
            searchFragment = new SearchFragment();
        }
        if (downloadsFragment == null) {
            downloadsFragment = new DownloadsFragment();
        }
        if (libraryFragment == null) {
            libraryFragment = new LibraryFragment();
        }

    }

    private void setupActionBar() {
        this.actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    }

    // setup tab functionality
    private void setupTabs() {

        setupViewPager();

        mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), searchFragment, downloadsFragment, libraryFragment);

        viewPager.setAdapter(mAdapter);

        // add Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

    }

    // setup view pager functionality
    private void setupViewPager() {
        viewPager = (ViewPager) findViewById(R.id.pager);

        // make tabs swipable
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // select tab on swipe
                actionBar.setSelectedNavigationItem(position);
                updateTabState(position);

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    private void updateTabState(int position) {
        switch (position) {
            case 0: {
                tabState = TAB_STATE.SEARCH;
                break;
            }
            case 1: {
                tabState = TAB_STATE.DOWNLOADS;
                break;
            }
            case 2: {
                tabState = TAB_STATE.LIBRARY;
                break;
            }
            default: {
                tabState = TAB_STATE.NA;
                break;
            }
        }
    }

    /**
     * ******************************************************************
     * ***************** Menu Item event handling ***********************
     * ******************************************************************
     */

    public static void initMenu(Menu menuInit) {
        menu = menuInit;
    }

    public static Menu getMenu() {
        return menu;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        LogUtils.logData("flow_debug", "MainActivity__setup menu");

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);

        /*
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(this); */

        // add search view dynamically
        MenuItem item = menu.add("Search");
        item.setIcon(R.drawable.ic_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setTitle("search");
        SearchView sv = new SearchView(this);
        sv.setOnQueryTextListener(this);
        item.setActionView(sv);

        sv.setIconified(false);

        actionBarProgressBar = menu.findItem(R.id.action_refresh);

        initMenu(menu);

        return super.onCreateOptionsMenu(menu);
    }

    // setup menu item functionality
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_refresh:
                // load the data from server
                executeSongSearch(query);
                return true;
            case R.id.settings:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * ******************************************************************
     * ********************* Tab event handling *************************
     * ******************************************************************
     */

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

        int position = tab.getPosition();
        // select tab on click
        viewPager.setCurrentItem(position);

        updateTabState(position);

        // hide keyboard on tab select
        UIUtils.hideSoftKeyboard(this);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        updateTabState(tab.getPosition());
    }

    private void executeSongSearch(String query) {
        LogUtils.logData("search_debug", "on submit, submitted : " + query);

        if (query != null && query.length() > 0) {
            UIUtils.hideSoftKeyboard(this);
            TextView loadingText = searchFragment.getLoadingText();
            String fixed = getString(R.string.loading_prefix);
            String presentedText = fixed + " " + query;
            loadingText.setText(presentedText + "...");

            this.searchFragment.executeSongSearch(this, query, actionBarProgressBar);
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (tabState.equals(TAB_STATE.SEARCH) || tabState.equals(TAB_STATE.DOWNLOADS)) {

            query = newText;
            String fixed = getString(R.string.loading_prefix);
            String presentedText = fixed + " " + newText;

            if (newText.equals("")) {
                presentedText = getString(R.string.empty_loading_text);
            }

            searchFragment.getLoadingText().setText(presentedText);
        } else if (tabState.equals(TAB_STATE.LIBRARY)) {
            FragmentUtils.filterLibrary(this, newText);
        }

        LogUtils.logData("search_debug", "on change, new text: " + newText);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String submittedText) {
        if (tabState.equals(TAB_STATE.SEARCH) || tabState.equals(TAB_STATE.DOWNLOADS)) {
            if (submittedText != null && submittedText.length() > 0) {
                executeSongSearch(submittedText);
            }
        } else if (tabState.equals(TAB_STATE.LIBRARY)) {
            FragmentUtils.filterLibrary(this, submittedText);
        }

        return false;
    }

    /**
     * ******************************************************************
     * ****************** activity flow handling ************************
     * ******************************************************************
     */

    @Override
    protected void onResume() {
        PlayerFragment.ResumePlayer();
        super.onResume();
    }

    @Override
    protected void onPause() {
        PlayerFragment.PausePlayer();
        super.onPause();
    }

}