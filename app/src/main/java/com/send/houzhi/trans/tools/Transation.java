package com.send.houzhi.trans.tools;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author houzhi
 * 
 */
public class Transation {
	
	/**
	 * 数据压缩
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] dataCompress(byte[] data) {
		GZIPOutputStream gos;
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			gos = new GZIPOutputStream(baos);

			byte[] buf = new byte[1024];
			int num;
			while ((num = bais.read(buf)) != -1) {
				gos.write(buf, 0, num);
			}
			gos.finish();
			gos.flush();
			gos.close();
			byte[] output = baos.toByteArray();
			return output;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 数据解压缩
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] dataDecompress(byte[] data) {
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			GZIPInputStream gis = new GZIPInputStream(bais);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int num;
			while ((num = gis.read(buf)) != -1) {
				baos.write(buf, 0, num);
			}
			gis.close();
			byte[] ret = baos.toByteArray();
			baos.close();
			return ret;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 将long 型转化成byte[]
	 * @param n
	 * @param digs
	 * @return
	 */
	public static byte[] longToBytes(long n,int digs){
		byte[] b = new byte[digs];
		Long temp=n;
		Long t=null;
		
		for (int i = 0; i < digs; i++) {
			t = (temp >> ((digs-1)*8-i * 8));
			b[i]= t.byteValue();
			
		}
		return b;
	}
	/**
	 * byte 数组转换成int
	 * @param b  数组
	 * @param from  起始位置
	 * @param to  结束位置   
	 * 包括from 点和不包括to 点，from<= ,< to;
	 * @return
	 */
	public static long byteToLong(byte[] b,int from ,int to) {
		long n = 0;
		int isPositive = b[from];
		//负数的话，先将n赋值为-1。
		if(isPositive<0){
			n = -1;
		}
		int mask = 0xff;
		int temp = 0;
		for (int i = from; i < to; i++) {
			n <<= 8;
			temp = b[i] & mask;
			n |= temp;
		}
		return n;
	}
	
	
	/**
	 * int 转换成byte数组，并且只留固定位数，把不需要的切除
	 * 
	 * @param n  需要转换的数
	 * @param digs 转换成byte数组的位数  应该有digs<=4
	 * @return
	 */
	public static byte[] intToBytes2(int n,int digs) {
		byte[] b = new byte[digs];
		Integer temp=n;
		Integer t=null;
		
		for (int i = 0; i < digs; i++) {
			t = (temp >> ((digs-1)*8-i * 8));
			b[i]= t.byteValue();
			
		}
		return b;
	}

	/**
	 * byte 数组转换成int
	 * @param b
	 * @return
	 */
	public static int byteToInt2(byte[] b,int digs) {
		//查看是否是小于0
		int n = 0;
		int isPositive = b[0];
		if(isPositive<0){
			n = -1;
		}
		int mask = 0xff;
		int temp = 0;
		for (int i = 0; i < digs; i++) {
			n <<= 8;
			temp = b[i] & mask;
			n |= temp;
		}
		return n;
	}
	/**
	 * byte 数组转换成int
	 * @param b  数组
	 * @param from  起始位置
	 * @param to  结束位置   
	 * 包括from 点和不包括to 点，from<= ,< to;
	 * @return
	 */
	public static int byteToInt2(byte[] b,int from ,int to) {
		int n = 0;
		int isPositive = b[from];
		//负数的话，先将n赋值为-1。
		if(isPositive<0){
			n = -1;
		}
		int mask = 0xff;
		int temp = 0;
		for (int i = from; i < to; i++) {
			n <<= 8;
			temp = b[i] & mask;
			n |= temp;
		}
		return n;
	}
	public  static void  main(String [] args){
		System.out.println("hello");
		
		int temp =2000;
		int result=Transation.byteToInt2(Transation.intToBytes2(temp,4),4);
		System.out.println(result);
		Long ts= 22222222222l;
		System.out.println(ts.doubleValue());
		long t = 2000000000000000l;
		byte[] b = Transation.longToBytes(t, 8);
		System.out.println(Arrays.toString(b));
		
	}
}
