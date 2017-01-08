package nc.vo.dip.warningset;
/**
 * @author wyd
 * @desc 预警那个时间配置的vo
 * 
 * */
import nc.util.dip.sj.IContrastUtil;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * 创建日期:2011-4-18
 * 
 * @author author
 * @version Your Project 1.0
 */
public class DipWarningsetDayTimeVO extends SuperVO {
	public String pk_dip_warningsetdaytime;
    public String pk_warningset;//预警表主键
	public String dwm;
	public String fo;
	public Integer periodtime;
	public String periodstart;
	public String periodset;
	public String periodend;
	public String warntime;
	public UFBoolean d1;
	public UFBoolean d2;
	public UFBoolean d3;
	public UFBoolean d4;
	public UFBoolean d5;
	public UFBoolean d6;
	public UFBoolean d7;
	public UFBoolean d8;
	public UFBoolean d9;
	public UFBoolean d10;
	public UFBoolean d11;
	public UFBoolean d12;
	public UFBoolean d13;
	public UFBoolean d14;
	public UFBoolean d15;
	public UFBoolean d16;
	public UFBoolean d17;
	public UFBoolean d18;
	public UFBoolean d19;
	public UFBoolean d20;
	public UFBoolean d21;
	public UFBoolean d22;
	public UFBoolean d23;
	public UFBoolean d24;
	public UFBoolean d25;
	public UFBoolean d26;
	public UFBoolean d27;
	public UFBoolean d28;
	public UFBoolean d29;
	public UFBoolean d30;
	public UFBoolean d31;

	public UFBoolean w1;
	public UFBoolean w2;
	public UFBoolean w3;
	public UFBoolean w4;
	public UFBoolean w5;
	public UFBoolean w6;
	public UFBoolean w7;
	
	public String ts;
    public Integer dr;


	/*
	 * 验证对象各属性之间的数据逻辑正确性.
	 * 
	 * 创建日期:2011-4-18 @exception nc.vo.pub.ValidationException 如果验证失败,抛出
	 * ValidationException,对错误进行解释.
	 *//*
		 * public void validate() throws ValidationException {
		 * 
		 * ArrayList errFields = new ArrayList(); // errFields record those null
		 *  // fields that cannot be null. // 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:
		 * 
		 * if (pk_warningset == null) { errFields.add(new
		 * String("pk_warningset")); }
		 * 
		 * StringBuffer message = new StringBuffer();
		 * message.append("下列字段不能为空:"); if (errFields.size() > 0) { String[]
		 * temp = (String[]) errFields.toArray(new String[0]);
		 * message.append(temp[0]); for ( int i= 1; i < temp.length; i++ ) {
		 * message.append(","); message.append(temp[i]); } throw new
		 * NullFieldException(message.toString()); } }
		 */

	public UFBoolean getD1() {
		return d1;
	}

	public void setD1(UFBoolean d1) {
		this.d1 = d1;
	}

	public UFBoolean getD10() {
		return d10;
	}

	public void setD10(UFBoolean d10) {
		this.d10 = d10;
	}

	public UFBoolean getD11() {
		return d11;
	}

	public void setD11(UFBoolean d11) {
		this.d11 = d11;
	}

	public UFBoolean getD12() {
		return d12;
	}

	public void setD12(UFBoolean d12) {
		this.d12 = d12;
	}

	public UFBoolean getD13() {
		return d13;
	}

	public void setD13(UFBoolean d13) {
		this.d13 = d13;
	}

	public UFBoolean getD14() {
		return d14;
	}

	public void setD14(UFBoolean d14) {
		this.d14 = d14;
	}

	public UFBoolean getD15() {
		return d15;
	}

	public void setD15(UFBoolean d15) {
		this.d15 = d15;
	}

	public UFBoolean getD16() {
		return d16;
	}

	public void setD16(UFBoolean d16) {
		this.d16 = d16;
	}

	public UFBoolean getD17() {
		return d17;
	}

	public void setD17(UFBoolean d17) {
		this.d17 = d17;
	}

	public UFBoolean getD18() {
		return d18;
	}

	public void setD18(UFBoolean d18) {
		this.d18 = d18;
	}

	public UFBoolean getD19() {
		return d19;
	}

	public void setD19(UFBoolean d19) {
		this.d19 = d19;
	}

	public UFBoolean getD2() {
		return d2;
	}

	public void setD2(UFBoolean d2) {
		this.d2 = d2;
	}

	public UFBoolean getD20() {
		return d20;
	}

	public void setD20(UFBoolean d20) {
		this.d20 = d20;
	}

	public UFBoolean getD21() {
		return d21;
	}

	public void setD21(UFBoolean d21) {
		this.d21 = d21;
	}

	public UFBoolean getD22() {
		return d22;
	}

	public void setD22(UFBoolean d22) {
		this.d22 = d22;
	}

	public UFBoolean getD23() {
		return d23;
	}

	public void setD23(UFBoolean d23) {
		this.d23 = d23;
	}

	public UFBoolean getD24() {
		return d24;
	}

	public void setD24(UFBoolean d24) {
		this.d24 = d24;
	}

	public UFBoolean getD25() {
		return d25;
	}

	public void setD25(UFBoolean d25) {
		this.d25 = d25;
	}

	public UFBoolean getD26() {
		return d26;
	}

	public void setD26(UFBoolean d26) {
		this.d26 = d26;
	}

	public UFBoolean getD27() {
		return d27;
	}

	public void setD27(UFBoolean d27) {
		this.d27 = d27;
	}

	public UFBoolean getD28() {
		return d28;
	}

