package com.azuyo.happybeing.Goals;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.Utils.CommonUtils;
import com.azuyo.happybeing.Utils.MySql;
import com.azuyo.happybeing.ui.ProfileScreen;

/**
 * Created by Admin on 21-12-2016.
 */

public class Benifits_goal_fragment extends Fragment implements View.OnClickListener {
    private Button nextButton;
    private long dbId;
    private String type;
    private EditText editText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.goal_benifits_layout, container, false);
        dbId = getArguments().getLong("dbId");
        type = getArguments().getString("TYPE");
        nextButton = (Button) view.findViewById(R.id.next_button);
        RelativeLayout nextLayout = (RelativeLayout) view.findViewById(R.id.done_layout);
        nextLayout.setOnClickListener(this);
        editText = (EditText) view.findViewById(R.id.benifits_edit_text);
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
            if (checkValidation()) {
                updateDB();
                Bundle bundle = new Bundle();
                bundle.putLong("dbId", dbId);
                bundle.putString("TYPE", type);
                CompromisesMade_goal_fragment steps_to_acheive_goal = new CompromisesMade_goal_fragment();
                steps_to_acheive_goal.setArguments(bundle);
                gotoFragment(steps_to_acheive_goal, "Fourth_fragment");
            }
        }

    }
    private void gotoFragment(Fragment fragment, String tag) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.goal_screen_fragment, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void updateDB() {
        MySql dbHelper = new MySql(getActivity(), "mydb", null, ProfileScreen.dbVersion);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("BENEFITS", editText.getText().toString());
        long id = db.update("Goals_Table", cv, "_id=" + dbId, null);
        //Log.i("Goal","Update is "+id);
    }
    private boolean checkValidation() {
        boolean ret = true;
        //checks username and password editText fields are empty or not
        if (!CommonUtils.hasText(editText, "Please fill the field"))
            ret = false;
        return ret;
    }


}
