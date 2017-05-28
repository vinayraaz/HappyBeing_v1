package com.azuyo.happybeing.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.Utils.AppConstants;
import com.azuyo.happybeing.Utils.CommonUtils;
import com.azuyo.happybeing.Utils.HideKeyboardActivity;
import com.azuyo.happybeing.Utils.MySql;
import com.azuyo.happybeing.Utils.PrefManager;
import com.azuyo.happybeing.Views.AudioFilesInfo;
import com.azuyo.happybeing.Views.CustomRelaxAdapter;
import com.azuyo.happybeing.Views.SmartAudioPlayer;
import com.azuyo.happybeing.events.Alarm;
import com.azuyo.happybeing.events.AlarmForEveningRelax;
import com.azuyo.happybeing.events.AlarmForMindGymAffirmations;
import com.azuyo.happybeing.events.AlarmForMindGymAudio;
import com.azuyo.happybeing.events.DownloadAudioFilesService;
import com.azuyo.happybeing.events.DownloadMindGymAudioFilesService;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class ProfileScreen extends HideKeyboardActivity implements View.OnClickListener, OnItemSelectedListener {

    public static SmartAudioPlayer mediaPlayer;
    public static MenuItem menuItem;
    public static int downloadedFileCount = 0, downloadMindGymAudioFileCount = 0;
    public static DownloadAudioFilesService downloadAudioFilesService;
    public static int dbVersion = 4;
    SharedPreferences.Editor editor;
    private SQLiteDatabase db;

    private ListView listView;
    private CustomRelaxAdapter customAudioFilesAdapter;
    private List<AudioFilesInfo> list;
    private Button Let_Get_Start;
    private LinearLayout Student,Looking_Job,Employed,Staff,Home_Maker,Senior_Citizen;
    String user_role ="";
    private boolean student_selected = false;
    private PrefManager pref = null;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_profile_screen);


        MySql dbHelper = new MySql(this, "mydb", null, ProfileScreen.dbVersion);
        db = dbHelper.getWritableDatabase();
        listView = (ListView) findViewById(R.id.profiles_listView);
        Let_Get_Start = (Button) findViewById(R.id.later_button);
        Student = (LinearLayout) findViewById(R.id.student);
        Looking_Job = (LinearLayout) findViewById(R.id.looking_job);
        Employed = (LinearLayout) findViewById(R.id.employed);
        Staff = (LinearLayout) findViewById(R.id.staff);
        Home_Maker = (LinearLayout) findViewById(R.id.homemaker);
        Senior_Citizen = (LinearLayout) findViewById(R.id.seniorcitizen);

        Let_Get_Start.setOnClickListener(this);
        Student.setOnClickListener(this);
        Looking_Job.setOnClickListener(this);
        Employed.setOnClickListener(this);
        Staff.setOnClickListener(this);
        Home_Maker.setOnClickListener(this);
        Senior_Citizen.setOnClickListener(this);

        setInformationList();
        customAudioFilesAdapter = new CustomRelaxAdapter(this, list, this);
        // listView.setAdapter(customAudioFilesAdapter);

        SharedPreferences prefs = getSharedPreferences("HAPPY_BEING", MODE_PRIVATE);
        editor = getSharedPreferences("HAPPY_BEING", MODE_PRIVATE).edit();
        boolean isFirstTime = prefs.getBoolean("IsFirstTimeHomeScreen", true);
        boolean downloadAudioFiles = prefs.getBoolean("DownloadAudioFiles", true);
        boolean isAlarmSet = prefs.getBoolean("IsAllNotificationsSet", false);

        String profile = prefs.getString(AppConstants.ROLE, "");

      /*  if (isFirstTime || profile.equals("")) {
            deleteOldFile();
            loadFromPreferences();

        }
        else {
            startActivity(new Intent(this, NewHomeScreen.class));
            finish();
            //TODO: navigate to next screen
        }*/
        if (downloadAudioFiles) {
            downloadingAudioFiles();
            Handler mHandler = new Handler();
            editor.putBoolean("DownloadAudioFiles", false);
            editor.commit();
        }
        if (!isAlarmSet) {
            scheduleAlarms();
            editor.putBoolean("IsAllNotificationsSet", true);
            editor.commit();
        }
    }



    private void setInformationList() {
        list = new ArrayList<>();
        AudioFilesInfo audioFilesInfo1 = new AudioFilesInfo(1, getResources().getDrawable(R.drawable.profile_student), "Student", true);
        AudioFilesInfo audioFilesInfo2 = new AudioFilesInfo(1, getResources().getDrawable(R.drawable.looking_for_a_job), "Looking for a job", true);
        AudioFilesInfo audioFilesInfo3 = new AudioFilesInfo(1, getResources().getDrawable(R.drawable.working_full_time), "Employed", true);
        AudioFilesInfo audioFilesInfo4 = new AudioFilesInfo(1, getResources().getDrawable(R.drawable.healthcare_staff), "Health care staff", true);
        AudioFilesInfo audioFilesInfo5 = new AudioFilesInfo(1, getResources().getDrawable(R.drawable.home_maker), "Home maker", true);
        AudioFilesInfo audioFilesInfo6 = new AudioFilesInfo(1, getResources().getDrawable(R.drawable.senior_citizen), "Senior citizen", true);
        list.add(audioFilesInfo1);
        list.add(audioFilesInfo2);
        list.add(audioFilesInfo3);
        list.add(audioFilesInfo4);
        list.add(audioFilesInfo5);
        list.add(audioFilesInfo6);
    }

    private void deleteOldFile() {
        String fileDirPath = getFilesDir().getAbsolutePath() + File.separator + "HappyBeing";
        for (int i = 0; i < 18; i++) {
            File file1 = new File(fileDirPath, "/audiofiles/file"+i+".mp3");
            //Log.i("Mindfulness","File name is "+file1);
            //Log.i("Mindfulness", "****File exists "+file1.exists());
            if (file1.exists()) {
                file1.delete();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case  R.id.later_button:
                String role ="";
                editor.putString(AppConstants.ROLE, role);
                editor.commit();
                Intent intent = new Intent(this, HelpScreen.class);
                intent.putExtra("FROM_SCREEN", "HOME_SCREEN");
                startActivity(intent);
                finish();
                break;
            case R.id.student:
                Student.setBackgroundColor(Color.parseColor("#ff0000"));
                user_role = "Student";
                //  SharedPreferences.Editor editor = getSharedPreferences("happy_being", MODE_PRIVATE).edit();
                editor.putString(AppConstants.ROLE,user_role);
                editor.commit();
                Intent student_intent = new Intent(this, NewHomeScreen.class);
                startActivity(student_intent);
                finish();
                break;
            case R.id.looking_job:
                Looking_Job.setBackgroundColor(Color.parseColor("#ff0000"));
                user_role = "looking_job";
                editor.putString(AppConstants.ROLE,user_role);
                editor.commit();
                Intent looking_job_intent = new Intent(this, NewHomeScreen.class);
                startActivity(looking_job_intent);
                finish();
                break;
            case R.id.employed:
                Employed.setBackgroundColor(Color.parseColor("#ff0000"));
                user_role = "employed";
                editor.putString(AppConstants.ROLE,user_role);
                editor.commit();
                Intent employed_intent = new Intent(this, NewHomeScreen.class);
                startActivity(employed_intent);
                finish();
                break;
            case R.id.staff:
                Staff.setBackgroundColor(Color.parseColor("#ff0000"));
                user_role = "staff";
                editor.putString(AppConstants.ROLE,user_role);
                editor.commit();
                Intent staff_intent = new Intent(this, NewHomeScreen.class);
                startActivity(staff_intent);
                finish();
                break;
            case R.id.homemaker:
                Home_Maker.setBackgroundColor(Color.parseColor("#ff0000"));
                user_role = "homemaker";
                editor.putString(AppConstants.ROLE,user_role);
                editor.commit();
                Intent homemaker_intent = new Intent(this, NewHomeScreen.class);
                startActivity(homemaker_intent);
                finish();
                break;
            case R.id.seniorcitizen:
                Senior_Citizen.setBackgroundColor(Color.parseColor("#ff0000"));
                user_role = "seniorcitizen";
                editor.putString(AppConstants.ROLE,user_role);
                editor.commit();
                Intent seniorcitizen_intent = new Intent(this, NewHomeScreen.class);
                startActivity(seniorcitizen_intent);
                finish();
                break;




        }
    }

    private void loadFromPreferences() {
        ContentValues cv = new ContentValues();

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(System.currentTimeMillis());
        calendar1.set(Calendar.HOUR, 8);
        calendar1.set(Calendar.MINUTE, 00);
        calendar1.set(Calendar.AM_PM, Calendar.AM);
        cv.put("MIND_GYM_START_TIME", calendar1.getTimeInMillis());
        calendar1.set(Calendar.HOUR, 9);
        calendar1.set(Calendar.MINUTE, 30);
        calendar1.set(Calendar.AM_PM, Calendar.PM);
        cv.put("MIND_GYM_END_TIME", calendar1.getTimeInMillis());
        calendar1.set(Calendar.HOUR, 10);
        calendar1.set(Calendar.MINUTE, 00);
        calendar1.set(Calendar.AM_PM, Calendar.AM);
        cv.put("RELAX_START_TIME", calendar1.getTimeInMillis());
        calendar1.set(Calendar.HOUR, 7);
        calendar1.set(Calendar.MINUTE, 00);
        calendar1.set(Calendar.AM_PM, Calendar.PM);
        cv.put("RELAX_END_TIME", calendar1.getTimeInMillis());
        db.insert("NotificationsTimings", null, cv);
    }

    private void downloadingAudioFiles() {
        if (CommonUtils.isNetworkAvailable(this)) {
            startService(new Intent(ProfileScreen.this, DownloadAudioFilesService.class));
        }
        else {
            //TODO: MAke connectionFailure flag 0
        }
    }

    private void scheduleAlarms() {
        int id = 0;
        ContentValues contentValues = new ContentValues();
        boolean updateDB = false;
        long mindgymAudioTime = 0, mindGymAffirmationsTime = 0, relaxStartTime = 0, relaxEndTime = 0;
        Cursor cursor = db.rawQuery("SELECT * FROM NotificationsTimings", null);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            id = cursor.getInt(cursor.getColumnIndex("_id"));
            mindgymAudioTime = cursor.getLong(cursor.getColumnIndex("MIND_GYM_START_TIME"));
            mindGymAffirmationsTime = cursor.getLong(cursor.getColumnIndex("MIND_GYM_END_TIME"));
            relaxStartTime = cursor.getLong(cursor.getColumnIndex("RELAX_START_TIME"));
            relaxEndTime = cursor.getLong(cursor.getColumnIndex("RELAX_END_TIME"));
        }
        //Alarm for 6 am
        // Log.i("AlarmForMorning", "CUrrent time "+System.currentTimeMillis()+" 8 am time "+mindgymAudioTime);
        if(mindgymAudioTime > System.currentTimeMillis()) {
            //Log.i("AlarmForMorning","IN if loop");
            AlarmForMindGymAudio alarmForMindGymAudio = new AlarmForMindGymAudio();
            alarmForMindGymAudio.setAlarm(this, mindgymAudioTime);
            //MINDGYM_AUDIOFILE_NUMBER = 0;
            MainScreen.workoutTypeString = "MORNING WORKOUT";
            setDefaultWorkOutName();
        }
        else {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTimeInMillis(mindgymAudioTime);
            calendar1.add(Calendar.DATE, 1);
            //Log.i("AlarmForMorning", "In else loop"+calendar1.getTimeInMillis());
            AlarmForMindGymAudio alarmForMindGymAudio = new AlarmForMindGymAudio();
            alarmForMindGymAudio.setAlarm(this, calendar1.getTimeInMillis());
            MainScreen.workoutTypeString = "MORNING WORKOUT";
            contentValues.put("MIND_GYM_START_TIME", calendar1.getTimeInMillis());
            updateDB = true;
            setDefaultWorkOutName();
        }

        //Alarm for 9 30 pm
        Calendar calendar2 = new GregorianCalendar();
        //Log.i("AlarmForEvening", "CUrrent time "+System.currentTimeMillis()+" 8 am time "+mindGymAffirmationsTime);
        if(mindGymAffirmationsTime > System.currentTimeMillis()) {
            //Log.i("AlarmForEvening", "IN if loop");
            MainScreen.workoutTypeString = "EVENING WORKOUT";
            MainScreen.workOutNameString = "AFFIRMATIONS";
            AlarmForMindGymAffirmations alarmForMindGymAffirmations = new AlarmForMindGymAffirmations();
            alarmForMindGymAffirmations.setAlarm(this, mindGymAffirmationsTime);
        }
        else {
            calendar2.setTimeInMillis(mindGymAffirmationsTime);
            calendar2.add(Calendar.DATE, 1);
            //Log.i("AlarmForEvening", "IN else loop"+calendar2.getTimeInMillis());
            AlarmForMindGymAffirmations alarmForMindGymAffirmations = new AlarmForMindGymAffirmations();
            alarmForMindGymAffirmations.setAlarm(this, calendar2.getTimeInMillis());
            contentValues.put("MIND_GYM_END_TIME", calendar2.getTimeInMillis());
            updateDB = true;
        }

        //Relax start time
        if (relaxStartTime > System.currentTimeMillis()) {
            Alarm alarm = new Alarm();
            alarm.setAlarm(this, relaxStartTime);
        }
        else {
            Calendar calendar1 = new GregorianCalendar();
            calendar1.setTimeInMillis(relaxStartTime);
            calendar1.add(Calendar.DATE, 1);
            Alarm alarm = new Alarm();
            alarm.setAlarm(this, calendar1.getTimeInMillis());
            contentValues.put("RELAX_START_TIME", calendar1.getTimeInMillis());
            updateDB = true;
        }

        if (relaxEndTime > System.currentTimeMillis()) {
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
            updateDB = true;
        }
        if (updateDB)
            db.update("NotificationsTimings", contentValues, "_id="+id, null);
        cursor.close();
    }

    private void setDefaultWorkOutName() {
        SharedPreferences preferences  = getSharedPreferences("HAPPY_BEING", MODE_PRIVATE);
        String profile = preferences.getString(AppConstants.ROLE, "");
        String name;
        if (profile.equals("Student")) {
            //Log.i("AlarmForResettingFiles", "Audio file number is "+ProfileScreen.MINDGYM_AUDIOFILE_NUMBER);
            name = DownloadMindGymAudioFilesService.mindGymForStudentsAudioNames[0];
        }
        else if (profile.contains("Expecting_Mom") || profile.contains("Want_to_be_mom")){
            name = DownloadMindGymAudioFilesService.mindGymForPregnecyWomenAdioNames[0];
        }
        else {
            name = DownloadMindGymAudioFilesService.mindGymForAdultsAudioListNames[0];
        }
        MainScreen.workOutNameString = name;
    }

    @Override
    public void onItemSelected(AudioFilesInfo audioFilesInfo) {
        storeProfileAndNavigateToNextScreen(audioFilesInfo);
    }

    @Override
    public void onDownloadButtonClicked(AudioFilesInfo audioFilesInfo) {

    }

    private void storeProfileAndNavigateToNextScreen(AudioFilesInfo audioFilesInfo) {
        String role = audioFilesInfo.getTextView();
        System.out.println("role**************"+role);
        editor.putString(AppConstants.ROLE, role);
        editor.putBoolean("IsFirstTimeHomeScreen", false);
        editor.commit();
        Intent intent = new Intent(this, HelpScreen.class);
        intent.putExtra("FROM_SCREEN", "HOME_SCREEN");
        startActivity(intent);
        finish();

    }


}
