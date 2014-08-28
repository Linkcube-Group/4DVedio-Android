package me.linkcube.FourDVedio.widget;

import me.linkcube.FourDVedio.R;
import me.linkcube.FourDVedio.ui.listener.OnSingleStatusBarClickListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * 状态栏控件
 * 
 * @author orange
 * 
 */
public class SingleStatusBarView extends LinearLayout {


	private Button connectIndicatorBtn;

	private OnSingleStatusBarClickListener mListener;

	public SingleStatusBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SingleStatusBarView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = mInflater.inflate(R.layout.single_status_bar_view, this,
				true);
		connectIndicatorBtn = (Button) view
				.findViewById(R.id.connect_indicator_btn);

		connectIndicatorBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mListener.showBluetoothSettingActivity();
			}
		});


	}

	public void setBluetoothState(boolean success) {
		int resId = success ? R.drawable.btn_connect_indicator_on
				: R.drawable.btn_connect_indicator_off;
		connectIndicatorBtn.setBackgroundResource(resId);
		//ViewUtils.setInvisible(resetBtn, !success);
	}

	public void setOnSingleStatusBarClickListener(
			OnSingleStatusBarClickListener listener) {
		this.mListener = listener;
	}
}
