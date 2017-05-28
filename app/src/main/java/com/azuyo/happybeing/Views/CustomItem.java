package com.azuyo.happybeing.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.azuyo.happybeing.R;


/**
 * Created by kavya on 7/29/2015.
 */
public class CustomItem extends LinearLayout {

    private ImageView imageview;
    private TextView title_textview, info_textview;
    private Drawable src;
    private String titleName, info_text;

    public CustomItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrbs(context, attrs);
        init();
    }

    private void init()
    {
        inflate(getContext(), R.layout.custom_itemlayout, this);
        imageview = (ImageView)findViewById(R.id.emotion_image);
        title_textview = (TextView) findViewById(R.id.title_text);
        info_textview = (TextView) findViewById(R.id.info_text);

        imageview.setImageDrawable(src);
        title_textview.setText(titleName);
        info_textview.setText(info_text);
    }

    private void getAttrbs(Context context, AttributeSet attributeSet)
    {
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.Options, 0, 0);
        titleName = a.getString(R.styleable.Options_titleText);
        info_text = a.getString(R.styleable.Options_infoText);
        src = a.getDrawable(R.styleable.Options_imageScr);
        a.recycle();

    }

}
