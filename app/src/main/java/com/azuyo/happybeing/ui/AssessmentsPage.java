package com.azuyo.happybeing.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.Utils.AppConstants;
import com.azuyo.happybeing.Utils.CommonUtils;
import com.azuyo.happybeing.Utils.HappyBeingApplicationClass;
import com.azuyo.happybeing.Utils.UserInformation;
import com.azuyo.happybeing.events.DownloadMindGymAudioFilesService;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Admin on 21-12-2016.
 */

public class AssessmentsPage extends BaseActivity implements View.OnClickListener {
    private CardView testAnxietyAssessment, selfAssessment, happinessAssessment;
    private String profile;
    private TextView testAnxietyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessments_activity_layout);
        SharedPreferences sharedPreferences = getSharedPreferences("HAPPY_BEING", MODE_PRIVATE);
        profile = sharedPreferences.getString(AppConstants.ROLE, "");
        testAnxietyText = (TextView) findViewById(R.id.test_anxiety_text);

        testAnxietyAssessment = (CardView) findViewById(R.id.test_anxiety_card);
        selfAssessment = (CardView) findViewById(R.id.self_card);
        happinessAssessment = (CardView) findViewById(R.id.happiness_card);
        testAnxietyAssessment.setOnClickListener(this);
        selfAssessment.setOnClickListener(this);
        happinessAssessment.setOnClickListener(this);

        if (profile.equals("Student")) {
            testAnxietyText.setText("Exam anxiety");
        }
        else {
            testAnxietyText.setText("Perception stress");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.test_anxiety_card:
                if (profile.equals("Student"))
                    gotoWebViewClass("https://nsmiles.typeform.com/to/UizlpC");
                else
                    gotoWebViewClass("https://nsmiles.typeform.com/to/CFXsQZ");
                break;
            case R.id.self_card:
                if (profile.equals("Student"))
                    gotoWebViewClass("https://nsmiles.typeform.com/to/rCBYRU");
                else
                    gotoWebViewClass("https://nsmiles.typeform.com/to/rCBYRU");
                break;
            case R.id.happiness_card:
                if (profile.equals("Student"))
                    gotoWebViewClass("https://nsmiles.typeform.com/to/MBnkE1");
                else
                    gotoWebViewClass("https://nsmiles.typeform.com/to/ZelnCQ");
                break;

        }
    }

    private void gotoWebViewClass(String link) {
        Intent intent = new Intent(this, AssessmetWebView.class);
        intent.putExtra("LINK", link);
        startActivity(intent);
    }
}