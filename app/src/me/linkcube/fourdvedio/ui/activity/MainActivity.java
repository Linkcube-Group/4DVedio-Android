package me.linkcube.fourdvedio.ui.activity;

import me.linkcube.fourdvedio.R;
import me.linkcube.fourdvedio.service.ToyServiceConnection;
import me.linkcube.fourdvedio.ui.BaseActivity;
import me.linkcube.fourdvedio.ui.bluetooth.BluetoothSettingActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends BaseActivity {
	
	private ToyServiceConnection toyServiceConnection;

	private Button button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		button=(Button)findViewById(R.id.test_btn);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				startActivity(new Intent(mActivity, BluetoothSettingActivity.class));
			}
		});
		
		bindToyService();
	}
	
	private void bindToyService() {
		toyServiceConnection = new ToyServiceConnection();
		Intent toyintent = new Intent(this,
				me.linkcube.fourdvedio.service.ToyService.class);
		this.startService(toyintent);
		bindService(toyintent, toyServiceConnection, Context.BIND_AUTO_CREATE);
		//mShakeSensor = new ShakeSensor(this.getApplicationContext());
		//mVoiceSensor = new VoiceSensor();
	}
	
	

}
