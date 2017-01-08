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
* 属性code的Getter方法.
*
* 创建日期:2011-4-20
* @return String
*/
public String getCode() {
return code;
}   

/**
* 属性code的Setter方法.
*
* 创建日期:2011-4-20
* @param newCode String
*/
public void setCode(String newCode) {

code = newCode;
} 	  

/**
* 属性def_str_3的Getter方法.
*
* 创建日期:2011-4-20
* @return String
*/
public String getDef_str_3() {
return def_str_3;
}   

/**
* 属性def_str_3的Setter方法.
*
* 创建日期:2011-4-20
* @param newDef_str_3 String
*/
public void setDef_str_3(String newDef_str_3) {

def_str_3 = newDef_str_3;
} 	  

/**
* 属性ts的Getter方法.
*
* 创建日期:2011-4-20
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
* 属性ts的Setter方法.
*
* 创建日期:2011-4-20
* @param newTs UFDateTime
*/
public void setTs(String newTs) {

ts = newTs;
} 	  

/**
* 属性def_str_1的Getter方法.
*
* 创建日期:2011-4-20
* @return String
*/
public String getDef_str_1() {
return def_str_1;
}   

/**
* 属性def_str_1的Setter方法.
*
* 创建日期:2011-4-20
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
* 属性dr的Getter方法.
*
* 创建日期:2011-4-20
* @return Integer
*/
public Integer getDr() {
return dr;
}   

/**
* 属性dr的Setter方法.
*
* 创建日期:2011-4-20
* @param newDr Integer
*/
public void setDr(Integer newDr) {

dr = newDr;
} 	  

/**
* 属性def_integer的Getter方法.
*
* 创建日期:2011-4-20
* @return Integer
*/
public Integer getDef_integer() {
return def_integer;
}   

/**
* 属性def_integer的Setter方法.
*
* 创建日期:2011-4-20
* @param newDef_integer Integer
*/
public void setDef_integer(Integer newDef_integer) {

def_integer = newDef_integer;
} 	  

/**
* 属性def_double的Getter方法.
*
* 创建日期:2011-4-20
* @return UFDouble
*/
public UFDouble getDef_double() {
return def_double;
}   

/**
* 属性def_double的Setter方法.
*
* 创建日期:2011-4-20
* @param newDef_double UFDouble
*/
public void setDef_double(UFDouble newDef_double) {

def_double = newDef_double;
} 	  

/**
* 属性def_str_2的Getter方法.
*
* 创建日期:2011-4-20
* @return String
*/
public String getDef_str_2() {
return def_str_2;
}   

/**
* 属性def_str_2的Setter方法.
*
* 创建日期:2011-4-20
* @param newDef_str_2 String
*/
public void setDef_str_2(String newDef_str_2) {

def_str_2 = newDef_str_2;
} 	  

/**
* 属性name的Getter方法.
*
* 创建日期:2011-4-20
* @return String
*/
public String getName() {
return name;
}   

/**
* 属性name的Setter方法.
*
* 创建日期:2011-4-20
* @param newName String
*/
public void setName(String newName) {

name = newName;
} 	  


/**
* 验证对象各属性之间的数据逻辑正确性.
*
* 创建日期:2011-4-20
* @exception nc.vo.pub.ValidationException 如果验证失败,抛出
* ValidationException,对错误进行解释.
*/
public void validate() throws ValidationException {

ArrayList errFields = new ArrayList(); // errFields record those null

                                             // fields that cannot be null.
		  // 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:

		if (pk_messageplugregister == null) {
	errFields.add(new String("pk_messageplugregister"));
	  }	
	
StringBuffer message = new StringBuffer();
message.append("下列字段不能为空:");
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
* <p>取得父VO主键字段.
* <p>
* 创建日期:2011-4-20
* @return java.lang.String
*/
public java.lang.String getParentPKFieldName() {
	 
    return null;

}   

/**
* <p>取得表主键.
* <p>
* 创建日期:2011-4-20
* @return java.lang.String
*/
public java.lang.String getPKFieldName() {
  return "pk_messageplugregister";
}

/**
* <p>返回表名称.
* <p>
* 创建日期:2011-4-20
* @return java.lang.String
*/
public java.lang.String getTableName() {
		
return "dip_messageplugregister";
}    

/**
* 按照默认方式创建构造子.
*
* 创建日期:2011-4-20
*/
public DipMessagePlugRegisterVO() {
	
	   super();	
}    

   /**
* 使用主键进行初始化的构造子.
*
* 创建日期:2011-4-20
* @param newPk_taskregister 主键值
*/
public DipMessagePlugRegisterVO(String newPk_messageplugregister) {

// 为主键字段赋值:
	pk_messageplugregister = newPk_messageplugregister;

}


/**
* 返回对象标识,用来唯一定位对象.
*
* 创建日期:2011-4-20
* @return String
*/
public String getPrimaryKey() {
		
return pk_messageplugregister;

}

/**
* 设置对象标识,用来唯一定位对象.
*
* 创建日期:2011-4-20
* @param newPk_taskregister  String    
*/
public void setPrimaryKey(String newPk_messageplugregister) {
		
	pk_messageplugregister = newPk_messageplugregister; 
		
} 
  
/**
* 返回数值对象的显示名称.
*
* 创建日期:2011-4-20
* @return java.lang.String 返回数值对象的显示名称.
*/
public String getEntityName() {
		
return "dip_messageplubregister"; 
		
} 
} 
