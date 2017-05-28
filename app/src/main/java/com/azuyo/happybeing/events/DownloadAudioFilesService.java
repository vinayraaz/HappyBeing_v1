package com.azuyo.happybeing.events;

import android.app.Activity;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.azuyo.happybeing.MindGym.GetDownloadStatus;
import com.azuyo.happybeing.ui.ProfileScreen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Admin on 25-01-2017.
 */

public class DownloadAudioFilesService extends IntentService {

    String TAG = "Mindfulness";

    private String fileDirPath;
    SharedPreferences.Editor editor = null;
    private GetDownloadStatus getDownloadStatus;
    public static String[] relaxAndRelieveFileNames = { "BELLY-BREATHING.mp3","4-7-8-BREATHING.mp3","Abdominal-Breathing.mp3",
            "Tense-and-relax.mp3", "Shake-ur-Body.mp3", "Calmness-meditation.mp3","self-comapssion-meditation.mp3",
            "Mindful-Body-Scan.mp3","Self-Massage.mp3","","","","","","","Ocean-Waves-Loop.mp4",
            "nature_calms.mp3","rain.mp3"};
    public static String[] relaxAndRelieveFiles = {
            "http://mentalhealthcure.com/v1/download/relieve-and-relax-list/BELLY-BREATHING.mp3",
            "http://mentalhealthcure.com/v1/download/relieve-and-relax-list/4-7-8-BREATHING.mp3",
            "http://mentalhealthcure.com/v1/download/relieve-and-relax-list/Abdominal-Breathing.mp3",
            "http://mentalhealthcure.com/v1/download/relieve-and-relax-list/Tense-and-relax.mp3",
            "http://mentalhealthcure.com/v1/download/relieve-and-relax-list/Shake-ur-Body.mp3",
            "http://mentalhealthcure.com/v1/download/relieve-and-relax-list/Calmness-meditation.mp3",
            "http://mentalhealthcure.com/v1/download/relieve-and-relax-list/self-comapssion-meditation.mp3",
            "http://mentalhealthcure.com/v1/download/relieve-and-relax-list/Mindful-Body-Scan.mp3",
            "http://mentalhealthcure.com/v1/download/relieve-and-relax-list/Self-Massage.mp3",
            "",
            "",
            "",
            "",
            "",
            "",
            "http://mentalhealthcure.com/v1/download/relieve-and-relax-list/Ocean-Waves-Loop.mp4",
            "http://mentalhealthcure.com/v1/download/relieve-and-relax-list/nature_calms.mp3",
            "http://mentalhealthcure.com/v1/download/relieve-and-relax-list/rain.mp3",
    };


    private MyPhoneStateListener phoneStateListener = null;
    private TelephonyManager telephony;
    public DownloadAudioFilesService(String name) {
        super(name);
    }

    public DownloadAudioFilesService() {
        super("");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        // Log.i("Mindfulness","In on Handle of Downloading service"+ ProfileScreen.downloadedFileCount);
        ProfileScreen.downloadAudioFilesService = this;
        editor = getSharedPreferences("HAPPY_BEING", MODE_PRIVATE).edit();
        fileDirPath = getFilesDir().getAbsolutePath() + File.separator + "HappyBeing";
        downloadFile(relaxAndRelieveFileNames[0], 0);
        if (phoneStateListener == null) {
            phoneStateListener = new MyPhoneStateListener(getApplicationContext());
            telephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            telephony.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    private void downloadFile(String name , int audioFileNumber) {
        final DownloadTask downloadTask = new DownloadTask(getApplicationContext(), name);
        downloadTask.execute(relaxAndRelieveFiles[audioFileNumber]);
    }

    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;
        private String fileName;

        public DownloadTask(Context context, String fileName) {
            this.context = context;
            this.fileName = fileName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            if (result != null) {
                //TODO: make particular boolean as false
                //Log.i(TAG, "Download error: " + result);
            }
            else {
                //TODO: make file downloaded true and download another file
                //Log.i(TAG, "File downloaded");
                editor.putBoolean("downloadfile"+fileName, true);
                editor.commit();
                ProfileScreen.downloadedFileCount++;
                if (ProfileScreen.downloadedFileCount < relaxAndRelieveFiles.length) {
                    downloadFile(relaxAndRelieveFileNames[ProfileScreen.downloadedFileCount], ProfileScreen.downloadedFileCount);
                }
            }
        }
        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6ImRlZmF1bHQiLCJpYXQiOjE0ODk5MzA5NDZ9.x3MobPwdUIUx_JrNCu7bG2aMFz4oaMAWo4VbUmy6FDY");
                connection.connect();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }
                int fileLength = connection.getContentLength();
                // download the file
                input = connection.getInputStream();
                String fileDirPath = getFilesDir().getAbsolutePath() + File.separator + "HappyBeing";
                final String storagePath = fileDirPath + "/audiofiles";
                final File dir = new File(storagePath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                output = new FileOutputStream(dir+"/"+fileName);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }
    }
    public static class DownloadOneFileTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;
        private String fileName;
        ProgressDialog mProgressDialog;
        private GetDownloadStatus getDownloadStatus;

        public DownloadOneFileTask(Context context, String fileName) {
            this.context = context;
            this.fileName = fileName;
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Downloading");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(true);
            getDownloadStatus = (GetDownloadStatus) (Activity)context;

            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cancel(true);
                }
            });
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }
        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressDialog.dismiss();
            if (result != null) {
                //TODO: make particular boolean as false
                // Log.i("Mindfulness", "Download error: " + result);
            }
            else {
                //TODO: make file downloaded true and download another file
                //   Log.i("Mindfulness", "File downloaded");
                SharedPreferences.Editor editor =  context.getSharedPreferences("HAPPY_BEING", MODE_PRIVATE).edit();
                editor.putBoolean("downloadfile"+fileName, true);
                getDownloadStatus.downloadedStatus(true);
                editor.commit();
            }
        }
        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6ImRlZmF1bHQiLCJpYXQiOjE0ODk5MzA5NDZ9.x3MobPwdUIUx_JrNCu7bG2aMFz4oaMAWo4VbUmy6FDY");
                connection.connect();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }
                int fileLength = connection.getContentLength();
                // download the file
                input = connection.getInputStream();
                String fileDirPath = context.getFilesDir().getAbsolutePath() + File.separator + "HappyBeing";
                final String storagePath = fileDirPath + "/audiofiles";
                final File dir = new File(storagePath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                output = new FileOutputStream(dir+"/"+fileName);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }
    }

    @Override
    public void onDestroy() {
        ProfileScreen.downloadAudioFilesService = null;
        super.onDestroy();
    }
}

