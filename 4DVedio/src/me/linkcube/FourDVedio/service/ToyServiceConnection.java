package me.linkcube.FourDVedio.service;

import me.linkcube.FourDVedio.FourDVedioApplication;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * 玩具启动服务与activity通信
 * 
 * 
 */
public class ToyServiceConnection implements ServiceConnection {

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		FourDVedioApplication.toyServiceCall = (IToyServiceCall) service;
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		FourDVedioApplication.toyServiceCall = null;
	}
}