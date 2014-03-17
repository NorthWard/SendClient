package com.scut.sendclient.ui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ant.liao.GifView;
import com.scut.sendclient.R;
import com.scut.sendclient.ui.llp.DeviceListActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sensor.analyse.selfdefine.AvgCount;
import sensor.analyse.selfdefine.DetectionAmplitude;
import sensor.analyse.selfdefine.MaxNoException;
import sensor.bluetooth.llp.BluetoothService;
import sensor.bluetooth.llp.Utils;
import sensor.gps.GPSLocation;
import sensor.task.SendSensorDataTask;
import sensor.task.SendSimpleResultTask;
import sensor.tools.Constant;

public class RunActivity extends BaseActivity {

	private ExecutorService executorService = Executors.newCachedThreadPool();

	private SendSensorDataTask send; // 发送task

	private SendSimpleResultTask sendResult; // 发送简单分析结果task
	private DetectionAmplitude detection; // 检测
	Button exitButton;
	private int send_rate;

	private int analysis_rate;
	BluetoothDevice btDevice;

	BluetoothAdapter btAdapter;
	BluetoothSocket btSocket;
	BluetoothService btService = null;
	public GPSLocation gps = null;

	private String resultRes = "no exception";

	private boolean isCountAvg = false;

	private final Handler handler = new Handler( ){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case Constant.SEND_SENSOR_FAIL:
					Toast.makeText(RunActivity.this, "发送数据失败", Toast.LENGTH_SHORT)
							.show();
					break;
				default:
			}
		}
	};

	private final BluetoothService.BTDataHandler BTHandler = new BluetoothService.BTDataHandler() {
		ArrayList<Long> list = new ArrayList<Long>();
		int count = 0;

		@Override
		public void runHandler(byte[] btData) {
			System.out.println("read");
			String readMessage = Utils.bytes2HexString(btData);
			System.out.println("read:\n" + readMessage);
			if (readMessage.length() == 54) {
				count = count + 1;

				if (count % analysis_rate == 0) {
					list.add((long) Utils.convert2Short(readMessage, 10));
					list.add((long) Utils
							.convert2Short(readMessage, 10 + 4));
					list.add((long) Utils.convert2Short(readMessage,
							10 + 4 * 2));
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
					for (Long l : list) {
						Log.i("test", l + "");
					}
					if (!isCountAvg)
						detection.doDectecting(list);
					else
						detection.addSetAvg(list);
					sendResult.setMsg(resultRes);
					executorService.execute(sendResult);
					list.clear();
				}
				// 发送数据
				if (count % send_rate == 0) {
					if (analysis_rate < send_rate)
						count = 0;
					byte temp[] = new byte[btData.length];
					for (int i = 0; i != temp.length; ++i) {
						temp[i] = btData[i];
					}
					System.out.println("??" + temp[0]);
					send.setByte(temp);
					executorService.execute(send);
				}
			}
		}
	};
	private static final String TAG = "RUN";

	private void connect(Intent data) {
		String address = data.getStringExtra(Constant.EXTRA_DEVICE_ADDRESS);
		Log.i(TAG, address);
		if (address == null || address.length() < 3) {
			// 地址错误,连接蓝牙
			connectBlueTooch();
			return;
		}
		btDevice = btAdapter.getRemoteDevice(address);
		btService.connect(btDevice);
		Log.i("connect", "connect");
	}

	private void initSet() {

		final TextView result = (TextView) findViewById(R.id.result);

	exitButton =(Button) findViewById(R.id.run_exit);
			
			
			
		// 获取SharedPreferences，将用户名保存起来，方便以后使用
		SharedPreferences sp = getSharedPreferences(Constant.SHARE_NAME,
				Context.MODE_PRIVATE);
		send_rate = sp.getInt(Constant.SEND_RATE, Constant.DEFAULT_SEND_RATE);
		analysis_rate = sp.getInt(Constant.ANALYSIS_RATE,
				Constant.DEFAULT_ANALYSIS_RATE);
		float xf = sp.getFloat(Constant.MAX_NO_EXCEPTION_DATA,
				MaxNoException.DEFAULT_VALUE);
		if (xf < 5) {
			xf = MaxNoException.DEFAULT_VALUE;
		}
		MaxNoException max = new MaxNoException(xf);
		detection = new DetectionAmplitude(max);
		int avgNums = sp.getInt(Constant.DETECTION_AVG_COUNT, 0);
		if (avgNums > 0) {
			Log.i("test", avgNums + "");
			List<Long> list = new ArrayList<Long>();
			for (int i = 0; i != avgNums; ++i)
				list.add(sp.getLong(Constant.DETECTION_AVG + i, 0));
			detection.addSetAvg(list);
		}
		exitButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (btDevice != null) {
					if(!btService.isNull())
			System.out.print("123stop")		;
					btService.stop();
				}
				SharedPreferences sp = getSharedPreferences(
						Constant.SHARE_NAME, Context.MODE_PRIVATE);
				SharedPreferences.Editor ed = sp.edit();
				MaxNoException max = detection.getMaxNoException();
				ed.putFloat(Constant.MAX_NO_EXCEPTION_DATA, max.getMaxNoException());
				if (detection.getAvgCount() != null) {

					AvgCount avg = detection.getAvgCount();
					Log.i("run", avg.size() + "");
					ed.putInt(Constant.DETECTION_AVG_COUNT, avg.size());
					for (int i = 0; i != avg.size(); ++i)
						ed.putLong(Constant.DETECTION_AVG + i, avg.get(i));
				}
				ed.commit();
				RunActivity.this.finish();
			}

			
		});
		detection
				.setOnDetectingException(new DetectionAmplitude.OnDetectingException() {

					@Override
					public void onDectectionException(String exception) {
						// TODO Auto-generated method stub
						result.setText("出现了异常:" + exception);
						resultRes = "出现了异常";
						MediaPlayer player = MediaPlayer.create(
								RunActivity.this, R.raw.warn1);
						try {
							player.prepare();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						player.start();
					}
				});

		// 获取一个gps的操纵
		gps = GPSLocation.getInstance(this);
		gps.setUpdateTime(Constant.UPDATE_TIME); // 设置更新时间间隔为10s

		send = new SendSensorDataTask(gps, handler);
		sendResult = new SendSimpleResultTask(handler);

		btAdapter = BluetoothAdapter.getDefaultAdapter();
		btService = BluetoothService.getInstance(this, BTHandler);
		if (btAdapter == null) {
			Toast.makeText(this, "蓝牙不可用", Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		int def = sp.getInt(Constant.MODE_APUD, 0);
		final SharedPreferences.Editor editor = sp.edit();
		btService.setModeApdu(def);
	}

	private void connectBlueTooch() {
		Intent intent = new Intent(RunActivity.this, DeviceListActivity.class);
		startActivityForResult(intent, Constant.REQUEST_CONNECT_DEVICE);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case Constant.REQUEST_CONNECT_DEVICE:
			if (resultCode == Activity.RESULT_OK) {
				connect(data);
				// 连接蓝牙吧？
				Toast.makeText(this, "成功连接", Toast.LENGTH_SHORT).show();
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.run);
		GifView gif = (GifView) findViewById(R.id.gif1);
		gif.setGifImage(R.drawable.run_progress);

		initSet();

		connect(getIntent());
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
}
