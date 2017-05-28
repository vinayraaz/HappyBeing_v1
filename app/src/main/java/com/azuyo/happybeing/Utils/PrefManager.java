package com.azuyo.happybeing.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nSmiles on 5/19/2017.
 */

public class PrefManager {
    public static SharedPreferences pref;
    public static SharedPreferences prefArray;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editorArray;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    public static final String PREF_NAME = "happybeing";
    private static final String IS_LOGIN = "IsLoggedIn";

    // Constructor
    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void SetloginSession(String userlogin, String username, String useremail) {
        pref = _context.getSharedPreferences("happybeing", PRIVATE_MODE);
        editor = pref.edit();
        editor.putString(AppConstants.USER_LOGIN, userlogin);
        editor.putString(AppConstants.USER_NAME, username);
        editor.putString(AppConstants.USER_EMAIL, useremail);
        editor.commit();
        System.out.println("AppConstants.USER_EMAIL*******************"+AppConstants.USER_EMAIL);

    }
}
