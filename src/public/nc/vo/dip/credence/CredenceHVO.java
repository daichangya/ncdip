/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
  \***************************************************************/
package nc.vo.dip.credence;

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
 * 创建日期:2011-4-12
 * @author author
 * @version Your Project 1.0
 */
public class CredenceHVO extends SuperVO {

	public String systype;
	public String doperatordate;
	public String vdef4;
	public String unit;
	public String attmentnum;
	public String busdata;
	public Integer dr;
	public String vdef3;
	public String voperatorid;
	public String credtype;
	public String accoutbook;
	public String code;
	public String vdef2;
	public String ts;
	public UFBoolean sysmodel;
	public String vdef1;
	public String vdef5;
	public String name;
	public String pk_credence_h;
	public String pk_datadefinit_h;
	public String def_credtype;
	public String def_attmentnum;
	public String def_voperatorid; 
	public String def_doperatordate;
	public String corp;


	public static final String CORP="corp";
	public static final String  SYSTYPE="systype";   
	public static final String  DOPERATORDATE="doperatordate";   
	public static final String  VDEF4="vdef4";   
	public static final String  UNIT="unit";   
	public static final String  ATTMENTNUM="attmentnum";   
	public static final String  BUSDATA="busdata";   
	public static final String  DR="dr";   
	public static final String  VDEF3="vdef3";   
	public static final String  VOPERATORID="voperatorid";   
	public static final String  CREDTYPE="credtype";   
	public static final String  ACCOUTBOOK="accoutbook";   
	public static final String  CODE="code";   
	public static final String  VDEF2="vdef2";   
	public static final String  TS="ts";   
	public static final String  SYSMODEL="sysmodel";   
	public static final String  VDEF1="vdef1";   
	public static final String  VDEF5="vdef5";   
	public static final String  NAME="name";   
	public static final String  PK_CREDENCE_H="pk_credence_h";  
	public static final String PK_DATADEFINIT_H="pk_datadefinit_h";


	public String getCorp() {
		return corp;
	}

	public void setCorp(String corp) {
		this.corp = corp;
	}

	public String getDef_attmentnum() {
		return def_attmentnum;
	}

	public void setDef_attmentnum(String def_attmentnum) {
		this.def_attmentnum = def_attmentnum;
	}

	public String getDef_credtype() {
		return def_credtype;
	}

	public void setDef_credtype(String def_credtype) {
		this.def_credtype = def_credtype;
	}

	public String getDef_doperatordate() {
		return def_doperatordate;
	}

	public void setDef_doperatordate(String def_doperatordate) {
		this.def_doperatordate = def_doperatordate;
	}

	public String getDef_voperatorid() {
		return def_voperatorid;
	}

	public void setDef_voperatorid(String def_voperatorid) {
		this.def_voperatorid = def_voperatorid;
	}

	public String getAttmentnum() {
		return attmentnum;
	}

	public void setAttmentnum(String attmentnum) {
		this.attmentnum = attmentnum;
	}

	public String getDoperatordate() {
		return doperatordate;
	}

	public void setDoperatordate(String doperatordate) {
		this.doperatordate = doperatordate;
	}


	public UFBoolean getSysmodel() {
		return sysmodel;
	}

	public void setSysmodel(UFBoolean sysmodel) {
		this.sysmodel = sysmodel;
	}

	public String getPk_datadefinit_h() {
		return pk_datadefinit_h;
	}

	public void setPk_datadefinit_h(String pk_datadefinit_h) {
		this.pk_datadefinit_h = pk_datadefinit_h;
	}

	/**
	 * 属性systype的Getter方法.
	 *
	 * 创建日期:2011-4-12
	 * @return String
	 */
	 public String getSystype() {
		 return systype;
	 }   

	 /**
	  * 属性systype的Setter方法.
	  *
	  * 创建日期:2011-4-12
	  * @param newSystype String
	  */
	 public void setSystype(String newSystype) {

		 systype = newSystype;
	 } 	  

	 /**
	  * 属性vdef4的Getter方法.
	  *
	  * 创建日期:2011-4-12
	  * @return String
	  */
	 public String getVdef4() {
		 return vdef4;
	 }   

