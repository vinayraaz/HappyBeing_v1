package com.azuyo.happybeing.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.Utils.AppConstants;
import com.azuyo.happybeing.Views.AudioFilesInfo;
import com.azuyo.happybeing.Views.CustomRelaxAdapter;
import com.azuyo.happybeing.events.DownloadAudioFilesService;
import com.azuyo.happybeing.ui.AssessmetWebView;
import com.azuyo.happybeing.ui.AudioScreen;
import com.azuyo.happybeing.ui.ComposeGratitudeDiary;
import com.azuyo.happybeing.ui.ImageScreen;
import com.azuyo.happybeing.ui.LoadCompleteList;
import com.azuyo.happybeing.ui.NewHomeScreen;
import com.azuyo.happybeing.ui.OnItemSelectedListener;
import com.azuyo.happybeing.ui.VentOutJournal;
import com.azuyo.happybeing.ui.VentoutRecording;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Admin on 21-12-2016.
 */

public class AssessmentFragment extends Fragment implements View.OnClickListener {
    String selected_role="",webview_url_anxiety="",webview_url__esteem="",webview_url_control="";
    private TextView tv_anxiety,tv_esteem,tv_control;
    private LinearLayout ll_anxiety,ll_esteem,ll_control;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Self Assessments");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_assessments_activity_layout, container, false);
        tv_anxiety = (TextView)view.findViewById(R.id.tv_anxiety);
        tv_esteem = (TextView)view.findViewById(R.id.tv_esteem);
        tv_control = (TextView)view.findViewById(R.id.tv_control);

        ll_anxiety = (LinearLayout)view.findViewById(R.id.ll_anxiety);
        ll_esteem = (LinearLayout)view.findViewById(R.id.ll_esteem);
        ll_control = (LinearLayout)view.findViewById(R.id.ll_control);
        ll_anxiety.setOnClickListener(this);
        ll_esteem.setOnClickListener(this);
        ll_control.setOnClickListener(this);

        selected_role = NewHomeScreen.selected_role.toUpperCase();
        switch (selected_role)
        {
            case "STUDENT":
                valuesForStudent();
                break;
            case "LOOKING_JOB":
                valuesForLookingForLob();
                break;
            case "EMPLOYED":
                valuesForEmployed();
                break;
            case "STAFF":
                valuesForStaff();
                break;
            case "HOMEMAKER":
                valuesForHomeMaker();
                break;
            case "SENIORCITIZEN":

                break;
        }


        return view;
    }
    private void valuesForStudent() {
        tv_anxiety.setText("Exam anxiety");
        webview_url_anxiety ="https://nsmiles.typeform.com/to/UizlpC";
        webview_url__esteem = "https://nsmiles.typeform.com/to/rCBYRU";
        webview_url_control = "https://nsmiles.typeform.com/to/N0l7UV";
        tv_esteem.setText("Self esteem");
        tv_control.setText("Self control");
    }
    private  void valuesForLookingForLob() {
        tv_anxiety.setText("Exam anxiety");
        tv_esteem.setText("Self esteem");
        tv_control.setText("Self control");
        webview_url_anxiety ="https://nsmiles.typeform.com/to/UizlpC";
        webview_url__esteem = "https://nsmiles.typeform.com/to/rCBYRU";
        webview_url_control = "https://nsmiles.typeform.com/to/N0l7UV";
    }
    private  void valuesForEmployed() {
        tv_anxiety.setText("Stress");
        tv_esteem.setText("Mindfulness");
        tv_control.setText("Resilience");
        webview_url_anxiety ="https://nsmiles.typeform.com/to/FWCIlw";
        webview_url__esteem = "https://nsmiles.typeform.com/to/zL2A65";
        webview_url_control = "https://nsmiles.typeform.com/to/lbgvWO";
    }
    private  void valuesForStaff() {
        tv_anxiety.setText("Stress");
        tv_esteem.setText("Mindfulness");
        tv_control.setText("Resilience");
        webview_url_anxiety ="https://nsmiles.typeform.com/to/FWCIlw";
        webview_url__esteem = "https://nsmiles.typeform.com/to/zL2A65";
        webview_url_control = "https://nsmiles.typeform.com/to/lbgvWO";
    }
    private  void valuesForHomeMaker() {
        tv_anxiety.setText("Stress");
        tv_esteem.setText("Self esteem");
        tv_control.setText("Happiness levels");
        webview_url_anxiety ="https://nsmiles.typeform.com/to/piX1PS";
        webview_url__esteem = "https://nsmiles.typeform.com/to/rCBYRU";
        webview_url_control = "https://nsmiles.typeform.com/to/ZelnCQ";
    }
    private  void valuesForSenioreCitizen(){

    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_anxiety:
                gotoWebViewClass(webview_url_anxiety);
                break;
            case R.id.ll_esteem:
                gotoWebViewClass(webview_url__esteem);
                break;
            case R.id.ll_control:
                gotoWebViewClass(webview_url_control);
                break;

        }
    }

    private void gotoWebViewClass(String link) {
        Intent intent = new Intent(getActivity(), AssessmetWebView.class);
        intent.putExtra("LINK", link);
        startActivity(intent);
    }



}
