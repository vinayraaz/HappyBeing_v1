package com.azuyo.happybeing.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.azuyo.happybeing.MindGym.GetDownloadStatus;
import com.azuyo.happybeing.R;
import com.azuyo.happybeing.Utils.AppConstants;
import com.azuyo.happybeing.Utils.CommonUtils;
import com.azuyo.happybeing.Utils.MediaPlayerInterface;
import com.azuyo.happybeing.Views.SmartAudioPlayer;
import com.azuyo.happybeing.events.DownloadAudioFilesService;
import com.azuyo.happybeing.events.DownloadMindGymAudioFilesService;

import java.io.File;

/**
 * Created by Admin on 24-03-2017.
 */
public class KnowledgeHubDayLayout extends BaseActivity implements View.OnClickListener, GetDownloadStatus, MediaPlayerInterface {
    //private LinearLayout linearLayout1, linearLayout2, linearLayout3;
    private WebView webView;
    private int presentLayout = 1;
    private RelativeLayout knowledge_hub_parent_layout;
    private ImageView playButton, download_image;
    private SmartAudioPlayer smartAudioPlayer;
    private String audioFile;
    public static Drawable drawableFile;
    public static String audioFileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.knowldege_hub_dayone);
        webView = (WebView) findViewById(R.id.webview);
        knowledge_hub_parent_layout = (RelativeLayout) findViewById(R.id.knowledge_hub_parent_layout);
        SharedPreferences sharedPreferences = getSharedPreferences("HAPPY_BEING", MODE_PRIVATE);
       String role = sharedPreferences.getString(AppConstants.ROLE, "");
       if (role.equals("Student")) {
           webView.setVisibility(View.VISIBLE);
           knowledge_hub_parent_layout.setVisibility(View.GONE);
           Intent intent = getIntent();
           if (intent.hasExtra("DAY_NUMBER")) {
               presentLayout = intent.getIntExtra("DAY_NUMBER", 1);
           }
           setWebViewContent();

       }
       else {
           webView.setVisibility(View.GONE);
           knowledge_hub_parent_layout.setVisibility(View.VISIBLE);

       }

        smartAudioPlayer = (SmartAudioPlayer) findViewById(R.id.smartAudioPlayer);
        smartAudioPlayer.setPlayButtonVisible(false);
        playButton = (ImageView) findViewById(R.id.play_button);
        download_image = (ImageView) findViewById(R.id.download_image);

        Intent intent = getIntent();
        if (intent.hasExtra("AUDIO_FILE")) {
            audioFile = intent.getStringExtra("AUDIO_FILE");
            //Log.i("KnowledgeHub", "Audio file is ::::: "+audioFile);
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
                        // DownloadAudioFilesService.DownloadOneFileTask downloadOneFileTask = new DownloadAudioFilesService.DownloadOneFileTask(this, DownloadAudioFilesService.relaxAndRelieveFileNames[fileNumber]);
                        //downloadOneFileTask.execute(DownloadAudioFilesService.relaxAndRelieveFiles[fileNumber]);
                    }
                }
            }
        }

        playButton.setOnClickListener(this);
        download_image.setOnClickListener(this);

       /*linearLayout1 = (LinearLayout) findViewById(R.id.dayOneLayout);
       linearLayout2 = (LinearLayout) findViewById(R.id.day_two_layout);
       linearLayout3 = (LinearLayout) findViewById(R.id.day_3_layout);
*/
    }

    private void setWebViewContent() {
        if (presentLayout == 0) {
            loadLink("http://happybeing.azurewebsites.net/knowledge_hublesson0.html");
        }
        if (presentLayout == 1) {
            loadLink("http://happybeing.azurewebsites.net/knowledge_hublesson1.html");
        }
        else if (presentLayout == 2) {
            loadLink("http://happybeing.azurewebsites.net/knowledge_hublesson2.html");
        }
        else if (presentLayout == 3) {
            loadLink("http://happybeing.azurewebsites.net/knowledge_hublesson3.html");
        }
        else if (presentLayout == 4) {
            loadLink("http://happybeing.azurewebsites.net/knowledge_hublesson4.html");
        }
        else if (presentLayout == 5) {
            loadLink("http://happybeing.azurewebsites.net/knowledge_hublesson5.html");
        }
        else if (presentLayout == 6) {
            loadLink("http://happybeing.azurewebsites.net/knowledge_hublesson6.html");
        }
        else if (presentLayout == 7) {
            loadLink("http://happybeing.azurewebsites.net/knowledge_hublesson7.html");
        }
        else if (presentLayout == 8) {
            loadLink("http://happybeing.azurewebsites.net/knowledge_hublesson8.html");
        }
    }

    private void loadLink(String linkString) {
        if (CommonUtils.isNetworkAvailable(this)) {
            webView.loadUrl(linkString);
            WebSettings settings = webView.getSettings();
            webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

            final ProgressDialog progressBar = ProgressDialog.show(this, "", "Loading...");
            final Activity activity = this;

            webView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (progressBar.isShowing()) {
                        progressBar.dismiss();
                    }
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    Toast.makeText(activity, "Error while loading!!!", Toast.LENGTH_LONG).show();
                }

            });
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setDatabaseEnabled(true);
        }

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
                        Toast.makeText(this, "Setting it up for you...", Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case R.id.download_image:
                Intent intent = getIntent();
                int fileNumber = intent.getIntExtra("AUDIO_FILE_NUMBER", -1);
                if (fileNumber != -1) {
                    audioFileName = DownloadMindGymAudioFilesService.mindGymForAdultsAudioListNames[fileNumber]+".mp3";
                    DownloadMindGymAudioFilesService.DownloadOneFileTask downloadOneFileTask = new DownloadMindGymAudioFilesService.DownloadOneFileTask(this, DownloadMindGymAudioFilesService.mindGymForAdultsAudioListNames[fileNumber]+".mp3");
                    downloadOneFileTask.execute(DownloadMindGymAudioFilesService.mindGymAdultAudioFiles[fileNumber]);
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
