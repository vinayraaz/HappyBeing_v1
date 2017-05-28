package com.azuyo.happybeing.ui;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.ServerAPIConnectors.APIProvider;
import com.azuyo.happybeing.ServerAPIConnectors.API_Response_Listener;
import com.azuyo.happybeing.Utils.AppConstants;
import com.azuyo.happybeing.Utils.CommonUtils;
import com.azuyo.happybeing.Utils.HappyBeingApplicationClass;
import com.azuyo.happybeing.Utils.UserInformation;

import java.util.Calendar;

public class UserAccountScreen extends BaseActivity implements View.OnClickListener {
    private LinearLayout settings_layout, share_happiness, feed_back_layout, signed_out_user_layout;
    private Button signUp, signIn, buy_now;
    private ImageView profile_role_image;
    private TextView user_name_textview, profile_role_text, payment_status_text;
    private Dialog dialog_custom;
    private boolean is_signed_in = false, isPaid = false;
    private SharedPreferences sharedPreferences;
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_account);
        user_name_textview = (TextView) findViewById(R.id.user_name_textview);
        signed_out_user_layout = (LinearLayout) findViewById(R.id.signed_out_user_layout);

        profile_role_image = (ImageView) findViewById(R.id.profile_role_image);
        profile_role_text = (TextView) findViewById(R.id.profile_role_text);

        signUp = (Button) findViewById(R.id.sign_up_button);
        signIn = (Button) findViewById(R.id.sign_in_button);
        buy_now = (Button) findViewById(R.id.buy_now);
        payment_status_text = (TextView) findViewById(R.id.payment_status_text);
        settings_layout = (LinearLayout) findViewById(R.id.settings_layout);
        share_happiness = (LinearLayout) findViewById(R.id.share_app_layout);
        feed_back_layout = (LinearLayout) findViewById(R.id.feed_back_layout);
        sharedPreferences = getSharedPreferences("HAPPY_BEING", MODE_PRIVATE);

        settings_layout.setOnClickListener(this);
        share_happiness.setOnClickListener(this);
        feed_back_layout.setOnClickListener(this);
        signIn.setOnClickListener(this);
        signUp.setOnClickListener(this);
        buy_now.setOnClickListener(this);
        setProfileImageAndText();
        is_signed_in = sharedPreferences.getBoolean(AppConstants.IS_SIGNED_IN, false);
        if (is_signed_in) {
            signed_out_user_layout.setVisibility(View.GONE);
            buy_now.setVisibility(View.VISIBLE);
            buy_now.setText("Get premium access");
            user_name_textview.setVisibility(View.VISIBLE);
            payment_status_text.setText("Get personalized content too");
            user_name_textview.setText(sharedPreferences.getString(AppConstants.NAME, ""));
        }
        else {
            signed_out_user_layout.setVisibility(View.VISIBLE);
            user_name_textview.setVisibility(View.GONE);
            payment_status_text.setText("Get access to all our awesome content");
            buy_now.setVisibility(View.GONE);
        }
        isPaid = sharedPreferences.getBoolean(AppConstants.IS_PAID, false);
        //Log.i("UserAccount", "Is paid is "+sharedPreferences.getBoolean(AppConstants.IS_PAID, false));
        updatePaymentDetailsContent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        is_signed_in = sharedPreferences.getBoolean(AppConstants.IS_SIGNED_IN, false);
        if (is_signed_in) {
            buy_now.setVisibility(View.VISIBLE);
            buy_now.setText("Get premium access");
            signed_out_user_layout.setVisibility(View.GONE);
            user_name_textview.setVisibility(View.VISIBLE);
            user_name_textview.setText(sharedPreferences.getString(AppConstants.NAME, ""));
            isPaid = sharedPreferences.getBoolean(AppConstants.IS_PAID, false);
            payment_status_text.setText("Get personalized content too");
            updatePaymentDetailsContent();
        }
        else {
            buy_now.setVisibility(View.GONE);
            signed_out_user_layout.setVisibility(View.VISIBLE);
            user_name_textview.setVisibility(View.GONE);
            payment_status_text.setText("Get access to all our awesome content");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.share_app_layout:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "I like to see you always happy. Hence sharing with you Happy Being App - Daily mindcare for your happiness, peace and success. http://bit.ly/2i6AA5U" );
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Social Media Share"));
                break;
            case R.id.settings_layout:
                startActivity(new Intent(this, SettingsLayout.class));
                break;
            case R.id.feed_back_layout:
                openFeedBackDialogBox();
                break;
            case R.id.like_button:
                launchMarket();
                dialog_custom.dismiss();
                break;
            case R.id.feedback_button:
                startActivity(new Intent(this, FeedBackScreen.class));
                dialog_custom.dismiss();
                break;
            case R.id.sign_in_button:
                Intent intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
                break;
            case R.id.sign_up_button:
                Intent intent1 = new Intent(this, SignUpActivity.class);
                intent1.putExtra("TYPE","SIGN_UP");
                startActivity(intent1);
                break;
            case R.id.buy_now:
                if (is_signed_in) {
                    startActivity(new Intent(this, PaymentScreen.class));
                }
                else {
                    Toast.makeText(this, "Signin first", Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(this, SignUpActivity.class);
                    intent2.putExtra("FROM", "BUY");
                    startActivity(intent2);
                }
                break;
        }
    }

    private void openFeedBackDialogBox() {
        dialog_custom = new Dialog(this);
        dialog_custom.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_custom.setContentView(R.layout.custom_dialog_for_feedback);

        Button yesButton = (Button) dialog_custom.findViewById(R.id.like_button);
        Button feedBackButton = (Button) dialog_custom.findViewById(R.id.feedback_button);
        yesButton.setOnClickListener(this);
        feedBackButton.setOnClickListener(this);
        dialog_custom.show();
    }

    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }

    private void setProfileImageAndText() {
        String profile = sharedPreferences.getString(AppConstants.ROLE, "");
        Log.i("UserAccountScreen", "Role is "+profile);
        if (profile != null && !profile.equals("")) {
            if (profile.contains("Student")) {
                profile_role_image.setBackground(getResources().getDrawable(R.drawable.student));
                profile_role_text.setText("Student");
            } else if (profile.contains("Home_maker")) {
                profile_role_image.setBackground(getResources().getDrawable(R.drawable.home_maker));
                profile_role_text.setText("Home maker");
            } else if (profile.contains("Expecting_Mom")) {
                profile_role_image.setBackground(getResources().getDrawable(R.drawable.expecting_mom));
                profile_role_text.setText("Expecting mom");
            } else if (profile.contains("Senior_Citizen")) {
                profile_role_image.setBackground(getResources().getDrawable(R.drawable.elderly));
                profile_role_text.setText("Senior citizen");
            } else if (profile.contains("Working_full_time")) {
                profile_role_image.setBackground(getResources().getDrawable(R.drawable.working_full_time));
                profile_role_text.setText("Working full time");
            }
            else if (profile.contains("It_Professional")) {
                profile_role_text.setText("It professional");
            }
            else if (profile.contains("Looking_For_Job")) {
                profile_role_text.setText("Looking for job");
            }
            else if (profile.contains("Health_Care_staff")) {
                profile_role_text.setText("Health care staff");
            }
            else if (profile.contains("Want_to_be_mom")) {
                profile_role_text.setText("Want to be mom");
            }
        }
    }

    private void updatePaymentDetailsContent() {
        if (CommonUtils.isNetworkAvailable(this)) {
            boolean isLogedIn = sharedPreferences.getBoolean(AppConstants.IS_SIGNED_IN, false);
            String emailId = sharedPreferences.getString(AppConstants.EMAIL_ID, "");
            //Log.i("UserAccount", "Is logegd in "+isLogedIn);
            if (isLogedIn) {
                new APIProvider.PaymentStatus(emailId, 6, new API_Response_Listener<Integer>() {
                    @Override
                    public void onComplete(Integer data, long request_code, String failure_code) {
                       // Log.i("UserAccount","In on complete "+data);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if (data != null && data > 0) {
                            editor.putInt("ExpiryDate", data);
                            editor.putBoolean(AppConstants.IS_PAID, true);
                            editor.commit();
                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.DATE, data);
                            String date = CommonUtils.parseDateToddMMyyyy(calendar.getTime().toString());
                            payment_status_text.setText("");
                            buy_now.setVisibility(View.VISIBLE);
                            buy_now.setText("Premium access \nRenew before " + date);
                        } else {
                            editor.putBoolean(AppConstants.IS_PAID, false);
                            editor.apply();
                            payment_status_text.setText("Get personalized content too");
                            buy_now.setVisibility(View.VISIBLE);
                            buy_now.setText("GET PREMIUM ACCESS");
                        }
                    }
                }).call();
            }
            else {
                payment_status_text.setText("Get access to all our awesome content");
                buy_now.setVisibility(View.GONE);
                signed_out_user_layout.setVisibility(View.VISIBLE);
            }
        }
    }
}
