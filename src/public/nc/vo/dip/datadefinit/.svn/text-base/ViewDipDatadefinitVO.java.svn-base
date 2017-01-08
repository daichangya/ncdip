package nc.vo.dip.datadefinit;

import java.util.ArrayList;

import nc.util.dip.sj.IContrastUtil;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

/**
 *版权信息：商佳科技
 *作者：   程莉
 *版本：   
 *描述：   左树右表结构树视图vo
 *创建日期：2011-04-19
 * @throws Exception 
 */
public class ViewDipDatadefinitVO extends SuperVO {
	public String syscode;
	public String sysname;
	public String pk;
	public String fpk;
	public String ts;
	public Integer dr;
	public UFBoolean isdeploy;

	public static final String  SYSCODE="syscode";   
	public static final String  SYSNAME="sysname";   
	public static final String  PK="pk";   
	public static final String  FPK="fpk";
	public static final String  DR="dr";   
	public static final String  TS="ts";   
	public static final String  ISDEPLOY="isdeploy";   

	/**
	 * 属性isdeploy的Getter方法.
	 *
	 * 创建日期:2011-4-7
	 * @return String
	 */
	public UFBoolean getIsdeploy() {
		return isdeploy;
	}   

	/**
	 * 属性isdeploy的Setter方法.
	 *
	 * 创建日期:2011-4-7
	 * @param newIsdeploy String
	 */
	public void setIsdeploy(UFBoolean newIsdeploy) {

		isdeploy = newIsdeploy;
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
	 * 属性ts的Getter方法.
	 *
	 * 创建日期:2011-4-7
	 * @return String
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
	 * @param newTs String
	 */
	public void setTs(String newTs) {

		ts = newTs;
	} 	  
	public String getFpk() {
		return fpk;
	}
	public void setFpk(String newFpk) {
		this.fpk = newFpk;
	}
	public String getPk() {
		return pk;
	}
	public void setPk(String newPk) {
		this.pk = newPk;
	}
	public String getSyscode() {
		return syscode;
	}
	public void setSyscode(String newSyscode) {
		this.syscode = newSyscode;
	}
	public String getSysname() {
		return sysname;
	}
	public void setSysname(String newSysname) {
		this.sysname = newSysname;
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

		if (pk == null) {
			errFields.add(new String("pk"));
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

		return null;

	}   

	/**
	 * <p>取得表主键.
	 * <p>
	 * 创建日期:2011-4-7
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {
		return "pk";
	}

	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:2011-4-7
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {

		return "v_dip_datadefinit";
	}    

	/**
	 * 按照默认方式创建构造子.
	 *
	 * 创建日期:2011-4-7
	 */
	public ViewDipDatadefinitVO() {
		// TODO Auto-generated constructor stub
		super();
	}

	/**
	 * 使用主键进行初始化的构造子.
	 *
	 * 创建日期:2011-4-7
	 * @param newPk_contdata_h 主键值
	 */
	public ViewDipDatadefinitVO(String newPk) {

		// 为主键字段赋值:
		pk = newPk;

	}

	/**
	 * 返回对象标识,用来唯一定位对象.
	 *
	 * 创建日期:2011-4-7
	 * @return String
	 */
	public String getPrimaryKey() {

		return pk;

	}

	/**
	 * 设置对象标识,用来唯一定位对象.
	 *
	 * 创建日期:2011-4-7
	 * @param newPk_contdata_h  String    
	 */
	public void setPrimaryKey(String newPk) {

		pk = newPk; 

	} 

	/**
	 * 返回数值对象的显示名称.
	 *
	 * 创建日期:2011-4-7
	 * @return java.lang.String 返回数值对象的显示名称.
	 */
	public String getEntityName() {

		return "v_dip_datadefinit"; 

	} 
}
