package sensor.analyse.selfdefine;

import java.util.ArrayList;
import java.util.List;

public class AvgCount{
	private List<Long> listAvg = new ArrayList<Long>();
	private int count =0;
	
	/**
	 * 
	 * @return 有多少组数据计算出来的。
	 */
	public int getNums(){
		return count;
	}
	
	public AvgCount(List<Long> list,int count){
		this.listAvg = list;
		this.count = count;
	}

	
	public int size(){
		return listAvg.size();
	}
	public Long get(int i){
		return listAvg.get(i);
	}
	public AvgCount(List<Long> list){
		for(int i=0;i!=list.size();++i)
			listAvg.add(list.get(i));
		count = 1;
	}
	
	public List<Long> getAvg(){
		return listAvg;
	}
	public void add(List<Long> list){
		if(list.size()!=listAvg.size()){
			throw new RuntimeException("list 长度不一致");
		}
		for(int i=0;i!=listAvg.size();++i){
			listAvg.set(i, (listAvg.get(i)*count+list.get(i))/(count+1));
		}
		count++;
	}
}
