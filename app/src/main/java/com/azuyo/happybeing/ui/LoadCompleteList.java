package com.azuyo.happybeing.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.Views.AudioFilesInfo;
import com.azuyo.happybeing.Views.CustomRelaxAdapter;
import com.azuyo.happybeing.events.DownloadAudioFilesService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 17-03-2017.
 */
public class LoadCompleteList extends BaseActivity implements OnItemSelectedListener {
    private ListView relaxListView, relaxByBodyListView, takeTimeOutListView, expressListView, nourishListView;
    private List<AudioFilesInfo> relaxList, relaxByBodyList, relaxByTimeOut, relaxByExpress, relaxByNourish;
    private String[] audiofilesList = new String[DownloadAudioFilesService.relaxAndRelieveFileNames.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complete_list_layout);
        relaxListView = (ListView) findViewById(R.id.relaxListView);
        relaxByBodyListView = (ListView) findViewById(R.id.relaxbyBodyListView);
        takeTimeOutListView = (ListView) findViewById(R.id.takeTimeOutListView);
        expressListView = (ListView) findViewById(R.id.expressListView);
        nourishListView = (ListView) findViewById(R.id.nourishListView);

        loadImages();
        loadRelaxListView();
        CustomRelaxAdapter customRelaxAdapter = new CustomRelaxAdapter(this, relaxList, this);
        relaxListView.setAdapter(customRelaxAdapter);

        relaxbyBodyList();
        CustomRelaxAdapter customRelaxAdapter1 = new CustomRelaxAdapter(this, relaxByBodyList, this);
        relaxByBodyListView.setAdapter(customRelaxAdapter1);

        relaxByTimeOut();
        CustomRelaxAdapter customRelaxAdapter2 = new CustomRelaxAdapter(this, relaxByTimeOut, this);
        takeTimeOutListView.setAdapter(customRelaxAdapter2);

        relaxByExpressList();
        CustomRelaxAdapter customRelaxAdapter3 = new CustomRelaxAdapter(this, relaxByExpress, this);
        expressListView.setAdapter(customRelaxAdapter3);

        relaxByNourishList();
        CustomRelaxAdapter customRelaxAdapter4 = new CustomRelaxAdapter(this, relaxByNourish, this);
        nourishListView.setAdapter(customRelaxAdapter4);

