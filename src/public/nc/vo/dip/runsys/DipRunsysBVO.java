  /***************************************************************\
  *     The skeleton of this class is generated by an automatic *
  * code generator for NC product. It is based on Velocity.     *
  \***************************************************************/
      	package nc.vo.dip.runsys;
   	
	import java.util.ArrayList;

import nc.util.dip.sj.IContrastUtil;
	import nc.vo.pub.*;
import nc.vo.pub.lang.*;
	
/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 * 创建日期:2011-4-1
 * @author ${vmObject.author}
 * @version Your Project 1.0
 */
     public class DipRunsysBVO extends SuperVO {
         
         //public String pk_runsys_h;
         public String str_def_1;
         public String ts;
         public String str_def_3;
         public String sysname;
         public String syscode;
         public Integer dr;
         public String str_def_2;
         public String sysvalue;
         public String pk_runsys_b;
         public String remark;
        
        // public static final String  PK_RUNSYS_H="pk_runsys_h";   
         public static final String  STR_DEF_1="str_def_1";   
         public static final String  TS="ts";   
         public static final String  STR_DEF_3="str_def_3";   
         public static final String  SYSNAME="sysname";   
         public static final String  SYSCODE="syscode";   
         public static final String  DR="dr";   
         public static final String  STR_DEF_2="str_def_2";   
         public static final String  SYSVALUE="sysvalue";   
         public static final String  PK_RUNSYS_B="pk_runsys_b";  
         public static final String  REMARK="remark";
  

    /**
   * 属性pk_runsys_h的Getter方法.
   *
   * 创建日期:2011-4-1
   * @return String
   */
/* public String getPk_runsys_h() {
	 return pk_runsys_h;
  }   */
  
 /**
   * 属性pk_runsys_h的Setter方法.
   *
   * 创建日期:2011-4-1
   * @param newPk_runsys_h String
   */
/*public void setPk_runsys_h(String newPk_runsys_h) {
	
	pk_runsys_h = newPk_runsys_h;
 } 	 */ 
   
    /**
   * 属性str_def_1的Getter方法.
   *
   * 创建日期:2011-4-1
   * @return String
   */
 public String getStr_def_1() {
	 return str_def_1;
  }   
  
 /**
   * 属性str_def_1的Setter方法.
   *
   * 创建日期:2011-4-1
   * @param newStr_def_1 String
   */
public void setStr_def_1(String newStr_def_1) {
	
	str_def_1 = newStr_def_1;
 } 	  
   
    /**
   * 属性ts的Getter方法.
   *
   * 创建日期:2011-4-1
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
   * 创建日期:2011-4-1
   * @param newTs UFDateTime
   */
public void setTs(String newTs) {
	
	ts = newTs;
 } 	  
   
    /**
   * 属性str_def_3的Getter方法.
   *
   * 创建日期:2011-4-1
   * @return String
   */
 public String getStr_def_3() {
	 return str_def_3;
  }   
  
 /**
   * 属性str_def_3的Setter方法.
   *
   * 创建日期:2011-4-1
   * @param newStr_def_3 String
   */
public void setStr_def_3(String newStr_def_3) {
	
	str_def_3 = newStr_def_3;
 } 	  
   
    /**
   * 属性sysname的Getter方法.
   *
   * 创建日期:2011-4-1
   * @return String
   */
 public String getSysname() {
	 return sysname;
  }   
  
 /**
   * 属性sysname的Setter方法.
   *
   * 创建日期:2011-4-1
   * @param newSysname String
   */
public void setSysname(String newSysname) {
	
	sysname = newSysname;
 } 	  
   
    /**
   * 属性syscode的Getter方法.
   *
   * 创建日期:2011-4-1
   * @return String
   */
 public String getSyscode() {
	 return syscode;
  }   
  
 /**
   * 属性syscode的Setter方法.
   *
   * 创建日期:2011-4-1
   * @param newSyscode String
   */
public void setSyscode(String newSyscode) {
	
	syscode = newSyscode;
 } 	  
   
    /**
   * 属性dr的Getter方法.
   *
   * 创建日期:2011-4-1
   * @return Integer
   */
 public Integer getDr() {
	 return dr;
  }   
  
 /**
   * 属性dr的Setter方法.
   *
   * 创建日期:2011-4-1
   * @param newDr Integer
   */
public void setDr(Integer newDr) {
	
	dr = newDr;
 } 	  
   
    /**
   * 属性str_def_2的Getter方法.
   *
   * 创建日期:2011-4-1
   * @return String
   */
 public String getStr_def_2() {
	 return str_def_2;
  }   
  
 /**
   * 属性str_def_2的Setter方法.
   *
   * 创建日期:2011-4-1
   * @param newStr_def_2 String
   */
public void setStr_def_2(String newStr_def_2) {
	
	str_def_2 = newStr_def_2;
 } 	  
   
    /**
   * 属性sysvalue的Getter方法.
   *
   * 创建日期:2011-4-1
   * @return UFDateTime
   */
 public String getSysvalue() {
	 return sysvalue;
  }   
  
 /**
   * 属性sysvalue的Setter方法.
   *
   * 创建日期:2011-4-1
   * @param newSysvalue UFDateTime
   */
public void setSysvalue(String newSysvalue) {
	
	sysvalue = newSysvalue;
 } 	  
   
    /**
   * 属性pk_runsys_b的Getter方法.
   *
   * 创建日期:2011-4-1
   * @return String
   */
 public String getPk_runsys_b() {
	 return pk_runsys_b;
  }   
  
 /**
   * 属性pk_runsys_b的Setter方法.
   *
   * 创建日期:2011-4-1
   * @param newPk_runsys_b String
   */
public void setPk_runsys_b(String newPk_runsys_b) {
	
	pk_runsys_b = newPk_runsys_b;
 } 	  
   
   
/**
  * 验证对象各属性之间的数据逻辑正确性.
  *
  * 创建日期:2011-4-1
  * @exception nc.vo.pub.ValidationException 如果验证失败,抛出
  * ValidationException,对错误进行解释.
 */
 public void validate() throws ValidationException {

 	ArrayList errFields = new ArrayList(); // errFields record those null

                                                  // fields that cannot be null.
   		  // 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:

   		if (pk_runsys_b == null) {
		errFields.add(new String("pk_runsys_b"));
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
  * 创建日期:2011-4-1
  * @return java.lang.String
  */
public java.lang.String getParentPKFieldName() {
  	 
 		//return "pk_runsys_h";
	return null;
 	
}   

/**
  * <p>取得表主键.
  * <p>
  * 创建日期:2011-4-1
  * @return java.lang.String
  */
public java.lang.String getPKFieldName() {
 	  return "pk_runsys_b";
 	}

/**
  * <p>返回表名称.
  * <p>
  * 创建日期:2011-4-1
  * @return java.lang.String
 */
public java.lang.String getTableName() {
			
	return "dip_runsys_b";
}    

/**
  * 按照默认方式创建构造子.
  *
  * 创建日期:2011-4-1
  */
public DipRunsysBVO() {
		
		   super();	
  }    

        /**
 * 使用主键进行初始化的构造子.
 *
 * 创建日期:2011-4-1
 * @param newPk_runsys_b 主键值
 */
 public DipRunsysBVO(String newPk_runsys_b) {
	
	// 为主键字段赋值:
	 pk_runsys_b = newPk_runsys_b;

	}

 
 /**
  * 返回对象标识,用来唯一定位对象.
  *
  * 创建日期:2011-4-1
  * @return String
  */
   public String getPrimaryKey() {
			
	 return pk_runsys_b;
   
   }

 /**
  * 设置对象标识,用来唯一定位对象.
  *
  * 创建日期:2011-4-1
  * @param newPk_runsys_b  String    
  */
 public void setPrimaryKey(String newPk_runsys_b) {
			
			pk_runsys_b = newPk_runsys_b; 
			
 } 
       
  /**
   * 返回数值对象的显示名称.
   *
   * 创建日期:2011-4-1
   * @return java.lang.String 返回数值对象的显示名称.
   */
 public String getEntityName() {
			
   return "dip_runsys_b"; 
			
 }

public String getRemark() {
	return remark;
}

public void setRemark(String remark) {
	this.remark = remark;
} 
} 
