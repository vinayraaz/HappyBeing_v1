package com.azuyo.happybeing.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.Utils.CommonUtils;
import com.azuyo.happybeing.Utils.MySql;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class ViewHappyMoment extends BaseActivity {
    private SQLiteDatabase db;
    private TextView contentEdittext;
    private ImageView myhappymomentImage;
    private AdView mAdView = null;
    private long dbId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_happymoment_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(true);
        TextView titleText = (TextView) findViewById(R.id.title_name);
        titleText.setText("Happy moment");
        ImageView userAccount = (ImageView) findViewById(R.id.account_circle);
        userAccount.setVisibility(View.GONE);
        Intent intent = getIntent();
        dbId = intent.getLongExtra("id", 0);

        MySql dbHelper = new MySql(this, "mydb", null, ProfileScreen.dbVersion);
        db = dbHelper.getWritableDatabase();

        contentEdittext = (TextView) findViewById(R.id.notes_textview);
        myhappymomentImage = (ImageView) findViewById(R.id.my_happymoments_image);

        addGoogleAds();
        getDetailsFromLocalDb(dbId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.edit_entry:
                //TODO: open compose Gratitude dairy with text and dbid as extra.
                Intent intent = new Intent(this, ComposeNewHappyMoment.class);
                intent.putExtra("dbId", dbId);
                startActivity(intent);
                finish();
                break;
            case R.id.delete_entry:
                //TODO: delete the db entry
                int num = db.delete("My_Happy_Moments", "_id='" + dbId + "'", null);
                Toast.makeText(this, "Entry deleted!!", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDetailsFromLocalDb(long dbId) {
        Cursor cursor = db.rawQuery("SELECT * FROM My_Happy_Moments where _id=" + "'" + dbId + "'", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String title = cursor.getString(cursor.getColumnIndex("TITLE"));
            String content = cursor.getString(cursor.getColumnIndex("CONTENT"));
            String imageUriString = cursor.getString(cursor.getColumnIndex("IMAGE_URI"));
            if (imageUriString != null) {
                Uri imageuri = Uri.parse(imageUriString);
                CommonUtils.fillImageView(this, imageuri, myhappymomentImage);
            }
            contentEdittext.setText(title);
        }
    }
    private void addGoogleAds() {
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7447665870261345~2301766396");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

}