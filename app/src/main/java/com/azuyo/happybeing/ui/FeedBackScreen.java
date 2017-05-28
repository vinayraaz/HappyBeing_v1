package com.azuyo.happybeing.ui;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.azuyo.happybeing.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class FeedBackScreen extends BaseActivity implements View.OnClickListener {
    private AdView mAdView = null;
    private EditText messageEdittext;
    private Button sendButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(true);
        addGoogleAds();
        messageEdittext = (EditText) findViewById(R.id.message_edittext);
        sendButton = (Button) findViewById(R.id.send_feedback);

        sendButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void addGoogleAds() {
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7447665870261345~2301766396");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.send_feedback:

               /* Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setType("plain/text");
                sendIntent.setData(Uri.parse("help@nsmiles.com"));
                sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"help@nsmiles.com"});
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                sendIntent.putExtra(Intent.EXTRA_TEXT, messageEdittext.getText().toString());*/

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "help@nsmiles.com" });
                email.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                email.putExtra(Intent.EXTRA_TEXT, messageEdittext.getText().toString());

//need this to prompts email client only
                email.setType("message/rfc822");

                startActivityForResult(Intent.createChooser(email, "Choose an Email client :"), 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 ) {
            displayDialog();
        }
    }

    private void displayDialog() {
        final Dialog dialog_custom = new Dialog(this);
        dialog_custom.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_custom.setContentView(R.layout.custom_dialog_for_thankfeedback);
        dialog_custom.show();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

            }
        };
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
               if (dialog_custom != null) {
                   dialog_custom.dismiss();
                   finish();
               }
            }
        }, 2000);
    }
}