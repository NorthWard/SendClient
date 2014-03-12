/**
 * 将GPS 数据信息封装
 */

package sensor.gps;

import android.app.Activity;

public class GPSLocation{
	private static final long PREC_MOV_DIGTIPLY = (long) Math.pow(10, 14);
	
	private static final int MOV_DIG = 32;

    

	private GPS gps;
	/**
	 * 单一实例
	 */
	private static GPSLocation gpsLocation=null;
	/**
	 * 需要有一个Activity
	 */
	private GPSLocation(Activity activity){
		gps = new GPS(activity);
		
	}
	public static GPSLocation getInstance(Activity activity){
		if(gpsLocation == null){
			gpsLocation = new GPSLocation(activity);
		}
		
		return gpsLocation;
	}
	
   
    /**
     * 设置更新时间
     * @param time  是long 型的
     */
    public void setUpdateTime(long time){
    	gps.setUpdateTime(time);
    }
    /**
     * 把double  变成long
     * @param d
     * @return  
     */
    public static long double2long(double d){
    	long t= (long)(d*PREC_MOV_DIGTIPLY);
    	
    	return t;
    }
    
    
    /**
     * 把long  变成double
     * @param d
     * @return  
     */
    public static double long2double(long l){
    	double d= (double)l/PREC_MOV_DIGTIPLY;
    	
    	return d;
    }
    /**
     * 纬度
     * @return
     */
    public double getLatitude(){
    	return gps.getLatitude();
    }
    
    /**
     * 经度
     */
    public double getLongitude(){
    	return  gps.getLongitude();
    }
    /**
     * 高度
     */
    public double getAltitude(){
    	return  gps.getAltitude();
    }
    
    /**
     * 纬度
     * @return
     */
    public long getLatitudeT(){
    	return double2long(gps.getLatitude());
    }
    
    /**
     * 经度
     */
    public long getLongitudeT(){
    	return  double2long(gps.getLongitude());
    }
    /**
     * 高度
     */
    public long getAltitudeT(){
    	return  double2long(gps.getAltitude());
    }
    
    
    /**
     * 纬度 高32位
     * @return
     */
    public int getLatitudeHigh(){
    	return getHighPartOtherDig(gps.getAltitude());
    }
    /**
     * 纬度低32位
     * @return
     */
    public int getLatitudeLow(){
    	return getLowPart8Dig(gps.getAltitude());
    }
    /**
     * 经度高32位
     */
    public int getLongitudeHigh(){
    	return  getHighPartOtherDig(gps.getLongitude());
    }
    /**
     * 经度低32位
     */
    public int getLongitudeLow(){
    	
    	return  getLowPart8Dig(gps.getLongitude());
    }
    
    /**
     * 重新获取纬度
     */
    public double recoverAlti(int h,int l){
    	 
    	return recoverFromSplit(h, l);
    }
    
    /**
     * 重新获取纬度
     */
    public double recoverLongi(int h,int l){
    	
    	return  (recoverFromSplit(h, l));
    }
    /**
     * 重新获取纬度
     */
    public double getAlti(int h,int l){
    	return recoverFromSplit(h, l);
    }
	/** 
	 *  返回 经度，纬度，高度
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "经度："+gps.getLongitude()+"\n纬度："+gps.getLatitude()+"\n高度："+gps.getAltitude();
	}
    
    /**
     * 测试用的
     */
    public void doWork() { 
    	gps.doWork();
    }
    /**
     * 获取double 的低（MOV_DIG）位
     * @param a
     * @return
     */
    private int getLowPart8Dig(double a){
    	long t= (long)(a*PREC_MOV_DIGTIPLY);
    	int b =(int)( t );
    	return b;
    }
    /**
     * 获取double 的高32位
     * @param a
     * @return
     */
    private int getHighPartOtherDig(double a){
    	long t= (long)(a*PREC_MOV_DIGTIPLY);
    	int b =(int)( t >> MOV_DIG);
    	return b;
    }
    /**
     * 将拆分的两位数，恢复成double
     * @param h 高
     * @param l 低
     * @return  
     */
    private double recoverFromSplit(int h,int l){
    	long t1 = h<<32;
    	long t2 = l;
    	long mask = 0xffff;
    	long t3 = t2 & mask;
    	double r = (double)(t3+t2) /PREC_MOV_DIGTIPLY;
    	return r;
    }
}
