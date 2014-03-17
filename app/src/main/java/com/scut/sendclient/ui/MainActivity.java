package com.scut.sendclient.ui;


import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scut.sendclient.R;

import sensor.bluetooth.llp.BluetoothService;
import sensor.tools.Constant;

public class MainActivity extends ActivityGroup implements View.OnClickListener{
	RelativeLayout content ;
	private TextView title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		new MenuSlideSetLeft(this);
		
		content  = (RelativeLayout) findViewById(R.id.contentview);
		title = (TextView)findViewById(R.id.titlename);


		View v = findViewById(R.id.test);
		v.setOnClickListener(this);
		
		v = findViewById(R.id.set);
		v.setOnClickListener(this);
		
		v = findViewById(R.id.run);
		v.setOnClickListener(this);
		
		v = findViewById(R.id.exit);
		v.setOnClickListener(this);
		//测试是否已经有连接
		testConnect();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.test:
			//launchActivity(TAG_TEST, TestActivity.class,R.string.title_test);
			break;
		case R.id.set:
			launchActivity("set", SetActivity.class,R.string.title_set);
			break;
		case R.id.run:
			launchActivity("run", RunActivity.class,R.string.title_run);
			break;
		case R.id.exit:
			if(!BluetoothService.isNull())
				BluetoothService.getInstance(this, null).stop();
			android.os.Process.killProcess(android.os.Process.myPid());
			break;
		}
	}
	private void launchActivity(String id, Class<?> activityClass,int titleId) {
        content.removeAllViews();
        title.setText(titleId);
        Intent intent =  new Intent(MainActivity.this, activityClass);
        intent.putExtra(Constant.EXTRA_DEVICE_ADDRESS, address);
        
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        
        Window window = getLocalActivityManager().startActivity(id, intent);
        View view = window.getDecorView();
        content.addView(view);
    }
	private String address;
	private void testConnect() {
		
		SharedPreferences sp = getSharedPreferences(Constant.SHARE_NAME, Context.MODE_PRIVATE);
		address =sp.getString(Constant.EXTRA_DEVICE_ADDRESS, null);
		if(address==null){
			//启动到设置界面
			// launchActivity(TAG_TEST, TestActivity.class,R.string.title_test);
		}else{
			launchActivity("run", RunActivity.class,R.string.title_run);
		}
	}
	
	public static final String TAG_TEST = "test";
}
