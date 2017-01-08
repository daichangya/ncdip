package nc.ui.bd.ref;


import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.contdata.DipContdataVO;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;

/**
对照系统表名		contabname
对照系统物理字段	contcolcode
对照系统编码     	concodefiled
对照系统名称字段 	connamefiled

被对照系统表名 	extetabname
被对照系统物理字段 	extecolcode
被对照系统编码字段 	exteconcodefiled
被对照系统名称字段 	exteconnamefiled
*/
//<nc.ui.bd.ref.DataComparCommenRefModel>
public class DataComparCommenRefModel extends AbstractRefModel {
	//显示的字段
	private String[] deffieldcode;
	//显示的字段名称
	private String[] deffieldname;
	//主键
	private String defpk;
	//参照标题
	private String defreftitle;
	//表名
	private String deftablename;
	private String[] ncsql;
	private String[] wbsql;
	private String wherepart;
	
//	/**@desc 由于参照是动态的，所以显示公式也是动态的
//	  * 外部系统的编码上的显示公式
//	  * */
//	 private String[] defwbmaFomulars;
//	 /**@desc 由于参照是动态的，所以显示公式也是动态的
//	  * nc系统的编码上的显示公式
//	  * */
//	 private String[] defncbmFomulars;
	
	public String whereStr = "";

	/** 参照所需要显示的数据库表中的字段，必须包含主键*/
	@Override
	public String[] getFieldCode() {
		return getDefFieldcode();
	}

	/**参照所显示的数据库中对应的字段的汉字*/
	@Override
	public String[] getFieldName() {
		return getDefFieldname();
	}

	/** 隐藏字段，即此字段不显示，一般为主键*/
	@Override
	public String[] getHiddenFieldCode() {
		return getDefHildfield();
	}

	/** 参照对应数据库表中的主键字段*/
	@Override
	public String getPkFieldCode() {
		return getDefPk();
	}

	/** 参照标题*/
	@Override
	public String getRefTitle() {
		return getDefReftitle();
	}

	/** 参照对应的取数的数据库表结构，可以为多个表连接*/
	@Override
	public String getTableName() {
		return getDefTablename();
	}

	public String getWhereStr() {
		return whereStr;
	}

	public void setWhereStr(String str) {
		this.whereStr = " and 1=1 " + str;
		this.addWherePart(null);
	}

	@Override
	public void addWherePart(String arg0) {
		System.out.println("...TakeBillStatusRefModel" + getWhereStr());
		super.addWherePart(" " + getWhereStr());
	}
	
	
	//一些setget方法-----------------------------------------------------

	public String[] getDefFieldcode() {
		return deffieldcode;
	}

	public void setDefFieldcode(String[] fieldcode) {
		this.deffieldcode = fieldcode;
	}

	public String[] getDefFieldname() {
		return deffieldname;
	}

	public void setDefFieldname(String[] fieldname) {
		this.deffieldname = fieldname;
	}

	public String[] getDefHildfield() {
		return new String[]{getDefPk()};
	}

	public String getDefPk() {
		return defpk;
	}

	public void setDefPk(String pk) {
		this.defpk = pk;
	}

	public String getDefReftitle() {
		return defreftitle;
	}

	public void setDefReftitle(String reftitle) {
		this.defreftitle = reftitle;
	}

	public String getDefTablename() {
		return deftablename;
	}

	public void setDefTablename(String tablename) {
		this.deftablename = tablename;
	}
	
//	public String[] getDefncbmFomulars() {
//		return defncbmFomulars;
//	}
//
//	public void setDefncbmFomulars(String[] defncbmFomulars) {
//		this.defncbmFomulars = defncbmFomulars;
//	}
//
//	public String[] getDefwbmaFomulars() {
//		return defwbmaFomulars;
//	}
//
//	public void setDefwbmaFomulars(String[] defwbmaFomulars) {
//		this.defwbmaFomulars = defwbmaFomulars;
//	}

	public String[] getNcsql() {
		return ncsql;
	}

	public void setNcsql(String[] ncsql) {
		this.ncsql = ncsql;
	}

