package com.azuyo.happybeing.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.Utils.CommonUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.IOException;

public class VentoutRecording extends BaseActivity implements View.OnClickListener, Animation.AnimationListener, CompoundButton.OnCheckedChangeListener {
    private SeekBar seekBar;
    private TextView recordingTimer;
    private ToggleButton toggleButton, playOrPause;
    private AdView mAdView = null;
    Boolean isRecording;
    int recordTime,playTime;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer, mediaPlayer1;
    Handler handler;
    private String recordFilePath;
    public static final String STORAGE_DIR = "recordings";
    private Button done_button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.vent_out_recording);
        TextView titleText = (TextView) findViewById(R.id.title_name);
        titleText.setText("Vent in this safe place");
        ImageView imageView = (ImageView) findViewById(R.id.account_circle);
        imageView.setVisibility(View.GONE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toggleButton = (ToggleButton) findViewById(R.id.start_button);
        toggleButton.setOnCheckedChangeListener(this);
        playOrPause = (ToggleButton) findViewById(R.id.play_button);
        playOrPause.setOnCheckedChangeListener(this);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        recordingTimer = (TextView) findViewById(R.id.txttime);
        done_button = (Button) findViewById(R.id.done_button);
        done_button.setOnClickListener(this);
        handler = new Handler();
        toggleButton.setVisibility(View.VISIBLE);
        playOrPause.setVisibility(View.GONE);
        done_button.setVisibility(View.GONE);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(true);

        addGoogleAds();
        isRecording=false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.text_item_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.text_item);
        menuItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.done_button) {
            //TODO delete the audio file and start animation of delete image
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
            if (recordFilePath != null && !recordFilePath.equals(""))
                deleteFile(recordFilePath);
            ImageView deleteImageView = (ImageView)findViewById(R.id.deletion_icon);
            deleteImageView.setVisibility(View.VISIBLE);
            deleteImageView.startAnimation(s);
            mediaPlayer1 = MediaPlayer.create(this, R.raw.trash);
            mediaPlayer1.start();
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (mediaPlayer1 != null) {
            mediaPlayer1.stop();
        }
        finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public boolean deleteFile(String selectedFilePath) {
        boolean deleted = false;
        File file = new File(selectedFilePath);
       // Log.i("Ventoutrecording","File exists is "+file.exists());
        if (file.exists()) {
            deleted = file.delete();
        }
        return deleted;
    }

    private void addGoogleAds() {
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7447665870261345~2301766396");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void startRecording() {
        CommonUtils.checkForOtherAppsPlaying(this);
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource( MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat( MediaRecorder.OutputFormat.THREE_GPP);
        recordFilePath = Environment.getExternalStorageDirectory() + "/" + STORAGE_DIR + "/" + System.currentTimeMillis() + ".amr";
        mediaRecorder.setOutputFile(recordFilePath);
        mediaRecorder.setAudioEncoder( MediaRecorder.AudioEncoder.AMR_NB);
        if(!isRecording){
            //Create MediaRecorder and initialize audio source, output format, and audio encoder
            // Starting record time
            recordTime = 0;
            // Show TextView that displays record time
            recordingTimer.setVisibility(TextView.VISIBLE);
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            }
            catch (IllegalStateException ie ) {
                Toast.makeText(this, "Not able to record..", Toast.LENGTH_SHORT).show();
            }
            catch (IOException e) {
                Log.e("LOG_TAG", "prepare failed");
            }
            isRecording = true;
            handler.post(UpdateRecordTime);
        }
    }
    public void stopRecording(){
        if(isRecording) {
            // Stop recording and release resource
            try {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
            }
            catch (IllegalStateException ie) {
                Log.i("VentOut", "IllegalStateException");
            }
                // Change isRecording flag to false
                isRecording = false;
                // Hide TextView that shows record time
                recordingTimer.setVisibility(TextView.GONE);

        }
    }

    public void playIt(){
        // Create MediaPlayer object
        mediaPlayer = new MediaPlayer();
        // set start time
        playTime = 0;
        // Reset max and progress of the SeekBar
        seekBar.setMax(recordTime);
        seekBar.setProgress(0);
        try {
            // Initialize the player and start playing the audio
            mediaPlayer.setDataSource(recordFilePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            // Post the play progress
            handler.post(UpdatePlayTime);
        } catch (IOException e) {
            Log.e("LOG_TAG", "prepare failed");
        }
    }

    Runnable UpdateRecordTime = new Runnable(){
        public void run(){
            if(isRecording) {
                seekBar.setMax(100);
                recordingTimer.setText(String.valueOf(recordTime));
                recordTime += 1;
                seekBar.setProgress(recordTime);
                // Delay 1s before next call
                handler.postDelayed(this, 1000);
            }
        }
    };
    Runnable UpdatePlayTime = new Runnable(){
        public void run(){
            if(mediaPlayer.isPlaying()) {
                recordingTimer.setVisibility(View.VISIBLE);
                recordingTimer.setText(String.valueOf(playTime));
                // Update play time and SeekBar
                playTime += 1;
                seekBar.setProgress(playTime);
                // Delay 1s before next call
                handler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if (id == R.id.start_button) {
            if (!isRecording) {
                if (permissionsCheck(this)) {
                    startRecording();
                }
                else {
                    toggleButton.setChecked(false);
                    askForPermission(Manifest.permission.RECORD_AUDIO, 2);
                }
            } else {
                stopRecording();
                playOrPause.setVisibility(View.VISIBLE);
                done_button.setVisibility(View.VISIBLE);
                toggleButton.setVisibility(View.INVISIBLE);
            }
        }
        else if (id == R.id.play_button) {
            //Log.i("VentOut","MediaPlayer "+mediaPlayer);
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                } else {
                    playIt();
                }
            }
            else {
                playIt();
            }
        }
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            }
        }
    }

    private boolean permissionsCheck(Context context) {
        boolean permissionsGranted = true;
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsGranted = false;
            }
        }
        return permissionsGranted;
    }

}