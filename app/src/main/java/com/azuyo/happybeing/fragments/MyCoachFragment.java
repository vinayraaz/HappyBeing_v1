package com.azuyo.happybeing.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.Utils.PrefManager;
import com.azuyo.happybeing.ui.NewHomeScreen;

/**
 * Created by nSmiles on 5/17/2017.
 */

public class MyCoachFragment extends Fragment {

    PrefManager prefManager;
    private RadioGroup radio_group;
    private RadioButton rb_To_Conceive, rb_To_pregnant,rb_To_Caregive,rb_To_Depression,rb_To_Chronic,rb_To_None;

    String selected_role="";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Personal Wellness Guide");
    }

    // Vinay @ 24/5/2017
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_coach_fragment_layout, container, false);
        prefManager = new PrefManager(getActivity());
        radio_group = (RadioGroup)view.findViewById(R.id.radio_group);
        rb_To_Conceive = (RadioButton)view.findViewById(R.id.rd_try_conceive);
        rb_To_pregnant = (RadioButton)view.findViewById(R.id.rd_pregnant);
        rb_To_Caregive = (RadioButton)view.findViewById(R.id.rd_caregiver);
        rb_To_Depression = (RadioButton)view.findViewById(R.id.rd_depression);
        rb_To_Chronic = (RadioButton)view.findViewById(R.id.rd_chronicpain);
        rb_To_None = (RadioButton)view.findViewById(R.id.rd_none);

        selected_role = NewHomeScreen.selected_role.toUpperCase();
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    }
        });switch (selected_role)
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
                valuesForSenioreCitizen();
                break;
        }

        return view;
    }

    // Vinay @ 24.05.2017
    // Assign values to student
    private void valuesForStudent() {
        rb_To_Conceive.setVisibility(View.GONE);
        rb_To_pregnant.setVisibility(View.GONE);
        rb_To_Caregive.setVisibility(View.GONE);
        rb_To_Depression.setVisibility(View.VISIBLE);
        rb_To_Chronic.setVisibility(View.VISIBLE);
        rb_To_None.setVisibility(View.VISIBLE);
    }

    private  void valuesForLookingForLob() {
        rb_To_Conceive.setVisibility(View.GONE);
        rb_To_pregnant.setVisibility(View.GONE);
        rb_To_Caregive.setVisibility(View.GONE);
        rb_To_Depression.setVisibility(View.GONE);
        rb_To_Chronic.setVisibility(View.GONE);
        rb_To_None.setVisibility(View.VISIBLE);
    } private  void valuesForEmployed() {
        rb_To_Conceive.setVisibility(View.VISIBLE);
        rb_To_pregnant.setVisibility(View.VISIBLE);
        rb_To_Caregive.setVisibility(View.VISIBLE);
        rb_To_Depression.setVisibility(View.VISIBLE);
        rb_To_Chronic.setVisibility(View.VISIBLE);
        rb_To_None.setVisibility(View.VISIBLE);
    } private  void valuesForStaff() {
        rb_To_Conceive.setVisibility(View.VISIBLE);
        rb_To_pregnant.setVisibility(View.VISIBLE);
        rb_To_Caregive.setVisibility(View.VISIBLE);
        rb_To_Depression.setVisibility(View.VISIBLE);
        rb_To_Chronic.setVisibility(View.VISIBLE);
        rb_To_None.setVisibility(View.VISIBLE);
    } private  void valuesForHomeMaker() {
        rb_To_Conceive.setVisibility(View.VISIBLE);
        rb_To_pregnant.setVisibility(View.VISIBLE);
        rb_To_Caregive.setVisibility(View.VISIBLE);
        rb_To_Depression.setVisibility(View.VISIBLE);
        rb_To_Chronic.setVisibility(View.VISIBLE);
        rb_To_None.setVisibility(View.VISIBLE);
    } private  void valuesForSenioreCitizen() {
        rb_To_Conceive.setVisibility(View.GONE);
        rb_To_pregnant.setVisibility(View.GONE);
        rb_To_Caregive.setVisibility(View.GONE);
        rb_To_Depression.setVisibility(View.VISIBLE);
        rb_To_Chronic.setVisibility(View.VISIBLE);
        rb_To_None.setVisibility(View.VISIBLE);
    }
}
