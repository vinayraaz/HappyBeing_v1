package com.azuyo.happybeing.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.azuyo.happybeing.R;

/**
 * Created by nSmiles on 5/26/2017.
 */

public class HelpScreenActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(false);
    }
}
