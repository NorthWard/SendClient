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
public class Send3Activity extends BaseActivity {
	Button bt,bt2;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send3);
		 bt = (Button) findViewById(R.id.send3_button1);
		 bt2 = (Button) findViewById(R.id.send3_button2);
		bt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
		//		Send3Activity.this.bt.setBackgroundColor(Color.GRAY);
				Intent intent = new Intent();    
	            intent.setClass(Send3Activity.this, Send3_1Activity.class);   
	            Send3Activity.this.startActivity(intent);  
	            Send3Activity.this.finish();
			}
		});
		
		bt2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
	//			Send3Activity.this.bt.setBackgroundColor(Color.GRAY);
				Intent intent = new Intent();    
	            intent.setClass(Send3Activity.this, Send4Activity.class);   
	            Send3Activity.this.startActivity(intent); 
	            Send3Activity.this.finish();
			}
		});
		}
		
}
