/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
  \***************************************************************/
package nc.vo.dip.actionset;

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
 * 创建日期:2011-4-18
 * @author author
 * @version Your Project 1.0
 */
public class ActionSetHVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String code;
	public String vdef2;
	public String vdef4;
	public String ts;
	public String pk_actionset_h;
	public Integer dr;
	public String vdef1;
	public String vdef3;
	public String vdef5;
	public String name;
	public String pk_contdata_h;
	public String pk_role_group;
	private UFBoolean is_query;
	private UFBoolean is_export;
	private UFBoolean is_cancal;
	private UFBoolean is_delline;
	private UFBoolean is_save;
	private UFBoolean is_addline;
	private UFBoolean is_edit;
	private UFBoolean is_add;
	private UFBoolean is_import;
	private UFBoolean is_del;
	private UFBoolean is_extend;
	private UFBoolean selected;

	public static final String  CODE="code";   
	public static final String  VDEF2="vdef2";   
	public static final String  VDEF4="vdef4";   
	public static final String  TS="ts";   
	public static final String  PK_ACTIONSET_H="pk_actionset_h";   
	public static final String  DR="dr";   
	public static final String  VDEF1="vdef1";   
	public static final String  VDEF3="vdef3";   
	public static final String  VDEF5="vdef5";   
	public static final String  NAME="name";   

	
	public UFBoolean getSelected() {
		return selected;
	}

	public void setSelected(UFBoolean selected) {
		this.selected = selected;
	}

	public String getPk_role_group() {
		return pk_role_group;
	}

	public void setPk_role_group(String pk_role_group) {
		this.pk_role_group = pk_role_group;
	}

	
	public String getPk_contdata_h() {
		return pk_contdata_h;
	}

	public void setPk_contdata_h(String pk_contdata_h) {
		this.pk_contdata_h = pk_contdata_h;
	}

	public UFBoolean getIs_query() {
		return is_query;
	}

	public void setIs_query(UFBoolean is_query) {
		this.is_query = is_query;
	}

	public UFBoolean getIs_export() {
		return is_export;
	}

	public void setIs_export(UFBoolean is_export) {
		this.is_export = is_export;
	}

	public UFBoolean getIs_cancal() {
		return is_cancal;
	}

	public void setIs_cancal(UFBoolean is_cancal) {
		this.is_cancal = is_cancal;
	}

	public UFBoolean getIs_delline() {
		return is_delline;
	}

	public void setIs_delline(UFBoolean is_delline) {
		this.is_delline = is_delline;
	}

	public UFBoolean getIs_save() {
		return is_save;
	}

	public void setIs_save(UFBoolean is_save) {
		this.is_save = is_save;
	}

	public UFBoolean getIs_addline() {
		return is_addline;
	}

	public void setIs_addline(UFBoolean is_addline) {
		this.is_addline = is_addline;
	}

	public UFBoolean getIs_edit() {
		return is_edit;
	}

	public void setIs_edit(UFBoolean is_edit) {
		this.is_edit = is_edit;
	}

	public UFBoolean getIs_add() {
		return is_add;
	}

	public void setIs_add(UFBoolean is_add) {
		this.is_add = is_add;
	}

	public UFBoolean getIs_import() {
		return is_import;
	}

	public void setIs_import(UFBoolean is_import) {
		this.is_import = is_import;
	}

	public UFBoolean getIs_del() {
		return is_del;
	}

	public void setIs_del(UFBoolean is_del) {
		this.is_del = is_del;
	}

	public UFBoolean getIs_extend() {
		return is_extend;
	}

	public void setIs_extend(UFBoolean is_extend) {
		this.is_extend = is_extend;
	}

	/**
	 * 属性code的Getter方法.
	 *
	 * 创建日期:2011-4-18
	 * @return String
	 */
	public String getCode() {
		return code;
	}   

	/**
	 * 属性code的Setter方法.
	 *
	 * 创建日期:2011-4-18
	 * @param newCode String
	 */
	public void setCode(String newCode) {

		code = newCode;
	} 	  

	/**
	 * 属性vdef2的Getter方法.
	 *
	 * 创建日期:2011-4-18
	 * @return String
	 */
	public String getVdef2() {
		return vdef2;
	}   

	/**
	 * 属性vdef2的Setter方法.
	 *
	 * 创建日期:2011-4-18
	 * @param newVdef2 String
	 */
	public void setVdef2(String newVdef2) {

		vdef2 = newVdef2;
	} 	  

	/**
	 * 属性vdef4的Getter方法.
	 *
	 * 创建日期:2011-4-18
	 * @return String
	 */
	public String getVdef4() {
		return vdef4;
	}   

	/**
	 * 属性vdef4的Setter方法.
	 *
	 * 创建日期:2011-4-18
	 * @param newVdef4 String
	 */
	public void setVdef4(String newVdef4) {

		vdef4 = newVdef4;
	} 	  

	/**
	 * 属性ts的Getter方法.
	 *
	 * 创建日期:2011-4-18
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
	 * 创建日期:2011-4-18
	 * @param newTs UFDateTime
	 */
	public void setTs(String newTs) {

		ts = newTs;
	} 	  

	/**
	 * 属性pk_actionset_h的Getter方法.
	 *
	 * 创建日期:2011-4-18
	 * @return String
	 */
	public String getPk_actionset_h() {
		return pk_actionset_h;
	}   

	/**
	 * 属性pk_actionset_h的Setter方法.
	 *
	 * 创建日期:2011-4-18
	 * @param newPk_actionset_h String
	 */
	public void setPk_actionset_h(String newPk_actionset_h) {

		pk_actionset_h = newPk_actionset_h;
	} 	  

	/**
	 * 属性dr的Getter方法.
	 *
	 * 创建日期:2011-4-18
	 * @return Integer
	 */
	public Integer getDr() {
		return dr;
	}   

	/**
	 * 属性dr的Setter方法.
	 *
	 * 创建日期:2011-4-18
	 * @param newDr Integer
	 */
	public void setDr(Integer newDr) {

		dr = newDr;
	} 	  

	/**
	 * 属性vdef1的Getter方法.
	 *
	 * 创建日期:2011-4-18
	 * @return String
	 */
	public String getVdef1() {
		return vdef1;
	}   

	/**
	 * 属性vdef1的Setter方法.
	 *
	 * 创建日期:2011-4-18
	 * @param newVdef1 String
	 */
	public void setVdef1(String newVdef1) {

		vdef1 = newVdef1;
	} 	  

	/**
	 * 属性vdef3的Getter方法.
	 *
	 * 创建日期:2011-4-18
	 * @return String
	 */
	public String getVdef3() {
		return vdef3;
	}   

	/**
	 * 属性vdef3的Setter方法.
	 *
	 * 创建日期:2011-4-18
	 * @param newVdef3 String
	 */
	public void setVdef3(String newVdef3) {

		vdef3 = newVdef3;
	} 	  

	/**
	 * 属性vdef5的Getter方法.
	 *
	 * 创建日期:2011-4-18
	 * @return String
	 */
	public String getVdef5() {
		return vdef5;
	}   

	/**
	 * 属性vdef5的Setter方法.
	 *
	 * 创建日期:2011-4-18
	 * @param newVdef5 String
	 */
	public void setVdef5(String newVdef5) {

		vdef5 = newVdef5;
	} 	  

	/**
	 * 属性name的Getter方法.
	 *
	 * 创建日期:2011-4-18
	 * @return String
	 */
	public String getName() {
		return name;
	}   

	/**
	 * 属性name的Setter方法.
	 *
	 * 创建日期:2011-4-18
	 * @param newName String
	 */
	public void setName(String newName) {

		name = newName;
	} 	  


	/**
	 * 验证对象各属性之间的数据逻辑正确性.
	 *
	 * 创建日期:2011-4-18
	 * @exception nc.vo.pub.ValidationException 如果验证失败,抛出
	 * ValidationException,对错误进行解释.
	 */
	public void validate() throws ValidationException {

		ArrayList errFields = new ArrayList(); // errFields record those null

		// fields that cannot be null.
		// 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:

		if (pk_actionset_h == null) {
			errFields.add(new String("pk_actionset_h"));
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
	 * 创建日期:2011-4-18
	 * @return java.lang.String
	 */
	public java.lang.String getParentPKFieldName() {

		return null;

	}   

	/**
	 * <p>取得表主键.
	 * <p>
	 * 创建日期:2011-4-18
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {
		return "pk_actionset_h";
	}

	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:2011-4-18
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {

		return "dip_actionset_h";
	}    

	/**
	 * 按照默认方式创建构造子.
	 *
	 * 创建日期:2011-4-18
	 */
	public ActionSetHVO() {

		super();	
	}    

	/**
	 * 使用主键进行初始化的构造子.
	 *
	 * 创建日期:2011-4-18
	 * @param newPk_actionset_h 主键值
	 */
	public ActionSetHVO(String newPk_actionset_h) {

		// 为主键字段赋值:
		pk_actionset_h = newPk_actionset_h;

	}


	/**
	 * 返回对象标识,用来唯一定位对象.
	 *
	 * 创建日期:2011-4-18
	 * @return String
	 */
	public String getPrimaryKey() {

		return pk_actionset_h;

	}

	/**
	 * 设置对象标识,用来唯一定位对象.
	 *
	 * 创建日期:2011-4-18
	 * @param newPk_actionset_h  String    
	 */
	public void setPrimaryKey(String newPk_actionset_h) {

		pk_actionset_h = newPk_actionset_h; 

	} 

	/**
	 * 返回数值对象的显示名称.
	 *
	 * 创建日期:2011-4-18
	 * @return java.lang.String 返回数值对象的显示名称.
	 */
	public String getEntityName() {

		return "dip_actionset_h"; 

	}
} 