	public void setD28(UFBoolean d28) {
		this.d28 = d28;
	}

	public UFBoolean getD29() {
		return d29;
	}

	public void setD29(UFBoolean d29) {
		this.d29 = d29;
	}

	public UFBoolean getD3() {
		return d3;
	}

	public void setD3(UFBoolean d3) {
		this.d3 = d3;
	}

	public UFBoolean getD30() {
		return d30;
	}

	public void setD30(UFBoolean d30) {
		this.d30 = d30;
	}

	public UFBoolean getD31() {
		return d31;
	}

	public void setD31(UFBoolean d31) {
		this.d31 = d31;
	}

	public UFBoolean getD4() {
		return d4;
	}

	public void setD4(UFBoolean d4) {
		this.d4 = d4;
	}

	public UFBoolean getD5() {
		return d5;
	}

	public void setD5(UFBoolean d5) {
		this.d5 = d5;
	}

	public UFBoolean getD6() {
		return d6;
	}

	public void setD6(UFBoolean d6) {
		this.d6 = d6;
	}

	public UFBoolean getD7() {
		return d7;
	}

	public void setD7(UFBoolean d7) {
		this.d7 = d7;
	}

	public UFBoolean getD8() {
		return d8;
	}

	public void setD8(UFBoolean d8) {
		this.d8 = d8;
	}

	public UFBoolean getD9() {
		return d9;
	}

	public void setD9(UFBoolean d9) {
		this.d9 = d9;
	}


	public String getPk_dip_warningsetdaytime() {
		return pk_dip_warningsetdaytime;
	}

	public void setPk_dip_warningsetdaytime(String pk_dip_warningsetdaytime) {
		this.pk_dip_warningsetdaytime = pk_dip_warningsetdaytime;
	}

	public String getPk_warningset() {
		return pk_warningset;
	}

	public void setPk_warningset(String pk_warningset) {
		this.pk_warningset = pk_warningset;
	}


	public String getWarntime() {
		return warntime;
	}

	public void setWarntime(String warntime) {
		this.warntime = warntime;
	}

	public UFBoolean getW1() {
		return w1;
	}

	public void setW1(UFBoolean w1) {
		this.w1 = w1;
	}

	public UFBoolean getW2() {
		return w2;
	}

	public void setW2(UFBoolean w2) {
		this.w2 = w2;
	}

	public UFBoolean getW3() {
		return w3;
	}

	public void setW3(UFBoolean w3) {
		this.w3 = w3;
	}

	public UFBoolean getW4() {
		return w4;
	}

	public void setW4(UFBoolean w4) {
		this.w4 = w4;
	}

	public UFBoolean getW5() {
		return w5;
	}

	public void setW5(UFBoolean w5) {
		this.w5 = w5;
	}

	public UFBoolean getW6() {
		return w6;
	}

	public void setW6(UFBoolean w6) {
		this.w6 = w6;
	}

	public UFBoolean getW7() {
		return w7;
	}

	public void setW7(UFBoolean w7) {
		this.w7 = w7;
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
	 * <p>
	 * 取得父VO主键字段.
	 * <p>
	 * 创建日期:2011-4-18
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
	 * 创建日期:2011-4-18
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {
		return "pk_dip_warningsetdaytime";
	}

	/**
	 * <p>
	 * 返回表名称.
	 * <p>
	 * 创建日期:2011-4-18
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {

		return "dip_warningsetdaytime";
	}

	/**
	 * 按照默认方式创建构造子.
	 * 
	 * 创建日期:2011-4-18
	 */
	public DipWarningsetDayTimeVO() {

		super();
	}

	/**
	 * 使用主键进行初始化的构造子.
	 * 
	 * 创建日期:2011-4-18
	 * 
	 * @param newPk_warningset
	 *            主键值
	 */
	public DipWarningsetDayTimeVO(String newPk_warningset) {

		// 为主键字段赋值:
		this.pk_dip_warningsetdaytime = newPk_warningset;

	}

	/**
	 * 返回对象标识,用来唯一定位对象.
	 * 
	 * 创建日期:2011-4-18
	 * 
	 * @return String
	 */
	public String getPrimaryKey() {

		return pk_dip_warningsetdaytime;

	}

	/**
	 * 设置对象标识,用来唯一定位对象.
	 * 
	 * 创建日期:2011-4-18
	 * 
	 * @param newPk_warningset
	 *            String
	 */
	public void setPrimaryKey(String newPk_warningset) {

		pk_dip_warningsetdaytime = newPk_warningset;

	}

	/**
	 * 返回数值对象的显示名称.
	 * 
	 * 创建日期:2011-4-18
	 * 
	 * @return java.lang.String 返回数值对象的显示名称.
	 */
	public String getEntityName() {

		return "dip_warningsetdaytime";

	}


	public String getPeriodend() {
		return periodend;
	}

	public void setPeriodend(String periodend) {
		this.periodend = periodend;
	}

	public String getPeriodstart() {
		return periodstart;
	}

	public void setPeriodstart(String periodstart) {
		this.periodstart = periodstart;
	}



	public String getDwm() {
		return dwm;
	}

	public void setDwm(String dwm) {
		this.dwm = dwm;
	}

	public String getFo() {
		return fo;
	}

	public void setFo(String fo) {
		this.fo = fo;
	}

	public String getPeriodset() {
		return periodset;
	}

	public void setPeriodset(String periodset) {
		this.periodset = periodset;
	}

	public Integer getPeriodtime() {
		return periodtime;
	}

	public void setPeriodtime(Integer periodtime) {
		this.periodtime = periodtime;
	}


}
