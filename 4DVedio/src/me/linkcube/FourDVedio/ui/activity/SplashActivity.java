package me.linkcube.FourDVedio.ui.activity;

import me.linkcube.FourDVedio.R;
import me.linkcube.FourDVedio.database.DataManager;
import me.linkcube.FourDVedio.ui.BaseActivity;
import me.linkcube.FourDVedio.ui.bluetooth.BluetoothSettingActivity;
import me.linkcube.FourDVedio.utils.PreferenceUtils;
import me.linkcube.FourDVedio.utils.Timber;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class SplashActivity extends BaseActivity {

	private boolean isShowGuide;

	// private UserEntity user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_activity);
		Timber.d("数据库初始化");
		DataManager.getInstance().initDatabase(
				mActivity.getApplicationContext());
		Timber.d("键值存储初始化");
		PreferenceUtils.initDataShare(getApplicationContext());

	}

	@Override
	protected void onResume() {
		super.onResume();
		new Handler().postDelayed(new Runnable() {
			public void run() {
				startActivity(new Intent(mActivity, MainActivity.class));
				finish();
			}
		}, 2000);
	}
}
