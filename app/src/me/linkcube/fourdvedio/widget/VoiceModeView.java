package me.linkcube.fourdvedio.widget;

import me.linkcube.fourdvedio.R;
import me.linkcube.fourdvedio.core.bluetooth.DeviceConnectionManager;
import me.linkcube.fourdvedio.ui.listener.ModeControlListener;
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
	
	private boolean isRegisterReceiver=false;

	public VoiceModeView(Context context) {
		super(context);
		this.context=context;
		init();
	}

	public VoiceModeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
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
			try {
				if (!DeviceConnectionManager.getInstance().isConnected()) {
					//mListener.showConnectBluetoothTip();
					Toast.makeText(context, "连接连酷玩具感受更棒的4D效果！", Toast.LENGTH_SHORT).show();
					if(!isRegisterReceiver){
						mListener.registerModifyAudioSettingReceiver();
					}
					isRegisterReceiver=true;
					//return;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			
			AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
			boolean isHeadsetOn = audioManager.isWiredHeadsetOn();
			switch (level) {
			case 0:
				level = 2;
				mListener.showOpenMusicPlayerDialog();
				modeBtn.setBackgroundResource(R.drawable.voice_mode_4);
				mListener.onVoiceMode(level,isHeadsetOn);
				break;
			case 2:
				level = 0;
				modeBtn.setBackgroundResource(R.drawable.voice_mode_0);
				mListener.offVoiceMode(level,isHeadsetOn);
				break;
			default:
				break;
			}

		}
	};


	private Handler resetViewHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			level = 0;
			modeBtn.setBackgroundResource(R.drawable.voice_mode_0);
		}

	};

}