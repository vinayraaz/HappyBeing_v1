package com.azuyo.happybeing.Utils;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.azuyo.happybeing.R;

/**
 * Created by saulmm on 16/02/16.
 */
public class OnBoardingWithSimpleBackground extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Fake a long startup time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setTheme(R.style.AppTheme);
                setContentView(R.layout.activity_lancher);
            }

        }, 500);


    }
}
