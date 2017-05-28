package com.azuyo.happybeing.Utils;

/**
 * Created by Admin on 19-03-2017.
 */
public class LoginInfo {
    private String email_id;
    private String password;
    private String screenName, name, mobileNumber, amount;

    private String transactionId;
    private String transactionMessage, paymentMode, endDateInIso;

    public LoginInfo(String email_id, String password) {
        this.email_id = email_id;
        this.password = password;
    }
    public LoginInfo(String email_id, String name, String password, String mobileNumber) {
        this.email_id = email_id;
        this.password = password;
        this.name = name;
        this.mobileNumber = mobileNumber;
    }
    public LoginInfo(String email_id, String password, String screenName, String name, String mobileNumber) {
        this.email_id = email_id;
        this.password = password;
        this.screenName = screenName;
        this.name = name;
        this.mobileNumber = mobileNumber;
    }

    public LoginInfo(String email_id, String transactionId, String transactionMessage, String paymentMode, String endDateInIso, String amount) {
        this.email_id = email_id;
        this.transactionId = transactionId;
        this.transactionMessage = transactionMessage;
        this.paymentMode = paymentMode;
        this.endDateInIso = endDateInIso;
        this.amount = amount;
    }
    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransactionMessage() {
        return transactionMessage;
    }

    public void setTransactionMessage(String transactionMessage) {
        this.transactionMessage = transactionMessage;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getEndDateInIso() {
        return endDateInIso;
    }

    public void setEndDateInIso(String endDateInIso) {
        this.endDateInIso = endDateInIso;
    }
}
