package com.azuyo.happybeing.Goals;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.Utils.MySql;
import com.azuyo.happybeing.ui.BaseActivity;
import com.azuyo.happybeing.ui.ProfileScreen;

public class ComposeNewGoal extends BaseActivity implements View.OnClickListener, SetTitleInterface {
    private SQLiteDatabase db;
    private String type = "";
    private LinearLayout buttonsLayout, customLayout, fitst_layout;
    private Button button1, button2, button3, button4, button5, button6, button7, button8, button9, button10, button11, button12,
            button13, button14, button15, button16, customButton, customDoneButton;
    private EditText customEdittext;
    private TextView titleText;
    private FrameLayout frameLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compose_new_goal);
        titleText = (TextView) findViewById(R.id.title_name);
        ImageView userAccount = (ImageView) findViewById(R.id.account_circle);
        userAccount.setVisibility(View.GONE);
        buttonsLayout = (LinearLayout) findViewById(R.id.health_options);
        fitst_layout = (LinearLayout) findViewById(R.id.fitst_layout);
        frameLayout = (FrameLayout) findViewById(R.id.goal_screen_fragment);
        button1 = (Button) findViewById(R.id.health1);
        button2 = (Button) findViewById(R.id.health2);
        button3 = (Button) findViewById(R.id.health3);
        button4 = (Button) findViewById(R.id.health4);
        button5 = (Button) findViewById(R.id.health5);
        button6 = (Button) findViewById(R.id.health6);
        button7 = (Button) findViewById(R.id.health7);
        button8 = (Button) findViewById(R.id.health8);
        button9 = (Button) findViewById(R.id.health9);
        button10 = (Button) findViewById(R.id.health10);
        button11 = (Button) findViewById(R.id.health11);
        button12 = (Button) findViewById(R.id.health12);
        button13 = (Button) findViewById(R.id.health13);
        button14 = (Button) findViewById(R.id.health14);
        button15 = (Button) findViewById(R.id.health15);
        button16 = (Button) findViewById(R.id.health16);
        customButton = (Button) findViewById(R.id.custom_goal);
        customDoneButton = (Button) findViewById(R.id.custom_text_done);

        customLayout = (LinearLayout) findViewById(R.id.custom_goal_layout);
        customEdittext = (EditText) findViewById(R.id.custom_title_edittext);

        MySql dbHelper = new MySql(this, "mydb", null, ProfileScreen.dbVersion);
        db = dbHelper.getWritableDatabase();
        Intent intent = getIntent();
        if (intent.hasExtra("TYPE")) {
            type = intent.getStringExtra("TYPE");
        }
        if (type != null) {
            getTypeOfGoal(type);
        }

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        button10.setOnClickListener(this);
        button11.setOnClickListener(this);
        button12.setOnClickListener(this);
        button13.setOnClickListener(this);
        button14.setOnClickListener(this);
        button15.setOnClickListener(this);
        button16.setOnClickListener(this);
        customButton.setOnClickListener(this);
        customDoneButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.text_item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.text_item:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getTypeOfGoal(String type) {
        if (type.equals("health")) {
            titleText.setText("Health");
            setVisibiityOfButton(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE);
            setTextOfButton( "Being more relaxed", "Drinking less coffee or tea", "Losing weight", "Learning deep relaxation techniques",
                    "Drinking less alcohol", "Consuming less cigarettes", "Manage pain better", "Getting better sleep",
                    "Getting massages", "Meeting specific health needs", "Getting more fit", "Eating less junk", "Run a marathon",
                    "Going for a trek", "Build strength", "Boost immunity");
        }
        else if (type.equals("work")) {
            titleText.setText("Career and work");
            setVisibiityOfButton(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE);
            setTextOfButton("Passing a exam", "Getting good scores", "Improve my performance", "Next promotion", "File a patent",
                    "Write a white paper", "Change of job", "Career move", "Salary rise", "Improve my quality of work",
                    "Getting better in meeting deadlines", "", "", "", "", "");
        }
        else if (type.equals("money")) {
            titleText.setText("Money and prosperity");
            setVisibiityOfButton(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE);
            setTextOfButton( "Start savings", "Start investing", "Buying a car", "Buying a bike", "Buying a property", "Build my dream house",
                    "Buy cattle", "Buy farmland", "Build my own office", "Improve my sales", "", "", "", "", "", "");
        }
        else if (type.equals("family")) {
            titleText.setText("Family and relationships");
            setVisibiityOfButton(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE, View.GONE,
                    View.GONE);
            setTextOfButton("Spend time with family", "Spend time with kids", "Spend more time with friends", "Improve my relation with a loved one",
                    "Be a better parent", "Attain respect in my family circles", "Take parents to vacation", "Send parents to vacation",
                    "Having family dinners", "Making friends", "Having better social network", "Forgiving someone", "Asking someone for forgiveness",
                    "", "", "");
        }
        else if (type.equals("spirituality")) {
            titleText.setText("Spirituality");
            setVisibiityOfButton(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE);
            setTextOfButton("Exploring my own beliefs around spirituality", "Making daily spiritual practices", "Join a spiritual group",
                    "Find a spiritual guru", "Understand my religion", "Read holy scriptures", "Practicing meditation", "", "", "", "", "",
                    "", "", "", "");
        }
        else if (type.equals("making_difference")) {
            titleText.setText("Making a differnce");
            setVisibiityOfButton(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE);
            setTextOfButton("Get involved in a fund raiser", "Support a cause by spending time and resources", "Talk to someone you know might be in distress",
                    "Be involved in a charity concert or a run",  "Find someone who needs a smile or a hug and give them", "", "", "", "", "",
                    "", "", "", "", "", "");
        }
        else if (type.equals("recreation")) {
            titleText.setText("Recreation");
            setVisibiityOfButton(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE);
            setTextOfButton("Reading a particular book", "Learning a hobby, Resuming a hobby", "Participating in a play", "Performing an art",
                    "Solving puzzles", "Learning new things", "Study to improve knowledge", "Personally develop a new habit", "Travel", "Learn a new language",
                    "", "", "", "", "", "");
        }
        else if (type.equals("intellect")) {
            titleText.setText("Intellect");
            setVisibiityOfButton(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE);
            setTextOfButton("Reading a particular book", "Learning a hobby, Resuming a hobby", "Participating in a play", "Performing an art",
                    "Solving puzzles", "Learning new things", "Study to improve knowledge", "Personally develop a new habit", "Travel", "Learn a new language",
                    "", "", "", "", "", "");
        }
    }

    private void setVisibiityOfButton(int visibility1,int visibility2,int visibility3,int visibility4,int visibility5,int visibility6,
                                      int visibility7,int visibility8,int visibility9,int visibility10,int visibility11,int visibility12,
                                      int visibility13,int visibility14,int visibility15,int visibility16) {
        button1.setVisibility(visibility1);
        button2.setVisibility(visibility2);
        button3.setVisibility(visibility3);
        button4.setVisibility(visibility4);
        button5.setVisibility(visibility5);
        button6.setVisibility(visibility6);
        button7.setVisibility(visibility7);
        button8.setVisibility(visibility8);
        button9.setVisibility(visibility9);
        button10.setVisibility(visibility10);
        button11.setVisibility(visibility11);
        button12.setVisibility(visibility12);
        button13.setVisibility(visibility13);
        button14.setVisibility(visibility14);
        button15.setVisibility(visibility15);
        button16.setVisibility(visibility16);

    }

    private void setTextOfButton(String string1, String string2, String string3, String string4, String string5, String string6,
                                 String string7, String string8, String string9, String string10, String string11, String string12,
                                 String string13, String string14, String string15, String string16) {
        button1.setText(string1);
        button2.setText(string2);
        button3.setText(string3);
        button4.setText(string4);
        button5.setText(string5);
        button6.setText(string6);
        button7.setText(string7);
        button8.setText(string8);
        button9.setText(string9);
        button10.setText(string10);
        button11.setText(string11);
        button12.setText(string12);
        button13.setText(string13);
        button14.setText(string14);
        button15.setText(string15);
        button16.setText(string16);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String title = "Happy being";
        if (id == R.id.custom_goal) {
            navigateToNextScreen("");
        }
        else if (id == R.id.custom_text_done) {
            title = customEdittext.getText().toString();
            navigateToNextScreen(title);
        }
        else if (id == R.id.health1){
            title = button1.getText().toString();
            navigateToNextScreen(title);
        }
        else if (id == R.id.health2){
            title = button2.getText().toString();
            navigateToNextScreen(title);
        }
        else if (id == R.id.health3){
            title = button3.getText().toString();
            navigateToNextScreen(title);
        }
        else if (id == R.id.health4){
            title = button4.getText().toString();
            navigateToNextScreen(title);
        }
        else if (id == R.id.health5){
            title = button5.getText().toString();
            navigateToNextScreen(title);
        }
        else if (id == R.id.health6){
            title = button6.getText().toString();
            navigateToNextScreen(title);
        }
        else if (id == R.id.health7){
            title = button7.getText().toString();
            navigateToNextScreen(title);
        }
        else if (id == R.id.health8){
            title = button8.getText().toString();
            navigateToNextScreen(title);
        }
        else if (id == R.id.health9){
            title = button9.getText().toString();
            navigateToNextScreen(title);
        }
        else if (id == R.id.health10){
            title = button10.getText().toString();
            navigateToNextScreen(title);
        }
        else if (id == R.id.health11){
            title = button11.getText().toString();
            navigateToNextScreen(title);
        }
        else if (id == R.id.health12){
            title = button12.getText().toString();
            navigateToNextScreen(title);
        }
        else if (id == R.id.health13){
            title = button13.getText().toString();
            navigateToNextScreen(title);
        }
        else if (id == R.id.health14){
            title = button14.getText().toString();
            navigateToNextScreen(title);
        }
        else if (id == R.id.health15){
            title = button15.getText().toString();
            navigateToNextScreen(title);
        }
        else if (id == R.id.health16){
            title = button16.getText().toString();
            navigateToNextScreen(title);
        }
    }

    private void navigateToNextScreen(String title) {
        long dbId = doSaveToLocalDB();
        frameLayout.setVisibility(View.VISIBLE);
        fitst_layout.setVisibility(View.GONE);
        Bundle bundle = new Bundle();
        bundle.putLong("dbId", dbId);
        bundle.putString("TITLE", title);
        bundle.putString("TYPE", type);
        Goal_Time_fragment goal_time_fragment = new Goal_Time_fragment();
        goal_time_fragment.setArguments(bundle);
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.add(R.id.goal_screen_fragment, goal_time_fragment, "FIRST_FRAGMENT");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private long doSaveToLocalDB() {
        ContentValues cv = new ContentValues();
        cv.put("TITLE", titleText.getText().toString());
        long id = db.insert("Goals_Table", null, cv);
        return id;
    }

    @Override
    public void onBackPressed() {
        if (frameLayout.getVisibility() == View.VISIBLE) {
            //Log.i("ProfileScreen","Fragement is visible");
            frameLayout.setVisibility(View.GONE);
            fitst_layout.setVisibility(View.VISIBLE);
        }
        else {
            finish();
        }
    }

    @Override
    public void setTitleText(String text) {
        if (text != null) {
            titleText.setText(text);
        }
    }

    /*
    private void doSaveToLocalDB() {
        String title = title_edit_text.getText().toString();
        String stepsToAcheive = steps_to_acheive_edit_text.getText().toString();
        long time= System.currentTimeMillis();
        ContentValues cv = new ContentValues();
        cv.put("TITLE", title);
        cv.put("STEPS_TO_ACHIEVE", stepsToAcheive);
        cv.put("TIME", time);
        cv.put("IMAGE_URI", logoUri.toString());
        cv.put("TYPE", type);
        long id = db.insert("Goals_Table", null, cv);
        Toast.makeText(this, "Entry saved.",Toast.LENGTH_SHORT).show();
        Log.i("ComposeGraritude","Inserted into table "+id);
        finish();
    }
*/
}