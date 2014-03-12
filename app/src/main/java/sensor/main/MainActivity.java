package sensor.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.scut.sendclient.R;
import com.scut.sendclient.ui.TestActivity;

import java.util.HashMap;
import java.util.Map;

import sensor.tools.Constant;
import sensor.tools.HttpUploadUtil;

/**
 * 整个程序的入口类，有登录、注册、退出功能
 * @author 林培东
 */
public class MainActivity extends Activity 
{
	private EditText	m_username;
	private EditText	m_password;
	private EditText	m_password2;
	
	private String		username;
	private String		password;
	private String		password2;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
		Button loginButton = (Button) findViewById(R.id.login);
		Button registButton = (Button) findViewById(R.id.regist);
		Button quitButton = (Button) findViewById(R.id.quit);
		final EditText ip = (EditText)findViewById(R.id.ip);				//输入ip
		SharedPreferences sp=MainActivity.this.getSharedPreferences("actm", Context.MODE_PRIVATE);
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
					SharedPreferences sp=MainActivity.this.getSharedPreferences("actm", Context.MODE_PRIVATE);
					SharedPreferences.Editor editor=sp.edit();
					editor.putString("ip",ip.getText().toString());
					Constant.IP=ip.getText().toString();
					Constant.serverURL = "http://"+Constant.IP+":8080"+"/Server/";
					editor.commit();
				}
			}
		});
		
		quitButton.setOnClickListener(new Button.OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				MainActivity.this.finish();
			}
		});
		
		/* 临时设置 */
		loginButton.setOnClickListener(new Button.OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, TestActivity.class);
				startActivity(intent);
				MainActivity.this.finish();
			}
		});
		
//		loginButton.setOnClickListener(new Button.OnClickListener() 
//		{
//			@Override
//			public void onClick(View v)
//			{
//				LayoutInflater factory = LayoutInflater.from(MainActivity.this);
//				//得到自定义对话框
//                final View DialogView = factory.inflate(R.layout.logindialog, null);
//                
//                //创建登录框
//                AlertDialog dlg = new AlertDialog.Builder(MainActivity.this)
//                .setTitle("登录")
//                .setView(DialogView)//设置自定义对话框的样式
//                .setPositiveButton("确定", //设置“确定"按钮
//                		new DialogInterface.OnClickListener()
//                		{
//							@Override
//							public void onClick(DialogInterface dialog, int whichButton)
//                			{         		        
//                		        m_username = (EditText) DialogView.findViewById(R.id.username);
//                		        m_password = (EditText) DialogView.findViewById(R.id.password);
//
//                				username = m_username.getText().toString();
//                				password = m_password.getText().toString();
//                				connectServer("loginAncAccount.jsp");
//                			}
//                		}
//                )
//                .setNegativeButton("取消", //设置“取消”按钮
//    	                new DialogInterface.OnClickListener() 
//    	                {
//    	                    @Override
//							public void onClick(DialogInterface dialog, int whichButton)
//    	                    {
//    	        				dialog.dismiss();
//    	                    }
//    	                })
//    	                .create();
//
//                
//                dlg.show();
//			}
//		});
		
		registButton.setOnClickListener(new Button.OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				LayoutInflater factory = LayoutInflater.from(MainActivity.this);
				//得到自定义对话框
                final View DialogView = factory.inflate(R.layout.registdialog, null);
                
                //创建登录框
                AlertDialog dlg = new AlertDialog.Builder(MainActivity.this)
                .setTitle("注册")
                .setView(DialogView)//设置自定义对话框的样式
                .setPositiveButton("确定", //设置“确定"按钮
                		new DialogInterface.OnClickListener()
                		{
							@Override
							public void onClick(DialogInterface dialog, int whichButton)
                			{         
                		        m_username = (EditText) DialogView.findViewById(R.id.username);
                		        m_password = (EditText) DialogView.findViewById(R.id.password);
                		        m_password2 = (EditText) DialogView.findViewById(R.id.password2);

                				username = m_username.getText().toString();
                				password = m_password.getText().toString();
                				password2 = m_password2.getText().toString();
                				if(password.equals(password2))
                					connectServer("regAncAccount.jsp");
                				else
                					Toast.makeText(MainActivity.this, "密码不一致！", Toast.LENGTH_LONG).show();               				
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
		
		handler = new Handler(getMainLooper()){
 
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case 1:
					Toast.makeText(MainActivity.this, msg.getData().getString("msg"), Toast.LENGTH_LONG).show();
					break;

				default:
					break;
				}
			}
			
		};
    }
    private Handler handler;
    private void connectServer(String style) 
    {
    	final String url=Constant.serverURL+style;
    	final ProgressDialog loginDialog = ProgressDialog.show(MainActivity.this, "请等待...",
				"下载中...", true);
		new Thread() {
			public void run() {
		    	
		    	//保存参数
				final Map<String,String> params=new HashMap<String,String>();		
				params.put("params1", username);
				params.put("params2", password);
				
				//获取SharedPreferences，将用户名保存起来，方便以后使用
				SharedPreferences sp=MainActivity.this.getSharedPreferences("actm", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor=sp.edit();
				editor.putString("uname",username);
				editor.commit();
				
				/**
				 * 发送数据，这里最好用多线程来处理
				 * 因为如果发生错误的话整个界面就没有响应
				 */
				try
				{
					String msgStr=HttpUploadUtil.postWithoutFile(url, params);//将url和参数传给jsp
//					Toast.makeText(MainActivity.this, msgStr, Toast.LENGTH_LONG).show();
					System.out.println(msgStr);//测试用
					Message msg = new Message();
					msg.what = 1;
					Bundle b = new Bundle();
					b.putString("msg", msgStr);
					msg.setData(b);
					handler.sendMessage(msg);
					//登录或者注册成功则跳转到另一个界面
					if(msgStr.equals("登录成功") || msgStr.equals("注册成功"))
					{
						//跳转
						Intent intent=new Intent();
						intent.setClass(MainActivity.this, TestActivity.class);
						startActivity(intent);
						MainActivity.this.finish();
					}
				} catch(Exception e){
					e.printStackTrace();
					
				}finally{
					loginDialog.dismiss();
				}
		    }
		}.start();
    }
    
}