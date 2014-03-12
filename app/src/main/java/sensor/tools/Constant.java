package sensor.tools;

import java.util.UUID;

/**
 * 放置一些常量，方便统一修改
 * @author 林培东
 *
 */
public class Constant 
{
	//public static String IP = "192.168.1.102";
	
	public static String IP = "222.201.175.46";
	
	/* 本地的服务器地址 */
	public static String serverURL="http://"+IP+"/Server/";
	
	/* 新浪SAE服务器的地址 
	public static final String serverURL="http://testsensor.sinaapp.com/";
	*/
	
	//获取传感器数据的时间间隔
	public static final int TIME_FETCH=100;
	//向服务器发送数据的时间间隔
	public static final int TIME_SEND=1000;
	
	//更新时间间隔
	public static final int UPDATE_TIME=1000;
	//以下一组数据为老人所处的状态
	public static final int STATE_SIT = 1;
	public static final int STATE_SLEEP = 2;
	public static final int STATE_STAND = 3;
	public static final int STATE_WALK = 4;
	public static final int STATE_RUN = 5;
	
	/**
	 * sharepreference name
	 */
	public static final String SHARE_NAME = "actm";
	
	
	/**
	 * 设置发送频率
	 */
	public static final String SEND_RATE = "send_rate";

	public static final String ANALYSIS_RATE = "analysis_rate";
	public static final String MODE_APUD = "mode_apud";
	public static final String MAX_NO_EXCEPTION_DATA = "max_no_exception_data";
	public static final String DETECTION_AVG_COUNT = "detection_avg_count";
	public static final String DETECTION_AVG = "detection_avg";

	public static final String BLUETOOTH_ADDRESS = "address";
	
	public static final int DEFAULT_SEND_RATE = 3;
	public static final int DEFAULT_ANALYSIS_RATE = 2;

	public static final UUID SPP_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	public static final int REQUEST_CONNECT_DEVICE = 1;
	public static final int REQUEST_ENABLE_BT = 2;
	public static final int MESSAGE_READ = 2;
	public static final int SEND_SENSOR_FAIL = 3; // 发送传感器数据失败
	public static final int SEND_RESULT_FAIL = 4; // 发送分析结果失败
	
	public static String EXTRA_DEVICE_ADDRESS = "device_address";
}
