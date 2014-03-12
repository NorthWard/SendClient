package com.send.houzhi.trans.readxml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;



public class ReadSensorByteXml{
	
	/**
	 * 读取文件，需要InputStream 
	 * @param inStream
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static TreeMap<String, Byte>readSensorByteXml(InputStream inStream) throws SAXException, IOException, ParserConfigurationException{ 
		
        SAXParserFactory spf = SAXParserFactory.newInstance(); // 初始化sax解析器  
        SAXParser sp = spf.newSAXParser(); // 创建sax解析器  
        //XMLReader xr = sp.getXMLReader();// 创建xml解析器  
        XMLContentHandler handler = new XMLContentHandler();  
        sp.parse(inStream, handler);  
        return handler.getByteMap();  
    }  
	
	/**
	 * 读取文件，不需要InputStream 
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static TreeMap<String, Byte> readSensorByteXml(String name) throws SAXException, IOException, ParserConfigurationException {//类装载器  
	    
		//装载
		InputStream inputStream = ReadSensorByteXml.class.getClassLoader().getResourceAsStream(name);
		return readSensorByteXml(inputStream);
	}
	
	/**
	 * 读取文件，不需要InputStream 
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static TreeMap<String, Byte> readSensorByteXml2(String name) throws SAXException, IOException, ParserConfigurationException {//类装载器  
	    
		//装载
		InputStream inputStream = new FileInputStream(name);
		return readSensorByteXml(inputStream);
	}
	/**
	 * 读取文件，需要InputStream  ，自配map
	 * @param inStream  输入流
	 * @param TreeMap   
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static void readSensorByteXml(InputStream inStream,TreeMap<String, Byte> TreeMap) throws SAXException, IOException, ParserConfigurationException{ 
		
        SAXParserFactory spf = SAXParserFactory.newInstance(); // 初始化sax解析器  
        SAXParser sp = spf.newSAXParser(); // 创建sax解析器  
        //XMLReader xr = sp.getXMLReader();// 创建xml解析器  
        XMLContentHandler handler = new XMLContentHandler(TreeMap);  
        sp.parse(inStream, handler);
    }  
	
	/**
	 * 读取文件，读取文件，需要InputStream  ，自配map，自配map
	 * @param TreeMap
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static void readSensorByteXml(TreeMap<String, Byte> TreeMap,String name) throws SAXException, IOException, ParserConfigurationException {//类装载器  
	    
		//装载
		InputStream inputStream = ReadSensorByteXml.class.getClassLoader().getResourceAsStream(name);
		readSensorByteXml(inputStream,TreeMap);
	}    
}
