package com.azuyo.happybeing.ServerAPIConnectors;

import android.app.Activity;
import android.util.Log;

import com.azuyo.happybeing.Utils.LoginInfo;
import com.azuyo.happybeing.Utils.UserInformation;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by Kavya on 19/03/2017.
 */
public class APIProvider {

    public static int INVALID_CREDENTIALS = 1001;
    public static int LOGIN_FAILED = 1002;

    public static int FORGOT_PASSWORD_FAILED = 1003;

    public static int EMAIL_ALREADY_EXISTS = 1004;
    public static int INVALID_EMAIL = 1005;
    public static int INVALID_LOGIN_METHOD = 1035;
    public static int CANNOT_CREATE_USER = 1006;

    public static int CANNOT_CREATE_MAIL = 1007;
    public static int CANNOT_UPDATE_USER = 1008;
    public static int CANNOT_PUT_MAIL = 1010;

    public static String MOBILE_NUMBER_EXISTS = "MOBILE_NUMBER_EXISTS";
    public static String EMAIL_EXISTS = "EMAIL_EXISTS";


    public static class CheckLogin extends WaitingAPIAdapter<LoginInfo, UserInformation> {
        public LoginInfo data;

        public CheckLogin(LoginInfo input_data, long requestCode, Activity activity, String message, API_Response_Listener<UserInformation> listener) {
            super(input_data, requestCode, activity, message, listener);
            this.data = input_data;
        }

        @Override
        public String getURL() {
            return Urls.getSignInUrl();
        }

        @Override
        public String getHttpMethod() {
            return "POST";
        }

        @Override
        public JSONObject convertDataToJSON(LoginInfo data) {
           // Log.i("APiProvider","Json object is "+JSONParser.getJsonForLogin(data));
            return JSONParser.getJsonForLogin(data);
        }

        @Override
        public UserInformation convertJSONToData(String response) {
            //Log.i("APiProvider","Response is "+response);
            return JSONParser.getUserInformation(response);
        }

        @Override
        public String intrepret_error(String error) {
            //Log.i("ApiProvider","Error is "+error);
            return error;
        }
    }

    public static class SignUpUser extends WaitingAPIAdapter<LoginInfo, UserInformation> {
        public LoginInfo data;
        private Activity activity;
        public SignUpUser(LoginInfo input_data, long requestCode, Activity activity, String message, API_Response_Listener<UserInformation> listener) {
            super(input_data, requestCode, activity, message, listener);
            this.data = input_data;
            this.activity = activity;
        }

        @Override
        public String getURL() {
            return Urls.getSignUpUrl();
        }

        @Override
        public String getHttpMethod() {
            return "POST";
        }

        @Override
        public JSONObject convertDataToJSON(LoginInfo data) {
            return JSONParser.getJsonForSignUp(activity, data);
        }

        @Override
        public UserInformation convertJSONToData(String response) {
            return JSONParser.getUserInformation(response);
        }

        @Override
        public String intrepret_error(String error) {
            return error;
        }
    }

    public static class SetPayment extends WaitingAPIAdapter<LoginInfo, UserInformation> {
        public LoginInfo data;

        public SetPayment(LoginInfo input_data, long requestCode, Activity activity, String message, API_Response_Listener<UserInformation> listener) {
            super(input_data, requestCode, activity, message, listener);
            this.data = input_data;
        }

        @Override
        public String getURL() {
            return Urls.getSetPaymentStatusUrl();
        }

        @Override
        public String getHttpMethod() {
            return "POST";
        }

        @Override
        public JSONObject convertDataToJSON(LoginInfo data) {
            //Log.i("ApiProvider","Json for payment is "+JSONParser.getJsonForPayment(data));
            return JSONParser.getJsonForPayment(data);
        }

        @Override
        public UserInformation convertJSONToData(String response) {
            //Log.i("ApiProvider","Response for payment is "+JSONParser.getUserInfoFromJson(response));
            return JSONParser.getUserInfoFromJson(response);
        }

        @Override
        public String intrepret_error(String error) {
            return error;
        }
    }
    public static class PaymentStatus extends WaitingAPIAdapter<String, Integer> {
        public String data;

        public PaymentStatus(String input_data, long requestCode, Activity activity, String message, API_Response_Listener<Integer> listener) {
            super(input_data, requestCode, activity, message, listener);
            this.data = input_data;
        }
        public PaymentStatus(String input_data, long requestCode, API_Response_Listener<Integer> listener) {
            super(input_data, requestCode, null, null, listener);
            this.data = input_data;
        }

        @Override
        public String getURL() {
            return Urls.getPaymentStatusUrl();
        }

        @Override
        public String getHttpMethod() {
            return "POST";
        }

        @Override
        public JSONObject convertDataToJSON(String data) {
            //Log.i("ApiProvider","Json for payment is "+JSONParser.getJsonForPaymentStatus(data));
            return JSONParser.getJsonForPaymentStatus(data);
        }

        @Override
        public Integer convertJSONToData(String response) {
            //Log.i("ApiProvider","Response for payment is "+JSONParser.getUserInfoFromJson(response));
            return JSONParser.getDaysLeftFromJson(response);
        }

        @Override
        public String intrepret_error(String error) {
            return error;
        }
    }
    public static class DownloadFile extends WaitingAPIAdapter<String, File> {
        public String data;

        public DownloadFile(String input_data, long requestCode, Activity activity, String message, API_Response_Listener<File> listener) {
            super(input_data, requestCode, activity, message, listener);
            this.data = input_data;
        }
        public DownloadFile(String input_data, long requestCode, API_Response_Listener<File> listener) {
            super(input_data, requestCode, null, null, listener);
            this.data = input_data;
        }
        @Override
        public String getURL() {
            return Urls.getDownloadUrl();
        }
        @Override
        public String getHttpMethod() {
            return "GET";
        }

        @Override
        public File convertJSONToData(String response) {
            //Log.i("ApiProvider","Response for download file is  "+JSONParser.getUserInfoFromJson(response));
            return JSONParser.getFileFromJson(response);
        }
    }
    public static class ForgotPassword extends WaitingAPIAdapter<String, String> {
        public String data;

        public ForgotPassword(String input_data, long requestCode, Activity activity, String message, API_Response_Listener<String> listener) {
            super(input_data, requestCode, activity, message, listener);
            this.data = input_data;
        }
        public ForgotPassword(String input_data, long requestCode, API_Response_Listener<String> listener) {
            super(input_data, requestCode, null, null, listener);
            this.data = input_data;
        }
        @Override
        public String getURL() {
            return Urls.getForgotPasswordUrl();
        }
        @Override
        public String getHttpMethod() {
            return "POST";
        }

        @Override
        public JSONObject convertDataToJSON(String data) {
            return JSONParser.getJsonForPaymentStatus(data);
        }

        @Override
        public String convertJSONToData(String response) {
            //Log.i("ApiProvider","Response for download file is  "+JSONParser.getUserInfoFromJson(response));
            if (response != null && response.equalsIgnoreCase("invalid mobile number or email id")) {
                return "Invalid email id";
            }
            else {
                return JSONParser.getSuccessResponse(response);
            }
        }
    }
}
