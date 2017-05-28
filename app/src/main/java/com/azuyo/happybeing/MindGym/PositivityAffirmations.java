package com.azuyo.happybeing.MindGym;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.Utils.AppConstants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Random;

public class PositivityAffirmations extends AppCompatActivity implements View.OnLayoutChangeListener {

    private Animation fade_in, fade_out;
    private ViewFlipper studentViewFlipper, momViewFlipper, othersViewFlipper, viewFlipper;
    MediaPlayer mediaPlayer;
    private Handler mHandler = new Handler();
    private Random mRand = new Random();
    private AdView mAdView = null;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.positive_affirmations);

        fade_in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        studentViewFlipper = (ViewFlipper) findViewById(R.id.student_viewflipper);
        momViewFlipper = (ViewFlipper) findViewById(R.id.expecting_mom_viewflipper);
        othersViewFlipper = (ViewFlipper) findViewById(R.id.others_viewflipper);
        Intent intent = getIntent();

        setTypeOfFlipper();

        viewFlipper.setInAnimation(fade_in);
        viewFlipper.setOutAnimation(fade_out);
        viewFlipper.setDisplayedChild(mRand.nextInt(viewFlipper.getChildCount()));
        viewFlipper.setFlipInterval(10000);
        viewFlipper.startFlipping();
        viewFlipper.addOnLayoutChangeListener(this);
        CustomGestureDetector customGestureDetector = new CustomGestureDetector();
        mGestureDetector = new GestureDetector(this, customGestureDetector);

        addGoogleAds();
    }

    @Override
    public void onBackPressed() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        super.onBackPressed();
    }

    private Runnable mFlip = new Runnable() {

        @Override
        public void run() {
            //TODO: check to show random or next textview
            viewFlipper.showNext();
        }
    };

    private void addGoogleAds() {
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7447665870261345~2301766396");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
       // Log.i("PositiveAffirmations", "In on focus changed"+viewFlipper.getDisplayedChild()+", "+viewFlipper.getChildCount());
        if (viewFlipper.getDisplayedChild() == viewFlipper.getChildCount() - 1) {
            finish();
        }
    }

    class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // Swipe left (next)
            if (e1.getX() > e2.getX()) {
                viewFlipper.showNext();
            }
            // Swipe right (previous)
            if (e1.getX() < e2.getX()) {
                viewFlipper.showPrevious();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    private void setTypeOfFlipper() {
        SharedPreferences preferences = getSharedPreferences("HAPPY_BEING", MODE_PRIVATE);
        String profile = preferences.getString(AppConstants.ROLE, "");
        if (profile.equals("Student")) {
            studentViewFlipper.setVisibility(View.VISIBLE);
            viewFlipper = studentViewFlipper;
        }
        else if(profile.contains("Expecting_Mom") || profile.contains("Want_to_be_mom")) {
            viewFlipper = momViewFlipper;
            momViewFlipper.setVisibility(View.VISIBLE);
        }
        else {
            viewFlipper = othersViewFlipper;
            othersViewFlipper.setVisibility(View.VISIBLE);
        }
    }
}
