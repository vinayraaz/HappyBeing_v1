package com.azuyo.happybeing.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.ui.ComposeGratitudeDiary;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Admin on 07-12-2016.
 */

public class ShowGratitudeNotification extends BroadcastReceiver {

    private final static String TAG = "ShowNotification";
    private Context context;
    public static int GRATITUDE_NOTIFICATION_ID = 7;

    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.i("NotificationService","In on receive of notification");
        Intent mainIntent = new Intent(context, ComposeGratitudeDiary.class);
        this.context = context;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.app_logo);
        Notification noti = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(context, GRATITUDE_NOTIFICATION_ID, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentTitle("Lets express gratitude for the day")
                .setSmallIcon(R.drawable.app_logo)
                .setLargeIcon(icon)
                .setWhen(System.currentTimeMillis())
                .build();

        notificationManager.notify(0, noti);
    }
}
