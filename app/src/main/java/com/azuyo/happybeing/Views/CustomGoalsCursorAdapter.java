package com.azuyo.happybeing.Views;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.Utils.CommonUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CustomGoalsCursorAdapter extends CursorAdapter {
    LayoutInflater inflater;
    Context context;
    Cursor cursor;
    View view;

   public CustomGoalsCursorAdapter(Context context, Cursor c, int flags) {
       super(context, c,flags);
       this.context = context;
       this.cursor = c;
       inflater = (LayoutInflater) context.getSystemService(
               Context.LAYOUT_INFLATER_SERVICE);
   }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    @Override
    public void bindView(View view, final Context context, final Cursor cursor){
        final int dbid = cursor.getInt(cursor.getColumnIndex("_id"));
        ImageView imageView = (ImageView) view.findViewById(R.id.gratitude_icon);
        TextView timeView = (TextView)view.findViewById(R.id.time);
        TextView dateView = (TextView) view.findViewById(R.id.date);
        TextView titleView = (TextView) view.findViewById(R.id.title);

        String title = cursor.getString(cursor.getColumnIndex("TITLE"));
        titleView.setText(title);
        long timeinMillisec = cursor.getLong(cursor.getColumnIndex("TIME"));
        String date = getDate(timeinMillisec, "dd/MM/yyyy hh:mm:ss.SSS");
        dateView.setText(date);
        String timeString = DateFormat.format("hh:mm a", timeinMillisec).toString();
        timeView.setText(timeString);
        String imageUriString = cursor.getString(cursor.getColumnIndex("IMAGE_URI"));
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            CommonUtils.fillImageView(context, imageUri, imageView);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.gratitude_list_item, parent, false);
        this.cursor = cursor;
        bindView(view, context, cursor);
        return view;
    }

    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);
    }

    @Override
    public Cursor swapCursor(Cursor c) {
                return super.swapCursor(c);
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        String fulldate = formatter.format(calendar.getTime());
        String[] date = fulldate.split(" ");
        return date[0];
    }
}
