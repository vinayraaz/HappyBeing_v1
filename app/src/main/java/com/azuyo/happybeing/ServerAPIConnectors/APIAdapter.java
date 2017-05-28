package com.azuyo.happybeing.ServerAPIConnectors;

import org.json.JSONObject;

public abstract class APIAdapter<T, L> {

    public static int CHILD_MAIL = 10;
    public static int PARENT_MAIL = 11;

    public static String CONNECTION_FAILED = "1000";
    public static int INVALID_RESPONSE = 1001;
    public static String NO_FAILURE = "-1";

    public static int NO_FILE_TO_LOAD = 1002;

    private T input_data;
    private long requestCode;

    private String filepath;
    private String fileKey;
    private API_Response_Listener<L> listener;

    APIAdapter(T input_data, long requestCode, final API_Response_Listener<L> listener) {
        this.input_data = input_data;
        this.requestCode = requestCode;
        this.listener = listener;
    }

    APIAdapter(T input_data, long requestCode, String filename, String fileKey, final API_Response_Listener<L> listener) {
        this.input_data = input_data;
        this.filepath = filename;
        this.requestCode = requestCode;
        this.listener = listener;
        this.fileKey = fileKey;
    }

    public void call() {

        String url = getURL();
        String http_method = getHttpMethod();

        JSONObject params = convertDataToJSON(input_data);
        //Log.d("APIAdapter_input" , input_data.toString());
        ServerHandlerListener serverListener = new ServerHandlerListener() {
            @Override
            public void onSuccess(long requestCode, String result) {
                postProcess();
                L result_data = convertJSONToData(result);
                listener.onComplete(result_data, requestCode, NO_FAILURE);
            }

            @Override
            public void onError(long requestCode, String error) {
                postProcess();
                String failure_code = intrepret_error(error);
                listener.onComplete(null, requestCode, failure_code);
            }

            @Override
            public void onFailure(long requestCode, String errorCode) {
                postProcess();
                listener.onComplete(null, requestCode, CONNECTION_FAILED);
            }
        };

        ServerHandler server;

        if (filepath != null) {
            server = new ServerHandler(url, params, filepath, fileKey, serverListener);
        } else {
            server = new ServerHandler(url, http_method, params, serverListener);
        }

        preProcess();
        server.call(requestCode);
    }

    public abstract String getURL();

    public String getHttpMethod() {
        return "GET";
    }

    public JSONObject convertDataToJSON(T data) {
        return null;
    }

    public L convertJSONToData(String response) {
        return null;
    }

    public String intrepret_error(String error) {
        return NO_FAILURE;
    }

    public void preProcess() {
        /* DO nothing */
    }

    public void postProcess() {
        /* DO Nothing */
    }
}
