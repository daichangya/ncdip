package nc.vo.dip.util;

import java.io.Serializable;

public class DipSuccOrFailClass implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3228553924333840525L;
	private String classname;
	private Integer sucount=0;
	private Integer facount=0;
	public void setAddSucount(){
		if(sucount==null){sucount=0;}
		this.sucount=sucount+1;
	}
	public void setAddFacount(){
		if(facount==null){facount=0;}
		this.facount=facount+1;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public Integer getFacount() {
		return facount;
	}
	public void setFacount(Integer facount) {
		this.facount = facount;
	}
	public Integer getSucount() {
		return sucount;
	}
	public void setSucount(Integer sucount) {
		this.sucount = sucount;
	}
}
