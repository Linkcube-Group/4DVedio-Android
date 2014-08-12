package me.linkcube.fourdvedio.ui.bluetooth;

import me.linkcube.fourdvedio.R;
import me.linkcube.fourdvedio.bluetooth.DeviceConnectionManager;
import me.linkcube.fourdvedio.ui.BaseListAdapter;
import me.linkcube.fourdvedio.utils.Const;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewGroup;

/**
 * 蓝牙搜索列表适配器
 * 
 * @author orange
 * 
 */
public class BluetoothDeviceAdapter extends BaseListAdapter<BluetoothDevice> {

	private Context mContext;

	public BluetoothDeviceAdapter(Context context) {
		this.mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BluetoothDeviceListCell cell;
		if (convertView == null) {
			cell = new BluetoothDeviceListCell(mContext);
		} else {
			cell = (BluetoothDeviceListCell) convertView;
		}
		BluetoothDevice device = getItem(position);
		String name = device.getName();
		cell.setDeviceName(name);

		if (DeviceConnectionManager.getInstance().isConnected()) {
			cell.setDeviceState(R.string.connected);
		} else {
			if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
				cell.setDeviceState(R.string.bonded);
			} else if (device.getBondState() == BluetoothDevice.BOND_NONE) {
				cell.setDeviceState(R.string.unbond);
			} else if (device.getBondState() == BluetoothDevice.BOND_BONDING) {
				cell.setDeviceState(R.string.bonding);
			}
		}

		return cell;
	}

}