	public String[] getWbsql() {
		return wbsql;
	}

	public void setWbsql(String[] wbsql) {
		this.wbsql = wbsql;
	}

	/**
	 * @author wyd
	 * @param initpk 外系统注册主键
	 * @param isWxt  是否是外系统，如果是的话，true;如果是nc系统，false
	 * @desc 首先根据外系统主键，查处那些没有维护的数据，
	 * 然后把参照的编码和名称从数据库里边取出来放到参照的编码和名称上边去，
	 * 然后把表名放上去，
	 * 然后把参照的标题查出来[系统对照定义的名称字段]
	 * */
	public void setInitData(String initpk,boolean isWxt){
		try {
			if(isWxt){
			
				DipContdataVO dcvo=(DipContdataVO) HYPubBO_Client.queryByPrimaryKey(DipContdataVO.class, initpk);
				setDefTablename(dcvo.getContabcode());
				String code="";
				DipDatadefinitBVO codevo=(DipDatadefinitBVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitBVO.class, dcvo.getConcodefiled());
				code=SJUtil.isNull(codevo)?"":codevo.getEname();
				String name="";
				DipDatadefinitBVO namevo=(DipDatadefinitBVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitBVO.class, dcvo.getConnamefiled());
				name=SJUtil.isNull(namevo)?"":namevo.getEname();
				
				String pk="";
				DipDatadefinitBVO pkvo=(DipDatadefinitBVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitBVO.class, dcvo.getContcolcode());
				pk=SJUtil.isNull(pkvo)?"":pkvo.getEname();
				
				setDefFieldcode(new String[]{code,name});
				setDefFieldname(new String[]{"编码","名称"});
				setDefReftitle(dcvo.getName());
				setDefPk(pk);
				addWherePart(getDefPk()+" not in SELECT syscode FROM dip_contdatawh where pk_sysregister_h='"+initpk+"'");
				setWbsql(new String[]{"select "+code+" from "+getTableName()+" where "+getPkFieldCode()+"=",
						"select "+name+" from "+getTableName()+" where "+getPkFieldCode()+"="});
//				String[] famulars=new String[]{"sysname->getColValue("+getTableName()+", "+getDefFieldcode()[0]+", "+pk+", syscode)",
//										"wbbm->getColValue("+getTableName()+", "+getDefFieldcode()[1]+", "+pk+", syscode)"};
//				setDefwbmaFomulars(famulars);
				
			}else{
				DipContdataVO dcvo=(DipContdataVO) HYPubBO_Client.queryByPrimaryKey(DipContdataVO.class, initpk);
				setDefTablename(dcvo.getExtetabcode());
				String code=dcvo.getExteconcodefiled();
//				DipDatadefinitBVO codevo=(DipDatadefinitBVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitBVO.class, dcvo.getConcodefiled());
//				code=SJUtil.isNull(codevo)?"":codevo.getEname();
				String name=dcvo.getExteconnamefiled();
//				DipDatadefinitBVO namevo=(DipDatadefinitBVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitBVO.class, dcvo.getConnamefiled());
//				name=SJUtil.isNull(namevo)?"":namevo.getEname();
				setDefFieldcode(new String[]{code,name});
				setDefFieldname(new String[]{"编码","名称"});
				setDefReftitle(dcvo.getName());
				setDefPk(dcvo.getExtecolcode());
				addWherePart(getDefPk()+" not in SELECT nccode FROM dip_contdatawh where pk_sysregister_h='"+initpk+"'");
				setNcsql(new String[]{"select "+code+" from "+getTableName()+" where "+getPkFieldCode()+"=",
						"select "+name+" from "+getTableName()+" where "+getPkFieldCode()+"="});
//				String[] famulars=new String[]{"ncname->getColValue("+getTableName()+", "+getDefFieldcode()[0]+", "+dcvo.getExtecolcode()+", syscode)",
//						"ncbm->getColValue("+getTableName()+", "+getDefFieldcode()[1]+", "+dcvo.getExtecolcode()+", syscode)"};
//				setDefncbmFomulars(famulars);
			}
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
