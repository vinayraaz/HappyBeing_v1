package com.azuyo.happybeing.Goals;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.Utils.MySql;
import com.azuyo.happybeing.ui.ProfileScreen;

/**
 * Created by Admin on 21-12-2016.
 */

public class Define_master_team_goal_fragment extends Fragment implements View.OnClickListener {
    private Button nextButton;
    private long dbId;
    private LinearLayout edittextLayout;
    private String type;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.define_master_team_layout, container, false);
        dbId = getArguments().getLong("dbId");
        type = getArguments().getString("TYPE");
        nextButton = (Button) view.findViewById(R.id.next_button);
        RelativeLayout nextLayout = (RelativeLayout) view.findViewById(R.id.done_layout);
        nextLayout.setOnClickListener(this);
        edittextLayout = (LinearLayout) view.findViewById(R.id.edit_texts_layout);
        nextButton.setOnClickListener(this);
        return view;
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
            Toast.makeText(getActivity(), "Entry created", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }

    private void updateDB() {
        String contentCompleteString = "";
        final int childcount = edittextLayout.getChildCount();
        for (int i = 0; i < childcount; i++) {
           // Log.i("ComposeGratitudeDairy", "in for loop and i "+childcount+" "+i);
            LinearLayout editTextLayout1 = (LinearLayout) edittextLayout.getChildAt(i);
            EditText editText = (EditText) editTextLayout1.getChildAt(1);
            int count = i+1;
            if (contentCompleteString.equals("")) {
                contentCompleteString = count+"."+editText.getText().toString();
            }
            else {
                contentCompleteString = contentCompleteString + "\n" + count + "." + editText.getText().toString();
            }
        }
        MySql dbHelper = new MySql(getActivity(), "mydb", null, ProfileScreen.dbVersion);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("TEAM_MEMBERS", contentCompleteString);
        cv.put("TYPE", type);
        long id = db.update("Goals_Table", cv, "_id=" + dbId, null);
        //Log.i("Goal","Update is "+id);
    }

}