        setListViewHeightBasedOnChildren(relaxListView);
        setListViewHeightBasedOnChildren(relaxByBodyListView);
        setListViewHeightBasedOnChildren(takeTimeOutListView);
        setListViewHeightBasedOnChildren(expressListView);
        setListViewHeightBasedOnChildren(nourishListView);

    }

    private void loadRelaxListView() {
        relaxList = new ArrayList<>();
        AudioFilesInfo audioFilesInfo1 = new AudioFilesInfo(1, getResources().getDrawable(R.drawable.belly_breathing), "Belly breathing", "");
        AudioFilesInfo audioFilesInfo2 = new AudioFilesInfo(2, getResources().getDrawable(R.drawable.breathing_4_7_8), "4-7-8 breathing", "");
        AudioFilesInfo audioFilesInfo3 = new AudioFilesInfo(3, getResources().getDrawable(R.drawable.abdominal_breathing), "Abdominal breathing", "");
        AudioFilesInfo audioFilesInfo4 = new AudioFilesInfo(4, getResources().getDrawable(R.drawable.tense_and_relax), "Tense and relax", "");
        AudioFilesInfo audioFilesInfo5 = new AudioFilesInfo(5, getResources().getDrawable(R.drawable.shake_your_body), "Shake your body", "");
        AudioFilesInfo audioFilesInfo6 = new AudioFilesInfo(6, getResources().getDrawable(R.drawable.calming_meditation), "Calming meditation", "");
        AudioFilesInfo audioFilesInfo7 = new AudioFilesInfo(7, getResources().getDrawable(R.drawable.self_compassion), "Self-compassion", "");
        AudioFilesInfo audioFilesInfo9 = new AudioFilesInfo(9, getResources().getDrawable(R.drawable.body_scan), "Body scan", "");
        AudioFilesInfo audioFilesInfo11 = new AudioFilesInfo(11, getResources().getDrawable(R.drawable.self_massage), "Self massage", "");
        relaxList.add(audioFilesInfo1);
        relaxList.add(audioFilesInfo2);
        relaxList.add(audioFilesInfo3);
        relaxList.add(audioFilesInfo4);
        relaxList.add(audioFilesInfo5);
        relaxList.add(audioFilesInfo6);
        relaxList.add(audioFilesInfo7);
        relaxList.add(audioFilesInfo9);
        relaxList.add(audioFilesInfo11);
    }

    private void relaxbyBodyList() {
        relaxByBodyList = new ArrayList<>();
        AudioFilesInfo audioFilesInfo12 = new AudioFilesInfo(19, getResources().getDrawable(R.drawable.stretching_to_relax_your_muscles), "Stretching to relax your muscles", true);
        AudioFilesInfo audioFilesInfo13 = new AudioFilesInfo(17, getResources().getDrawable(R.drawable.drop_some_cold), "Drop some cold water on your wrists and behind your earlobes", false);
        AudioFilesInfo audioFilesInfo14 = new AudioFilesInfo(17, getResources().getDrawable(R.drawable.brush_hair), "Brush your hair", false);
        AudioFilesInfo audioFilesInfo15 = new AudioFilesInfo(17, getResources().getDrawable(R.drawable.rub_your_golf_ball), "Rub Your Feet Over a Golf Ball", false);
        AudioFilesInfo audioFilesInfo16 = new AudioFilesInfo(17, getResources().getDrawable(R.drawable.squeeze_stress_ball), "Squeeze a Stress Ball", false);
        relaxByBodyList.add(audioFilesInfo12);
        relaxByBodyList.add(audioFilesInfo13);
        relaxByBodyList.add(audioFilesInfo14);
        relaxByBodyList.add(audioFilesInfo15);
        relaxByBodyList.add(audioFilesInfo16);
    }

    private void relaxByTimeOut() {
        relaxByTimeOut = new ArrayList<>();
        AudioFilesInfo audioFilesInfo17 = new AudioFilesInfo(20, getResources().getDrawable(R.drawable.have_a_alone), "Have a Alone time to collect your thoughts and clear your head", true);
        AudioFilesInfo audioFilesInfo18 = new AudioFilesInfo(21, getResources().getDrawable(R.drawable.make_free_zone), "Make or create a stress free zone and relax there for a while till you feel calm", true);
        AudioFilesInfo audioFilesInfo19 = new AudioFilesInfo(17, getResources().getDrawable(R.drawable.head_outside), "Head outside and get some bright sun", false);
        AudioFilesInfo audioFilesInfo20 = new AudioFilesInfo(17, getResources().getDrawable(R.drawable.take_five_minute), "Take a five-minute break to do nothing but stare out the window to a natural scene like a park or a tree", false);
        AudioFilesInfo audioFilesInfo21 = new AudioFilesInfo(18, getResources().getDrawable(R.drawable.journal_your_thoughts), "Write a gratitude journal", true);
        AudioFilesInfo audioFilesInfo22 = new AudioFilesInfo(22, getResources().getDrawable(R.drawable.vent_journal),"Journal your thoughts and feelings", true);
        relaxByTimeOut.add(audioFilesInfo17);
        relaxByTimeOut.add(audioFilesInfo18);
        relaxByTimeOut.add(audioFilesInfo19);
        relaxByTimeOut.add(audioFilesInfo20);
        relaxByTimeOut.add(audioFilesInfo21);
        relaxByTimeOut.add(audioFilesInfo22);
    }
    private void relaxByExpressList() {
        relaxByExpress = new ArrayList<>();
        AudioFilesInfo audioFilesInfo1 = new AudioFilesInfo(12, getResources().getDrawable(R.drawable.vent), "Vent your burdens and disappointments", "");
        AudioFilesInfo audioFilesInfo24 = new AudioFilesInfo(17, getResources().getDrawable(R.drawable.dance_song), "Dance to your favourite song - Tap you legs and move your body intune with the music", false);
        AudioFilesInfo audioFilesInfo25 = new AudioFilesInfo(13, getResources().getDrawable(R.drawable.listen_to_nature_calms), "Nature calms", "");
        AudioFilesInfo audioFilesInfo26 = new AudioFilesInfo(14, getResources().getDrawable(R.drawable.ocean_waves), "Listen to Ocean waves", "");
        AudioFilesInfo audioFilesInfo27 = new AudioFilesInfo(15, getResources().getDrawable(R.drawable.birds_chirping), "Listen to Birds chirping", "");
        AudioFilesInfo audioFilesInfo28 = new AudioFilesInfo(16, getResources().getDrawable(R.drawable.sounds_of_rain), "Listen to Sounds of rain and relax", "");
        AudioFilesInfo audioFilesInfo29 = new AudioFilesInfo(17, getResources().getDrawable(R.drawable.pet_curddle), "Cuddle a pet and feel relaxed", false);

        relaxByExpress.add(audioFilesInfo1);
        relaxByExpress.add(audioFilesInfo24);
        relaxByExpress.add(audioFilesInfo25);
        relaxByExpress.add(audioFilesInfo26);
        relaxByExpress.add(audioFilesInfo27);
        relaxByExpress.add(audioFilesInfo28);
        relaxByExpress.add(audioFilesInfo29);

    }
    private void relaxByNourishList() {
        relaxByNourish = new ArrayList<>();

        AudioFilesInfo audioFilesInfo28 = new AudioFilesInfo(17, getResources().getDrawable(R.drawable.walk), "Walk for 50 steps and grab some  Water while you clear your thoughts", false);
        AudioFilesInfo audioFilesInfo29 = new AudioFilesInfo(17, getResources().getDrawable(R.drawable.coconut_water), "Hydrate yourself with some Pure coconut water", false);
        AudioFilesInfo audioFilesInfo30 = new AudioFilesInfo(17, getResources().getDrawable(R.drawable.hot_or_cold_milk), "Drink Hot or cold milk while you relax", false);
        AudioFilesInfo audioFilesInfo31 = new AudioFilesInfo(17, getResources().getDrawable(R.drawable.sip_green_tea), "Sip green tea and relax", false);
        AudioFilesInfo audioFilesInfo32 = new AudioFilesInfo(17, getResources().getDrawable(R.drawable.fresh_veg_juice), "Drink Fresh vegetable juice and relax", false);
        AudioFilesInfo audioFilesInfo33 = new AudioFilesInfo(17, getResources().getDrawable(R.drawable.whole_fruit), "Have a whole fruit and relax", false);
        AudioFilesInfo audioFilesInfo34 = new AudioFilesInfo(17, getResources().getDrawable(R.drawable.chochlate), "Have a square of dark chocolate and pleasure your senses", false);
        AudioFilesInfo audioFilesInfo35 = new AudioFilesInfo(17, getResources().getDrawable(R.drawable.honey), "Try a spoonful of honey and relinquish the taste on your tongue as it's calm you", false);
        AudioFilesInfo audioFilesInfo36 = new AudioFilesInfo(17, getResources().getDrawable(R.drawable.chewing_gum), "Take a chewing gum and feel the pressure off from your mind", false);
        AudioFilesInfo audioFilesInfo37 = new AudioFilesInfo(17, getResources().getDrawable(R.drawable.handful_nuts), "Handful of nuts like organic pistachios, cashewnuts, walnuts, almonds, raisins", false);
        AudioFilesInfo audioFilesInfo38 = new AudioFilesInfo(17, getResources().getDrawable(R.drawable.crunchy_snack), "Munch a crunchy snack and relax", false);
        AudioFilesInfo audioFilesInfo39 = new AudioFilesInfo(17, getResources().getDrawable(R.drawable.veg_salad), "Have a Fresh Vegetable salad to energise yourself", false);
        AudioFilesInfo audioFilesInfo40 = new AudioFilesInfo(17, getResources().getDrawable(R.drawable.ice_cream), "Have an Ice cream by rejoicing the taste", false);
        AudioFilesInfo audioFilesInfo41 = new AudioFilesInfo(17, getResources().getDrawable(R.drawable.smell_some_flowers), "Smell some flowers and let the fragrance soothe your nerves", false);
        AudioFilesInfo audioFilesInfo42 = new AudioFilesInfo(17, getResources().getDrawable(R.drawable.dab_some_oil), "Dab some lavender or lemon or some essential oil on your palm and inhale for relaxing your senses", false);
        AudioFilesInfo audioFilesInfo43 = new AudioFilesInfo(17, getResources().getDrawable(R.drawable.smell_lemons), "Smell some citrus fruits - oranges, lemons and get energised", false);
        AudioFilesInfo audioFilesInfo44 = new AudioFilesInfo(17, getResources().getDrawable(R.drawable.smell_some_coffee_beans), "Smell some coffee beans and let the smell of coffee soothe you.", false);
        relaxByNourish.add(audioFilesInfo28);
        relaxByNourish.add(audioFilesInfo29);
        relaxByNourish.add(audioFilesInfo30);
        relaxByNourish.add(audioFilesInfo31);
        relaxByNourish.add(audioFilesInfo32);
        relaxByNourish.add(audioFilesInfo33);
        relaxByNourish.add(audioFilesInfo34);
        relaxByNourish.add(audioFilesInfo35);
        relaxByNourish.add(audioFilesInfo36);
        relaxByNourish.add(audioFilesInfo37);
        relaxByNourish.add(audioFilesInfo38);
        relaxByNourish.add(audioFilesInfo39);
        relaxByNourish.add(audioFilesInfo40);
        relaxByNourish.add(audioFilesInfo41);
        relaxByNourish.add(audioFilesInfo42);
        relaxByNourish.add(audioFilesInfo43);
        relaxByNourish.add(audioFilesInfo44);


    }

    @Override
    public void onItemSelected(AudioFilesInfo audioFilesInfo) {
        int id = audioFilesInfo.getId();
        switch (id) {
            case 1:
                gotoAudioPlayingActivity(audioFilesInfo.getTextView(), audiofilesList[3], R.drawable.breathing_background);
                break;
            case 2:
                gotoAudioPlayingActivity(audioFilesInfo.getTextView(), audiofilesList[0], R.drawable.breathing_background);
                break;
            case 3:
                gotoAudioPlayingActivity(audioFilesInfo.getTextView(), audiofilesList[2], R.drawable.breathing_background);
                break;
            case 4:
                gotoAudioPlayingActivity(audioFilesInfo.getTextView(), audiofilesList[12], R.drawable.breathing_background);
                break;
            case 5:
                gotoAudioPlayingActivity(audioFilesInfo.getTextView(), audiofilesList[11], R.drawable.breathing_background);
                break;
            case 6:
                gotoAudioPlayingActivity(audioFilesInfo.getTextView(), audiofilesList[4], R.drawable.breathing_background);
                break;
            case 7:
                gotoAudioPlayingActivity(audioFilesInfo.getTextView(), audiofilesList[9], R.drawable.breathing_background);
                break;
            case 8:
                gotoAudioPlayingActivity(audioFilesInfo.getTextView(), audiofilesList[15], R.drawable.breathing_background);
                break;
            case 9:
                gotoAudioPlayingActivity(audioFilesInfo.getTextView(), audiofilesList[5], R.drawable.breathing_background);
                break;
            case 10:
                gotoAudioPlayingActivity(audioFilesInfo.getTextView(), audiofilesList[14], R.drawable.breathing_background);
                break;
            case 11:
                gotoAudioPlayingActivity(audioFilesInfo.getTextView(), audiofilesList[10], R.drawable.breathing_background);
                break;
            case 12:
                startActivity(new Intent(this, VentoutRecording.class));
                break;
            case 13:
                startActivity(new Intent(this, NatureCalms.class));
                break;
            case 14:
                //TODO: change audio file to ocean waves
                gotoAudioPlayingActivity(audioFilesInfo.getTextView(), audiofilesList[13], R.drawable.breathing_background);
                break;
            case 15:
                //TODO: change audio file to Birds chipping
                gotoAudioPlayingActivity(audioFilesInfo.getTextView(), audiofilesList[6], R.drawable.breathing_background);
                break;
            case 16:
                //TODO: change audio file to rain
                gotoAudioPlayingActivity(audioFilesInfo.getTextView(), audiofilesList[7], R.drawable.breathing_background);
                break;

            case 17:
                break;
            case 18:
                startActivity(new Intent(this, ComposeGratitudeDiary.class));
                break;
            case 19:
                Intent intent = new Intent(this, ImageScreen.class);
                intent.putExtra("IMAGE_ID", R.drawable.streach_background);
                startActivity(intent);
                break;
            case 20:
                intent = new Intent(this, ImageScreen.class);
                intent.putExtra("IMAGE_ID", R.drawable.have_a_alone_time);
                startActivity(intent);
                break;
            case 21:
                intent = new Intent(this, ImageScreen.class);
                intent.putExtra("IMAGE_ID", R.drawable.make_a_free_zone_backgorund);
                startActivity(intent);
                break;
            case 22:
                intent = new Intent(this, VentOutJournal.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDownloadButtonClicked(AudioFilesInfo audioFilesInfo) {
    }

    private void gotoAudioPlayingActivity(String title, String audioFile, int drawable) {
        Intent intent = new Intent(this, AudioScreen.class);
        intent.putExtra("TITLE", title);
        intent.putExtra("AUDIO_FILE", audioFile);
        AudioScreen.setMainLayoutBackground(getResources().getDrawable(drawable));
        startActivity(intent);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
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
                boolean fileNotExist = true;
            }

        }
    }


}
