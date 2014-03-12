package com.scut.sendclient.ui;

import com.scut.sendclient.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class SplashActivity extends BaseActivity {
	private ImageView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		title = (ImageView) findViewById(R.id.title);

		title.startAnimation(new TranslateAnimation(0, 60, 0, 60));
		final Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if (msg.what == 1) {
					Intent intent = new Intent(SplashActivity.this,
							Send2Activity.class);
					SplashActivity.this.startActivity(intent);
					finish();
				}
			}

		};

		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			}
		}, 2000);
	}

}
