package com.azuyo.happybeing.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.azuyo.happybeing.MindGym.PositivityAffirmations;
import com.azuyo.happybeing.R;
import com.azuyo.happybeing.ServerAPIConnectors.APIProvider;
import com.azuyo.happybeing.ServerAPIConnectors.API_Response_Listener;
import com.azuyo.happybeing.Utils.AppConstants;
import com.azuyo.happybeing.Utils.CommonUtils;
import com.azuyo.happybeing.Utils.HappyBeingApplicationClass;
import com.azuyo.happybeing.Utils.UserInformation;
import com.azuyo.happybeing.events.DownloadMindGymAudioFilesService;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Admin on 21-12-2016.
 */

public class MindGymScreen extends BaseActivity implements View.OnClickListener {
    private CardView testAnxietyAssessment, selfAssessment, happinessAssessment, today_work_out, positive_affirmations;
    private TextView testAnxietyText;
    private String profile;
    private TextView name_of_workout;
    private int stored_date = 1;
    private String audio_file = "";
    private String[] audiofilesList;
    int daycount, fileNumber;
    private ImageView profileImage, help, knowledge_hub, close_button;
    private RelativeLayout help_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mind_gym);

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        profileImage = (ImageView) findViewById(R.id.profile_image);
        help = (ImageView) findViewById(R.id.help_image);
        knowledge_hub = (ImageView) findViewById(R.id.knowledge_hub);
        help_layout = (RelativeLayout) findViewById(R.id.help_layout);
        close_button = (ImageView) findViewById(R.id.close_button);

        SimpleDateFormat df1 = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate1 = df1.format(c.getTime());
        String[] formatedDates = formattedDate1.split("-");
        int onlyDate = Integer.parseInt(formatedDates[0]);
        daycount = onlyDate - stored_date;
        fileNumber = 0;
        testAnxietyAssessment = (CardView) findViewById(R.id.test_anxiety_card);
        selfAssessment = (CardView) findViewById(R.id.self_card);
        happinessAssessment = (CardView) findViewById(R.id.happiness_card);
        today_work_out = (CardView) findViewById(R.id.today_work_out);
        testAnxietyText = (TextView) findViewById(R.id.test_anxiety_text);
        positive_affirmations = (CardView) findViewById(R.id.positive_affirmations);
        name_of_workout = (TextView) findViewById(R.id.name_of_workout);

        testAnxietyAssessment.setOnClickListener(this);
        selfAssessment.setOnClickListener(this);
        happinessAssessment.setOnClickListener(this);
        today_work_out.setOnClickListener(this);
        positive_affirmations.setOnClickListener(this);
        String regex ="^[0-9]";
        Intent intent = getIntent();
        SharedPreferences sharedPreferences = getSharedPreferences("HAPPY_BEING", MODE_PRIVATE);
        profile = sharedPreferences.getString(AppConstants.ROLE, "");
        if (profile.equals("Student")) {
            //Log.i("AlarmForResettingFiles", "Audio file number is "+ProfileScreen.MINDGYM_AUDIOFILE_NUMBER);
            audiofilesList = new String[DownloadMindGymAudioFilesService.mindGymForStudentsAudioNames.length];
            String name = "" ;//= DownloadMindGymAudioFilesService.mindGymForStudentsAudioNames[ProfileScreen.MINDGYM_AUDIOFILE_NUMBER];
            name = repalceNumbers(name);
            name_of_workout.setText(name);
            testAnxietyText.setText("Exam anxiety");
            knowledge_hub.setVisibility(View.VISIBLE);
        }
        else if (profile.contains("Expecting_Mom") || profile.contains("Want_to_be_mom")){
            audiofilesList = new String[DownloadMindGymAudioFilesService.mindGymForPregnecyWomenAdioNames.length];
            String name = "";//DownloadMindGymAudioFilesService.mindGymForPregnecyWomenAdioNames[ProfileScreen.MINDGYM_AUDIOFILE_NUMBER];
            name_of_workout.setText(repalceNumbers(name));
            testAnxietyText.setText("Perception stress");
            knowledge_hub.setVisibility(View.VISIBLE);
        }
        else {
            audiofilesList = new String[DownloadMindGymAudioFilesService.mindGymForAdultsAudioListNames.length];
            String name = "";//DownloadMindGymAudioFilesService.mindGymForAdultsAudioListNames[ProfileScreen.MINDGYM_AUDIOFILE_NUMBER];
            name_of_workout.setText(repalceNumbers(name));
            testAnxietyText.setText("Perception stress");
            knowledge_hub.setVisibility(View.GONE);
        }
        loadImages();

        profileImage.setOnClickListener(this);
        help.setOnClickListener(this);
        knowledge_hub.setOnClickListener(this);
        close_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.test_anxiety_card:
                if (profile.equals("Student"))
                    gotoWebViewClass("https://nsmiles.typeform.com/to/UizlpC");
                else
                    gotoWebViewClass("https://nsmiles.typeform.com/to/CFXsQZ");
                break;
            case R.id.self_card:
                if (profile.equals("Student"))
                    gotoWebViewClass("https://nsmiles.typeform.com/to/rCBYRU");
                else
                    gotoWebViewClass("https://nsmiles.typeform.com/to/rCBYRU");
                break;
            case R.id.happiness_card:
                if (profile.equals("Student"))
                    gotoWebViewClass("https://nsmiles.typeform.com/to/MBnkE1");
                else
                    gotoWebViewClass("https://nsmiles.typeform.com/to/ZelnCQ");
                break;
            case R.id.today_work_out:
                Intent intent = new Intent(this, TodayWorkOutScreen.class);
                intent.putExtra("AUDIO_FILE_NAME", name_of_workout.getText().toString());
                /*intent.putExtra("AUDIO_FILE", audiofilesList[ProfileScreen.MINDGYM_AUDIOFILE_NUMBER]);
                intent.putExtra("AUDIO_FILE_NUMBER", ProfileScreen.MINDGYM_AUDIOFILE_NUMBER);*/
                checkForSignedInAndPaid(intent);
                break;
            case R.id.positive_affirmations:
                Intent intent1 = new Intent(this, PositivityAffirmations.class);
                checkForSignedInAndPaid(intent1);
                break;
            case R.id.profile_image:
                startActivity(new Intent(this, UserAccountScreen.class));
                break;
            case R.id.knowledge_hub:
                Intent intent2;
                SharedPreferences sharedPreferences = getSharedPreferences("HAPPY_BEING", MODE_PRIVATE);
                if (sharedPreferences.getString(AppConstants.ROLE, "").equals("Student")) {
                    intent2 = new Intent(this, KnowledgeHubListView.class);
                }
                else {
                    intent2 = new Intent(this, KnowledgeHubDayLayout.class);
                    intent2.putExtra("AUDIO_FILE", audiofilesList[24]);
                    intent2.putExtra("AUDIO_FILE_NUMBER", 24);
                }
                checkForSignedInAndPaid(intent2);
                break;
            case R.id.help_image:
                if (help_layout.getVisibility() == View.VISIBLE) {
                    help_layout.setVisibility(View.GONE);
                }
                else {
                    help_layout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.close_button:
                if (help_layout.getVisibility() == View.VISIBLE) {
                    help_layout.setVisibility(View.GONE);
                }
                break;
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
                new APIProvider.PaymentStatus(userEmailId, 2235, MindGymScreen.this, "Fetching days left", new API_Response_Listener<Integer>() {
                    @Override
                    public void onComplete(Integer data, long request_code, String failure_code) {
                        if (data != null && data > 0) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("ExpiryDate", data);
                            editor.apply();
                            startActivity(intent);
                        }
                        else {
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

    private void gotoWebViewClass(String link) {
        Intent intent = new Intent(this, AssessmetWebView.class);
        intent.putExtra("LINK", link);
        startActivity(intent);
    }

    private void loadImages() {
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

    private String repalceNumbers(String name) {
        name = name.replace("1", "");
        name = name.replace("2", "");
        name = name.replace("3", "");
        name = name.replace("4", "");
        name = name.replace("5", "");
        name = name.replace("6", "");
        name = name.replace("7", "");
        name = name.replace("8", "");
        name = name.replace("9", "");
        name = name.replace("10", "");
        name = name.replace("11", "");
        name = name.replace("12", "");
        name = name.replace("13", "");
        name = name.replace("14", "");
        name = name.replace("15", "");
        name = name.replace("16", "");
        name = name.replace("17", "");
        name = name.replace("18", "");
        name = name.replace("19", "");
        name = name.replace("20", "");
        name = name.replace("21", "");
        name = name.replace("Day", "");
        name = name.replace("-", " ");
        return name;
    }

    @Override
    public void onBackPressed() {
        if (help_layout.getVisibility() == View.VISIBLE) {
            help_layout.setVisibility(View.GONE);
        }
        else
            super.onBackPressed();
    }
}
