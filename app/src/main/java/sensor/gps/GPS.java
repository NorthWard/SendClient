/**
 * @author houzhi
 * 获取gps数据信息
 */

package sensor.gps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

class GPS {
	/**
	 * 更新时间
	 */
	private long updateTime=60000;
	
	private LocationManager locationManager;
	/**
	 * 位置 Location
	 */
	private Location location = null;
	/**
	 * 调用的Activity
	 */
	private Activity myActivity;    
	
	/**
	 * 需要有一个Activity
	 */
	public GPS(Activity activity){
		myActivity=activity;
		locationManager = (LocationManager) myActivity.getSystemService(Context.LOCATION_SERVICE); 
		checkGPSSettings();
		gpsInitial();

	}
	/**
	 * 检查GPS设置
	 */
    private void checkGPSSettings() {  
        if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {  
            Toast.makeText(myActivity, "GPS模块正常", Toast.LENGTH_SHORT).show();  
            return;  
        }  
        Toast.makeText(myActivity, "请开启GPS！", Toast.LENGTH_SHORT).show();  
        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);  
        
		myActivity.startActivityForResult(intent,0); //此为设置完成后返回到获取界面  
  
    } 
    /**
     * 初始化
     */
    private void  gpsInitial(){
    	
        //查找到服务信息
        //criteria获取精确度的方法
        Criteria criteria=new Criteria();
        //位置解析的精度，高或低，参数
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //是否提供海拔高度信息
        criteria.setAltitudeRequired(true);
        //是否提供方向信息
        criteria.setBearingRequired(false);
        //是否允许运营商计费
        criteria.setCostAllowed(true);
        //电池消耗，无，低，中，高参数。power——low为低
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        //获取gps的信息
        String provider=locationManager.getBestProvider(criteria, true);
        //通过gps获取信息
         
        location=locationManager.getLastKnownLocation(provider);
        // 设置监听器，updateTime更新一次位置坐标，不考虑位置的变化。最后引用locationlistener并且实现次方法
        locationManager.requestLocationUpdates(provider, updateTime, 0,
                        new LocationListener(){
                        //坐标改变时触发此函数
                        @Override
                        public void onLocationChanged(Location location) {
                               GPS.this.location = location;
                        }
                        //provider被disable时触发此函数，比如gps被关闭
                        @Override
                        public void onProviderDisabled(String provider) {
                               
                        }
                        //provide被enable时触发此函数，比如gps被打开
                        @Override
                        public void onProviderEnabled(String provider) {
                                
                        }
                        //provider的转态在：可用，暂时不可用和无服务3个状态直接切换时触发此函数
                        @Override
                        public void onStatusChanged(String provider,
                                        int status, Bundle extras) {
                        	
                        }
                        });
    }
    /**
     * 设置更新时间
     * @param time  是long 型的
     */
    public void setUpdateTime(long time){
    	updateTime = time;
    }
    /**
     * 纬度
     * @return
     */
    public double getLatitude(){
    	if(location==null){
    		return 30.659259;
    	}
    	return location.getLatitude();
    }
    /**
     * 经度
     */
    public double getLongitude(){
    	if(location==null){
    		return 104.065762;
    	}
    	return  location.getLongitude();
    }
    /**
     * 高度
     */
    public double getAltitude(){
    	if(location==null){
    		return 0;
    	}
    	return location.getAltitude();
    }
    
    
    /**
     * 测试用的
     */
    public void doWork() { 
    	checkGPSSettings();
    	String msg="";
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);  
        criteria.setAltitudeRequired(true);  
        criteria.setBearingRequired(false);  
        criteria.setCostAllowed(true);  
        criteria.setPowerRequirement(Criteria.POWER_LOW);  
        String provider = locationManager.getBestProvider(criteria, false);  
 
        location = locationManager.getLastKnownLocation(provider);  
        if(location == null){
        	 Log.e("error","gps is null");
        	 Toast.makeText(myActivity, "location is null", Toast.LENGTH_SHORT).show();
         }
    }
}
