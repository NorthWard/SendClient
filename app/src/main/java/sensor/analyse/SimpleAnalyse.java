package sensor.analyse;

import java.util.ArrayList;
import java.util.HashMap;

public class SimpleAnalyse 
{
	public ArrayList<HashMap<String,Integer>> data;
	public int size; //一共有多少组数据
	int table[][]=new int[10][6]; //要同时更改
	
	public SimpleAnalyse(ArrayList<HashMap<String,Integer>> list) 
	{
		data=list;
		size=list.size();
		for(int i=0;i<size;i++)
		{
			HashMap<String,Integer> map=list.get(i);
			table[i][0]=
					map.get("accX");
			table[i][1]=map.get("accY");
			table[i][2]=map.get("accZ");
			table[i][3]=map.get("gyroX");
			table[i][4]=map.get("gyroY");
			table[i][5]=map.get("gyroZ");
		}
	}
	
	public void printData()
	{
		String str="";
		for(int i=0;i<6;i++)
		{
			for(int j=0;j<size;j++)
				str=str+table[j][i]+" ";
			
			str=str+"   ";
		}
		System.out.println(str);
	}
	
	public String analyse(){
		return "It is no result!";
	}
}