	 /**
	  * 属性vdef4的Setter方法.
	  *
	  * 创建日期:2011-4-12
	  * @param newVdef4 String
	  */
	 public void setVdef4(String newVdef4) {

		 vdef4 = newVdef4;
	 } 	  

	 /**
	  * 属性unit的Getter方法.
	  *
	  * 创建日期:2011-4-12
	  * @return String
	  */
	 public String getUnit() {
		 return unit;
	 }   

	 /**
	  * 属性unit的Setter方法.
	  *
	  * 创建日期:2011-4-12
	  * @param newUnit String
	  */
	 public void setUnit(String newUnit) {

		 unit = newUnit;
	 } 	  
	 /**
	  * 属性busdata的Getter方法.
	  *
	  * 创建日期:2011-4-12
	  * @return String
	  */
	 public String getBusdata() {
		 return busdata;
	 }   

	 /**
	  * 属性busdata的Setter方法.
	  *
	  * 创建日期:2011-4-12
	  * @param newBusdata String
	  */
	 public void setBusdata(String newBusdata) {

		 busdata = newBusdata;
	 } 	  

	 /**
	  * 属性dr的Getter方法.
	  *
	  * 创建日期:2011-4-12
	  * @return Integer
	  */
	 public Integer getDr() {
		 return dr;
	 }   

	 /**
	  * 属性dr的Setter方法.
	  *
	  * 创建日期:2011-4-12
	  * @param newDr Integer
	  */
	 public void setDr(Integer newDr) {

		 dr = newDr;
	 } 	  

	 /**
	  * 属性vdef3的Getter方法.
	  *
	  * 创建日期:2011-4-12
	  * @return String
	  */
	 public String getVdef3() {
		 return vdef3;
	 }   

	 /**
	  * 属性vdef3的Setter方法.
	  *
	  * 创建日期:2011-4-12
	  * @param newVdef3 String
	  */
	 public void setVdef3(String newVdef3) {

		 vdef3 = newVdef3;
	 } 	  

	 /**
	  * 属性voperatorid的Getter方法.
	  *
	  * 创建日期:2011-4-12
	  * @return String
	  */
	 public String getVoperatorid() {
		 return voperatorid;
	 }   

	 /**
	  * 属性voperatorid的Setter方法.
	  *
	  * 创建日期:2011-4-12
	  * @param newVoperatorid String
	  */
	 public void setVoperatorid(String newVoperatorid) {

		 voperatorid = newVoperatorid;
	 } 	  

	 /**
	  * 属性credtype的Getter方法.
	  *
	  * 创建日期:2011-4-12
	  * @return String
	  */
	 public String getCredtype() {
		 return credtype;
	 }   

	 /**
	  * 属性credtype的Setter方法.
	  *
	  * 创建日期:2011-4-12
	  * @param newCredtype String
	  */
	 public void setCredtype(String newCredtype) {

		 credtype = newCredtype;
	 } 	  

	 /**
	  * 属性accoutbook的Getter方法.
	  *
	  * 创建日期:2011-4-12
	  * @return String
	  */
	 public String getAccoutbook() {
		 return accoutbook;
	 }   

	 /**
	  * 属性accoutbook的Setter方法.
	  *
	  * 创建日期:2011-4-12
	  * @param newAccoutbook String
	  */
	 public void setAccoutbook(String newAccoutbook) {

		 accoutbook = newAccoutbook;
	 } 	  

	 /**
	  * 属性code的Getter方法.
	  *
	  * 创建日期:2011-4-12
	  * @return String
	  */
	 public String getCode() {
		 return code;
	 }   

	 /**
	  * 属性code的Setter方法.
	  *
	  * 创建日期:2011-4-12
	  * @param newCode String
	  */
	 public void setCode(String newCode) {

		 code = newCode;
	 } 	  

	 /**
	  * 属性vdef2的Getter方法.
	  *
	  * 创建日期:2011-4-12
	  * @return String
	  */
	 public String getVdef2() {
		 return vdef2;
	 }   

	 /**
	  * 属性vdef2的Setter方法.
	  *
	  * 创建日期:2011-4-12
	  * @param newVdef2 String
	  */
	 public void setVdef2(String newVdef2) {

		 vdef2 = newVdef2;
	 } 	  

