  /***************************************************************\
  *     The skeleton of this class is generated by an automatic *
  * code generator for NC product. It is based on Velocity.     *
  \***************************************************************/
      	package nc.vo.dip.datarec;
   	
	import java.util.ArrayList;

import nc.util.dip.sj.IContrastUtil;
	import nc.vo.pub.*;
import nc.vo.pub.lang.*;
	
/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 *
 * <p>
 *     �ڴ˴����Ӵ����������Ϣ
 * </p>
 *
 * ��������:2011-4-7
 * @author ${vmObject.author}
 * @version Your Project 1.0
 */
     public class DipDatarecBVO extends SuperVO {
           
             public String messformat;
             public String colno;
             public String flowtype;
             public String managepro;
             public String def_str_1;
             public String ts;
             public String beginflag;
             public Integer dr;
             public String pk_datarec_b;
             public String def_str_2;
             public String pk_datarec_h;
             
             public UFBoolean trancon;
             public String formatcode;
             public String formatname;
             public String sourcetype;
             public String sourcecon;
             public String sourceparam;
             public String format;
            
             public static final String  MESSFORMAT="messformat";   
             public static final String  COLNO="colno";   
             public static final String  FLOWTYPE="flowtype";   
             public static final String  MANAGEPRO="managepro";   
             public static final String  DEF_STR_1="def_str_1";   
             public static final String  TS="ts";   
             public static final String  BEGINFLAG="beginflag";   
             public static final String  DR="dr";   
             public static final String  PK_DATAREC_B="pk_datarec_b";   
             public static final String  DEF_STR_2="def_str_2";   
             public static final String  PK_DATAREC_H="pk_datarec_h";   
      
    
             public static final String  TRANCON="trancon";   
             public static final String  FROMATCODE="formatcode";
             public static final String  FORMATNAME="formatname";
             public static final String  FORMAT="format"; 
             public static final String  SOURCEPARAM="sourceparam"; 
             public static final String  SOURCECON="sourcecon";   
             public static final String  SOURCETYPE="sourcetype";  
             
             public String getSourceparam() {
         		return sourceparam;
         	}

         	public void setSourceparam(String newSourceparam) {
         		this.sourceparam = newSourceparam;
         	} 
         	
             /**
       	   * ����trancon��Getter����.
       	   *
       	   * ��������:2011-4-7
       	   * @return UFBoolean
       	   */
       	 public UFBoolean getTrancon() {
       		 return trancon;
       	  }   
       	  
            /**
       	   * ����trancon��Setter����.
       	   *
       	   * ��������:2011-4-7
       	   * @param newTrancon UFBoolean
       	   */
       	public void setTrancon(UFBoolean newTrancon) {
       		
       		trancon = newTrancon;
       	 } 	  
              
               /**
       	   * ����format��Getter����.
       	   *
       	   * ��������:2011-4-7
       	   * @return String
       	   */
       	 public String getFormat() {
       		 return format;
       	  }   
       	  
            /**
       	   * ����format��Setter����.
       	   *
       	   * ��������:2011-4-7
       	   * @param newFormat String
       	   */
       	public void setFormat(String newFormat) {
       		
       		format = newFormat;
       	 } 	  
              
             /**
       	   * ����sourcecon��Getter����.
       	   *
       	   * ��������:2011-4-7
       	   * @return String
       	   */
       	 public String getSourcecon() {
       		 return sourcecon;
       	  }   
       	  
            /**
       	   * ����sourcecon��Setter����.
       	   *
       	   * ��������:2011-4-7
       	   * @param newSourcecon String
       	   */
       	public void setSourcecon(String newSourcecon) {
       		
       		sourcecon = newSourcecon;
       	 } 	  
              
               /**
       	   * ����sourcetype��Getter����.
       	   *
       	   * ��������:2011-4-7
       	   * @return String
       	   */
       	 public String getSourcetype() {
       		 return sourcetype;
       	  }   
       	  
            /**
       	   * ����sourcetype��Setter����.
       	   *
       	   * ��������:2011-4-7
       	   * @param newSourcetype String
       	   */
       	public void setSourcetype(String newSourcetype) {
       		
       		sourcetype = newSourcetype;
       	 } 	  
        /**
	   * ����messformat��Getter����.
	   *
	   * ��������:2011-4-7
	   * @return String
	   */
	 public String getMessformat() {
		 return messformat;
	  }   
	  
     /**
	   * ����messformat��Setter����.
	   *
	   * ��������:2011-4-7
	   * @param newMessformat String
	   */
	public void setMessformat(String newMessformat) {
		
		messformat = newMessformat;
	 } 	  
       
        /**
	   * ����colno��Getter����.
	   *
	   * ��������:2011-4-7
	   * @return String
	   */
	 public String getColno() {
		 return colno;
	  }   
	  
     /**
	   * ����colno��Setter����.
	   *
	   * ��������:2011-4-7
	   * @param newColno String
	   */
	public void setColno(String newColno) {
		
		colno = newColno;
	 } 	  
       
        /**
	   * ����flowtype��Getter����.
	   *
	   * ��������:2011-4-7
	   * @return String
	   */
	 public String getFlowtype() {
		 return flowtype;
	  }   
	  
     /**
	   * ����flowtype��Setter����.
	   *
	   * ��������:2011-4-7
	   * @param newFlowtype String
	   */
	public void setFlowtype(String newFlowtype) {
		
		flowtype = newFlowtype;
	 } 	  
       
        /**
	   * ����managepro��Getter����.
	   *
	   * ��������:2011-4-7
	   * @return String
	   */
	 public String getManagepro() {
		 return managepro;
	  }   
	  
     /**
	   * ����managepro��Setter����.
	   *
	   * ��������:2011-4-7
	   * @param newManagepro String
	   */
	public void setManagepro(String newManagepro) {
		
		managepro = newManagepro;
	 } 	  
       
        /**
	   * ����def_str_1��Getter����.
	   *
	   * ��������:2011-4-7
	   * @return String
	   */
	 public String getDef_str_1() {
		 return def_str_1;
	  }   
	  
     /**
	   * ����def_str_1��Setter����.
	   *
	   * ��������:2011-4-7
	   * @param newDef_str_1 String
	   */
	public void setDef_str_1(String newDef_str_1) {
		
		def_str_1 = newDef_str_1;
	 } 	  
       
        /**
	   * ����ts��Getter����.
	   *
	   * ��������:2011-4-7
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
	   * ��������:2011-4-7
	   * @param newTs UFDateTime
	   */
	public void setTs(String newTs) {
		
		ts = newTs;
	 } 	  
       
        /**
	   * ����beginflag��Getter����.
	   *
	   * ��������:2011-4-7
	   * @return String
	   */
	 public String getBeginflag() {
		 return beginflag;
	  }   
	  
     /**
	   * ����beginflag��Setter����.
	   *
	   * ��������:2011-4-7
	   * @param newBeginflag String
	   */
	public void setBeginflag(String newBeginflag) {
		
		beginflag = newBeginflag;
	 } 	  
       
        /**
	   * ����dr��Getter����.
	   *
	   * ��������:2011-4-7
	   * @return Integer
	   */
	 public Integer getDr() {
		 return dr;
	  }   
	  
     /**
	   * ����dr��Setter����.
	   *
	   * ��������:2011-4-7
	   * @param newDr Integer
	   */
	public void setDr(Integer newDr) {
		
		dr = newDr;
	 } 	  
       
        /**
	   * ����pk_datarec_b��Getter����.
	   *
	   * ��������:2011-4-7
	   * @return String
	   */
	 public String getPk_datarec_b() {
		 return pk_datarec_b;
	  }   
	  
     /**
	   * ����pk_datarec_b��Setter����.
	   *
	   * ��������:2011-4-7
	   * @param newPk_datarec_b String
	   */
	public void setPk_datarec_b(String newPk_datarec_b) {
		
		pk_datarec_b = newPk_datarec_b;
	 } 	  
       
        /**
	   * ����def_str_2��Getter����.
	   *
	   * ��������:2011-4-7
	   * @return String
	   */
	 public String getDef_str_2() {
		 return def_str_2;
	  }   
	  
     /**
	   * ����def_str_2��Setter����.
	   *
	   * ��������:2011-4-7
	   * @param newDef_str_2 String
	   */
	public void setDef_str_2(String newDef_str_2) {
		
		def_str_2 = newDef_str_2;
	 } 	  
       
        /**
	   * ����pk_datarec_h��Getter����.
	   *
	   * ��������:2011-4-7
	   * @return String
	   */
	 public String getPk_datarec_h() {
		 return pk_datarec_h;
	  }   
	  
     /**
	   * ����pk_datarec_h��Setter����.
	   *
	   * ��������:2011-4-7
	   * @param newPk_datarec_h String
	   */
	public void setPk_datarec_h(String newPk_datarec_h) {
		
		pk_datarec_h = newPk_datarec_h;
	 } 	  
       
       
    /**
	  * ��֤���������֮��������߼���ȷ��.
	  *
	  * ��������:2011-4-7
	  * @exception nc.vo.pub.ValidationException �����֤ʧ��,�׳�
	  * ValidationException,�Դ�����н���.
	 */
	 public void validate() throws ValidationException {
	
	 	ArrayList errFields = new ArrayList(); // errFields record those null

                                                      // fields that cannot be null.
       		  // ����Ƿ�Ϊ�������յ��ֶθ��˿�ֵ,�������Ҫ�޸��������ʾ��Ϣ:
	
	   		if (pk_datarec_b == null) {
			errFields.add(new String("pk_datarec_b"));
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
	  * ��������:2011-4-7
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	  	 
	 		return "pk_datarec_h";
	 	
	}   
    
    /**
	  * <p>ȡ�ñ�����.
	  * <p>
	  * ��������:2011-4-7
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	 	  return "pk_datarec_b";
	 	}
    
	/**
      * <p>���ر�����.
	  * <p>
	  * ��������:2011-4-7
	  * @return java.lang.String
	 */
	public java.lang.String getTableName() {
				
		return "dip_datarec_b";
	}    
    
    /**
	  * ����Ĭ�Ϸ�ʽ����������.
	  *
	  * ��������:2011-4-7
	  */
	public DipDatarecBVO() {
			
			   super();	
	  }    
    
            /**
	 * ʹ���������г�ʼ���Ĺ�����.
	 *
	 * ��������:2011-4-7
	 * @param newPk_datarec_b ����ֵ
	 */
	 public DipDatarecBVO(String newPk_datarec_b) {
		
		// Ϊ�����ֶθ�ֵ:
		 pk_datarec_b = newPk_datarec_b;
	
    	}
    
     
     /**
	  * ���ض����ʶ,����Ψһ��λ����.
	  *
	  * ��������:2011-4-7
	  * @return String
	  */
	   public String getPrimaryKey() {
				
		 return pk_datarec_b;
	   
	   }

     /**
	  * ���ö����ʶ,����Ψһ��λ����.
	  *
	  * ��������:2011-4-7
	  * @param newPk_datarec_b  String    
	  */
	 public void setPrimaryKey(String newPk_datarec_b) {
				
				pk_datarec_b = newPk_datarec_b; 
				
	 } 
           
	  /**
       * ������ֵ�������ʾ����.
	   *
	   * ��������:2011-4-7
	   * @return java.lang.String ������ֵ�������ʾ����.
	   */
	 public String getEntityName() {
				
	   return "dip_datarec_b"; 
				
	 }

	public String getFormatcode() {
		return formatcode;
	}

	public void setFormatcode(String newFormatcode) {
		this.formatcode = newFormatcode;
	}

	public String getFormatname() {
		return formatname;
	}

	public void setFormatname(String newFormatname) {
		this.formatname = newFormatname;
	} 
} 