package com.azuyo.happybeing.Views;

import android.graphics.Bitmap;

/**
 * Created by Admin on 23-01-2017.
 */

public class CustomImageView {
    private int index, duration;
    private String title, description;
    private String playUrl;
    private Bitmap bitmap;
    public CustomImageView() {

    }

    public CustomImageView(int index, int duration, String title, String description, String playUrl) {
        this.duration = duration;
        this.index = index;
        this.description = description;
        this.title = title;
        this.playUrl = playUrl;
    }

    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
