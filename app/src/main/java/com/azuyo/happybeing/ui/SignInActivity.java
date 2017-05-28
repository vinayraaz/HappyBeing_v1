package com.azuyo.happybeing.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.ServerAPIConnectors.APIProvider;
import com.azuyo.happybeing.ServerAPIConnectors.API_Response_Listener;
import com.azuyo.happybeing.Utils.AppConstants;
import com.azuyo.happybeing.Utils.CommonUtils;
import com.azuyo.happybeing.Utils.HappyBeingApplicationClass;
import com.azuyo.happybeing.Utils.HideKeyboardActivity;
import com.azuyo.happybeing.Utils.LoginInfo;
import com.azuyo.happybeing.Utils.PrefManager;
import com.azuyo.happybeing.Utils.UserInformation;
import com.azuyo.happybeing.events.DownloadMindGymAudioFilesService;

import static com.azuyo.happybeing.Utils.HappyBeingApplicationClass.userInformation;
import static com.azuyo.happybeing.Utils.PrefManager.pref;

/**
 * Created by Admin on 07-03-2017.
 */

public class SignInActivity extends HideKeyboardActivity implements View.OnClickListener {
    private EditText email_id, password;
    private Button sign_in, forgot_password;
    private LinearLayout goto_sign_up_layout;
    public String fromScreen;
    private PrefManager pref = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        pref = new PrefManager(this);
        email_id = (EditText) findViewById(R.id.email_id);
        password = (EditText) findViewById(R.id.password);
        goto_sign_up_layout = (LinearLayout) findViewById(R.id.goto_sign_up_layout);
        sign_in = (Button) findViewById(R.id.sign_in);
        forgot_password = (Button) findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(this);
        sign_in.setOnClickListener(this);
        goto_sign_up_layout.setOnClickListener(this);
        final SharedPreferences sharedPreferences = getSharedPreferences("happybeing", MODE_PRIVATE);
        Intent intent = getIntent();
        if (intent.hasExtra("FROM")) {
            fromScreen = intent.getStringExtra("FROM");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.goto_sign_up_layout:
                startActivity(new Intent(this, SignUpActivity.class));
                finish();
                break;
            case R.id.sign_in:
                if (checkValidation()) {
                    checkLogin();
                }
                break;
            case R.id.forgot_password:
                if (CommonUtils.isNetworkAvailable(this)) {
                    if (CommonUtils.hasText(email_id, "Enter email id")) {
                        forgotPassword(email_id.getText().toString());
                    }
                } else {
                    Toast.makeText(this, "Network not available!!!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void forgotPassword(String email) {
        new APIProvider.ForgotPassword(email, 887, this, "Sending password..", new API_Response_Listener<String>() {
            @Override
            public void onComplete(String data, long request_code, String failure_code) {
                if (data != null) {
                    if (data.equalsIgnoreCase("Invalid email id")) {
                        Toast.makeText(SignInActivity.this, "Invalid email id", Toast.LENGTH_SHORT).show();
                    }
                    else if (data.equalsIgnoreCase("success")){
                        Toast.makeText(SignInActivity.this, "Password sent successfully", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(SignInActivity.this, "Error sending password. Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(SignInActivity.this, "Error sending password. Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        }).call();
    }

    private void checkLogin() {
        LoginInfo loginInfo = new LoginInfo(email_id.getText().toString().trim(), password.getText().toString().trim());
        new APIProvider.CheckLogin(loginInfo, 2, this, "Signing in....", new API_Response_Listener<UserInformation>() {

            @Override
            public void onComplete(UserInformation data, long request_code, String failure_code) {
                if (failure_code.equals("invalid credentials")) {
                    Toast.makeText(SignInActivity.this, "Password incorrect..Please try again!!!", Toast.LENGTH_SHORT).show();
                }
                else if (failure_code.equals("user not exists")) {
                    Toast.makeText(SignInActivity.this, "User not exists!!!", Toast.LENGTH_SHORT).show();
                }
                else if (data != null) {
                    Toast.makeText(SignInActivity.this, "Successfully signed in", Toast.LENGTH_SHORT).show();

                    storeToPreferences(data);
                    userInformation = data;
                    if (fromScreen != null && fromScreen.equals("BUY")) {
                        if (CommonUtils.isNetworkAvailable(SignInActivity.this)) {
                            startService(new Intent(SignInActivity.this, DownloadMindGymAudioFilesService.class));
                        }
                        else {
                            Toast.makeText(SignInActivity.this, "Network not available!!!", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                    else {
                        finish();
                    }                }
                else {
                    Toast.makeText(SignInActivity.this, "Error signing in ", Toast.LENGTH_SHORT).show();
                }
            }
        }).call();
    }

    private void storeToPreferences(UserInformation userInformation) {


        SharedPreferences.Editor editor = getSharedPreferences("HAPPY_BEING", MODE_PRIVATE).edit();
        editor.putBoolean(AppConstants.IS_SIGNED_IN, true);
        editor.putString("user_login", "true");
        editor.putString("user_name",userInformation.getName().toString());
        editor.putString("user_email",userInformation.getEmailId().toString());
        editor.putBoolean("user_paid",userInformation.isPaid());
        editor.putString("user_number",userInformation.getMobileNumber().toString());
        editor.putString(AppConstants.NAME, userInformation.getName());


        editor.putString(AppConstants.SCREEN_NAME, userInformation.getScreenName());
        editor.putString(AppConstants.EMAIL_ID, userInformation.getEmailId());
        editor.putBoolean(AppConstants.IS_PAID, userInformation.isPaid());
        editor.putString(AppConstants.MOBILE_NUMBER, userInformation.getMobileNumber());
        editor.commit();
        AppConstants.IS_LOGIN =true;
        Intent i2 = new Intent(SignInActivity.this, NewHomeScreen.class);
        startActivity(i2);
        finish();
    }

    private boolean checkValidation() {
       // Log.i("Tag","In check validation method ");
        boolean ret = true;
        //checks username and password editText fields are empty or not
        if (!CommonUtils.hasText(email_id, "Please enter email id"))
            ret = false;
        if (!CommonUtils.hasText(password, "Please enter password"))
            ret = false;
        return ret;
    }
}
