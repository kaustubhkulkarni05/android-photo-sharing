package ee.ut.cs.mobile;

import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ShareActivity extends Activity {

	private static final int REQUEST_ENABLE_BT = 100;

	private ArrayList<String> items = new ArrayList<String>();
	private ArrayAdapter<String> itemAdapter;

	private BroadcastReceiver mReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share);

		if (!BluetoothManager.isBluetoothSupported()) {
			Toast.makeText(this, "Bluetooth not supported!", Toast.LENGTH_LONG)
					.show();
			finish();
			return;
		}

		ListView shareList = (ListView) findViewById(R.id.shareList);
		itemAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, items);
		shareList.setAdapter(itemAdapter);
		registerForContextMenu(shareList);
		shareList.setClickable(true);
		shareList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			}
		});

		BluetoothManager.enable(this, REQUEST_ENABLE_BT);
		if (BluetoothManager.isEnabled()) {
			searchBluetoothDevices();
		}

		final Activity activity = this;
		Button discoverable = (Button) findViewById(R.id.discoverable);
		discoverable.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				BluetoothManager.ensureDiscoverable(activity);
			}
		});
	}

	private void searchBluetoothDevices() {
		Set<BluetoothDevice> pairedDevices = BluetoothManager.getBondedDevices();

		// Loop through paired devices
		for (BluetoothDevice device : pairedDevices) {
			// Add the name and address to an array adapter to show in a
			// ListView
			itemAdapter.add(device.getName() + "\n" + device.getAddress());
		}

		BluetoothManager.startDeviceSearch(this, new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				// When discovery finds a device
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					// Get the BluetoothDevice object from the Intent
					BluetoothDevice device = intent
							.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					// Add the name and address to an array adapter to show in a
					// ListView
					String deviceString = device.getName() + "\n"
							+ device.getAddress();
					if (!items.contains(deviceString)) {
						itemAdapter.add(device.getName() + "\n"
								+ device.getAddress());
					}
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			searchBluetoothDevices();
			return;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		if (mReceiver != null) {
			BluetoothManager.stopDeviceSearch(this, mReceiver);
		}
		super.onDestroy();
	}
}