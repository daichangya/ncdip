  /***************************************************************\
  *     The skeleton of this class is generated by an automatic *
  * code generator for NC product. It is based on Velocity.     *
  \***************************************************************/
      	package nc.vo.dip.dateprocess;
   	
	import java.util.ArrayList;

import nc.util.dip.sj.IContrastUtil;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
	
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
     public class DateprocessVO extends SuperVO {
           
             /**
	 * 
	 */
//	private static final long serialVersionUID = 155555554535343l;
			public String def_str_3;
             public String ts;
             public String def_str_1;//提示
             public String sysname;
             public String pk_datacheckstat;
             public Integer dr;
             public String content;
             public Integer def_integer;
             public UFDouble def_double;
             public String def_str_2;
             public String sdate;
             public String active;
             public String activetype;
             public String activecode;
             public String pk_bus;//业务表主键
             public String syscode;
             public String edate;//
             public UFBoolean success;
             public String pk_processflow_b;
             public static final String PK_BUS="pk_bus";
             public static final String ACTIVE="active";
             public static final String  DEF_STR_3="def_str_3";   
             public static final String  TS="ts";   
             public static final String  DEF_STR_1="def_str_1";   
             public static final String  SYSNAME="sysname";   
             public static final String  PK_DATACHECKSTAT="pk_datacheckstat";   
             public static final String  DR="dr";   
             public static final String  CONTENT="content";   
             public static final String  DEF_INTEGER="def_integer";   
             public static final String  DEF_DOUBLE="def_double";   
             public static final String  DEF_STR_2="def_str_2";   
             public static final String  SDATE="sdate";   
             public static final String  ACTIVETYPE="activetype";   
             public static final String  ACTIVECODE="activecode";
             public static final String  EDATA="edata";
             public static final String  SUCCESS="success";
             public static final String  PK_PROCESSFLOW_B="pk_processflow_b";
      
             
//         private    IQueryField iq;
//        public   IQueryField getIQueryField(){
//        	if(iq==null){
//        		iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
//        	}
//        	return iq;
//        }   
             
        public String getPk_processflow_b() {
				return pk_processflow_b;
			}

			public void setPk_processflow_b(String pk_processflow_b) {
				this.pk_processflow_b = pk_processflow_b;
			}

		public UFBoolean getSuccess() {
				return success;
			}

			public void setSuccess(UFBoolean success) {
				this.success = success;
			}

		public String getEdate() {
				return edate;
			}

			public void setEdate(String edate) {
				this.edate = edate;
			}

		public String getPk_bus() {
				return pk_bus;
			}

			public void setPk_bus(String pk_bus) {
				this.pk_bus = pk_bus;
//				activeAddIOflag();
			}

		public String getActive() {
				return active;
			}

			public void setActive(String active) {
				this.active = active;
//				activeAddIOflag();
			}
			
			
			 /**
			   * 属性activetype的Getter方法.
			   *
			   * 创建日期:2011-5-12
			   * @return String
			   */
			 public String getActivetype() {
				 return activetype;
			  }   
			  
		     /**
			   * 属性activetype的Setter方法.
			   *
			   * 创建日期:2011-5-12
			   * @param newActivetype String
			   */
			public void setActivetype(String newActivetype) {
				
				activetype = newActivetype;
//				activeAddIOflag();
			 } 	  
		       
		        /**
			   * 属性activecode的Getter方法.
			   *
			   * 创建日期:2011-5-12
			   * @return String
			   */
			 public String getActivecode() {
				 return activecode;
			  }   
			  
		     /**
			   * 属性activecode的Setter方法.
			   *
			   * 创建日期:2011-5-12
			   * @param newActivecode String
			   */
			public void setActivecode(String newActivecode) {
				
				activecode = newActivecode;
			 } 	  
		       

		/**
	   * 属性def_str_3的Getter方法.
	   *
	   * 创建日期:2011-4-8
	   * @return String
	   */
	 public String getDef_str_3() {
		 return def_str_3;
	  }   
	  
     /**
	   * 属性def_str_3的Setter方法.
	   *
	   * 创建日期:2011-4-8
	   * @param newDef_str_3 String
	   */
	public void setDef_str_3(String newDef_str_3) {
		
		def_str_3 = newDef_str_3;
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
	   * 属性def_str_1的Getter方法.
	   *
	   * 创建日期:2011-4-8
	   * @return String
	   */
	 public String getDef_str_1() {
		 return def_str_1;
	  }   
	  
     /**
	   * 属性def_str_1的Setter方法.
	   *
	   * 创建日期:2011-4-8
	   * @param newDef_str_1 String
	   */
	public void setDef_str_1(String newDef_str_1) {
		
		def_str_1 = newDef_str_1;
	 } 	  
       
        /**
	   * 属性sysname的Getter方法.
	   *
	   * 创建日期:2011-4-8
	   * @return String
	   */
	 public String getSysname() {
		 return sysname;
	  }   
	  
     /**
	   * 属性sysname的Setter方法.
	   *
	   * 创建日期:2011-4-8
	   * @param newSysname String
	   */
	public void setSysname(String newSysname) {
		
		sysname = newSysname;
	 } 	  
       
        /**
	   * 属性pk_datacheckstat的Getter方法.
	   *
	   * 创建日期:2011-4-8
	   * @return String
	   */
	 public String getPk_datacheckstat() {
		 return pk_datacheckstat;
	  }   
	  
     /**
	   * 属性pk_datacheckstat的Setter方法.
	   *
	   * 创建日期:2011-4-8
	   * @param newPk_datacheckstat String
	   */
	public void setPk_datacheckstat(String newPk_datacheckstat) {
		
		pk_datacheckstat = newPk_datacheckstat;
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
	   * 属性content的Getter方法.
	   *
	   * 创建日期:2011-4-8
	   * @return String
	   */
	 public String getContent() {
		 return content;
	  }   
	  
     /**
	   * 属性content的Setter方法.
	   *
	   * 创建日期:2011-4-8
	   * @param newContent String
	   */
	public void setContent(String newContent) {
		
		content = newContent;
	 } 	  
       
        /**
	   * 属性def_integer的Getter方法.
	   *
	   * 创建日期:2011-4-8
	   * @return Integer
	   */
	 public Integer getDef_integer() {
		 return def_integer;
	  }   
	  
     /**
	   * 属性def_integer的Setter方法.
	   *
	   * 创建日期:2011-4-8
	   * @param newDef_integer Integer
	   */
	public void setDef_integer(Integer newDef_integer) {
		
		def_integer = newDef_integer;
	 } 	  
       
        /**
	   * 属性def_double的Getter方法.
	   *
	   * 创建日期:2011-4-8
	   * @return UFDouble
	   */
	 public UFDouble getDef_double() {
		 return def_double;
	  }   
	  
     /**
	   * 属性def_double的Setter方法.
	   *
	   * 创建日期:2011-4-8
	   * @param newDef_double UFDouble
	   */
	public void setDef_double(UFDouble newDef_double) {
		
		def_double = newDef_double;
	 } 	  
       
        /**
	   * 属性def_str_2的Getter方法.
	   *
	   * 创建日期:2011-4-8
	   * @return String
	   */
	 public String getDef_str_2() {
		 return def_str_2;
	  }   
	  
     /**
	   * 属性def_str_2的Setter方法.
	   *
	   * 创建日期:2011-4-8
	   * @param newDef_str_2 String
	   */
	public void setDef_str_2(String newDef_str_2) {
		
		def_str_2 = newDef_str_2;
	 } 	  
       
        /**
	   * 属性sdate的Getter方法.
	   *
	   * 创建日期:2011-4-8
	   * @return String
	   */
	 public String getSdate() {
		 return sdate;
	  }   
	  
     /**
	   * 属性sdate的Setter方法.
	   *
	   * 创建日期:2011-4-8
	   * @param newSdate String
	   */
	public void setSdate(String newSdate) {
		
		sdate = newSdate;
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
	
	   		if (pk_datacheckstat == null) {
			errFields.add(new String("pk_datacheckstat"));
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
	 	  return "pk_datacheckstat";
	 	}
    
	/**
      * <p>返回表名称.
	  * <p>
	  * 创建日期:2011-4-8
	  * @return java.lang.String
	 */
	public java.lang.String getTableName() {
				
		return "dip_dataprocess";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2011-4-8
	  */
	public DateprocessVO() {
			
			   super();	
	  }    
    
            /**
	 * 使用主键进行初始化的构造子.
	 *
	 * 创建日期:2011-4-8
	 * @param newPk_datacheckstat 主键值
	 */
	 public DateprocessVO(String newPk_datacheckstat) {
		
		// 为主键字段赋值:
		 pk_datacheckstat = newPk_datacheckstat;
	
    	}
    
     
     /**
	  * 返回对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2011-4-8
	  * @return String
	  */
	   public String getPrimaryKey() {
				
		 return pk_datacheckstat;
	   
	   }

     /**
	  * 设置对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2011-4-8
	  * @param newPk_datacheckstat  String    
	  */
	 public void setPrimaryKey(String newPk_datacheckstat) {
				
				pk_datacheckstat = newPk_datacheckstat; 
				
	 } 
           
	  /**
       * 返回数值对象的显示名称.
	   *
	   * 创建日期:2011-4-8
	   * @return java.lang.String 返回数值对象的显示名称.
	   */
	 public String getEntityName() {
				
	   return "dip_dataprocess"; 
				
	 }

	public String getSyscode() {
		return syscode;
	}

	public void setSyscode(String syscode) {
		this.syscode = syscode;
	} 
	
//	public void activeAddIOflag(){
//		if(active!=null&&(active.endsWith("-输入")||active.endsWith("-输出"))){
//			return;
//		}
//		if(activetype!=null&&activetype.equals("数据同步")&&pk_bus!=null&&!pk_bus.trim().equals("")&&active!=null&&!active.trim().equals("")){
//			String sql="select hh.ioflag from dip_datarec_h hh where hh.pk_datarec_h in (select ch.dataname from dip_datasynch ch where ch.pk_datasynch='"+pk_bus+"')";
//			iq=getIQueryField();
//			String value="";
//			try {
//				value=iq.queryfield(sql);
//			} catch (BusinessException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (DbException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			if(value!=null&&!value.trim().equals("")){
//				this.active=this.active+"-"+value;
//			}
//		}
//	}
} 
