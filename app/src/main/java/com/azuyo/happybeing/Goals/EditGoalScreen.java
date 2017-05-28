package com.azuyo.happybeing.Goals;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.Utils.MySql;
import com.azuyo.happybeing.fourmob.datepicker.DatePickerDialog;
import com.azuyo.happybeing.fourmob.timepicker.RadialPickerLayout;
import com.azuyo.happybeing.fourmob.timepicker.TimePickerDialog;
import com.azuyo.happybeing.ui.BaseActivity;
import com.azuyo.happybeing.ui.ProfileScreen;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class EditGoalScreen extends BaseActivity implements View.OnClickListener {
    private SQLiteDatabase db;
    private EditText titleTextview, define_goal, benefits, stepsToAcheiveTextview, compromisesMade, compromisesnotMade,
            masterTeam;
    private ImageView imageView;
    public int final_start_hour, final_end_hour, final_start_min,final_end_min, final_start_year,final_end_year, final_start_month,final_end_month, final_start_dayOfMonth,final_end_dayOfMonth;
    public TextView start_time, start_date;
    private String type;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_goal_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        ImageView userAccount = (ImageView) findViewById(R.id.account_circle);
        userAccount.setVisibility(View.GONE);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        Intent intent = getIntent();
        if (intent.hasExtra("Type")) {
            type = intent.getStringExtra("Type");
        }
        //Log.i("Goal", "Type is "+type);

        MySql dbHelper = new MySql(this, "mydb", null, ProfileScreen.dbVersion);
        db = dbHelper.getWritableDatabase();
        start_date = (TextView) findViewById(R.id.date1);
        start_time = (TextView) findViewById(R.id.time1);
        titleTextview = (EditText) findViewById(R.id.title_textview);
        define_goal = (EditText) findViewById(R.id.define_goal_textview);
        benefits = (EditText) findViewById(R.id.benifits_goal_textview);
        compromisesMade = (EditText) findViewById(R.id.compromises_made_textview);
        compromisesnotMade = (EditText) findViewById(R.id.compromises_not_made_textview);
        masterTeam = (EditText) findViewById(R.id.team_members_textview);

        stepsToAcheiveTextview = (EditText) findViewById(R.id.steps_to_acheive_textview);
        Calendar c = Calendar.getInstance();
        initTimeVariables(c);
        updateTime(start_time, final_start_hour, final_start_min);
        updateDate(start_date, final_start_year, final_start_month+1, final_start_dayOfMonth);
        start_date.setOnClickListener(this);
        start_time.setOnClickListener(this);
        getDetailsFromLocalDb(type);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.text_item_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.text_item:
                //TODO: delete the db entry
                updateDB();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateDB() {
        Calendar calender = new GregorianCalendar(final_start_year, final_start_month, final_start_dayOfMonth, final_start_hour, final_start_min);
        long start_time = calender.getTimeInMillis();
        ContentValues cv = new ContentValues();
        cv.put("TITLE", titleTextview.getText().toString());
        cv.put("ACHIEVE_BY", start_time);
        cv.put("DEFINE", define_goal.getText().toString());
        cv.put("BENEFITS", benefits.getText().toString());
        cv.put("STEPS_TO_ACHIEVE", stepsToAcheiveTextview.getText().toString());
        cv.put("COMPROMISE_MADE", compromisesMade.getText().toString());
        cv.put("COMPROMISE_NOT_MADE", compromisesnotMade.getText().toString());
        cv.put("TEAM_MEMBERS", masterTeam.getText().toString());
        long id = db.update("Goals_Table", cv, "TYPE=\""+type+"\"" ,null);
        Toast.makeText(this, "Entry updated!!!", Toast.LENGTH_SHORT).show();
    }

    private void getDetailsFromLocalDb(String type) {
        Cursor cursor = db.rawQuery("SELECT * FROM Goals_Table where TYPE=" + "'" + type + "'", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String title = cursor.getString(cursor.getColumnIndex("TITLE"));
            String define = cursor.getString(cursor.getColumnIndex("DEFINE"));
            String benifits = cursor.getString(cursor.getColumnIndex("BENEFITS"));
            String compromises_made = cursor.getString(cursor.getColumnIndex("COMPROMISE_MADE"));
            String compromises_not_made = cursor.getString(cursor.getColumnIndex("COMPROMISE_NOT_MADE"));
            String stepsToAcheive = cursor.getString(cursor.getColumnIndex("STEPS_TO_ACHIEVE"));
            String team_members = cursor.getString(cursor.getColumnIndex("TEAM_MEMBERS"));
            //Log.i("ViewGoal", "Team mem" +team_members+","+ define+ ", "+benifits);
            titleTextview.setText(title);
            define_goal.setText(define);
            benefits.setText(benifits);
            compromisesMade.setText(compromises_made);
            compromisesnotMade.setText(compromises_not_made);
            stepsToAcheiveTextview.setText(stepsToAcheive);
            masterTeam.setText(team_members);
        }
    }

    public void updateDate(TextView view, int y, int m, int d) {
        String month;
        switch(m){
            case 1:month = "Jan";
                break;
            case 2:month = "Feb";
                break;
            case 3:month = "Mar";
                break;
            case 4:month = "April";
                break;
            case 5:month = "May";
                break;
            case 6:month = "Jun";
                break;
            case 7:month = "July";
                break;
            case 8:month = "Aug";
                break;
            case 9:month = "Sep";
                break;
            case 10:month = "Oct";
                break;
            case 11:month = "Nov";
                break;
            case 12:month = "Dec";
                break;
            default:month = "Jan";
                break;
        }
        String aDate = new StringBuilder().append(d).append(' ').append(month).append(' ').append(y).toString();/*.append(' ').append(DayOfWeek)*/
        view.setText(aDate);
    }

    public void updateTime(TextView v, int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        String aTime = new
                StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();
        v.setText(aTime);
    }
    public void initTimeVariables(Calendar c) {
        final_start_hour = c.get(Calendar.HOUR_OF_DAY);
        final_end_hour = c.get(Calendar.HOUR_OF_DAY) + 1;
        final_start_min = c.get(Calendar.MINUTE);
        final_end_min = c.get(Calendar.MINUTE);
        final_start_dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        final_start_month = c.get(Calendar.MONTH);
        final_start_year = c.get(Calendar.YEAR);
    }


    @Override
    public void onClick(View v) {
        final Calendar calendar = Calendar.getInstance();
        final String DATEPICKER_TAG = "datepicker";
        final String TIMEPICKER_TAG = "timepicker";
        int id = v.getId();
        if(v.getId() == R.id.time1) {
            final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                    final_start_hour = hourOfDay;
                    final_start_min = minute;
                    int endHourOfDay = 0, endMinute;
                    endMinute = minute + 60;
                    if (endMinute >= 60) {
                        endHourOfDay = endHourOfDay + 1;
                        endMinute = minute % 60;
                    }
                    endHourOfDay = endHourOfDay + hourOfDay;
                    if (hourOfDay >= 24) {
                        endHourOfDay = endHourOfDay % 24;
                    }

                    final_end_hour = endHourOfDay;
                    final_end_min = endMinute;
                    updateTime(start_time, hourOfDay, minute);
                }
            }, final_start_hour, final_start_min, false, false);
            timePickerDialog.setVibrate(true);
            timePickerDialog.setCloseOnSingleTapMinute(false);
            timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);

        }
        else if(v.getId() == R.id.date1) {
            final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {

                    final_start_year = year;
                    final_start_month = month;
                    final_start_dayOfMonth = day;
                    final_end_year = year;
                    final_end_month = month;
                    final_end_dayOfMonth = day;
                    updateDate(start_date, year, month + 1, day);
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), true);
            datePickerDialog.setVibrate(true);
            datePickerDialog.setYearRange(1985, 2028);
            datePickerDialog.setCloseOnSingleTapDay(true);
            datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);

        }

    }
}