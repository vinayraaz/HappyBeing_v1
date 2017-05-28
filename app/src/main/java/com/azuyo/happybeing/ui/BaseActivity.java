package com.azuyo.happybeing.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.azuyo.happybeing.Utils.HappyBeingApplicationClass;


public class BaseActivity extends ActionBarActivity {


/**
* Fragment managing the behaviors, interactions and presentation of the
* navigation drawer.
*/
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HappyBeingApplicationClass.activityResumed();
    }

    @Override
    public void setTitle(CharSequence title) {
    // TODO Auto-generated method stub
    mTitle = title;
    super.setTitle(title);
    };

    @Override
    protected void onResume() {
        super.onResume();
        HappyBeingApplicationClass.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        HappyBeingApplicationClass.activityPaused();
    }

    public void restoreActionBar(String mTitle) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + mTitle + "</font>")));
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff6633")));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    /** Handle action bar item clicks here. The action bar will
    * automatically handle clicks on the Home/Up button, so long
     *as you specify a parent activity in AndroidManifest.xml.
     */
    int id = item.getItemId();

    return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()))
            {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }
}

