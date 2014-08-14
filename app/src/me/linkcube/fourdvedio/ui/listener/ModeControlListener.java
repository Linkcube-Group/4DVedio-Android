package me.linkcube.fourdvedio.ui.listener;

import android.R.bool;

/**
 * 模式选中回调接口
 * @author xinyang
 *
 */
public interface ModeControlListener {

	/**
	 * 声控模式的回调
	 * 
	 * @param level
	 * @param headset 是否插入耳机
	 */
	void onVoiceMode(int level,boolean headsetOn);
	
	/**
	 * 关闭声控模式的回调
	 * 
	 * @param level
	 * @param headset 是否插入耳机
	 */
	void offVoiceMode(int level,boolean headsetOn);
	/**
	 * 手机震动
	 */
	void mobileVibrator(int sound);
	/**
	 * 提示蓝牙没有连接
	 */
	void showConnectBluetoothTip();
	/**
	 * 提示用户打开外部声音
	 */
	void showOpenMusicPlayerDialog();
	/**
	 * 注册监听耳机是否连接广播
	 */
	void registerModifyAudioSettingReceiver();
}
