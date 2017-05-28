package com.azuyo.happybeing.ui;

import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.Utils.CommonUtils;
import com.azuyo.happybeing.Views.SmartAudioPlayer;

import java.io.File;

public class NatureCalms extends BaseActivity {

    private Animation fade_in, fade_out;
    private ViewFlipper viewFlipper;
    private GestureDetector mGestureDetector;
    private AudioManager audioManager;
    private SmartAudioPlayer mediaPlayer;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            setContentView(R.layout.activity_nature_calms);

            viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);

            fade_in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
            fade_out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
            audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
            String fileDirPath = getFilesDir().getAbsolutePath() + File.separator + "HappyBeing";;
            File file = new File(fileDirPath, "/audiofiles/file17.mp3");
            Log.i("NatureCalms","File is "+file);
            Log.i("NatureCalms", "fILE Exists is "+file.exists());
            mediaPlayer = (SmartAudioPlayer) findViewById(R.id.smartAudioPlayer);
            if (file.exists()) {
                String filePath = file.getAbsolutePath();
                CommonUtils.checkForOtherAppsPlaying(this);
                mediaPlayer.setDataSource(filePath, this);
            }
            viewFlipper.setInAnimation(fade_in);
            viewFlipper.setOutAnimation(fade_out);

            viewFlipper.setAutoStart(true);
            viewFlipper.setFlipInterval(5000);
            CustomGestureDetector customGestureDetector = new CustomGestureDetector();
            mGestureDetector = new GestureDetector(this, customGestureDetector);

        }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("NatureCalms","Media player"+mediaPlayer);
        if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
            if (mediaPlayer != null)
            mediaPlayer.setPlayButton(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.finish();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);

        return super.onTouchEvent(event);
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

}
