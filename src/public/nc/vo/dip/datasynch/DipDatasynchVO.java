  /***************************************************************\
  *     The skeleton of this class is generated by an automatic *
  * code generator for NC product. It is based on Velocity.     *
  \***************************************************************/
      	package nc.vo.dip.datasynch;
   	
	import java.util.ArrayList;

import nc.util.dip.sj.IContrastUtil;
import nc.vo.pub.*;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
	
/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 * 创建日期:2011-4-7
 * @author ${vmObject.author}
 * @version Your Project 1.0
 */
     public class DipDatasynchVO extends SuperVO {
           
             public String phyname;
             public String datasite;//数据定义主键
             public String datacon;
             public String tasktype;
             public String def_str_1;
             public String ts;
             public String constatus;
             public Integer dr;
             public String dataname;//同步定义主键
             public String pk_datasynch;
             public String Pk_datadefinit_h;
             public String def_str_2;
             public String endstatus;
             public String fpk;
             public String code;
             public String name;
             public String pk_xt;
             public UFBoolean isfolder;
            
             public static final String  PHYNAME="phyname";   
             public static final String  DATASITE="datasite";   
             public static final String  DATACON="datacon";   
             public static final String  TASKTYPE="tasktype";   
             public static final String  DEF_STR_1="def_str_1";   
             public static final String  TS="ts";   
             public static final String  CONSTATUS="constatus";   
             public static final String  DR="dr";   
             public static final String  DATANAME="dataname";   
             public static final String  PK_DATASYNCH="pk_datasynch";   
             public static final String  DEF_STR_2="def_str_2";   
             public static final String  ENDSTATUS="endstatus"; 
             public static final String PK_DATADEFINIT_H="Pk_datadefinit_h";
             public static final String PK_XT="pk_xt";
             public static final String ISFOLDER="isfolder";
            
      
    
        public String getPk_datadefinit_h() {
				return Pk_datadefinit_h;
			}

			public void setPk_datadefinit_h(String pk_datadefinit_h) {
				Pk_datadefinit_h = pk_datadefinit_h;
			}
			

		/**
	   * 属性phyname的Getter方法.
	   *
	   * 创建日期:2011-4-7
	   * @return String
	   */
	 public String getPhyname() {
		 return phyname;
	  }   
	  
     /**
	   * 属性phyname的Setter方法.
	   *
	   * 创建日期:2011-4-7
	   * @param newPhyname String
	   */
	public void setPhyname(String newPhyname) {
		
		phyname = newPhyname;
	 } 	  
       
        /**
	   * 属性datasite的Getter方法.
	   *
	   * 创建日期:2011-4-7
	   * @return String
	   */
	 public String getDatasite() {
		 return datasite;
	  }   
	  
     /**
	   * 属性datasite的Setter方法.
	   *
	   * 创建日期:2011-4-7
	   * @param newDatasite String
	   */
	public void setDatasite(String newDatasite) {
		
		datasite = newDatasite;
	 } 	  
       
        /**
	   * 属性datacon的Getter方法.
	   *
	   * 创建日期:2011-4-7
	   * @return String
	   */
	 public String getDatacon() {
		 return datacon;
	  }   
	  
     /**
	   * 属性datacon的Setter方法.
	   *
	   * 创建日期:2011-4-7
	   * @param newDatacon String
	   */
	public void setDatacon(String newDatacon) {
		
		datacon = newDatacon;
	 } 	  
       
        /**
	   * 属性tasktype的Getter方法.
	   *
	   * 创建日期:2011-4-7
	   * @return String
	   */
	 public String getTasktype() {
		 return tasktype;
	  }   
	  
     /**
	   * 属性tasktype的Setter方法.
	   *
	   * 创建日期:2011-4-7
	   * @param newTasktype String
	   */
	public void setTasktype(String newTasktype) {
		
		tasktype = newTasktype;
	 } 	  
       
        /**
	   * 属性def_str_1的Getter方法.
	   *
	   * 创建日期:2011-4-7
	   * @return String
	   */
	 public String getDef_str_1() {
		 return def_str_1;
	  }   
	  
     /**
	   * 属性def_str_1的Setter方法.
	   *
	   * 创建日期:2011-4-7
	   * @param newDef_str_1 String
	   */
	public void setDef_str_1(String newDef_str_1) {
		
		def_str_1 = newDef_str_1;
	 } 	  
       
        /**
	   * 属性ts的Getter方法.
	   *
	   * 创建日期:2011-4-7
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
	   * 创建日期:2011-4-7
	   * @param newTs UFDateTime
	   */
	public void setTs(String newTs) {
		
		ts = newTs;
	 } 	  
       
        /**
	   * 属性constatus的Getter方法.
	   *
	   * 创建日期:2011-4-7
	   * @return String
	   */
	 public String getConstatus() {
		 return constatus;
	  }   
	  
     /**
	   * 属性constatus的Setter方法.
	   *
	   * 创建日期:2011-4-7
	   * @param newConstatus String
	   */
	public void setConstatus(String newConstatus) {
		
		constatus = newConstatus;
	 } 	  
       
        /**
	   * 属性dr的Getter方法.
	   *
	   * 创建日期:2011-4-7
	   * @return Integer
	   */
	 public Integer getDr() {
		 return dr;
	  }   
	  
     /**
	   * 属性dr的Setter方法.
	   *
	   * 创建日期:2011-4-7
	   * @param newDr Integer
	   */
	public void setDr(Integer newDr) {
		
		dr = newDr;
	 } 	  
       
        /**
	   * 属性dataname的Getter方法.
	   *
	   * 创建日期:2011-4-7
	   * @return String
	   */
	 public String getDataname() {
		 return dataname;
	  }   
	  
     /**
	   * 属性dataname的Setter方法.
	   *
	   * 创建日期:2011-4-7
	   * @param newDataname String
	   */
	public void setDataname(String newDataname) {
		
		dataname = newDataname;
	 } 	  
       
        /**
	   * 属性pk_datasynch的Getter方法.
	   *
	   * 创建日期:2011-4-7
	   * @return String
	   */
	 public String getPk_datasynch() {
		 return pk_datasynch;
	  }   
	  
     /**
	   * 属性pk_datasynch的Setter方法.
	   *
	   * 创建日期:2011-4-7
	   * @param newPk_datasynch String
	   */
	public void setPk_datasynch(String newPk_datasynch) {
		
		pk_datasynch = newPk_datasynch;
	 } 	  
       
        /**
	   * 属性def_str_2的Getter方法.
	   *
	   * 创建日期:2011-4-7
	   * @return String
	   */
	 public String getDef_str_2() {
		 return def_str_2;
	  }   
	  
     /**
	   * 属性def_str_2的Setter方法.
	   *
	   * 创建日期:2011-4-7
	   * @param newDef_str_2 String
	   */
	public void setDef_str_2(String newDef_str_2) {
		
		def_str_2 = newDef_str_2;
	 } 	  
       
        /**
	   * 属性endstatus的Getter方法.
	   *
	   * 创建日期:2011-4-7
	   * @return String
	   */
	 public String getEndstatus() {
		 return endstatus;
	  }   
	  
     /**
	   * 属性endstatus的Setter方法.
	   *
	   * 创建日期:2011-4-7
	   * @param newEndstatus String
	   */
	public void setEndstatus(String newEndstatus) {
		
		endstatus = newEndstatus;
	 } 	  
       
       
    /**
	  * 验证对象各属性之间的数据逻辑正确性.
	  *
	  * 创建日期:2011-4-7
	  * @exception nc.vo.pub.ValidationException 如果验证失败,抛出
	  * ValidationException,对错误进行解释.
	 */
	 public void validate() throws ValidationException {
	
	 	ArrayList errFields = new ArrayList(); // errFields record those null

                                                      // fields that cannot be null.
       		  // 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:
	
	   		if (pk_datasynch == null) {
			errFields.add(new String("pk_datasynch"));
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
	  * 创建日期:2011-4-7
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	  	 
	 	    return "fpk";
	 	
	}   
    
    /**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2011-4-7
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	 	  return "pk_datasynch";
	 	}
    
	/**
      * <p>返回表名称.
	  * <p>
	  * 创建日期:2011-4-7
	  * @return java.lang.String
	 */
	public java.lang.String getTableName() {
				
		return "dip_datasynch";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2011-4-7
	  */
	public DipDatasynchVO() {
			
			   super();	
	  }    
    
            /**
	 * 使用主键进行初始化的构造子.
	 *
	 * 创建日期:2011-4-7
	 * @param newPk_datasynch 主键值
	 */
	 public DipDatasynchVO(String newPk_datasynch) {
		
		// 为主键字段赋值:
		 pk_datasynch = newPk_datasynch;
	
    	}
    
     
     /**
	  * 返回对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2011-4-7
	  * @return String
	  */
	   public String getPrimaryKey() {
				
		 return pk_datasynch;
	   
	   }

     /**
	  * 设置对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2011-4-7
	  * @param newPk_datasynch  String    
	  */
	 public void setPrimaryKey(String newPk_datasynch) {
				
				pk_datasynch = newPk_datasynch; 
				
	 } 
           
	  /**
       * 返回数值对象的显示名称.
	   *
	   * 创建日期:2011-4-7
	   * @return java.lang.String 返回数值对象的显示名称.
	   */
	 public String getEntityName() {
				
	   return "dip_datasynch"; 
				
	 }

	public String getFpk() {
		return fpk;
	}

	public void setFpk(String fpk) {
		this.fpk = fpk;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UFBoolean getIsfolder() {
		return isfolder;
	}

	public void setIsfolder(UFBoolean isfolder) {
		this.isfolder = isfolder;
	}

	public String getPk_xt() {
		return pk_xt;
	}

	public void setPk_xt(String pk_xt) {
		this.pk_xt = pk_xt;
	} 
} 
