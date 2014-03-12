package com.send.houzhi.trans.transfer;


import android.util.Log;

import com.send.houzhi.trans.tools.SensorDataBytes;
import com.send.houzhi.trans.tools.SensorDataBytesC;
import com.send.houzhi.trans.tools.SensorDataBytesS;
import com.send.houzhi.trans.tools.StaticFinalVariable;
import com.send.houzhi.trans.tools.Transation;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;


public class DataTransIn {

	private byte []b;
	private SensorDataBytes sensorDataBytes=null;
	private Set<String> setTemp;
	
	
	/**
	 * @param name 表示存储文件字节的名称
	 * @param isClient 表示是服务器在用还是客户端调用
	 */
	public DataTransIn(String xmlname,boolean isClient){
		System.out.println("DataTransIn:hello1");
		if(isClient){
			sensorDataBytes=SensorDataBytesC.getInstance(xmlname);
		}
		else{
			System.out.println("hello3");
			sensorDataBytes=SensorDataBytesS.getInstance(xmlname);
			System.out.println("hello3");
		}
		System.out.println("hello2");
		//按照numSizeTable的顺序导入到map 中，没个map都是默认的顺序，所以能够保持所有的顺序一致
		setTemp= sensorDataBytes.getKeySet() ;
	}
	/**
	 * 把数据传入其中，函数是为了之后将服务器中的数据传入手机接收端而用。
	 * @param treeMap
	 * @param id
	 * @param gzip
	 * @param response
	 * @throws DigitException
	 * @throws IOException
	 */
    public void setDateToRes(TreeMap<String, Integer> treeMap,String id,boolean gzip,HttpServletResponse response) throws DigitException, IOException{
    	InDataTobyteArr(treeMap,StaticFinalVariable.BYTE_TO_CLIENT_XML_NAME_S); 
    	addIdToArr(id);
    	compressCon(gzip);
    	OutputStream outputStream= response.getOutputStream();
    	outputStream.write(b);
    }
	 /**
	  * 把byte数据传到服务器的具体传送部分
	  * @param b 数据字节数组
	  * @param id
	  * @param gzip
	  * @param url
	  * @return
	  */
    public boolean transDate(byte b[],String id,boolean gzip,String url) throws DigitException{
    	
    	this.b = b;
    	addIdToArr(id);   //
    	compressCon(gzip);
    	Log.e("intransdate","1");
    	return transDate(url);
    }
    /**
     * 把数据传到服务器
     * @param treeMap 数据map
     * @param id 四位整数的id号
     * @param isZip  0表示不压缩，其他的表示压缩
     * @param url ip 和端口号
     */
    public boolean syncDataToServer(TreeMap<String, Integer> treeMap,String id,boolean gzip,String url) throws DigitException{
    	//变成字节数组
    	InDataTobyteArr(treeMap,StaticFinalVariable.BYTE_STORY_XML_NAME_C); 
    	addIdToArr(id);   //
    	compressCon(gzip);
    	
    	return transDate(url);
    }
    /**
     * 自定义压缩下届
     * @param treeMap
     * @param minlength 压缩下届
     * @throws DigitException
     */
    public void syncDataToServer(TreeMap<String, Integer> treeMap,String id,long minlength,String url) throws DigitException{
    	
    	if(sensorDataBytes.getSumByte()>minlength){
    		syncDataToServer(treeMap, id,true,url);
    	}
    	else{
    		syncDataToServer(treeMap,id,false,url);
    	}
    }
    /**
     * 按照系统自定义的量去决定是否压缩
     * @param treeMap
     * @throws DigitException
     * @param url ip地址
     */
    public void syncDataToServer(TreeMap<String, Integer> treeMap,String id,String url) throws DigitException{
    	if(sensorDataBytes.getSumByte()>StaticFinalVariable.MIN_LENGTH){
    		syncDataToServer(treeMap,id, true,url);
    	}
    	else{
    		syncDataToServer(treeMap, id,false,url);
    	}
    }
    
	/**
	 * 输入数据，把所有的数据转换成byte [] 
	 * @param treeMap 输入的数据为TreeMap<String,Integer>,表示各项数据的值
	 */
	private void InDataTobyteArr( TreeMap<String,Integer> treeMap,String xmlName) {
		
		//暂时由Arraylist 存储着，以免到时候能够修改哪些存储大小的值
		ArrayList<Byte> blist=new ArrayList<Byte>(treeMap.size()*2);
		Integer i;
		
		for(String name:setTemp){      //同样由传感器传送数据字节表 来传送
			i=treeMap.get(name);
			if(i==null){
				i=0;
			}
			b=Transation.intToBytes2(i, sensorDataBytes.getByte(name));
			for(int j=0;j!=b.length;++j){
				blist.add(b[j]);
			}
		}
		byte r[]= new byte [blist.size()];
		for(int  j=0;j!=blist.size();++j){
			r[j]=blist.get(j);
		}
		b=r;
	}
    /**
     * 把byte数据传到服务器的具体传送部分
     * @param url 服务器地址
     */
    private boolean transDate(String url){
    	System.out.println(url);
    	HttpPost request = new HttpPost(url);
//    	for(int i=0;i!=b.length;++i){
//			System.out.println(b[i]);
//		}
    	ByteArrayEntity byteArrayEntity = new ByteArrayEntity(b);
    	boolean isOk=false;
		try {
			request.setEntity(byteArrayEntity);
			HttpResponse response = new DefaultHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode()==200){
				isOk=true;
				System.out.println("isok1");
			}
			Log.e("e",response.getStatusLine().getStatusCode()+"");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getStackTrace());
		}
		System.out.println("isok11  "+isOk);
		return isOk;
    }
  

    /**
     * 压缩并且加上数据大小
     * @param gzip
     * @throws DigitException
     */
    private void compressCon(boolean gzip) throws DigitException{
    	byte t[],te[];
       	if(gzip){  //如果是需要压缩
    		t = Transation.dataCompress(b);
    		b=new byte [t.length+StaticFinalVariable.TRANS_DATA_BYTES+1];
    		b[0]=1;
    	}
    	else{
    		t = b;
    		b=new byte [t.length+StaticFinalVariable.TRANS_DATA_BYTES+1];
    		b[0]=0;
    	}
    	if(t.length>=Math.pow(2, StaticFinalVariable.TRANS_DATA_BYTES*8)){
			throw new DigitException("压缩长度超过预定的位数");
		}
    	//加上数据大小
    	te=Transation.intToBytes2(t.length, StaticFinalVariable.TRANS_DATA_BYTES);
    	System.arraycopy(te, 0, b, 1, StaticFinalVariable.TRANS_DATA_BYTES);
    	System.arraycopy(t, 0, b, 1+StaticFinalVariable.TRANS_DATA_BYTES, t.length);
    }
    
    /**
     * 把i加入b
     * @param i
     */
    private void addIdToArr(String i){
    	int id= Integer.parseInt(i);
       	byte t[],te[];
    	t =Transation.intToBytes2(id, StaticFinalVariable.ID_BYTES);
    	te = b;
    	b =new byte[t.length+b.length]; 
    	System.arraycopy(t, 0, b, 0, StaticFinalVariable.ID_BYTES);
    	System.arraycopy(te, 0, b, t.length,te.length);
    }
}
