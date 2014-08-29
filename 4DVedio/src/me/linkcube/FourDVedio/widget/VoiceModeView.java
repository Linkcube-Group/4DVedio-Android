package me.linkcube.FourDVedio.widget;

import me.linkcube.FourDVedio.R;
import me.linkcube.FourDVedio.core.bluetooth.DeviceConnectionManager;
import me.linkcube.FourDVedio.ui.listener.ModeControlListener;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class VoiceModeView extends RelativeLayout {

	private Context context;

	private Button modeBtn;

	private int level = 0;

	private ModeControlListener mListener;

	private boolean isRegisterReceiver = false;

	public VoiceModeView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public VoiceModeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	private void init() {
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = mInflater.inflate(R.layout.voice_mode_view, this, true);
		modeBtn = (Button) view.findViewById(R.id.voice_mode_btn);
		modeBtn.setOnClickListener(onClickListener);
	}

	public void setOnModeControlListener(ModeControlListener listener) {
		mListener = listener;
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			//updateView();
			mListener.openBlutToothSetting();
		}
	};

	private void updateView() {
		try {
			if (!DeviceConnectionManager.getInstance().isConnected()) {
				// mListener.showConnectBluetoothTip();
				if (!isRegisterReceiver) {
					mListener.registerModifyAudioSettingReceiver();
				}
				isRegisterReceiver = true;
				// return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		boolean isHeadsetOn = audioManager.isWiredHeadsetOn();
		switch (level) {
		case 0:
			level = 2;
			mListener.showOpenMusicPlayerDialog();
			modeBtn.setBackgroundResource(imageResources[1]);
			mListener.onVoiceMode(level, isHeadsetOn);
			break;
		case 2:
			level = 0;
			modeBtn.setBackgroundResource(imageResources[0]);
			mListener.offVoiceMode(level, isHeadsetOn);
			break;
		default:
			break;
		}

	}

	private Handler resetViewHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			level = 0;
			modeBtn.setBackgroundResource(imageResources[0]);
		}

	};

	private int[] imageResources = { R.drawable.mobile_shake_normal, R.drawable.mobile_shake_pressed };

	public void changeViewBg(boolean isMobileMode) {
		if (isMobileMode) {
			imageResources[0] = R.drawable.mobile_shake_normal;
			imageResources[1] = R.drawable.mobile_shake_pressed;
		} else {
			imageResources[0] = R.drawable.toy_shake_normal;
			imageResources[1] = R.drawable.toy_shake_pressed;
		}
		if (level == 0)
			level = 2;
		else {
			level = 0;
		}
		//updateView();
	}

	public void resetView() {
		level = 0;
		modeBtn.setBackgroundResource(imageResources[0]);
	}
	
	public void changeViewBg(){
		modeBtn.setBackgroundResource(R.drawable.toy_shake_pressed);
	}

}