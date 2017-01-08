package nc.vo.dip.datarec;

import nc.util.dip.sj.IContrastUtil;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

public class DipDatarecSpecialTab extends SuperVO{
		private String	   pk_datarec_specialtab;
		private String	   pk_datarec_h         ;
		private UFBoolean	   is_back              ;
		private String	   name                 ;
		private String	   value                ;
		private UFBoolean	   is_xml               ;
		private int	   nodenumber           ;
		private String	   nodename             ;
		private String	   nodeproperty         ;
		private String	   str_vef1             ;
		private String	   str_vef2             ;
		private String	   str_vef3             ;
		private String	   str_vef4             ;
		private String	   str_vef5             ;
		private String	   str_vef6             ;
		private String	   str_vef7             ;
		private String	   str_vef8             ;
		private String	   str_vef9             ;
		private String	   str_vef10            ;
		private int	   dr                   ;
		private String	   ts                ;
		private UFBoolean is_xtfixed;
	
		public static final String	   IS_XTFIXED="is_xtfixed";
		public static final String	   PK_DATAREC_SPECIALTAB="pk_datarec_specialtab";
		public static final String	   PK_DATAREC_H="pk_datarec_h"         ;
		public static final String	   IS_BACK="is_back"              ;
		public static final String	   NAME="name"                 ;
		public static final String	   VALUE="value"                ;
		public static final String  IS_XML="is_xml"               ;
		public static final String	   NODENUMBER="nodenumber"           ;
		public static final String	   NODENAME="nodename"             ;
		public static final String	   NODEPROPERTY="nodeproperty"         ;
		public static final String	   STR_VEF1="str_vef1"             ;
		public static final String	   STR_VEF2="str_vef2"             ;
		public static final String	   STR_VEF3="str_vef3"             ;
		public static final String	   STR_VEF4="str_vef4"             ;
		public static final String	   STR_VEF5="str_vef5"             ;
		public static final String	   STR_VEF6="str_vef6"             ;
		public static final String	   STR_VEF7="str_vef7"             ;
		public static final String	   STR_VEF8="str_vef8"             ;
		public static final String	   STR_VEF9="str_vef9"             ;
		public static final String	   STR_VEF10="str_vef10"            ;
			public static final String	DR="dr"                   ;
		public static final String	   TS="ts"                ;
		
		
		
		
	    public int getDr() {
			return dr;
		}

		public void setDr(int dr) {
			this.dr = dr;
		}

		public UFBoolean getIs_back() {
			return is_back;
		}

		public void setIs_back(UFBoolean is_back) {
			this.is_back = is_back;
		}

		public UFBoolean getIs_xml() {
			return is_xml;
		}

		public void setIs_xml(UFBoolean is_xml) {
			this.is_xml = is_xml;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getNodename() {
			return nodename;
		}

		public void setNodename(String nodename) {
			this.nodename = nodename;
		}

		public int getNodenumber() {
			return nodenumber;
		}

		public void setNodenumber(int nodenumber) {
			this.nodenumber = nodenumber;
		}

		public String getNodeproperty() {
			return nodeproperty;
		}

		public void setNodeproperty(String nodeproperty) {
			this.nodeproperty = nodeproperty;
		}

		public String getPk_datarec_h() {
			return pk_datarec_h;
		}

		public void setPk_datarec_h(String pk_datarec_h) {
			this.pk_datarec_h = pk_datarec_h;
		}

		public String getPk_datarec_specialtab() {
			return pk_datarec_specialtab;
		}

		public void setPk_datarec_specialtab(String pk_datarec_specialtab) {
			this.pk_datarec_specialtab = pk_datarec_specialtab;
		}

		

		public String getStr_vef1() {
			return str_vef1;
		}

		public void setStr_vef1(String str_vef1) {
			this.str_vef1 = str_vef1;
		}

		public String getStr_vef10() {
			return str_vef10;
		}

		public void setStr_vef10(String str_vef10) {
			this.str_vef10 = str_vef10;
		}

		public String getStr_vef2() {
			return str_vef2;
		}

		public void setStr_vef2(String str_vef2) {
			this.str_vef2 = str_vef2;
		}

		public String getStr_vef3() {
			return str_vef3;
		}

		public void setStr_vef3(String str_vef3) {
			this.str_vef3 = str_vef3;
		}

		public String getStr_vef4() {
			return str_vef4;
		}

		public void setStr_vef4(String str_vef4) {
			this.str_vef4 = str_vef4;
		}

		public String getStr_vef5() {
			return str_vef5;
		}

		public void setStr_vef5(String str_vef5) {
			this.str_vef5 = str_vef5;
		}

		public String getStr_vef6() {
			return str_vef6;
		}

		public void setStr_vef6(String str_vef6) {
			this.str_vef6 = str_vef6;
		}

		public String getStr_vef7() {
			return str_vef7;
		}

		public void setStr_vef7(String str_vef7) {
			this.str_vef7 = str_vef7;
		}

		public String getStr_vef8() {
			return str_vef8;
		}

		public void setStr_vef8(String str_vef8) {
			this.str_vef8 = str_vef8;
		}

		public String getStr_vef9() {
			return str_vef9;
		}

		public void setStr_vef9(String str_vef9) {
			this.str_vef9 = str_vef9;
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

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	public UFBoolean getIs_xtfixed() {
			return is_xtfixed;
		}

		public void setIs_xtfixed(UFBoolean is_xtfixed) {
			this.is_xtfixed = is_xtfixed;
		}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_datarec_specialtab";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "dip_datarec_specialtab";
	}

}
