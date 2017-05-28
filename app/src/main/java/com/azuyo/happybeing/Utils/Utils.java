package com.azuyo.happybeing.Utils;

import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

public class Utils {

    public static String[] split(String original, String separator) {
        if (!check(original))
            return null;

        ArrayList<String> nodes = new ArrayList<String>();

        // Parse nodes into vector
        int index = original.indexOf(separator);
        while (index >= 0) {
            String str = original.substring(0, index);
            if (str != null) {
                str = str.trim();
                if (str.length() > 0) {
                    nodes.add(str);
                }
            }

            original = original.substring(index + separator.length());
            index = original.indexOf(separator);
        }

        // Get the last node
        if (original != null) {
            original = original.trim();
            if (original.length() > 0) {
                nodes.add(original);
            }
        }

        // Create splitted string array
        String[] result = new String[nodes.size()];
        if (nodes.size() > 0) {
            for (int loop = 0; loop < nodes.size(); loop++) {
                result[loop] = (String) nodes.get(loop);
            }
        }

        nodes.clear();
        //nodes = null;
        return result;
    }

    /*
     * To convert the InputStream to String we use the BufferedReader.readLine()
     * method. We iterate until the BufferedReader return null which means
     * there's no more data to read. Each line will appended to a StringBuilder
     * and returned as String.
     */
    public static String convertStreamToString(InputStream is) {
        try {
            if (is != null) {
                StringBuilder sb = new StringBuilder();
                String line;

                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                } finally {
                    is.close();
                }

                return sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static boolean check(String str) {
        return !(str == null || str.length() <= 0);

    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        }
        catch (SecurityException se) {
           // CommonUtils.displayDialog(context);
        }
        finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static long getFileSize(Uri uri, Context context) {
        long size = 0;
        String path = getPath(uri, context);
        if (path != null) {
            File f = new File(path);
            size = f.length();
        }
        if (size < 0) {
            size = 0;
        }
        return size;
    }

    public static String getPath(final Uri uri, final Context context) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static boolean hasPermission(Context context, String permission) {

        int res = context.checkCallingOrSelfPermission(permission);

        Log.v("TAG", "permission: " + permission + " = \t\t" +
                (res == PackageManager.PERMISSION_GRANTED ? "GRANTED" : "DENIED"));

        return res == PackageManager.PERMISSION_GRANTED;

    }

    /** Determines if the context calling has the required permissions
     * @param context - the IPC context
     * @param permissions - The permissions to check
     * @return true if the IPC has the granted permission
     */
    public static boolean hasPermissions(Context context, String... permissions) {

        boolean hasAllPermissions = true;

        for(String permission : permissions) {
            //return false instead of assigning, but with this you can log all permission values
            if (! hasPermission(context, permission)) {hasAllPermissions = false; }
        }

        return hasAllPermissions;

    }
}
