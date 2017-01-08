package nc.ui.bd.ref;


import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.contdata.DipContdataVO;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;

/**
����ϵͳ����		contabname
����ϵͳ�����ֶ�	contcolcode
����ϵͳ����     	concodefiled
����ϵͳ�����ֶ� 	connamefiled

������ϵͳ���� 	extetabname
������ϵͳ�����ֶ� 	extecolcode
������ϵͳ�����ֶ� 	exteconcodefiled
������ϵͳ�����ֶ� 	exteconnamefiled
*/
//<nc.ui.bd.ref.DataComparCommenRefModel>
public class DataComparCommenRefModel extends AbstractRefModel {
	//��ʾ���ֶ�
	private String[] deffieldcode;
	//��ʾ���ֶ�����
	private String[] deffieldname;
	//����
	private String defpk;
	//���ձ���
	private String defreftitle;
	//����
	private String deftablename;
	private String[] ncsql;
	private String[] wbsql;
	private String wherepart;
	
//	/**@desc ���ڲ����Ƕ�̬�ģ�������ʾ��ʽҲ�Ƕ�̬��
//	  * �ⲿϵͳ�ı����ϵ���ʾ��ʽ
//	  * */
//	 private String[] defwbmaFomulars;
//	 /**@desc ���ڲ����Ƕ�̬�ģ�������ʾ��ʽҲ�Ƕ�̬��
//	  * ncϵͳ�ı����ϵ���ʾ��ʽ
//	  * */
//	 private String[] defncbmFomulars;
	
	public String whereStr = "";

	/** ��������Ҫ��ʾ�����ݿ���е��ֶΣ������������*/
	@Override
	public String[] getFieldCode() {
		return getDefFieldcode();
	}

	/**��������ʾ�����ݿ��ж�Ӧ���ֶεĺ���*/
	@Override
	public String[] getFieldName() {
		return getDefFieldname();
	}

	/** �����ֶΣ������ֶβ���ʾ��һ��Ϊ����*/
	@Override
	public String[] getHiddenFieldCode() {
		return getDefHildfield();
	}

	/** ���ն�Ӧ���ݿ���е������ֶ�*/
	@Override
	public String getPkFieldCode() {
		return getDefPk();
	}

	/** ���ձ���*/
	@Override
	public String getRefTitle() {
		return getDefReftitle();
	}

	/** ���ն�Ӧ��ȡ�������ݿ��ṹ������Ϊ���������*/
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
	
	
	//һЩsetget����-----------------------------------------------------

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
	 * @param initpk ��ϵͳע������
	 * @param isWxt  �Ƿ�����ϵͳ������ǵĻ���true;�����ncϵͳ��false
	 * @desc ���ȸ�����ϵͳ�������鴦��Щû��ά�������ݣ�
	 * Ȼ��Ѳ��յı�������ƴ����ݿ����ȡ�����ŵ����յı���������ϱ�ȥ��
	 * Ȼ��ѱ�������ȥ��
	 * Ȼ��Ѳ��յı�������[ϵͳ���ն���������ֶ�]
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
				setDefFieldname(new String[]{"����","����"});
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
				setDefFieldname(new String[]{"����","����"});
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
