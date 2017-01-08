package nc.servlet.dip.server.pub;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.dao.BaseDAO;
//import nc.impl.wbxtprj.pub.QueryFieldImpl;mport nc.itf.wbxtprj.pub.IQueryField;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.BusinessException;


@SuppressWarnings("serial")
public class SynCountServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		service(arg0, arg1);
	}

	@Override
	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		service(arg0, arg1);
	}
	public String getIpAddr(HttpServletRequest request) {   
		   String ip = request.getHeader("x-forwarded-for");   
		   if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
		      ip = request.getHeader("Proxy-Client-IP");   
		    }   
		    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
		        ip = request.getHeader("WL-Proxy-Client-IP");   
		   }   
		    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
		        ip = request.getRemoteAddr();   
		    }   
		   return ip;   
		} 
	@Override
	protected void service(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		// 接收传递过来的信息
//		IQueryField query;
		String classname = request.getParameter("classname");
		
		int count = 0;
		boolean issucc=true;
		String msg="";
		if(null==classname||"".equals(classname)){
			issucc=false;
			msg="传过的表名为空";
		}else{
			InputStream inputStream=getServletContext().getResourceAsStream("/WEB-INF/wbxtprj.properties");
			Properties properties=new Properties();
			properties.load(inputStream);
			String dataSource=(String)properties.get("dataSource");
			String ips=(String)properties.get("authip");
			String ip=getIpAddr(request);
			if(ips.indexOf("["+ip+"]")>=0){
				String sql="select count(*) count from "+classname;
				//一次获取全部数据
				IUAPQueryBS ibs=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				List<Map> list = null;
				try {
					list = (List<Map>)new BaseDAO(dataSource).executeQuery(sql, new MapListProcessor());
					
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					issucc=false;
					msg="servlet服务端查询数据库错误"+e.getMessage();
				}
				if(null!=list&&list.size()>0){
					Map map=list.get(0);
					count = Integer.parseInt(map.get("count").toString());
				}
				
			}else{
				issucc=false;
				msg="ip不允许访问"+ip;
			}
		}
		ObjectOutputStream oos = new ObjectOutputStream(response
				.getOutputStream());
		oos.writeObject(issucc?count:msg);
		oos.flush();
	}
}
