package com.scut.sendclient.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.scut.sendclient.R;
import com.scut.sendclient.ui.llp.DeviceListActivity;

import sensor.analyse.selfdefine.AvgCount;
import sensor.analyse.selfdefine.DetectionAmplitude;
import sensor.analyse.selfdefine.MaxNoException;
import sensor.bluetooth.llp.BluetoothService;
import sensor.bluetooth.llp.Utils;
import sensor.gps.GPSLocation;
import sensor.task.SendSensorDataTask;
import sensor.task.SendSimpleResultTask;
import sensor.tools.Constant;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class Send2Activity extends BaseActivity {
	Button bt;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send2);
		 bt = (Button) findViewById(R.id.send2_sure);
		bt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				bt.setBackgroundColor(Color.GRAY);
				Intent intent = new Intent();    
	            intent.setClass(Send2Activity.this, Send3Activity.class);   
	            Send2Activity.this.startActivity(intent);  
	            Send2Activity.this.finish();
			}
		});
		}
		
}
