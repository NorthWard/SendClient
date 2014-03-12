package com.scut.sendclient.ui;

import java.util.HashMap;
import java.util.Map;

import sensor.main.MainActivity;
import sensor.tools.Constant;
import sensor.tools.HttpUploadUtil;

import com.scut.sendclient.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetActivity extends BaseActivity {

	Button addson;
	public EditText son=null;
	public String sonId=null;//监控帐号的id
	public String username=null;//该用户名保存在SharePreferences中
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set);
		
		addson=(Button)findViewById(R.id.addson);
		/**
		 * 为父子之间添加一种映射关系，使得接收端有监控权限
		 */
		addson.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LayoutInflater factory = LayoutInflater.from(SetActivity.this);
				//得到自定义对话框
                final View DialogView = factory.inflate(R.layout.adddialog, null);
                
                //创建登录框
                AlertDialog dlg = new AlertDialog.Builder(SetActivity.this)
                .setTitle("注意")
                .setView(DialogView)//设置自定义对话框的样式
                .setPositiveButton("确定", //设置“确定"按钮
                		new DialogInterface.OnClickListener()
                		{
							@Override
							public void onClick(DialogInterface dialog, int whichButton)
                			{         		        
                		        son = (EditText) DialogView.findViewById(R.id.son);
                				sonId = son.getText().toString();
                				if(sonId.equals(""))
                					Toast.makeText(SetActivity.this, "输入不能为空", Toast.LENGTH_LONG).show();
                				else
                					connectServer("addSon.jsp");
                			}
                		}
                )
                .setNegativeButton("取消", //设置“取消”按钮
    	                new DialogInterface.OnClickListener() 
    	                {
    	                    @Override
							public void onClick(DialogInterface dialog, int whichButton)
    	                    {
    	        				dialog.dismiss();
    	                    }
    	                })
    	                .create();

                
                dlg.show();
			}
		});
		//获取SharedPreferences，将用户名保存起来，方便以后使用
		SharedPreferences sp=getSharedPreferences(Constant.SHARE_NAME, Context.MODE_PRIVATE);
		username=sp.getString("uname", null);
		
		
		Button loginButton = (Button) findViewById(R.id.login);
		Button registButton = (Button) findViewById(R.id.regist);
		Button quitButton = (Button) findViewById(R.id.quit);
		final EditText ip = (EditText)findViewById(R.id.ip);				//输入ip
		ip.setText(sp.getString("ip", ""));
		String temp = sp.getString("ip", "");
		if(!temp.equals("")){
			Constant.IP=temp;
			Constant.serverURL = "http://"+Constant.IP+":8080"+"/Server/";
		}
		Button sureIp = (Button)findViewById(R.id.sure_ip);			//确认ip
		sureIp.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(ip.getText()!=null){
					SharedPreferences sp=SetActivity.this.
							getSharedPreferences(Constant.SHARE_NAME, Context.MODE_PRIVATE);
					SharedPreferences.Editor editor=sp.edit();
					editor.putString("ip",ip.getText().toString());
					Constant.IP=ip.getText().toString();
					Constant.serverURL = "http://"+Constant.IP+":8080"+"/Server/";
					editor.commit();
					Toast.makeText(SetActivity.this, "ip已确认", Toast.LENGTH_LONG).show();
				
				}
				else ip.setText("ip为null");
			}
		});
		
	}
	
	
	
	
	private void connectServer(String style) 
    {
    	final String url=Constant.serverURL+style;
    	//保存参数
		final Map<String,String> params=new HashMap<String,String>();		
		params.put("params1", username);
		params.put("params2", sonId);
		System.out.println(username+" "+sonId);//证明SharePreferences使用正确
		
		/**
		 * 发送数据，这里最好用多线程来处理
		 * 因为如果发生错误的话整个界面就没有响应
		 */
		try
		{
			String msgStr=HttpUploadUtil.postWithoutFile(url, params);//将url和参数传给jsp
			Toast.makeText(SetActivity.this, msgStr, Toast.LENGTH_LONG).show();
			System.out.println(msgStr);//测试用
		} catch(Exception e){
			e.printStackTrace();
		}
    }
}
