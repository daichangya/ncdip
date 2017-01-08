  /***************************************************************\
  *     The skeleton of this class is generated by an automatic *
  * code generator for NC product. It is based on Velocity.     *
  \***************************************************************/
      	package nc.vo.dip.esbfilesend;
   	
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
 * ��������:2011-4-21
 * @author ${vmObject.author}
 * @version Your Project 1.0
 */
     public class DipEsbSendVO extends SuperVO {
           
             public String def_str2;
             public String tasktype;
             public String contorlstatus;
             public Integer dr;
             public String def_str3;
             public String filepath;
             public String endstatus;
             public String code;
             public String ts;
             public String threadno;
             public String def_str1;
             public String pk_esbsend;
             public String server;
             public String name;
             public String def_str4;
             public UFBoolean issendfolder;
             public String pk_sys;
             public String fpk;
             public String pk_xt;
             public String sendfilename;
             public UFBoolean isfolder;
             public String bakpath;
             public String deeltype;
             
            
             public static final String  DEF_STR2="def_str2";   
             public static final String  TASKTYPE="tasktype";   
             public static final String  CONTORLSTATUS="contorlstatus";   
             public static final String  BUSINESSDATA="businessdata";   
             public static final String  DR="dr";   
             public static final String  DEF_STR3="def_str3";   
             public static final String  FILEPATH="filepath";   
             public static final String  ENDSTATUS="endstatus";   
             public static final String  CODE="code";   
             public static final String  TS="ts";   
             public static final String  THREADNO="threadno";   
             public static final String  DEF_STR1="def_str1";   
             public static final String  PK_DATASEND="pk_datasend";   
             public static final String  SERVER="server";   
             public static final String  NAME="name";   
             public static final String  DEF_STR4="def_str4";   
             public static final String  PK_SYS="pk_sys";   
             public static final String  ISSENDBACKGROUND="issendbackground";   
      
    
        public String getPk_sys() {
				return pk_sys;
			}

			public void setPk_sys(String pk_sys) {
				this.pk_sys = pk_sys;
			}

		/**
	   * ����def_str2��Getter����.
	   *
	   * ��������:2011-4-21
	   * @return String
	   */
	 public String getDef_str2() {
		 return def_str2;
	  }   
	  
     /**
	   * ����def_str2��Setter����.
	   *
	   * ��������:2011-4-21
	   * @param newDef_str2 String
	   */
	public void setDef_str2(String newDef_str2) {
		
		def_str2 = newDef_str2;
	 } 	  
       
        /**
	   * ����tasktype��Getter����.
	   *
	   * ��������:2011-4-21
	   * @return String
	   */
	 public String getTasktype() {
		 return tasktype;
	  }   
	  
     /**
	   * ����tasktype��Setter����.
	   *
	   * ��������:2011-4-21
	   * @param newTasktype String
	   */
	public void setTasktype(String newTasktype) {
		
		tasktype = newTasktype;
	 } 	  
       
        /**
	   * ����contorlstatus��Getter����.
	   *
	   * ��������:2011-4-21
	   * @return String
	   */
	 public String getContorlstatus() {
		 return contorlstatus;
	  }   
	  
     /**
	   * ����contorlstatus��Setter����.
	   *
	   * ��������:2011-4-21
	   * @param newContorlstatus String
	   */
	public void setContorlstatus(String newContorlstatus) {
		
		contorlstatus = newContorlstatus;
	 } 	  
       
       
        /**
	   * ����dr��Getter����.
	   *
	   * ��������:2011-4-21
	   * @return Integer
	   */
	 public Integer getDr() {
		 return dr;
	  }   
	  
     /**
	   * ����dr��Setter����.
	   *
	   * ��������:2011-4-21
	   * @param newDr Integer
	   */
	public void setDr(Integer newDr) {
		
		dr = newDr;
	 } 	  
       
        /**
	   * ����def_str3��Getter����.
	   *
	   * ��������:2011-4-21
	   * @return String
	   */
	 public String getDef_str3() {
		 return def_str3;
	  }   
	  
     /**
	   * ����def_str3��Setter����.
	   *
	   * ��������:2011-4-21
	   * @param newDef_str3 String
	   */
	public void setDef_str3(String newDef_str3) {
		
		def_str3 = newDef_str3;
	 } 	  
       
        /**
	   * ����filepath��Getter����.
	   *
	   * ��������:2011-4-21
	   * @return String
	   */
	 public String getFilepath() {
		 return filepath;
	  }   
	  
     /**
	   * ����filepath��Setter����.
	   *
	   * ��������:2011-4-21
	   * @param newFilepath String
	   */
	public void setFilepath(String newFilepath) {
		
		filepath = newFilepath;
	 } 	  
       
        /**
	   * ����endstatus��Getter����.
	   *
	   * ��������:2011-4-21
	   * @return String
	   */
	 public String getEndstatus() {
		 return endstatus;
	  }   
	  
     /**
	   * ����endstatus��Setter����.
	   *
	   * ��������:2011-4-21
	   * @param newEndstatus String
	   */
	public void setEndstatus(String newEndstatus) {
		
		endstatus = newEndstatus;
	 } 	  
       
        /**
	   * ����code��Getter����.
	   *
	   * ��������:2011-4-21
	   * @return String
	   */
	 public String getCode() {
		 return code;
	  }   
	  
     /**
	   * ����code��Setter����.
	   *
	   * ��������:2011-4-21
	   * @param newCode String
	   */
	public void setCode(String newCode) {
		
		code = newCode;
	 } 	  
       
        /**
	   * ����ts��Getter����.
	   *
	   * ��������:2011-4-21
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
	   * ��������:2011-4-21
	   * @param newTs UFDateTime
	   */
	public void setTs(String newTs) {
		
		ts = newTs;
	 } 	  
       
        /**
	   * ����threadno��Getter����.
	   *
	   * ��������:2011-4-21
	   * @return String
	   */
	 public String getThreadno() {
		 return threadno;
	  }   
	  
     /**
	   * ����threadno��Setter����.
	   *
	   * ��������:2011-4-21
	   * @param newThreadno String
	   */
	public void setThreadno(String newThreadno) {
		
		threadno = newThreadno;
	 } 	  
       
        /**
	   * ����def_str1��Getter����.
	   *
	   * ��������:2011-4-21
	   * @return String
	   */
	 public String getDef_str1() {
		 return def_str1;
	  }   
	  
     /**
	   * ����def_str1��Setter����.
	   *
	   * ��������:2011-4-21
	   * @param newDef_str1 String
	   */
	public void setDef_str1(String newDef_str1) {
		
		def_str1 = newDef_str1;
	 } 	  
       
       
        /**
	   * ����server��Getter����.
	   *
	   * ��������:2011-4-21
	   * @return String
	   */
	 public String getServer() {
		 return server;
	  }   
	  
     /**
	   * ����server��Setter����.
	   *
	   * ��������:2011-4-21
	   * @param newServer String
	   */
	public void setServer(String newServer) {
		
		server = newServer;
	 } 	  
       
        /**
	   * ����name��Getter����.
	   *
	   * ��������:2011-4-21
	   * @return String
	   */
	 public String getName() {
		 return name;
	  }   
	  
     /**
	   * ����name��Setter����.
	   *
	   * ��������:2011-4-21
	   * @param newName String
	   */
	public void setName(String newName) {
		
		name = newName;
	 } 	  
       
        /**
	   * ����def_str4��Getter����.
	   *
	   * ��������:2011-4-21
	   * @return String
	   */
	 public String getDef_str4() {
		 return def_str4;
	  }   
	  
     /**
	   * ����def_str4��Setter����.
	   *
	   * ��������:2011-4-21
	   * @param newDef_str4 String
	   */
	public void setDef_str4(String newDef_str4) {
		
		def_str4 = newDef_str4;
	 } 	  
       
       
    /**
	  * ��֤���������֮��������߼���ȷ��.
	  *
	  * ��������:2011-4-21
	  * @exception nc.vo.pub.ValidationException �����֤ʧ��,�׳�
	  * ValidationException,�Դ�����н���.
	 */
	 public void validate() throws ValidationException {
	
	 	ArrayList errFields = new ArrayList(); // errFields record those null

                                                      // fields that cannot be null.
       		  // ����Ƿ�Ϊ�������յ��ֶθ��˿�ֵ,�������Ҫ�޸��������ʾ��Ϣ:
	
	   		if (pk_esbsend == null) {
			errFields.add(new String("pk_esbsend"));
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
	  * ��������:2011-4-21
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	  	 
	 	    return "fpk";
	 	
	}   
    
    /**
	  * <p>ȡ�ñ�����.
	  * <p>
	  * ��������:2011-4-21
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	 	  return "pk_esbsend";
	 	}
    
	/**
      * <p>���ر�����.
	  * <p>
	  * ��������:2011-4-21
	  * @return java.lang.String
	 */
	public java.lang.String getTableName() {
				
		return "dip_esbsend";
	}    
    
    /**
	  * ����Ĭ�Ϸ�ʽ����������.
	  *
	  * ��������:2011-4-21
	  */
	public DipEsbSendVO() {
			
			   super();	
	  }    
    
            /**
	 * ʹ���������г�ʼ���Ĺ�����.
	 *
	 * ��������:2011-4-21
	 * @param newPk_datasend ����ֵ
	 */
	 public DipEsbSendVO(String newPk_datasend) {
		
		// Ϊ�����ֶθ�ֵ:
		 pk_esbsend = newPk_datasend;
	
    	}
    
     
     /**
	  * ���ض����ʶ,����Ψһ��λ����.
	  *
	  * ��������:2011-4-21
	  * @return String
	  */
	   public String getPrimaryKey() {
				
		 return pk_esbsend;
	   
	   }

     /**
	  * ���ö����ʶ,����Ψһ��λ����.
	  *
	  * ��������:2011-4-21
	  * @param newPk_datasend  String    
	  */
	 public void setPrimaryKey(String newPk_datasend) {
				
		 pk_esbsend = newPk_datasend; 
				
	 } 
           
	  /**
       * ������ֵ�������ʾ����.
	   *
	   * ��������:2011-4-21
	   * @return java.lang.String ������ֵ�������ʾ����.
	   */
	 public String getEntityName() {
				
	   return "dip_esbsend"; 
				
	 }

	public String getFpk() {
		return fpk;
	}

	public void setFpk(String fpk) {
		this.fpk = fpk;
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

	public UFBoolean getIssendfolder() {
		return issendfolder;
	}

	public void setIssendfolder(UFBoolean issendfolder) {
		this.issendfolder = issendfolder;
	}

	public String getPk_esbsend() {
		return pk_esbsend;
	}

	public void setPk_esbsend(String pk_esbsend) {
		this.pk_esbsend = pk_esbsend;
	}

	public String getSendfilename() {
		return sendfilename;
	}

	public void setSendfilename(String sendfilename) {
		this.sendfilename = sendfilename;
	}

	public String getBakpath() {
		return bakpath;
	}

	public void setBakpath(String bakpath) {
		this.bakpath = bakpath;
	}

	public String getDeeltype() {
		return deeltype;
	}

	public void setDeeltype(String deeltype) {
		this.deeltype = deeltype;
	} 
} 