package me.linkcube.FourDVedio.ui.listener;

/**
 * StatusBar实现类
 * 
 * @author orange
 * 
 */
public interface OnSingleStatusBarClickListener {

	/**
	 * 展示Setting页面
	 */
	void showMoreSettingActivity();

	/**
	 * 连接玩具
	 */
	void showBluetoothSettingActivity();

	/**
	 * 重置玩具档位
	 */
	void resetToy();

}
