package com.scut.sendclient.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.scut.sendclient.R;


public class Send2Activity extends BaseActivity {
	Button bt;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send2);
		 bt = (Button) findViewById(R.id.send2_sure);
		bt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				bt.setBackgroundColor(Color.GRAY);
				Intent intent = new Intent();    
	            intent.setClass(Send2Activity.this, Send3Activity.class);   
	            Send2Activity.this.startActivity(intent);  
	            Send2Activity.this.finish();
			}
		});
		}
		
}
