package nc.vo.dip.roleandtable;

import nc.util.dip.sj.IContrastUtil;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

public class DipRoleAndTableBVO extends SuperVO{
	private String   pk_roleandtable_b;
	private String   pk_roleandtable_h;
	private String   memorytable;
	private String   display;
	private String   strdef_1;
	private String   strdef_2;
	private String   strdef_3;
	private String   strdef_4;
	private String   strdef_5;
	private String   strdef_6;
	private String   ts;
	private Integer  dr;
	
	public static final String   PK_ROLEANDTABLE_B="pk_roleandtable_b";
	public static final String   PK_ROLEANDTABLE_H="pk_roleandtable_h";
	public static final String   MEMORYTABLE="memorytable";
	public static final String   DISPLAY="display";
	public static final String   STRDEF_1="strdef_1";
	public static final String   STRDEF_2="strdef_2";
	public static final String   STRDEF_3="strdef_3";
	public static final String   STRDEF_4="strdef_4";
	public static final String   STRDEF_5="strdef_5";
	public static final String   STRDEF_6="strdef_6";
	public static final String   TS="ts";
	public static final String   DR="dr";
	
	
	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getMemorytable() {
		return memorytable;
	}

	public void setMemorytable(String memorytable) {
		this.memorytable = memorytable;
	}

	public String getPk_roleandtable_b() {
		return pk_roleandtable_b;
	}

	public void setPk_roleandtable_b(String pk_roleandtable_b) {
		this.pk_roleandtable_b = pk_roleandtable_b;
	}

	public String getPk_roleandtable_h() {
		return pk_roleandtable_h;
	}

	public void setPk_roleandtable_h(String pk_roleandtable_h) {
		this.pk_roleandtable_h = pk_roleandtable_h;
	}

	public String getStrdef_1() {
		return strdef_1;
	}

	public void setStrdef_1(String strdef_1) {
		this.strdef_1 = strdef_1;
	}

	public String getStrdef_2() {
		return strdef_2;
	}

	public void setStrdef_2(String strdef_2) {
		this.strdef_2 = strdef_2;
	}

	public String getStrdef_3() {
		return strdef_3;
	}

	public void setStrdef_3(String strdef_3) {
		this.strdef_3 = strdef_3;
	}

	public String getStrdef_4() {
		return strdef_4;
	}

	public void setStrdef_4(String strdef_4) {
		this.strdef_4 = strdef_4;
	}

	public String getStrdef_5() {
		return strdef_5;
	}

	public void setStrdef_5(String strdef_5) {
		this.strdef_5 = strdef_5;
	}

	public String getStrdef_6() {
		return strdef_6;
	}

	public void setStrdef_6(String strdef_6) {
		this.strdef_6 = strdef_6;
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

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_roleandtable_b";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_roleandtable_h";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "dip_roleandtable_b";
	}

}
