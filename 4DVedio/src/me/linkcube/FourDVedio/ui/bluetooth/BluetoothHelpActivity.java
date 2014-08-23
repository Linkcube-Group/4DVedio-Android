package me.linkcube.FourDVedio.ui.bluetooth;

import me.linkcube.FourDVedio.R;
import me.linkcube.FourDVedio.ui.BaseActivity;
import android.os.Bundle;

public class BluetoothHelpActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetooth_help_activity);
		configureActionBar(R.string.bluetooth_help);
	}

	

}
