package com.azuyo.happybeing.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Admin on 19-03-2017.
 */
public class UserInformation {
    public static String csrf_key = "";
    private String name;
    private String screenName;
    private String mobileNumber;
    private String emailId;
    private boolean isLogedIn;
    private boolean isPaid;
    private String role;
    private SharedPreferences sharedPreferences;
    public boolean isPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isLogedIn(Context context) {
        sharedPreferences = context.getSharedPreferences("HAPPY_BEING", MODE_PRIVATE);
        return sharedPreferences.getBoolean(AppConstants.IS_SIGNED_IN, false);
    }

    public void setLogedIn(Context context, boolean logedIn) {
        sharedPreferences = context.getSharedPreferences("HAPPY_BEING", MODE_PRIVATE);
        sharedPreferences.getBoolean(AppConstants.IS_SIGNED_IN, logedIn);
    }
}
