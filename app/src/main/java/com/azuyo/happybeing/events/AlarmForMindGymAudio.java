package com.azuyo.happybeing.events;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.PowerManager;

import com.azuyo.happybeing.Utils.AppConstants;
import com.azuyo.happybeing.Utils.MySql;
import com.azuyo.happybeing.Utils.ShowMindGymAudioNotification;
import com.azuyo.happybeing.ui.ProfileScreen;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Admin on 29-12-2016.
 */

public class AlarmForMindGymAudio extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        //Log.i("AlarmForMorning","Is activity visible "+Foreground.get().isForeground());
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();
        //Log.i("AlarmForMorning","In on receive of alarm for mindgym" );
        setResettingOfAudioFiles(context);
        setnowNotificationTrigger(context);
        setNextDayAlarm(context);
        wl.release();
    }

    private void setResettingOfAudioFiles(Context context) {
        MySql dbHelper = new MySql(context, "mydb", null, ProfileScreen.dbVersion);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int mingym_audio_nummber = 0;
        Cursor cursor = db.rawQuery("SELECT * FROM MindGymAudioCount", null);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            mingym_audio_nummber = cursor.getInt(cursor.getColumnIndex("MIND_GYM_AUDIO_COUNT"));
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("HAPPY_BEING", MODE_PRIVATE);
        String role = sharedPreferences.getString(AppConstants.ROLE, "");
        //Log.i("AlarmForMorning", "In set resetting of audio files "+role);
        if (role.contains("Student")) {
            //Log.i("AlarmForMorning", "In set resetting of audio files Student**");
            if (mingym_audio_nummber < DownloadMindGymAudioFilesService.mindGymForStudentsAudioNames.length - 1) {
                //Log.i("AlarmForMorning", "Student:Incrementing audio file number");
                mingym_audio_nummber++;
            }
            else {
                //Log.i("AlarmForMorning", "Student:Setting to zero");
                mingym_audio_nummber = 0;
            }
        }
        else if (role.contains("Expecting_Mom") || role.contains("Want_to_be_mom")) {
            //Log.i("AlarmForMorning", "In set resetting of audio files Expecting mom**");
            if (mingym_audio_nummber < DownloadMindGymAudioFilesService.mindGymForPregnecyWomenAdioNames.length -1) {
                mingym_audio_nummber++;
                //Log.i("AlarmForMorning", "Expecting_Mom:Incrementing audio file number");
            }
            else {
                //Log.i("AlarmForMorning", "Expecting_Mom:Setting to zero");
                mingym_audio_nummber = 0;
            }
        }
        else {
            //Log.i("AlarmForMorning", "In set resetting of audio files Others**");
            if (mingym_audio_nummber < DownloadMindGymAudioFilesService.mindGymForAdultsAudioListNames.length - 1) {
                mingym_audio_nummber++;
                //Log.i("AlarmForMorning", "Others:Incrementing audio file number");
            }
            else {
               // Log.i("AlarmForMorning", "Others:Setting to zero");
                mingym_audio_nummber = 0;
            }
        }

        ContentValues cv = new ContentValues();
        cv.put("MIND_GYM_AUDIO_COUNT", mingym_audio_nummber);
        long id = db.insert("MindGymAudioCount", null, cv);
        cursor.close();
        db.close();
    }

    private void setNextDayAlarm(Context context) {
        //Log.i("AlarmForMorning", "IN set next day alarm");
        int db_id = -1;
        Calendar calendar = new GregorianCalendar();
        long mindGymAffirmationsTime = 0;
        MySql dbHelper = new MySql(context, "mydb", null, ProfileScreen.dbVersion);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM NotificationsTimings", null);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            db_id = cursor.getInt(cursor.getColumnIndex("_id"));
            mindGymAffirmationsTime = cursor.getLong(cursor.getColumnIndex("MIND_GYM_START_TIME"));
        }
        else {
            //TODO: set mind gym to default time.
            mindGymAffirmationsTime = 0;
        }
        //Log.i("AlarmForMorning", "Mind gym affirmations time is "+mindGymAffirmationsTime);
        cursor.close();
        calendar.setTimeInMillis(mindGymAffirmationsTime);
        calendar.add(Calendar.DATE, 1);
        AlarmForMindGymAudio alarmForMindGymAudio = new AlarmForMindGymAudio();
        alarmForMindGymAudio.setAlarm(context, calendar.getTimeInMillis());
        ContentValues contentValues = new ContentValues();
        contentValues.put("MIND_GYM_START_TIME", calendar.getTimeInMillis());
        if (db_id != -1 ) {
            db.update("NotificationsTimings", contentValues, "_id="+db_id, null);
        }
        else {
            db.insert("NotificationsTimings", null, contentValues);
        }
        //Log.i("AlarmForMorning", "Setting alarm for "+calendar.getTimeInMillis());
        db.close();
    }

    public void setAlarm(Context context, long millisec)
    {
        //Log.i("AlarmForMorning", "IN set alarm "+millisec);
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        //Log.i("AlarmForMorning","In set alarm for Mind gym "+millisec);
        Intent notificationIntent = new Intent(context, AlarmForMindGymAudio.class);
        PendingIntent contentIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, 0);
        am.set(AlarmManager.RTC_WAKEUP, millisec, contentIntent);
    }

    public static void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, AlarmForMindGymAudio.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    private void setnowNotificationTrigger(Context context) {
        //Log.i("AlarmForMorning","In set alarms method");
        Intent notificationIntent = new Intent(context, ShowMindGymAudioNotification.class);
        PendingIntent contentIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),contentIntent);
    }

}