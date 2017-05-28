package com.azuyo.happybeing.events;

import android.content.Context;
import android.os.HandlerThread;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.azuyo.happybeing.ui.ProfileScreen;


public class MyPhoneStateListener extends PhoneStateListener {


	public MyPhoneStateListener(Context context) {
        HandlerThread thread = new HandlerThread("PhoneEventsHandler");
        thread.start();
    }
	
	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		super.onCallStateChanged(state, incomingNumber);
		switch(state) {
		case TelephonyManager.CALL_STATE_RINGING:
            //Log.i("MyPhoneStateListener","In call state ringing "+incomingNumber);
			if (ProfileScreen.mediaPlayer != null) {
			}
    		break;
		
		case TelephonyManager.CALL_STATE_OFFHOOK:
            //Log.i("MyPhoneStateListener","In call state offhook"+incomingNumber);
            String numberToSend = "";
            break;
	
		case TelephonyManager.CALL_STATE_IDLE:
            //Log.i("MyPHone","IS call state idle");
			break;
		}
	}
}