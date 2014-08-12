package me.linkcube.fourdvedio.utils;

public class Const {

	public static class Preference {

		/** 引导页展示标志位 */
		public static final String SHOW_GUIDE = "ShowGuide";

		/** 是否自动登录 */
		public static final String AUTO_LOGIN = "AutoLogin";

		/** 登录用户�? */
		public static final String USER_NAME = "UserName";

		/** 登录密码 */
		public static final String USER_PWD = "UserPwd";

		/** 原始密码 */
		public static final String OLD_USER_PWD = "OldUserPwd";
	}

	/**
	 * 用户个人信息
	 * 
	 * @author Rodriguez-xin
	 * 
	 */
	public static class VCard {

		public static final String GENDER = "GENDER";

		public static final String BIRTHDAY = "BIRTHDAY";

		public static final String PERSONSTATE = "PERSONSTATE";

	}

	/**
	 * 多人游戏时的控制消息
	 * 
	 * @author Rodriguez-xin
	 * 
	 */
	public static class Game {

		public static String REQUESTCMD = "c:";

		public static String REQUESTCONNECTCMD = "connectrequest";

		public static String ACCEPTCONNECTCMD = "acceptconnect";

		public static String REFUSECONNECTCMD = "refuseconnect";

		public static String DISCONNECTCMD = "disconnect";

		public static String POSITIONMODECMD = "m:";

		public static String SHAKESPEEDCMD = "s:";

		public static String SHAKESPEEDLEVELCMD = "sl:";

		public static int ACCEPTTIMECMD = 30;
	}

	/**
	 * 软件更新
	 * 
	 * @author Rodriguez-xin
	 * 
	 */
	public static class AppUpdate {

		public static String CHECK_VERSION_URL = "http://115.29.175.17/version";

		public static String APK_UPDATE_FLAG = "apk_update_flag";// "0"表示不需要更新也不需要展示，�?1”表示需要提示更新，�?2”表示已经提示过，需要在setting页面展示

		public static String APK_NAME = "linkcube.apk";

		public static String APK_VERSION = "apk_version";

		public static String APK_SIZE = "apk_size";

		public static String APK_DESCRIPTION = "apk_description";

		public static String APK_DOWNLOAD_URL = "apk_download_url";

	}

	/**
	 * 游戏�?�?
	 * 
	 * @author Rodriguez-xin
	 * 
	 */
	public static class GameInviteMsg {

		public static String GAME_INVITE_TO = "game_invite_to";

		public static String GAME_INVITE_FROM = "game_invite_from";

		public static String GAME_INVITE_BOTH = "game_invite_both";
	}

	public static class Device {

		public static String DEVICE_NAME = "device_name";

		public static String DEVICE_ADDRESS = "device_address";
	}

	/**
	 * 阅后即焚
	 * 
	 * @author Rodriguez-xin
	 * 
	 */
	public static class DeleteAfterRead {

		public static int COUNT_DOWN = 6;

	}

	/**
	 * 有盟统计
	 * 
	 * @author Rodriguez-xin
	 * 
	 */
	public static class UmengEvent {

		public static String SHACK_MODE_EVENT = "shackmodeevent";

		public static String VOICE_MODE_EVENT = "voicemodeevent";

		public static String MIC_MODE_EVENT = "micmodeevent";

		public static String SEXPOSITION_MODE_EVENT = "sexpositionmodeevent";

		public static String IS_CONNECT_TOY = "isconnecttoy";

		public static String CONNECT_TOY_DURATION = "ConnectToyDuration";

	}

	/**
	 * 模式选择
	 * 
	 * @author Rodriguez-xin
	 * 
	 */
	public static class ToyConst {

		public final static int STATE_NONE = -1;

		public final static int STATE_SHAKE = 0;

		public final static int STATE_VOICE = 1;

		public final static int STATE_POSITION = 2;

		public final static int STATE_CALL = 3;

		public final static int STATE_MIC = 4;

	}

	public static class DownloadAppConst {
		// 下载状�?�：正常，暂停，下载中，已下载，排队�?
		public static final int DOWNLOAD_STATE_NORMAL = 0x00;

		public static final int DOWNLOAD_STATE_PAUSE = 0x01;

		public static final int DOWNLOAD_STATE_DOWNLOADING = 0x02;

		public static final int DOWNLOAD_STATE_FINISH = 0x03;

		public static final int DOWNLOAD_STATE_WAITING = 0x04;

		public static final String[] RELEVANT_SAVE_APK_NAME = {"1","cloudmusic.apk","esvideo.apk","papa.apk","changba.apk","2","sixrooms.apk","ishow.apk","uinew.apk"};

		public static String[] RELEVANT_APP_URL = {
				"1",
				"http://dd.myapp.com/16891/E7BF7E17F704C110EF5FD42E5CD3514E.apk?fsname=com%2Enetease%2Ecloudmusic%5F2%2E0%2E2%5F31.apk",
				"http://dd.myapp.com/16891/6720E4F28E999536B96839778A08D5DD.apk?fsname=com%2Eesvideo%5F1%2E19%2E9%5F20.apk",
				"http://dd.myapp.com/16891/259E405C7F6B3A8E867BD42CD1B2E14D.apk?fsname=me%2Epapa%5F4%2E2%2E2%5F42200.apk",
				"http://dd.myapp.com/16891/53EADB30F63DCB227185A08C29077446.apk?fsname=com%2Echangba%5F5%2E1%2E1%5F511.apk",
				"2",
				"http://dd.myapp.com/16891/7C561367721A31308555ACC15002A10F.apk?fsname=cn%2Ev6%2Esixrooms%5F3%2E0%2E2%5F13.apk",
				"http://dd.myapp.com/16891/9894BDFC7FE202A1D2650F6A5ADFF7DB.apk?fsname=com%2Ewole56%2Eishow%5F2%2E7%2E0%5F11.apk",
				"http://dd.myapp.com/16891/0BC5478C49584375325AA9945B3C57B2.apk?fsname=cn%2Edpocket%2Emoplusand%2Euinew%5F3%2E6%2E1%2Dg%5F58.apk" };

		public static String[] RELEVANT_APP_NAME = { "1", "网易云音�?", "看片神器",
				"啪啪", "唱吧", "2", "六间房秀�?", "我秀美女直播", "友加" };

		public static String[] RELEVANT_APP_ICON = {
				"1",
				"http://pp.myapp.com/ma_icon/0/icon_1168851_18516085_1405915246/72.png",
				"http://pp.myapp.com/ma_icon/0/icon_10309662_18383333_1405656095/72.png",
				"http://pp.myapp.com/ma_icon/0/icon_1020153_18351646_1405585683/72.png",
				"http://pp.myapp.com/ma_icon/0/icon_206678_18643905_1406195127/72.png",
				"2",
				"http://pp.myapp.com/ma_icon/0/icon_10197845_18679894_1406800515/72.png",
				"http://pp.myapp.com/ma_icon/0/icon_1141422_18718654_1407204477/72.png",
				"http://pp.myapp.com/ma_icon/0/icon_243970_18336075_1404810993/72.png" };

	}
}
