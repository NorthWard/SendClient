package sensor.bluetooth.llp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import sensor.tools.Constant;

/**
 * 做了修改，把时间间隔用常量代替，方便修改，如出错要修改回来
 * @at 2012/9/26
 */
public class BluetoothService {

	static final UUID SPP_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	Handler mhandler;
	private final BluetoothAdapter mAdapter;
	private ConnectedThread connectedThread;
	private ConnectThread connectThread;
	private static boolean flag = false;

	private BluetoothService(Context context, Handler handler) {
		mhandler = handler;
		mAdapter = BluetoothAdapter.getDefaultAdapter();
	}
	
	private static BluetoothService instance;
	
	public static boolean isNull(){
		return instance==null;
	}
	
	public static BluetoothService getInstance(Context context,Handler handler){
		if(instance==null){
			instance = new BluetoothService(context, handler);
		}
		return instance;
	}
	
	
	private int mode_apdu=0;
	
	public void setModeApdu(int i){
		mode_apdu = i;
		if(connectThread!=null)
			connectThread.setModeApdu(i);
	}
	private long sleep_time =10;
	private class ConnectThread extends Thread {
		public String modeApdu = "2005501f280000";
		
		public String getModeApdu() {
			return modeApdu;
		}
		
		public void setModeApdu(int i) {
			switch(i){
				// ModeApdu meaning:
				// 20 05 50: prefix
				// 1: no AHRS, no RFU0, calibrated data, with ACC
				// F: with GYRO, with MAG, with PRESS, with TEMP
				// xx: setting FQ and USB output interface
				// 0000: continuous mode
			case 0:
				sleep_time =1000;
				modeApdu="2005501f000000";
				break;
			case 1:
				sleep_time =100;
				modeApdu="2005501f080000";
				break;
			case 2:
				sleep_time =40;
				modeApdu="2005501f100000";
				break;
			case 3:
				sleep_time =20;
				modeApdu="2005501f180000";
				break;
			case 4:
				sleep_time =34;
				modeApdu="2005501f200000";
				break;
			case 5:	//100
				sleep_time = 10;
				modeApdu="2005501f280000";
				break;
			case 6:
				sleep_time =3;
				modeApdu="2005501f300000";
				break;
			}
			
			
		}

		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;
		private InputStream inputStream;
		private OutputStream outputStream;
		 
		private static final String OPEN_LIGHT = "20020801";
		private static final String CLOSE_LIGHT = "20020800";
		private static final String CONNECT_APDU = "200100";
		//下面的Mode_apdu_x代表频率为X的发送模式
		private static final String MODE_APDU_1  = 	"2005501f000000";
		private static final String MODE_APDU_10 = 	"2005501f080000";
		private static final String MODE_APDU_25 = 	"2005501f100000";
		
		private static final String SEND_APDU = "200152";
		private static final String STOP_APDU = "200153";
		private static final String DISCONNECT_APDU = "200101";

		public ConnectThread(BluetoothDevice device) {
			Log.i("ConnectThread run","connect");
			mmDevice = device;
			BluetoothSocket tmp = null;
			try {
				tmp = device.createRfcommSocketToServiceRecord(SPP_UUID);
			} catch (IOException e) {
				e.printStackTrace();
			}
			setModeApdu(mode_apdu);
			mmSocket = tmp;
		}

