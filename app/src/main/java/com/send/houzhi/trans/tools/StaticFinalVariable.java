package com.send.houzhi.trans.tools;

public final class StaticFinalVariable {
	/**
	 * 默认服务器地址
	 */
	public static final String URL_Str = ":8080/Server/HelloWorldServlet";
	
	public static final String URL_MSG= ":8080/Server/ReturnMsg";
	/**
	 * 转换成字节时，总共的字节数 的数量占用的位数
	 */
	public static final int  TRANS_DATA_BYTES = 2;
	/**
	 * 存储传感器字节大小文件名称
	 */
	public static  final String BYTE_STORY_XML_NAME_S="D:\\eclipse workplace jee\\HelloWeb\\src\\sensor_byte.xml";
	
	/**
	 * 存储传感器字节大小文件名称
	 */
	public static  final String BYTE_STORY_XML_NAME_C="sensor_byte.xml";
	/**
	 * 存储服务器到接收端的文件名称
	 */
	public static final String BYTE_TO_CLIENT_XML_NAME_C="sensor_byte2.xml";
	
	/**
	 * 存储服务器到接收端的文件名称
	 */
	public static final String BYTE_TO_CLIENT_XML_NAME_S="D:\\eclipse workplace jee\\HelloWeb\\src\\sensor_byte2.xml";
	/**
	 * 最小压缩大小
	 */
	public static final long MIN_LENGTH=500;
	
	/**
	 * id 整数长度
	 */
	public static final int ID_BYTES= 4;
}
