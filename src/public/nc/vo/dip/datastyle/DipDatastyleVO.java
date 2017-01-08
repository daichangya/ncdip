  /***************************************************************\
  *     The skeleton of this class is generated by an automatic *
  * code generator for NC product. It is based on Velocity.     *
  \***************************************************************/
      	package nc.vo.dip.datastyle;
   	
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
     public class DipDatastyleVO extends SuperVO {
           
             public String code;
             public String def_str_3;
             public String ts;
             public String def_str_1;
             public String pk_datastyle;
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
             public static final String  PK_DATASTYLE="pk_datastyle";   
             public static final String  DR="dr";   
             public static final String  DEF_INTEGER="def_integer";   
             public static final String  DEF_DOUBLE="def_double";   
             public static final String  DEF_STR_2="def_str_2";   
             public static final String  NAME="name"; 
             public static final String  ISSYSPREF="issyspref";
      
    
        /**
	   * 属性code的Getter方法.
	   *
	   * 创建日期:2011-4-1
	   * @return String
	   */
	 public String getCode() {
		 return code;
	  }   
	  
     /**
	   * 属性code的Setter方法.
	   *
	   * 创建日期:2011-4-1
	   * @param newCode String
	   */
	public void setCode(String newCode) {
		
		code = newCode;
	 } 	  
       
        /**
	   * 属性def_str_3的Getter方法.
	   *
	   * 创建日期:2011-4-1
	   * @return String
	   */
	 public String getDef_str_3() {
		 return def_str_3;
	  }   
	  
     /**
	   * 属性def_str_3的Setter方法.
	   *
	   * 创建日期:2011-4-1
	   * @param newDef_str_3 String
	   */
	public void setDef_str_3(String newDef_str_3) {
		
		def_str_3 = newDef_str_3;
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
	   * 属性def_str_1的Getter方法.
	   *
	   * 创建日期:2011-4-1
	   * @return String
	   */
	 public String getDef_str_1() {
		 return def_str_1;
	  }   
	  
     /**
	   * 属性def_str_1的Setter方法.
	   *
	   * 创建日期:2011-4-1
	   * @param newDef_str_1 String
	   */
	public void setDef_str_1(String newDef_str_1) {
		
		def_str_1 = newDef_str_1;
	 } 	  
       
        /**
	   * 属性pk_datastyle的Getter方法.
	   *
	   * 创建日期:2011-4-1
	   * @return String
	   */
	 public String getPk_datastyle() {
		 return pk_datastyle;
	  }   
	  
     /**
	   * 属性pk_datastyle的Setter方法.
	   *
	   * 创建日期:2011-4-1
	   * @param newPk_datastyle String
	   */
	public void setPk_datastyle(String newPk_datastyle) {
		
		pk_datastyle = newPk_datastyle;
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
	   * 属性def_integer的Getter方法.
	   *
	   * 创建日期:2011-4-1
	   * @return Integer
	   */
	 public Integer getDef_integer() {
		 return def_integer;
	  }   
	  
     /**
	   * 属性def_integer的Setter方法.
	   *
	   * 创建日期:2011-4-1
	   * @param newDef_integer Integer
	   */
	public void setDef_integer(Integer newDef_integer) {
		
		def_integer = newDef_integer;
	 } 	  
       
        /**
	   * 属性def_double的Getter方法.
	   *
	   * 创建日期:2011-4-1
	   * @return UFDouble
	   */
	 public UFDouble getDef_double() {
		 return def_double;
	  }   
	  
     /**
	   * 属性def_double的Setter方法.
	   *
	   * 创建日期:2011-4-1
	   * @param newDef_double UFDouble
	   */
	public void setDef_double(UFDouble newDef_double) {
		
		def_double = newDef_double;
	 } 	  
       
        /**
	   * 属性def_str_2的Getter方法.
	   *
	   * 创建日期:2011-4-1
	   * @return String
	   */
	 public String getDef_str_2() {
		 return def_str_2;
	  }   
	  
     /**
	   * 属性def_str_2的Setter方法.
	   *
	   * 创建日期:2011-4-1
	   * @param newDef_str_2 String
	   */
	public void setDef_str_2(String newDef_str_2) {
		
		def_str_2 = newDef_str_2;
	 } 	  
       
        /**
	   * 属性name的Getter方法.
	   *
	   * 创建日期:2011-4-1
	   * @return String
	   */
	 public String getName() {
		 return name;
	  }   
	  
     /**
	   * 属性name的Setter方法.
	   *
	   * 创建日期:2011-4-1
	   * @param newName String
	   */
	public void setName(String newName) {
		
		name = newName;
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
	
	   		if (pk_datastyle == null) {
			errFields.add(new String("pk_datastyle"));
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
	  	 
	 	    return null;
	 	
	}   
    
    /**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2011-4-1
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	 	  return "pk_datastyle";
	 	}
    
	/**
      * <p>返回表名称.
	  * <p>
	  * 创建日期:2011-4-1
	  * @return java.lang.String
	 */
	public java.lang.String getTableName() {
				
		return "dip_datastyle";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2011-4-1
	  */
	public DipDatastyleVO() {
			
			   super();	
	  }    
    
            /**
	 * 使用主键进行初始化的构造子.
	 *
	 * 创建日期:2011-4-1
	 * @param newPk_datastyle 主键值
	 */
	 public DipDatastyleVO(String newPk_datastyle) {
		
		// 为主键字段赋值:
		 pk_datastyle = newPk_datastyle;
	
    	}
    
     
     /**
	  * 返回对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2011-4-1
	  * @return String
	  */
	   public String getPrimaryKey() {
				
		 return pk_datastyle;
	   
	   }

     /**
	  * 设置对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2011-4-1
	  * @param newPk_datastyle  String    
	  */
	 public void setPrimaryKey(String newPk_datastyle) {
				
				pk_datastyle = newPk_datastyle; 
				
	 } 
           
	  /**
       * 返回数值对象的显示名称.
	   *
	   * 创建日期:2011-4-1
	   * @return java.lang.String 返回数值对象的显示名称.
	   */
	 public String getEntityName() {
				
	   return "dip_datastyle"; 
				
	 }

	public UFBoolean getIssyspref() {
		return issyspref;
	}

	public void setIssyspref(UFBoolean issyspref) {
		this.issyspref = issyspref;
	} 
	 
	 
} 
