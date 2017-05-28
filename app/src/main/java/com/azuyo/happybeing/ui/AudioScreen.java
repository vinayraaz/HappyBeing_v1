package com.azuyo.happybeing.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.azuyo.happybeing.MindGym.GetDownloadStatus;
import com.azuyo.happybeing.R;
import com.azuyo.happybeing.ServerAPIConnectors.APIProvider;
import com.azuyo.happybeing.Utils.CommonUtils;
import com.azuyo.happybeing.Utils.MediaPlayerInterface;
import com.azuyo.happybeing.Views.SmartAudioPlayer;
import com.azuyo.happybeing.events.DownloadAudioFilesService;

import java.io.File;

/**
 * Created by Admin on 21-12-2016.
 */

public class AudioScreen extends BaseActivity implements View.OnClickListener, GetDownloadStatus, MediaPlayerInterface {
    public static RelativeLayout main_layout;
    private ImageView playButton, download_image;
    private TextView titleText;
    private SmartAudioPlayer smartAudioPlayer;
    private String audioFile,text_title="";
    public static Drawable drawableFile;
    public static String audioFileName;
    private ImageView Imageview_set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_audio_layout);

        main_layout = (RelativeLayout) findViewById(R.id.main_layout);
        smartAudioPlayer = (SmartAudioPlayer) findViewById(R.id.smartAudioPlayer);
        smartAudioPlayer.setPlayButtonVisible(false);
        titleText = (TextView) findViewById(R.id.textview);
        playButton = (ImageView) findViewById(R.id.play_button);
        download_image = (ImageView) findViewById(R.id.download_image);
        Imageview_set = (ImageView) findViewById(R.id.image_view);

        Intent intent = getIntent();
        if (intent.hasExtra("TITLE")) {
            titleText.setText(intent.getStringExtra("TITLE"));
            main_layout.setVisibility(View.GONE);
            Imageview_set.setVisibility(View.VISIBLE);
            text_title = intent.getStringExtra("TITLE");
            System.out.println("AudioScreen.TITLE "+intent.getStringExtra("TITLE"));
        }
        //to match title
        switch (text_title){
            case "Stretching to relax your muscles":
                main_layout.setVisibility(View.GONE);
                Imageview_set.setVisibility(View.VISIBLE);
                Imageview_set.setImageResource(R.drawable.streach_background);
                break;
            case "Have a Alone time to collect your thoughts and clear your head":
                main_layout.setVisibility(View.GONE);
                Imageview_set.setVisibility(View.VISIBLE);
                Imageview_set.setImageResource(R.drawable.have_a_alone_time);
                break;
            case "Make or create a stress free zone and relax there for a while till you feel calm":
                main_layout.setVisibility(View.GONE);
                Imageview_set.setVisibility(View.VISIBLE);
                Imageview_set.setImageResource(R.drawable.make_a_free_zone_backgorund);
                break;
            case "Write a Gratitude journal":
                main_layout.setVisibility(View.GONE);
                Imageview_set.setVisibility(View.VISIBLE);
                break;
            case "Journal your thoughts and feelings":
                main_layout.setVisibility(View.GONE);
                Imageview_set.setVisibility(View.VISIBLE);
                break;
            case "Vent your burdens and disappointments":
                main_layout.setVisibility(View.GONE);
                Imageview_set.setVisibility(View.VISIBLE);
                break;
            default:
                main_layout.setVisibility(View.VISIBLE);
                Imageview_set.setVisibility(View.GONE);
        }

        if (intent.hasExtra("AUDIO_FILE")) {
            audioFile = intent.getStringExtra("AUDIO_FILE");
            System.out.println("AUDIO_FILE***************"+audioFile);
            if (audioFile != null && !audioFile.equals("")) {
                CommonUtils.checkForOtherAppsPlaying(this);
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
                        }
                }
            }
        }

        playButton.setOnClickListener(this);
        download_image.setOnClickListener(this);

        if (drawableFile != null) {
            main_layout.setBackground(drawableFile);
        }
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
                        smartAudioPlayer.setDataSource(audioFile, this);
                        smartAudioPlayer.setPlayButton(true);
                        smartAudioPlayer.start();
                    }
                    else {
                        Intent intent = getIntent();
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

            break;

            case R.id.download_image:
                Intent intent = getIntent();
                int fileNumber = intent.getIntExtra("AUDIO_FILE_NUMBER", -1);
                if (fileNumber != -1) {
                    audioFileName = DownloadAudioFilesService.relaxAndRelieveFileNames[fileNumber];
                    DownloadAudioFilesService.DownloadOneFileTask downloadOneFileTask = new DownloadAudioFilesService.DownloadOneFileTask(this, DownloadAudioFilesService.relaxAndRelieveFileNames[fileNumber]);
                    downloadOneFileTask.execute(DownloadAudioFilesService.relaxAndRelieveFiles[fileNumber]);
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
        //Log.i("AudioScreen", "File downloaded"+isDownloaded);
        String fileDirPath = getFilesDir().getAbsolutePath() + File.separator + "HappyBeing";
        if (isDownloaded) {
            download_image.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);
            File file1 = new File(fileDirPath, "/audiofiles/"+audioFileName);
            if (file1.exists()) {
                String filePath = file1.getAbsolutePath();
                audioFile = filePath;
                if (audioFile != null && !audioFile.equals("")) {
                    CommonUtils.checkForOtherAppsPlaying(this);
                    smartAudioPlayer.setDataSource(audioFile, this);
                    smartAudioPlayer.setPlayButton(true);
                    smartAudioPlayer.start();
                    playButton.setBackground(getResources().getDrawable(R.drawable.pause_button));
                }
            }
        }
    }

    @Override
    public void setStatusOfPlayButton(boolean status) {
        if (!status)
        playButton.setBackground(getResources().getDrawable(R.drawable.play_button));

    }
}

