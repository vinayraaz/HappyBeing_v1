package com.azuyo.happybeing.ui;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.azuyo.happybeing.R;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by DELL on 20-04-17.
 */

public class HelpScreen extends BaseActivity implements View.OnClickListener {

    private RelativeLayout help1, help2, help3, help4, help5, help6, help7;
    private Button helpButton1, helpButton2, helpButton3, helpButton4, helpButton5, helpButton6, helpButton7;
    private String fromScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_activity);
        Intent intent = getIntent();
        if (intent.hasExtra("FROM_SCREEN")) {
            fromScreen = intent.getStringExtra("FROM_SCREEN");
        }
        help1 = (RelativeLayout) findViewById(R.id.help_1_layout);
        help2 = (RelativeLayout) findViewById(R.id.help_2_layout);
        help3 = (RelativeLayout) findViewById(R.id.help_3_layout);
        help4 = (RelativeLayout) findViewById(R.id.help_4_layout);
        help5 = (RelativeLayout) findViewById(R.id.help_5_layout);
        help6 = (RelativeLayout) findViewById(R.id.help_6_layout);
        help7 = (RelativeLayout) findViewById(R.id.help_7_layout);

        helpButton1 = (Button) findViewById(R.id.next_1);
        helpButton2 = (Button) findViewById(R.id.next_2);
        helpButton3 = (Button) findViewById(R.id.next_3);
        helpButton4 = (Button) findViewById(R.id.next_4);
        helpButton5 = (Button) findViewById(R.id.next_5);
        helpButton6 = (Button) findViewById(R.id.next_6);
        helpButton7 = (Button) findViewById(R.id.next_7);

        helpButton1.setOnClickListener(this);
        helpButton2.setOnClickListener(this);
        helpButton3.setOnClickListener(this);
        helpButton4.setOnClickListener(this);
        helpButton5.setOnClickListener(this);
        helpButton6.setOnClickListener(this);
        helpButton7.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.next_1:
                setLayoutVisibility(GONE, VISIBLE, GONE, GONE, GONE, GONE, GONE);
                break;
            case R.id.next_2:
                setLayoutVisibility(GONE, GONE, VISIBLE, GONE, GONE, GONE, GONE);
                break;
            case R.id.next_3:
                setLayoutVisibility(GONE, GONE, GONE, VISIBLE,GONE, GONE, GONE);
                break;
            case R.id.next_4:
                setLayoutVisibility(GONE, GONE, GONE, GONE, VISIBLE, GONE, GONE);
                break;
            case R.id.next_5:
                setLayoutVisibility(GONE, GONE, GONE, GONE, GONE, VISIBLE, GONE);
                break;
            case R.id.next_6:
                setLayoutVisibility(GONE, GONE, GONE, GONE, GONE, GONE, VISIBLE);
                break;
            case R.id.next_7:
                if (fromScreen != null && fromScreen.equals("HOME_SCREEN")) {
                    Intent intent = new Intent(this, NewHomeScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else {
                    finish();
                }
                break;
        }
    }

    private void setLayoutVisibility(int visibility1, int visibility2, int visibility3, int visibility4, int visibility5, int visibility6, int visibility7) {
        help1.setVisibility(visibility1);
        help2.setVisibility(visibility2);
        help3.setVisibility(visibility3);
        help4.setVisibility(visibility4);
        help5.setVisibility(visibility5);
        help6.setVisibility(visibility6);
        help7.setVisibility(visibility7);

    }
}
