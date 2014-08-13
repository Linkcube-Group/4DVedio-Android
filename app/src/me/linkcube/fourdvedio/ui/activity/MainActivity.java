package me.linkcube.fourdvedio.ui.activity;

import me.linkcube.fourdvedio.FourDVedioApplication;
import me.linkcube.fourdvedio.R;
import me.linkcube.fourdvedio.core.voice.SensorProvider;
import me.linkcube.fourdvedio.core.voice.VoiceSensor;
import me.linkcube.fourdvedio.service.ToyServiceConnection;
import me.linkcube.fourdvedio.ui.BaseActivity;
import me.linkcube.fourdvedio.ui.bluetooth.BluetoothSettingActivity;
import me.linkcube.fourdvedio.ui.listener.ModeControlListener;
import me.linkcube.fourdvedio.ui.listener.OnSingleStatusBarClickListener;
import me.linkcube.fourdvedio.utils.AlertUtils;
import me.linkcube.fourdvedio.utils.Timber;
import me.linkcube.fourdvedio.widget.SingleStatusBarView;
import me.linkcube.fourdvedio.widget.VoiceModeView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;

public class MainActivity extends BaseActivity implements
		OnSingleStatusBarClickListener, ModeControlListener {

	private ToyServiceConnection toyServiceConnection;

	private SingleStatusBarView statusBarView;
	
	private VoiceModeView voiceModeView;

	private VoiceSensor mVoiceSensor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		bindToyService();
	}

	private void bindToyService() {
		toyServiceConnection = new ToyServiceConnection();
		Intent toyintent = new Intent(this,
				me.linkcube.fourdvedio.service.ToyService.class);
		this.startService(toyintent);
		bindService(toyintent, toyServiceConnection, Context.BIND_AUTO_CREATE);

		statusBarView = (SingleStatusBarView) findViewById(R.id.status_bar);
		statusBarView.setOnSingleStatusBarClickListener(this);
		voiceModeView=(VoiceModeView)findViewById(R.id.voice_mode_view);
		voiceModeView.setOnModeControlListener(this);
		mVoiceSensor = new VoiceSensor();
	}
	
	

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
	public void onVoiceMode(int level) {
		Timber.d("开启音浪模式--注册声音传感器");
		registerVoiceSensor();
		mVoiceSensor.setVoiceLevel(level);
	}

	@Override
	public void offVoiceMode(int level) {
		Timber.d("关闭音浪模式--注销声音传感器");
		if (mVoiceSensor != null) {
			mVoiceSensor.unregisterVoiceListener();
		}
	}

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
//		new AlertDialog.Builder(mActivity)
//		.setMessage("最小化连酷APP，打开音乐或视频播放器，玩具将随着音浪High起来！")
//		.setTitle("提示")
//		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				Intent intent = new Intent(Intent.ACTION_MAIN);
//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
//				intent.addCategory(Intent.CATEGORY_HOME);
//				mActivity.startActivity(intent);"连酷 Linkcube");
//			}
//		})
//		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//
//			}
//		}).show();
	}

}
