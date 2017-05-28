package com.azuyo.happybeing.ui;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.Utils.AppConstants;
import com.azuyo.happybeing.Utils.HappyBeingApplicationClass;
import com.azuyo.happybeing.Utils.PrefManager;
import com.azuyo.happybeing.Utils.RoundedImageView;
import com.azuyo.happybeing.Utils.UserInformation;
import com.azuyo.happybeing.fragments.AssessmentFragment;
import com.azuyo.happybeing.fragments.MyCoachFragment;
import com.azuyo.happybeing.fragments.MyGoalsFragment;
import com.azuyo.happybeing.fragments.RelaxAndRelieveFragment;

import static com.azuyo.happybeing.R.drawable.student;

/**
 * Created by Admin on 21-12-2016.
 */

public class NewHomeScreen extends BaseActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private ImageView relax_image, assess_image, my_coach_image, my_goals_image, moments_image;
    PrefManager pfm;
    private TextView SignUp, SignIn, User_name, User_email, User_Role;
    String user_login = "false", user_name, user_email;
    private LinearLayout User_details, SignUp_SignIn_Layout;
    public static String selected_role = "";
    ImageView Role_image;
    NavigationView navigationView;
    DrawerLayout drawer;
    private int STORAGE_PERMISSION_CODE = 23;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

        pfm = new PrefManager(NewHomeScreen.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);

         navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        SignUp = (TextView) header.findViewById(R.id.tv_signUp);
        SignIn = (TextView) header.findViewById(R.id.tv_signIn);
        User_Role = (TextView) header.findViewById(R.id.role_type);
        User_name = (TextView) header.findViewById(R.id.tv_name);
        User_email = (TextView) header.findViewById(R.id.tv_email);
        Role_image = (ImageView) header.findViewById(R.id.imageView);
        User_details = (LinearLayout) header.findViewById(R.id.user_details);
        SignUp_SignIn_Layout = (LinearLayout) header.findViewById(R.id.linear_signIn_signUp);

        relax_image = (ImageView) findViewById(R.id.relax_image);
        assess_image = (ImageView) findViewById(R.id.assess_image);
        my_coach_image = (ImageView) findViewById(R.id.my_coach_image);
        my_goals_image = (ImageView) findViewById(R.id.my_goals_image);
        moments_image = (ImageView) findViewById(R.id.moments_image);

         drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(NewHomeScreen.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        RelaxAndRelieveFragment relaxAndRelieveFragment = new RelaxAndRelieveFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        fragmentTransaction.add(R.id.fragment_layout, relaxAndRelieveFragment, "RELAX_AND_RELIVE");
        fragmentTransaction.commit();

        relax_image.setBackground(getResources().getDrawable(R.drawable.relax_orange));

        relax_image.setOnClickListener(this);
        assess_image.setOnClickListener(this);
        my_coach_image.setOnClickListener(this);
        my_goals_image.setOnClickListener(this);
        moments_image.setOnClickListener(this);
        SignIn.setOnClickListener(this);
        SignUp.setOnClickListener(this);

        SharedPreferences prefs = getSharedPreferences("HAPPY_BEING", MODE_PRIVATE);
        user_login = prefs.getString("user_login", "");
        user_name = prefs.getString("user_name", "");
        user_email = prefs.getString("user_email", "");

        selected_role = prefs.getString(AppConstants.ROLE, "");
        NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        hideItem();
        if (navigationMenuView != null) {
            navigationMenuView.setVerticalScrollBarEnabled(false);
        }

        if (user_login.equalsIgnoreCase("true")) {
            SignUp_SignIn_Layout.setVisibility(View.GONE);
            User_details.setVisibility(View.VISIBLE);
            User_name.setText(user_name);
            User_email.setText(user_email);

        } else {
            SignUp_SignIn_Layout.setVisibility(View.VISIBLE);
            User_details.setVisibility(View.GONE);
        }


        switch (selected_role)
        {
            case "Student":
                Role_image.setBackground(getResources().getDrawable(R.drawable.student));
                User_Role.setText("Student");
                break;
            case "looking_job":
                Role_image.setBackground(getResources().getDrawable(R.drawable.looking_for_a_job));
                User_Role.setText("Looking for job");
                break;
            case "employed":
                Role_image.setBackground(getResources().getDrawable(R.drawable.in_other_full_time_job));
                User_Role.setText("Employed");
                break;
            case "staff":
                Role_image.setBackground(getResources().getDrawable(R.drawable.healthcare_staff));
                User_Role.setText("Healthcare staff");
                break;
            case "homemaker":
                Role_image.setBackground(getResources().getDrawable(R.drawable.home_maker));
                User_Role.setText("Home maker");
                break;
            case "seniorcitizen":
                Role_image.setBackground(getResources().getDrawable(R.drawable.senior_citizen));
                User_Role.setText("Senior citizen");
                break;
        }

        /*permission check */
        if(permissioncheck()){
            return;
        }
        //If the app has not the permission then asking for the permission
        requestStoragePermission();



    }

    private boolean permissioncheck() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
       // int result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        return false;
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){

        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.RECEIVE_SMS, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE
                },STORAGE_PERMISSION_CODE);
    }
    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }



    private void hideItem() {
        if (user_login.equalsIgnoreCase("true")) {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.get_premium).setVisible(true);

        }else {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.get_premium).setVisible(false);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.relax_image:
                RelaxAndRelieveFragment relaxAndRelieveFragment = new RelaxAndRelieveFragment();
                gotoFragment(relaxAndRelieveFragment, "RELAX_AND_RELIVE");
                setImageColor(R.drawable.relax_orange, R.drawable.assess, R.drawable.coach, R.drawable.my_goals, R.drawable.my_moments);
                break;
            case R.id.assess_image:
                AssessmentFragment assessmentFragment = new AssessmentFragment();
                gotoFragment(assessmentFragment, "ASSESSMENT");
                setImageColor(R.drawable.relax, R.drawable.assess_orange, R.drawable.coach, R.drawable.my_goals, R.drawable.my_moments);
                break;
            case R.id.my_coach_image:
                MyCoachFragment myCoachFragment = new MyCoachFragment();
                gotoFragment(myCoachFragment, "MY_COACH");
                setImageColor(R.drawable.relax, R.drawable.assess, R.drawable.my_coach_orage, R.drawable.my_goals, R.drawable.my_moments);
                break;
            case R.id.my_goals_image:
                MyGoalsFragment myGoals_fragment = new MyGoalsFragment();
                gotoFragment(myGoals_fragment, "MY_GOAL");
                setImageColor(R.drawable.relax, R.drawable.assess, R.drawable.coach, R.drawable.my_goals_orange, R.drawable.my_moments);
                break;
            case R.id.moments_image:
                setImageColor(R.drawable.relax, R.drawable.assess, R.drawable.coach, R.drawable.my_goals, R.drawable.my_moments_orange);
                break;

            case R.id.tv_signIn:
                Intent signin_intent = new Intent(NewHomeScreen.this, SignInActivity.class);
                startActivity(signin_intent);
                break;
            case R.id.tv_signUp:
                Intent signup_intent = new Intent(NewHomeScreen.this, SignUpActivity.class);
                startActivity(signup_intent);
        }
    }

    private void gotoFragment(Fragment fragment, String fragment_name) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        fragmentTransaction.replace(R.id.fragment_layout, fragment, fragment_name);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    private void setImageColor(int image1, int image2, int image3, int image4, int image5) {
        relax_image.setBackground(getResources().getDrawable(image1));
        assess_image.setBackground(getResources().getDrawable(image2));
        my_coach_image.setBackground(getResources().getDrawable(image3));
        my_goals_image.setBackground(getResources().getDrawable(image4));
        moments_image.setBackground(getResources().getDrawable(image5));

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.alert_setting) {
            drawer.closeDrawers();
            Intent profile_intent = new Intent(NewHomeScreen.this, SettingsLayout.class);
            startActivity(profile_intent);
            return true;
        }else if (id == R.id.my_profile) {
            drawer.closeDrawers();
            Intent profile_intent = new Intent(NewHomeScreen.this, MyProfileLayout.class);
            startActivity(profile_intent);
            return true;
        }
        else if (id == R.id.share) {
            drawer.closeDrawers();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "I like to see you always happy. Hence sharing with you Happy Being App - Daily mindcare for your happiness, peace and success. http://bit.ly/2i6AA5U");
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Social Media Share"));
            return true;
        } else if (id == R.id.rateus) {
            drawer.closeDrawers();
            Intent profile_intent = new Intent(NewHomeScreen.this, FeedBackScreen.class);
            startActivity(profile_intent);
            return true;
        } else if (id == R.id.call) {
            drawer.closeDrawers();
            UserPhoneCall();
            return true;
        }else if (id == R.id.help) {
            drawer.closeDrawers();
            Intent premium_intent = new Intent(NewHomeScreen.this, HelpScreenActivity.class);
            startActivity(premium_intent);
            return true;
        }else if (id == R.id.get_premium) {
            drawer.closeDrawers();
            Intent premium_intent = new Intent(NewHomeScreen.this, PaymentScreen.class);
            startActivity(premium_intent);
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void UserPhoneCall() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(NewHomeScreen.this, android.Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(NewHomeScreen.this);
            dialog.setTitle("Storage Setting");
            dialog.setMessage("Please enable Storage and Location permissions  \n click Go ->Permissions->Enable options");

            dialog.setPositiveButton("Go", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {

                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",
                                NewHomeScreen.this.getPackageName(), null);
                        intent.setData(uri);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        startActivityForResult(intent, 2);
                    } catch (Exception e) {

                    }
                }
            });

            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "Permissions not enabled. Please Enable Storage, Sms, Call and Location permissions", Toast.LENGTH_LONG).show();
                }
            });
            dialog.show();
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + AppConstants.CONTACT_NO));
            startActivity(callIntent);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
           int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search_button) {
            Intent premium_intent = new Intent(NewHomeScreen.this, HelpScreenActivity.class);
            startActivity(premium_intent);

            return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

}

