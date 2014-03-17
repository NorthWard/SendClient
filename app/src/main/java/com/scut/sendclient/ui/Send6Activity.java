package com.scut.sendclient.ui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.scut.sendclient.R;
import com.scut.sendclient.ui.llp.DeviceListActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sensor.analyse.selfdefine.DetectionAmplitude;
import sensor.analyse.selfdefine.MaxNoException;
import sensor.bluetooth.llp.BluetoothService;
import sensor.bluetooth.llp.Utils;
import sensor.gps.GPSLocation;
import sensor.task.SendSensorDataTask;
import sensor.task.SendSimpleResultTask;
import sensor.tools.Constant;

/**
 * 通过登录或注册后，跳转到操作界面 负责添加监控用户和开始启动发送数据功能
 * 
 * @author 林培东、刘亮澎
 */
public class Send6Activity extends BaseActivity {

	private ExecutorService executorService = Executors.newCachedThreadPool();//构造一个线程池

	private SendSensorDataTask send; // 发送task
	private SendSimpleResultTask sendResult; // 发送简单分析结果task
	private DetectionAmplitude detection; // 检测
	Button connectButton;
	Button exitButton;

	private int send_rate;
	private int analysis_rate;

	BluetoothDevice btDevice;
	BluetoothAdapter btAdapter;
	BluetoothSocket btSocket;
	BluetoothService btService = null;

	public GPSLocation gps = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send6);

		initSet();

		// 获取一个gps的操纵
		gps = GPSLocation.getInstance(this);
		gps.setUpdateTime(Constant.UPDATE_TIME); // 设置更新时间间隔为10s

		send = new SendSensorDataTask(gps, handler);
		sendResult = new SendSimpleResultTask(handler);

		btAdapter = BluetoothAdapter.getDefaultAdapter();
		btService = BluetoothService.getInstance(this, btHandler);
		if (btAdapter == null) {
			Toast.makeText(this, "蓝牙不可用", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		if (!btAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, Constant.REQUEST_ENABLE_BT);
		} else {
			if (btService == null)
				;
		}
	}

	private void initSet() {

		connectButton = (Button) findViewById(R.id.send6_connect);
		connectButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			//	Send6Activity.this.connectButton.setBackgroundColor(Color.GRAY);
				Intent intent = new Intent(Send6Activity.this,
						DeviceListActivity.class);
				startActivityForResult(intent, Constant.REQUEST_CONNECT_DEVICE);
			}
		});



		// 获取SharedPreferences，将用户名保存起来，方便以后使用
		SharedPreferences sp = getSharedPreferences(Constant.SHARE_NAME,
				Context.MODE_PRIVATE);
		
		send_rate = sp.getInt(Constant.SEND_RATE, Constant.DEFAULT_SEND_RATE);
		System.out.println("send_rate="+send_rate);
		analysis_rate = sp.getInt(Constant.ANALYSIS_RATE, Constant.DEFAULT_ANALYSIS_RATE);
	
		float xf = sp.getFloat(Constant.MAX_NO_EXCEPTION_DATA,
				MaxNoException.DEFAULT_VALUE);
		if (xf < 5) {
			xf = MaxNoException.DEFAULT_VALUE;
		}
		MaxNoException max = new MaxNoException(xf);
		detection = new DetectionAmplitude(max);
		int avgNums = sp.getInt(Constant.DETECTION_AVG_COUNT, 0);
		if (avgNums > 0) {
			Log.i("send6", avgNums + "");
			List<Long> list = new ArrayList<Long>();
			for (int i = 0; i != avgNums; ++i)
				list.add(sp.getLong(Constant.DETECTION_AVG + i, 0));
			detection.addSetAvg(list);
		}
		
		detection
				.setOnDetectingException(new DetectionAmplitude.OnDetectingException() {

					@Override
					public void onDectectionException(String exception) {
						resultRes = "出现了异常";
					}
					
				}
						);
	}

	
	
	
	
	private String resultRes = "no exception";
	private boolean isCountAvg = false;

	@Override
	public void onResume() {
		super.onResume();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case Constant.REQUEST_CONNECT_DEVICE:
			if (resultCode == Activity.RESULT_OK) {
				
				//保存蓝牙地址
				SharedPreferences sp = getSharedPreferences(Constant.SHARE_NAME, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sp.edit();
				editor.putString(Constant.EXTRA_DEVICE_ADDRESS, data.getStringExtra(Constant.EXTRA_DEVICE_ADDRESS));
				editor.commit();
				connect(data);
				 data.setClass(Send6Activity.this,RunActivity.class);  
				
		            Send6Activity.this.startActivity(data); 
		            Send6Activity.this.finish();
				
			}
			break;
		case Constant.REQUEST_ENABLE_BT:
			if (resultCode == Activity.RESULT_OK) {
			} else {
				Toast.makeText(this, "蓝牙没有成功打开", Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	private void connect(Intent data) {
		String address = data.getExtras().getString(
				Constant.EXTRA_DEVICE_ADDRESS);
		btDevice = btAdapter.getRemoteDevice(address);
		btService.connect(btDevice);
		Log.i("connect", "connect");
		// 连接蓝牙
		Toast.makeText(this, "成功连接", Toast.LENGTH_SHORT).show();
		
	}

	private final Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case Constant.SEND_SENSOR_FAIL:
					Toast.makeText(Send6Activity.this, "发送传感器数据失败", Toast.LENGTH_SHORT)
							.show();
					break;
				default:
			}
		}
	};

	private final BluetoothService.BTDataHandler btHandler = new BluetoothService.BTDataHandler() {
		ArrayList<Long> list = new ArrayList<Long>();
		int count = 0;

		@Override
		public void runHandler(byte[] readBuf) {
			System.out.println("TestActivity：read");
			String readMessage = Utils.bytes2HexString(readBuf);
			System.out.println("TestActivity：read"+readMessage);
			if (readMessage.length() == 54) {
				count = count + 1;

				if (count % analysis_rate == 0) {
					list.add((long) Utils.convert2Short(readMessage, 10));
					list.add((long) Utils.convert2Short(readMessage, 10 + 4));
					list.add((long) Utils.convert2Short(readMessage,10 + 4 * 2));
					list.add((long) Utils.convert2Short(readMessage,
							10 + 4 * 3));
					list.add((long) Utils.convert2Short(readMessage,
							10 + 4 * 4));
					list.add((long) Utils.convert2Short(readMessage,
							10 + 4 * 5));
					list.add((long) Utils.convert2Short(readMessage,
							10 + 4 * 6));
					list.add((long) Utils.convert2Short(readMessage,
							10 + 4 * 7));
					list.add((long) Utils.convert2Short(readMessage,
							10 + 4 * 8));
					if (analysis_rate > send_rate)
						count = 0;
					// 做你们想做的分析工作
					System.out.println("list");
					System.out.println(list);
					System.out.println("list");
					for (Long l : list) {
						Log.i("test", l + "");
					}
					if (!isCountAvg)
						detection.doDectecting(list);
					else
						detection.addSetAvg(list);
					// sendResult.setMsg(resultRes);
					// executorService.execute(sendResult);
					list.clear();
				}
				// 发送数据
				if (count % send_rate == 0) {
					if (analysis_rate < send_rate)
						count = 0;
					byte temp[] = new byte[readBuf.length];
					for (int i = 0; i != temp.length; ++i) {
						temp[i] = readBuf[i];
					}

					send.setByte(temp);
					executorService.execute(send);//send是一个线程
				}
			}
		}
	};
}
