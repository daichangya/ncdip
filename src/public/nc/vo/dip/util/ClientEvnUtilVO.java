package nc.vo.dip.util;

import java.util.Map;

public class ClientEvnUtilVO {
	public String[] cnames;
	public QueryUtilVO[] vos;
	public Map<String,String> map;
	
	
	public Map<String, String> getMap() {
		return map;
	}
	public void setMap(Map<String, String> map) {
		this.map = map;
	}
	public String[] getCnames() {
		return cnames;
	}
	public void setCnames(String[] cnames) {
		this.cnames = cnames;
	}
	public QueryUtilVO[] getVos() {
		return vos;
	}
	public void setVos(QueryUtilVO[] vos) {
		this.vos = vos;
	}
}
