package nc.impl.dip.sjjgimpl;

import java.sql.SQLException;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IDataProcess;
import nc.itf.dip.pub.ILogAndTabMonitorSys;
import nc.itf.dip.pub.IQueryField;
import nc.jdbc.framework.exception.DbException;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.RetMessage;
import nc.vo.dip.dataproce.DipDataproceHVO;
import nc.vo.pub.BusinessException;


/**
 * 数据清洗插件类
 * 作者：冯建芬
 * 时间：2011-07-02
 * */
public class DataProcessSjqxImpl implements IDataProcess {

	public RetMessage dataprocess(String hpk, String sql, String tablename,String oldtablename) throws Exception {
		IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
		iqf.exesql(sql);
		return new RetMessage(true,"数据清洗成功！");
	}
	
	
}