package com.azuyo.happybeing.Views;

import android.graphics.drawable.Drawable;

/**
 * Created by Admin on 01-03-2017.
 */

public class AudioFilesInfo {
    private Drawable imageSource;
    private String textView;
    private String audioFile;
    private int id, option_id;
    private boolean setNextImageVisibility = true;

    public AudioFilesInfo(Drawable imageSource, String textView, String audioFile) {
        this.imageSource = imageSource;
        this.textView = textView;
        this.audioFile = audioFile;
    }
    public AudioFilesInfo(int id, String textView) {
        this.textView = textView;
        this.id = id;
    }
    public AudioFilesInfo(int id, Drawable imageSource, String textView, String audioFile) {
        this.id = id;
        this.imageSource = imageSource;
        this.textView = textView;
        this.audioFile = audioFile;
    }
    public AudioFilesInfo(int id, Drawable imageSource, String textView, boolean visibility) {
        this.id = id;
        this.imageSource = imageSource;
        this.textView = textView;
        this.setNextImageVisibility = visibility;
    }
    public AudioFilesInfo(int id, int option_id, Drawable imageSource, String textView) {
        this.id = id;
        this.imageSource = imageSource;
        this.textView = textView;
        this.option_id = option_id;
    }

    public Drawable getImageSource() {
        return imageSource;
    }

    public void setImageSource(Drawable imageSource) {
        this.imageSource = imageSource;
    }

    public String getTextView() {
        return textView;
    }

    public void setTextView(String textView) {
        this.textView = textView;
    }

    public String getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSetNextImageVisibility() {
        return setNextImageVisibility;
    }

    public void setSetNextImageVisibility(boolean setNextImageVisibility) {
        this.setNextImageVisibility = setNextImageVisibility;
    }
}
