package com.azuyo.happybeing.Goals;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.Utils.MySql;
import com.azuyo.happybeing.ui.ProfileScreen;

/**
 * Created by Admin on 21-12-2016.
 */

public class Steps_to_acheive_goal extends android.support.v4.app.Fragment implements View.OnClickListener {
    private Button nextButton, plus_button;
    private long dbId;
    private String type;
    private LinearLayout edittextLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.steps_to_acheive_goal_layout, container, false);
        nextButton = (Button) view.findViewById(R.id.next_button);
        plus_button= (Button) view.findViewById(R.id.plus_button);
        edittextLayout = (LinearLayout) view.findViewById(R.id.edit_texts_layout);

        plus_button.setOnClickListener(this);
        dbId = getArguments().getLong("dbId");
        type = getArguments().getString("TYPE");
        RelativeLayout nextLayout = (RelativeLayout) view.findViewById(R.id.done_layout);
        nextLayout.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.next_button || id == R.id.done_layout) {
            updateDB();
            Bundle bundle = new Bundle();
            bundle.putLong("dbId", dbId);
            bundle.putString("TYPE", type);
            Define_master_team_goal_fragment compromisesMade_goal_fragment = new Define_master_team_goal_fragment();
            compromisesMade_goal_fragment.setArguments(bundle);
            gotoFragment(compromisesMade_goal_fragment, "Six_fragment");
        }
        else if (id == R.id.plus_button) {
            addLinearLayout();
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

    private void updateDB() {
        MySql dbHelper = new MySql(getActivity(), "mydb", null, ProfileScreen.dbVersion);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String contentCompleteString = "";
        final int childcount = edittextLayout.getChildCount();
        for (int i = 0; i < childcount; i++) {
            //Log.i("ComposeGratitudeDairy", "in for loop and i "+childcount+" "+i);
            LinearLayout editTextLayout1 = (LinearLayout) edittextLayout.getChildAt(i);
            EditText editText = (EditText) editTextLayout1.getChildAt(0);
            //Log.i("ComposeGratitudeDairy", "Edit text is "+editText);
            int count = i+1;
            if (contentCompleteString.equals("")) {
                contentCompleteString = count + "." +editText.getText().toString();
            }
            else {
                contentCompleteString = contentCompleteString + "\n" + count + "." + editText.getText().toString();
            }
        }
        cv.put("STEPS_TO_ACHIEVE", contentCompleteString);
        long id = db.update("Goals_Table", cv, "_id=" + dbId, null);
        //Log.i("Goal","Update is "+id);
    }

    private void addLinearLayout() {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 130));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(10, 10, 10, 0);
        //numberText.setLayoutParams(new TextView.Params(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        EditText editText = new EditText(getActivity());
        editText.setBackgroundResource(R.drawable.edit_text);
        editText.setId(edittextLayout.getChildCount()+1);
        editText.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
        editText.setPadding(15, 0, 0, 0);
        editText.setHint("Task "+Integer.toString(edittextLayout.getChildCount()+1));
        linearLayout.addView(editText, lp);
        edittextLayout.addView(linearLayout);

    }
}
