package com.azuyo.happybeing.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.Utils.CommonUtils;
import com.azuyo.happybeing.Utils.MySql;
import com.azuyo.happybeing.Utils.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class ComposeNewHappyMoment extends BaseActivity implements View.OnClickListener {
    private EditText titleEdittext1, editText2, editText3, editText4;
    private RelativeLayout uploadImageLayout;
    private Dialog dialog_custom;
    protected static final int PICK_Camera_IMAGE = 0;
    private int FETCH_PHOTO_REQUEST = 4;
    private Uri imageFilePath, cameraImageFileUri;
    private ImageView imageView, upoadImageview;
    private SQLiteDatabase db;
    private AdView mAdView = null;
    private TextView titleText;
    private long dbId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compose_new_happy_moment);
        titleText = (TextView) findViewById(R.id.title_name);
        titleText.setText("Happy moments");
        ImageView userAccount = (ImageView) findViewById(R.id.account_circle);
        userAccount.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(true);
        MySql dbHelper = new MySql(this, "mydb", null, ProfileScreen.dbVersion);
        db = dbHelper.getWritableDatabase();
        titleEdittext1 = (EditText) findViewById(R.id.title_edittext1);
        editText2 = (EditText) findViewById(R.id.title_edittext2);
        editText3 = (EditText) findViewById(R.id.title_edittext3);
        editText4 = (EditText) findViewById(R.id.title_edittext4);
        uploadImageLayout = (RelativeLayout) findViewById(R.id.uploadImageLayout);
        imageView = (ImageView) findViewById(R.id.image_view);
        upoadImageview = (ImageView) findViewById(R.id.upoadImageview);

        uploadImageLayout.setOnClickListener(this);
        addGoogleAds();
        Intent intent = getIntent();
        if (intent.hasExtra("dbId")) {
            dbId = intent.getLongExtra("dbId", 0);
            loadDetailsFromDB(dbId);
            titleText.setText("Edit happy moment");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.text_item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.text_item:
                if (checkValidation())
                saveToDB();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.uploadImageLayout) {
            displayDialogToCapturePictureOrSelectImage();
        }
        if(id == R.id.camera) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            ContentValues values = new ContentValues(3);
            values.put(MediaStore.Images.Media.DISPLAY_NAME, Calendar.getInstance().toString());
            values.put(MediaStore.Images.Media.DESCRIPTION, "this is description");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            try {
                imageFilePath = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                startActivityForResult(intent, PICK_Camera_IMAGE);
            }
            catch (SecurityException se) {
                askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 1);
                askForPermission(Manifest.permission.CAMERA, 83);
            }
        }
        if (id == R.id.gallery) {
            getImage(FETCH_PHOTO_REQUEST);
        }
    }

    public void getImage(int requestType) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), requestType);
    }

    private void displayDialogToCapturePictureOrSelectImage() {
        dialog_custom = new Dialog(this);
        dialog_custom.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_custom.setContentView(R.layout.custom_dialog_for_image_options);

        ImageView camera = (ImageView) dialog_custom.findViewById(R.id.camera);
        camera.setOnClickListener(this);
        ImageView gallery = (ImageView) dialog_custom.findViewById(R.id.gallery);
        gallery.setOnClickListener(this);
        dialog_custom.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        upoadImageview.setVisibility(View.GONE);
        if (requestCode == PICK_Camera_IMAGE && resultCode == RESULT_OK) {
            dialog_custom.dismiss();
            //TODO: store the image file path to local db of my happy moments
            if (data != null) {
                if (data.getData() != null) {
                    cameraImageFileUri = data.getData();
                }
               if (cameraImageFileUri != null) {
                    String path = Utils.getPath(cameraImageFileUri, this);
                    //Log.i("HappyMoment","PAth is "+path);
                    Bitmap bitmap = null;
                    try {
                        bitmap = CommonUtils.getBitmapFromUri(this, cameraImageFileUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //Log.i("HappyMoment","Result bitmap is "+bitmap);
                    Bitmap resultBitmap = CommonUtils.rotateImageIfRequired(bitmap, path);
                    imageView.setImageBitmap(resultBitmap);
                }
                else {
                  Bitmap bitmap  = (Bitmap) data.getExtras().get("data");
                   imageView.setImageBitmap(bitmap);
                   cameraImageFileUri = getImageUri(this, bitmap);
               }
              //  CommonUtils.fillImageView(this, cameraImageFileUri, imageView);

            }
        } else if (requestCode == FETCH_PHOTO_REQUEST && resultCode == RESULT_OK) {
            dialog_custom.dismiss();
            if (data != null) {
                //Log.i("HappyMoment","Fetch request Image uri in on activity result is "+data.getData());
                cameraImageFileUri = data.getData();
                CommonUtils.fillImage(this, cameraImageFileUri, imageView);
            }
        }
    }

    private void saveToDB() {
        long time= System.currentTimeMillis();
        long id;
        if (cameraImageFileUri != null && !cameraImageFileUri.equals("")) {
            //TODO: create a  table entry with Image uri
            ContentValues cv = new ContentValues();
            cv.put("TITLE", titleEdittext1.getText().toString()+" \n "
                    +editText2.getText().toString()+" \n "+editText3.getText().toString()+"\n"+editText4.getText().toString());
            cv.put("IMAGE_URI", cameraImageFileUri.toString());
            cv.put("TIME", time);
            if (dbId == 0) {
                id = db.insert("My_Happy_Moments", null, cv);
            }
            else {
                id = db.update("My_Happy_Moments", cv, "_id="+dbId, null);
            }
            finish();
            //Log.i("ProfileScreen","Inserted in local db id is "+id+" Uri is "+cameraImageFileUri);
        }
        else {
            Toast.makeText(this, "Please enter all fields..", Toast.LENGTH_SHORT).show();
        }

    }
    private void addGoogleAds() {
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7447665870261345~2301766396");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (cameraImageFileUri != null) {
            outState.putString("cameraImageUri", cameraImageFileUri.toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("cameraImageUri")) {
            cameraImageFileUri = Uri.parse(savedInstanceState.getString("cameraImageUri"));
        }
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            }
        }
    }

    private void loadDetailsFromDB(long dbId) {
        Cursor cursor = db.rawQuery("SELECT * FROM My_Happy_Moments where _id=" + "'" + dbId + "'", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String title = cursor.getString(cursor.getColumnIndex("TITLE"));
            String content = cursor.getString(cursor.getColumnIndex("CONTENT"));
            String imageUriString = cursor.getString(cursor.getColumnIndex("IMAGE_URI"));
            if (imageUriString != null) {
                Uri imageuri = Uri.parse(imageUriString);
                cameraImageFileUri = imageuri;
                CommonUtils.fillImage(this, imageuri, imageView);
                upoadImageview.setVisibility(View.GONE);
            }
            titleEdittext1.setText(title);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private boolean checkValidation() {
        boolean ret = true;
        //checks username and password editText fields are empty or not

        if (!CommonUtils.hasText(titleEdittext1, "Please fill out this field"))
            ret = false;
        if (!CommonUtils.hasText(editText2, "Please fill out this field"))
            ret = false;
        if (!CommonUtils.hasText(editText3, "Please fill out this field"))
            ret = false;
        if (!CommonUtils.hasText(editText4, "Please fill out this field"))
            ret = false;
        return ret;
    }

}