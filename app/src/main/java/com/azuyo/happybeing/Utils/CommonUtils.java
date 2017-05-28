package com.azuyo.happybeing.Utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.Settings;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Admin on 02-12-2016.
 */

public class CommonUtils {
    public static long MAX_FILE_SIZE = 819200;

    public static boolean imageValidation(Context context, Intent data){
        if(data!=null) {
            long size = Utils.getFileSize(data.getData(), context);
            if (size < MAX_FILE_SIZE) {
                return true;
            } else {
                Toast.makeText(context, "Please upload file less than 800 KB", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            Toast.makeText(context, "Error fetching image", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public static void fillImageView(Context context, Uri uri, ImageView imageView){
        if(uri!=null) {

            Bitmap ThumbImage = null;
            String path = Utils.getPath(uri, context);
            try {
                ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path), 450, 535);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(ThumbImage);
            imageView.setBackgroundResource(android.R.color.transparent);
        }
    }

    public static void fillImage(Context context, Uri uri, ImageView imageView) {
        Bitmap bitmap = null;
        String path = Utils.getPath(uri, context);
        Log.i("CommonUtils", "Path is "+path);
        File file = new File(path);
        bitmap = decodeFile(file);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            imageView.setBackgroundResource(android.R.color.transparent);
        }
        else {
            Log.i("Image", "Bitmap is null *&&&&&&&&&&&");
        }


    }
    public static Bitmap decodeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE=70;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }

    public static Bitmap getBitmapFromUri(Context context, Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                context.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


    public static Bitmap rotateImageIfRequired(Bitmap bitmap, String selectedImage) {

        // Detect rotation
        Matrix matrix = new Matrix();
        matrix.postRotate(getImageOrientation(selectedImage));
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        return rotatedBitmap;
    }

    public static int getImageOrientation(String imagePath){
        int rotate = 0;
        try {

            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotate;
    }

    private static int getRotation(Context context,Uri selectedImage) {

        int rotation = 0;
        ContentResolver content = context.getContentResolver();

        Cursor mediaCursor = content.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { "orientation", "date_added" },
                null, null, "date_added desc");

        if (mediaCursor != null && mediaCursor.getCount() != 0) {
            while(mediaCursor.moveToNext()){
                rotation = mediaCursor.getInt(0);
                break;
            }
        }
        mediaCursor.close();
        return rotation;
    }



    public static void fillForHappyMomentsImageView(Context context, Uri uri, ImageView imageView){
        if(uri!=null){
            Bitmap ThumbImage = null;
            String path = Utils.getPath(uri, context);
            ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path), 450, 435);
            imageView.setImageBitmap(ThumbImage);
            imageView.setBackgroundResource(android.R.color.transparent);
        }
    }
    public static boolean permissionsCheck(Context context) {
        boolean permissionsGranted = true;
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=  PackageManager.PERMISSION_GRANTED)
                    || (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    || (ContextCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
                    || (ContextCompat.checkSelfPermission(context, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
                    || (ContextCompat.checkSelfPermission(context, android.Manifest.permission.SET_ALARM) != PackageManager.PERMISSION_GRANTED))
            {
                permissionsGranted = false;
                displayDialog(context);
            }
        }
        return permissionsGranted;
    }
    public static void displayDialog(final Context context) {
        final AlertDialog.Builder dailog = new AlertDialog.Builder(context);
        dailog.setMessage("Happy being needs permissions for you to use all features.").setTitle("App Permissions");
        dailog.setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final Intent i = new Intent();
                i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                i.addCategory(Intent.CATEGORY_DEFAULT);
                i.setData(Uri.parse("package:" + context.getPackageName()));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                context.startActivity(i);
                dialog.dismiss();
            }
        });
        dailog.setCancelable(false);
        dailog.show();
    }

    public static boolean isNetworkAvailable(Context c) {
        boolean isConnectedWifi = false;
        boolean isConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equals(ni.getTypeName()))
                if (ni.isConnected())
                    isConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase(ni.getTypeName()))
                if (ni.isConnected())
                    isConnectedMobile = true;
        }
        return isConnectedWifi || isConnectedMobile;
    }

    public static boolean checkForOtherAppsPlaying(final Context context) {
        AudioManager.OnAudioFocusChangeListener focusChangeListener =
                new AudioManager.OnAudioFocusChangeListener() {
                    public void onAudioFocusChange(int focusChange) {
                        AudioManager am =(AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
                        switch (focusChange) {

                            case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) :
                                // Lower the volume while ducking.
                                break;
                            case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) :
                                break;

                            case (AudioManager.AUDIOFOCUS_LOSS) :
                                break;

                            case (AudioManager.AUDIOFOCUS_GAIN) :
                                break;
                            default: break;
                        }
                    }
                };

        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int result = am.requestAudioFocus(focusChangeListener, AudioManager.STREAM_MUSIC,  AudioManager.AUDIOFOCUS_GAIN);
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;

    }
    public static boolean hasText(EditText editText, String message) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
                editText.setError(Html.fromHtml("<font color='red'>"+message+"</font>"));
            }else {
                editText.setError(message);
            }
            return false;
        }

        return true;
    }

    @SuppressLint("NewApi")
    public static Bitmap blurRenderScript(Context context,Bitmap smallBitmap, int radius, ImageView imageView) {
        try {
            smallBitmap = RGB565toARGB888(smallBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bitmap bitmap = Bitmap.createBitmap(
                imageView.getWidth(), imageView.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript renderScript = RenderScript.create(context);

        Allocation blurInput = Allocation.createFromBitmap(renderScript, smallBitmap);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(radius); // radius must be 0 < r <= 25
        blur.forEach(blurOutput);

        blurOutput.copyTo(bitmap);
        renderScript.destroy();
        imageView.setImageBitmap(bitmap);
        return bitmap;
    }

    private static Bitmap RGB565toARGB888(Bitmap img) throws Exception {
        int numPixels = img.getWidth() * img.getHeight();
        int[] pixels = new int[numPixels];

        //Get JPEG pixels.  Each int is the color values for one pixel.
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        //Create a Bitmap of the appropriate format.
        Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);

        //Set RGB pixels.
        result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
        return result;
    }

    public static String getTimeFormatInISO(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(tz);
        String nowAsISO = df.format(date);
        return  nowAsISO;
    }

    public static String parseDateToddMMyyyy(String time) {
        String inputPattern = "EEE MMM dd HH:mm:ss Z yyyy";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static boolean isValidPhoneNumber(EditText editText, String abrevation){

        if (!hasText(editText, "Enter Number"))
            return false;

        editText.setError(null);
        Phonenumber.PhoneNumber countryNumberProto = null;
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            countryNumberProto = phoneUtil.parse(editText.getText().toString(), abrevation);
            if(countryNumberProto!=null){
                if(phoneUtil.isValidNumber(countryNumberProto)){
                    return true;
                }
            }
        } catch (NumberParseException e) {

            System.err.println("NumberParseException was thrown: " + e.toString());
        }
        editText.setError("Invalid No");
        return false;
    }

    public static String repalceNumbers(String name) {
        name = name.replace("1", "");
        name = name.replace("2", "");
        name = name.replace("3", "");
        name = name.replace("4", "");
        name = name.replace("5", "");
        name = name.replace("6", "");
        name = name.replace("7", "");
        name = name.replace("8", "");
        name = name.replace("9", "");
        name = name.replace("10", "");
        name = name.replace("11", "");
        name = name.replace("12", "");
        name = name.replace("13", "");
        name = name.replace("14", "");
        name = name.replace("15", "");
        name = name.replace("16", "");
        name = name.replace("17", "");
        name = name.replace("18", "");
        name = name.replace("19", "");
        name = name.replace("20", "");
        name = name.replace("21", "");
        name = name.replace("Day", "");
        name = name.replace("-", " ");
        return name;
    }

}
