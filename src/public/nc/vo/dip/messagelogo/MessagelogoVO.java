  /***************************************************************\
  *     The skeleton of this class is generated by an automatic *
  * code generator for NC product. It is based on Velocity.     *
  \***************************************************************/
      	package nc.vo.dip.messagelogo;
   	
	import java.util.ArrayList;

import nc.util.dip.sj.IContrastUtil;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
	
/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 * 创建日期:2011-4-8
 * @author author
 * @version Your Project 1.0
 */
     public class MessagelogoVO extends SuperVO {
           
             public String code;
             public String vdef2;
             public String vdef4;
             public String pk_messagelogo;
             public String ts;
             public String cdata;
             public String ctype;
             public UFBoolean vdef1;
             public Integer dr;
             public String vdef3;
             public String vdef5;
             public String cname;
            
             public static final String  CODE="code";   
             public static final String  VDEF2="vdef2";   
             public static final String  VDEF4="vdef4";   
             public static final String  PK_MESSAGELOGO="pk_messagelogo";   
             public static final String  TS="ts";   
             public static final String  CDATA="cdata";   
             public static final String  CTYPE="ctype";   
             public static final String  VDEF1="vdef1";   
             public static final String  DR="dr";   
             public static final String  VDEF3="vdef3";   
             public static final String  VDEF5="vdef5";   
             public static final String  CNAME="cname";   
      
    
        /**
	   * 属性code的Getter方法.
	   *
	   * 创建日期:2011-4-8
	   * @return String
	   */
	 public String getCode() {
		 return code;
	  }   
	  
     /**
	   * 属性code的Setter方法.
	   *
	   * 创建日期:2011-4-8
	   * @param newCode String
	   */
	public void setCode(String newCode) {
		
		code = newCode;
	 } 	  
       
        /**
	   * 属性vdef2的Getter方法.
	   *
	   * 创建日期:2011-4-8
	   * @return String
	   */
	 public String getVdef2() {
		 return vdef2;
	  }   
	  
     /**
	   * 属性vdef2的Setter方法.
	   *
	   * 创建日期:2011-4-8
	   * @param newVdef2 String
	   */
	public void setVdef2(String newVdef2) {
		
		vdef2 = newVdef2;
	 } 	  
       
        /**
	   * 属性vdef4的Getter方法.
	   *
	   * 创建日期:2011-4-8
	   * @return String
	   */
	 public String getVdef4() {
		 return vdef4;
	  }   
	  
     /**
	   * 属性vdef4的Setter方法.
	   *
	   * 创建日期:2011-4-8
	   * @param newVdef4 String
	   */
	public void setVdef4(String newVdef4) {
		
		vdef4 = newVdef4;
	 } 	  
       
        /**
	   * 属性pk_messagelogo的Getter方法.
	   *
	   * 创建日期:2011-4-8
	   * @return String
	   */
	 public String getPk_messagelogo() {
		 return pk_messagelogo;
	  }   
	  
     /**
	   * 属性pk_messagelogo的Setter方法.
	   *
	   * 创建日期:2011-4-8
	   * @param newPk_messagelogo String
	   */
	public void setPk_messagelogo(String newPk_messagelogo) {
		
		pk_messagelogo = newPk_messagelogo;
	 } 	  
       
        /**
	   * 属性ts的Getter方法.
	   *
	   * 创建日期:2011-4-8
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
	   * 创建日期:2011-4-8
	   * @param newTs UFDateTime
	   */
	public void setTs(String newTs) {
		
		ts = newTs;
	 } 	  
       
        /**
	   * 属性cdata的Getter方法.
	   *
	   * 创建日期:2011-4-8
	   * @return String
	   */
	 public String getCdata() {
		 return cdata;
	  }   
	  
     /**
	   * 属性cdata的Setter方法.
	   *
	   * 创建日期:2011-4-8
	   * @param newCdata String
	   */
	public void setCdata(String newCdata) {
		
		cdata = newCdata;
	 } 	  
       
        /**
	   * 属性ctype的Getter方法.
	   *
	   * 创建日期:2011-4-8
	   * @return String
	   */
	 public String getCtype() {
		 return ctype;
	  }   
	  
     /**
	   * 属性ctype的Setter方法.
	   *
	   * 创建日期:2011-4-8
	   * @param newCtype String
	   */
	public void setCtype(String newCtype) {
		
		ctype = newCtype;
	 } 	  
       
        /**
	   * 属性vdef1的Getter方法.
	   *
	   * 创建日期:2011-4-8
	   * @return String
	   */
	 public UFBoolean getVdef1() {
		 return vdef1;
	  }   
	  
     /**
	   * 属性vdef1的Setter方法.
	   *
	   * 创建日期:2011-4-8
	   * @param newVdef1 String
	   */
	public void setVdef1(UFBoolean newVdef1) {
		
		vdef1 = newVdef1;
	 } 	  
       
        /**
	   * 属性dr的Getter方法.
	   *
	   * 创建日期:2011-4-8
	   * @return Integer
	   */
	 public Integer getDr() {
		 return dr;
	  }   
	  
     /**
	   * 属性dr的Setter方法.
	   *
	   * 创建日期:2011-4-8
	   * @param newDr Integer
	   */
	public void setDr(Integer newDr) {
		
		dr = newDr;
	 } 	  
       
        /**
	   * 属性vdef3的Getter方法.
	   *
	   * 创建日期:2011-4-8
	   * @return String
	   */
	 public String getVdef3() {
		 return vdef3;
	  }   
	  
     /**
	   * 属性vdef3的Setter方法.
	   *
	   * 创建日期:2011-4-8
	   * @param newVdef3 String
	   */
	public void setVdef3(String newVdef3) {
		
		vdef3 = newVdef3;
	 } 	  
       
        /**
	   * 属性vdef5的Getter方法.
	   *
	   * 创建日期:2011-4-8
	   * @return String
	   */
	 public String getVdef5() {
		 return vdef5;
	  }   
	  
     /**
	   * 属性vdef5的Setter方法.
	   *
	   * 创建日期:2011-4-8
	   * @param newVdef5 String
	   */
	public void setVdef5(String newVdef5) {
		
		vdef5 = newVdef5;
	 } 	  
       
        /**
	   * 属性cname的Getter方法.
	   *
	   * 创建日期:2011-4-8
	   * @return String
	   */
	 public String getCname() {
		 return cname;
	  }   
	  
     /**
	   * 属性cname的Setter方法.
	   *
	   * 创建日期:2011-4-8
	   * @param newCname String
	   */
	public void setCname(String newCname) {
		
		cname = newCname;
	 } 	  
       
       
    /**
	  * 验证对象各属性之间的数据逻辑正确性.
	  *
	  * 创建日期:2011-4-8
	  * @exception nc.vo.pub.ValidationException 如果验证失败,抛出
	  * ValidationException,对错误进行解释.
	 */
	 public void validate() throws ValidationException {
	
	 	ArrayList errFields = new ArrayList(); // errFields record those null

                                                      // fields that cannot be null.
       		  // 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:
	
	   		if (pk_messagelogo == null) {
			errFields.add(new String("pk_messagelogo"));
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
	  * 创建日期:2011-4-8
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	  	 
	 	    return null;
	 	
	}   
    
    /**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2011-4-8
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	 	  return "pk_messagelogo";
	 	}
    
	/**
      * <p>返回表名称.
	  * <p>
	  * 创建日期:2011-4-8
	  * @return java.lang.String
	 */
	public java.lang.String getTableName() {
				
		return "dip_messagelogo";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2011-4-8
	  */
	public MessagelogoVO() {
			
			   super();	
	  }    
    
            /**
	 * 使用主键进行初始化的构造子.
	 *
	 * 创建日期:2011-4-8
	 * @param newPk_messagelogo 主键值
	 */
	 public MessagelogoVO(String newPk_messagelogo) {
		
		// 为主键字段赋值:
		 pk_messagelogo = newPk_messagelogo;
	
    	}
    
     
     /**
	  * 返回对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2011-4-8
	  * @return String
	  */
	   public String getPrimaryKey() {
				
		 return pk_messagelogo;
	   
	   }

     /**
	  * 设置对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2011-4-8
	  * @param newPk_messagelogo  String    
	  */
	 public void setPrimaryKey(String newPk_messagelogo) {
				
				pk_messagelogo = newPk_messagelogo; 
				
	 } 
           
	  /**
       * 返回数值对象的显示名称.
	   *
	   * 创建日期:2011-4-8
	   * @return java.lang.String 返回数值对象的显示名称.
	   */
	 public String getEntityName() {
				
	   return "dip_messagelogo"; 
				
	 } 
} 
