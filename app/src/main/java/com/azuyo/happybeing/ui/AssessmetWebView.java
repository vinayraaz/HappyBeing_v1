package com.azuyo.happybeing.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.Utils.CommonUtils;

/**
 * Created by Admin on 21-12-2016.
 */

public class AssessmetWebView extends BaseActivity {
    private WebView webView;
    private ProgressDialog progressBar;
    private String linkString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessment_web_view);
        webView = (WebView) findViewById(R.id.assessment_webview);

        Intent intent = getIntent();
        if (intent.hasExtra("LINK")) {
            linkString = intent.getStringExtra("LINK");
        }
        if (CommonUtils.isNetworkAvailable(this)) {
            webView.loadUrl(linkString);
            WebSettings settings = webView.getSettings();
            webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

            progressBar = ProgressDialog.show(this, "", "Loading...");
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
        else {
            Toast.makeText(this, "Network not available!", Toast.LENGTH_SHORT).show();
        }
    }
}

