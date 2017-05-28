package com.azuyo.happybeing.ui;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.ui.BaseActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class VentOutJournal extends BaseActivity implements Animation.AnimationListener, View.OnClickListener {
    private EditText titleEditText, contentEdittext;
    private String type;
    private Button doneButton;
    private ImageView deleteImageView;
    private AdView mAdView = null;
    private MediaPlayer mediaPlayer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vent_out_journal);

        TextView titleText = (TextView) findViewById(R.id.title_name);
        titleText.setText("Express through writing");
        ImageView imageView = (ImageView) findViewById(R.id.account_circle);
        imageView.setVisibility(View.GONE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(true);
        contentEdittext = (EditText) findViewById(R.id.content_editext);
        deleteImageView = (ImageView)findViewById(R.id.deletion_icon);
        deleteImageView.setVisibility(View.GONE);
        doneButton = (Button) findViewById(R.id.done_button);
        doneButton.setOnClickListener(this);
        addGoogleAds();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.text_item_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.text_item);
        menuItem.setTitle("");
        menuItem.setIcon(R.drawable.delete_icon);
        menuItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id ) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation)
    {
        if (mediaPlayer != null)
            mediaPlayer.stop();
        finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
    private void addGoogleAds() {
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7447665870261345~2301766396");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.done_button) {
            if (!contentEdittext.getText().toString().equals("")) {
                Animation fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
                Animation fade_out = AnimationUtils.loadAnimation(this, R.anim.fade_out);
                Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
                fade_out.setAnimationListener(this);
                AnimationSet s = new AnimationSet(false);
                s.addAnimation(fade_in);
                int duration = 2500;
                rotate.setDuration((long) duration);
                rotate.setStartOffset(fade_in.getDuration());
                s.addAnimation(rotate);
                fade_out.setStartOffset(fade_in.getDuration() + rotate.getDuration());
                s.addAnimation(fade_out);
                s.setFillAfter(true);
                deleteImageView.setVisibility(View.VISIBLE);
                deleteImageView.startAnimation(s);
                mediaPlayer = MediaPlayer.create(this, R.raw.trash);
                mediaPlayer.start();
            }
            else {
                finish();
            }
        }
    }
}