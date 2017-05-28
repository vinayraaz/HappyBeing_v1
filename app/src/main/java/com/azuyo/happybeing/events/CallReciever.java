package com.azuyo.happybeing.events;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.ui.ProfileScreen;

public class CallReciever extends BroadcastReceiver {
	@Override
	public void onReceive(final Context context, Intent intent) {
        //Log.i("CallReceiver","In on receive");
		// TODO Handle on call, another call dial
       // Log.i("CallReceiver","In on call receiver method");
        if (ProfileScreen.mediaPlayer != null) {
            if (ProfileScreen.mediaPlayer.isPlaying()) {
                ProfileScreen.mediaPlayer.pause();
                if (ProfileScreen.menuItem != null) {
                    ProfileScreen.menuItem.setTitle("Play");
                    ProfileScreen.menuItem.setIcon(context.getDrawable(R.drawable.play_button));
                }
            }
        }
	}
}