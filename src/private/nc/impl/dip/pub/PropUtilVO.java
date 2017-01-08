package nc.impl.dip.pub;

import java.util.HashMap;
import java.util.Map;
/**
 * @desc ESB消息发送文件的PROP文件的对应的VO
 * */
public class PropUtilVO {
	String xtbz;
	String zdbz;
	String ywbz;
	String tablename;
	String datasumnum;
	String datanum;
	String wjlname;
	Map<String,String> tag=new HashMap<String, String>();
	
	public String getDatanum() {
		return datanum;
	}
	public void setDatanum(String datanum) {
		this.datanum = datanum;
	}
	public String getDatasumnum() {
		return datasumnum;
	}
	public void setDatasumnum(String datasumnum) {
		this.datasumnum = datasumnum;
	}
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	public String getWjlname() {
		return wjlname;
	}
	public void setWjlname(String wjlname) {
		this.wjlname = wjlname;
	}
	public String getXtbz() {
		return xtbz;
	}
	public void setXtbz(String xtbz) {
		this.xtbz = xtbz;
	}
	public String getYwbz() {
		return ywbz;
	}
	public void setYwbz(String ywbz) {
		this.ywbz = ywbz;
	}
	public String getZdbz() {
		return zdbz;
	}
	public void setZdbz(String zdbz) {
		this.zdbz = zdbz;
	}
	public void setAttribuate(String key,String value){
		if(key.equals("xtbz")){setXtbz(value);}    
		if(key.equals("zdbz")){setZdbz(value);}     
		if(key.equals("ywbz")){setYwbz(value);}     
		if(key.equals("tablename")){setTablename(value);}     
		if(key.equals("datasumnum")){setDatasumnum(value);}     
		if(key.equals("datanum")){setDatanum(value);}     
		if(key.equals("wjlname")){setWjlname(value);}    

	}
}
