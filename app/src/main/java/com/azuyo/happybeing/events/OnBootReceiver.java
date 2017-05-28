package com.azuyo.happybeing.events;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.azuyo.happybeing.Utils.MySql;
import com.azuyo.happybeing.ui.ProfileScreen;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class OnBootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(final Context context, Intent intent) {
        scheduleAlarms(context);
	}

    private void scheduleAlarms(Context context) {
        // Log.i("SettingsAlarm", "IN schedule alarms");
        AlarmForMindGymAffirmations.cancelAlarm(context);
        AlarmForMindGymAudio.cancelAlarm(context);
        Alarm.cancelAlarm(context);
        long mindgymAudioTime = 0, mindGymAffirmationsTime = 0, relaxStartTime = 0, relaxEndTime = 0;
        //Alarm for 6 am
        //Log.i("SettingsAlarm", "Mind Gym audio ::::CUrrent time "+System.currentTimeMillis()+" 8 am time "+mindgymAudioTime);
        MySql dbHelper = new MySql(context, "mydb", null, ProfileScreen.dbVersion);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM NotificationsTimings", null);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            mindgymAudioTime = cursor.getLong(cursor.getColumnIndex("MIND_GYM_START_TIME"));
            mindGymAffirmationsTime = cursor.getLong(cursor.getColumnIndex("MIND_GYM_END_TIME"));
            relaxStartTime = cursor.getLong(cursor.getColumnIndex("RELAX_START_TIME"));
            relaxEndTime = cursor.getLong(cursor.getColumnIndex("RELAX_END_TIME"));
        }
        else {
            //TODO: set mind gym to default time.
        }
        cursor.close();
        db.close();

        if(mindgymAudioTime > System.currentTimeMillis()) {
            //Log.i("SettingsAlarm","MindGym Audio ::::::IN if loop");
            AlarmForMindGymAudio alarmForMindGymAudio = new AlarmForMindGymAudio();
            alarmForMindGymAudio.setAlarm(context, mindgymAudioTime);
        }
        else {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTimeInMillis(mindgymAudioTime);
            calendar1.add(Calendar.DATE, 1);
            //Log.i("SettingsAlarm", "MindGym Audio ::::::In else loop"+calendar1.getTimeInMillis());
            AlarmForMindGymAudio alarmForMindGymAudio = new AlarmForMindGymAudio();
            alarmForMindGymAudio.setAlarm(context, calendar1.getTimeInMillis());
        }

        //Alarm for 9 30 pm
        Calendar calendar2 = new GregorianCalendar();
        ///Log.i("SettingsAlarm", "CUrrent time "+System.currentTimeMillis()+" 8 am time "+mindGymAffirmationsTime);
        if(mindGymAffirmationsTime > System.currentTimeMillis()) {
            // Log.i("SettingsAlarm", "MindGym Affirmations ::::::IN if loop");
            AlarmForMindGymAffirmations alarmForMindGymAffirmations = new AlarmForMindGymAffirmations();
            alarmForMindGymAffirmations.setAlarm(context, mindGymAffirmationsTime);
        }
        else {
            calendar2.setTimeInMillis(mindGymAffirmationsTime);
            calendar2.add(Calendar.DATE, 1);
            //Log.i("SettingsAlarm", "MindGym Affirmations ::::::IN else loop"+calendar2.getTimeInMillis());
            AlarmForMindGymAffirmations alarmForMindGymAffirmations = new AlarmForMindGymAffirmations();
            alarmForMindGymAffirmations.setAlarm(context, calendar2.getTimeInMillis());
        }

        //Relax start time
        if (relaxStartTime > System.currentTimeMillis()) {
            //Log.i("SettingsAlarm", "Relax start time::::: In if loop "+relaxStartTime);
            Alarm alarm = new Alarm();
            alarm.setAlarm(context, relaxStartTime);
        }
        else {
            Calendar calendar1 = new GregorianCalendar();
            calendar1.setTimeInMillis(relaxStartTime);
            calendar1.add(Calendar.DATE, 1);
            Alarm alarm = new Alarm();
            alarm.setAlarm(context, calendar1.getTimeInMillis());
            //Log.i("SettingsAlarm", "Relax start time::::: In else loop "+calendar1.getTimeInMillis());
        }

        if (relaxEndTime > System.currentTimeMillis()) {
            // Log.i("SettingsAlarm", "Relax End time::::: In if loop "+relaxEndTime);
            Alarm alarmEnd = new Alarm();
            alarmEnd.setAlarm(context, relaxEndTime);
        }
        else {
            Calendar calendar1 = new GregorianCalendar();
            calendar1.setTimeInMillis(relaxEndTime);
            calendar1.add(Calendar.DATE, 1);
            Alarm alarmEnd = new Alarm();
            alarmEnd.setAlarm(context, calendar1.getTimeInMillis());
            //Log.i("SettingsAlarm", "Relax End time::::: In else loop "+calendar1.getTimeInMillis());
        }
    }

}