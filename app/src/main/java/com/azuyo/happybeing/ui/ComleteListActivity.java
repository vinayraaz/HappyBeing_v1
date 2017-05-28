package com.azuyo.happybeing.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.adapter.CompleteListAdapter;
import com.azuyo.happybeing.events.DownloadAudioFilesService;


/**
 * Created by nSmiles on 5/24/2017.
 */

public class ComleteListActivity extends BaseActivity{
    RecyclerView rv_complete_list;
    private RecyclerView.LayoutManager completelistLayoutManager;
    CompleteListAdapter completeListAdapter;
   // private String[] audiofilesList = new String[DownloadAudioFilesService.relaxAndRelieveFileNames.length];
    public static String[] text_value_list = {"Belly breathing", "4-7-8 breathing", "Abdominal breathing", "Tense and relax",
            "Shake your body", "Calming meditation", "Self-compassion", "Body scan", "Self massage",
            "Stretching to relax your muscles", "Have a Alone time to collect your thoughts and clear your head",
            "Make or create a stress free zone and relax there for a while till you feel calm", "Write a Gratitude journal",
            "Journal your thoughts and feelings", "Vent your burdens and disappointments", "Listen to Ocean waves and relax",
            "Listen to Birds chirping and relax", "Listen to Sounds of rain and relax","Drop some cold water on your wrists and behind your earlobes. Feel the sense of calmness",
            "Pinch the length of earlobes and feel sense of relaxation", "Brush your hair", "Rub Your Feet Over a Golf Ball",
            "Squeeze a Stress Ball", "Make or create a stress free zone and relax there for a while till you feel calm",
            "Head outside and get some bright sun", "Take a five-minute break to do nothing but stare out the window to a natural scene like a park or a tree",
            "Take a time out to de-clutter your working desk", "Dance to your favourite song - Tap you legs and move your body in tune with the music",
            "Walk for 50 steps and grab some  Water while you clear your thoughts", "Hydrate yourself with some Pure coconut water", "Drink Hot or cold milk while you relax",
            "Sip green tea and relax", "Have a whole fruit and relax", "Have a square of dark chocolate and pleasure your senses", "Drink Fresh vegetable juice",
            "Try a spoonful of honey and relinquish the taste on your tongue as it's calm you", "Take a chewing gum",
            "Handful of nuts like organic pistachios, cashewnuts, walnuts, almonds, raisins", "Munch a crunchy snack and relax", "Have a Fresh Vegetable salad to energise yourself",
            "Have an Ice cream and relax by rejoicing the taste", "Smell some flowers and let the fragrance soothe your nerves", "Dab some essential oil on your palm and inhale for relaxing your senses",
            "Smell some citrus fruits - oranges, lemons and get energised", "Smell some coffee beans and let the smell of coffee soothe you.",
            "Have a fresh fruit salad and let the sweetness of fruits energise you.", "Cuddle a pet and feel relaxed"};
    public static int image_value_list[] = {R.drawable.belly_breathing, R.drawable.breathing_4_7_8, R.drawable.abdominal_breathing,
            R.drawable.tense_and_relax, R.drawable.shake_your_body, R.drawable.calming_meditation, R.drawable.self_compassion,
            R.drawable.body_scan, R.drawable.self_massage, R.drawable.stretching_to_relax_your_muscles,
            R.drawable.have_a_alone, R.drawable.make_free_zone, R.drawable.journal_your_thoughts, R.drawable.vent_journal,
            R.drawable.vent, R.drawable.ocean_waves, R.drawable.birds_chirping, R.drawable.sounds_of_rain,
            R.drawable.drop_some_cold, R.drawable.tense_and_relax, R.drawable.brush_hair, R.drawable.rub_your_golf_ball,
            R.drawable.tense_and_relax, R.drawable.make_free_zone, R.drawable.head_outside, R.drawable.take_five_minute,
            R.drawable.take_five_minute,R.drawable.dance_song, R.drawable.walk, R.drawable.coconut_water,
            R.drawable.hot_or_cold_milk, R.drawable.sip_green_tea,R.drawable.whole_fruit, R.drawable.chochlate,
            R.drawable.fresh_veg_juice, R.drawable.honey, R.drawable.chewing_gum, R.drawable.handful_nuts,
            R.drawable.crunchy_snack, R.drawable.veg_salad, R.drawable.ice_cream, R.drawable.smell_some_flowers,
            R.drawable.dab_some_oil, R.drawable.smell_lemons, R.drawable.smell_some_coffee_beans,
            R.drawable.have_fruit_salad, R.drawable.pet_curddle};
    public static String[] relaxAndRelieveFiles_new = {"http://mentalhealthcure.com/v1/download/relieve-and-relax-list/BELLY-BREATHING.mp3",
             "http://mentalhealthcure.com/v1/download/relieve-and-relax-list/4-7-8-BREATHING.mp3",
             "http://mentalhealthcure.com/v1/download/relieve-and-relax-list/Abdominal-Breathing.mp3",
             "http://mentalhealthcure.com/v1/download/relieve-and-relax-list/Tense-and-relax.mp3",
             "http://mentalhealthcure.com/v1/download/relieve-and-relax-list/Shake-ur-Body.mp3",
             "http://mentalhealthcure.com/v1/download/relieve-and-relax-list/Calmness-meditation.mp3",
             "http://mentalhealthcure.com/v1/download/relieve-and-relax-list/self-comapssion-meditation.mp3",
             "http://mentalhealthcure.com/v1/download/relieve-and-relax-list/Mindful-Body-Scan.mp3",
             "http://mentalhealthcure.com/v1/download/relieve-and-relax-list/Self-Massage.mp3",
             "", "", "", "", "", "",
             "http://mentalhealthcure.com/v1/download/relieve-and-relax-list/Ocean-Waves-Loop.mp4",
             "http://mentalhealthcure.com/v1/download/relieve-and-relax-list/nature_calms.mp3",
             "http://mentalhealthcure.com/v1/download/relieve-and-relax-list/rain.mp3","", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
    };
   /* public static String[] relaxAndRelieveFiles_new = {"BELLY-BREATHING.mp3","4-7-8-BREATHING.mp3","Abdominal-Breathing.mp3",
            "Tense-and-relax.mp3", "Shake-ur-Body.mp3", "Calmness-meditation.mp3","self-comapssion-meditation.mp3",
            "Mindful-Body-Scan.mp3","Self-Massage.mp3","","","","","","","Ocean-Waves-Loop.mp4", "nature_calms.mp3","rain.mp3",
            "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
    };
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_complete_list_layout);
        loadlistviewmethod(text_value_list,image_value_list,relaxAndRelieveFiles_new);
    }

    private void loadlistviewmethod(String[] text_value_list, int[] image_value_list, String[] relaxAndRelieveFiles_new) {
        rv_complete_list = (RecyclerView) findViewById(R.id.rv_bookingticket);
        rv_complete_list.setHasFixedSize(true);
        completelistLayoutManager = new LinearLayoutManager(ComleteListActivity.this);
        rv_complete_list.setLayoutManager(completelistLayoutManager);
        completeListAdapter = new CompleteListAdapter(ComleteListActivity.this, text_value_list,image_value_list,relaxAndRelieveFiles_new);
        rv_complete_list.setAdapter(completeListAdapter);
        rv_complete_list.getAdapter().notifyDataSetChanged();
    }


}
