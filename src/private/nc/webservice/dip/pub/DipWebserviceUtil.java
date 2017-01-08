package nc.webservice.dip.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.sm.config.XMLToJavaObject;
import nc.bs.sm.login.AccountXMLUtil;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.sm.config.Account;
import nc.vo.sm.config.ConfigParameter;
/**
 * liyunzhe ,������Ǹ���servlet��ServletUtil���࣬��Ҫ�����м������У���õ�
 * @author Administrator
 *
 */
public class DipWebserviceUtil {
	public static String IP_SYSINIT_INICODE="DIP0001";
	public static String ROLE_RESOURCE_SYSINIT_INICODE="DIP0002";
	public static String DATASOURCE="datasource";
	public static String DIP_ROLEANDTABLE_B="dip_roleandtable_b";
	public static String DIP_ROLEANDFUNCTION_C="dip_roleandfunction_c";
	private static Map<String,BaseDAO> map=new HashMap<String,BaseDAO>();
	public  static BaseDAO getBaseDao(String datasource){
		BaseDAO  basedao;
		if(map.get(datasource)!=null){
			basedao=map.get(datasource);
		}else{
			basedao=new BaseDAO(datasource);
		}
		return basedao;
	}
	/**
	 * У������Դ�����Ƿ���Ч�����У������Դ��prop.xml�д��ڣ�����Ч������
	 * @param datasource
	 * @return
	 */
	public boolean validateDatasourde(String datasource){
		boolean flag=false;
		String strDatasources[]=getDataSource();
		if(strDatasources!=null&&strDatasources.length>0&&datasource!=null){
			for(int i=0;i<strDatasources.length;i++){
				String str=strDatasources[i];
				if(str.equals(datasource)){
					flag=true;
				}
			}
		}
		return flag;
	}
	/**
	 * �õ�ip��ַ
	 * @param request
	 * @return
	 */
//	public static String getIpAddr(HttpServletRequest request) {   
//		   String ip = request.getHeader("x-forwarded-for");   
//		   if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
//		      ip = request.getHeader("Proxy-Client-IP");   
//		    }   
//		    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
//		        ip = request.getHeader("WL-Proxy-Client-IP");   
//		   }   
//		    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
//		        ip = request.getRemoteAddr();   
//		    }   
//		   return ip;   
//		} 
	/**
	 * ����ip������ip�Ƿ��з��ʸķ�������Ȩ��
	 * @param ip
	 * @return 
	 */
	public static boolean validateIp(String ip,String datasource){
		if(ip==null||ip.trim().equals("")){
			return false;
		}
		BaseDAO dao=getBaseDao(datasource);
		boolean isEnable=getSysinitIsEnable(datasource, IP_SYSINIT_INICODE);
		boolean success=false;
		if(isEnable){//������У��ip���ܣ���ȥ�������ݿ⣬��У��dip_authorization_ip���Ƿ��и�ip������з���true�����򷵻�false
			try {
				List<Map> list=(List<Map>) dao.executeQuery(" select ip from dip_authorization_ip where nvl(dr,0)=0 and ip='"+ip+"' ", new MapListProcessor());
				if(list!=null&&list.size()>0){
						success=true;
				}
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			success=true;
		}
		return success;
	}
	
	/**
	 * ����usercodeȥ�жϣ������ɫ��������Щ��Դ
	 * @param usercode   �û�����
	 * @param datasource ����Դ
	 * @param tablecode  Ҫ���Ǹ����в�ѯ��Դ��
	 * @param resource   ��Ҫ��֤����Դ
	 * @return
	 */
	public static boolean validateResources(String usercode,String datasource,String tablecode,String resource){
		BaseDAO dao=getBaseDao(datasource);
		boolean isEnable=getSysinitIsEnable(datasource, ROLE_RESOURCE_SYSINIT_INICODE);
		boolean success=false;
		if(isEnable){//��ʾ��������ԴȨ��У��
			String sql=" select pk_roleandtable_h from dip_roleandtable_h "+
	  		           " where pk_role in " +
	  		           " (select distinct (pk_role) from sm_user_role ss "+
	                   " where ss.cuserid = (select cuserid "+
	                                  " from sm_user "+
	                                  " where user_code = '"+usercode+"'"+
	                                  " and nvl(dr, 0) = 0)"+
	                   " and nvl(ss.dr, 0) = 0)"+
	                   " and strdef_1='2' and nvl(dr, 0) = 0";//strdef_1=2 ��ʾ��servlet
		    List list=null;
			try {
				list=(List) dao.executeQuery(sql, new MapListProcessor());
				if(list!=null&&list.size()>0){
					Map map=(Map) list.get(0);
					String pk_roleandtable_h="";
					if(map!=null){
						pk_roleandtable_h=map.get("pk_roleandtable_h")==null?"":map.get("pk_roleandtable_h").toString();
						List listb=null;
						String sqlb="";
						if(DIP_ROLEANDTABLE_B.equals(tablecode)){
							 sqlb="select memorytable from "+DIP_ROLEANDTABLE_B+" where pk_roleandtable_h='"+pk_roleandtable_h+"' and memorytable='"+resource+"' and nvl(dr,0)=0";
							 
						}else if(DIP_ROLEANDFUNCTION_C.equals(tablecode)){
							sqlb=" select bb.module_prefix from nc_iufo_module bb where bb.module_pk in (select cc.module_pk from "+DIP_ROLEANDFUNCTION_C+" cc where cc.pk_roleandtable_h='"+pk_roleandtable_h+"' and nvl(cc.dr,0)=0 )"+
								 " and bb.module_prefix like '"+resource+"%' and nvl(bb.dr,0)=0 ";
						}	
						if(sqlb.trim().length()>0){
							listb=(List) dao.executeQuery(sqlb, new MapListProcessor());
							if(listb!=null&&list.size()>0){
								success=true;
							}
						}
					}
				}
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			success=true;
		}
		
		return success;
	}
	
	/**
	 * �õ����е�����
	 *
	 */
	public static Account[] getAccountDataSource(){
		XMLToJavaObject ob=new XMLToJavaObject();
		Account[] account=null;
		try {
			ConfigParameter a=(ConfigParameter) ob.getJavaObjectFromFile("./ierp/bin/account.xml");
			account=a.getAryAccounts();
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return account;
	}
	/**
	 * ��������Դ�õ����ױ���
	 * @param datasource ����Դ����
	 */
	public static String getAccountCode(String datasource){
		String accountcode=null;
		Account accountArry[]=getAccountDataSource();
		if(accountArry!=null&&accountArry.length>0){
			for(int i=0;i<accountArry.length;i++){
				Account account=accountArry[i];
				if(account!=null){
					if(account.getDataSourceName()!=null&&account.getDataSourceName().equals(datasource)){
						accountcode=account.getAccountCode();
						break;
					}
				}
			}
		}
		return accountcode;
	}
	/**
	 * У��datasource�Ƿ�Ϸ���
	 * @param datasource
	 * @return
	 */
	public static boolean validateDatasource(String datasource){
		boolean flag=false;
		String datasources[]=getDataSource();
		if(datasources!=null&&datasources.length>0){
			for(int i=0;i<datasources.length;i++){
				if(datasources[i].equals(datasource)){
					flag=true;
					break;
				}
				
			}
		}
		return flag;
	}
	
	/**
	 *  �õ�prop.xml����������Դ���ơ�
	 * @return
	 */
	public static  String[] getDataSource(){
		String datasources[]=null;
		try {
			 datasources=AccountXMLUtil.findDataSource();
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datasources;
	}
	/**
	 * ��������Դ�Ͳ������룬��ȷ�ϲ����Ƿ����ã���������Ҳ��������߳����쳣�����ᰴ������������
	 * ipУ�鴦��Ĳ���������DIP0001
	 * ��ɫ����ԴУ�鴦��Ĳ���������DIP0002
	 * @param datasource
	 * @param initcode
	 * @return
	 */
	public static boolean getSysinitIsEnable(String datasource,String initcode){
		BaseDAO dao=getBaseDao(datasource);
		boolean flag=true;
		try {
			List list=(List) dao.executeQuery("select value from pub_sysinit where initcode='"+initcode+"' and nvl(dr,0)=0 ", new MapListProcessor());
			if(list!=null&&list.size()>0){
				Map map=(Map) list.get(0);
				if(map!=null&&map.get("value")!=null&&map.get("value").toString().equals("N")){
					flag=false;
				}
			}
		} catch (DAOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return flag;
	}
	/**
	 * У���û��Ƿ����
	 * @param usercode
	 * @param datasource
	 * @return
	 */
	public static boolean validateUser(String usercode, String datasource) {
		// TODO Auto-generated method stub
		BaseDAO dao=getBaseDao(datasource);
		boolean flag=false;
		List list=null;
		try {
			list=(List) dao.executeQuery("select ss.user_name from sm_user ss where ss.user_code='"+usercode+"' and nvl(dr,0)=0  ", new MapListProcessor());
			if(list!=null&&list.size()>0){
				flag=true;
			}
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 *��У������Դ�Ƿ���ڣ��������Դ����У��ip�Ƿ�Ϸ������ip�Ϸ���У���û��Ƿ���ڣ��������ȷ�ͷ���true������ͷ��ش�����ʾ��
	 * @param datasource ����Դ
	 * @param ip         ����127.0.0.1
	 * @param usercode   �û�����
	 * @return
	 */
	public  static Object validate(String datasource,String ip,String usercode){
		Object resultObj=new Boolean(false);
		if(!DipWebserviceUtil.validateDatasource(datasource)){
			resultObj="�Ҳ�������Դ"+datasource;
		}else{
				if(DipWebserviceUtil.validateIp(ip, datasource)){//У��ip
					if(DipWebserviceUtil.validateUser(usercode,datasource)){//У���û�
						resultObj=new Boolean(true);
					}else{
						resultObj=usercode+"�û�������";
					}
				}else{
					resultObj="ip:"+ip+"û��Ȩ�޷���";
				}
		}
		
		return resultObj;
	
		
	}
	
	
	
}
