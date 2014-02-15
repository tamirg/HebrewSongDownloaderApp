package com.alfa.HebrewSongDownloaderApp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TabHost;

/**
 * Created by Micha on 2/12/14.
 */
public class TabedMainActivity extends Activity implements
        android.widget.RadioGroup.OnCheckedChangeListener {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabed_menu);

        setupTabHost();
    }

    /**
     * handles tabs setup
     */
    void setupTabHost() {
        TabHost OptionTabHost = (TabHost) findViewById(R.id.tabhost);
        OptionTabHost.setup();

        // setup search tab
        TabHost.TabSpec specs = OptionTabHost.newTabSpec("tag search");
        specs.setContent(R.id.search);
        specs.setIndicator("search");
        OptionTabHost.addTab(specs);

        // setup download tab
        specs = OptionTabHost.newTabSpec("tag download");
        specs.setContent(R.id.download);
        specs.setIndicator("download");
        OptionTabHost.addTab(specs);

        // setup library_fragment tab
        specs = OptionTabHost.newTabSpec("tag library");
        specs.setContent(R.id.library);
        specs.setIndicator("library");
        OptionTabHost.addTab(specs);

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // do nothing
    }
}