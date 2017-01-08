package nc.vo.dip.roleandtable;

import nc.util.dip.sj.IContrastUtil;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

public class DipRoleAndTableHVO extends SuperVO{
	  private String pk_roleandtable_h   ;
	  private String pk_role             ;
	  private String pk_corp             ;
	  private String strdef_1            ;//权限类型 jdbc=1,servlet=2,webservice=3
	  private String strdef_2            ;
	  private String strdef_3            ;
	  private String strdef_4            ;
	  private String strdef_5            ;
	  private String strdef_6            ;
	  private String ts                  ;
	  private Integer dr                 ;
	  
	  public static final String PK_ROLEANDTABLE_H="pk_roleandtable_h";
	  public static final String PK_ROLE="pk_role";
	  public static final String PK_CORP="pk_corp";
	  public static final String STRDEF_1="strdef_1";
	  public static final String STRDEF_2="strdef_2";
	  public static final String STRDEF_3="strdef_3";
	  public static final String STRDEF_4="strdef_4";
	  public static final String STRDEF_5="strdef_5";
	  public static final String STRDEF_6="strdef_6";
	  public static final String TS="ts";
	  public static final String DR="dr";
	  
	  
	
	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getPk_role() {
		return pk_role;
	}

	public void setPk_role(String pk_role) {
		this.pk_role = pk_role;
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
		return "pk_roleandtable_h";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "dip_roleandtable_h";
	}
	
	

}
