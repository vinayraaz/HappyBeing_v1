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
import com.azuyo.happybeing.Utils.AppConstants;
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

public class DownloadMindGymAudioFilesService extends IntentService {

    String TAG = "Mindfulness";

    private String fileDirPath;
    private String contentType = "";
    SharedPreferences.Editor editor = null;
    public static String[] mindGymForAdultsAudioListNames = {"Day1-Loving-Kindness", "Day2-Progressive-Muscle-Relaxation", "Day3-JOYFULL-MEDITATION",
            "Day4-INNER-ADVICE-MEDITATION", "Day4-INNER-ADVICE-MEDITATION", "Day5-Mindfullness-Of-Beathing", "Day6-OVERCOME-FEAR-OF-FAILURE",
            "Day7--Mindfulness-of-thoughts", "Day8-FORGIVENESS-MEDITATION", "Day9-ENHANCE-INNER-STRENGTH", "Day10-Visualisation-with-snorkelling",
            "Day11-BOOST-SELF-CONFIDENCE", "Day12-IMPROVING-SELF-DISCIPLINE", "Day13--Mindfulness-of-greed", "Day14-GRATITUDE-MEDITATION", "Day15-MINDFULLNESS-DISTRACTION",
            "Day16-Mindfulness-of-pain", "Day17-Overcoming-obstacles-and-mental-blocks", "Day18-REACH-YOUR-HIGHEST-POTENTIAL", "Day-19-UNLEASH-THE-WINNER-IN-YOU",
            "Day-20-Visualization-for-Inner-peace", "Day21-BOOST-WILL-POWER", "Day22-Higher-Advice", "Day23-PAIN-MANAGEMENT", "Q_A"};

    public static String[] mindGymAdultAudioFiles = {"http://mentalhealthcure.com/v1/download/mindgym-for-adults/Day1-Loving-Kindness.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-adults/Day2-Progressive-Muscle-Relaxation.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-adults/Day3-JOYFULL-MEDITATION.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-adults/Day4-INNER-ADVICE-MEDITATION.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-adults/Day4-INNER-ADVICE-MEDITATION.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-adults/Day5-Mindfullness-Of-Beathing.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-adults/Day6-OVERCOME-FEAR-OF-FAILURE.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-adults/Day7--Mindfulness-of-thoughts.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-adults/Day8-FORGIVENESS-MEDITATION.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-adults/Day9-ENHANCE-INNER-STRENGTH.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-adults/Day10-Visualisation-with-snorkelling.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-adults/Day11-BOOST-SELF-CONFIDENCE.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-adults/Day12-IMPROVING-SELF-DISCIPLINE.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-adults/Day13--Mindfulness-of-greed.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-adults/Day14-GRATITUDE-MEDITATION.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-adults/Day15-MINDFULLNESS-DISTRACTION.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-adults/Day16-Mindfulness-of-pain.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-adults/Day17-Overcoming-obstacles-and-mental-blocks.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-adults/Day18-REACH-YOUR-HIGHEST-POTENTIAL.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-adults/Day-19-UNLEASH-THE-WINNER-IN-YOU.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-adults/Day-20-Visualization-for-Inner-peace.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-adults/Day21-BOOST-WILL-POWER.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-adults/Day22-Higher-Advice.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-adults/Day23-PAIN-MANAGEMENT.mp3",
            "http://mentalhealthcure.com/v1/download/knowledge-hub-for-parents/Q_A.mp3"};

    public static String[] mindGymForPregnecyWomenAdioNames = {"1-Loving-Kindness", "2-Dealing-with-Painful-emotions", "3-Clearing-emotional-blocks-and-barriers",
            "4-INNER-ADVICE-MEDITATION", "5-Concentration-meditation", "6-JOYFULL-MEDITATION", "7-MINDFULLNESS-DISTRACTION", "8-FORGIVENESS-MEDITATION",
            "9-Visualisation-with-snorkelling", "10-Imaginary-Guide-to-support-you", "11-Mindfulness-of-breathing", "12-OVERCOME-FEAR-OF-FAILURE",
            "13-IMPROVING-SELF-DISCIPLINE", "14-Mindfulness-of-greed", "15-Progressive-Muscle-Relaxation", "16-GRATITUDE-MEDITATION", "17-Mindfulness-of-pain",
            "18-Mindfulness-of-thoughts", "19-PAIN-MANAGEMENT", "20-Rising-and-falling-meditation", "21-Visualization-for-Inner-peace"};

    public static String[] mindGymPregnecyAudioList = {"http://mentalhealthcure.com/v1/download/mindgym-for-want-to-be-moms/1-Loving-Kindness.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-want-to-be-moms/2-Dealing-with-Painful-emotions.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-want-to-be-moms/3-Clearing-emotional-blocks-and-barriers.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-want-to-be-moms/4-INNER-ADVICE-MEDITATION.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-want-to-be-moms/5-Concentration-meditation.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-want-to-be-moms/6-JOYFULL-MEDITATION.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-want-to-be-moms/7-MINDFULLNESS-DISTRACTION.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-want-to-be-moms/8-FORGIVENESS-MEDITATION.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-want-to-be-moms/9-Visualisation-with-snorkelling.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-want-to-be-moms/10-Imaginary-Guide-to-support-you.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-want-to-be-moms/11-Mindfulness-of-breathing.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-want-to-be-moms/12-OVERCOME-FEAR-OF-FAILURE.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-want-to-be-moms/13-IMPROVING-SELF-DISCIPLINE.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-want-to-be-moms/14-Mindfulness-of-greed.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-want-to-be-moms/15-Progressive-Muscle-Relaxation.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-want-to-be-moms/16-GRATITUDE-MEDITATION.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-want-to-be-moms/17-Mindfulness-of-pain.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-want-to-be-moms/18-Mindfulness-of-thoughts.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-want-to-be-moms/19-PAIN-MANAGEMENT.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-want-to-be-moms/20-Rising-and-falling-meditation.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-want-to-be-moms/21-Visualization-for-Inner-peace.mp3"};


