package nc.impl.dip.sjjgimpl;


import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IDataProcess;
import nc.itf.dip.pub.IQueryField;
import nc.util.dip.sj.RetMessage;

/**
 * �����û������
 * ���ߣ��뽨��
 * ʱ�䣺2011-07-02
 * */
public class DataProcessSjzhImpl implements IDataProcess {

	public RetMessage dataprocess(String hpk, String sql, String tablename, String oldtablename) throws Exception {
		IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
		iqf.exesql(sql);
		return new RetMessage(true,"����ת���ɹ���");
	}
	
}
