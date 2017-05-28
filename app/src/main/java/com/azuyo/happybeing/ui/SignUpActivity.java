package com.azuyo.happybeing.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.ServerAPIConnectors.APIProvider;
import com.azuyo.happybeing.ServerAPIConnectors.API_Response_Listener;
import com.azuyo.happybeing.Utils.AppConstants;
import com.azuyo.happybeing.Utils.CommonUtils;
import com.azuyo.happybeing.Utils.CountryCodes;
import com.azuyo.happybeing.Utils.HappyBeingApplicationClass;
import com.azuyo.happybeing.Utils.LoginInfo;
import com.azuyo.happybeing.Utils.UserInformation;
import com.azuyo.happybeing.events.DownloadMindGymAudioFilesService;

/**
 * Created by Admin on 07-03-2017.
 */

public class SignUpActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private EditText email_id, password, name, phonenumber;
    private Button sign_in;
    private String fromScreen;
    private LinearLayout goto_sign_in_layout;
    private TextView countryCodeTv;
    private Spinner countryCode;
    private CountryCodes countrycodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email_id = (EditText) findViewById(R.id.email_id);
        password = (EditText) findViewById(R.id.password);
        name = (EditText) findViewById(R.id.name);

        countrycodes = new CountryCodes();
        String[] countries = countrycodes.getCountriesList();

        //onScreenname = (EditText) findViewById(R.id.on_screen_name);
        phonenumber = (EditText) findViewById(R.id.phone_number);
        goto_sign_in_layout = (LinearLayout) findViewById(R.id.goto_sign_in_layout);
        countryCodeTv = (TextView)findViewById(R.id.countryCodeTexview);

        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.spinner_layout,countries);
        countryCodeTv.setOnClickListener(this);
        countryCode = (Spinner) findViewById(R.id.countrySpinner);
        countryCode.setAdapter(adapter);
        countryCode.setOnItemSelectedListener(this);
        countryCode.setSelection(92);

        sign_in = (Button) findViewById(R.id.sign_in);
        sign_in.setOnClickListener(this);
        goto_sign_in_layout.setOnClickListener(this);
        Intent intent = getIntent();
        if (intent.hasExtra("FROM")) {
            fromScreen = intent.getStringExtra("FROM");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.goto_sign_in_layout:
                Intent intent = new Intent(this, SignInActivity.class);
                if (fromScreen != null && !fromScreen.equals("")) {
                    intent.putExtra("FROM", fromScreen);
                }
                startActivity(intent);
                finish();
                break;
            case R.id.sign_in:
                if (checkValidation()) {
                    if (CommonUtils.isNetworkAvailable(this)) {
                        createUser();
                    }
                    else {
                        Toast.makeText(this, "Network not available!!!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.countryCodeTexview:
                countryCode.performClick();
                break;
        }
    }

    private boolean isValidMobile(String phone) {
        if (!phone.trim().equals("") && phone.length() > 10)
        {
            return Patterns.PHONE.matcher(phone).matches();
        }

        return false;
    }
    private void createUser() {
        int pos = countryCode.getSelectedItemPosition();
        String CountrycodeNo = countrycodes.getCountryCode(pos);
        String phoneNo = CountrycodeNo + phonenumber.getText().toString();
        LoginInfo loginInfo = new LoginInfo(email_id.getText().toString(), name.getText().toString(), password.getText().toString(),
                phoneNo);
        new APIProvider.SignUpUser(loginInfo, 3, this, "Creating user...", new API_Response_Listener<UserInformation>() {
            @Override
            public void onComplete(UserInformation data, long request_code, String failure_code) {
                if (failure_code.equals(APIProvider.EMAIL_EXISTS)) {
                    Toast.makeText(SignUpActivity.this, "Email id already exists!!!", Toast.LENGTH_SHORT).show();
                }
                else if (failure_code.equals(APIProvider.MOBILE_NUMBER_EXISTS)){
                    Toast.makeText(SignUpActivity.this, "mobile number already exists!!!", Toast.LENGTH_SHORT).show();
                }
                else if (data != null) {
                    //Log.i("SignUp", "User created");
                    storeToPreferences(data);
                    HappyBeingApplicationClass.userInformation = data;
                    Toast.makeText(SignUpActivity.this, "User created", Toast.LENGTH_SHORT).show();
                    if (fromScreen != null && fromScreen.equals("BUY")) {
                        if (CommonUtils.isNetworkAvailable(SignUpActivity.this)) {
                            startService(new Intent(SignUpActivity.this, DownloadMindGymAudioFilesService.class));
                        }
                        else {
                            Toast.makeText(SignUpActivity.this, "Network not available!!!", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                    else {
                        finish();
                    }
                }
                else {
                    Toast.makeText(SignUpActivity.this, "Error occured. Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        }).call();
    }
    private void storeToPreferences(UserInformation userInformation) {
        SharedPreferences.Editor editor = getSharedPreferences("HAPPY_BEING", MODE_PRIVATE).edit();
        editor.putBoolean(AppConstants.IS_SIGNED_IN, true);
        editor.putString(AppConstants.NAME, userInformation.getName());
        editor.putString(AppConstants.SCREEN_NAME, userInformation.getScreenName());
        editor.putString(AppConstants.EMAIL_ID, userInformation.getEmailId());
       // Log.i("SignIn", "Paid status is "+userInformation.isPaid());
        editor.putBoolean(AppConstants.IS_PAID, userInformation.isPaid());
        editor.putString(AppConstants.MOBILE_NUMBER, userInformation.getMobileNumber());
        editor.commit();
    }

    private boolean checkValidation() {
        boolean ret = true;
        String CountryAbbrevation;
        int pos = countryCode.getSelectedItemPosition();
        CountryAbbrevation = countrycodes.getCountryAbrevation(pos);

        //checks username and password editText fields are empty or not
        if (!CommonUtils.hasText(email_id, "Please enter email id"))
            ret = false;
        if (!CommonUtils.hasText(password, "Please enter password"))
            ret = false;
        if (!CommonUtils.isValidPhoneNumber(phonenumber, CountryAbbrevation))
            ret = false;
        if (!CommonUtils.hasText(name, "Please enter name"))
            ret = false;
        return ret;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String CountrycodeNo = countrycodes.getCountryCode(position);
        //TextView tv = (TextView)findViewById(R.id.countryCodeTexview);
        countryCodeTv.setText("+" + CountrycodeNo);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
