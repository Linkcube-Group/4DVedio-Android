package me.linkcube.fourdvedio;

import me.linkcube.fourdvedio.service.IToyServiceCall;
import me.linkcube.fourdvedio.utils.Timber;
import android.app.Application;

public class FourDVedioApplication extends Application {

	public static IToyServiceCall toyServiceCall;

	public FourDVedioApplication() {
		super();
	}

	@Override
	public void onCreate() {
		if (BuildConfig.DEBUG) {
			Timber.plant(new Timber.DebugTree());
		} else {
			Timber.plant(new Timber.HollowTree());
		}
	}

}
