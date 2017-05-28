package com.azuyo.happybeing.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.azuyo.happybeing.Goals.ComposeNewGoal;
import com.azuyo.happybeing.R;
import com.azuyo.happybeing.Utils.HappyBeingApplicationClass;
import com.azuyo.happybeing.Utils.MySql;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Admin on 21-12-2016.
 */

public class MyGoals_fragment extends BaseActivity implements View.OnClickListener {

    private ImageView goal1, goal2, goal3, goal4, goal5, goal6, goal7;
    private LinearLayout healthGoalLayout, workGoalLayout, moneyGoalLayout, familyGoalLayout, spiritualityGoalLayout,
            makingDiffGoalLayout, intellectGoalLayout, recreationGoalLayout;
    private SQLiteDatabase db;
    private Cursor healthCursor, workCursor, moneyCursor, familyCursor, spiritualityCursor, makingdiffCursor, intellectCursor, recreationCursor;
    private Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mygoals_fragment_layout);
        tracker = ((HappyBeingApplicationClass) getApplication()).getDefaultTracker();

        goal1 = (ImageView) findViewById(R.id.goal_1);
        goal2 = (ImageView) findViewById(R.id.goal_2);
        goal3 = (ImageView) findViewById(R.id.goal_3);
        goal4 = (ImageView) findViewById(R.id.goal_4);
        goal5 = (ImageView) findViewById(R.id.goal_5);
        goal6 = (ImageView) findViewById(R.id.goal_6);
        goal7 = (ImageView) findViewById(R.id.goal_7);

        healthGoalLayout = (LinearLayout) findViewById(R.id.health_layout);
        workGoalLayout = (LinearLayout) findViewById(R.id.work_goals_layout);
        moneyGoalLayout = (LinearLayout) findViewById(R.id.money_goal_layout);
        familyGoalLayout = (LinearLayout) findViewById(R.id.family_goal_layout);
        spiritualityGoalLayout = (LinearLayout) findViewById(R.id.spirituality_goal_layout);
        makingDiffGoalLayout = (LinearLayout) findViewById(R.id.making_diff_goal_layout);
        intellectGoalLayout = (LinearLayout) findViewById(R.id.intellect_goal_layout);
        recreationGoalLayout = (LinearLayout) findViewById(R.id.recreation_goal_layout);

        MySql dbHelper = new MySql(this, "mydb", null, ProfileScreen.dbVersion);
        db = dbHelper.getWritableDatabase();

        updateLayouts();
        healthGoalLayout.setOnClickListener(this);
        workGoalLayout.setOnClickListener(this);
        moneyGoalLayout.setOnClickListener(this);
        familyGoalLayout.setOnClickListener(this);
        spiritualityGoalLayout.setOnClickListener(this);
        makingDiffGoalLayout.setOnClickListener(this);
        intellectGoalLayout.setOnClickListener(this);
        recreationGoalLayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.health_layout:
                if (healthCursor.getCount() > 0) {
                    openViewGoalIntent("health");
                    //TODO: open view goal with Type
                }
                else {
                    openCreateGoalIntent("health");
                    //TODO: open create goal with type
                }
                tracker.send(new HitBuilders.EventBuilder().setCategory("MyGoalsFragment").setAction("Health").build());

                break;
            case R.id.work_goals_layout:
                tracker.send(new HitBuilders.EventBuilder().setCategory("MyGoalsFragment").setAction("Work").build());
                if (workCursor.getCount() > 0) {
                    openViewGoalIntent("work");
                }
                else {
                    openCreateGoalIntent("work");
                }
                break;
            case R.id.money_goal_layout:
                tracker.send(new HitBuilders.EventBuilder().setCategory("MyGoalsFragment").setAction("Money").build());

                if (moneyCursor.getCount() > 0) {
                    openViewGoalIntent("money");
                }
                else {
                    openCreateGoalIntent("money");
                }

                break;
            case R.id.family_goal_layout:
                tracker.send(new HitBuilders.EventBuilder().setCategory("MyGoalsFragment").setAction("Family").build());

                if (familyCursor.getCount() > 0) {
                    openViewGoalIntent("family");
                }
                else {
                    openCreateGoalIntent("family");
                }
                break;
            case R.id.spirituality_goal_layout:
                tracker.send(new HitBuilders.EventBuilder().setCategory("MyGoalsFragment").setAction("Spirituality").build());
                if (spiritualityCursor.getCount() > 0) {
                    openViewGoalIntent("spirituality");
                }
                else {
                    openCreateGoalIntent("spirituality");
                }
                break;
            case R.id.making_diff_goal_layout:
                tracker.send(new HitBuilders.EventBuilder().setCategory("MyGoalsFragment").setAction("Making difference").build());
                if (makingdiffCursor.getCount() > 0) {
                    openViewGoalIntent("making_difference");
                }
                else {
                    openCreateGoalIntent("making_difference");
                }
                break;
            case R.id.intellect_goal_layout:
                tracker.send(new HitBuilders.EventBuilder().setCategory("MyGoalsFragment").setAction("Intellect").build());
                if (intellectCursor.getCount() > 0) {
                    openViewGoalIntent("intellect");
                }
                else {
                    openCreateGoalIntent("intellect");
                }
                break;
            case R.id.recreation_goal_layout:
                tracker.send(new HitBuilders.EventBuilder().setCategory("MyGoalsFragment").setAction("Recreation").build());
                if (recreationCursor.getCount() > 0) {
                    openViewGoalIntent("recreation");
                }
                else {
                    openCreateGoalIntent("recreation");
                }

                break;
        }
    }

