package sensor.tools;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

/**
 * 通过Http协议发送带文件或不带文件的请求的工具类
 * 
 * @author 林培东
 */
public class HttpUploadUtil {
	/**
	 * 不带文件的请求发送方法
	 * 
	 * @param actionUrl
	 *            请求的URL
	 * @param params
	 *            请求的参数序列
	 * @return 请求结果
	 */
	public static String postWithoutFile(String actionUrl,
			Map<String, String> params) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(actionUrl);
		Log.i("params",params+"");
		Log.i("params.entrySet",params.entrySet()+"");
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					params.size());
			
			for (Map.Entry<String, String> entry : params.entrySet()) {// 构建表单字段内容
				nameValuePairs.add(new BasicNameValuePair(entry.getKey(),
						MyConverter.escape(entry.getValue())));
			}
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response;
			response = httpclient.execute(httppost);
			InputStream in = response.getEntity().getContent();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int ch = 0;
			while ((ch = in.read()) != -1) {
				baos.write(ch);
			}
			byte[] data = baos.toByteArray();
			baos.close();
			return MyConverter.unescape(new String(data).trim());
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
}
