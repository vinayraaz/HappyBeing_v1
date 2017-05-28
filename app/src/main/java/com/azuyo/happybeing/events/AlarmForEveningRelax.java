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
import com.azuyo.happybeing.Utils.ShowEveningNotification;
import com.azuyo.happybeing.ui.ProfileScreen;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Admin on 29-12-2016.
 */

public class AlarmForEveningRelax extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {

        //Log.i("Alarm","Is activity visible "+Foreground.get().isForeground());
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();
        //Log.i("Alarm","In on receive of alarm");
        setnowNotificationTrigger(context);
        setNetDayAlarm(context);
        wl.release();
    }

    private void setNetDayAlarm(Context c) {
        int db_id = -1;
        MySql dbHelper = new MySql(c, "mydb", null, ProfileScreen.dbVersion);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long mindGymAffirmationsTime = 0;
        Cursor cursor = db.rawQuery("SELECT * FROM NotificationsTimings", null);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            db_id = cursor.getInt(cursor.getColumnIndex("_id"));
            mindGymAffirmationsTime = cursor.getLong(cursor.getColumnIndex("RELAX_END_TIME"));
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(mindGymAffirmationsTime);
        calendar.add(Calendar.DATE, 1);
        AlarmForEveningRelax alarm = new AlarmForEveningRelax();
        alarm.setAlarm(c, calendar.getTimeInMillis());
        cursor.close();
        ContentValues contentValues = new ContentValues();
        contentValues.put("RELAX_END_TIME", calendar.getTimeInMillis());
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
        //Log.i("Alarm","In Set alarm method of eve alarm "+millisec);
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, AlarmForEveningRelax.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.set(AlarmManager.RTC_WAKEUP, millisec, pi); // Millisec * Second * Minute
    }

    private void setnowNotificationTrigger(Context context) {
        //Log.i("Alarm","In set alarms method");
        Intent notificationIntent = new Intent(context, ShowEveningNotification.class);
        PendingIntent contentIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, 0);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        // Set the alarm's trigger time to 12:00 .m.
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),contentIntent);
    }

    public static void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, AlarmForEveningRelax.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}