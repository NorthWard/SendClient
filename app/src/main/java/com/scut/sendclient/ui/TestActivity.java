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
public class TestActivity extends BaseActivity {

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
		setContentView(R.layout.test);

		initSet();

		// 获取一个gps的操纵
		gps = GPSLocation.getInstance(this);
		gps.setUpdateTime(Constant.UPDATE_TIME); // 设置更新时间间隔为10s

		send = new SendSensorDataTask(gps, handler);
		sendResult = new SendSimpleResultTask(handler);

		btAdapter = BluetoothAdapter.getDefaultAdapter();
		btService = BluetoothService.getInstance(this, handler);
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

		connectButton = (Button) findViewById(R.id.connectButton);
		exitButton = (Button) findViewById(R.id.exitButton);
		final Button exception = (Button) findViewById(R.id.button3);
		final Button cancelException = (Button) findViewById(R.id.button2);
		final Button setAvg = (Button) findViewById(R.id.button4);
		final TextView result = (TextView) findViewById(R.id.textView1);

		connectButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(TestActivity.this,
						DeviceListActivity.class);
				startActivityForResult(intent, Constant.REQUEST_CONNECT_DEVICE);
			}
		});

		exitButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (btDevice != null) {
					btService.stop();
				}
				SharedPreferences sp = getSharedPreferences(
						Constant.SHARE_NAME, Context.MODE_PRIVATE);
				SharedPreferences.Editor ed = sp.edit();
				MaxNoException max = detection.getMaxNoException();
				ed.putFloat(Constant.MAX_NO_EXCEPTION_DATA, max.getMaxNoException());
				if (detection.getAvgCount() != null) {

					AvgCount avg = detection.getAvgCount();
					Log.i("test", avg.size() + "");
					ed.putInt(Constant.DETECTION_AVG_COUNT, avg.size());
					for (int i = 0; i != avg.size(); ++i)
						ed.putLong(Constant.DETECTION_AVG + i, avg.get(i));
				}
				ed.commit();
				System.exit(0);
			}
		});
		Button clear = (Button) findViewById(R.id.clear);
		clear.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				detection.clearAvg();
			}
		});
		Button clearShow = (Button) findViewById(R.id.clearShow);
		clearShow.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				result.setText("");
			}

		});
		// 获取SharedPreferences，将用户名保存起来，方便以后使用
		SharedPreferences sp = getSharedPreferences(Constant.SHARE_NAME,
				Context.MODE_PRIVATE);
		send_rate = sp.getInt(Constant.SEND_RATE, Constant.DEFAULT_SEND_RATE);
		analysis_rate = sp.getInt(Constant.ANALYSIS_RATE, Constant.DEFAULT_ANALYSIS_RATE);
		final EditText et1 = (EditText) findViewById(R.id.editText1);
		final EditText et2 = (EditText) findViewById(R.id.EditText01);
		et1.setText(send_rate + "");
		et2.setText(analysis_rate + "");
		final Button b1 = (Button) findViewById(R.id.button1);
		final Button b2 = (Button) findViewById(R.id.Button01);

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
		b1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (et2.getText() != null && et2.getText().toString() != null) {
					send_rate = Integer.valueOf(et1.getText().toString());
					SharedPreferences sp = getSharedPreferences(
							Constant.SHARE_NAME, Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = sp.edit();
					editor.putInt(Constant.SEND_RATE, send_rate);
					editor.commit();
				}
			}
		});
		b2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (et2.getText() != null && et2.getText().toString() != null) {
					analysis_rate = Integer.valueOf(et2.getText().toString());
					SharedPreferences sp = getSharedPreferences(
							Constant.SHARE_NAME, Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = sp.edit();
					editor.putInt(Constant.ANALYSIS_RATE, analysis_rate);
					editor.commit();
				}
			}
		});

		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		int def = sp.getInt(Constant.MODE_APUD, 0);
		final SharedPreferences.Editor editor = sp.edit();
		spinner.setSelection(def);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				btService.setModeApdu(position);
				editor.putInt(Constant.MODE_APUD, position);

				editor.commit();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		setAvg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isCountAvg)
					setAvg.setText("点击结束计算平均值");
				else
					setAvg.setText("点击开始计算平均值");
				isCountAvg = !isCountAvg;
			}
		});
		exception.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				detection.doLackInformErrorException();
			}
		});
		cancelException.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				detection.doSurplusInformErrorException();
				result.setText("检测异常中。。。");
				resultRes = "no exception";
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
								TestActivity.this, R.raw.warn1);
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
				// 连接蓝牙
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

	private void connect(Intent data) {
		String address = data.getExtras().getString(
				Constant.EXTRA_DEVICE_ADDRESS);
		btDevice = btAdapter.getRemoteDevice(address);
		btService.connect(btDevice);
		Log.i("connect", "connect");
	}

	private final Handler handler = new Handler() {
		ArrayList<Long> list = new ArrayList<Long>();
		int count = 0;

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.MESSAGE_READ:
				System.out.println("TestActivity：read");
				byte[] readBuf = (byte[]) msg.obj;
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
						System.out.println(temp[0]);
						send.setByte(temp);
						executorService.execute(send);//send是一个线程
					}
				}
				break;
			case Constant.SEND_SENSOR_FAIL:
				Toast.makeText(TestActivity.this, "发送传感器数据失败", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
			}
		}
	};
}
