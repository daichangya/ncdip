package nc.impl.dip.sjjgimpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import nc.vo.pub.lang.UFBoolean;

/**
 * 数据加工数据汇总插件类
 * 作者：冯建芬
 * 时间：2011-07-02
 * */
public class DataProcessSjjgImpl  implements IDataProcess {

	public RetMessage dataprocess(String hpk, String sql, String tablename,String oldtablename) throws BusinessException, SQLException, DbException {
		IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
//		查询加工后的结果集
		List listValue = iqf.queryListMapSingleSql(sql);
		String tagertField = ""; //目标字段
		String sourceField = "";//源字段
		String tagertFieldValue = "";//目标字段值
		if(listValue!=null && listValue.size()>0){
			//查询需要插入加工数据表的字段，后续需优化
			sql = "select b.cname,b.ename,b.quotetable,b.quotecolu,b.ispk from dip_dataproce_h h " +
			" left join dip_dataproce_b b on b.pk_dataproce_h = h.pk_dataproce_h " +
			" where nvl(h.dr,0)=0 and nvl(b.dr,0)=0 and h.procetab='"+tablename+"' ";
			List listTableV = iqf.queryFieldSingleSql(sql);
			UFBoolean isPK = new UFBoolean(false);
			String insertSql = "";

			for(int i = 0;i<listValue.size();i++){
				StringBuffer fileds = new StringBuffer();
				StringBuffer fieldsValue = new StringBuffer();
				HashMap mapValue = (HashMap)listValue.get(i);
				//组建插入语句
				insertSql = "insert into "+tablename+" (";

				if(listTableV!=null && listTableV.size()>0){
					for(int j = 0;j<listTableV.size();j++){
						List listField = (ArrayList)listTableV.get(j);
						tagertField = listField.get(1) == null ?"":listField.get(1).toString();
						sourceField = listField.get(1) == null?"":listField.get(1).toString();
						isPK = listField.get(4)==null ? new UFBoolean(false):new UFBoolean(listField.get(4).toString());
						//判断是否是PK
						if(isPK.booleanValue()){
							tagertFieldValue = iqf.getOID();
						}else{
							tagertFieldValue = mapValue.get(sourceField.toUpperCase())==null?"":mapValue.get(sourceField.toUpperCase()).toString();
						}

						//fileds.append(sourceField+",");
						//2011-05-27 
						fileds.append(tagertField+",");

						fieldsValue.append("'"+tagertFieldValue+"',");
					}
				}
				String field = fileds.toString().substring(0, fileds.toString().length()-1);
				String fieldValue = fieldsValue.toString().substring(0, fieldsValue.toString().length()-1);
				insertSql = insertSql+field+") values ("+fieldValue+") ";
				iqf.exesql(insertSql);
			}
		}
		return new RetMessage(true,"数据汇总成功");
	}
	
	
	
	}
