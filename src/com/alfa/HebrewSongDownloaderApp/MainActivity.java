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
import com.alfa.utils.AsyncTaskManager;
import com.alfa.utils.logic.LogUtils;

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
    private MenuItem refreshMenuItem;
    private static Menu menu;
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;

    // Tab titles
    private String[] tabs = {"Search", "Downloads", "Library"};

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
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
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
        item.setIcon(android.R.drawable.ic_menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setTitle("search");
        SearchView sv = new SearchView(this);
        sv.setOnQueryTextListener(this);
        item.setActionView(sv);

        /*
        refreshMenuItem = menu.add("search_load");
        refreshMenuItem.setIcon(android.R.drawable.ic_btn_speak_now);
        refreshMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS); */

        refreshMenuItem = menu.findItem(R.id.action_refresh);

        initMenu(menu);

        return super.onCreateOptionsMenu(menu);
    }

    // setup menu item functionality
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            //case R.id.action_search:
            // search action
            //return true;
            case R.id.action_refresh:
                // refresh
                refreshMenuItem = item;
                // load the data from server
                AsyncTaskManager.syncData(refreshMenuItem);
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
        // select tab on click
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        LogUtils.logData("search_debug", "on submit, submitted : " + query);

        this.searchFragment.executeSongSearch(this, query, refreshMenuItem);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        LogUtils.logData("search_debug", "on change, new text : " + newText);
        return false;
    }
}