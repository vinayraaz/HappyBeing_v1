package com.azuyo.happybeing.ui;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.Utils.AppConstants;
import com.azuyo.happybeing.Utils.MySql;
import com.azuyo.happybeing.events.Alarm;
import com.azuyo.happybeing.events.AlarmForEveningRelax;
import com.azuyo.happybeing.events.AlarmForMindGymAffirmations;
import com.azuyo.happybeing.events.AlarmForMindGymAudio;
import com.azuyo.happybeing.fourmob.timepicker.RadialPickerLayout;
import com.azuyo.happybeing.fourmob.timepicker.TimePickerDialog;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MyProfileLayout extends BaseActivity implements View.OnClickListener {
    private Button  signUp, signIn,signOutButton;
    private TextView dailyGymStartTime, dailyGymEndTime, relaxStartTime, relaxEndTime, saveText,Back_Button;
    public int final_start_hour, final_end_hour, final_start_min, final_end_min, final_start_year, final_start_AM_Or_PM,
            final_end_year, final_end_AM_OR_PM, final_start_month,final_end_month, final_start_dayOfMonth,final_end_dayOfMonth;
    public int relax_final_start_hour, relax_final_end_hour, relax_final_start_min, relax_final_end_min, relax_final_start_year,
            relax_final_start_AM_Or_PM, relax_final_end_year, relax_final_end_AM_OR_PM, relax_final_start_month,
            relax_final_end_month, relax_final_start_dayOfMonth, relax_final_end_dayOfMonth;
    private  View divider_line;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private boolean dailyGymStartIsChanged = false, dailyGymEndIsChanged = false, relaxStartIsChaged = false, relaxEndIsChanged = false;
    private SQLiteDatabase db;
    private int id = 0;
    private LinearLayout Premium_linear,Signup_signin_linear_button;
    String user_login="false";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(false);

        sharedPreferences = getSharedPreferences("HAPPY_BEING", MODE_PRIVATE);
        user_login = sharedPreferences.getString("user_login","");

        signOutButton = (Button) findViewById(R.id.sign_out_button);
        divider_line = findViewById(R.id.divider_line);
        dailyGymStartTime = (TextView) findViewById(R.id.daily_gym_start_time);
        dailyGymEndTime = (TextView) findViewById(R.id.daily_gym_end_time);
        relaxStartTime = (TextView) findViewById(R.id.relax_start_time);
        relaxEndTime = (TextView) findViewById(R.id.relax_end_time);
        saveText = (TextView) findViewById(R.id.saveText);
        Back_Button = (TextView) findViewById(R.id.back_button);
        Premium_linear = (LinearLayout) findViewById(R.id.linear_third);
        Signup_signin_linear_button = (LinearLayout) findViewById(R.id.linear_second);

        signUp = (Button) findViewById(R.id.sign_up_button);
        signIn = (Button) findViewById(R.id.sign_in_button);
        Signup_signin_linear_button.setVisibility(View.GONE);
        MySql dbHelper = new MySql(this, "mydb", null, ProfileScreen.dbVersion);
        db = dbHelper.getWritableDatabase();

        initTimeVariables();
        boolean isSignedIn = sharedPreferences.getBoolean(AppConstants.IS_SIGNED_IN, false);
        System.out.println("userlogin*****"+isSignedIn);
        if (isSignedIn){
            Signup_signin_linear_button.setVisibility(View.GONE);
            Premium_linear.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.VISIBLE);
            divider_line.setVisibility(View.GONE);
        }else {

            Signup_signin_linear_button.setVisibility(View.GONE);
            Premium_linear.setVisibility(View.GONE);
        }

        signOutButton.setOnClickListener(this);
        dailyGymStartTime.setOnClickListener(this);
        dailyGymEndTime.setOnClickListener(this);
        relaxStartTime.setOnClickListener(this);
        relaxEndTime.setOnClickListener(this);
        saveText.setOnClickListener(this);
        signIn.setOnClickListener(this);
        signUp.setOnClickListener(this);
        Back_Button.setOnClickListener(this);
        updateTime(dailyGymStartTime, final_start_hour, final_start_min, final_start_AM_Or_PM);
        updateTime(dailyGymEndTime, final_end_hour, final_end_min, final_end_AM_OR_PM);

        updateTime(relaxStartTime, relax_final_start_hour, relax_final_start_min, relax_final_start_AM_Or_PM);
        updateTime(relaxEndTime, relax_final_end_hour, relax_final_end_min, relax_final_end_AM_OR_PM);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.text_item_menu, menu);
        MenuItem actionOne = menu.findItem(R.id.text_item);
        actionOne.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        SharedPreferences sharedPreferences = getSharedPreferences("HAPPY_BEING", MODE_PRIVATE);
        final SharedPreferences.Editor editor= sharedPreferences.edit();
        int id = v.getId();
        final Calendar calendar = Calendar.getInstance();
        final String DATEPICKER_TAG = "datepicker";
        final String TIMEPICKER_TAG = "timepicker";
        switch (id) {
            case R.id.sign_out_button:
                final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        FirebaseAuth.getInstance().signOut();
                        editor.putString("user_login", "false");
                        editor.putBoolean(AppConstants.IS_SIGNED_IN, false);
                        editor.commit();
                        signOutButton.setVisibility(View.GONE);
                        divider_line.setVisibility(View.GONE);
                        Signup_signin_linear_button.setVisibility(View.VISIBLE);
                        Premium_linear.setVisibility(View.GONE);
                    }
                }, 1000);

                break;
            case R.id.daily_gym_start_time:
                final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                        final_start_hour = hourOfDay;
                        final_start_min = minute;
                        dailyGymStartIsChanged = true;
                        updateTime(dailyGymStartTime, hourOfDay, minute, -1);
                        //updateTime(end_time, endHourOfDay, endMinute);
                    }
                }, final_start_hour, final_start_min, false, false);
                timePickerDialog.setVibrate(true);
                timePickerDialog.setCloseOnSingleTapMinute(false);
                timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);
                break;
            case R.id.daily_gym_end_time:
                final TimePickerDialog timePickerDialog2 = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                        final_end_hour = hourOfDay;
                        final_end_min = minute;
                        dailyGymEndIsChanged = true;
                        updateTime(dailyGymEndTime, hourOfDay, minute, -1);
                    }
                }, final_end_hour, final_end_min, false, false);
                timePickerDialog2.setVibrate(true);
                timePickerDialog2.setCloseOnSingleTapMinute(false);
                timePickerDialog2.show(getSupportFragmentManager(), TIMEPICKER_TAG);
                break;
            case R.id.relax_start_time:
                final TimePickerDialog timePickerDialog3 = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                        relax_final_start_hour = hourOfDay;
                        relax_final_start_min = minute;
                        relaxStartIsChaged = true;
                        updateTime(relaxStartTime, hourOfDay, minute, -1);
                    }
                }, relax_final_start_hour, relax_final_start_min, false, false);
                timePickerDialog3.setVibrate(true);
                timePickerDialog3.setCloseOnSingleTapMinute(false);
                timePickerDialog3.show(getSupportFragmentManager(), TIMEPICKER_TAG);
                break;
            case R.id.relax_end_time:
                final TimePickerDialog timePickerDialog4 = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                        relax_final_end_hour = hourOfDay;
                        relax_final_end_min = minute;
                        relaxEndIsChanged = true;
                        updateTime(relaxEndTime, hourOfDay, minute, -1);
                    }
                }, relax_final_end_hour, relax_final_end_min, false, false);
                timePickerDialog4.setVibrate(true);
                timePickerDialog4.setCloseOnSingleTapMinute(false);
                timePickerDialog4.show(getSupportFragmentManager(), TIMEPICKER_TAG);
                break;
            case R.id.saveText:
                if (user_login.equalsIgnoreCase("true")){
                    saveToPreferences();
                    Toast.makeText(this, "Updated changes", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    android.support.v7.app.AlertDialog.Builder builder1 = new
                            android.support.v7.app.AlertDialog.Builder(MyProfileLayout.this);
                    builder1.setTitle("Happy Being");
                    builder1.setMessage("Do you want signIn ?.");
                    builder1.setCancelable(false);
                    builder1.setPositiveButton(
                            "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    Intent i2 = new Intent(MyProfileLayout.this, SignInActivity.class);
                                    startActivity(i2);
                                    finish();
                                }
                            });
                    builder1.setNegativeButton(
                            "NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                }
                            })
                    ;
                    android.support.v7.app.AlertDialog alert11 = builder1.create();
                    alert11.show();
                }

                break;
            //signup& signin

            case R.id.sign_in_button:
                Intent intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
                break;
            case R.id.sign_up_button:
                Intent intent1 = new Intent(this, SignUpActivity.class);
                intent1.putExtra("TYPE","SIGN_UP");
                startActivity(intent1);
                break;
            case R.id.back_button:
                Intent back_intent = new Intent(this, NewHomeScreen.class);
                startActivity(back_intent);
                finish();
        }
    }

    private void saveToPreferences() {
        ContentValues cv = new ContentValues();
        if (dailyGymStartIsChanged) {
            Calendar calender = new GregorianCalendar(final_start_year, final_start_month, final_start_dayOfMonth, final_start_hour, final_start_min);
            long dailyGymStartTime = calender.getTimeInMillis();
            cv.put("MIND_GYM_START_TIME", dailyGymStartTime);
        }
        if (dailyGymEndIsChanged) {
            Calendar calender1 = new GregorianCalendar(final_end_year, final_end_month, final_end_dayOfMonth, final_end_hour, final_end_min);
            long dailyGymEndTime = calender1.getTimeInMillis();
            cv.put("MIND_GYM_END_TIME", dailyGymEndTime);
        }
        if (relaxStartIsChaged) {
            Calendar calender2 = new GregorianCalendar(relax_final_start_year, relax_final_start_month, relax_final_start_dayOfMonth, relax_final_start_hour, relax_final_start_min);
            long relaxStartTime = calender2.getTimeInMillis();
            cv.put("RELAX_START_TIME", relaxStartTime);
        }
        if (relaxEndIsChanged) {
            Calendar calender3 = new GregorianCalendar(relax_final_end_year, relax_final_end_month, relax_final_end_dayOfMonth, relax_final_end_hour, relax_final_end_min);
            long relaxEndTime = calender3.getTimeInMillis();
            cv.put("RELAX_END_TIME", relaxEndTime);
        }
        if (dailyGymEndIsChanged || dailyGymStartIsChanged || relaxStartIsChaged || relaxEndIsChanged) {
            int count = db.update("NotificationsTimings", cv, "_id=" + id, null);
        }
        scheduleAlarms();
    }

    public void initTimeVariables() {
        Cursor cursor = db.rawQuery("SELECT * FROM NotificationsTimings", null);
        long dailyGymTime = 0, dailyGymEndTime = 0, relaxStartTime = 0, relaxEndTime = 0;
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            id = cursor.getInt(cursor.getColumnIndex("_id"));
            dailyGymTime = cursor.getLong(cursor.getColumnIndex("MIND_GYM_START_TIME"));
            dailyGymEndTime = cursor.getLong(cursor.getColumnIndex("MIND_GYM_END_TIME"));
            relaxStartTime = cursor.getLong(cursor.getColumnIndex("RELAX_START_TIME"));
            relaxEndTime = cursor.getLong(cursor.getColumnIndex("RELAX_END_TIME"));
        }
        //Log.i("SettingsAlarm", "INIT:::::::: daiily gym time "+dailyGymTime);
        //Log.i("SettingsAlarm", "INIT:::::::: daiily gym end time "+dailyGymEndTime);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(dailyGymTime);
        final_start_hour = calendar1.get(Calendar.HOUR);
        final_start_min = calendar1.get(Calendar.MINUTE);
        final_start_month = calendar1.get(Calendar.MONTH);
        calendar1.add(Calendar.DAY_OF_MONTH, -1);
        final_start_dayOfMonth = calendar1.get(Calendar.DAY_OF_MONTH);
        //Log.i("Settings","Day is "+final_start_dayOfMonth);
        final_start_year = calendar1.get(Calendar.YEAR);
        final_start_AM_Or_PM = calendar1.get(Calendar.AM_PM);

        calendar1.setTimeInMillis(dailyGymEndTime);
        final_end_hour = calendar1.get(Calendar.HOUR);
        final_end_min = calendar1.get(Calendar.MINUTE);
        final_end_month = calendar1.get(Calendar.MONTH);
        calendar1.add(Calendar.DAY_OF_MONTH, -1);
        final_end_dayOfMonth = calendar1.get(Calendar.DAY_OF_MONTH);
        final_end_year = calendar1.get(Calendar.YEAR);
        final_end_AM_OR_PM = calendar1.get(Calendar.AM_PM);

        calendar1.setTimeInMillis(relaxStartTime);
        relax_final_start_hour = calendar1.get(Calendar.HOUR);
        relax_final_start_min = calendar1.get(Calendar.MINUTE);
        relax_final_start_month = calendar1.get(Calendar.MONTH);
        calendar1.add(Calendar.DAY_OF_MONTH, -1);
        relax_final_start_dayOfMonth = calendar1.get(Calendar.DAY_OF_MONTH);
        relax_final_start_year = calendar1.get(Calendar.YEAR);
        relax_final_start_AM_Or_PM = calendar1.get(Calendar.AM_PM);

        calendar1.setTimeInMillis(relaxEndTime);
        relax_final_end_hour = calendar1.get(Calendar.HOUR);
        relax_final_end_min = calendar1.get(Calendar.MINUTE);
        relax_final_end_month = calendar1.get(Calendar.MONTH);
        calendar1.add(Calendar.DAY_OF_MONTH, -1);
        relax_final_end_dayOfMonth = calendar1.get(Calendar.DAY_OF_MONTH);
        relax_final_end_year = calendar1.get(Calendar.YEAR);
        relax_final_end_AM_OR_PM = calendar1.get(Calendar.AM_PM);
    }

    public void updateTime(TextView v, int hours, int mins, int amOrPm) {
        // Log.i("Settings", "Hours is "+hours+" Min is "+mins+" am or pm is "+amOrPm);
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        }
        else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        if (amOrPm != -1) {
            if (amOrPm == 0) {
                timeSet = "AM";
            } else {
                timeSet = "PM";
            }
        }

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        String aTime = new
                StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();
        v.setText(aTime);
    }

    private void scheduleAlarms() {
        ContentValues contentValues = new ContentValues();
        boolean canUpdate = false;
        // Log.i("SettingsAlarm", "IN schedule alarms");
        Cursor cursor = db.rawQuery("SELECT * FROM NotificationsTimings", null);
        long dailyGymTime = 0, dailyGymEndTime = 0, relaxStartTime = 0, relaxEndTime = 0;
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            id = cursor.getInt(cursor.getColumnIndex("_id"));
            dailyGymTime = cursor.getLong(cursor.getColumnIndex("MIND_GYM_START_TIME"));
            dailyGymEndTime = cursor.getLong(cursor.getColumnIndex("MIND_GYM_END_TIME"));
            relaxStartTime = cursor.getLong(cursor.getColumnIndex("RELAX_START_TIME"));
            relaxEndTime = cursor.getLong(cursor.getColumnIndex("RELAX_END_TIME"));
        }
        AlarmForMindGymAffirmations.cancelAlarm(this);
        AlarmForMindGymAudio.cancelAlarm(this);
        Alarm.cancelAlarm(this);
        //Alarm for 6 am
        // Log.i("SettingsAlarm", "Mind Gym audio ::::CUrrent time "+System.currentTimeMillis()+" 8 am time "+dailyGymTime);
        if(dailyGymTime > System.currentTimeMillis()) {
            //Log.i("SettingsAlarm","MindGym Audio ::::::IN if loop");
            AlarmForMindGymAudio alarmForMindGymAudio = new AlarmForMindGymAudio();
            alarmForMindGymAudio.setAlarm(this, dailyGymTime);
        }
        else {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTimeInMillis(dailyGymTime);
            calendar1.add(Calendar.DATE, 1);
            //Log.i("SettingsAlarm", "MindGym Audio ::::::In else loop"+calendar1.getTimeInMillis());
            AlarmForMindGymAudio alarmForMindGymAudio = new AlarmForMindGymAudio();
            alarmForMindGymAudio.setAlarm(this, calendar1.getTimeInMillis());
            contentValues.put("MIND_GYM_START_TIME", calendar1.getTimeInMillis());
            canUpdate = true;
        }

        //Alarm for 9 30 pm
        Calendar calendar2 = new GregorianCalendar();
        //Log.i("SettingsAlarm", "CUrrent time "+System.currentTimeMillis()+" 8 am time "+dailyGymEndTime);
        if(dailyGymEndTime > System.currentTimeMillis()) {
            // Log.i("SettingsAlarm", "MindGym Affirmations ::::::IN if loop");
            AlarmForMindGymAffirmations alarmForMindGymAffirmations = new AlarmForMindGymAffirmations();
            alarmForMindGymAffirmations.setAlarm(this, dailyGymEndTime);
        }
        else {
            calendar2.setTimeInMillis(dailyGymEndTime);
            calendar2.add(Calendar.DATE, 1);
            //Log.i("SettingsAlarm", "MindGym Affirmations ::::::IN else loop"+calendar2.getTimeInMillis());
            AlarmForMindGymAffirmations alarmForMindGymAffirmations = new AlarmForMindGymAffirmations();
            alarmForMindGymAffirmations.setAlarm(this, calendar2.getTimeInMillis());
            contentValues.put("MIND_GYM_END_TIME", calendar2.getTimeInMillis());
            canUpdate = true;
        }

        //Relax start time
        //Log.i("Alarm", "Relax start time is "+relaxStartTime);
        if (relaxStartTime > System.currentTimeMillis()) {
            //Log.i("Alarm", "Relax start time::::: In if loop "+relaxStartTime);
            Alarm alarm = new Alarm();
            alarm.setAlarm(this, relaxStartTime);
        }
        else {
            Calendar calendar1 = new GregorianCalendar();
            calendar1.setTimeInMillis(relaxStartTime);
            calendar1.add(Calendar.DATE, 1);
            Alarm alarm = new Alarm();
            alarm.setAlarm(this, calendar1.getTimeInMillis());
            //Log.i("Alarm", "Relax start time::::: In else loop "+calendar1.getTimeInMillis());
            contentValues.put("RELAX_START_TIME", calendar1.getTimeInMillis());
            canUpdate = true;
        }

        if (relaxEndTime > System.currentTimeMillis()) {
            // Log.i("SettingsAlarm", "Relax End time::::: In if loop "+relaxEndTime);
            AlarmForEveningRelax alarmEnd = new AlarmForEveningRelax();
            alarmEnd.setAlarm(this, relaxEndTime);
        }
        else {
            Calendar calendar1 = new GregorianCalendar();
            calendar1.setTimeInMillis(relaxEndTime);
            calendar1.add(Calendar.DATE, 1);
            AlarmForEveningRelax alarmEnd = new AlarmForEveningRelax();
            alarmEnd.setAlarm(this, calendar1.getTimeInMillis());
            contentValues.put("RELAX_END_TIME", calendar1.getTimeInMillis());
            canUpdate = true;
            //Log.i("SettingsAlarm", "Relax End time::::: In else loop "+calendar1.getTimeInMillis());
        }
        if (canUpdate) {
            db.update("NotificationsTimings", contentValues, "_id=" + id, null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isSignedIn = sharedPreferences.getBoolean(AppConstants.IS_SIGNED_IN, false);
        System.out.println("userlogin*****"+isSignedIn);
        if (isSignedIn){
            Signup_signin_linear_button.setVisibility(View.GONE);
            Premium_linear.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.VISIBLE);
            divider_line.setVisibility(View.GONE);

        }else {


            Signup_signin_linear_button.setVisibility(View.VISIBLE);
            Premium_linear.setVisibility(View.GONE);
        }
    }
}