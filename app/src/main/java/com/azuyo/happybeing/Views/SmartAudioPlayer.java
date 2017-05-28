package com.azuyo.happybeing.Views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.Utils.MediaPlayerInterface;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class SmartAudioPlayer extends LinearLayout implements OnPreparedListener, OnCompletionListener, OnSeekBarChangeListener, OnClickListener {
	
	private SeekBar seekbar;
	private ToggleButton playButton;
	private TextView timeLeftView;

	private MediaPlayer mediaPlayer;
	private double duration, timeRemaining;
	private boolean isPlaying = false;
	private boolean isPause = false;
	private Uri sourceUri;
	private String sourceUrl;
	private Handler durationHandler = new Handler();
	private ProgressDialog waitDialog;
	private boolean isPreparing = false;
	private MediaPlayerInterface mediaPlayerInterface;
	private Context context;
	private boolean replay = false;
	public SmartAudioPlayer(Context context) {
		super(context);
        this.context = context;
		init();
	}

	public SmartAudioPlayer(Context context, AttributeSet attrs) {
		super(context, attrs);
        this.context = context;
		init();
	}

	public void setPlayButtonVisible(boolean visible) {
		if (!visible) {
			playButton.setVisibility(GONE);
		}
		else {
			playButton.setVisibility(VISIBLE);
		}
	}

	public void setEnabled(boolean enabled) {
		if (!enabled) {
			finish();
		}
		playButton.setEnabled(enabled);
	}

	private void init() {
		Log.i("SmartAudioPlayer","In init method");
		inflate(getContext(), R.layout.smart_player, this);
		seekbar = (SeekBar) findViewById(R.id.player_seekbar);
		playButton = (ToggleButton) findViewById(R.id.player_play);
		timeLeftView = (TextView) findViewById(R.id.player_timeleft);

		playButton.setEnabled(false); //Enable only when datasource is attached
		//setButtonState(false);
		setRemainingTime(0);

		playButton.setOnClickListener(this);
		seekbar.setOnSeekBarChangeListener(this);

		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setOnCompletionListener(this);
	}

	private void setRemainingTime(double timeRemaining) {
		timeLeftView.setText(String.format("%02d:%02d sec", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));
	}
	
	private void updateProgress() {
		int timeElapsed = mediaPlayer.getCurrentPosition();
       // Log.i("TAG", "Time Elapsed " + timeElapsed);
        timeRemaining = duration - timeElapsed;
        
        if(timeRemaining > 0){
        	setRemainingTime(timeRemaining);
        	seekbar.setProgress((int) timeElapsed);
        } else {
        	seekbar.setProgress(0);
        }
	}

	//TODO: Do Toast on error
	public void setDataSource(Uri uri, Activity activity) {
		mediaPlayerInterface = (MediaPlayerInterface) activity;
		try {
			sourceUri = uri;
			mediaPlayer.reset();
			mediaPlayer.setDataSource(context, uri);
			isPreparing=true;
			mediaPlayer.prepareAsync();
			mediaPlayer.setOnPreparedListener(this);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
			mediaPlayer.reset();
		}
	}
	public void setDataSource(Uri uri) {
		try {
			sourceUri = uri;
			mediaPlayer.reset();
			mediaPlayer.setDataSource(context, uri);
			isPreparing=true;
			mediaPlayer.prepareAsync();
			mediaPlayer.setOnPreparedListener(this);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
			mediaPlayer.reset();
		}
	}

	//TODO: Do Toast on error
	public void setDataSource(String url, Activity activity) {
		mediaPlayerInterface = (MediaPlayerInterface) activity;
		int currentVersion = Build.VERSION.SDK_INT;
		if (currentVersion < Build.VERSION_CODES.HONEYCOMB) {
			url = url.replace("https://", "http://");
		}
		try {
			sourceUrl = url;
			mediaPlayer.reset();
			mediaPlayer.setDataSource(url);
			isPreparing = true;
			mediaPlayer.prepareAsync();
			mediaPlayer.setOnPreparedListener(this);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		Log.i("SmartAudioPlayer", "Media Preparation Done");
		if(waitDialog!=null){
			waitDialog.dismiss();
		}
		isPreparing = false;
		playButton.setEnabled(true);
		Log.i("SmartAudioPlayer", "Is playing in onprepared is "+isPlaying);
		if(!isPlaying) {
			start();
		}
		duration = mp.getDuration();
		seekbar.setMax((int)duration);
	}
	
	public void start() {
		//setButtonState(true);
        Log.i("SmartAudioPlayer","In On Playing "+isPreparing);
		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.start();
		isPlaying = true;
		isPause = false;
		durationHandler.postDelayed(updateSeekBarTime, 100);
	}

	public void pause() {
		//setButtonState(false);
		isPause = true;
		mediaPlayer.pause();
		durationHandler.removeCallbacks(updateSeekBarTime);
	}
	
	public void release() {
		if (mediaPlayer != null) {
			pause();
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
			isPause = false;
		}
	}

	public void finish() {
		mediaPlayer.stop();
		playButton.setChecked(false);
		Log.i("SmartAudioPlayer","Time remaining ********* is "+timeRemaining);
		setRemainingTime(0);
		durationHandler.removeCallbacks(updateSeekBarTime);
		isPlaying = false;
		isPause = false;
		seekbar.setProgress(0);
	}
	
	@Override
	public void onCompletion(MediaPlayer arg0) {
		Log.i("SmartAudioPlayer","In on completion");
		finish();
		if (mediaPlayerInterface != null) {
			mediaPlayerInterface.setStatusOfPlayButton(false);
		}
		/* Android MediaPlayer doesn't work as per documentation */
		/*if (sourceUrl != null) {
			setDataSource(sourceUrl);
		} else if (sourceUri != null) {
			setDataSource(sourceUri);
		}*/
	}
	
	private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
        	if(mediaPlayer!=null){
        		updateProgress();
        		durationHandler.postDelayed(this, 100);
        	}
        }
    };

