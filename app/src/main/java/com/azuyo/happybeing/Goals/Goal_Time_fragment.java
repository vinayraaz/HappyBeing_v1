package com.azuyo.happybeing.Goals;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.Utils.CommonUtils;
import com.azuyo.happybeing.Utils.MySql;
import com.azuyo.happybeing.fourmob.datepicker.DatePickerDialog;
import com.azuyo.happybeing.fourmob.timepicker.RadialPickerLayout;
import com.azuyo.happybeing.fourmob.timepicker.TimePickerDialog;
import com.azuyo.happybeing.ui.ProfileScreen;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Admin on 21-12-2016.
 */

public class Goal_Time_fragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    private Button nextButton;
    public int final_start_hour, final_end_hour, final_start_min,final_end_min, final_start_year,final_end_year, final_start_month,final_end_month, final_start_dayOfMonth,final_end_dayOfMonth;
    public TextView start_time, start_date;
    private FragmentActivity fragmentActivity;
    private long dbId;
    private String type;
    private EditText titleEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.goal_time_layout, container, false);
        nextButton = (Button) view.findViewById(R.id.next_button);
        start_date = (TextView) view.findViewById(R.id.date1);
        start_time = (TextView) view.findViewById(R.id.time1);
        titleEditText = (EditText) view.findViewById(R.id.title_edit_text);

        RelativeLayout nextLayout = (RelativeLayout) view.findViewById(R.id.done_layout);
        nextLayout.setOnClickListener(this);
        dbId = getArguments().getLong("dbId");
        type = getArguments().getString("TYPE");
        String titleName = getArguments().getString("TITLE");
        titleEditText.setText(titleName);
        Calendar c = Calendar.getInstance();
        initTimeVariables(c);
        updateTime(start_time, final_start_hour, final_start_min);
        updateDate(start_date, final_start_year, final_start_month+1, final_start_dayOfMonth);
        start_date.setOnClickListener(this);
        start_time.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        fragmentActivity = (FragmentActivity)context;
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        final Calendar calendar = Calendar.getInstance();
        final String DATEPICKER_TAG = "datepicker";
        final String TIMEPICKER_TAG = "timepicker";
        int id = v.getId();
        if (id == R.id.next_button || id == R.id.done_layout) {
            if (checkValidation()) {
                updateDB();
                Bundle bundle = new Bundle();
                bundle.putLong("dbId", dbId);
                bundle.putString("TYPE", type);
                bundle.putString("title_text", titleEditText.getText().toString());
                Define_goal_fragment define_goal_fragment = new Define_goal_fragment();
                define_goal_fragment.setArguments(bundle);
                gotoFragment(define_goal_fragment, "Second_fragment");
            }
        }
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
            //Log.i("TimeFragment", "Fragment activity is "+fragmentActivity);
            timePickerDialog.show(getActivity().getSupportFragmentManager(), TIMEPICKER_TAG);

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
            datePickerDialog.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);

        }

    }
    private void gotoFragment(android.support.v4.app.Fragment fragment, String tag) {
        android.support.v4.app.FragmentManager fm = getFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.goal_screen_fragment, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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

    private void updateDB() {
        MySql dbHelper = new MySql(getActivity(), "mydb", null, ProfileScreen.dbVersion);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Calendar calender = new GregorianCalendar(final_start_year, final_start_month, final_start_dayOfMonth, final_start_hour, final_start_min);
        long start_time = calender.getTimeInMillis();
        ContentValues cv = new ContentValues();
        cv.put("ACHIEVE_BY", start_time);
        cv.put("TITLE", titleEditText.getText().toString());
        long id = db.update("Goals_Table", cv, "_id=" + dbId, null);
    }

    private boolean checkValidation() {
        boolean ret = true;
        //checks username and password editText fields are empty or not

        if (!CommonUtils.hasText(titleEditText, "Please enter title"))
            ret = false;
        return ret;
    }


}
