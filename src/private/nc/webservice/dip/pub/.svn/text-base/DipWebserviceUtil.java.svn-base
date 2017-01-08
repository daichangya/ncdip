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
 * liyunzhe ,这个类是复制servlet中ServletUtil的类，主要是在中间表中做校验用的
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
	 * 校验数据源名称是否有效，如果校验数据源在prop.xml中存在，就有效，否则
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
	 * 得到ip地址
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
	 * 根据ip来返回ip是否有访问改服务器的权限
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
		if(isEnable){//启用了校验ip功能，就去访问数据库，并校验dip_authorization_ip中是否有改ip，如果有返回true，否则返回false
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
	 * 根据usercode去判断，这个角色启用了那些资源
	 * @param usercode   用户编码
	 * @param datasource 数据源
	 * @param tablecode  要从那个表中查询资源，
	 * @param resource   需要验证的资源
	 * @return
	 */
	public static boolean validateResources(String usercode,String datasource,String tablecode,String resource){
		BaseDAO dao=getBaseDao(datasource);
		boolean isEnable=getSysinitIsEnable(datasource, ROLE_RESOURCE_SYSINIT_INICODE);
		boolean success=false;
		if(isEnable){//表示启用了资源权限校验
			String sql=" select pk_roleandtable_h from dip_roleandtable_h "+
	  		           " where pk_role in " +
	  		           " (select distinct (pk_role) from sm_user_role ss "+
	                   " where ss.cuserid = (select cuserid "+
	                                  " from sm_user "+
	                                  " where user_code = '"+usercode+"'"+
	                                  " and nvl(dr, 0) = 0)"+
	                   " and nvl(ss.dr, 0) = 0)"+
	                   " and strdef_1='2' and nvl(dr, 0) = 0";//strdef_1=2 表示是servlet
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
	 * 得到所有的帐套
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
	 * 根据数据源得到帐套编码
	 * @param datasource 数据源编码
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
	 * 校验datasource是否合法。
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
	 *  得到prop.xml的所有数据源名称。
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
	 * 根据数据源和参数代码，来确认参数是否启用，如果参数找不到，或者出现异常，都会按照启用来处理
	 * ip校验处理的参数代码是DIP0001
	 * 角色和资源校验处理的参数代码是DIP0002
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
	 * 校验用户是否存在
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
	 *先校验数据源是否存在，如果数据源存在校验ip是否合法，如果ip合法在校验用户是否存在，如果都正确就返回true，否则就返回错误提示。
	 * @param datasource 数据源
	 * @param ip         例如127.0.0.1
	 * @param usercode   用户编码
	 * @return
	 */
	public  static Object validate(String datasource,String ip,String usercode){
		Object resultObj=new Boolean(false);
		if(!DipWebserviceUtil.validateDatasource(datasource)){
			resultObj="找不到数据源"+datasource;
		}else{
				if(DipWebserviceUtil.validateIp(ip, datasource)){//校验ip
					if(DipWebserviceUtil.validateUser(usercode,datasource)){//校验用户
						resultObj=new Boolean(true);
					}else{
						resultObj=usercode+"用户不存在";
					}
				}else{
					resultObj="ip:"+ip+"没有权限访问";
				}
		}
		
		return resultObj;
	
		
	}
	
	
	
}
