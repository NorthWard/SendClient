package com.scut.sendclient.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.scut.sendclient.R;

/**
 * 通过登录或注册后，跳转到操作界面 负责添加监控用户和开始启动发送数据功能
 * 
 * @author 林培东、刘亮澎
 */
public class Send3_1Activity extends BaseActivity {
	Button bt2,bt;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send3_1);
		 bt = (Button) findViewById(R.id.send3_1_button1);
		 bt2 = (Button) findViewById(R.id.send3_1_button2);
		bt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				bt.setBackgroundColor(Color.GRAY);
				Intent intent = new Intent();    
	            intent.setClass(Send3_1Activity.this, Send4Activity.class);   
	            Send3_1Activity.this.startActivity(intent);  
	            Send3_1Activity.this.finish();
			}
		});
		
		bt2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				bt2.setBackgroundColor(Color.GRAY);
				Toast.makeText(Send3_1Activity.this, "获取支持", Toast.LENGTH_LONG).show();
			}
		});
		}
		
}
