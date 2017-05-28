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

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.ui.ProfileScreen;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Admin on 07-12-2016.
 */

public class ShowEveningNotification extends BroadcastReceiver {

    private final static String TAG = "ShowNotification";
    private Context context;
    public static int FEELING_NOTIFICATION_ID = 588;
    Bitmap icon;
    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.i("NotificationService","In on receive of notification");
        Intent mainIntent = new Intent(context, ProfileScreen.class);
        this.context = context;
        icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.app_logo);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Intent remindMeIntent = new Intent("REMIND_ME");
        Notification noti = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(PendingIntent.getActivity(context, 1, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentTitle("Get some positive boost")
                .setSmallIcon(getNotificationIcon())
                .setLargeIcon(icon)
                .setWhen(System.currentTimeMillis())
                .build();
        notificationManager.notify(FEELING_NOTIFICATION_ID, noti);
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.notification_whiteicon : R.drawable.app_logo;
    }
}
