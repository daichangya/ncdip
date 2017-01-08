package nc.vo.dip.authorization.ip;

import nc.util.dip.sj.IContrastUtil;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

public class AuthorizationIpVO extends SuperVO{
	
	
	private String   pk_authorization_ip  ;
	private String   code                 ;
	private String   name                 ;
	private String   ip                   ;
	private String   defstr_1             ;
	private String   defstr_2             ;
	private String   defstr_3             ;
	private String   defstr_4             ;
	private String   defstr_5             ;
	private String   defstr_6             ;
	private String   ts                   ;
	private Integer   dr                  ;
	
	
	public static final String PK_AUTHORIZATION="pk_authorization_ip";
	public static final String CODE="code";
	public static final String NAME="name";
	public static final String IP="ip";
	public static final String DEFSTR_1="defstr_1";
	public static final String DEFSTR_2="defstr_2";
	public static final String DEFSTR_3="defstr_3";
	public static final String DEFSTR_4="defstr_4";
	public static final String DEFSTR_5="defstr_5";
	public static final String DEFSTR_6="defstr_6";
	public static final String TS="ts";
	public static final String DR="dr";
	
	
	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_authorization_ip";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "dip_authorization_ip";
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDefstr_1() {
		return defstr_1;
	}

	public void setDefstr_1(String defstr_1) {
		this.defstr_1 = defstr_1;
	}

	public String getDefstr_2() {
		return defstr_2;
	}

	public void setDefstr_2(String defstr_2) {
		this.defstr_2 = defstr_2;
	}

	public String getDefstr_3() {
		return defstr_3;
	}

	public void setDefstr_3(String defstr_3) {
		this.defstr_3 = defstr_3;
	}

	public String getDefstr_4() {
		return defstr_4;
	}

	public void setDefstr_4(String defstr_4) {
		this.defstr_4 = defstr_4;
	}

	public String getDefstr_5() {
		return defstr_5;
	}

	public void setDefstr_5(String defstr_5) {
		this.defstr_5 = defstr_5;
	}

	public String getDefstr_6() {
		return defstr_6;
	}

	public void setDefstr_6(String defstr_6) {
		this.defstr_6 = defstr_6;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPk_authorization_ip() {
		return pk_authorization_ip;
	}

	public void setPk_authorization_ip(String pk_authorization_ip) {
		this.pk_authorization_ip = pk_authorization_ip;
	}

	public Object getTs() {
		if(ts==null){
			return null;
		}else{
			if(IContrastUtil.VERSION.equals("nc502")){
				  return new UFDateTime(ts);
			  }else if(IContrastUtil.VERSION.equals("nc507")){
				  return ts;  
			  }
		}
		return ts;
	} 

	public void setTs(String ts) {
		this.ts = ts;
	}
	
	
}
