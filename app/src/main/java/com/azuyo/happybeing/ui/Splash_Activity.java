package com.azuyo.happybeing.ui;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.Utils.AppConstants;
import com.azuyo.happybeing.Utils.MySql;
import com.azuyo.happybeing.adapter.DBAdapter;

import java.io.File;
import java.util.Calendar;

/**
 * Created by nSmiles on 5/19/2017.
 */

public class Splash_Activity extends Activity {
    private static int SPLASH_TIME_OUT = 200;
    DBAdapter dbAdapter;
    String selected_role = "";
    public static int dbVersion = 4;
    SharedPreferences.Editor editor;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        MySql dbHelper = new MySql(this, "mydb", null, Splash_Activity.dbVersion);
        db = dbHelper.getWritableDatabase();
       /* SharedPreferences prefs = getSharedPreferences("HAPPY_BEING", MODE_PRIVATE);
        editor = getSharedPreferences("HAPPY_BEING", MODE_PRIVATE).edit();
        boolean isFirstTime = prefs.getBoolean("IsFirstTimeHomeScreen", true);
        boolean downloadAudioFiles = prefs.getBoolean("DownloadAudioFiles", true);
        boolean isAlarmSet = prefs.getBoolean("IsAllNotificationsSet", false);*/

       final SharedPreferences prefs = getSharedPreferences("HAPPY_BEING", MODE_PRIVATE);
        selected_role = prefs.getString(AppConstants.ROLE,"");
        System.out.println("selected_role**************"+selected_role);

        selected_role = prefs.getString(AppConstants.ROLE,"");
        dbAdapter = new DBAdapter(Splash_Activity.this);
        dbAdapter.open();
        dbAdapter.close();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (selected_role.isEmpty() || selected_role.equals("") || selected_role.equals(null)){
                   // deleteOldFile();
                   // loadFromPreferences();
                    Intent intent = new Intent(Splash_Activity.this, ProfileScreen.class);
                    startActivity(intent);
                    finish();
                }else {

                    Intent intent = new Intent(Splash_Activity.this, NewHomeScreen.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);

        String path = openOrCreateDatabase("mydb",MODE_WORLD_READABLE,null).getPath();
        Intent intent =new Intent(Intent.ACTION_EDIT);
        intent.setData(Uri.parse("Sqlite database: "+path));
    }




   /* private void deleteOldFile() {
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
    }*/
}
