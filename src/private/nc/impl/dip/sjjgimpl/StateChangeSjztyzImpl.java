package nc.impl.dip.sjjgimpl;
import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IDataProcess;
import nc.itf.dip.pub.IQueryField;
import nc.util.dip.sj.RetMessage;
public class StateChangeSjztyzImpl implements IDataProcess{

	public RetMessage dataprocess(String hpk, String sql, String tablename, String oldtablename) throws Exception {
		// TODO Auto-generated method stub
		
		IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
		String aa=iqf.queryfield(sql);//返回0或者1，如果是0表示正确，1表示错误。
		if(aa!=null){
			if(aa.equals("0")){
				return new RetMessage(true,"0");
			}else if(aa.equals("1")){
				return new RetMessage(true,"1");
			}else{
				return new RetMessage(false,"sql语句返回值为！("+aa+")");
			}
		}
			return new RetMessage(false,"sql语句返回值为null！");
		
		
	}
 
}
