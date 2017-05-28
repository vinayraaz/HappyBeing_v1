package com.azuyo.happybeing.ServerAPIConnectors;

/**
 * Created by Admin on 18-03-2017.
 */

public class Urls {
   // public static final String SERVER_ADDRESS = "";
   //Dev server address
    public static final String SERVER_ADDRESS = "http://mentalhealthcure.com";

    public static String getSignUpUrl() {
        return SERVER_ADDRESS+"/v1/user/signup";
    }
    public static String getSignInUrl() {
        return SERVER_ADDRESS+"/v1/user/login";
    }
    public static String getCsrfKayUrl() {
        return SERVER_ADDRESS+"/csrfToken";
    }

    public static String getSetPaymentStatusUrl() {
        return SERVER_ADDRESS+"/v1/user/payment";
    }

    public static String getPaymentStatusUrl() {
        return SERVER_ADDRESS+"/v1/user/payment-expiry";
    }

    public static String getDownloadUrl() {
        return "http://mentalhealthcure.comv1/download/mindgym-for-adults/Day1-Loving-Kindness.mp3";
    }

    public static String getForgotPasswordUrl() {
        return SERVER_ADDRESS+"/v1/user/forgot-password";
    }
}
