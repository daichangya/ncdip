package nc.vo.dip.messageplugregister;

import java.util.ArrayList;

import nc.util.dip.sj.IContrastUtil;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class DipMessagePlugRegisterVO extends SuperVO {
    
    public String code;
    public String def_str_3;
    public String ts;
    public String def_str_1;
    public String pk_messageplugregister;
    public Integer dr;
    public Integer def_integer;
    public UFDouble def_double;
    public String def_str_2;
    public String name;
    public UFBoolean issyspref;
   
    public static final String  CODE="code";   
    public static final String  DEF_STR_3="def_str_3";   
    public static final String  TS="ts";   
    public static final String  DEF_STR_1="def_str_1";   
    public static final String  PK_MESSAGEPLUGREGISTER="pk_messageplugregister";   
    public static final String  DR="dr";   
    public static final String  DEF_INTEGER="def_integer";   
    public static final String  DEF_DOUBLE="def_double";   
    public static final String  DEF_STR_2="def_str_2";   
    public static final String  NAME="name";   
    public static final String  ISSYSPREF="issyspref";
    

public UFBoolean getIssyspref() {
		return issyspref;
	}

	public void setIssyspref(UFBoolean issyspref) {
		this.issyspref = issyspref;
	}

/**
* ����code��Getter����.
*
* ��������:2011-4-20
* @return String
*/
public String getCode() {
return code;
}   

/**
* ����code��Setter����.
*
* ��������:2011-4-20
* @param newCode String
*/
public void setCode(String newCode) {

code = newCode;
} 	  

/**
* ����def_str_3��Getter����.
*
* ��������:2011-4-20
* @return String
*/
public String getDef_str_3() {
return def_str_3;
}   

/**
* ����def_str_3��Setter����.
*
* ��������:2011-4-20
* @param newDef_str_3 String
*/
public void setDef_str_3(String newDef_str_3) {

def_str_3 = newDef_str_3;
} 	  

/**
* ����ts��Getter����.
*
* ��������:2011-4-20
* @return UFDateTime
*/
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

/**
* ����ts��Setter����.
*
* ��������:2011-4-20
* @param newTs UFDateTime
*/
public void setTs(String newTs) {

ts = newTs;
} 	  

/**
* ����def_str_1��Getter����.
*
* ��������:2011-4-20
* @return String
*/
public String getDef_str_1() {
return def_str_1;
}   

/**
* ����def_str_1��Setter����.
*
* ��������:2011-4-20
* @param newDef_str_1 String
*/
public void setDef_str_1(String newDef_str_1) {

def_str_1 = newDef_str_1;
} 	  

  

public String getPk_messageplubregister() {
	return pk_messageplugregister;
}

public void setPk_messageplubregister(String pk_messageplugregister) {
	this.pk_messageplugregister = pk_messageplugregister;
}

/**
* ����dr��Getter����.
*
* ��������:2011-4-20
* @return Integer
*/
public Integer getDr() {
return dr;
}   

/**
* ����dr��Setter����.
*
* ��������:2011-4-20
* @param newDr Integer
*/
public void setDr(Integer newDr) {

dr = newDr;
} 	  

/**
* ����def_integer��Getter����.
*
* ��������:2011-4-20
* @return Integer
*/
public Integer getDef_integer() {
return def_integer;
}   

/**
* ����def_integer��Setter����.
*
* ��������:2011-4-20
* @param newDef_integer Integer
*/
public void setDef_integer(Integer newDef_integer) {

def_integer = newDef_integer;
} 	  

/**
* ����def_double��Getter����.
*
* ��������:2011-4-20
* @return UFDouble
*/
public UFDouble getDef_double() {
return def_double;
}   

/**
* ����def_double��Setter����.
*
* ��������:2011-4-20
* @param newDef_double UFDouble
*/
public void setDef_double(UFDouble newDef_double) {

def_double = newDef_double;
} 	  

/**
* ����def_str_2��Getter����.
*
* ��������:2011-4-20
* @return String
*/
public String getDef_str_2() {
return def_str_2;
}   

/**
* ����def_str_2��Setter����.
*
* ��������:2011-4-20
* @param newDef_str_2 String
*/
public void setDef_str_2(String newDef_str_2) {

def_str_2 = newDef_str_2;
} 	  

/**
* ����name��Getter����.
*
* ��������:2011-4-20
* @return String
*/
public String getName() {
return name;
}   

/**
* ����name��Setter����.
*
* ��������:2011-4-20
* @param newName String
*/
public void setName(String newName) {

name = newName;
} 	  


/**
* ��֤���������֮��������߼���ȷ��.
*
* ��������:2011-4-20
* @exception nc.vo.pub.ValidationException �����֤ʧ��,�׳�
* ValidationException,�Դ�����н���.
*/
public void validate() throws ValidationException {

ArrayList errFields = new ArrayList(); // errFields record those null

                                             // fields that cannot be null.
		  // ����Ƿ�Ϊ�������յ��ֶθ��˿�ֵ,�������Ҫ�޸��������ʾ��Ϣ:

		if (pk_messageplugregister == null) {
	errFields.add(new String("pk_messageplugregister"));
	  }	
	
StringBuffer message = new StringBuffer();
message.append("�����ֶβ���Ϊ��:");
if (errFields.size() > 0) {
String[] temp = (String[]) errFields.toArray(new String[0]);
message.append(temp[0]);
for ( int i= 1; i < temp.length; i++ ) {
	message.append(",");
	message.append(temp[i]);
}
throw new NullFieldException(message.toString());
}
}
	   

/**
* <p>ȡ�ø�VO�����ֶ�.
* <p>
* ��������:2011-4-20
* @return java.lang.String
*/
public java.lang.String getParentPKFieldName() {
	 
    return null;

}   

/**
* <p>ȡ�ñ�����.
* <p>
* ��������:2011-4-20
* @return java.lang.String
*/
public java.lang.String getPKFieldName() {
  return "pk_messageplugregister";
}

/**
* <p>���ر�����.
* <p>
* ��������:2011-4-20
* @return java.lang.String
*/
public java.lang.String getTableName() {
		
return "dip_messageplugregister";
}    

/**
* ����Ĭ�Ϸ�ʽ����������.
*
* ��������:2011-4-20
*/
public DipMessagePlugRegisterVO() {
	
	   super();	
}    

   /**
* ʹ���������г�ʼ���Ĺ�����.
*
* ��������:2011-4-20
* @param newPk_taskregister ����ֵ
*/
public DipMessagePlugRegisterVO(String newPk_messageplugregister) {

// Ϊ�����ֶθ�ֵ:
	pk_messageplugregister = newPk_messageplugregister;

}


/**
* ���ض����ʶ,����Ψһ��λ����.
*
* ��������:2011-4-20
* @return String
*/
public String getPrimaryKey() {
		
return pk_messageplugregister;

}

/**
* ���ö����ʶ,����Ψһ��λ����.
*
* ��������:2011-4-20
* @param newPk_taskregister  String    
*/
public void setPrimaryKey(String newPk_messageplugregister) {
		
	pk_messageplugregister = newPk_messageplugregister; 
		
} 
  
/**
* ������ֵ�������ʾ����.
*
* ��������:2011-4-20
* @return java.lang.String ������ֵ�������ʾ����.
*/
public String getEntityName() {
		
return "dip_messageplubregister"; 
		
} 
} 