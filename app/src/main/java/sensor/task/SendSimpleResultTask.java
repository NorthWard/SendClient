package sensor.task;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.send.houzhi.trans.tools.StaticFinalVariable;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import sensor.tools.Constant;
import sensor.tools.MyConverter;

public class SendSimpleResultTask implements Runnable{
	private String msg;
	private Handler handler;
	public SendSimpleResultTask(Handler handler){
		
		this.handler = handler;
	}
	
	public void setMsg(String msg){
		this.msg=msg;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		HttpPost request = new HttpPost("http://"+Constant.IP+StaticFinalVariable.URL_MSG);
    	
		
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
		params
			.add(new BasicNameValuePair("msg", MyConverter.escape(msg)));
		
    	boolean isOk=false;
		try {
//			StringEntity string = new StringEntity(msg, "utf-8");
			request.setEntity(new UrlEncodedFormEntity(
					params));
			HttpResponse response = new DefaultHttpClient().execute(request);
			Message msg = new Message();
			if(response.getStatusLine().getStatusCode()==200){
				//成功
				Log.i("send", "send message ok ");
			}
			else{
				synchronized (handler) {
					msg.what = Constant.SEND_RESULT_FAIL;
					handler.sendMessage(msg);
				}
			}
			Log.e("e",response.getStatusLine().getStatusCode()+"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
