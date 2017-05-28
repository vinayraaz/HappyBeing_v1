package com.azuyo.happybeing.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.azuyo.happybeing.MindGym.PositivityAffirmations;
import com.azuyo.happybeing.R;
import com.azuyo.happybeing.ServerAPIConnectors.APIProvider;
import com.azuyo.happybeing.ServerAPIConnectors.API_Response_Listener;
import com.azuyo.happybeing.Utils.AppConstants;
import com.azuyo.happybeing.Utils.CommonUtils;
import com.azuyo.happybeing.Utils.MySql;
import com.azuyo.happybeing.events.DownloadMindGymAudioFilesService;

import java.io.File;
import java.sql.Time;
import java.util.Calendar;

/**
 * Created by Admin on 21-12-2016.
 */

public class MainScreen extends BaseActivity implements Animation.AnimationListener, View.OnClickListener {
    private TextView tileText, mentalStrenghtText, goalsText, relaxText;
    private LinearLayout mainLayout;
    private Animation animation1;
    private ImageView profile, help, addHappyMomentsImage, close_button, assessmentsImage, addGratitudeImage;
    private RelativeLayout goals_card, destress_card, wellness_measure_card;
    private RelativeLayout help_layout;
    private TextView firstCardSubTitleText, secondCardTitleText, secondCardSubTitleText, thirdCardTitleText,
            thirdCardSubTitleText;
    public static ImageView playButton;
    private TextView workoutType, workOutName;
    public static String workoutTypeString = "", workOutNameString = "";
    private String[] audiofilesList;
    private int mindgym_audiofile_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        tileText = (TextView) findViewById(R.id.title_text);
        mentalStrenghtText = (TextView) findViewById(R.id.mentalText);
        goalsText = (TextView) findViewById(R.id.goalsText);
        relaxText = (TextView) findViewById(R.id.relaxText);
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        profile = (ImageView) findViewById(R.id.profile_image);
        help = (ImageView) findViewById(R.id.help_image);
        close_button = (ImageView) findViewById(R.id.close_button);
        assessmentsImage = (ImageView) findViewById(R.id.assessments_image);
        addGratitudeImage = (ImageView) findViewById(R.id.gratitude_image);
        addHappyMomentsImage = (ImageView) findViewById(R.id.feeling_capture_image);
        goals_card = (RelativeLayout) findViewById(R.id.goals_card);
        destress_card = (RelativeLayout) findViewById(R.id.destress_card);
        wellness_measure_card = (RelativeLayout) findViewById(R.id.wellness_measure_card);
        help_layout = (RelativeLayout) findViewById(R.id.help_layout);
        workoutType =(TextView) findViewById(R.id.workoutType);
        workOutName = (TextView) findViewById(R.id.todayWorkoutName);

        firstCardSubTitleText = (TextView) findViewById(R.id.firstcard_sub_titleText);
        secondCardTitleText = (TextView) findViewById(R.id.secondcard_titleText);
        secondCardSubTitleText = (TextView) findViewById(R.id.secondcard_sub_titleText);
        thirdCardTitleText = (TextView) findViewById(R.id.thirdcard_titleText);
        thirdCardSubTitleText = (TextView) findViewById(R.id.thirdcard_sub_titleText);

        animation1 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        animation1.setAnimationListener(this);
        mainLayout.setVisibility(View.VISIBLE);
        mainLayout.startAnimation(animation1);
        profile.setOnClickListener(this);
        help.setOnClickListener(this);
        assessmentsImage.setOnClickListener(this);

        close_button.setOnClickListener(this);
        addHappyMomentsImage.setOnClickListener(this);
        addGratitudeImage.setOnClickListener(this);
        tileText.setVisibility(View.VISIBLE);
        goals_card.setOnClickListener(this);
        destress_card.setOnClickListener(this);
        wellness_measure_card.setOnClickListener(this);
        setViewBasedOnProfile();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        SharedPreferences sharedPreferences = getSharedPreferences("HAPPY_BEING", MODE_PRIVATE);
        String profile = sharedPreferences.getString(AppConstants.ROLE, "");

