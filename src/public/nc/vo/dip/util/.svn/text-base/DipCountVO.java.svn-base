package nc.vo.dip.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DipCountVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1220207744644062414L;
	public DipCountVO() {
	}
	private Integer count=0;
	private Integer scount=0;
	private Integer fcount=0;
	private Integer mcount=0;//报文个数
	private Map<String,DipSuccOrFailClass> classverfiy;
	public void setAddCount(){
		if(count==null){count=0;}
		this.count=count+1;
	}
	public void setAddScount(){
		if(scount==null){scount=0;}
		this.scount=scount+1;
	}
	public void setAddFcount(){
		if(fcount==null){fcount=0;}
		this.fcount=fcount+1;
	}
	public void setAddMcount(){
		if(mcount==null){mcount=0;}
		this.mcount=mcount+1;
	}
	public void setAddFClass(String classname){
		if(classverfiy==null){
			classverfiy=new HashMap<String, DipSuccOrFailClass>();
		}
		DipSuccOrFailClass c=new DipSuccOrFailClass();
		if(classverfiy.containsKey(classname)){
			c=classverfiy.get(classname);
		}
		c.setAddFacount();
		classverfiy.put(classname, c);
		
	}
	public void setAddCClass(String classname){
		if(classverfiy==null){
			classverfiy=new HashMap<String, DipSuccOrFailClass>();
		}
		DipSuccOrFailClass c=new DipSuccOrFailClass();
		if(classverfiy.containsKey(classname)){
			c=classverfiy.get(classname);
		}
		c.setAddSucount();
		classverfiy.put(classname, c);
		
	}
	public Map<String, DipSuccOrFailClass> getClassverfiy() {
		return classverfiy;
	}
	public void setClassverfiy(Map<String, DipSuccOrFailClass> classverfiy) {
		this.classverfiy = classverfiy;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getFcount() {
		return fcount;
	}
	public void setFcount(Integer fcount) {
		this.fcount = fcount;
	}
	public Integer getScount() {
		return scount;
	}
	public void setScount(Integer scount) {
		this.scount = scount;
	}
	public Integer getMcount() {
		return mcount;
	}
	public void setMcount(Integer mcount) {
		this.mcount = mcount;
	}
	
	
}
