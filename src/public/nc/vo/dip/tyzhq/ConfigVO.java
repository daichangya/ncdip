package nc.vo.dip.tyzhq;

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
	private DipTYZHDatachangeHVO parent;
	//公司那个组织对照VO
	private DipContdataVO cdata;
	//字段的<Ename,数据定义的表体VO>
	private Hashtable<String, DipDatadefinitBVO> datadef;
	//这个是组织的map<对照系统字段，被对照系统字段>
//	private Hashtable<String, String> orgMap;
	//组织表临时表表名
	private String temptablename;
	
	private String tagpk;
	private boolean isfg;
	private String pk_sourtab;

	public String getPk_sourtab() {
		return pk_sourtab;
	}

	public void setPk_sourtab(String pk_sourtab) {
		this.pk_sourtab = pk_sourtab;
	}

	public boolean isIsfg() {
		return isfg;
	}

	public void setIsfg(boolean isfg) {
		this.isfg = isfg;
	}

	public String getTagpk() {
		return tagpk;
	}

	public void setTagpk(String tagpk) {
		this.tagpk = tagpk;
	}

	public Hashtable<String, DipDatadefinitBVO> getDatadef() {
		return datadef;
	}

	public void setDatadef(Hashtable<String, DipDatadefinitBVO> datadef) {
		this.datadef = datadef;
	}

	public DipTYZHDatachangeHVO getParent() {
		return parent;
	}

	public void setParent(DipTYZHDatachangeHVO parent) {
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
		return null;
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

}
