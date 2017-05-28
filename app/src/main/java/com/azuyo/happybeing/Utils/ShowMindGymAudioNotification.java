package com.azuyo.happybeing.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.events.DownloadMindGymAudioFilesService;
import com.azuyo.happybeing.ui.ProfileScreen;
import com.azuyo.happybeing.ui.MainScreen;
import com.azuyo.happybeing.ui.PaymentScreen;
import com.azuyo.happybeing.ui.SignUpActivity;
import com.azuyo.happybeing.ui.TodayWorkOutScreen;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Admin on 07-12-2016.
 */

public class ShowMindGymAudioNotification extends BroadcastReceiver {

    private final static String TAG = "ShowNotification";
    private Context context;
    public static int MindGym_NOTIFICATION_ID = 35;
    private String name;
    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.i("AlarmForMorning","In on receive of notification");
        SharedPreferences sharedPreferences = context.getSharedPreferences("HAPPY_BEING", MODE_PRIVATE);
        Intent mainIntent;
        boolean isSignedIn = sharedPreferences.getBoolean(AppConstants.IS_SIGNED_IN, false);
        boolean isPaid = sharedPreferences.getBoolean(AppConstants.IS_PAID, false);
        String profile = sharedPreferences.getString(AppConstants.ROLE, "");
        MySql dbHelper = new MySql(context, "mydb", null, ProfileScreen.dbVersion);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int mingym_audio_nummber = 0;
        Cursor cursor = db.rawQuery("SELECT * FROM MindGymAudioCount", null);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            mingym_audio_nummber = cursor.getInt(cursor.getColumnIndex("MIND_GYM_AUDIO_COUNT"));
        }
        if (profile.equals("Student")) {
            name = DownloadMindGymAudioFilesService.mindGymForStudentsAudioNames[mingym_audio_nummber];
        }
        else if (profile.contains("Expecting_Mom") || profile.contains("Want_to_be_mom")){
            name = DownloadMindGymAudioFilesService.mindGymForPregnecyWomenAdioNames[mingym_audio_nummber];
        }
        else {
            name = DownloadMindGymAudioFilesService.mindGymForAdultsAudioListNames[mingym_audio_nummber];
        }
        MainScreen.workOutNameString = name;
        MainScreen.workoutTypeString = "MORNING WORKOUT";
        if (isSignedIn) {
            if (isPaid) {
                mainIntent = new Intent(context, TodayWorkOutScreen.class);
                mainIntent.putExtra("AUDIO_FILE_NAME", name);
                mainIntent.putExtra("AUDIO_FILE_NUMBER", mingym_audio_nummber);
            }
            else {
                mainIntent = new Intent(context, PaymentScreen.class);
            }
        }
        else {
            mainIntent = new Intent(context, SignUpActivity.class);
        }
        db.close();
        name = CommonUtils.repalceNumbers(name);
        this.context = context;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.app_logo);
        Notification noti = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(context, MindGym_NOTIFICATION_ID, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentTitle("Today’s workout: "+name)
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
