package nc.vo.dip.voucher;

import java.util.Hashtable;

import nc.vo.dip.contdata.DipContdataVO;
import nc.vo.dip.datachange.DipDatachangeHVO;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.pub.SuperVO;

/**
 * @author Administrator
 * @公共配置信息
 */
public class ConfigVO extends SuperVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//这个时候这个表头的memorytable已经改为表的物理名称
	private DipDatachangeHVO parent;
	//公司那个组织对照VO
	private DipContdataVO cdata;
	//字段的<Ename,数据定义的表体VO>
	private Hashtable<String, DipDatadefinitBVO> datadef;
	//影响因素<effect(i),vo数据的Ename>,如果没有查到是NULL
	private Hashtable<String, String> influence;
	//合并凭证设置的 <effect(i),vo数据的Ename>,如果没有查到是NULL
	private Hashtable<String, String> combine;
	//这个是组织的map<对照系统字段，被对照系统字段>
//	private Hashtable<String, String> orgMap;
	//是否是固定组织
	private boolean isgdzz;
	//组织表临时表表名
	private String temptablename;
	

	public boolean isIsgdzz() {
		return isgdzz;
	}

	public void setIsgdzz(boolean isgdzz) {
		this.isgdzz = isgdzz;
	}

//	public Hashtable<String, String> getOrgMap() {
//		return orgMap;
//	}
//
//	public void setOrgMap(Hashtable<String, String> orgMap) {
//		this.orgMap = orgMap;
//	}

	public Hashtable<String, String> getInfluence() {
		return influence;
	}

	public void setInfluence(Hashtable<String, String> influence) {
		this.influence = influence;
	}

	public Hashtable<String, DipDatadefinitBVO> getDatadef() {
		return datadef;
	}

	public void setDatadef(Hashtable<String, DipDatadefinitBVO> datadef) {
		this.datadef = datadef;
	}

	public DipDatachangeHVO getParent() {
		return parent;
	}

	public void setParent(DipDatachangeHVO parent) {
		this.parent = parent;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	public Hashtable<String, String> getCombine() {
		return combine;
	}

	public void setCombine(Hashtable<String, String> combine) {
		this.combine = combine;
	}

	public DipContdataVO getCdata() {
		return cdata;
	}

	public void setCdata(DipContdataVO cdata) {
		this.cdata = cdata;
	}

	public String getTemptablename() {
		return temptablename;
	}

	public void setTemptablename(String temptablename) {
		this.temptablename = temptablename;
	}
	/**
	 * @desc 返回组织字段的引用字段的英文名称
	 * */
	public String getYWZD(){
		DipDatadefinitBVO orgField = getDatadef().get(getParent().getOrg());  //获取组织字段 字段VO
		return orgField.getEname();
	}

}
