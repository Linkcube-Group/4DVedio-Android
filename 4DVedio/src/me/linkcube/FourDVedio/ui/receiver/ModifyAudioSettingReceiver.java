package me.linkcube.FourDVedio.ui.receiver;

import me.linkcube.FourDVedio.ui.listener.ModeControlListener;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.ActionBar;
import android.widget.Toast;

/**
 * 接受耳机插拔的广播
 * 
 * @author xinyang
 * 
 */
public class ModifyAudioSettingReceiver extends BroadcastReceiver {

	private ModeControlListener modifyAudioSettingListener;

	private boolean isAppRunInBackground = false;

	private boolean isOnVoiceMode = false;

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals("android.intent.action.HEADSET_PLUG")) {
			if (!isAppRunInBackground && isOnVoiceMode) {
				if (intent.hasExtra("state")) {
					if (intent.getIntExtra("state", 0) == 0) {

						modifyAudioSettingListener.onVoiceMode(2, false);
					} else if (intent.getIntExtra("state", 0) == 1) {

						modifyAudioSettingListener.onVoiceMode(2, true);
					}
				}
			}
		} else if (action.equals("me.linkcube.toyconnected")) {
			AudioManager audioManager = (AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE);
			boolean isHeadsetOn = audioManager.isWiredHeadsetOn();
			modifyAudioSettingListener.onVoiceMode(2, isHeadsetOn);
		}

	}

	public void setModifyAudioSettingListener(
			ModeControlListener modifyAudioSettingListener) {
		this.modifyAudioSettingListener = modifyAudioSettingListener;
	}

	public void setAppRunInBackground(boolean isAppRunInBackground) {
		this.isAppRunInBackground = isAppRunInBackground;
	}

	public void setOnVoiceMode(boolean isOnVoiceMode) {
		this.isOnVoiceMode = isOnVoiceMode;
	}

}
