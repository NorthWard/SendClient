package sensor.analyse.selfdefine;

import android.util.Log;

import java.util.List;

public class DetectionAmplitude {
	public static final String TAG = "DetectionAmplitude";
	
	public static interface OnDetectingException{
		public void onDectectionException(String exception);
	}
	
	private OnDetectingException mOnDetectingException = new OnDetectingException() {
		
		@Override
		public void onDectectionException(String exception) {
			// TODO Auto-generated method stub
			System.out.println("onDectectionException");
		}
	};
	
	/**
	 * @return the mOnDetectingException
	 */
	public OnDetectingException getOnDetectingException() {
		return mOnDetectingException;
	}

	/**
	 * @param mOnDetectingException the mOnDetectingException to set
	 */
	public void setOnDetectingException(OnDetectingException mOnDetectingException) {
		this.mOnDetectingException = mOnDetectingException;
	}

	private MaxNoException maxNoException;
	public MaxNoException getMaxNoException(){
		return maxNoException;
	}
	public DetectionAmplitude(MaxNoException maxNoException){
		this.maxNoException = maxNoException;
	}
	public DetectionAmplitude(){
		maxNoException = new MaxNoException(MaxNoException.DEFAULT_VALUE);
	}
	/**
	 * 计算平方和的平均值
	 * @param vec
	 * @return
	 */
	public double countMod(List<Long> vec){
		
		double sum =0;
		if(avg==null){
			Log.i(TAG,"avg is null");
			for(Long f:vec){
				sum+=Math.pow(f, 2);
			}
		}else{
			if(vec.size()!=avg.size()){
				throw new RuntimeException("vec 与跟的数据长度不一样！");
			}
			Log.i(TAG,"avg is not null");
			Log.i(TAG,vec.size()+"!="+avg.size());
			
			for(int i=0;i!=vec.size();++i){
				sum+=Math.pow(vec.get(i)-avg.get(i), 2);
			}
		}
		return Math.sqrt(sum);
	}

	/**
	 * 
	 * @param vec 监测的数据
	 */
	public void doDectecting(List<Long> vec){
		double mod = countMod(vec);
		boolean exception = false;
		synchronized(maxNoException){
			Log.i(TAG,"mod:"+mod+">maxNoException" +
					".getMaxNoException():"+maxNoException.getMaxNoException()+"");
			if(mod>maxNoException.getMaxNoException()){
				exception = true;
			}
		}
		if(exception){
			if(mOnDetectingException!=null){
				mOnDetectingException.
				onDectectionException("mod:"+mod+">maxNoException.getMaxNoException():"+maxNoException.getMaxNoException()+"");
			}
		}
	}
	/**
	 * 通知报告了一个错误的异常。即本没有异常，却报告异常
	 */
	public void doSurplusInformErrorException(){
		synchronized(maxNoException){
			maxNoException.incCorrect();
		}
	}
	
	/**
	 * 本来出现异常了，却没有报告
	 */
	public void doLackInformErrorException(){
		synchronized(maxNoException){
			maxNoException.decCorrect();
		}
		
	}
	/**
	 * 去除平均值
	 */
	public void clearAvg(){
		avg = null;
	}
	/**
	 * 添加方式来设置平均值
	 * @param list
	 */
	public void addSetAvg(List<Long> list){
		if(avg==null){
			avg = new AvgCount(list);
		}else
			avg.add(list);
	}
	public void countAvg(){
		
	}
	private AvgCount avg;

	public AvgCount getAvgCount(){
		return avg;
	}
	public void setAvgCount(AvgCount avg){
		this.avg = avg;
	}
}
