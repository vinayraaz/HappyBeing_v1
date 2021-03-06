package com.azuyo.happybeing.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.Views.AudioFilesInfo;
import com.azuyo.happybeing.Views.CustomRelaxAdapter;
import com.azuyo.happybeing.events.DownloadAudioFilesService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Admin on 21-12-2016.
 */

public class InstantStressReliefScreen extends BaseActivity implements OnItemSelectedListener, View.OnClickListener {

    private ListView listView;
    private CustomRelaxAdapter customAudioFilesAdapter;
    private List<AudioFilesInfo> list;
    private String[] audiofilesList = new String[DownloadAudioFilesService.relaxAndRelieveFileNames.length];
    private boolean called = false, fileNotExist = false;
    private TextView card1Textview,card2Textview,card3Textview ;
    private ImageView card1ImageView, card2ImageView, card3ImageView;

    private String[] optionsList1 = {"Belly breathing", "4-7-8 breathing", "Abdominal breathing", "Tense and relax","Shake your body",
            "Calming meditation", "Self-compassion", "Body scan",  "Self massage",
            "Stretching to relax your muscles", "Have a Alone time to collect your thoughts and clear your head",
            "Make or create a stress free zone and relax there for a while till you feel calm", "Write a Gratitude journal",
            "Journal your thoughts and feelings", "Vent your burdens and disappointments", "Listen to Ocean waves and relax",
            "Listen to Birds chirping and relax", "Listen to Sounds of rain and relax"};
    private int list1Drawable[] = {R.drawable.belly_breathing, R.drawable.breathing_4_7_8, R.drawable.abdominal_breathing, R.drawable.tense_and_relax,
            R.drawable.shake_your_body, R.drawable.calming_meditation, R.drawable.self_compassion, R.drawable.body_scan,
            R.drawable.self_massage, R.drawable.stretching_to_relax_your_muscles,
            R.drawable.have_a_alone, R.drawable.make_free_zone, R.drawable.journal_your_thoughts,  R.drawable.vent_journal, R.drawable.vent, R.drawable.ocean_waves,
            R.drawable.birds_chirping, R.drawable.sounds_of_rain};
    private String[] optionsList2 = {"Drop some cold water on your wrists and behind your earlobes. Feel the sense of calmness",
            "Pinch the length of earlobes and feel sense of relaxation",
            "Brush your hair", "Rub Your Feet Over a Golf Ball", "Squeeze a Stress Ball", "Make or create a stress free zone and relax there for a while till you feel calm",
            "Head outside and get some bright sun", "Take a five-minute break to do nothing but stare out the window to a natural scene like a park or a tree",
            "Take a time out to de-clutter your working desk", "Dance to your favourite song - Tap you legs and move your body in tune with the music",
            "Walk for 50 steps and grab some  Water while you clear your thoughts", "Hydrate yourself with some Pure coconut water", "Drink Hot or cold milk while you relax",
            "Sip green tea and relax", "Have a whole fruit and relax", "Have a square of dark chocolate and pleasure your senses", "Drink Fresh vegetable juice",
            "Try a spoonful of honey and relinquish the taste on your tongue as it's calm you", "Take a chewing gum",
            "Handful of nuts like organic pistachios, cashewnuts, walnuts, almonds, raisins", "Munch a crunchy snack and relax", "Have a Fresh Vegetable salad to energise yourself",
            "Have an Ice cream and relax by rejoicing the taste", "Smell some flowers and let the fragrance soothe your nerves", "Dab some essential oil on your palm and inhale for relaxing your senses",
            "Smell some citrus fruits - oranges, lemons and get energised", "Smell some coffee beans and let the smell of coffee soothe you.",
            "Have a fresh fruit salad and let the sweetness of fruits energise you.", "Cuddle a pet and feel relaxed"};
    private int list2Drawable[] = {R.drawable.drop_some_cold, R.drawable.tense_and_relax, R.drawable.brush_hair, R.drawable.rub_your_golf_ball,
            R.drawable.tense_and_relax, R.drawable.make_free_zone, R.drawable.head_outside, R.drawable.take_five_minute, R.drawable.take_five_minute
            , R.drawable.dance_song, R.drawable.walk, R.drawable.coconut_water, R.drawable.hot_or_cold_milk, R.drawable.sip_green_tea
            , R.drawable.whole_fruit, R.drawable.chochlate, R.drawable.fresh_veg_juice, R.drawable.honey, R.drawable.chewing_gum,
            R.drawable.handful_nuts, R.drawable.crunchy_snack, R.drawable.veg_salad, R.drawable.ice_cream, R.drawable.smell_some_flowers
            , R.drawable.dab_some_oil, R.drawable.smell_lemons, R.drawable.smell_some_coffee_beans, R.drawable.have_fruit_salad, R.drawable.pet_curddle};
    private int option1, option2;
    Random random = new Random();
    private CardView cardView1, cardView2, cardView3;
    public static int screenNumber = 0, screenNumber2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instant_stress_relief);
        listView = (ListView) findViewById(R.id.listView);
     //   customAudioFilesAdapter = new CustomRelaxAdapter(this, list, this);
       // listView.setAdapter(customAudioFilesAdapter);

        cardView1 = (CardView) findViewById(R.id.cardview1);
        cardView2 = (CardView) findViewById(R.id.cardview2);
        cardView3 = (CardView) findViewById(R.id.cardview3);

        card1ImageView = (ImageView) findViewById(R.id.cardView1_image);
        card2ImageView = (ImageView) findViewById(R.id.cardView2_image);
        card3ImageView = (ImageView) findViewById(R.id.cardView3_image);

        card1Textview = (TextView) findViewById(R.id.cardview1_textview);
        card2Textview = (TextView) findViewById(R.id.cardview2_textview);
        card3Textview = (TextView) findViewById(R.id.cardview3_textview);

        cardView1.setOnClickListener(this);
        cardView3.setOnClickListener(this);

        setInformationList();
        loadImages();
    }

    private void setInformationList() {
     //   Log.i("InstantRelief", "Screen number"+screenNumber+" option number"+optionsList1.length);
        if (screenNumber < optionsList1.length - 1) {
            screenNumber++;
        }
        else {
            screenNumber = 0;
        }
        if (screenNumber2 < optionsList2.length - 1) {
            screenNumber2++;
        }
        else {
            screenNumber2 = 0;
        }

        option1 = screenNumber;
        option2 = screenNumber2;
        //Log.i("StressReleif","OPtion 1 list count "+optionsList1.length+" drawable list count is "+list1Drawable.length);
        //Log.i("StressReleif","OPtion 2 list count "+optionsList2.length+" drawable list count is "+list2Drawable.length);
        list = new ArrayList<>();
        card1Textview.setText(optionsList1[option1]);
        card1ImageView.setBackground(getResources().getDrawable(list1Drawable[option1]));

        card2Textview.setText(optionsList2[option2]);
        card2ImageView.setBackground(getResources().getDrawable(list2Drawable[option2]));

        card3Textview.setText("Let me choose");
        card3ImageView.setBackground(getResources().getDrawable(R.drawable.choose_icon));

       // AudioFilesInfo audioFilesInfo1 = new AudioFilesInfo(1, getResources().getDrawable(list1Drawable[option1]), optionsList1[option1], "");
        AudioFilesInfo audioFilesInfo2 = new AudioFilesInfo(2, getResources().getDrawable(list2Drawable[option2]), optionsList2[option2], "");
        AudioFilesInfo audioFilesInfo3 = new AudioFilesInfo(3, "Let me choose"); audioFilesInfo2.setSetNextImageVisibility(false);
        audioFilesInfo3.setImageSource(getResources().getDrawable(R.drawable.choose_icon));

       // list.add(audioFilesInfo1);
        list.add(audioFilesInfo2);
        list.add(audioFilesInfo3);
    }

    @Override
    public void onItemSelected(AudioFilesInfo audioFilesInfo) {
        int id = audioFilesInfo.getId();
        if (id == 1) {
            //Navigate to Audio file Screen with all data
            Intent intent = new Intent(this, AudioScreen.class);
            switch (option1) {
                case 0:
                    //Belly breathing
                    intent.putExtra("TITLE", optionsList1[0]);
                    intent.putExtra("AUDIO_FILE", audiofilesList[3]);
                    intent.putExtra("AUDIO_FILE_NUMBER", 3);
                    AudioScreen.setMainLayoutBackground(getResources().getDrawable(R.drawable.breathing_background));
                    break;
                case 1:
                    //4-7-8
                    intent.putExtra("TITLE", optionsList1[1]);
                    intent.putExtra("AUDIO_FILE", audiofilesList[0]);
                    intent.putExtra("AUDIO_FILE_NUMBER", 0);
                    AudioScreen.setMainLayoutBackground(getResources().getDrawable(R.drawable.breathing_background));
                    break;
                case 2:
                    //abnormal
                    intent.putExtra("TITLE", optionsList1[2]);
                    intent.putExtra("AUDIO_FILE", audiofilesList[2]);
                    intent.putExtra("AUDIO_FILE_NUMBER", 2);
                    AudioScreen.setMainLayoutBackground(getResources().getDrawable(R.drawable.breathing_background));
                    break;
                case 3:
                    //Tense and relax
                    intent.putExtra("TITLE", optionsList1[3]);
                    intent.putExtra("AUDIO_FILE", audiofilesList[12]);
                    intent.putExtra("AUDIO_FILE_NUMBER", 12);
                    AudioScreen.setMainLayoutBackground(getResources().getDrawable(R.drawable.breathing_background));
                    break;
                case 4:
                    //Shake your body
                    intent.putExtra("TITLE", optionsList1[4]);
                    intent.putExtra("AUDIO_FILE", audiofilesList[11]);
                    intent.putExtra("AUDIO_FILE_NUMBER", 11);
                    AudioScreen.setMainLayoutBackground(getResources().getDrawable(R.drawable.breathing_background));
                    break;
                case 5:
                    //Calming meditation
                    intent.putExtra("TITLE", optionsList1[5]);
                    intent.putExtra("AUDIO_FILE", audiofilesList[4]);
                    intent.putExtra("AUDIO_FILE_NUMBER", 4);
                    AudioScreen.setMainLayoutBackground(getResources().getDrawable(R.drawable.breathing_background));
                    break;
                case 6:
                 //Self-compassion", "",  ""
                    intent.putExtra("TITLE", optionsList1[6]);
                    intent.putExtra("AUDIO_FILE", audiofilesList[9]);
                    intent.putExtra("AUDIO_FILE_NUMBER", 9);
                    AudioScreen.setMainLayoutBackground(getResources().getDrawable(R.drawable.breathing_background));
                    break;
/*
                case 7:
                    intent.putExtra("TITLE", optionsList1[7]);
                    intent.putExtra("AUDIO_FILE", audiofilesList[15]);
                    intent.putExtra("AUDIO_FILE_NUMBER", 15);
                    AudioScreen.setMainLayoutBackground(getResources().getDrawable(R.drawable.breathing_background));
                    break;
*/
                case 7:
                    //Body scan
                    intent.putExtra("TITLE", optionsList1[8]);
                    intent.putExtra("AUDIO_FILE", audiofilesList[5]);
                    intent.putExtra("AUDIO_FILE_NUMBER", 5);
                    AudioScreen.setMainLayoutBackground(getResources().getDrawable(R.drawable.breathing_background));
                    break;
                case 8:
                    //Stretching to relax your muscles"
                    intent.putExtra("TITLE", optionsList1[10]);
                    intent.putExtra("AUDIO_FILE", audiofilesList[11]);
                    AudioScreen.setMainLayoutBackground(getResources().getDrawable(R.drawable.breathing_background));
                    intent.putExtra("AUDIO_FILE_NUMBER", 11);
                    break;
                case 9:
                    //Have a Alone
                    intent = new Intent(this, ImageScreen.class);
                    intent.putExtra("IMAGE_ID", R.drawable.streach_background);
                    break;
                case 10:
                    //stress free
                    intent = new Intent(this, ImageScreen.class);
                    intent.putExtra("IMAGE_ID", R.drawable.have_a_alone_time);
                    break;
                case 11:
                    //Write a Gratitude journal
                    intent = new Intent(this, ImageScreen.class);
                    intent.putExtra("IMAGE_ID", R.drawable.make_a_free_zone_backgorund);
                    break;
                case 12:
                    //Jornal
                    intent = new Intent(this, ComposeGratitudeDiary.class);
                    break;
                case 13:
                    //Vent your burdens a
                    intent = new Intent(this, VentOutJournal.class);
                    break;
                case 14:
                    //ocean waves
                    intent = new Intent(this, VentoutRecording.class);
                    break;
                case 15:
                    //birds chipping
                    intent = new Intent(this, AudioScreen.class);
                    intent.putExtra("TITLE", optionsList1[15]);
                    intent.putExtra("AUDIO_FILE", audiofilesList[13]);
                    intent.putExtra("AUDIO_FILE_NUMBER", 13);
                    AudioScreen.setMainLayoutBackground(getResources().getDrawable(R.drawable.breathing_background));
                    break;
                case 16:
                    //rain
                    intent.putExtra("TITLE", optionsList1[16]);
                    intent.putExtra("AUDIO_FILE", audiofilesList[6]);
                    intent.putExtra("AUDIO_FILE_NUMBER", 6);
                    AudioScreen.setMainLayoutBackground(getResources().getDrawable(R.drawable.breathing_background));
                    break;
                case 17:
                    intent.putExtra("TITLE", optionsList1[17]);
                    intent.putExtra("AUDIO_FILE_NUMBER", 7);
                    intent.putExtra("AUDIO_FILE", audiofilesList[7]);
                    AudioScreen.setMainLayoutBackground(getResources().getDrawable(R.drawable.breathing_background));
                    break;
            }
            startActivity(intent);
        }
        else if (id == 2) {

        }
        else if (id == 3) {
            startActivity(new Intent(this, LoadCompleteList.class));
        }
    }

    @Override
    public void onDownloadButtonClicked(AudioFilesInfo audioFilesInfo) {

    }

    private void loadImages() {
        String fileDirPath = getFilesDir().getAbsolutePath() + File.separator + "HappyBeing";
        String relaxAndReleiveFilesList[] = DownloadAudioFilesService.relaxAndRelieveFileNames;
        for (int i = 0; i < relaxAndReleiveFilesList.length; i++) {
            File file1 = new File(fileDirPath, "/audiofiles/"+relaxAndReleiveFilesList[i]);
            //Log.i("Mindfulness","File name is "+file1);
            //Log.i("Mindfulness", "****File exists "+file1.exists());
            if (file1.exists()) {
                String filePath = file1.getAbsolutePath();
                audiofilesList[i] = filePath;
            }
            else {
                fileNotExist = true;
            }

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.cardview1:
                Intent intent = new Intent(this, AudioScreen.class);
                switch (option1) {
                    case 0:
                        //Belly breathing
                        intent.putExtra("TITLE", optionsList1[0]);
                        intent.putExtra("AUDIO_FILE", audiofilesList[3]);
                        intent.putExtra("AUDIO_FILE_NUMBER", 3);
                        AudioScreen.setMainLayoutBackground(getResources().getDrawable(R.drawable.breathing_background));
                        break;
                    case 1:
                        //4-7-8
                        intent.putExtra("TITLE", optionsList1[1]);
                        intent.putExtra("AUDIO_FILE", audiofilesList[0]);
                        intent.putExtra("AUDIO_FILE_NUMBER", 0);
                        AudioScreen.setMainLayoutBackground(getResources().getDrawable(R.drawable.breathing_background));
                        break;
                    case 2:
                        //abnormal
                        intent.putExtra("TITLE", optionsList1[2]);
                        intent.putExtra("AUDIO_FILE", audiofilesList[2]);
                        intent.putExtra("AUDIO_FILE_NUMBER", 2);
                        AudioScreen.setMainLayoutBackground(getResources().getDrawable(R.drawable.breathing_background));
                        break;
                    case 3:
                        //Tense and relax
                        intent.putExtra("TITLE", optionsList1[3]);
                        intent.putExtra("AUDIO_FILE", audiofilesList[12]);
                        intent.putExtra("AUDIO_FILE_NUMBER", 12);
                        AudioScreen.setMainLayoutBackground(getResources().getDrawable(R.drawable.breathing_background));
                        break;
                    case 4:
                        //Shake your body
                        intent.putExtra("TITLE", optionsList1[4]);
                        intent.putExtra("AUDIO_FILE", audiofilesList[11]);
                        intent.putExtra("AUDIO_FILE_NUMBER", 11);
                        AudioScreen.setMainLayoutBackground(getResources().getDrawable(R.drawable.breathing_background));
                        break;
                    case 5:
                        //Calming meditation
                        intent.putExtra("TITLE", optionsList1[5]);
                        intent.putExtra("AUDIO_FILE", audiofilesList[4]);
                        intent.putExtra("AUDIO_FILE_NUMBER", 4);
                        AudioScreen.setMainLayoutBackground(getResources().getDrawable(R.drawable.breathing_background));
                        break;
                    case 6:
                        //Self-compassion", "",  ""
                        intent.putExtra("TITLE", optionsList1[6]);
                        intent.putExtra("AUDIO_FILE", audiofilesList[9]);
                        intent.putExtra("AUDIO_FILE_NUMBER", 9);
                        AudioScreen.setMainLayoutBackground(getResources().getDrawable(R.drawable.breathing_background));
                        break;
/*
                case 7:
                    intent.putExtra("TITLE", optionsList1[7]);
                    intent.putExtra("AUDIO_FILE", audiofilesList[15]);
                    intent.putExtra("AUDIO_FILE_NUMBER", 15);
                    AudioScreen.setMainLayoutBackground(getResources().getDrawable(R.drawable.breathing_background));
                    break;
*/
                    case 7:
                        //Body scan
                        intent.putExtra("TITLE", optionsList1[8]);
                        intent.putExtra("AUDIO_FILE", audiofilesList[5]);
                        intent.putExtra("AUDIO_FILE_NUMBER", 5);
                        AudioScreen.setMainLayoutBackground(getResources().getDrawable(R.drawable.breathing_background));
                        break;
                    case 8:
                        //Stretching to relax your muscles"
                        intent.putExtra("TITLE", optionsList1[10]);
                        intent.putExtra("AUDIO_FILE", audiofilesList[11]);
                        AudioScreen.setMainLayoutBackground(getResources().getDrawable(R.drawable.breathing_background));
                        intent.putExtra("AUDIO_FILE_NUMBER", 11);
                        break;
                    case 9:
                        //Have a Alone
                        intent = new Intent(this, ImageScreen.class);
                        intent.putExtra("IMAGE_ID", R.drawable.streach_background);
                        break;
                    case 10:
                        //stress free
                        intent = new Intent(this, ImageScreen.class);
                        intent.putExtra("IMAGE_ID", R.drawable.have_a_alone_time);
                        break;
                    case 11:
                        //Write a Gratitude journal
                        intent = new Intent(this, ImageScreen.class);
                        intent.putExtra("IMAGE_ID", R.drawable.make_a_free_zone_backgorund);
                        break;
                    case 12:
                        //Jornal
                        intent = new Intent(this, ComposeGratitudeDiary.class);
                        break;
                    case 13:
                        //Vent your burdens a
                        intent = new Intent(this, VentOutJournal.class);
                        break;
                    case 14:
                        //ocean waves
                        intent = new Intent(this, VentoutRecording.class);
                        break;
                    case 15:
                        //birds chipping
                        intent = new Intent(this, AudioScreen.class);
                        intent.putExtra("TITLE", optionsList1[15]);
                        intent.putExtra("AUDIO_FILE", audiofilesList[13]);
                        intent.putExtra("AUDIO_FILE_NUMBER", 13);
                        AudioScreen.setMainLayoutBackground(getResources().getDrawable(R.drawable.breathing_background));
                        break;
                    case 16:
                        //rain
                        intent.putExtra("TITLE", optionsList1[16]);
                        intent.putExtra("AUDIO_FILE", audiofilesList[6]);
                        intent.putExtra("AUDIO_FILE_NUMBER", 6);
                        AudioScreen.setMainLayoutBackground(getResources().getDrawable(R.drawable.breathing_background));
                        break;
                    case 17:
                        intent.putExtra("TITLE", optionsList1[17]);
                        intent.putExtra("AUDIO_FILE_NUMBER", 7);
                        intent.putExtra("AUDIO_FILE", audiofilesList[7]);
                        AudioScreen.setMainLayoutBackground(getResources().getDrawable(R.drawable.breathing_background));
                        break;
                }
                startActivity(intent);
                break;
            case R.id.cardview3:
                startActivity(new Intent(this, LoadCompleteList.class));
                break;
        }
    }
}

