package com.azuyo.happybeing.ui;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.Utils.CommonUtils;
import com.azuyo.happybeing.Utils.MySql;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class ComposeGratitudeDiary extends BaseActivity implements View.OnClickListener {
    private SQLiteDatabase db;
    private EditText titleEditText;
   // private SmartAudioRecorder smartAudioRecorder;
    private String type;
    private Dialog dialog_custom;
    private int stored_date = 02;
    private Button add_edittext_button;
    private String[] hintText = {"Parents", "Family – Husband, siblings, kids, parents", "Friends", "People who willing to teach", "Ability to learn",
    "Ability to Work", "Having Money in bank", "Opportunity to get education", "Things I possess", "Things in my home", "Things I hear", "Things I see",
            "Things I see", "Things I touch/feel", "Things I smell", "Things I taste", "Heart", "Lungs", "Immune system", "Hands", "Legs", "Mind",
            "Good health", "Mentors", "The Sun , The moon and stars", "Seasons", "3 inventions", "Oxygen", "Water", "Food to eat", "Nature"};
    private Random mRand = new Random();
    private LinearLayout editTextLayout;
    private AdView mAdView = null;
    private long dbId = 0;
    private EditText contentEdittext, contentEdittext2, contentEdittext3;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compose_gratitude_diary);
        final TextView titleText = (TextView) findViewById(R.id.title_name);
        titleText.setText("Compose Thankyou notes");
        ImageView userAccount = (ImageView) findViewById(R.id.account_circle);
        userAccount.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(true);
        addGoogleAds();
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df1 = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate1 = df1.format(c.getTime());
        String[] formatedDates = formattedDate1.split("-");
        int onlyDate = Integer.parseInt(formatedDates[0]);
        int daycount = onlyDate - stored_date;
        Intent intent = getIntent();
        MySql dbHelper = new MySql(this, "mydb", null, ProfileScreen.dbVersion);
        db = dbHelper.getWritableDatabase();
        titleEditText = (EditText) findViewById(R.id.title_edittext);

        //titleEditText.addTextChangedListener(textWatcher);
        contentEdittext = (EditText) findViewById(R.id.content_editext);
        contentEdittext2 = (EditText) findViewById(R.id.content_editext2);
        contentEdittext3 = (EditText) findViewById(R.id.content_editext3);
        contentEdittext.addTextChangedListener(textWatcher);
        contentEdittext2.addTextChangedListener(textWatcher);
        contentEdittext3.addTextChangedListener(textWatcher);

        add_edittext_button = (Button) findViewById(R.id.add_edittext_button);
        add_edittext_button.setOnClickListener(this);
        editTextLayout = (LinearLayout) findViewById(R.id.edit_texts_layout);
        //  smartAudioRecorder = (SmartAudioRecorder) findViewById(R.id.recorder_smart_recorder);
        if (intent.hasExtra("dbId")) {
            dbId = intent.getLongExtra("dbId", 0);
            loadDetailsFromDB(dbId);
            titleText.setText("Edit gratitude dairy");
        }
        titleEditText.setHint(hintText[mRand.nextInt(hintText.length)]);
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
                if (checkValidation()) {
                    doSaveToLocalDB();
                }
                else {
                    Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.add_edittext_button:
                addLinearLayout();
                break;
        }
    }

    private void loadDetailsFromDB(long dbId) {
        Cursor cursor = db.rawQuery("SELECT * FROM Gratitude_Table where _id=" + "'" + dbId + "'", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String title = cursor.getString(cursor.getColumnIndex("TITLE"));
            String content = cursor.getString(cursor.getColumnIndex("CONTENT"));
            titleEditText.setText(title);
            contentEdittext.setText(content);
        }
    }
    private void addLinearLayout() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 130));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(10, 10, 10, 0);
        TextView numberText = new TextView(this);
        //numberText.setLayoutParams(new TextView.Params(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        numberText.setText(Integer.toString(editTextLayout.getChildCount()+1)+".");
        EditText editText = new EditText(this);
        editText.setMinLines(3);
        editText.setBackgroundResource(R.drawable.edit_text);
        editText.setId(editTextLayout.getChildCount()+1);
        editText.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
        editText.setPadding(15, 0, 0, 0);
        editText.setHint("Enter notes");
        linearLayout.addView(numberText);
        linearLayout.addView(editText, lp);
        editTextLayout.addView(linearLayout);

    }

    private void doSaveToLocalDB() {
        String recording_file_path = "";
        String title = titleEditText.getText().toString();
        String content = contentEdittext.getText().toString();
        long time= System.currentTimeMillis();
        String contentCompleteString = "";
        final int childcount = editTextLayout.getChildCount();
        for (int i = 0; i < childcount; i++) {
           // Log.i("ComposeGratitudeDairy", "in for loop and i "+childcount+" "+i);
            LinearLayout editTextLayout1 = (LinearLayout) editTextLayout.getChildAt(i);
            EditText editText = (EditText) editTextLayout1.getChildAt(1);
            int count = i+1;
            if (contentCompleteString.equals("")) {
                contentCompleteString = count+"."+editText.getText().toString();
            }
            else {
                contentCompleteString = contentCompleteString +"\n"+count+"."+editText.getText().toString();
            }
        }
        //Log.i("ComposeGratitudeDairy","Complete string is "+contentCompleteString);
        ContentValues cv = new ContentValues();
        cv.put("TITLE", title);
        cv.put("PLAY_URL", recording_file_path);
        cv.put("TYPE", type);
        cv.put("CONTENT", contentCompleteString);
        cv.put("TIME", time);
        if (dbId == 0) {
            long id = db.insert("Gratitude_Table", null, cv);
            Toast.makeText(this, "Entry saved.", Toast.LENGTH_SHORT).show();
        }
        else {
            db.update("Gratitude_Table", cv, "_id="+dbId, null);
            Toast.makeText(this, "Entry updated.", Toast.LENGTH_SHORT).show();
        }
       // Log.i("ComposeGraritude","Inserted into table ");
        finish();
    }

    private void addGoogleAds() {
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7447665870261345~2301766396");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            //Log.i("ComposeGratitude","In after text changed listener");
            if (titleEditText.getText().toString().equals("")) {
                titleEditText.setText(titleEditText.getHint());
            }
        }
    };

    private boolean checkValidation() {
        boolean ret = true;
        //checks username and password editText fields are empty or not

        if (!CommonUtils.hasText(titleEditText, "Please enter title"))
            ret = false;
        if (!CommonUtils.hasText(contentEdittext, "Please enter notes"))
            ret = false;
        if (!CommonUtils.hasText(contentEdittext2, "Please enter notes"))
            ret = false;
        if (!CommonUtils.hasText(contentEdittext3, "Please enter notes"))
            ret = false;
        return ret;
    }

}