package me.linkcube.fourdvedio.ui.listener;

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
	 */
	void onVoiceMode(int level);
	
	/**
	 * 关闭声控模式的回调
	 * 
	 * @param level
	 */
	void offVoiceMode(int level);

	/**
	 * 提示蓝牙没有连接
	 */
	void showConnectBluetoothTip();
	/**
	 * 提示用户打开外部声音
	 */
	void showOpenMusicPlayerDialog();
}
