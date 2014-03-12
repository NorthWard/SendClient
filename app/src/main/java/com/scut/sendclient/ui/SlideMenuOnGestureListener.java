package com.scut.sendclient.ui;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class SlideMenuOnGestureListener extends  GestureDetector.SimpleOnGestureListener{
	private static final String TAG = "SlideMenuOnGestureListener"; 
	
	public static interface OnScrollListener{

		/**
		 * 
		 * @param e1 The first down motion event that started the scrolling.
		 * @param e2 The move motion event that triggered the current onScroll.
		 * @param distanceX The distance along the X axis that has been scrolled since the last call to onScroll.
		 *   	 This is NOT the distance between e1 and e2.
		 * @param distanceY The distance along the Y axis that has been scrolled since the last call to onScroll. 
		 * 		This is NOT the distance between e1 and e2.
		 */
		public void onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY);
	}
	private float slideDistance =50;
	private float startX;
	private boolean hasInformSlide =false;
	
	public static final int LEFT = 1;
	public static final int RIGHT = -1;
	public static final int NO_SCROLL = 0;
	private int dir =0;
	
	
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2,
			float distanceX, float distanceY) {
		Log.i(TAG,"onScroll");
		if(Math.abs(e2.getRawX()-startX)>slideDistance){
			if(e2.getRawX()-startX<0){
				//scroll left
				dir = LEFT;
			}else{
				//scroll right
				dir = RIGHT;
			}
			hasInformSlide = true;
		}
		mOnScrollListener.onScroll(e1, e2, distanceX, distanceY);
		return false;
	}
	private float velocityX ;
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2,
			float velocityX, float velocityY) {
		Log.i(TAG,"onFling");
		this.velocityX = velocityX;
		if(!hasInformSlide){
			if(Math.abs(e2.getRawX()-startX)>slideDistance){
				if(e2.getRawX()-startX<0){
					//scroll left
					dir = LEFT;
				}else{
					//scroll right
					dir = RIGHT;
				}
			}else{
				//以极快的速度滑动，也能够触发scroll 的限制
				if(Math.abs(velocityX*0.3)>slideDistance){
					if(velocityX<0){
						//scroll left
						dir = LEFT;
					}else{
						//scroll right
						dir = RIGHT;
					}
				}
			}
		}
		return false;
	}
	

	@Override
	public boolean onDown(MotionEvent e) {
		startX = e.getRawX();
		hasInformSlide = false;
		velocityX = 10;
		Log.i(TAG,"onDown");
		return false;
	}

	
	OnScrollListener mOnScrollListener = new OnScrollListener() {
		@Override
		public void onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			
		}
	};
	public OnScrollListener getOnScrollListener() {
		return mOnScrollListener;
	}

	public void setOnScrollListener(OnScrollListener mOnScrollListener) {
		this.mOnScrollListener = mOnScrollListener;
	}

	
	public int getDirection(){
		return dir;
	}
	
	public float getSlideDistance() {
		return slideDistance;
	}

	public void setSlideDistance(float slideDistance) {
		this.slideDistance = slideDistance;
	}


	public float getVelocityX() {
		if(velocityX<=0){
			return 100;
		}
		return velocityX;
	}
}


