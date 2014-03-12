package sensor.analyse.selfdefine;

import android.util.Log;

public class MaxNoException {
	public static float DEFAULT_VALUE = 1000f;
	public static final String TAG="MaxNoException";
	
	// 最大非异常值
	private float maxNoException;

	public MaxNoException(float maxNoException){
		this.maxNoException = maxNoException;
	}
	
	public float getMaxNoException() {
		return maxNoException;
	}

	public void setMaxNoException(float maxNoException) {
		this.maxNoException = maxNoException;
	}

	private float increateBase = 3f;
	/**
	 * @return the increateBase
	 */
	public float getIncreateBase() {
		return increateBase;
	}

	/**
	 * @param increateBase the increateBase to set
	 */
	public void setIncreateBase(float increateBase) {
		this.increateBase = increateBase;
	}
	
	private float decreateBase =3f;
	/**
	 * @return the decreateBase
	 */
	public float getDecreateBase() {
		return decreateBase;
	}

	/**
	 * @param decreateBase the decreateBase to set
	 */
	public void setDecreateBase(float decreateBase) {
		this.decreateBase = decreateBase;
	}
	
	public void incCorrect() {
		maxNoException *= (1 + 1 / increateBase);
		Log.i(TAG,"after inc :"+maxNoException);
	}

	/**
	 * 将最大非异常值减小
	 */
	public void decCorrect() {
		maxNoException *= (1 - 1 / decreateBase);
	}
}
