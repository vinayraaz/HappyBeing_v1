package com.azuyo.happybeing.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.ServerAPIConnectors.APIProvider;
import com.azuyo.happybeing.ServerAPIConnectors.API_Response_Listener;
import com.azuyo.happybeing.ServerAPIConnectors.MyJsonObject;
import com.azuyo.happybeing.Utils.AppConstants;
import com.azuyo.happybeing.Utils.AppEnvironment;
import com.azuyo.happybeing.Utils.CommonUtils;
import com.azuyo.happybeing.Utils.HappyBeingApplicationClass;
import com.azuyo.happybeing.Utils.LoginInfo;
import com.azuyo.happybeing.Utils.UserInformation;
import com.citrus.sdk.Callback;
import com.citrus.sdk.CitrusClient;
import com.citrus.sdk.CitrusUser;
import com.citrus.sdk.TransactionResponse;
import com.citrus.sdk.logger.CitrusLogger;
import com.citrus.sdk.response.CitrusError;
import com.citrus.sdk.response.CitrusResponse;
import com.citruspay.sdkui.ui.utils.CitrusFlowManager;
import com.citruspay.sdkui.ui.utils.PPConfig;

import org.json.JSONException;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Admin on 19-03-2017.
 */

public class PaymentScreen extends BaseActivity implements View.OnClickListener {
    public static String paymentId;
    private String endDateOfPayment;
    private String amount = "";
    private String TAG = "PaymentScreen";
    public static final String DEFAULT_STORAGE_LOCATION = Environment.getExternalStorageDirectory() + "/happybeing/";
    ProgressDialog mProgressDialog;
    private CardView cardView1, cardView2, cardView3;
    public static final String returnUrlLoadMoney = "http://mentalhealthcure.com/citrus/redirect";
    private String userEmail;
    private String userMobile;
    private boolean isOverrideResultScreen;
    private int style = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_screen);
        cardView1 = (CardView) findViewById(R.id.month_purchase_layout);
        cardView2 = (CardView) findViewById(R.id.one_and_half_month_purchase_layout);
        cardView3 = (CardView) findViewById(R.id.year_purchase_layout);

        cardView1.setOnClickListener(this);
        cardView2.setOnClickListener(this);
        cardView3.setOnClickListener(this);
        ((HappyBeingApplicationClass) getApplication()).setAppEnvironment(AppEnvironment.PRODUCTION);
        setupCitrusConfigs();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.month_purchase_layout:
                amount = "250";
                startShoppingFlow();
                break;
            case R.id.one_and_half_month_purchase_layout:
                amount = "600";
                startShoppingFlow();
                break;
            case R.id.year_purchase_layout:
                amount = "2500";
                startShoppingFlow();
                break;
        }
    }

    private void initQuickPayFlow() {
        //Log.i("PaymentScreen", "Email id "+userEmail);
        //Log.i("PaymentScreen", "Mobile number is "+userMobile);
        if (userMobile.length() > 10) {
            userMobile = userMobile.replace("91", "");
        }
        //Log.i("PaymentScreen", "Mobile number is "+userMobile);
        if (isOverrideResultScreen) {
            Toast.makeText(this, "Result Screen will Override", Toast.LENGTH_SHORT).show();
        }
        if (style != -1) {
            CitrusFlowManager.startShoppingFlowStyle(this,
                    userEmail, userMobile, amount, style, isOverrideResultScreen);
        } else {
            CitrusFlowManager.startShoppingFlowStyle(this,
                    userEmail, userMobile, amount, R.style.AppTheme_default, isOverrideResultScreen);
        }
    }

    private void startShoppingFlow() {
        SharedPreferences sharedPreferences = getSharedPreferences("HAPPY_BEING", MODE_PRIVATE);
        userEmail = sharedPreferences.getString(AppConstants.EMAIL_ID, "");
        userMobile = sharedPreferences.getString(AppConstants.MOBILE_NUMBER, "");
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

    public boolean isDifferentUser(String userMobile) {
        String savedUserMobile = "";
        return !savedUserMobile.equals(userMobile);

    }
    private void showUserLoggedInDialog(boolean isQuickPayFlow) {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(this);
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
                CitrusFlowManager.startWalletFlow(PaymentScreen.this, userEmail, userMobile);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.i("PaymentScreen", "In activity result"+requestCode+requestCode+data);

        if (requestCode == CitrusFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=  null) {
            //Log.i("PaymentScreen", "Response captures");
            Toast.makeText(this, "Payment suceess!!!", Toast.LENGTH_SHORT).show();
            finish();
       /*     HappyBeingApplicationClass.userInformation.setIsPaid(true);
            //TODO: present payment successful screen and call api of payment
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 1);

            Date date = calendar.getTime();
            String dateStr = CommonUtils.getTimeFormatInISO(date);
            endDateOfPayment = dateStr;
            paymentId++;
            LoginInfo loginInfo = new LoginInfo(userInformation.getEmailId(), paymentId, "Payment Successfull", "card", endDateOfPayment, 1);
            new APIProvider.SetPayment(loginInfo, 4, this, "Updating payment details", new API_Response_Listener<UserInformation>() {
                @Override
                public void onComplete(UserInformation data, long request_code, String failure_code) {
                    if (data != null) {
                        Toast.makeText(PaymentScreen.this, "Payment successful!!!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Log.i("PaymentScreen","Payment done is failure");
                        //TODO: should try again to upload the content to server
                    }
                }
            }).call();

       */     // You will get data here if transaction flow is started through pay options other than wallet
            TransactionResponse transactionResponse = data.getParcelableExtra(CitrusFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);

            //Log.i("PaymentScreen","Transaction response is "+transactionResponse);
            Toast.makeText(this, "Payment details updated", Toast.LENGTH_SHORT).show();
            finish();
            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getJsonResponse() != null) {
                // Decide what to do with this data
                //Log.i("PaymentScreen", "Transaction json response "+transactionResponse.getJsonResponse());
                HappyBeingApplicationClass.userInformation.setIsPaid(true);
                //TODO: present payment successful screen and call api of payment
                try {
                    MyJsonObject jsonObject = new MyJsonObject(transactionResponse.getJsonResponse());
                    paymentId = jsonObject.getString("TxId");
                }
                catch (JSONException je) {

                }
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, 1);
                Date date = calendar.getTime();
                String dateStr = CommonUtils.getTimeFormatInISO(date);
                endDateOfPayment = dateStr;
                LoginInfo loginInfo = new LoginInfo(userEmail, paymentId, "Payment Successfull", "card", endDateOfPayment, amount);
/*
                new APIProvider.SetPayment(loginInfo, 4, this, "Updating payment details", new API_Response_Listener<UserInformation>() {
                    @Override
                    public void onComplete(UserInformation data, long request_code, String failure_code) {
                        if (data != null) {
                            Toast.makeText(PaymentScreen.this, "Payment successful!!!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Log.i("PaymentScreen","Payment done is failure");
                            //TODO: should try again to upload the content to server
                        }
                    }
                }).call();
*/
                //Log.d("PaymentScreen", "Transaction response : " + transactionResponse.getJsonResponse());
            }
            else {
                //Log.d("PaymentScreen", "Both objects are null!");
                finish();
            }
        }
    }

    private void setupCitrusConfigs() {
        PPConfig.getInstance().setLogLevel(this, CitrusLogger.LogLevel.DEBUG);
        AppEnvironment appEnvironment = ((HappyBeingApplicationClass) getApplication()).getAppEnvironment();
        CitrusFlowManager.initCitrusConfig(appEnvironment.getSignUpId(),
                appEnvironment.getSignUpSecret(), appEnvironment.getSignInId(),
                appEnvironment.getSignInSecret(), ContextCompat.getColor(this, R.color.white),
                this, appEnvironment.getEnvironment(), appEnvironment.getVanity(), appEnvironment.getBillUrl(),
                returnUrlLoadMoney);
        //Log.i("PaymentScreen", "BIll url "+appEnvironment.getBillUrl());
        //To Set the Log Level of Core SDK & Plug & Play

        //To Set the User details
        CitrusUser.Address customAddress = new CitrusUser.Address("Street1", "Street2", "City", "State", "Country", "411045");
        PPConfig.getInstance().setUserDetails("Custom_FName", "Custom_LName", customAddress);
    }
}
