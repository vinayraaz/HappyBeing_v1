package com.azuyo.happybeing.ServerAPIConnectors;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.azuyo.happybeing.Utils.AppConstants;
import com.azuyo.happybeing.Utils.LoginInfo;
import com.azuyo.happybeing.Utils.UserInformation;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class JSONParser {

    public static String getErrorString(String response) {
        if (response.contains("mobile already exists")) {
            return APIProvider.MOBILE_NUMBER_EXISTS;
        }
        else if (response.contains("email already exists")) {
            return APIProvider.EMAIL_EXISTS;
        }
        else {
            try {
                JSONObject jobj = new JSONObject(response);
                if (jobj.has("errors")) {
                    return jobj.getString("errors");
                }

                return null;
            } catch (JSONException e) {
                //e.printStackTrace();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jobj = jsonArray.getJSONObject(i);
                        if (jobj.has("errors")) {
                            return jobj.getString("errors");
                        }
                    }
                    return null;
                } catch (JSONException e1) {
                    e1.printStackTrace();
                    return "json exception";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "unknown error";
            }
        }
    }

    public static UserInformation getUserInformation(String response) {
        //Log.i("JsonParser","response for sign in is "+response);
        UserInformation userInformation = new UserInformation();
        try {
            MyJsonObject jsonObject = new MyJsonObject(response);
            String user = jsonObject.getString("user");
            String roles = jsonObject.getString("roles");


            MyJsonObject jsonObject1 = new MyJsonObject(user);
            if (roles != null && !roles.equals("")) {
                JSONArray jsonArray = new JSONArray(roles);
                for (int i = 0; i < jsonArray.length(); i++) {
                    userInformation.setRole(jsonArray.getString(i));
                }
            }

            String name = jsonObject1.getString("name");
            String screenName = jsonObject1.getString("screenname");
            String contactDetails = jsonObject1.getString("contactDetails");

            MyJsonObject jsonObject2 = new MyJsonObject(contactDetails);
            String mobile = jsonObject2.getString("mobile");
            String email = jsonObject2.getString("email");

            String paymentDetails = jsonObject1.getString("myPaymentDetails");
           // Log.i("JsonParser", "Payment details is "+paymentDetails);
            if (paymentDetails != null && !paymentDetails.equals("")) {
                MyJsonObject paymentJson = new MyJsonObject(paymentDetails);
                if (paymentDetails.contains("isPaymentDone")) {
                    boolean paymentDone = paymentJson.getBoolean("isPaymentDone");
                    //Log.i("JsonParser", "Payment done response is " + paymentDone);
                    userInformation.setIsPaid(paymentDone);
                }
                //Log.i("JsonParser","Paid is "+userInformation.isPaid());
            }
            else {
                userInformation.setIsPaid(false);
            }
            userInformation.setEmailId(email);
          //  userInformation.setLogedIn(, true);
            userInformation.setMobileNumber(mobile);
            userInformation.setScreenName(screenName);
            userInformation.setName(name);
            //Log.i("JsonParser","Paid is "+userInformation.isPaid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userInformation;
    }

    public static JSONObject getJsonForLogin(LoginInfo data) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("_csrf", UserInformation.csrf_key);
            jsonObject.put("email", data.getEmail_id());
            jsonObject.put("password", data.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    public static JSONObject getJsonForSignUp(Context context, LoginInfo data) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("HAPPY_BEING", MODE_PRIVATE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("_csrf", UserInformation.csrf_key);
            jsonObject.put("email", data.getEmail_id());
            jsonObject.put("password", data.getPassword());
            jsonObject.put("name", data.getName());
            jsonObject.put("mobile", data.getMobileNumber());
            jsonObject.put("user_role", sharedPreferences.getString(AppConstants.ROLE, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    public static String getResultCrsfKey(String response) {
        String crsf = "";
        try {
            MyJsonObject jsonObject = new MyJsonObject(response);
            crsf = jsonObject.getString("_csrf");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return crsf;
    }

    public static JSONObject getJsonForPayment(LoginInfo data) {
        JSONObject jsonObject = new JSONObject();
        Calendar calendar = Calendar.getInstance();
        //Log.i("JsonParser", "TXID "+data.getTransactionId()+" Expiry date "+data.getEndDateInIso() );
        try {
            jsonObject.put("TxId", data.getTransactionId());
            jsonObject.put("TxMsg", data.getTransactionMessage());
            jsonObject.put("paymentMode", data.getPaymentMode());
            jsonObject.put("txnDateTime", calendar.getTime());
            jsonObject.put("txnExpiryDateTime", data.getEndDateInIso());
            jsonObject.put("amount", data.getAmount());
            jsonObject.put("email", data.getEmail_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static UserInformation getUserInfoFromJson(String response) {
        UserInformation userInformation = new UserInformation();
        try {
            MyJsonObject jsonObject = new MyJsonObject(response);
            String successString = jsonObject.getString("success");
            if (successString != null && !successString.equals("")) {
                userInformation.setIsPaid(true);
            }
            else {
                userInformation.setIsPaid(false);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userInformation;
    }

    public static JSONObject getJsonForPaymentStatus(String data) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static Integer getDaysLeftFromJson(String response) {
        int daysLeft = 0;
        try {
            MyJsonObject jsonObject = new MyJsonObject(response);
            String success = jsonObject.getString("success");
            if (success != null && !success.equals("")) {
                MyJsonObject jsonObject1 = new MyJsonObject(success);
                long daysLeftlong = jsonObject1.getLong("daysleft");
                daysLeft = (int) (daysLeftlong / (1000*60*60*24));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return daysLeft;
    }

    public static File getFileFromJson(String response) {
        File file = null;
        return file;
    }


    public static String getSuccessResponse(String response) {
        boolean sentSuccess = false;
        String message = "";
        try {
            MyJsonObject jsonObject = new MyJsonObject(response);
            if (jsonObject.has("success")){
                 sentSuccess = jsonObject.getBoolean("success");
            }
            if (sentSuccess) {
                message = "success";
            }
            else
                message = "error";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }
}