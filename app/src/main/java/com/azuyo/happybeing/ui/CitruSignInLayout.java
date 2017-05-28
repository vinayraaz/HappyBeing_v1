package com.azuyo.happybeing.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.Utils.AppEnvironment;
import com.azuyo.happybeing.Utils.HappyBeingApplicationClass;
import com.citrus.sdk.Callback;
import com.citrus.sdk.CitrusClient;
import com.citrus.sdk.CitrusUser;
import com.citrus.sdk.TransactionResponse;
import com.citrus.sdk.logger.CitrusLogger;
import com.citrus.sdk.login.AccessType;
import com.citrus.sdk.login.AvailableLoginType;
import com.citrus.sdk.login.CitrusLoginApi;
import com.citrus.sdk.login.FindUserResponse;
import com.citrus.sdk.login.PasswordType;
import com.citrus.sdk.response.CitrusError;
import com.citrus.sdk.response.CitrusResponse;
import com.citruspay.sdkui.ui.utils.CitrusFlowManager;
import com.citruspay.sdkui.ui.utils.PPConfig;
import com.citruspay.sdkui.ui.utils.ResultModel;
/*import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;*/

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Admin on 08-03-2017.
 */
public class CitruSignInLayout extends Activity implements View.OnClickListener {
    String emailIdString;
    private EditText email_id, mobile_number;
    private Button loginButton;
    private CitrusLoginApi citrusLoginApi;
    private CitrusClient citrusClient = null;
    private FindUserResponse findUserResponse;
    private EditText editOtp = null;
    private EditText editPassword = null, amount;
    private boolean isOverrideResultScreen;
    int style = -1;
    public static final String returnUrlLoadMoney = "http://mentalhealthcure.com/redirect";
    String userEmail, userMobile;
    private String orderId = "";
    private String dummyAmount = "5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citrus_sign_in);
        Intent intent = getIntent();
        email_id = (EditText) findViewById(R.id.edit_email_id);
        mobile_number = (EditText) findViewById(R.id.edit_mobile_no);
        editPassword = (EditText) findViewById(R.id.edit_password);
//        editOtp = (EditText) findViewById(R.id.edit_otp_id);
        amount = (EditText) findViewById(R.id.amount_ed);

        loginButton = (Button) findViewById(R.id.btn_login);
        loginButton.setOnClickListener(this);
        if (intent.hasExtra("Email_id")) {
            emailIdString = intent.getStringExtra("Email_id");
            email_id.setText(emailIdString);
        }
        initOrderId();
        style = -1;
        ((HappyBeingApplicationClass) getApplication()).setAppEnvironment(AppEnvironment.PRODUCTION);
        setupCitrusConfigs();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_login:
                startShoppingFlow();
                //onStartTransaction();
                break;
        }
    }

    private void initOrderId() {
        Random r = new Random(System.currentTimeMillis());
        orderId = "ORDER" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000);
    }

    //Paytm integration method

    private void startShoppingFlow() {
        userEmail = email_id.getText().toString().trim();
        userMobile = mobile_number.getText().toString().trim();
        CitrusClient.getInstance(this).isUserSignedIn(new Callback<Boolean>() {
            @Override
            public void success(Boolean success) {
                if (success) {
                    if (isDifferentUser(userMobile)) {
                        showUserLoggedInDialog(true);
                    } else {
                        initQuickPayFlow();
                    }
                } else {
                    initQuickPayFlow();
                }
            }

            @Override
            public void error(CitrusError error) {

            }
        });

    }
    private void initQuickPayFlow() {
        if (isOverrideResultScreen) {
            Toast.makeText(this, "Result Screen will Override", Toast.LENGTH_SHORT).show();
        }
        if (style != -1) {
            CitrusFlowManager.startShoppingFlowStyle(this,
                    userEmail, userMobile,
                    TextUtils.isEmpty(amount.getText().toString()) ? dummyAmount : amount.getText().toString(),
                    style, isOverrideResultScreen);
        } else {
            CitrusFlowManager.startShoppingFlowStyle(this,
                    userEmail, userMobile,
                    TextUtils.isEmpty(amount.getText().toString()) ? dummyAmount : amount.getText().toString(),
                    R.style.AppTheme_default, isOverrideResultScreen);
        }
    }
    public boolean isDifferentUser(String userMobile) {
        String savedUserMobile = "";
        return !savedUserMobile.equals(userMobile);

    }

    private void showUserLoggedInDialog(boolean isQuickPayFlow) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Already logged in.");
        alertDialog.setMessage("Different User is already logged in\n do you want to logout ?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logoutUser();
                initQuickPayFlow();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CitrusFlowManager.startWalletFlow(CitruSignInLayout.this, userEmail, userMobile);
            }
        });
        alertDialog.show();
    }
    private void logoutUser() {
        logoutUser(this);
    }
    public void logoutUser(final Context context) {

        final ProgressDialog mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Logging user out...");
        mProgressDialog.show();
        CitrusClient.getInstance(context).signOut(new Callback<CitrusResponse>() {
            @Override
            public void success(CitrusResponse citrusResponse) {
                mProgressDialog.dismiss();
                Toast.makeText(context, "User is successfully logged out", Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void error(CitrusError error) {
                mProgressDialog.dismiss();
                Toast.makeText(context, "User could not be logged out", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setupCitrusConfigs() {
        AppEnvironment appEnvironment = ((HappyBeingApplicationClass) getApplication()).getAppEnvironment();
        if (appEnvironment == AppEnvironment.PRODUCTION) {
            Toast.makeText(this, "Environment Set to Production", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Environment Set to SandBox", Toast.LENGTH_SHORT).show();
        }

        CitrusFlowManager.initCitrusConfig(appEnvironment.getSignUpId(),
                appEnvironment.getSignUpSecret(), appEnvironment.getSignInId(),
                appEnvironment.getSignInSecret(), ContextCompat.getColor(this, R.color.white),
                this, appEnvironment.getEnvironment(), appEnvironment.getVanity(), appEnvironment.getBillUrl(),
                returnUrlLoadMoney);

        //To Set the Log Level of Core SDK & Plug & Play
        PPConfig.getInstance().setLogLevel(this, CitrusLogger.LogLevel.DEBUG);

        //To Set the User details
        CitrusUser.Address customAddress = new CitrusUser.Address("Street1", "Street2", "City", "State", "Country", "411045");
        PPConfig.getInstance().setUserDetails("Custom_FName", "Custom_LName", customAddress);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CitrusFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=  null) {

            // You will get data here if transaction flow is started through pay options other than wallet
            TransactionResponse transactionResponse = data.getParcelableExtra(CitrusFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);

            // You will get data here if transaction flow is started through wallet
            ResultModel resultModel = data.getParcelableExtra(CitrusFlowManager.ARG_RESULT);

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getJsonResponse() != null) {
                // Decide what to do with this data
                Log.d("Citrus", "Transaction response : " + transactionResponse.getJsonResponse());
            }
            else if (resultModel != null && resultModel.getTransactionResponse() != null) {
                // Decide what to do with this data
                Log.d("Citrus", "Result response : " + resultModel.getTransactionResponse().getTransactionId());
            }
            else if (resultModel != null && resultModel.getError() != null) {
                Log.d("Citrus", "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d("Citrus", "Both objects are null!");
            }
        }
    }
}
