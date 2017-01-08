package nc.bs.dip.dataprocess;

import java.sql.SQLException;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.itf.dip.pub.checkplugin.ICheckPlugin;
import nc.jdbc.framework.exception.DbException;
import nc.util.dip.sj.CheckMessage;
import nc.vo.pub.BusinessException;
//nc.bs.dip.dataprocess.DataProcessVerify
public class DataProcessVerify implements ICheckPlugin{
/**
 * @desc 数据加工的校验，只有一个参数传过来，那就是条件
 * */
	public CheckMessage doCheck(Object... ob) {
		CheckMessage cm=new CheckMessage();
//		if(ob[0]==null||ob[0].toString().length()>0){
//			cm.setSuccessful(true);
//			cm.setMessage("校验成功");
//			return cm;
//		}
		String sql=ob[0].toString();
		IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
		String ss=null;
		try {
			ss=iqf.queryfield(sql);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cm.setSuccessful(false);
			cm.setMessage(e.getMessage());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cm.setSuccessful(false);
			cm.setMessage(e.getMessage());
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cm.setSuccessful(false);
			cm.setMessage(e.getMessage());
		}
		if(ss==null){
			cm.setSuccessful(false);
			cm.setMessage("执行校验"+sql+"失败");
		}else if(ss.equals("0")){
				cm.setSuccessful(true);
				cm.setMessage("校验成功");
		}else{
			cm.setSuccessful(false);
			cm.setMessage("有"+ss+"条数据校验失败");
		}
		return cm;
	}

}
