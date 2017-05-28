package com.azuyo.happybeing.ServerAPIConnectors;

/**
 * Created by Bala on 6/30/2015.
 */
public interface API_Response_Listener<T> {
    public void onComplete(T data, long request_code, String failure_code);
}
