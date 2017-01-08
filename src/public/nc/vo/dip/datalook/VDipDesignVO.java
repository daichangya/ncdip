      	package nc.vo.dip.datalook;
   	
	import nc.util.dip.sj.IContrastUtil;
import nc.vo.pub.*;
import nc.vo.pub.lang.*;
	
/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 *
 * <p>
 *     �ڴ˴���Ӵ����������Ϣ
 * </p>
 *
 * ��������:2011-5-12
 * @author ${vmObject.author}
 * @version Your Project 1.0
 */
     public class VDipDesignVO extends SuperVO {
           
             public String pk_design;
             public String pk_datadefinit_h;
             public String pk_datadefinit_b;
             public String cname;
             public String ename;
             public UFBoolean isdisplay;
             public Integer disno;
             public Integer designtype;
             public String ts;
             public Integer dr;
             public Integer designlen;
             public Integer desigplen;
             public String contype;
             public UFBoolean ispk;
             public String datatype;
             public String conspk;
             public String consvalue;
             public UFBoolean islock;
             
             
            
       
   	public UFBoolean getIslock() {
				return islock;
			}

			public void setIslock(UFBoolean islock) {
				this.islock = islock;
			}

	public UFBoolean getIspk() {
				return ispk;
			}

			public void setIspk(UFBoolean ispk) {
				this.ispk = ispk;
			}

	public String getContype() {
				return contype;
			}

			public void setContype(String contype) {
				this.contype = contype;
			}

	public Integer getDesignlen() {
				return designlen;
			}

			public void setDesignlen(Integer designlen) {
				this.designlen = designlen;
			}

	public String getCname() {
				return cname;
			}

			public void setCname(String cname) {
				this.cname = cname;
			}

			public Integer getDesigntype() {
				return designtype;
			}

			public void setDesigntype(Integer designtype) {
				this.designtype = designtype;
			}

			public Integer getDisno() {
				return disno;
			}

			public void setDisno(Integer disno) {
				this.disno = disno;
			}

			public Integer getDr() {
				return dr;
			}

			public void setDr(Integer dr) {
				this.dr = dr;
			}

			public String getEname() {
				return ename;
			}

			public void setEname(String ename) {
				this.ename = ename;
			}

			public UFBoolean getIsdisplay() {
				return isdisplay;
			}

			public void setIsdisplay(UFBoolean isdisplay) {
				this.isdisplay = isdisplay;
			}

			public String getPk_datadefinit_b() {
				return pk_datadefinit_b;
			}

			public void setPk_datadefinit_b(String pk_datadefinit_b) {
				this.pk_datadefinit_b = pk_datadefinit_b;
			}

			public String getPk_datadefinit_h() {
				return pk_datadefinit_h;
			}

			public void setPk_datadefinit_h(String pk_datadefinit_h) {
				this.pk_datadefinit_h = pk_datadefinit_h;
			}

			public String getPk_design() {
				return pk_design;
			}

			public void setPk_design(String pk_design) {
				this.pk_design = pk_design;
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
	  * <p>ȡ�ø�VO�����ֶ�.
	  * <p>
	  * ��������:2011-5-12
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	  	 
	 	    return null;
	 	
	}   
    
    /**
	  * <p>ȡ�ñ�����.
	  * <p>
	  * ��������:2011-5-12
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	 	  return "pk_design";
	 	}
    
	/**
      * <p>���ر�����.
	  * <p>
	  * ��������:2011-5-12
	  * @return java.lang.String
	 */
	public java.lang.String getTableName() {
				
		return "v_dip_design";
	}    
    
    /**
	  * ����Ĭ�Ϸ�ʽ����������.
	  *
	  * ��������:2011-5-12
	  */
	public VDipDesignVO() {
			
			   super();	
	  }    
    
    
     
     /**
	  * ���ض����ʶ,����Ψһ��λ����.
	  *
	  * ��������:2011-5-12
	  * @return String
	  */
	   public String getPrimaryKey() {
				
		 return pk_design;
	   
	   }

     /**
	  * ���ö����ʶ,����Ψһ��λ����.
	  *
	  * ��������:2011-5-12
	  * @param newPk_datalook  String    
	  */
	 public void setPrimaryKey(String newPk_datalook) {
				
		 pk_design = newPk_datalook; 
				
	 } 
           
	  /**
       * ������ֵ�������ʾ����.
	   *
	   * ��������:2011-5-12
	   * @return java.lang.String ������ֵ�������ʾ����.
	   */
	 public String getEntityName() {
				
	   return "v_dip_design"; 
				
	 }

	public Integer getDesigplen() {
		return desigplen;
	}

	public void setDesigplen(Integer desigplen) {
		this.desigplen = desigplen;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getConspk() {
		return conspk;
	}

	public void setConspk(String conspk) {
		this.conspk = conspk;
	}

	public String getConsvalue() {
		return consvalue;
	}

	public void setConsvalue(String consvalue) {
		this.consvalue = consvalue;
	}


	
} 