		public void run() {
			mAdapter.cancelDiscovery();
			Log.i("ConnectThread run","run");
			try {
				mmSocket.connect();

			} catch (IOException e) {
				try {
					mmSocket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
				return;
			}
			try {
				inputStream = mmSocket.getInputStream();
				outputStream = mmSocket.getOutputStream();
				byte[] bufferOut = new byte[7];
				byte[] bufferIn = new byte[3];
				byte[] bufferInLight= new byte[4];
				bufferOut = Utils.convert2HexArray(CONNECT_APDU);
				outputStream.write(bufferOut);
				Thread.sleep(1000);
				outputStream.flush();
				inputStream.read(bufferIn);
				System.out.println("A: "+ Utils.bytes2HexString(bufferIn));
				if (Utils.bytes2HexString(bufferIn).substring(0, 6).equals("800100")) {
					outputStream.write(Utils.convert2HexArray(OPEN_LIGHT));
					outputStream.flush();
					Thread.sleep(1000);
					inputStream.read(bufferInLight);
					bufferOut = Utils.convert2HexArray(modeApdu);
					outputStream.write(bufferOut);
					outputStream.flush();
					Thread.sleep(1000);
					inputStream.read(bufferIn);
					System.out.println("B: "+ Utils.bytes2HexString(bufferIn));
					System.out.println("B: "+ Utils.byteToShort(bufferIn, 0));
					if (Utils.bytes2HexString(bufferIn).substring(0, 6).equals("800150")) {
						bufferOut = Utils.convert2HexArray(SEND_APDU);
						outputStream.write(bufferOut);
						outputStream.flush();
						Thread.sleep(1000);
						inputStream.read(bufferIn);
						System.out.println("c: "+ Utils.bytes2HexString(bufferIn));
						flag = true;
						
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
/*			synchronized (BluetoothService.this) {
				connectThread = null;
			}*/
			Connected(mmSocket); 
		}

		public void cancel() {
			System.out.println("cancle connect");
			flag = false;
			connectedThread.interrupt();
			try {
				byte[] b = new byte[3];
				byte[] bufferInLight= new byte[4];
				
				
				outputStream.write(Utils.convert2HexArray(STOP_APDU));
				outputStream.flush();
				Thread.sleep(1000);
				inputStream.read(b);
				System.out.println("D: "+ Utils.bytes2HexString(b).substring(0, 6));
				if (Utils.bytes2HexString(b).substring(0, 6).equals("800153")) {
					outputStream.write(Utils.convert2HexArray(CLOSE_LIGHT));
					outputStream.flush();
					Thread.sleep(1000);
					inputStream.read(bufferInLight);
					outputStream.write(Utils.convert2HexArray(DISCONNECT_APDU));
					outputStream.flush();
					Thread.sleep(1000);
					inputStream.read(b);
					System.out.println("E: "+ Utils.bytes2HexString(b).substring(0, 6));
					outputStream.close();
					inputStream.close();
					mmSocket.close();
					
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	private class ConnectedThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;

		public ConnectedThread(BluetoothSocket socket) {
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			try {
				tmpIn = mmSocket.getInputStream();
				tmpOut = mmSocket.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;

			
		}

		public void run() {
			int num_byte;
			byte[] bufferOld = new byte[27];
			while (flag) {
				try {
					//从流mmInStream 中获取数据,
					// System.out.println("get data");
					num_byte = mmInStream.read(bufferOld);
					// System.out.println("get data:\n"+Utils.bytes2HexString(bufferOld));
					if(num_byte>=27){
						String s = Utils.bytes2HexString(bufferOld);
						if(s.substring(0, 6).equals("401952")){
							byte[] buffer = bufferOld.clone();
							//把后台消息发送给UI 再进行传送
							// 数据是通过HTTP协议发送的
							mhandler.obtainMessage(Constant.MESSAGE_READ, num_byte,
									-1, buffer).sendToTarget();
						}
					}

					//sleep_time 表示睡眠时间
					Thread.sleep(sleep_time);
				} catch (Exception e) {
					break;
				}
			}
			
		}

		public void cancel() {
			System.out.println("cancle connected");
			
			try {
				mmSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void connect(BluetoothDevice device) {
		connectThread = new ConnectThread(device);
		connectThread.start();
	}

	public synchronized void Connected(BluetoothSocket socket) {

		connectedThread = new ConnectedThread(socket);

		connectedThread.start();
	}

	public synchronized void stop() {
		
		if (connectThread != null) {
			connectThread.cancel();
			connectThread = null;
		}
		if (connectedThread != null) {
			connectedThread.cancel();
			connectedThread = null;
		}
	}
}
