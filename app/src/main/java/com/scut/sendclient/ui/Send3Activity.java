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

/**
 * 通过登录或注册后，跳转到操作界面 负责添加监控用户和开始启动发送数据功能
 * 
 * @author 林培东、刘亮澎
 */
public class Send3Activity extends BaseActivity {
	Button bt,bt2;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send3);
		 bt = (Button) findViewById(R.id.send3_button1);
		 bt2 = (Button) findViewById(R.id.send3_button2);
		bt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
		//		Send3Activity.this.bt.setBackgroundColor(Color.GRAY);
				Intent intent = new Intent();    
	            intent.setClass(Send3Activity.this, Send3_1Activity.class);   
	            Send3Activity.this.startActivity(intent);  
	            Send3Activity.this.finish();
			}
		});
		
		bt2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
	//			Send3Activity.this.bt.setBackgroundColor(Color.GRAY);
				Intent intent = new Intent();    
	            intent.setClass(Send3Activity.this, Send4Activity.class);   
	            Send3Activity.this.startActivity(intent); 
	            Send3Activity.this.finish();
			}
		});
		}
		
}
