package com.send.houzhi.trans.tools;

import com.send.houzhi.trans.readxml.ReadSensorByteXml;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;

public class SensorDataBytesC extends SensorDataBytes{
	/**
	 * 存储着每个传感器的传送数据的大小
	 */
	private TreeMap<String, Byte> numSizeTable =null;
	/**
	 * 总共的byte 数
	 */
	private  long byteSums=0;
	
	/**
	 * 多例模式
	 */
	private static HashMap<String, SensorDataBytesC>  sensorDataBytesMap=new HashMap<String, SensorDataBytesC>(); 
	
	/**
	 * 
	 * @return 按照TreeMap 所示的总共需要的字节数
	 */
	public long getSumByte(){
		return byteSums;
	}
	
	/**
	 * 
	 * @param name  传感器名称
	 * @return
	 */
	public byte getByte(String name){
		return numSizeTable.get(name);
	}
	
	/**
	 * 传会传感器名称set
	 * @return
	 */
	public Set<String> getKeySet(){
		return numSizeTable.keySet();
	}
	
	/**
	 * 
	 * @param name   存储每个传感器数据大小的名称
	 */
	private SensorDataBytesC(String name){
		super();
		try {
			numSizeTable=ReadSensorByteXml.readSensorByteXml(name);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Set<String> temp=numSizeTable.keySet();
		for(String n :temp){
			byteSums+=numSizeTable.get(n );
		}
		System.out.println("字节表："+numSizeTable);
	}
	/**
	 * 获取实例
	 * @param name 表示存储的表的名称  必须得是xml
	 * @return  实例
	 */
	public static SensorDataBytes getInstance(String name){
		if(sensorDataBytesMap.get(name)==null){
			sensorDataBytesMap.put(name,new SensorDataBytesC(name));
		}
		return sensorDataBytesMap.get(name);
	}
}
