package com.azuyo.happybeing.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.azuyo.happybeing.Goals.EditGoalScreen;
import com.azuyo.happybeing.R;
import com.azuyo.happybeing.Utils.MySql;

public class ViewGoalScreen extends BaseActivity {
    private SQLiteDatabase db;
    private TextView titleTextview, acheive_by, define_goal, benefits, stepsToAcheiveTextview, compromisesMade, compromisesnotMade,
            masterTeam;
    private ImageView imageView;
    private String type;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_goal_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        ImageView userAccount = (ImageView) findViewById(R.id.account_circle);
        userAccount.setVisibility(View.GONE);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        Intent intent = getIntent();
        if (intent.hasExtra("TYPE")) {
            type = intent.getStringExtra("TYPE");
        }
        MySql dbHelper = new MySql(this, "mydb", null, ProfileScreen.dbVersion);
        db = dbHelper.getWritableDatabase();

        titleTextview = (TextView) findViewById(R.id.title_textview);
        acheive_by = (TextView) findViewById(R.id.acheive_by_textview);
        define_goal = (TextView) findViewById(R.id.define_goal_textview);
        benefits = (TextView) findViewById(R.id.benifits_goal_textview);
        compromisesMade = (TextView) findViewById(R.id.compromises_made_textview);
        compromisesnotMade = (TextView) findViewById(R.id.compromises_not_made_textview);
        masterTeam = (TextView) findViewById(R.id.team_members_textview);

        stepsToAcheiveTextview = (TextView) findViewById(R.id.steps_to_acheive_textview);

        getDetailsFromLocalDb(type);
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
                Intent intent = new Intent(this, EditGoalScreen.class);
                intent.putExtra("Type", type);
                startActivity(intent);
                finish();
                break;
            case R.id.delete_entry:
                //TODO: delete the db entry
                int num = db.delete("Goals_Table", "TYPE='" + type + "'", null);
                Toast.makeText(this, "Entry deleted!!", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDetailsFromLocalDb(String type) {
        Cursor cursor = db.rawQuery("SELECT * FROM Goals_Table where TYPE=" + "'" + type + "'", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String title = cursor.getString(cursor.getColumnIndex("TITLE"));
            long acheiveBy = cursor.getLong(cursor.getColumnIndex("ACHIEVE_BY"));
            String start1 = DateFormat.format("dd-MMM-yyyy hh:mm a", acheiveBy).toString();
            String define = cursor.getString(cursor.getColumnIndex("DEFINE"));
            String benifits = cursor.getString(cursor.getColumnIndex("BENEFITS"));
            String compromises_made = cursor.getString(cursor.getColumnIndex("COMPROMISE_MADE"));
            String compromises_not_made = cursor.getString(cursor.getColumnIndex("COMPROMISE_NOT_MADE"));
            String stepsToAcheive = cursor.getString(cursor.getColumnIndex("STEPS_TO_ACHIEVE"));
            String team_members = cursor.getString(cursor.getColumnIndex("TEAM_MEMBERS"));
            Log.i("ViewGoal", "Team mem" +team_members+","+ define+ ", "+benifits);
            titleTextview.setText(title);
            acheive_by.setText(start1);
            define_goal.setText(define);
            benefits.setText(benifits);
            compromisesMade.setText(compromises_made);
            compromisesnotMade.setText(compromises_not_made);
            stepsToAcheiveTextview.setText(stepsToAcheive);
            masterTeam.setText(team_members);
        }
    }
}