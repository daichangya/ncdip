package nc.vo.dip.datacheckyytj;

import java.util.ArrayList;

import nc.util.dip.sj.IContrastUtil;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

public class DataCheckYYTJVO extends SuperVO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6429639017056804150L;
	public String pksys;//系统主键
	public String syscode;//系统编码
	public String sysecode;//外部系统编码
	public String sysname;//系统名称
	public String datatype;//业务类型
	public String pkbus;//业务主键
	public String buscode;//业务编码
	public String busname;//业务名称
	public String pkvh;//数据校验的主表主键
	public String pkvb;//数据校验的附表主键
	public String vcode;//数据校验的附表编码
	public String vclass;//数据娇艳的附表的校验类
	public String vector;//数据校验错误处理方式
	public String pkv;//数据校验注册的主键
	public String vtype;//数据校验类型
	public String vdesc;//数据校验的描述，给通用校验用的
	public String verifycon;//通用数据校验的校验语句
	public String rwlx;//任务类型
	public Integer dr;//
	public String ts;//
	
	
	public String getRwlx() {
		return rwlx;
	}

	public void setRwlx(String rwlx) {
		this.rwlx = rwlx;
	}

	/**
	 * 按照默认方式创建构造子.
	 *
	 * 创建日期:2011-6-30
	 */
	public DataCheckYYTJVO() {

		super();	
	}    

	public String getBuscode() {
		return buscode;
	}

	public void setBuscode(String buscode) {
		this.buscode = buscode;
	}

	public String getBusname() {
		return busname;
	}

	public void setBusname(String busname) {
		this.busname = busname;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getPkbus() {
		return pkbus;
	}

	public void setPkbus(String pkbus) {
		this.pkbus = pkbus;
	}

	public String getPksys() {
		return pksys;
	}

	public void setPksys(String pksys) {
		this.pksys = pksys;
	}

	public String getPkv() {
		return pkv;
	}

	public void setPkv(String pkv) {
		this.pkv = pkv;
	}

	public String getPkvb() {
		return pkvb;
	}

	public void setPkvb(String pkvb) {
		this.pkvb = pkvb;
	}

	public String getPkvh() {
		return pkvh;
	}

	public void setPkvh(String pkvh) {
		this.pkvh = pkvh;
	}

	public String getSysecode() {
		return sysecode;
	}

	public void setSysecode(String sysecode) {
		this.sysecode = sysecode;
	}

	public String getVclass() {
		return vclass;
	}

	public void setVclass(String vclass) {
		this.vclass = vclass;
	}

	public String getVcode() {
		return vcode;
	}

	public void setVcode(String vcode) {
		this.vcode = vcode;
	}

	public String getVdesc() {
		return vdesc;
	}

	public void setVdesc(String vdesc) {
		this.vdesc = vdesc;
	}

	public String getVector() {
		return vector;
	}

	public void setVector(String vector) {
		this.vector = vector;
	}

	public String getVerifycon() {
		return verifycon;
	}

	public void setVerifycon(String verifycon) {
		this.verifycon = verifycon;
	}

	public String getVtype() {
		return vtype;
	}

	public void setVtype(String vtype) {
		this.vtype = vtype;
	}

	public Integer getDr() {
		return dr;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	public String getSyscode() {
		return syscode;
	}
	public void setSyscode(String syscode) {
		this.syscode = syscode;
	}
	public String getSysname() {
		return sysname;
	}
	public void setSysname(String sysname) {
		this.sysname = sysname;
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
	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pkvb";
	}
	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "v_dip_alldatatype";
	}
	/**
	 * 返回对象标识,用来唯一定位对象.
	 *
	 * 创建日期:2011-6-30
	 * @return String
	 */
	public String getPrimaryKey() {

		return pkvb;

	}

	/**
	 * 设置对象标识,用来唯一定位对象.
	 *
	 * 创建日期:2011-6-30
	 * @param newPk_dip_datacanzhao  String    
	 */
	public void setPrimaryKey(String pkvb) {

		pkvb = pkvb; 

	} 

	/**
	 * 返回数值对象的显示名称.
	 *
	 * 创建日期:2011-6-30
	 * @return java.lang.String 返回数值对象的显示名称.
	 */
	public String getEntityName() {

		return "v_dip_alldatatype"; 

	} 

}
