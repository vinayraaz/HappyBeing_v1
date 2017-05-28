package com.azuyo.happybeing.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.azuyo.happybeing.MindGym.PositivityAffirmations;
import com.azuyo.happybeing.R;
import com.azuyo.happybeing.ui.MainScreen;
import com.azuyo.happybeing.ui.MindGymScreen;
import com.azuyo.happybeing.ui.PaymentScreen;
import com.azuyo.happybeing.ui.SignUpActivity;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Admin on 07-12-2016.
 */

public class ShowMindGymAffirmationsNotification extends BroadcastReceiver {

    private final static String TAG = "ShowNotification";
    private Context context;
    public static int MindGym_Affirmations_NOTIFICATION_ID = 535;

    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.i("AlarmForEvening","In on receive of notification");
        SharedPreferences sharedPreferences = context.getSharedPreferences("HAPPY_BEING", MODE_PRIVATE);
        Intent mainIntent;
        boolean isSignedIn = sharedPreferences.getBoolean(AppConstants.IS_SIGNED_IN, false);
        boolean isPaid = sharedPreferences.getBoolean(AppConstants.IS_PAID, false);
        if (isSignedIn) {
            if (isPaid) {
                mainIntent = new Intent(context, PositivityAffirmations.class);
            }
            else {
                mainIntent = new Intent(context, PaymentScreen.class);
            }
        }
        else {
            mainIntent = new Intent(context, SignUpActivity.class);
        }
        MainScreen.workoutTypeString = "EVENING WORKOUT";
        MainScreen.workOutNameString = "AFFIRMATIONS";
        this.context = context;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.notification);
        Notification noti = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(context, MindGym_Affirmations_NOTIFICATION_ID, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentTitle("Today’s affirmations")
                .setSmallIcon(getNotificationIcon())
                .setLargeIcon(icon)
                .setWhen(System.currentTimeMillis())
                .build();

        notificationManager.notify(0, noti);
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.notification_whiteicon : R.drawable.app_logo;
    }

}
