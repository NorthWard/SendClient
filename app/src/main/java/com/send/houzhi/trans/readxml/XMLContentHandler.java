package com.send.houzhi.trans.readxml;

import java.util.TreeMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;



 

class XMLContentHandler extends DefaultHandler {
	private TreeMap<String, Byte>  bytemap;
	private final String ROOTNAME="sensor_bytes";
	private String name;
	private byte value;
	
	public TreeMap<String,Byte> getByteMap(){
		return bytemap;
	}
	public XMLContentHandler(){
		bytemap = new TreeMap<String, Byte>();
	}
	public XMLContentHandler(TreeMap<String, Byte>  TreeMap){
		bytemap = TreeMap;
	}
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
//		System.out.println("startE localName:"+localName+"@"+qName+"@"+uri);
		if(!ROOTNAME.equals(qName)){
			name = qName;
		
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		if(name!=null&&!ROOTNAME.equals(name)){
//			System.out.println(name+"@");
//			Log.e("error name",name);
			String v =new String (ch,start,length);
//			Log.e("error v",v);
//			System.out.println("v="+v+"@");
			int temp = 0;
			
			temp=Integer.parseInt(v);
			
			if(temp>127){
				throw new SAXException("字节数设置超过byte类型的范围");
			}
			value=(byte)temp;
		}
	}




	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
//		System.out.println("endE localName:"+localName+"@"+qName+"@"+uri);
		if(!ROOTNAME.equals(qName)){
			bytemap.put(name, value);
			name=null;
			
		}
	}
}
