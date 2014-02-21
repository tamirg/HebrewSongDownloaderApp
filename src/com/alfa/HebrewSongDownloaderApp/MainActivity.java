package com.alfa.HebrewSongDownloaderApp;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import com.alfa.utils.AsyncTaskManager;

/**
 * Created by Micha on 2/18/14.
 */
public class MainActivity extends FragmentActivity implements
        ActionBar.TabListener {

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

    private SearchFragment searchFragment;
    private DownloadsFragment downloadsFragment;
    private LibraryFragment libraryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        setupTabFragments();
        setupActionBar();
        setupTabs();
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
                // on changing the page
                // make respected tab selected
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

    private void setupActionBar() {
        this.actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    }

    private void setupTabFragments() {
        searchFragment = new SearchFragment();
        downloadsFragment = new DownloadsFragment();
        libraryFragment = new LibraryFragment();
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));

        initMenu(menu);

        return super.onCreateOptionsMenu(menu);
    }

    // setup menu item functionality
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_search:
                // search action
                return true;
            case R.id.action_refresh:
                // refresh
                refreshMenuItem = item;
                // load the data from server
                AsyncTaskManager.syncData(refreshMenuItem);
                return true;
            case R.id.action_help:
                // help action
                return true;
            case R.id.action_check_updates:
                // check for updates action
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
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // if (mFragment != null) {
        // Detach the fragment, because another one is being attached
        //     ft.detach(mFragment);
        // }
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }


}