    public static String[] mindGymForStudentsAudioNames = {"Day1-BOOST-SELF-CONFIDENCE", "Day2--REACH-YOUR-HIGHEST-POTENTIAL", "Day3-UNLEASH-THE-WINNER-IN-YOU",
            "Day4-ENHANCE-INNER-STRENGTH", "Day5-OVERCOME-FEAR-AND-GET-CONFIDENT", "Day6-ENHANCE-MEMORY", "Day7-OVERCOME-FEAR-OF-FAILURE", "Day8-BOOST-WILL-POWER",
            "Day9-OVERCOME-PROCASTINATION", "Day-10--Visualise-success-in-Exam", "Day11-Overcome-DISTRACTION", "Day11-Overcome-DISTRACTION", "Day13-Progressive-Muscle-relaxation",
            "Day14-Visualisation-with-snorkelling"};

    public static String[] mindGymForStudentsAudioList = {"http://mentalhealthcure.com/v1/download/mindgym-for-students/Day1-BOOST-SELF-CONFIDENCE.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-students/Day2--REACH-YOUR-HIGHEST-POTENTIAL.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-students/Day3-UNLEASH-THE-WINNER-IN-YOU.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-students/Day4-ENHANCE-INNER-STRENGTH.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-students/Day5-OVERCOME-FEAR-AND-GET-CONFIDENT.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-students/Day6-ENHANCE-MEMORY.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-students/Day7-OVERCOME-FEAR-OF-FAILURE.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-students/Day8-BOOST-WILL-POWER.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-students/Day9-OVERCOME-PROCASTINATION.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-students/Day-10--Visualise-success-in-Exam.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-students/Day11-Overcome-DISTRACTION.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-students/Day11-Overcome-DISTRACTION.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-students/Day13-Progressive-Muscle-relaxation.mp3",
            "http://mentalhealthcure.com/v1/download/mindgym-for-students/Day14-Visualisation-with-snorkelling.mp3"};
    private MyPhoneStateListener phoneStateListener = null;
    private TelephonyManager telephony;
    private GetDownloadStatus getDownloadStatus;
    public DownloadMindGymAudioFilesService(String name) {
        super(name);
    }
    public DownloadMindGymAudioFilesService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Log.i("Mindfulness","In on Handle of Downloading service"+ ProfileScreen.downloadMindGymAudioFileCount);
        editor = getSharedPreferences("HAPPY_BEING", MODE_PRIVATE).edit();
        fileDirPath = getFilesDir().getAbsolutePath() + File.separator + "HappyBeing";
        SharedPreferences sharedPreferences = getSharedPreferences("HAPPY_BEING", MODE_PRIVATE);
        contentType = sharedPreferences.getString(AppConstants.ROLE, "");

        if (contentType.equals("Student")) {
            downloadFile(mindGymForStudentsAudioNames[0], mindGymForStudentsAudioList[0]);
        }
        else if (contentType.contains("Expecting_Mom") || contentType.contains("Want_to_be_mom")) {
            downloadFile(mindGymForPregnecyWomenAdioNames[0], mindGymPregnecyAudioList[0]);
        }
        else {
            downloadFile(mindGymForAdultsAudioListNames[0], mindGymAdultAudioFiles[0]);
        }
        if (phoneStateListener == null) {
            phoneStateListener = new MyPhoneStateListener(getApplicationContext());
            telephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            telephony.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }

    }

    private void downloadFile(String name , String audioFile) {
        final DownloadTask downloadTask = new DownloadTask(getApplicationContext(), name+".mp3");
        downloadTask.execute(audioFile);
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
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
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
                //Log.i(TAG, "File downloaded"+fileName);
                editor.putBoolean("downloadfile"+fileName, true);
                editor.commit();
                ProfileScreen.downloadMindGymAudioFileCount++;
                if (contentType.equals("Student")) {
                    if (ProfileScreen.downloadMindGymAudioFileCount < mindGymForStudentsAudioList.length) {
                        downloadFile(mindGymForStudentsAudioNames[ProfileScreen.downloadMindGymAudioFileCount], mindGymForStudentsAudioList[ProfileScreen.downloadMindGymAudioFileCount]);
                    }
                }
                else if (contentType.contains("Expecting_Mom") || contentType.contains("Want_to_be_mom")) {
                    if (ProfileScreen.downloadMindGymAudioFileCount < mindGymPregnecyAudioList.length) {
                        downloadFile(mindGymForPregnecyWomenAdioNames[ProfileScreen.downloadMindGymAudioFileCount], mindGymPregnecyAudioList[ProfileScreen.downloadMindGymAudioFileCount]);
                    }
                }
                else {
                    if (ProfileScreen.downloadMindGymAudioFileCount < mindGymAdultAudioFiles.length) {
                        downloadFile(mindGymForAdultsAudioListNames[ProfileScreen.downloadMindGymAudioFileCount], mindGymAdultAudioFiles[ProfileScreen.downloadMindGymAudioFileCount]);
                    }
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
            getDownloadStatus = (GetDownloadStatus) (Activity)context;
            this.fileName = fileName;
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Downloading");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(true);

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
                //Log.i("Mindfulness", "Download error: " + result);
            }
            else {
                //TODO: make file downloaded true and download another file
                //Log.i("Mindfulness", "File downloaded "+fileName);
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