        MySql dbHelper = new MySql(this, "mydb", null, ProfileScreen.dbVersion);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM MindGymAudioCount", null);
       // Log.i("MainScreen", "Cursor count is "+cursor.getCount());
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            mindgym_audiofile_number = cursor.getInt(cursor.getColumnIndex("MIND_GYM_AUDIO_COUNT"));
        }
        else {
            mindgym_audiofile_number = 0;
        }

        //Log.i("MainScreen","Audio file number is "+mindgym_audiofile_number);
        if (profile.equals("Student")) {
            //Log.i("AlarmForResettingFiles", "Audio file number is "+ProfileScreen.MINDGYM_AUDIOFILE_NUMBER);
            audiofilesList = new String[DownloadMindGymAudioFilesService.mindGymForStudentsAudioNames.length];
        }
        else if (profile.contains("Expecting_Mom") || profile.contains("Want_to_be_mom")){
            audiofilesList = new String[DownloadMindGymAudioFilesService.mindGymForPregnecyWomenAdioNames.length];
        }
        else {
            audiofilesList = new String[DownloadMindGymAudioFilesService.mindGymForAdultsAudioListNames.length];
        }
        loadImages();
        setWorkOutTextToAffirmations();
        if (workoutTypeString != null)
            workoutType.setText(workoutTypeString);
        if (workOutNameString != null)
            workOutName.setText(workOutNameString);
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == animation1) {

        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MySql dbHelper = new MySql(this, "mydb", null, ProfileScreen.dbVersion);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM MindGymAudioCount", null);
        //Log.i("Mainscreen", "Cursor count is "+cursor.getCount());
        if (cursor.getCount() > 0) {
            //Log.i("MainScreen", "IN IF LOOP");
            cursor.moveToLast();
            mindgym_audiofile_number = cursor.getInt(cursor.getColumnIndex("MIND_GYM_AUDIO_COUNT"));
            //Log.i("Mainscreen", "Audio file number is "+cursor.getCount());
        }
        else {
            mindgym_audiofile_number = 0;
        }

        setWorkOutTextToAffirmations();
        if (workoutTypeString != null)
            workoutType.setText(workoutTypeString);
        if (workOutNameString != null)
            workOutName.setText(workOutNameString);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.profile_image:
                startActivity(new Intent(this, UserAccountScreen.class));
                break;
            case R.id.feeling_capture_image:
                startActivity(new Intent(this, ComposeNewHappyMoment.class));
                break;
            case R.id.help_image:
                startActivity(new Intent(this, HelpScreen.class));
                break;
            case R.id.close_button:
                if (help_layout.getVisibility() == View.VISIBLE) {
                    help_layout.setVisibility(View.GONE);
                }
                break;
            case R.id.goals_card:
                //TODO: Check for number of days left.
                Intent intent;
                if (workoutType.getText().equals("EVENING WORKOUT")) {
                    intent = new Intent(this, PositivityAffirmations.class);
                    checkForSignedInAndPaid(intent);
                }
                else {
                    intent = new Intent(this, TodayWorkOutScreen.class);
                    intent.putExtra("AUDIO_FILE_NAME", workOutName.getText().toString());
                    intent.putExtra("AUDIO_FILE", audiofilesList[mindgym_audiofile_number]);
                    intent.putExtra("AUDIO_FILE_NUMBER", mindgym_audiofile_number);
                    checkForSignedInAndPaid(intent);
                }
                break;
            case R.id.wellness_measure_card:
                startActivity(new Intent(this, MyGoals_fragment.class));
              break;
            case R.id.destress_card:
                startActivity(new Intent(this, InstantStressReliefScreen.class));
                break;
            case R.id.gratitude_image:
                startActivity(new Intent(this, ComposeGratitudeDiary.class));
                break;
            case R.id.assessments_image:
                startActivity(new Intent(this, AssessmentsPage.class));
                break;
        }
    }

    private void setViewBasedOnProfile() {
        SharedPreferences sharedPreferences = getSharedPreferences("HAPPY_BEING", MODE_PRIVATE);
        String profile = sharedPreferences.getString(AppConstants.ROLE, "");

        if (profile.equals("Student")) {
            tileText.setText("Champion mindset builder");
            mentalStrenghtText.setText("Mind strengthening provides set of meditations and positive affirmations designed to  help you increase mental strength along with having  positive thoughts and beliefs to support your end goal. Be it Performance at an exam or Enhancing self-esteem and confidence. Measure and track your progress of developing positive mental attitude using our assessments.");
            goalsText.setText("Define your goal, acknowledge what it will take to accomplish it and leverage the power of reinforcements and social accountability to turn that goal into reality. \n Go ahead and take the first step  towards holding yourself accountable.");
            relaxText.setText("We have rounded up for as many as 50 ways to relax and relieve from stress,fear, worry or anger in just five minutes or less. From chewing mint to some breathing techniques to cuddle your pet, all the tactics can create calm during tough times.");
        }
        else if(profile.contains("Expecting_Mom") || profile.contains("Want_to_be_mom")) {
            tileText.setText("Pregnancy body-mind care");
            mentalStrenghtText.setText("Mind strengthening provides set of meditations and positive affirmations designed to  help you increase mental strength along with having  positive thoughts and beliefs of you being a mom. \n Measure and track your progress of developing positive mental attitude using our assessments.");
            goalsText.setText("Define your goal, acknowledge what it will take to accomplish it and leverage the power of reinforcements and social accountability to turn that goal into reality.\n Go ahead and take the first step  towards holding yourself accountable.");
            relaxText.setText("We have rounded up for as many as 50 ways to relax and relieve from stress,fear, worry or anger in just five minutes or less. From sipping fresh juice to some meditation techniques to journaling, all the tactics can create calm during tough times.");
        }
        else {
            tileText.setText("Happy being");
            mentalStrenghtText.setText("Mind strengthening provides set of meditations and positive affirmations designed to  help you increase mental strength along with having  positive thoughts and beliefs to support your end goal of being positive, happier, healthier and peaceful. \nMeasure and track your progress of developing positive mental attitude using our assessments.");
            goalsText.setText("Define your goal, acknowledge what it will take to accomplish it and leverage the power of reinforcements and social accountability to turn that goal into reality \n Go ahead and take the first step  towards holding yourself accountable.");
            relaxText.setText("We have rounded up for as many as 50 ways to relax and relieve from stress,fear, worry or anger in just five minutes or less. From sipping tea to some breathing techniques to journaling, all the tactics can create calm during tough times.");
        }
        firstCardSubTitleText.setText("");
        secondCardTitleText.setText("Set and achieve my goals");
        secondCardSubTitleText.setText("Focus on what matters to you the most");
        thirdCardTitleText.setText("Relax and relieve instantly");
        thirdCardSubTitleText.setText("Destress and feel positive in 5 mins");
    }

    @Override
    public void onBackPressed() {
        if (help_layout.getVisibility() == View.VISIBLE) {
            help_layout.setVisibility(View.GONE);
        }
        else {
            super.onBackPressed();
        }
    }
    private void setWorkOutTextToAffirmations() {
        SharedPreferences sharedPreferences = getSharedPreferences("HAPPY_BEING", MODE_PRIVATE);
        MySql dbHelper = new MySql(this, "mydb", null, ProfileScreen.dbVersion);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long mindGymAffirmationsTime = 0;
        Cursor cursor = db.rawQuery("SELECT * FROM NotificationsTimings", null);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            mindGymAffirmationsTime = cursor.getLong(cursor.getColumnIndex("MIND_GYM_END_TIME"));
        }
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(mindGymAffirmationsTime);
        long mindGymAffirmationsHourTime = calendar1.get(Calendar.HOUR_OF_DAY);
        int hours = new Time(System.currentTimeMillis()).getHours();
        //Log.i("MainScreen", "milli sec is "+mindGymAffirmationsHourTime+" Curent"+hours);

        //Log.i("MainScreen", "Workout type "+workoutTypeString);
        if (mindGymAffirmationsHourTime <= hours) {
            workoutTypeString = "EVENING WORKOUT";
            workOutNameString = "Affirmations";
        }
        else {
            if (workOutNameString != null && !workoutTypeString.equals("Awesome!")) {
                workoutTypeString = "MORNING WORKOUT";
                String profile, nameOfWorkout;
                profile = sharedPreferences.getString(AppConstants.ROLE, "");
                if (profile.equals("Student")) {
                    //Log.i("AlarmForResettingFiles", "Audio file number is "+ProfileScreen.MINDGYM_AUDIOFILE_NUMBER);
                    nameOfWorkout = DownloadMindGymAudioFilesService.mindGymForStudentsAudioNames[mindgym_audiofile_number];
                } else if (profile.contains("Expecting_Mom") || profile.contains("Want_to_be_mom")) {
                    nameOfWorkout = DownloadMindGymAudioFilesService.mindGymForPregnecyWomenAdioNames[mindgym_audiofile_number];
                } else {
                    nameOfWorkout = DownloadMindGymAudioFilesService.mindGymForAdultsAudioListNames[mindgym_audiofile_number];
                }
                nameOfWorkout = CommonUtils.repalceNumbers(nameOfWorkout);
                workOutNameString = nameOfWorkout;
            }
        }
        cursor.close();
    }

    private void loadImages() {
        SharedPreferences sharedPreferences = getSharedPreferences("HAPPY_BEING", MODE_PRIVATE);
        String profile = sharedPreferences.getString(AppConstants.ROLE, "");

        String fileList[];
        String fileDirPath = getFilesDir().getAbsolutePath() + File.separator + "HappyBeing";
        if (profile.equals("Student")) {
            fileList = DownloadMindGymAudioFilesService.mindGymForStudentsAudioNames;
        }
        else if (profile.contains("Expecting_Mom")) {
            fileList = DownloadMindGymAudioFilesService.mindGymForPregnecyWomenAdioNames;
        }
        else {
            fileList = DownloadMindGymAudioFilesService.mindGymForAdultsAudioListNames;
        }
        for (int i = 0; i < fileList.length; i++) {
            File file1 = new File(fileDirPath, "/audiofiles/"+fileList[i]+".mp3");
            //Log.i("Mindfulness","File name is "+file1);
            //Log.i("Mindfulness", "****File exists "+file1.exists());
            if (file1.exists()) {
                String filePath = file1.getAbsolutePath();
                audiofilesList[i] = filePath;
            }
            else {
                boolean fileNotExist = true;
            }

        }
    }

    private void checkForSignedInAndPaid(final Intent intent) {
        // Log.i("MindGym","In Check for signed in");
        final SharedPreferences sharedPreferences = getSharedPreferences("HAPPY_BEING", MODE_PRIVATE);
        boolean isSignedIn = sharedPreferences.getBoolean(AppConstants.IS_SIGNED_IN, false);
        int expiryInNoOfDays = sharedPreferences.getInt("ExpiryDate", 0);
        String userEmailId = sharedPreferences.getString(AppConstants.EMAIL_ID, "");
        //Log.i("MindGym", "Sign in "+isSignedIn);
        if (isSignedIn) {
            //Log.i("MindGym", "Network avail"+CommonUtils.isNetworkAvailable(this));
            if (CommonUtils.isNetworkAvailable(this)) {
                new APIProvider.PaymentStatus(userEmailId, 2235, MainScreen.this, "Fetching days left", new API_Response_Listener<Integer>() {
                    @Override
                    public void onComplete(Integer data, long request_code, String failure_code) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if (data != null && data > 0) {
                            editor.putInt("ExpiryDate", data);
                            editor.putBoolean(AppConstants.IS_PAID, true);
                            editor.apply();
                            startActivity(intent);
                        }
                        else {
                            editor.putBoolean(AppConstants.IS_PAID, false);
                            editor.apply();
                            showPaidScreen();
                        }
                    }
                }).call();
            }
            else {
                int expityDate = sharedPreferences.getInt("ExpiryDate", 0);
                //Log.i("MindGym", "Expiry date is "+isSignedIn);
                if (expityDate > 0) {
                    startActivity(intent);
                }
                else {
                    showPaidScreen();
                }
            }
        }
        else {
            gotoSignInSxcreen();
        }
    }

    private void gotoSignInSxcreen() {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra("FROM","BUY");
        startActivity(intent);
    }

    private void showPaidScreen() {
        startActivity(new Intent(this, PaymentScreen.class));

    }
}

