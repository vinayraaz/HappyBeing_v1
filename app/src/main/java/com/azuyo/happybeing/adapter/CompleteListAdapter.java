package com.azuyo.happybeing.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.ui.AudioScreen;
import com.azuyo.happybeing.ui.ComleteListActivity;

/**
 * Created by nSmiles on 5/24/2017.
 */

public class CompleteListAdapter extends RecyclerView.Adapter<CompleteListAdapter.MyViewHolder> {
    String[] text_value_list;
    int image_value_list[];
    String[] relaxAndRelieveFiles_new;
    Context context;
    String tv_event_name,tv_audio_name,tv_event_name1,tv_audio_name1;
    int image_name,image_name1;
    public CompleteListAdapter(Context context, String[] text_value_list, int[] image_value_list, String[] relaxAndRelieveFiles) {
        this.context = context;
        this.text_value_list = text_value_list;
        this.image_value_list = image_value_list;
        this.relaxAndRelieveFiles_new = relaxAndRelieveFiles;
    }

    @Override
    public CompleteListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.complete_list_details_adapter,parent,false);
        CompleteListAdapter.MyViewHolder completeHolder  = new CompleteListAdapter.MyViewHolder(view);

        return completeHolder;
    }

    @Override
    public void onBindViewHolder(CompleteListAdapter.MyViewHolder holder, final int position) {
         tv_event_name = text_value_list[position];
         tv_audio_name = relaxAndRelieveFiles_new[position];
         image_name = image_value_list[position];
        holder.Text_Value.setText(tv_event_name);
        holder.Audio_Value.setText(tv_audio_name);
        holder.Image_Value.setImageResource(image_name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AudioScreen.class);
                tv_event_name1 = text_value_list[position];
                tv_audio_name1 = relaxAndRelieveFiles_new[position];
                image_name1 = image_value_list[position];

                intent.putExtra("TITLE", tv_event_name1);
                intent.putExtra("AUDIO_FILE", tv_audio_name1);
                intent.putExtra("AUDIO_FILE_NUMBER", 3);
                System.out.println("tv_event_name"+tv_event_name1);
                System.out.println("tv_audio_name1"+tv_audio_name1);
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return text_value_list.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView Text_Value,Audio_Value;
        private ImageView Image_Value;
        public MyViewHolder(View itemView) {
            super(itemView);
            Image_Value = (ImageView)itemView.findViewById(R.id.image_value);
            Text_Value = (TextView)itemView.findViewById(R.id.text_value);
            Audio_Value = (TextView)itemView.findViewById(R.id.audio_value);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
