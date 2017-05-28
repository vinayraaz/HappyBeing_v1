package com.azuyo.happybeing.Views;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.ui.OnItemSelectedListener;

import java.util.List;

public class CustomMindGymAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    Cursor cursor;
    View view;
    OnItemSelectedListener onItemSelectedListener;
    List<AudioFilesInfo> mList;
    AudioFilesInfo audioFilesInfo;
    static ProgressBar progressBar;
    static ImageView download_icon;
    public CustomMindGymAdapter(Context context, List<AudioFilesInfo> mList, OnItemSelectedListener onItemSelectedListener) {
        this.context = context;
        this.mList = mList;
        this.onItemSelectedListener = onItemSelectedListener;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Log.i("Tag","mALList size is "+mList.size());
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_mindgym_files_layout, parent, false);
            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.complete_layout);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            ImageView imageView = (ImageView) view.findViewById(R.id.option_icon);
            TextView title = (TextView) view.findViewById(R.id.option_title);
            download_icon = (ImageView) view.findViewById(R.id.download_icon);

            imageView.setBackground(mList.get(position).getImageSource());
            title.setText(mList.get(position).getTextView());

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemSelectedListener.onItemSelected(mList.get(position));
                }
            });
            download_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemSelectedListener.onDownloadButtonClicked(mList.get(position));
                }
            });
        }
        audioFilesInfo = mList.get(position);
        return view;
    }

    @Override
    public AudioFilesInfo getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public static void setProgressBarVisible(boolean visible) {
        if (visible) {
            progressBar.setVisibility(View.VISIBLE);
            download_icon.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            download_icon.setVisibility(View.VISIBLE);
        }

    }

}
