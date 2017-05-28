package com.azuyo.happybeing.events;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.PowerManager;

import com.azuyo.happybeing.Utils.MySql;
import com.azuyo.happybeing.Utils.ShowMindGymAffirmationsNotification;
import com.azuyo.happybeing.ui.ProfileScreen;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Admin on 29-12-2016.
 */

public class AlarmForMindGymAffirmations extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        //Log.i("AlarmForEvening","Is activity visible "+Foreground.get().isForeground());
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();
        //Log.i("AlarmForEvening","In on receive of alarm for mindgym" );
        setnowNotificationTrigger(context);
        setNextDayAlarm(context);
        wl.release();
    }

    private void setNextDayAlarm(Context c) {
        int db_id = -1;
        MySql dbHelper = new MySql(c, "mydb", null, ProfileScreen.dbVersion);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long mindGymAffirmationsTime = 0;
        Cursor cursor = db.rawQuery("SELECT * FROM NotificationsTimings", null);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            db_id = cursor.getInt(cursor.getColumnIndex("_id"));
            mindGymAffirmationsTime = cursor.getLong(cursor.getColumnIndex("MIND_GYM_END_TIME"));
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(mindGymAffirmationsTime);
        calendar.add(Calendar.DATE, 1);
        AlarmForMindGymAffirmations alarmForMindGymAffirmations = new AlarmForMindGymAffirmations();
        alarmForMindGymAffirmations.setAlarm(c, calendar.getTimeInMillis());
        cursor.close();
        ContentValues contentValues = new ContentValues();
        contentValues.put("MIND_GYM_END_TIME", calendar.getTimeInMillis());
        if (db_id != -1 ) {
            db.update("NotificationsTimings", contentValues, "_id="+db_id, null);
        }
        else {
            db.insert("NotificationsTimings", null, contentValues);
        }
        db.close();
    }

    public void setAlarm(Context context, long millisec)
    {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
       // Log.i("AlarmForEvening","In set alarm for Mind gym affirmations  "+millisec);
        Intent notificationIntent = new Intent(context, AlarmForMindGymAffirmations.class);
        PendingIntent contentIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, 0);
        am.set(AlarmManager.RTC_WAKEUP, millisec, contentIntent);
    }

    public static void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, AlarmForMindGymAffirmations.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    private void setnowNotificationTrigger(Context context) {
        //Log.i("NotificationTrigger","In set alarms method");
        Intent notificationIntent = new Intent(context, ShowMindGymAffirmationsNotification.class);
        PendingIntent contentIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, 0);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        // Set the alarm's trigger time to 12:00 .m.
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),contentIntent);
    }



}