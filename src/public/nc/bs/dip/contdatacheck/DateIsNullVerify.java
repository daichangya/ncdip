package nc.bs.dip.contdatacheck;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import nc.bs.framework.common.NCLocator;

import nc.itf.dip.pub.IQueryField;
import nc.itf.dip.pub.checkplugin.ICheckPlugin;
import nc.jdbc.framework.exception.DbException;
import nc.util.dip.sj.CheckMessage;
import nc.vo.dip.contdatawh.ContDataMapUtilVO;
import nc.vo.pub.BusinessException;

public class DateIsNullVerify implements ICheckPlugin{

	public CheckMessage doCheck(Object... ob) {
		// TODO Auto-generated method stub
		ContDataMapUtilVO vo=(ContDataMapUtilVO) ob[0];
		String middletablename=vo.getMiddletablename();
		CheckMessage checkmsg=new CheckMessage();
		String checkname=vo.getCheckname();
		if(!exist(middletablename)){
			checkmsg.setSuccessful(false);
			checkmsg.setMessage("校验["+checkname+"]：失败，维护表"+middletablename+"不存在！");
			return checkmsg;
		};
		
		String sql="select dd.pk from "+middletablename+" dd where dd.contpk is null or dd.extepk is null ";
		IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
		List list=null;
		HashMap map=new HashMap();
		try {
			list=iqf.queryfieldList(sql);
			if(list!=null && list.size()>0){
				checkmsg.setSuccessful(false);
				checkmsg.setMessage("校验["+checkname+"]：维护表"+middletablename+"存在"+list.size()+"条为空记录！");
				map.put(vo.getCheckclassname(),list);
				checkmsg.setMap(map);	
			}else{
				checkmsg.setSuccessful(true);
			}
			
			
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return checkmsg;
	}
	
	public boolean exist(String tablename){
		boolean flag=false;
		IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
		String str="select count(*) from user_tables where table_name='"+tablename.toUpperCase()+"'";
		try {
			String ss=iqf.queryfield(str);
			if("1".equals(ss)){
				flag=true;
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}
}
