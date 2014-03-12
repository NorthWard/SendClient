package com.scut.sendclient.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.scut.sendclient.R;

/**
 * 通过登录或注册后，跳转到操作界面 负责添加监控用户和开始启动发送数据功能
 * 
 * @author 林培东、刘亮澎
 */
public class Send4Activity extends BaseActivity {
	Button bt;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send4);
		
	 bt = (Button) findViewById(R.id.send4_login);
		
		bt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			//	Send4Activity.this.bt.setBackgroundColor(Color.GRAY);
				Intent intent = new Intent();    
	            intent.setClass(Send4Activity.this, Send5Activity.class);   
	            Send4Activity.this.startActivity(intent);
	            Send4Activity.this.finish();
			}
		});
		
		
		}
		
}
