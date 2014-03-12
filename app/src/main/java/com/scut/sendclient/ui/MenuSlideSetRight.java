package com.scut.sendclient.ui;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.scut.sendclient.R;

import java.util.concurrent.TimeUnit;

public class MenuSlideSetRight {
	final String TAG = "menuSlideSet";

	RelativeLayout content;
	LinearLayout menu;

	private boolean menuhasOpen = false;

	public MenuSlideSetRight(Activity activity) {
		content = (RelativeLayout) activity.findViewById(R.id.content);
		menu = (LinearLayout) activity.findViewById(R.id.listMenu);
		menuWidth = activity.getResources().getDimensionPixelOffset(
				R.dimen.menu_width);
		// listener to some blog ,it is the best to set real nums to params;
		ViewGroup.LayoutParams params = content.getLayoutParams();
		DisplayMetrics outMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		params.width = outMetrics.widthPixels;
		params.height = outMetrics.heightPixels;
		Log.i(TAG, params.width + "," + params.height);
		content.setLayoutParams(params);
		gestureListener = new SlideMenuOnGestureListener();
		content.setLongClickable(true);
		gestureListener
				.setOnScrollListener(new SlideMenuOnGestureListener.OnScrollListener() {
					@Override
					public void onScroll(MotionEvent e1, MotionEvent e2,
							float distanceX, float distanceY) { // distanceX
																// 是getX()
																// 相减的，即是相对控件的坐标
						Log.i(TAG, "distanceX:" + distanceX + "," + "e2-e1:"
								+ e2.getRawX() + "-" + e1.getRawX());
						int dis = (int) (e2.getRawX() - e1.getRawX());
						RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) content
								.getLayoutParams();
						if (dis < 0) {
							// left e2-e1<0
							if (-dis < menuWidth
									&& params.rightMargin < menuWidth) {
								params.rightMargin = -dis;
								params.leftMargin = dis;
								Log.i(TAG, "params.rightMargin:"
										+ params.rightMargin
										+ ",params.leftMargin:"
										+ params.leftMargin);
								content.setLayoutParams(params);
							} else {
								params.rightMargin = menuWidth;
								params.leftMargin = -menuWidth;
								Log.i(TAG, "params.rightMargin:"
										+ params.rightMargin
										+ ",params.leftMargin:"
										+ params.leftMargin);
								content.setLayoutParams(params);
							}
						} else {
							// right
							if (dis < menuWidth && params.rightMargin > 0) {
								params.rightMargin = menuWidth - dis;
								params.leftMargin = -(menuWidth - dis);
								Log.i(TAG, "params.rightMargin:"
										+ params.rightMargin
										+ ",params.leftMargin:"
										+ params.leftMargin);
								content.setLayoutParams(params);
							} else {
								params.rightMargin = 0;
								params.leftMargin = 0;
								Log.i(TAG, "params.rightMargin:"
										+ params.rightMargin
										+ ",params.leftMargin:"
										+ params.leftMargin);
								content.setLayoutParams(params);
							}
						}
					}
				});
		final GestureDetector detector = new GestureDetector(activity,
				gestureListener);

		content.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Log.i(TAG, "ontouchEvent");
				boolean result = detector.onTouchEvent(event);
				if (event.getAction() == MotionEvent.ACTION_UP) {
					// 结束
					switch (gestureListener.getDirection()) {
					case SlideMenuOnGestureListener.LEFT:
						// 启动向左的动画
						new UpdateMargin(MoveDirection.LEFT)
								.execute(gestureListener.getVelocityX());
						menuhasOpen = true;
						Log.i(TAG, "left end");
						break;
					case SlideMenuOnGestureListener.RIGHT:
						// 启动向右的动画
						new UpdateMargin(MoveDirection.RIGHT)
								.execute(gestureListener.getVelocityX());
						menuhasOpen = false;
						Log.i(TAG, "right end");
						break;
					case SlideMenuOnGestureListener.NO_SCROLL:
						Log.i(TAG, "no scroll");
						// 返回 恢复
						if (menuhasOpen) {
							new UpdateMargin(MoveDirection.LEFT)
									.execute(gestureListener.getVelocityX());
						} else {
							new UpdateMargin(MoveDirection.RIGHT)
									.execute(gestureListener.getVelocityX());
						}
						break;
					}
				}
				return result;
			}
		});
	}

	SlideMenuOnGestureListener gestureListener;

	private int menuWidth = 100;

	static enum MoveDirection {
		LEFT, RIGHT;
	}

	/**
	 * 
	 * @author houzhi
	 */
	class UpdateMargin extends AsyncTask<Float, Integer, Void> {

		final static long SLEEP_TIME = 50L;
		final static int MOVE_DISTANCE = 30;

		public MoveDirection direction;

		public UpdateMargin(MoveDirection direction) {
			this.direction = direction;
		}

		boolean isLeft;

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) content
					.getLayoutParams();
			layoutParams.leftMargin = values[0];
			layoutParams.rightMargin = values[1];
			content.setLayoutParams(layoutParams);
		}

		@Override
		protected Void doInBackground(Float... params) {
			RelativeLayout.LayoutParams layoutParams;
			int v = (int) (float) (params[0] / (100 / SLEEP_TIME));
			switch (direction) {
			case LEFT:
				// left
				while (true) {
					layoutParams = (RelativeLayout.LayoutParams) content
							.getLayoutParams();
					if (layoutParams.rightMargin >= menuWidth) {
						publishProgress(-menuWidth, menuWidth);
						break;
					}
					publishProgress(layoutParams.leftMargin - MOVE_DISTANCE,
							layoutParams.rightMargin + MOVE_DISTANCE);
					try {
						TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case RIGHT:
				// right
				while (true) {
					layoutParams = (RelativeLayout.LayoutParams) content
							.getLayoutParams();
					if (layoutParams.rightMargin - MOVE_DISTANCE <= 0) { // 保证下一次能够运行。
						publishProgress(0, 0);
						break;
					}
					publishProgress(layoutParams.leftMargin + MOVE_DISTANCE,
							layoutParams.rightMargin - MOVE_DISTANCE);
					try {
						TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			}
			return null;
		}

	}
}