//	@Override
//	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//		Log.i("Tag", "Play button clicked: state " + isChecked);
//		if (isChecked) {
//			start();
//		} else {
//			pause();
//		}
//	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		try {
			 if (mediaPlayer.isPlaying() || mediaPlayer != null) {
			 if (fromUser)
				 mediaPlayer.seekTo(progress);
			 } else if (mediaPlayer == null) {
				 Toast.makeText(context, "Media is not running", Toast.LENGTH_SHORT).show();
				 seekBar.setProgress(0);
			 }
		 } catch (Exception e) {
			 Log.e("seek bar", "" + e);
			 seekBar.setEnabled(false);
		 }
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		seekbar.setSecondaryProgress(seekbar.getProgress());
	}

	@Override
	public void onClick(View v) {
		Log.i("SmartAudioPlayer", "In on click IsPlaying " + isPlaying+" is preparing "+isPreparing);
		if (!isPlaying) {
			if(isPreparing){
				waitDialog = new ProgressDialog(context);
				waitDialog.setTitle("Buffering Audio");
				waitDialog.setMessage("Please Wait..");
				waitDialog.setCancelable(true);
				waitDialog.show();
			} else {
				start();
			}
			isPlaying = true;
			isPause = false;
		} else {
			pause();
			isPlaying = false;
		}
	}

    public void setPlayButton(boolean play) {
        Log.i("ViewDetails","play is smart Audio player is "+play);
        if(play) {
			if(isPreparing){
				waitDialog=new ProgressDialog(context);
				waitDialog.setTitle("Buffering Audio");
				waitDialog.setMessage("Please Wait..");
				waitDialog.setCancelable(true);
				waitDialog.show();
			}else {
				start();
			}
			isPlaying = true;
			playButton.setChecked(true);
		}
        else {
            playButton.setChecked(false);
            isPlaying = false;
            pause();
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

	public boolean isPause() {
		return isPause;
	}


	public void setReplay(boolean replay) {
		this.replay = replay;
	}

	public void setAudioStreamType(int streamMusic) {
		mediaPlayer.setAudioStreamType(streamMusic);
	}
}
