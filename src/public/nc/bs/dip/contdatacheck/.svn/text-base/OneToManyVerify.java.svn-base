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

public class OneToManyVerify implements ICheckPlugin{

	public CheckMessage doCheck(Object... ob) {
		// TODO Auto-generated method stub
		ContDataMapUtilVO vo=(ContDataMapUtilVO) ob[0];
		String middletablename=vo.getMiddletablename();
		String checkname=vo.getCheckname();
		CheckMessage checkmsg=new CheckMessage();
		StringBuffer sb=new StringBuffer();
		HashMap map=new HashMap();
		List list2=new ArrayList();
		if(!exist(middletablename)){
			checkmsg.setSuccessful(false);
			checkmsg.setMessage("校验["+checkname+"]：失败，维护表"+middletablename+"不存在！");
			return checkmsg;
		}
		IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
		List list=null;
		String sql=" select bb.contpk from ( "+
				   "  select count(*) as length, s.contpk from "+middletablename+" s"+ 
				   "  where nvl(s.dr,0)=0 and s.extepk is not null and s.contpk is not null "+
				   "  group by s.contpk) bb "+
				   " where bb.length>1 ";
		
		
		try {
			list=iqf.queryfieldList(sql);
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					if(!("".equals(list.get(i)))){
						String sql1=" select tt.extepk from  "+middletablename +" tt "+ 
						            " where tt.contpk='"+list.get(i).toString()+"' and  nvl(tt.dr,0)=0";
						 List list1=iqf.queryfieldList(sql1);
						 if(list1!=null&&list1.size()>0){
							 for(int j=0;j<list1.size();j++){
								 if(!("".equals(list1.get(j).toString().trim()))){
									 
									 sb.append(list1.get(j).toString().trim());
									 if(j==list1.size()-1){
										 sb.append(" ->"+list.get(i).toString().trim()+";");
									 }else{
										 sb.append(",");
									 }
									 
								 }
							 }
						 }
						 String sql2=" select tt.pk from "+middletablename +" tt "+ 
				            " where tt.contpk='"+list.get(i).toString()+"' and  nvl(tt.dr,0)=0";
						 	List  list3=iqf.queryfieldList(sql2);
				         if(list3!=null&&list3.size()>0){
				        	 for(int m=0;m<list3.size();m++){
				        		 list2.add(list3.get(m));
				        	 }
				         }
						 
						 
					}
				}
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
		
		if(sb.length()>0){
			checkmsg.setSuccessful(false);
			checkmsg.setMessage("校验["+checkname+"]：不满足的有以下记录："+sb.toString());
			map.put(vo.getCheckclassname(), list2);
			checkmsg.setMap(map);
			
		}else{
			checkmsg.setSuccessful(true);
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
