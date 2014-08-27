package me.linkcube.FourDVedio.ui.activity;

import me.linkcube.FourDVedio.FourDVedioApplication;
import me.linkcube.FourDVedio.R;
import me.linkcube.FourDVedio.core.bluetooth.DeviceConnectionManager;
import me.linkcube.FourDVedio.core.bluetooth.DeviceConnectionManager.CheckConnectionCallback;
import me.linkcube.FourDVedio.core.voice.AudioRecorder;
import me.linkcube.FourDVedio.core.voice.VoiceSensor;
import me.linkcube.FourDVedio.service.ToyServiceConnection;
import me.linkcube.FourDVedio.ui.BaseActivity;
import me.linkcube.FourDVedio.ui.bluetooth.BluetoothSettingActivity;
import me.linkcube.FourDVedio.ui.listener.ASmackRequestCallBack;
import me.linkcube.FourDVedio.ui.listener.ModeControlListener;
import me.linkcube.FourDVedio.ui.listener.OnSingleStatusBarClickListener;
import me.linkcube.FourDVedio.ui.receiver.ModifyAudioSettingReceiver;
import me.linkcube.FourDVedio.utils.AlertUtils;
import me.linkcube.FourDVedio.utils.Timber;
import me.linkcube.FourDVedio.widget.SingleStatusBarView;
import me.linkcube.FourDVedio.widget.VoiceModeView;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements
		OnSingleStatusBarClickListener, ModeControlListener {

	private ToyServiceConnection toyServiceConnection;

	private SingleStatusBarView statusBarView;

	private VoiceModeView voiceModeView;

	private VoiceSensor mVoiceSensor;

	private ModifyAudioSettingReceiver modifyAudioSettingReceiver;

	private TextView micSoundTv;

	private Vibrator vibrator = null;

	private Button connevtToyBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		bindToyService();

		initView();

		CheckDeviceConnect();
	}

	@Override
	protected void onResume() {
		modifyAudioSettingReceiver.setAppRunInBackground(false);
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	private void bindToyService() {
		toyServiceConnection = new ToyServiceConnection();
		Intent toyintent = new Intent(this,
				me.linkcube.FourDVedio.service.ToyService.class);
		this.startService(toyintent);
		bindService(toyintent, toyServiceConnection, Context.BIND_AUTO_CREATE);

	}

	private void initView() {
		statusBarView = (SingleStatusBarView) findViewById(R.id.status_bar);
		statusBarView.setOnSingleStatusBarClickListener(this);
		voiceModeView = (VoiceModeView) findViewById(R.id.voice_mode_view);
		voiceModeView.setOnModeControlListener(this);
		mVoiceSensor = new VoiceSensor();
		vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
		modifyAudioSettingReceiver = new ModifyAudioSettingReceiver();
		mVoiceSensor.setSoundListener(this);
		micSoundTv = (TextView) findViewById(R.id.mic_sound_tv);
		connevtToyBtn = (Button) findViewById(R.id.connect_indicator_btn);
		connevtToyBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showBluetoothSettingActivity();
			}
		});
	}

	private void CheckDeviceConnect() {
		DeviceConnectionManager.getInstance().setCheckConnectionCallBack(
				new CheckConnectionCallback() {

					@Override
					public void stable() {
						Log.d("CheckConnectionCallback", "stable");
					}

					@Override
					public void interrupted() {
						Log.d("CheckConnectionCallback", "interrupted");
						checkDeviceHandler.sendEmptyMessage(0);
					}

					@Override
					public void disconnect() {
						Log.d("CheckConnectionCallback", "disconnect");
					}
				});

	}

	private Handler checkDeviceHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			try {
				FourDVedioApplication.toyServiceCall.closeToy();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			offVoiceMode(2, true);
			voiceModeView.resetView();
			Toast.makeText(MainActivity.this,
					R.string.toast_toy_disconnect_try_again, Toast.LENGTH_SHORT)
					.show();
		}

	};

	@Override
	public void showBluetoothSettingActivity() {
		startActivity(new Intent(mActivity, BluetoothSettingActivity.class));
	}

	@Override
	public void resetToy() {
		try {
			FourDVedioApplication.toyServiceCall.closeToy();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void showMoreSettingActivity() {
		startActivity(new Intent(mActivity, BluetoothSettingActivity.class));
	}

	private void registerVoiceSensor() {
		Timber.d("注册声音传感器");
		mVoiceSensor.registerVoiceListener();
	}

	private void unregisterVoiceSensor() {
		Timber.d("注销声音传感器");
		if (mVoiceSensor != null) {
			mVoiceSensor.unregisterVoiceListener();
		}
	}

	@Override
	public void onVoiceMode(int level, boolean headsetOn) {

		mobileVibrator(0);
		if (headsetOn) {
			Log.d("onVoiceMode", "开启音浪模式--注册声音传感器");
			AudioRecorder.getInstance().stopAudioRecorder();
			registerVoiceSensor();
			mVoiceSensor.setVoiceLevel(level);
		} else {
			Log.d("onVoiceMode", "开启音浪模式--注册声音传感器---拔出耳机");
			if (mVoiceSensor != null) {
				mVoiceSensor.unregisterVoiceListener();
			}
			AudioRecorder.getInstance().startAudioRecorder(
					new ASmackRequestCallBack() {

						@Override
						public void responseSuccess(Object object) {
							Message msg = new Message();
							msg.what = (Integer) object;
							micHandler.sendMessage(msg);
						}

						@Override
						public void responseFailure(int reflag) {

						}
					});
		}
		modifyAudioSettingReceiver.setOnVoiceMode(true);
	}

	@Override
	public void offVoiceMode(int level, boolean headsetOn) {
		Log.d("offVoiceMode", "关闭音浪模式--注销声音传感器");
		vibrator.cancel();
		if (headsetOn) {
			if (mVoiceSensor != null) {
				mVoiceSensor.unregisterVoiceListener();
			}
		} else {
			try {
				FourDVedioApplication.toyServiceCall.setMicWave(0);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		AudioRecorder.getInstance().stopAudioRecorder();
		modifyAudioSettingReceiver.setOnVoiceMode(false);
	}

	private Handler micHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int micSound = msg.what;
			Log.d("micHandler", "micSound:" + micSound);
			micSoundTv.setText(micSound + "");
			mobileVibrator(micSound * 13);
			try {
				FourDVedioApplication.toyServiceCall.setMicWave(micSound);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	public void showConnectBluetoothTip() {
		AlertUtils
				.showToast(
						mActivity,
						getResources().getString(
								R.string.toast_toy_disconnect_pls_set));

	}

	@Override
	public void showOpenMusicPlayerDialog() {

		// new AlertDialog.Builder(mActivity)
		// .setMessage("最小化连酷APP，打开音乐或视频播放器，玩具将随着音浪High起来！")
		// .setTitle("提示")
		// .setPositiveButton("确定", new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// Intent intent = new Intent(Intent.ACTION_MAIN);
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
		// intent.addCategory(Intent.CATEGORY_HOME);
		// mActivity.startActivity(intent);"连酷 Linkcube");
		// }
		// })
		// .setNegativeButton("取消", new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		//
		// }
		// }).show();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(modifyAudioSettingReceiver);
		super.onDestroy();
	}

	@Override
	public void registerModifyAudioSettingReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.intent.action.HEADSET_PLUG");
		intentFilter.addAction("me.linkcube.toyconnected");
		registerReceiver(modifyAudioSettingReceiver, intentFilter);
		modifyAudioSettingReceiver.setModifyAudioSettingListener(this);
	}

	@Override
	public void mobileVibrator(int sound) {
		if (!DeviceConnectionManager.getInstance().isConnected()) {
			if (sound > 300)
				vibrator.vibrate(1000);
		} else {
			vibrator.cancel();
		}
	}

}
