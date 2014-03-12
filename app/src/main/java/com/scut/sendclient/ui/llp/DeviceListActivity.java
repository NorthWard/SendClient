package com.scut.sendclient.ui.llp;

import java.util.Set;

import sensor.tools.Constant;

import com.scut.sendclient.R;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 显示设备列表
 * @author 刘亮澎
 */
public class DeviceListActivity extends Activity{

	    private BluetoothAdapter mBtAdapter;
	    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
	    private ArrayAdapter<String> mNewDevicesArrayAdapter;

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        // Setup the window
	        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	        setContentView(R.layout.device_list);

	        // Set result CANCELED incase the user backs out
	        setResult(Activity.RESULT_CANCELED);

	        // Initialize the button to perform device discovery
	        Button scanButton = (Button) findViewById(R.id.button_scan);
	        scanButton.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	                doDiscovery();
	                v.setVisibility(View.GONE);
	            }
	        });

	        // Initialize array adapters. One for already paired devices and
	        // one for newly discovered devices
	        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
	        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

	        // Find and set up the ListView for paired devices
	        ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
	        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
	        pairedListView.setOnItemClickListener(mDeviceClickListener);

	        // Find and set up the ListView for newly discovered devices
	        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
	        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
	        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

	        // Register for broadcasts when a device is discovered
	        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	        this.registerReceiver(mReceiver, filter);

	        // Register for broadcasts when discovery has finished
	        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
	        this.registerReceiver(mReceiver, filter);

	        // Get the local Bluetooth adapter
	        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

	        // Get a set of currently paired devices
	        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

	        // If there are paired devices, add each one to the ArrayAdapter
	        if (pairedDevices.size() > 0) {
	            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
	            for (BluetoothDevice device : pairedDevices) {
	                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
	            }
	        } else {
	            String noDevices = "No Devices";
	            mPairedDevicesArrayAdapter.add(noDevices);
	        }
	    }

	    @Override
	    protected void onDestroy() {
	        super.onDestroy();

	        // Make sure we're not doing discovery anymore
	        if (mBtAdapter != null) {
	            mBtAdapter.cancelDiscovery();
	        }

	        // Unregister broadcast listeners
	        this.unregisterReceiver(mReceiver);
	    }

	    /**
	     * Start device discover with the BluetoothAdapter
	     */
	    private void doDiscovery() {

	        setProgressBarIndeterminateVisibility(true);
	        setTitle("搜索中");

	        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

	        if (mBtAdapter.isDiscovering()) {
	            mBtAdapter.cancelDiscovery();
	        }

	        mBtAdapter.startDiscovery();
	    }

	    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
	            mBtAdapter.cancelDiscovery();

	            String info = ((TextView) v).getText().toString();
	            String address = info.substring(info.length() - 17);

	            Intent intent = new Intent();
	            intent.putExtra(Constant.EXTRA_DEVICE_ADDRESS, address);

	            setResult(Activity.RESULT_OK, intent);
	            finish();
	        }
	    };

	    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            String action = intent.getAction();

	            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
	                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
	                }
	            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
	                setProgressBarIndeterminateVisibility(false);
	                if (mNewDevicesArrayAdapter.getCount() == 0) {
	                    String noDevices = "No Devices";
	                    mNewDevicesArrayAdapter.add(noDevices);
	                }
	            }
	        }
	    };

	}


