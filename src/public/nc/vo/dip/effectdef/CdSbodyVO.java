package nc.vo.dip.effectdef;

import java.util.ArrayList;

import nc.util.dip.sj.IContrastUtil;
import nc.vo.pub.*;
import nc.vo.pub.lang.*;

/**
 * <b> 卡片型单表体VO类 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * 创建日期:2008-4-4
 * 
 * @author 何冰
 * @version Your Project 1.0
 */
public class CdSbodyVO extends SuperVO {

	/** 字段描述 */
	private static final long serialVersionUID = -6704448370680639189L;

	public String effectname;

	public String pk_effectdef;
	public Integer flag;
	public String effect;

	public String effectfiled;


	public Integer dr;


	public String dip_datachange_h;


	public String ts;


	public static final String VBILLNO = "effectname";

	public static final String PK_CORP = "pk_effectdef";
	
	public static final String DIP_DATACHANGE_H = "dip_datachange_h";

	public static final String DOPERATORDATE = "effectfiled";

	public static final String DR = "dr";


	public static final String TS = "ts";
	public static final String FLAG="flag";




	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getDip_datachange_h() {
		return dip_datachange_h;
	}

	public void setDip_datachange_h(String dip_datachange_h) {
		this.dip_datachange_h = dip_datachange_h;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getEffectfiled() {
		return effectfiled;
	}

	public void setEffectfiled(String effectfiled) {
		this.effectfiled = effectfiled;
	}

	public String getEffectname() {
		return effectname;
	}

	public void setEffectname(String effectname) {
		this.effectname = effectname;
	}

	public String getPk_effectdef() {
		return pk_effectdef;
	}

	public void setPk_effectdef(String pk_effectdef) {
		this.pk_effectdef = pk_effectdef;
	}
	

	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}

	/**
	 * 属性ts的Getter方法.
	 * 
	 * 创建日期:2008-4-4
	 * 
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
	 * 创建日期:2008-4-4
	 * 
	 * @param newTs
	 *            UFDateTime
	 */
	public void setTs(String newTs) {

		ts = newTs;
	}

	

	

	/**
	 * 验证对象各属性之间的数据逻辑正确性.
	 * 
	 * 创建日期:2008-4-4
	 * 
	 * @exception nc.vo.pub.ValidationException
	 *                如果验证失败,抛出 ValidationException,对错误进行解释.
	 */
	public void validate() throws ValidationException {

		ArrayList errFields = new ArrayList(); // errFields record those null

		// fields that cannot be null.
		// 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:

		if (pk_effectdef == null) {
			errFields.add(new String("pk_effectdef"));
		}

		StringBuffer message = new StringBuffer();
		message.append("下列字段不能为空:");
		if (errFields.size() > 0) {
			String[] temp = (String[]) errFields.toArray(new String[0]);
			message.append(temp[0]);
			for (int i = 1; i < temp.length; i++) {
				message.append(",");
				message.append(temp[i]);
			}
			throw new NullFieldException(message.toString());
		}
	}

	/**
	 * <p>
	 * 取得父VO主键字段.
	 * <p>
	 * 创建日期:2008-4-4
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getParentPKFieldName() {

		return null;

	}

	/**
	 * <p>
	 * 取得表主键.
	 * <p>
	 * 创建日期:2008-4-4
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {
		return "pk_effectdef";
	}

	/**
	 * <p>
	 * 返回表名称.
	 * <p>
	 * 创建日期:2008-4-4
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {

		return "dip_effectdef";
	}

	/**
	 * 按照默认方式创建构造子.
	 * 
	 * 创建日期:2008-4-4
	 */
	public CdSbodyVO() {

		super();
	}

	/**
	 * 使用主键进行初始化的构造子.
	 * 
	 * 创建日期:2008-4-4
	 * 
	 * @param newPk_sbody
	 *            主键值
	 */
	public CdSbodyVO(String newPk_effectdef) {

		// 为主键字段赋值:
		pk_effectdef = newPk_effectdef;

	}

	/**
	 * 返回对象标识,用来唯一定位对象.
	 * 
	 * 创建日期:2008-4-4
	 * 
	 * @return String
	 */
	public String getPrimaryKey() {

		return pk_effectdef;

	}

	/**
	 * 设置对象标识,用来唯一定位对象.
	 * 
	 * 创建日期:2008-4-4
	 * 
	 * @param newPk_sbody
	 *            String
	 */
	public void setPrimaryKey(String newPk_effectdef) {

		pk_effectdef = newPk_effectdef;

	}

	/**
	 * 返回数值对象的显示名称.
	 * 
	 * 创建日期:2008-4-4
	 * 
	 * @return java.lang.String 返回数值对象的显示名称.
	 */
	public String getEntityName() {

		return "dip_effectdef";

	}
}
