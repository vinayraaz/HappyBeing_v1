package com.azuyo.happybeing.ServerAPIConnectors;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.azuyo.happybeing.Utils.AppConstants;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class ServerHandler {
    private ServerHandlerListener listener;
    private String url_string;
    private String request_method;
    private JSONObject api_params;

    private String filepath;
    private String fileKey;
    private long request_code;

    ServerHandler(String url_string, String request_method, JSONObject api_params, ServerHandlerListener listener) {
        this.url_string = url_string;
        this.request_method = request_method;
        this.api_params = api_params;
        this.listener = listener;
    }

    ServerHandler(String url_string, ServerHandlerListener listener) {
        this.url_string = url_string;
        this.request_method = "GET";
        this.api_params = null;
        this.listener = listener;
    }

    ServerHandler(String url_string, JSONObject api_params, String filename, String fileKey, ServerHandlerListener listener) {
        this.url_string = url_string;
        this.request_method = "POST";
        this.api_params = api_params;
        this.filepath = filename;
        this.fileKey = fileKey;
        this.listener = listener;
    }

    public void call(long request_code) {
        this.request_code = request_code;

        if (filepath != null) {
           // Log.i("ServerHandler","File path is not null calling file uploader");
            FileUploader uploader = new FileUploader();
            uploader.execute();
        } else {
            ServerCaller caller = new ServerCaller();
            caller.execute();
        }
    }

    private class ServerCaller extends AsyncTask<Void, Void, Bundle> {
        HttpURLConnection connection;

        @Override
        protected Bundle doInBackground(Void... params) {
            try {
                URL request_url = new URL(url_string);
                //Log.i("Tag", "URLS:" + url_string);
                connection = (HttpURLConnection) request_url.openConnection();
                connection.setDoInput(true);

                if (request_method.equalsIgnoreCase("Post") || request_method.equalsIgnoreCase("PUT")) {
                    connection.setDoOutput(true);
                } else {
                    connection.setDoOutput(false);
                }
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6ImRlZmF1bHQiLCJpYXQiOjE0ODk5MzA5NDZ9.x3MobPwdUIUx_JrNCu7bG2aMFz4oaMAWo4VbUmy6FDY");
                connection.setRequestMethod(request_method);
                connection.connect();
                if (api_params != null) {
                    OutputStreamWriter oStream = new OutputStreamWriter(connection.getOutputStream());
                    oStream.write(api_params.toString());
                    oStream.flush();
                    oStream.close();
                }
                int responsecode = connection.getResponseCode();
                Bundle bundle = new Bundle();
                //Log.i("ServerHandler","Api response code"+responsecode);
                if (responsecode == 200 || responsecode == 204) {
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    String resultString = result.toString();
                    connection.disconnect();
                    bundle.putInt("responseCode", responsecode);
                    bundle.putString("result", resultString);
                    //Log.i("ServerHandler" , "result is "+resultString);
                    return bundle;
                } else {
                    bundle.putInt("responseCode", responsecode);
                    return bundle;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            super.onPostExecute(bundle);
            if (bundle != null) {
                if (bundle.containsKey("result")) {
                    String response = bundle.getString("result");
                    String error = JSONParser.getErrorString(response);
                    if (error == null) {
                        ServerHandler.this.listener.onSuccess(request_code, response);
                    } else {
                        ServerHandler.this.listener.onError(request_code, error);
                    }
                } else if (bundle.containsKey("responseCode")) {
                    int errorcode = bundle.getInt("responseCode");
                    ServerHandler.this.listener.onFailure(request_code, Integer.toString(errorcode));
                }
            } else {
                ServerHandler.this.listener.onFailure(request_code, APIAdapter.CONNECTION_FAILED);
            }
        }
    }

    private class FileUploader extends AsyncTask<Void, Void, Bundle> {
        HttpURLConnection connection;

        String crlf = "\r\n";
        String twoHyphens = "--";
        private Random random	= new Random();
        protected String randomString( )
        {
            return Long.toString( random.nextLong(), 36);
        }
        String boundary	= "---------------------------" + randomString() + randomString() + randomString();

        @Override
        protected Bundle doInBackground(Void... params) {
            try {
                URL request_url = new URL(url_string);
                connection = (HttpURLConnection) request_url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);

                connection.setRequestMethod("POST");
                connection.setRequestProperty( "Content-Type", "multipart/form-data; boundary=" + boundary);
                connection.setRequestProperty("Accept-Encoding", "");
                connection.setRequestProperty("Connection", "Keep-Alive");
                //Log.i("ServerHandler","Calling connect connect ");
                connection.connect();
                DataOutputStream oStream = new DataOutputStream(connection.getOutputStream());
                if (api_params != null) {
                    Iterator<String> keys = api_params.keys();
                    while(keys.hasNext()) {
                        String key = keys.next();
                        write_parameter(oStream, key, (String) api_params.get(key));
                    }
                }


                if  (filepath != null) {
                    write_fileParams(oStream, filepath, fileKey);
                    InputStream is = new FileInputStream(filepath);
                    pipe(is, oStream);
                }

                oStream.flush();
                oStream.close();
                //Log.i("ServerHandler","response form handler");
                int responsecode = connection.getResponseCode();
                Bundle bundle = new Bundle();
                if (responsecode == 200 || responsecode == 204) {
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    String resultString = result.toString();
                   // Log.i("Tag", "result :" + resultString);
                    connection.disconnect();
                    bundle.putInt("responseCode", responsecode);
                    bundle.putString("result", resultString);
                    return bundle;
                } else {
                    bundle.putInt("responseCode", responsecode);
                    return bundle;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            super.onPostExecute(bundle);
            if (bundle != null) {
                if (bundle.containsKey("result")) {
                    String response = bundle.getString("result");
                    String error = JSONParser.getErrorString(response);
                    if (error == null) {
                        ServerHandler.this.listener.onSuccess(request_code, response);
                    } else {
                        ServerHandler.this.listener.onError(request_code, error);
                    }
                } else if (bundle.containsKey("responseCode")) {
                    int errorcode = bundle.getInt("responseCode");
                    ServerHandler.this.listener.onFailure(request_code, Integer.toString(errorcode));
                }
            } else {
                ServerHandler.this.listener.onFailure(request_code, APIAdapter.CONNECTION_FAILED);
            }
        }

        private void write_parameter(DataOutputStream os, String key, String value) throws IOException {
            os.writeBytes(this.twoHyphens + this.boundary + this.crlf);
            os.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + this.crlf + this.crlf + value + this.crlf);
            os.writeBytes(this.crlf);
        }


        private void write_fileParams(DataOutputStream os, String filepath, String fileKey) throws IOException {
            /*write( "");
            writeln( type);*/
            os.writeBytes(this.twoHyphens + this.boundary + this.crlf);
            os.writeBytes("Content-Disposition: form-data; name=\"" + fileKey + "\";filename=\"" + filepath + "\"" + this.crlf);
            os.writeBytes(this.crlf);
            String type = URLConnection.guessContentTypeFromName(filepath);
            if ( type == null)
                type = "application/octet-stream";
            //Log.i("Tag","type"+type);
            String key = "Content-Type: ";
            write_parameter(os, key, type);
        }

        private void pipe(InputStream in, DataOutputStream out) throws IOException
        {
            byte [] buf = new byte[50000];
            int nread;
            // int navailable;
            int total = 0;
            synchronized (in)
            {
                while ( ( nread = in.read( buf, 0, buf.length)) >= 0)
                {
                    out.write( buf, 0, nread);
                    total += nread;
                }
            }
            out.flush();
            buf = null;
        }
    }
}