	 /**
	  * 属性ts的Getter方法.
	  *
	  * 创建日期:2011-4-12
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
	  * 创建日期:2011-4-12
	  * @param newTs UFDateTime
	  */
	 public void setTs(String newTs) {

		 ts = newTs;
	 } 	  

	 /**
	  * 属性vdef1的Getter方法.
	  *
	  * 创建日期:2011-4-12
	  * @return String
	  */
	 public String getVdef1() {
		 return vdef1;
	 }   

	 /**
	  * 属性vdef1的Setter方法.
	  *
	  * 创建日期:2011-4-12
	  * @param newVdef1 String
	  */
	 public void setVdef1(String newVdef1) {

		 vdef1 = newVdef1;
	 } 	  

	 /**
	  * 属性vdef5的Getter方法.
	  *
	  * 创建日期:2011-4-12
	  * @return String
	  */
	 public String getVdef5() {
		 return vdef5;
	 }   

	 /**
	  * 属性vdef5的Setter方法.
	  *
	  * 创建日期:2011-4-12
	  * @param newVdef5 String
	  */
	 public void setVdef5(String newVdef5) {

		 vdef5 = newVdef5;
	 } 	  

	 /**
	  * 属性name的Getter方法.
	  *
	  * 创建日期:2011-4-12
	  * @return String
	  */
	 public String getName() {
		 return name;
	 }   

	 /**
	  * 属性name的Setter方法.
	  *
	  * 创建日期:2011-4-12
	  * @param newName String
	  */
	 public void setName(String newName) {

		 name = newName;
	 } 	  

	 /**
	  * 属性pk_credence_h的Getter方法.
	  *
	  * 创建日期:2011-4-12
	  * @return String
	  */
	 public String getPk_credence_h() {
		 return pk_credence_h;
	 }   

	 /**
	  * 属性pk_credence_h的Setter方法.
	  *
	  * 创建日期:2011-4-12
	  * @param newPk_credence_h String
	  */
	 public void setPk_credence_h(String newPk_credence_h) {

		 pk_credence_h = newPk_credence_h;
	 } 	  


	 /**
	  * 验证对象各属性之间的数据逻辑正确性.
	  *
	  * 创建日期:2011-4-12
	  * @exception nc.vo.pub.ValidationException 如果验证失败,抛出
	  * ValidationException,对错误进行解释.
	  */
	 public void validate() throws ValidationException {

		 ArrayList errFields = new ArrayList(); // errFields record those null

		 // fields that cannot be null.
		 // 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:

		 if (pk_credence_h == null) {
			 errFields.add(new String("pk_credence_h"));
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
	  * 创建日期:2011-4-12
	  * @return java.lang.String
	  */
	 public java.lang.String getParentPKFieldName() {

		 return null;

	 }   

	 /**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2011-4-12
	  * @return java.lang.String
	  */
	 public java.lang.String getPKFieldName() {
		 return "pk_credence_h";
	 }

	 /**
	  * <p>返回表名称.
	  * <p>
	  * 创建日期:2011-4-12
	  * @return java.lang.String
	  */
	 public java.lang.String getTableName() {

		 return "dip_credence_h";
	 }    

	 /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2011-4-12
	  */
	 public CredenceHVO() {

		 super();	
	 }    

	 /**
	  * 使用主键进行初始化的构造子.
	  *
	  * 创建日期:2011-4-12
	  * @param newPk_credence_h 主键值
	  */
	 public CredenceHVO(String newPk_credence_h) {

		 // 为主键字段赋值:
		 pk_credence_h = newPk_credence_h;

	 }


	 /**
	  * 返回对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2011-4-12
	  * @return String
	  */
	 public String getPrimaryKey() {

		 return pk_credence_h;

	 }

	 /**
	  * 设置对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2011-4-12
	  * @param newPk_credence_h  String    
	  */
	 public void setPrimaryKey(String newPk_credence_h) {

		 pk_credence_h = newPk_credence_h; 

	 } 

	 /**
	  * 返回数值对象的显示名称.
	  *
	  * 创建日期:2011-4-12
	  * @return java.lang.String 返回数值对象的显示名称.
	  */
	 public String getEntityName() {

		 return "dip_credence_h"; 

	 } 
} 
