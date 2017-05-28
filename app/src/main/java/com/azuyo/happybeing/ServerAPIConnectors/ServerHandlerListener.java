package com.azuyo.happybeing.ServerAPIConnectors;

/**
 * Created by Admin on 23-06-2015.
 */
public interface ServerHandlerListener {

    public void onSuccess(long requestCode, String result);
    public void onError(long requestCode, String error); /* API returns an error */
    public void onFailure(long requestCode, String errorCode);  /* HTTP response failure */
}
