package me.linkcube.FourDVedio.ui.activity;

import me.linkcube.FourDVedio.FourDVedioApplication;
import me.linkcube.FourDVedio.R;
import me.linkcube.FourDVedio.core.bluetooth.DeviceConnectionManager;
import me.linkcube.FourDVedio.core.bluetooth.DeviceConnectionManager.CheckConnectionCallback;
import me.linkcube.FourDVedio.core.update.DownloadNewApkHttpGet;
import me.linkcube.FourDVedio.core.update.DownloadNewApkHttpGet.AppUpdateCallback;
import me.linkcube.FourDVedio.core.update.UpdateHttpGet;
import me.linkcube.FourDVedio.core.update.UpdateManager;
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
import me.linkcube.FourDVedio.utils.Const;
import me.linkcube.FourDVedio.utils.PreferenceUtils;
import me.linkcube.FourDVedio.utils.Timber;
import me.linkcube.FourDVedio.widget.SingleStatusBarView;
import me.linkcube.FourDVedio.widget.VoiceModeView;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.Vibrator;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements OnSingleStatusBarClickListener, ModeControlListener {

	private ToyServiceConnection toyServiceConnection;

	private SingleStatusBarView statusBarView;

	private VoiceModeView voiceModeView;

	private VoiceSensor mVoiceSensor;

	private ModifyAudioSettingReceiver modifyAudioSettingReceiver;

	private Vibrator vibrator = null;
	
	private RelativeLayout main_relativelayout;
	// 手机震动
	private ImageButton mobileShake;
	// 玩具震动
	private ImageButton toyShake;
	private ImageView shakeMode;
	private ImageView shakemode_txt;
	private ImageView mobile_icon_iv;
	
	private ImageView banner_iv;
	
	private boolean receiverRegister=false;
	
	private int flieMaxSize = 0;

	private Intent msgIntent;
	private static PendingIntent msgPendingIntent;
	private int msgNotificationID = 1100;
	private Notification msgNotification;
	private NotificationManager msgNotificationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_main);

		bindToyService();

		initView();
		
		checkAppUpdate();

		CheckDeviceConnect();
	}

	@Override
	protected void onResume() {
		modifyAudioSettingReceiver.setAppRunInBackground(false);
		try {
			if (DeviceConnectionManager.getInstance().isConnected()) {
				voiceModeView.changeViewBg();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		onVoiceMode(2,false);

		super.onResume();
	}

	@Override
	protected void onStop() {
		// modifyAudioSettingReceiver.setAppRunInBackground(true);
		super.onStop();
	}

	private void bindToyService() {
		toyServiceConnection = new ToyServiceConnection();
		Intent toyintent = new Intent(this, me.linkcube.FourDVedio.service.ToyService.class);
		this.startService(toyintent);
		bindService(toyintent, toyServiceConnection, Context.BIND_AUTO_CREATE);

	}

	private void initView() {
		statusBarView = (SingleStatusBarView) findViewById(R.id.status_bar);
		statusBarView.setOnSingleStatusBarClickListener(this);
		voiceModeView = (VoiceModeView) findViewById(R.id.voice_mode_view);
		voiceModeView.setOnModeControlListener(this);
		mobileShake = (ImageButton) findViewById(R.id.mobileShake_bt);
		toyShake = (ImageButton) findViewById(R.id.toyShake_bt);
		// shakeMode=(ImageView)findViewById(R.id.shakemode);
		shakemode_txt = (ImageView) findViewById(R.id.shakemode_txt);
		mobileShake.setOnClickListener(myClickListener);
		
		main_relativelayout=(RelativeLayout)findViewById(R.id.main_relativelayout);

		mobile_icon_iv = (ImageView) findViewById(R.id.mobile_icon_iv);
		banner_iv=(ImageView)findViewById(R.id.banner_iv);
		banner_iv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Uri uri=Uri.parse("http://item.jd.com/1127528.html");
				Intent intent=new Intent(Intent.ACTION_VIEW,uri);
				startActivity(intent);
			}
		});

		toyShake.setOnClickListener(myClickListener);
		// 给shakeMode注册相应事件
		// to-do
		// 设置背景色,以示区分
		// mobileShake.setBackgroundColor(getResources().getColor(R.color.light_grey));

		mVoiceSensor = new VoiceSensor();
		vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
		modifyAudioSettingReceiver = new ModifyAudioSettingReceiver();
		mVoiceSensor.setSoundListener(this);

		// micSoundTv=(TextView)findViewById(R.id.mic_sound_tv);
	}

	OnClickListener myClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.mobileShake_bt:
				voiceModeView.changeViewBg(true);
				mobileShakeClick();
				break;
			case R.id.toyShake_bt:
				// if (DeviceConnectionManager.getInstance().isConnected()) {
				// voiceModeView.changeViewBg(false);
				// toyShakeClick();
				// } else {
				// Toast.makeText(MainActivity.this, "请连接连酷玩具感受更棒的4D效果！",
				// Toast.LENGTH_SHORT).show();
				// }
				voiceModeView.changeViewBg(false);
				toyShakeClick();
				break;

			default:
				break;
			}
		}
	};

	private void mobileShakeClick() {
		mobile_icon_iv.setVisibility(View.VISIBLE);
		voiceModeView.setVisibility(View.GONE);
		mobileShake.setBackground(getResources().getDrawable(R.drawable.mobileshake_grey));
		toyShake.setBackground(getResources().getDrawable(R.drawable.toyshake));
		shakemode_txt.setBackgroundResource(R.drawable.mobilemode_txt);
		main_relativelayout.setBackgroundResource(R.drawable.mobileshake_bg);
	}

	private void toyShakeClick() {
		mobile_icon_iv.setVisibility(View.GONE);
		voiceModeView.setVisibility(View.VISIBLE);
		mobileShake.setBackground(getResources().getDrawable(R.drawable.mobileshake));
		toyShake.setBackground(getResources().getDrawable(R.drawable.toyshake_grey));
		shakemode_txt.setBackgroundResource(R.drawable.toyshake_txt);
		main_relativelayout.setBackgroundResource(R.drawable.toyshake_bg);
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
			AudioRecorder.getInstance().startAudioRecorder(new ASmackRequestCallBack() {

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
			mobileVibrator(micSound * 13);
			if (DeviceConnectionManager.getInstance().isConnected()) {
				try {
					FourDVedioApplication.toyServiceCall.setMicWave(micSound);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	};

	@Override
	public void showConnectBluetoothTip() {
		AlertUtils.showToast(mActivity, getResources().getString(R.string.toast_toy_disconnect_pls_set));

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
		if(receiverRegister)
		unregisterReceiver(modifyAudioSettingReceiver);
		super.onDestroy();
	}

	@Override
	public void registerModifyAudioSettingReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.intent.action.HEADSET_PLUG");
		intentFilter.addAction("me.linkcube.toyconnected");
		registerReceiver(modifyAudioSettingReceiver, intentFilter);
		receiverRegister=true;
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

	private void CheckDeviceConnect() {
		DeviceConnectionManager.getInstance().setCheckConnectionCallBack(new CheckConnectionCallback() {

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
			Toast.makeText(MainActivity.this, R.string.toast_toy_disconnect_try_again, Toast.LENGTH_SHORT).show();
		}

	};

	@Override
	public void openBlutToothSetting() {
		startActivity(new Intent(mActivity, BluetoothSettingActivity.class));
	}
	
	
	/**
	 * 检测更新
	 */
	private void checkAppUpdate() {
		int addUpdateFlag = PreferenceUtils.getInt(
				Const.AppUpdate.APK_UPDATE_FLAG, -1);
		Log.d("MainActivity","--addUpdateFlag:" + addUpdateFlag);
		if (addUpdateFlag == -1) {
			PreferenceUtils.setInt(Const.AppUpdate.APK_UPDATE_FLAG, 0);
		}
		// 检测是否需要更新
		UpdateHttpGet updateHttpGet = new UpdateHttpGet(
				new ASmackRequestCallBack() {

					@Override
					public void responseSuccess(Object object) {
						String jsonString = (String) object;
						if (jsonString != null
								&& jsonString.startsWith("\ufeff")) {
							jsonString = jsonString.substring(1);
						}
						try {
							// XXX 常量字符串 和 在MainActivity的检测机制可能需要修改
							JSONObject jsonObject = new JSONObject(jsonString)
									.getJSONObject("updateInfo");
							PreferenceUtils.setString(
									Const.AppUpdate.APK_VERSION,
									jsonObject.getString("version"));
							boolean force = jsonObject.getBoolean("force");
							PreferenceUtils.setString(Const.AppUpdate.APK_SIZE,
									jsonObject.getString("size"));
							PreferenceUtils.setString(
									Const.AppUpdate.APK_DOWNLOAD_URL,
									jsonObject.getString("downloadURL"));
							PreferenceUtils.setString(
									Const.AppUpdate.APK_DESCRIPTION,
									jsonObject.getString("description"));
							UpdateManager.getInstance().setForcedUpdate(force);// force
							UpdateManager.getInstance().checkNeedUpdate(
									MainActivity.this,
									PreferenceUtils.getString(
											Const.AppUpdate.APK_VERSION, null));
						} catch (JSONException e) {
							e.printStackTrace();
						}

						int addUpdateFlag = PreferenceUtils.getInt(
								Const.AppUpdate.APK_UPDATE_FLAG, 0);
						Timber.d("addUpdateFlag:" + addUpdateFlag);

						if (UpdateManager.getInstance().isForcedUpdate()) {// UpdateManager.getInstance().isForcedUpdate()
							// 需要强制更新，取消则退出程序
						} else if (UpdateManager.getInstance().isUpdate()
								&& addUpdateFlag == 1) {
							showUpdateDialog();
						}
					}

					@Override
					public void responseFailure(int reflag) {

					}
				});

	}
	
	/**
	 * 更新Dialog
	 */
	private void showUpdateDialog() {
		AlertUtils.showAlert(
				this,
				Html.fromHtml(
						getResources().getString(R.string.update_version_code)
								+ PreferenceUtils.getString(
										Const.AppUpdate.APK_VERSION, null)
								+ "<br>"
								+ getResources().getString(
										R.string.update_file_size)
								+ PreferenceUtils.getString(
										Const.AppUpdate.APK_SIZE, null)
								+ "<br>"
								+ getResources().getString(
										R.string.update_description)
								+ "<br>"
								+ PreferenceUtils.getString(
										Const.AppUpdate.APK_DESCRIPTION, null))
						.toString(),
				getResources().getString(R.string.update_find_new_version),
				getResources().getString(R.string.update_now), getResources()
						.getString(R.string.update_nexttime),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						msgNotification = new Notification();
						msgNotification.icon = R.drawable.ic_launcher;
						msgNotification.flags = Notification.FLAG_NO_CLEAR;
						msgNotificationManager = (NotificationManager) mActivity
								.getSystemService(Context.NOTIFICATION_SERVICE);
						msgIntent = new Intent();
						msgIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
						msgPendingIntent = PendingIntent.getActivity(mActivity,
								0, msgIntent, 0);
						// 自定义界面
						final RemoteViews rv = new RemoteViews(mActivity
								.getPackageName(),
								R.layout.app_pause_notification);
						DownloadNewApkHttpGet downloadNewApkHttpGet = new DownloadNewApkHttpGet();
						downloadNewApkHttpGet
								.setAppUpdateCallback(new AppUpdateCallback() {

									@Override
									public void beforeApkDOwnload(int fileLength) {
										flieMaxSize = fileLength;
										Timber.d("flieMaxSize:" + flieMaxSize);
									}

									@Override
									public void inApkDownload(
											int downLoadFileSize) {
										float percent = (float) downLoadFileSize
												* 100 / flieMaxSize;
										Timber.d("percent:" + (int) percent);
										rv.setImageViewResource(
												R.id.update_noti_iv,
												R.drawable.ic_launcher);
										rv.setTextViewText(
												R.id.app_pause_name_tv,
												"正在下载更新文件：" + (int) percent
														+ "%");
										rv.setProgressBar(R.id.update_noti_pb,
												100, (int) percent, false);
										msgNotification.contentView = rv;
										msgNotification.contentIntent = msgPendingIntent;

										msgNotificationManager.notify(1100,
												msgNotification);
									}

									@Override
									public void afterApkDownload(int reFlag) {
										msgNotificationManager
												.cancel(msgNotificationID);
									}

									@Override
									public void FailureApkDownload(int reFlag) {
										failureUpdateHandler
												.sendEmptyMessage(0);
										msgNotificationManager
												.cancel(msgNotificationID);
									}
								});
						downloadNewApkHttpGet.downloadNewApkFile(
								mActivity,
								PreferenceUtils.getString(
										Const.AppUpdate.APK_DOWNLOAD_URL, null),
								Const.AppUpdate.APK_NAME);
						UpdateManager.getInstance().setUpdate(false);
					}
				}, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
//						PreferenceUtils.setInt(Const.AppUpdate.APK_UPDATE_FLAG,
//								2);
//						UpdateManager.getInstance().setUpdate(false);
					}

				});
	}
	
	private Handler failureUpdateHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			Toast.makeText(MainActivity.this,
					R.string.toast_network_error_download_file_failure,
					Toast.LENGTH_SHORT).show();
		}

	};

}
