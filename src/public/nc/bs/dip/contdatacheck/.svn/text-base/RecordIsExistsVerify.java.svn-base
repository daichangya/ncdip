package nc.bs.dip.contdatacheck;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.itf.dip.pub.checkplugin.ICheckPlugin;
import nc.jdbc.framework.exception.DbException;
import nc.util.dip.sj.CheckMessage;
import nc.vo.dip.contdatawh.ContDataMapUtilVO;
import nc.vo.pub.BusinessException;

public class RecordIsExistsVerify implements ICheckPlugin{
//检查对照维护表中的的对照系统和被对照系统记录是否存在（在对照表和被对照表）。
	public CheckMessage doCheck(Object... ob) {
		// TODO Auto-generated method stub
		ContDataMapUtilVO vo=(ContDataMapUtilVO) ob[0];
		String middletablename=vo.getMiddletablename();
		String contabname=vo.getContabname();
		String contcolname=vo.getContcolname();
		String extetabname=vo.getExtetabname();
		String extecolname=vo.getExtecolname();
		String checkname=vo.getCheckname();
		ArrayList middletablekey=new ArrayList();
		HashMap map=new HashMap();
		CheckMessage checkmsg=new CheckMessage();
		
		if(!exist(middletablename)){
			checkmsg.setSuccessful(false);
			checkmsg.setMessage("校验["+checkname+"]:失败，维护表"+middletablename+"不存在！");
			return checkmsg;
		}
		
		if(!exist(contabname)){
			checkmsg.setSuccessful(false);
			checkmsg.setMessage("校验["+checkname+"]:失败，对照表"+middletablename+"不存在！");
			return checkmsg;
		}
		
		if(!exist(extetabname)){
			checkmsg.setSuccessful(false);
			checkmsg.setMessage("校验["+checkname+"]:失败，被对照表"+middletablename+"不存在！");
			return checkmsg;
		}
		
		
		String sql="select contpk  from  "+middletablename +" where   contpk is not null and extepk is not null and nvl(dr,0) =0 ";
		String sql1="select extepk  from  "+middletablename +" where  contpk is not null and extepk is not null and nvl(dr,0) =0";
		StringBuffer err=new StringBuffer();
		IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
		List list=null;
		try {
			//校验对照系统是否存在中间表的记录。
			list=iqf.queryfieldList(sql);
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					String value=list.get(i).toString().trim();
					String ss="select count(*) from " +contabname+" where  "+contcolname+" ='"+value+"' and  nvl(dr,0)=0";
					String length=iqf.queryfield(ss);
					if(length!=null&&"0".equals(length)){
						if(err.length()==0){
							err.append(contabname+"中没有"+contcolname+"='"+value+"'");
						}else{
							err.append(",'"+value+"'");
						}
						
						String sql3="select pk from "+middletablename+" where nvl(dr,0)=0 and contpk='"+value+"'";
						List contpkisnull=iqf.queryfieldList(sql3);
						if(contpkisnull!=null&&contpkisnull.size()>0){
							for(int j=0;j<contpkisnull.size();j++){
							  middletablekey.add(contpkisnull.get(j));	
							}
						}
					}else{					
							continue;						
					}
					
					
					
				}
				if(err.length()>0){
					err.append("的记录！");
				}
			}
			
			//校验被对照系统是否存在中间表的记录。
			list=iqf.queryfieldList(sql1);
			if(list!=null&&list.size()>0){
				int kk=0;
				for(int i=0;i<list.size();i++){
					String value=list.get(i).toString().trim();
					String ss="select count(*) from " +extetabname+" where  "+extecolname+" ='"+value+"' and  nvl(dr,0)=0";
					String length=iqf.queryfield(ss);
					
					if(length!=null&&"0".equals(length)){
						if(err.length()==0||err.length()-1==err.indexOf("！")){
							err.append(extetabname+"中没有"+extecolname+"='"+value+"'");	
							kk=1;
						}else{
							err.append(",'"+value+"'");
						}
						
						String sql4="select pk from "+middletablename+" where nvl(dr,0)=0 and extepk='"+value+"'";
						List extepkisnull=iqf.queryfieldList(sql4);
						  if(extepkisnull!=null&&extepkisnull.size()>0){
							  for(int j=0;j<extepkisnull.size();j++){
								  middletablekey.add(extepkisnull.get(j));
							  }
						  }
						
					}else{					
							continue;						
					}
					
					
				}
				if(kk==1){
					err.append("的记录！");
				}
			}
			
			if(err.length()>0){
				checkmsg.setSuccessful(false);
				checkmsg.setMessage("校验["+checkname+"]:"+err.toString());
				map.put(vo.getCheckclassname(), middletablekey);
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
