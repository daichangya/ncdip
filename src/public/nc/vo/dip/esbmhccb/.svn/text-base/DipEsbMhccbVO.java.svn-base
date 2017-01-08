 package nc.vo.dip.esbmhccb;
   	
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
 * 创建日期:2011-5-12
 * @author ${vmObject.author}
 * @version Your Project 1.0
 */
     public class DipEsbMhccbVO extends SuperVO {
           
             public String ts;
             public Integer dr;
             public String pk_esb_mhccb;
             public String mcode;
             public String mname;
             public String pk_msr;
             public String sdate;
             public String msg;
             String entity;
public DipEsbMhccbVO(String tablenamne){
	super();
	this.entity=tablenamne;
}

			public Integer getDr() {
				return dr;
			}

			public void setDr(Integer dr) {
				this.dr = dr;
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

	/**
	  * <p>取得父VO主键字段.
	  * <p>
	  * 创建日期:2011-5-12
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	  	 
	 	    return null;
	 	
	}   
    
    /**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2011-5-12
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	 	  return "pk_esb_mhccb";
	 	}
    
	/**
      * <p>返回表名称.
	  * <p>
	  * 创建日期:2011-5-12
	  * @return java.lang.String
	 */
	public java.lang.String getTableName() {
				
		return entity;
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2011-5-12
	  */
	public DipEsbMhccbVO() {
			
			   super();	
	  }    
    
    
     
     /**
	  * 返回对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2011-5-12
	  * @return String
	  */
	   public String getPrimaryKey() {
				
		 return pk_esb_mhccb;
	   
	   }

     /**
	  * 设置对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2011-5-12
	  * @param newPk_datalook  String    
	  */
	 public void setPrimaryKey(String spk_esb_mhccb) {
				
		 pk_esb_mhccb = spk_esb_mhccb; 
				
	 } 
           
	  /**
       * 返回数值对象的显示名称.
	   *
	   * 创建日期:2011-5-12
	   * @return java.lang.String 返回数值对象的显示名称.
	   */
	 public String getEntityName() {
				
	   return entity; 
				
	 }

	public String getMcode() {
		return mcode;
	}

	public void setMcode(String mcode) {
		this.mcode = mcode;
	}

	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getPk_esb_mhccb() {
		return pk_esb_mhccb;
	}

	public void setPk_esb_mhccb(String pk_esb_mhccb) {
		this.pk_esb_mhccb = pk_esb_mhccb;
	}

	public String getPk_msr() {
		return pk_msr;
	}

	public void setPk_msr(String pk_msr) {
		this.pk_msr = pk_msr;
	}

	public String getSdate() {
		return sdate;
	}

	public void setSdate(String sdate) {
		this.sdate = sdate;
	}


} 
