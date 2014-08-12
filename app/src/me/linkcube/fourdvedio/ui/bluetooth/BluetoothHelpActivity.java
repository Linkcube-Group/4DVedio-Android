package me.linkcube.fourdvedio.ui.bluetooth;

import me.linkcube.fourdvedio.R;
import me.linkcube.fourdvedio.ui.BaseActivity;
import android.os.Bundle;

public class BluetoothHelpActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetooth_help_activity);
		configureActionBar(R.string.bluetooth_help);
	}

	

}
