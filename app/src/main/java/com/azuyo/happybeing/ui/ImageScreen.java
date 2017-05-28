package com.azuyo.happybeing.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.azuyo.happybeing.R;

/**
 * Created by vinay on 24-05-2017.
 */

public class ImageScreen extends BaseActivity {
    ImageView image_view;
    String text_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_image_screen);
        image_view = (ImageView) findViewById(R.id.image_view);
        Intent intent = getIntent();
        if (intent.hasExtra("IMAGE_TITLE")) {
            text_title = intent.getStringExtra("IMAGE_TITLE");
            System.out.println("ImageScreen.text_title "+text_title);
        }

       /* Intent intent = getIntent();
        if (intent.hasExtra("IMAGE_ID")) {
            int id = intent.getIntExtra("IMAGE_ID", 0);
            if (id != 0) {
                image_view.setBackground(getResources().getDrawable(id));
            }
        }*/
    }
}
