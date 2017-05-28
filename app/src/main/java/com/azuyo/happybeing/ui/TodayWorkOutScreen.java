package com.azuyo.happybeing.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.azuyo.happybeing.MindGym.GetDownloadStatus;
import com.azuyo.happybeing.R;
import com.azuyo.happybeing.Utils.AppConstants;
import com.azuyo.happybeing.Utils.CommonUtils;
import com.azuyo.happybeing.Utils.MediaPlayerInterface;
import com.azuyo.happybeing.Views.SmartAudioPlayer;
import com.azuyo.happybeing.events.DownloadMindGymAudioFilesService;

import java.io.File;

/**
 * Created by Admin on 21-03-2017.
 */
public class TodayWorkOutScreen extends BaseActivity implements View.OnClickListener, GetDownloadStatus, MediaPlayerInterface {
    public static RelativeLayout main_layout;
    private ImageView playButton, download_image;
    private TextView titleText;
    private SmartAudioPlayer smartAudioPlayer;
    private String audioFile;
    public static Drawable drawableFile;
    public static String fileName = "";
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todays_work_out);
        main_layout = (RelativeLayout) findViewById(R.id.main_layout);
        smartAudioPlayer = (SmartAudioPlayer) findViewById(R.id.smartAudioPlayer);
        smartAudioPlayer.setPlayButtonVisible(false);
        titleText = (TextView) findViewById(R.id.textview);
        playButton = (ImageView) findViewById(R.id.play_button);
        MainScreen.playButton = playButton;
        download_image = (ImageView) findViewById(R.id.download_image);
        sharedPreferences = getSharedPreferences("HAPPY_BEING", MODE_PRIVATE);
        Intent intent = getIntent();
        //Log.i("TodaysWorkOut", "Intent has extra "+intent.hasExtra("AUDIO_FILE_NAME"));
        if (intent.hasExtra("AUDIO_FILE_NAME")) {
            String fileName = intent.getStringExtra("AUDIO_FILE_NAME");
            //Log.i("TodaysWorkOut", "File name is "+fileName);
            fileName = CommonUtils.repalceNumbers(fileName);
            titleText.setText(fileName);
        }

        if (intent.hasExtra("AUDIO_FILE")) {
            audioFile = intent.getStringExtra("AUDIO_FILE");
           // Log.i("TodaysWorkOut", "Audio file is "+audioFile);
            if (audioFile != null && !audioFile.equals("")) {
                download_image.setVisibility(View.GONE);
                playButton.setVisibility(View.VISIBLE);
                //Log.i("TodaysWorkOut", "IN IF LOOP "+audioFile);
                CommonUtils.checkForOtherAppsPlaying(this);
                smartAudioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                smartAudioPlayer.setDataSource(audioFile, this);
                smartAudioPlayer.setPlayButton(true);
                smartAudioPlayer.start();
                playButton.setBackground(getResources().getDrawable(R.drawable.pause_button));
            }
            else {
                if (intent.hasExtra("AUDIO_FILE_NUMBER")) {
                    int fileNumber = intent.getIntExtra("AUDIO_FILE_NUMBER", -1);
                    if (fileNumber != -1) {
                        download_image.setVisibility(View.VISIBLE);
                        playButton.setVisibility(View.GONE);
                        // DownloadAudioFilesService.DownloadOneFileTask downloadOneFileTask = new DownloadAudioFilesService.DownloadOneFileTask(this, DownloadAudioFilesService.relaxAndRelieveFileNames[fileNumber]);
                        //downloadOneFileTask.execute(DownloadAudioFilesService.relaxAndRelieveFiles[fileNumber]);
                    }
                }
            }
        }

        playButton.setOnClickListener(this);
        download_image.setOnClickListener(this);

        if (drawableFile != null) {
            main_layout.setBackground(drawableFile);
        }

        loadImages();
    }
    public static void setMainLayoutBackground(Drawable drawable) {
        drawableFile = drawable;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.play_button:
                if (smartAudioPlayer.isPause()) {
                    CommonUtils.checkForOtherAppsPlaying(this);
                    smartAudioPlayer.start();
                    playButton.setBackground(getResources().getDrawable(R.drawable.pause_button));
                }
                else if (smartAudioPlayer.isPlaying()) {
                    smartAudioPlayer.pause();
                    playButton.setBackground(getResources().getDrawable(R.drawable.play_button));
                }
                else {
                    playButton.setBackground(getResources().getDrawable(R.drawable.pause_button));
                    if (audioFile != null) {
                        CommonUtils.checkForOtherAppsPlaying(this);
                        smartAudioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        smartAudioPlayer.setDataSource(audioFile, this);
                        smartAudioPlayer.setPlayButton(true);
                        smartAudioPlayer.start();
                    }
                    else {
                      //  Toast.makeText(this, "Setting it up for you...", Toast.LENGTH_SHORT).show();
                        download_image.setVisibility(View.VISIBLE);
                        playButton.setVisibility(View.GONE);
                    }
                }
                break;
            case R.id.download_image:
                Intent intent = getIntent();
                int fileNumber = intent.getIntExtra("AUDIO_FILE_NUMBER", -1);
                DownloadMindGymAudioFilesService downloadMindGymAudioFilesService = new DownloadMindGymAudioFilesService();
                if (fileNumber != -1) {
                    String profile = sharedPreferences.getString(AppConstants.ROLE, "");
                    DownloadMindGymAudioFilesService.DownloadOneFileTask downloadOneFileTask;
                    if (profile.equals("Student")) {
                        fileName = DownloadMindGymAudioFilesService.mindGymForStudentsAudioNames[fileNumber]+".mp3";
                        downloadOneFileTask = new DownloadMindGymAudioFilesService.DownloadOneFileTask(this, DownloadMindGymAudioFilesService.mindGymForStudentsAudioNames[fileNumber]+".mp3");
                        downloadOneFileTask.execute(DownloadMindGymAudioFilesService.mindGymForStudentsAudioList[fileNumber]);
                    }
                    else if (profile.contains("Expecting_Mom")|| profile.contains("Want_to_be_mom")) {
                        fileName = DownloadMindGymAudioFilesService.mindGymForPregnecyWomenAdioNames[fileNumber]+".mp3";
                        downloadOneFileTask = new DownloadMindGymAudioFilesService.DownloadOneFileTask(this, DownloadMindGymAudioFilesService.mindGymForPregnecyWomenAdioNames[fileNumber]+".mp3");
                        downloadOneFileTask.execute(DownloadMindGymAudioFilesService.mindGymPregnecyAudioList[fileNumber]);
                    }
                    else {
                        fileName = DownloadMindGymAudioFilesService.mindGymForAdultsAudioListNames[fileNumber]+".mp3";
                        downloadOneFileTask = new DownloadMindGymAudioFilesService.DownloadOneFileTask(this, DownloadMindGymAudioFilesService.mindGymForAdultsAudioListNames[fileNumber]+".mp3");
                        downloadOneFileTask.execute(DownloadMindGymAudioFilesService.mindGymAdultAudioFiles[fileNumber]);
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (smartAudioPlayer != null && smartAudioPlayer.isPlaying()) {
            smartAudioPlayer.finish();
        }
    }

    @Override
    public void downloadedStatus(boolean isDownloaded) {
      //  Log.i("Mindfulness", "In todays workout download completed");
        String fileDirPath = getFilesDir().getAbsolutePath() + File.separator + "HappyBeing";
        if (isDownloaded) {
            download_image.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);
            File file1 = new File(fileDirPath, "/audiofiles/"+fileName);
            if (file1.exists()) {
                String filePath = file1.getAbsolutePath();
                audioFile = filePath;
                if (audioFile != null && !audioFile.equals("")) {
                    CommonUtils.checkForOtherAppsPlaying(this);
                    smartAudioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    smartAudioPlayer.setDataSource(audioFile, this);
                    smartAudioPlayer.setPlayButton(true);
                    smartAudioPlayer.start();
                    playButton.setVisibility(View.VISIBLE);
                    playButton.setBackground(getResources().getDrawable(R.drawable.pause_button));
                }
            }
        }

    }
    private void loadImages() {
        Intent intent = getIntent();
        int fileNumber = intent.getIntExtra("AUDIO_FILE_NUMBER", -1);
        String fileList[];
        String fileDirPath = getFilesDir().getAbsolutePath() + File.separator + "HappyBeing";
        String profile = sharedPreferences.getString(AppConstants.ROLE, "");
        if (profile.equals("Student")) {
            fileList = DownloadMindGymAudioFilesService.mindGymForStudentsAudioNames;
        }
        else if (profile.contains("Expecting_Mom")|| profile.contains("Want_to_be_mom")) {
            fileList = DownloadMindGymAudioFilesService.mindGymForPregnecyWomenAdioNames;
        }
        else {
            fileList = DownloadMindGymAudioFilesService.mindGymForAdultsAudioListNames;
        }
        if(fileNumber != -1) {

            File file1 = new File(fileDirPath, "/audiofiles/" + fileList[fileNumber] + ".mp3");
            // Log.i("Mindfulness", "****File exists "+file1.exists());
            if (file1.exists()) {
                String filePath = file1.getAbsolutePath();
                audioFile = filePath;
                if (audioFile != null && !audioFile.equals("")) {
                    CommonUtils.checkForOtherAppsPlaying(this);
                    smartAudioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    smartAudioPlayer.setDataSource(audioFile, this);
                    smartAudioPlayer.setPlayButton(true);
                    smartAudioPlayer.start();
                    playButton.setVisibility(View.VISIBLE);
                    download_image.setVisibility(View.GONE);
                    playButton.setBackground(getResources().getDrawable(R.drawable.pause_button));
                }
            } else {
                boolean fileNotExist = true;
            }
        }
    }

    @Override
    public void setStatusOfPlayButton(boolean status) {
        if (!status) {
            playButton.setBackground(getResources().getDrawable(R.drawable.play_button));
            MainScreen.workOutNameString = "Good job on today’s workout";
            MainScreen.workoutTypeString = "Awesome!";
        }

    }
}