/*
    public void loadGoalsList(Cursor c){
        goalsAdapter = new CustomGoalsCursorAdapter(getActivity(), c, 0);
        goalsListView.setAdapter(goalsAdapter);
        goalsListView.setOnItemClickListener(this);
    }
*/

    @Override
    public void onResume() {
        super.onResume();
       // Log.i("MyGoalsFragment","In on resume ");
        updateLayouts();
    }

    private void openViewGoalIntent(String type) {
        Intent intent = new Intent(this, ViewGoalScreen.class);
        intent.putExtra("TYPE", type);
        startActivity(intent);
    }

    private void openCreateGoalIntent(String type) {
        Intent intent = new Intent(this, ComposeNewGoal.class);
        intent.putExtra("TYPE", type);
        startActivity(intent);
    }

    private void updateLayouts() {
        healthCursor = db.rawQuery("SELECT * FROM Goals_Table where TYPE="+"'"+"health"+"'", null);
        workCursor = db.rawQuery("SELECT * FROM Goals_Table where TYPE="+"'"+"work"+"'", null);
        moneyCursor = db.rawQuery("SELECT * FROM Goals_Table where TYPE="+"'"+"money"+"'", null);
        familyCursor = db.rawQuery("SELECT * FROM Goals_Table where TYPE="+"'"+"family"+"'", null);
        spiritualityCursor = db.rawQuery("SELECT * FROM Goals_Table where TYPE="+"'"+"spirituality"+"'", null);
        makingdiffCursor = db.rawQuery("SELECT * FROM Goals_Table where TYPE="+"'"+"making_difference"+"'", null);
        intellectCursor = db.rawQuery("SELECT * FROM Goals_Table where TYPE="+"'"+"intellect"+"'", null);
        recreationCursor = db.rawQuery("SELECT * FROM Goals_Table where TYPE="+"'"+"recreation"+"'", null);

        if (healthCursor.getCount() > 0) {
            healthGoalLayout.setBackgroundColor(Color.parseColor("#f44336"));
        }
        else {
            healthGoalLayout.setBackgroundColor(Color.WHITE);
        }
        if (workCursor.getCount() > 0) {
            workGoalLayout.setBackgroundColor(Color.parseColor("#009688"));
        }
        else {
            workGoalLayout.setBackgroundColor(Color.WHITE);
        }
        if (moneyCursor.getCount() > 0) {
            moneyGoalLayout.setBackgroundColor(Color.parseColor("#3f51b5"));
        }
        else {
            moneyGoalLayout.setBackgroundColor(Color.WHITE);
        }
        if (familyCursor.getCount() > 0) {
            familyGoalLayout.setBackgroundColor(Color.parseColor("#ff9800"));
        }
        else {
            familyGoalLayout.setBackgroundColor(Color.WHITE);
        }
        if (spiritualityCursor.getCount() > 0) {
            spiritualityGoalLayout.setBackgroundColor(Color.parseColor("#cddc39"));
        }
        else {
            spiritualityGoalLayout.setBackgroundColor(Color.WHITE);
        }
        if (makingdiffCursor.getCount() > 0) {
            makingDiffGoalLayout.setBackgroundColor(Color.parseColor("#e91863"));
        }
        else {
            makingDiffGoalLayout.setBackgroundColor(Color.WHITE);
        }
        if (intellectCursor.getCount() > 0) {
            intellectGoalLayout.setBackgroundColor(Color.parseColor("#00bcd4"));
        }
        else {
            intellectGoalLayout.setBackgroundColor(Color.WHITE);
        }
        if (recreationCursor.getCount() > 0) {
            recreationGoalLayout.setBackgroundColor(Color.parseColor("#8BC34A"));
        }
        else {
            recreationGoalLayout.setBackgroundColor(Color.WHITE);
        }
    }
}
