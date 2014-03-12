package com.scut.sendclient.ui;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.scut.sendclient.R;

import sensor.tools.Constant;


public class Send5Activity extends BaseActivity {
	private boolean click=true;
	Button bt;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send5);
		 final EditText ip = (EditText) findViewById(R.id.send5_editip);
	 bt = (Button) findViewById(R.id.send5_sureIP);
	
		bt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			
				if(ip.getText()!=null){
				//	Send5Activity.this.bt.setBackgroundColor(Color.GRAY);
					Toast.makeText(Send5Activity.this, "ip已确认,3秒钟后跳转", Toast.LENGTH_LONG).show();
					
					SharedPreferences sp=getSharedPreferences(Constant.SHARE_NAME, Context.MODE_PRIVATE);
					SharedPreferences.Editor editor=sp.edit();
					editor.putString("ip",ip.getText().toString());
					Constant.IP=ip.getText().toString();
					Constant.serverURL = "http://"+Constant.IP+":8080"+"/Server/";
					editor.commit();
					
					Intent intent = new Intent();    
		            intent.setClass(Send5Activity.this,Send6Activity.class);   
		            Send5Activity.this.startActivity(intent);
		            Send5Activity.this.finish();
				}
				else ip.setText("ip为null");
				
			}
		});
		
		
		}
		
}
