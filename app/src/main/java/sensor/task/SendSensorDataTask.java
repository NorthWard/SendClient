package sensor.task;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.send.houzhi.trans.tools.StaticFinalVariable;
import com.send.houzhi.trans.tools.Transation;
import com.send.houzhi.trans.transfer.DataTransIn;
import com.send.houzhi.trans.transfer.DigitException;

import sensor.gps.GPSLocation;
import sensor.tools.Constant;

public class SendSensorDataTask implements Runnable{
	private GPSLocation gps;
	private Handler handler;
	private DataTransIn dataTransIn = new DataTransIn(StaticFinalVariable.BYTE_STORY_XML_NAME_C,true);
	public SendSensorDataTask(GPSLocation gps,Handler handler){
		this.gps = gps;
		this.handler = handler;
	}
	private byte [] b;
	public void setByte(byte[] b){
		SendSensorDataTask.this.b = b;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			insertGPS();					//把gps 信息加上去
			if(dataTransIn.transDate(b,"1",false, "http://"+Constant.IP+StaticFinalVariable.URL_Str)){
				//				toastShow("发送成功");
			///Message msg = new Message();
			//	msg.what =Constant.MESSAGE_READ;
			//	handler.sendMessage(msg);
				Log.i("ok", "ok");
			}
			else{
				Log.i("fail", "fail");
				Message msg = new Message();
				msg.what =Constant.SEND_SENSOR_FAIL;
				handler.sendMessage(msg);
			}
		} catch (DigitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void insertGPS(){
		Log.i("gps","trans");
		byte [] temp = b;
		b= new byte[temp.length+24];
		System.arraycopy(temp, 0, b, 0, temp.length);
		
		//高度
		int front = temp.length;
		byte [] a =Transation.longToBytes(gps.getAltitudeT(), 8);
		System.arraycopy(a, 0, b, front, a.length);
		
		//经度
		front +=a.length;
		 a =Transation.longToBytes(gps.getLongitudeT(), 8);
		System.arraycopy(a, 0, b, front, a.length);
		
		//纬度
		front +=a.length;
		a =Transation.longToBytes(gps.getLatitudeT(), 8);
		System.arraycopy(a, 0, b, front, a.length);
		
		System.out.println(gps.toString());
	}
}
